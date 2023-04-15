package com.unfbx.chatgptsteamoutput.config;

public class PkUtil {

    private static IdWorker worker = new IdWorker(1, 1, 1);

    public static Long getId() {
        long id = worker.nextId();
        return Long.valueOf(String.valueOf(id).substring(3));
    }

    public static String getBusiness(String pre) {
        long id = worker.nextId();
        Long sub = Long.valueOf(String.valueOf(id).substring(3));
        return pre+sub;
    }
}