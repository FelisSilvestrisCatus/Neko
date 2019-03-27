package neko.utils;

import java.util.HashMap;
import java.util.Map;

public class generalMap {

    public static Map<String, String> getMap() {
        Map<String, String> map = new HashMap<>();
        map.put("state", "400");
        map.put("msg", "error");
        map.put("data", "");
        return map;
    }
}
