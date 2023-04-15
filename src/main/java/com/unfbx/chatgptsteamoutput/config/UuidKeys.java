package com.unfbx.chatgptsteamoutput.config;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class UuidKeys {
    public static void main(String[] args) {
        List<String> list =new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            UUID uuid = UUID.randomUUID();
            String str = uuid.toString().replace("-","");
            String keys= "sk-fp0Kv28dEJ"+str+"@aichatgptshopping";
            System.out.println(keys);
            list.add(keys);
        }
        String collect = list.stream().collect(Collectors.joining(","));
        System.out.println(collect);
    }
  public   static String  getShoppingKey(){
        UUID uuid = UUID.randomUUID();
        String str = uuid.toString().replace("-","");
        String keys= "sk-fp0Kv28dEJ"+str+"@aichatgptshopping";
        System.out.println(keys);
        return keys;
    }
}
