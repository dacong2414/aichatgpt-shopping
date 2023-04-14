package com.unfbx.chatgptsteamoutput.websocket;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unfbx.chatgpt.OpenAiStreamClient;
import com.unfbx.chatgpt.entity.chat.Message;
import com.unfbx.chatgpt.exception.BaseException;
import com.unfbx.chatgpt.exception.CommonError;
import com.unfbx.chatgptsteamoutput.config.LocalCache;
import com.unfbx.chatgptsteamoutput.listener.OpenAISSEEventSourceListener;
import com.unfbx.chatgptsteamoutput.listener.OpenAIWebSocketEventSourceListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 描述：websocket 服务端
 *
 * @author https:www.unfbx.com
 * @date 2023-03-23
 */
@Slf4j
@Component
@ServerEndpoint("/websocket/{uid}")
public class WebSocketServer {

    private static OpenAiStreamClient openAiStreamClient;

    @Autowired
    public void setOrderService(OpenAiStreamClient openAiStreamClient) {
        this.openAiStreamClient = openAiStreamClient;
    }

    //在线总数
    private static int onlineCount;
    //当前会话
    private Session session;
    //用户id -目前是按浏览器随机生成
    private String uid;

    private static CopyOnWriteArraySet<WebSocketServer> webSocketSet = new CopyOnWriteArraySet<>();

    /**
     * 用来存放每个客户端对应的WebSocketServer对象
     */
    private static ConcurrentHashMap<String, WebSocketServer> webSocketMap = new ConcurrentHashMap();

    /**
     * 为了保存在线用户信息，在方法中新建一个list存储一下【实际项目依据复杂度，可以存储到数据库或者缓存】
     */
    private final static List<Session> SESSIONS = Collections.synchronizedList(new ArrayList<>());


    private static ConcurrentHashMap<String, Integer> limitCount = new ConcurrentHashMap();

    private static ConcurrentHashMap<String, String> keyUserMap = new ConcurrentHashMap();

    private static List<String> word=Arrays.asList("嫖娼","赌博","毒品","黄色","宗教","共产党","中国","博彩");
    @Value("${chatgpt.shopping}")
    private List<String> shoppingKey;
    /**
     * 建立连接
     *
     * @param session
     * @param uid
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("uid") String uid) {
        this.session = session;
        this.uid = uid;
        webSocketSet.add(this);
        SESSIONS.add(session);
        if (webSocketMap.containsKey(uid)) {
            webSocketMap.remove(uid);
            webSocketMap.put(uid, this);
        } else {
            webSocketMap.put(uid, this);
            addOnlineCount();
        }
        log.info("[连接ID:{}] 建立连接, 当前连接数:{}", this.uid, getOnlineCount());
    }

    /**
     * 断开连接
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);
        if (webSocketMap.containsKey(uid)) {
            webSocketMap.remove(uid);
            subOnlineCount();
        }
        log.info("[连接ID:{}] 断开连接, 当前连接数:{}", uid, getOnlineCount());
    }

    /**
     * 发送错误
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.info("[连接ID:{}] 错误原因:{}", this.uid, error.getMessage());
        error.printStackTrace();
    }

    /**
     * 接收到客户端消息
     *
     * @param msg
     */
    @OnMessage
    public void onMessage(String msg) {
        //有不当词汇
        boolean b = word.stream().anyMatch(x -> msg != null && msg.contains(x));
        if (b){
            return;
        }
        if (msg!=null){
            String trim = msg.trim();
            if (trim.contains("sk-fp0Kv28dEJFppR3NellmT3BlbkFJn4Z2v8SJoQFWp")){
                String valueKeys = keyUserMap.get(this.uid);
                if (valueKeys==null||valueKeys.equals("")){
                    keyUserMap.put(this.uid,trim);
                }
            }
        }
        String valueKeys = keyUserMap.get(this.uid);
        if (valueKeys==null){
            if (checkFreeUser()) {
                return;
            }
        }
        log.info("[连接ID:{}] 收到消息:{}", this.uid, msg);
        //接受参数
        OpenAIWebSocketEventSourceListener eventSourceListener = new OpenAIWebSocketEventSourceListener(this.session);
        String messageContext = (String) LocalCache.CACHE.get(uid);
        List<Message> messages = new ArrayList<>();
        if (StrUtil.isNotBlank(messageContext)) {
            messages = JSONUtil.toList(messageContext, Message.class);
            if (messages.size() >= 10) {
                messages = messages.subList(1, 10);
            }
            Message currentMessage = Message.builder().content(msg).role(Message.Role.USER).build();
            messages.add(currentMessage);
        } else {
            Message currentMessage = Message.builder().content(msg).role(Message.Role.USER).build();
            messages.add(currentMessage);
        }
        openAiStreamClient.streamChatCompletion(messages, eventSourceListener);
        LocalCache.CACHE.put(uid, JSONUtil.toJsonStr(messages), LocalCache.TIMEOUT);
    }

    private boolean checkFreeUser() {
        Integer count = limitCount.get(this.uid);
        if (count == null) {
            count = 1;
            limitCount.put(this.uid, count);
        } else {
            count = count + 1;
            limitCount.put(this.uid, count);
        }
        if (count >10) {
            try {
                session.getBasicRemote().sendText("{\"content\": \"抱歉，免费额度已用完\"}");
                return true;
            } catch (Exception e) {
                log.error("发送异常", e);
            }
        }
        return false;
    }


    /**
     * 获取当前连接数
     *
     * @return
     */
    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    /**
     * 当前连接数加一
     */
    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    /**
     * 当前连接数减一
     */
    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }

}

