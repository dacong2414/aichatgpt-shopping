package com.unfbx.chatgptsteamoutput.websocket;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unfbx.chatgpt.OpenAiStreamClient;
import com.unfbx.chatgpt.entity.chat.Message;
import com.unfbx.chatgpt.exception.BaseException;
import com.unfbx.chatgpt.exception.CommonError;
import com.unfbx.chatgptsteamoutput.config.LocalCache;
import com.unfbx.chatgptsteamoutput.config.RateLimiter;
import com.unfbx.chatgptsteamoutput.entity.Login;
import com.unfbx.chatgptsteamoutput.listener.OpenAISSEEventSourceListener;
import com.unfbx.chatgptsteamoutput.listener.OpenAIWebSocketEventSourceListener;
import com.unfbx.chatgptsteamoutput.seviceImpl.LoginServiceImpl;
import com.unfbx.chatgptsteamoutput.seviceImpl.OpenaiKeyServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
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


    private static List<String> shoppingKey;

    private static OpenAiStreamClient openAiStreamClient;

    private static LoginServiceImpl loginService;

    private static HttpSession httpSession;


    @Autowired
    public void setOrderService(OpenAiStreamClient openAiStreamClient) {
        this.openAiStreamClient = openAiStreamClient;
    }

    @Value("${chatgpt.shopping}")
    public void setShoppingKey(List<String> shoppingKey) {
        this.shoppingKey = shoppingKey;
    }

    @Autowired
    public void setLoginService(LoginServiceImpl loginService) {
        this.loginService = loginService;
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


    public static ConcurrentHashMap<String, Integer> limitCount = new ConcurrentHashMap();

    public static ConcurrentHashMap<String, String> keyUserMap = new ConcurrentHashMap();

    private static List<String> word = Arrays.asList("嫖娼", "赌博", "毒品", "黄色", "宗教", "共产党", "博彩", "强奸", "政治", "卖淫", "犯罪", "凶杀", "教唆犯罪");

    RateLimiter rateLimiter = new RateLimiter(1000, 3); // 每秒最多处理3个请求

    /**
     * 建立连接
     *
     * @param session
     * @param uid
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("uid") String uid, EndpointConfig config) {
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
        //httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());

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

        if (rateLimiter.allowRequest()) {
            log.info("处理请求");
        } else {
            log.info("请求被限流");
            sendMsg("{\"content\": \"AI很忙，稍后在试试......\"}");
        }

        //有不当词汇
        boolean b = word.stream().anyMatch(x -> msg != null && msg.contains(x));
        if (b) {
            sendMsg("{\"content\": \"AI很忙,请文明发言，不能讨论敏感话题.....\"}");
            return;
        }
        if (msg != null) {
            String trim = msg.trim();
            if (trim.contains("sk-fp0Kv28dEJ")) {
                Login login = loginService.getLoginBykey(trim);
                if (ObjectUtil.isNotEmpty(login) && login.getEnableTime() > 0) {
                    String valueKeys = keyUserMap.get(this.uid);
                    if (valueKeys == null || valueKeys.equals("")) {
                        String oldKey = "";
                        for (Map.Entry<String, String> stringUserEntry : keyUserMap.entrySet()) {
                            if (stringUserEntry.getValue() != null && stringUserEntry.getValue().equals(trim)) {
                                oldKey = stringUserEntry.getKey();
                            }
                        }
                        if (!oldKey.equals("")) {
                            keyUserMap.remove(oldKey);
                        }
                        sendMsg("{\"content\": \"AI很忙,恭喜你，你的key已启用！！！\"}");
                        keyUserMap.put(this.uid, trim);
                        return;
                    }else{
                        sendMsg("{\"content\": \"AI很忙,恭喜你，你的key已启用！！！\"}");
                        return;
                    }
                }
            }
        }
        String valueKeys = keyUserMap.get(this.uid);
        if (valueKeys == null) {
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

    private void sendMsg(String s) {
        try {
            session.getBasicRemote().sendText(s);
        } catch (Exception e) {
            log.error("发送异常", e);
        }
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
        if (count > 200) {
            try {
                session.getBasicRemote().sendText("{\"content\": \"AI很忙,抱歉你访问次数超过200次，请联系管理员：微信号：dacong-sd-gpt \"}");
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

