package neko.utils;

import java.util.HashMap;
import java.util.Map;

public class generalMap {

    public static Map<String, String> getSuccessMap() {
        Map<String, String> map = new HashMap<>();
        map.put("state", "200");
        map.put("msg", "success");
        map.put("data", "");
        return map;
    }

    public static Map<String, String> getErrorMap() {
        Map<String, String> map = new HashMap<>();
        map.put("state", "400");
        map.put("msg", "error");
        map.put("data", "");
        return map;
    }
}
