package com.unfbx.chatgptsteamoutput.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Slf4j
public class BeanUtil {

    public static <T> T conversion(Object o, Class<T> c) {
        if (!(o instanceof String)) {
            o = JSON.toJSONString(o);
        }
        return JSONObject.parseObject((String) o, c);
    }

    public static <T> T conversion(Object o, TypeReference<T> tTypeReference) {
        if (!(o instanceof String)) {
            o = JSON.toJSONString(o);
        }
        return JSONObject.parseObject((String) o, tTypeReference);
    }

    /**
     * list拷贝
     *
     * @param fromList 拷贝前的值
     * @param toClass  拷贝类型
     * @return 拷贝后的值
     */
    public static <T> List<T> conversionList(List fromList, Class<T> toClass) {
        if (CollectionUtils.isEmpty(fromList)) {
            return new ArrayList<>();
        }
        try {
            List toList = new ArrayList();
            Object tempObj;
            for (Object aFromList : fromList) {
                tempObj = toClass.newInstance();
                BeanUtils.copyProperties(aFromList, tempObj, toClass);
                toList.add(tempObj);
            }
            return toList;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @param to   合并目标
     * @param from 合并源
     * @param <M>
     * @throws Exception
     */
    public static <M> void merge(M from, M to) {
        try {
            //获取目标bean
            BeanInfo beanInfo = Introspector.getBeanInfo(to.getClass());
            // 遍历所有属性
            for (PropertyDescriptor descriptor : beanInfo.getPropertyDescriptors()) {
                // 如果是可写属性
                if (descriptor.getWriteMethod() != null) {
                    Object defaultValue = descriptor.getReadMethod().invoke(from);
                    //可以使用StringUtil.isNotEmpty(defaultValue)来判断
                    if (defaultValue != null && !"".equals(defaultValue)) {
                        //用非空的defaultValue值覆盖到target去
                        descriptor.getWriteMethod().invoke(to, defaultValue);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将targetBean合并到 sourceBean
     *
     * @param sourceBean 合并后
     * @param targetBean 被合并
     * @param c          类
     * @param <T>        返回对象
     * @return
     */
    public static <T> T combine(T sourceBean, Object targetBean, Class<T> c) {
        JSONObject sourceObj = JSONObject.parseObject(JSON.toJSONString(sourceBean));
        JSONObject targetObj = JSONObject.parseObject(JSON.toJSONString(targetBean));
        Set<String> strings = targetObj.keySet();
        for (String str : strings) {
            if (!sourceObj.containsKey(str)) {
                sourceObj.put(str, targetObj.get(str));
            }
        }
        return conversion(sourceObj, c);
    }


}
