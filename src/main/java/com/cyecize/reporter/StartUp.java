package com.cyecize.reporter;

import com.cyecize.javache.JavacheConfigValue;
import com.cyecize.javache.embedded.JavacheEmbedded;

import java.util.HashMap;
import java.util.Map;

public class StartUp {

    public static void main(String[] args) {
        System.err.close();
        System.setErr(System.out);

        Map<String, Object> config = new HashMap<>() {{
            put(JavacheConfigValue.BROCCOLINA_TRACK_RESOURCES.name(), false);
        }};

        JavacheEmbedded.startServer(8000, config, StartUp.class);
    }
}
