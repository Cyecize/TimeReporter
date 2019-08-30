package com.cyecize.reporter;

import com.cyecize.javache.ConfigConstants;
import com.cyecize.javache.embedded.JavacheEmbedded;
import com.cyecize.toyote.ResourceHandler;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class StartUp {

    public static boolean isAppStartedFromEmbeddedServer = false;

    public static void main(String[] args) {
        isAppStartedFromEmbeddedServer = true;
        System.err.close();
        System.setErr(System.out);

        Map<String, Object> config = new HashMap<>() {{
            put(ConfigConstants.BROCCOLINA_TRACK_RESOURCES, false);
            put(ConfigConstants.TOYOTE_MAX_NUMBER_OF_CACHED_FILES, -1);
            put(ConfigConstants.TOYOYE_CACHED_FILE_MAX_SIZE, -1);
        }};

        JavacheEmbedded.startServer(8000, config, StartUp.class, StartUp::changeToyoteWorkingDir);
    }

    /**
     * Makes the resource handler look into the resources folder of the project so that whenever we edit a front end file it will be reflected.
     * <p>
     * Tested on IntelliJ only!
     */
    private static void changeToyoteWorkingDir() {
        ResourceHandler requestHandler = (ResourceHandler) JavacheEmbedded.requestHandlerLoadingService.getRequestHandlers().get(1);

        try {
            Field workingDirField = ResourceHandler.class.getDeclaredField("webappsDirName");
            Field mainAppName = ResourceHandler.class.getDeclaredField("mainAppName");

            workingDirField.setAccessible(true);
            mainAppName.setAccessible(true);

            workingDirField.set(requestHandler, "../../src/main/");
            mainAppName.set(requestHandler, "resources");

        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
