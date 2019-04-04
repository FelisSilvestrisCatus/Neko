package neko.utils;

import neko.entity.Users;

import java.util.HashMap;
import java.util.Map;

public class generalMethod {

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

    public static boolean isStudent(Users u) {
        return u.getType() == 2;
    }

    public static boolean isTeacher(Users u) {
        return u.getType() == 1;
    }
}
