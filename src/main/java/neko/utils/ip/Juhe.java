package neko.utils.ip;

//import net.sf.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import neko.utils.redis.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class Juhe {

    @Autowired
    private RedisUtil redisUtil;
    //Redis过期时间
    private static final long tokenExpire = 6;
    //Redis过期时间单位
    private static final TimeUnit tokenExpireTimeUnit = TimeUnit.HOURS;

    public static final String DEF_CHATSET = "UTF-8";
    public static final int DEF_CONN_TIMEOUT = 30000;
    public static final int DEF_READ_TIMEOUT = 30000;
    public static String userAgent = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36";

    public String getValueOnlyCity(String ip) {
        String area = "";
        try {
            JSONObject jsonObject = getRequest(ip);
            assert jsonObject != null;
            area = jsonObject.get("City") + "";
        } catch (Exception e) {
            area = "未知地址";
        }
        return area;
    }

    public String getValue(String ip) {
        String area = "";
        try {
            JSONObject jsonObject = getRequest(ip);
            assert jsonObject != null;
            area = jsonObject.getString("Country") + jsonObject.get("Province") + jsonObject.get("City") + jsonObject.get("Isp");
        } catch (Exception e) {
            area = "未知地址";
        }
        return area;
    }

    public static JSONObject getRequest(String ip) {
        String result = null;
        String url = "http://apis.juhe.cn/ip/ipNew?ip=" + ip + "&key=f187e4af4790ce0d50f7c649f7fd24d0";//请求接口地址

        try {
            result = net(url, "GET");
            JSONObject object = JSONObject.parseObject(result);
            if (object.getIntValue("error_code") == 0) {
                return JSONObject.parseObject(object.getString("result"));
            } else {
                System.out.println(object.get("error_code") + ":" + object.get("reason"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * @param strUrl 请求地址
     * @param method 请求方法
     * @return 网络请求字符串
     * @throws Exception
     */
    public static String net(String strUrl, String method) throws Exception {
        HttpURLConnection conn = null;
        BufferedReader reader = null;
        String rs = null;
        try {
            StringBuffer sb = new StringBuffer();
            URL url = new URL(strUrl);
            conn = (HttpURLConnection) url.openConnection();
            if (method == null || method.equals("GET")) {
                conn.setRequestMethod("GET");
            } else {
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
            }
            conn.setRequestProperty("User-agent", userAgent);
            conn.setUseCaches(false);
            conn.setConnectTimeout(DEF_CONN_TIMEOUT);
            conn.setReadTimeout(DEF_READ_TIMEOUT);
            conn.setInstanceFollowRedirects(false);
            conn.connect();

            InputStream is = conn.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, DEF_CHATSET));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sb.append(strRead);
            }
            rs = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
        return rs;
    }

    //将map型转为请求参数型
    public static String urlencode(Map<String, Object> data) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry i : data.entrySet()) {
            try {
                sb.append(i.getKey()).append("=").append(URLEncoder.encode(i.getValue() + "", "UTF-8")).append("&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    //获取天气
    public JSONObject getWeather(String ip) throws IOException {
        JSONObject weather = new JSONObject();
        String key = "f5e272f5f52ffb93a0902474025efb16";
        String city = getValueOnlyCity(ip).replace("市", "");
        if (city.contains("内网") || city.contains("未知")) {
            city = "青岛";
        }
        System.out.println("city = " + city);

        //检查redis中是否有缓存
        if (redisUtil.hasKey(city)) {
            weather = JSON.parseObject(redisUtil.get(city));
            System.out.println("weather = " + weather);
            return weather;
        }

        URL u = new URL("http://apis.juhe.cn/simpleWeather/query?city=" + city + "&key=" + key);
        InputStream in = u.openStream();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            byte[] buf = new byte[1024];
            int read = 0;
            while ((read = in.read(buf)) > 0) {
                out.write(buf, 0, read);
            }

            byte[] b = out.toByteArray();
            String result = new String(b, "UTF-8");
            JSONObject json = JSONObject.parseObject(result);
            weather = json.getJSONObject("result").getJSONObject("realtime");
            weather.remove("wid");
            weather.remove("aqi");
            redisUtil.set(city, JSON.toJSONString(weather));
            redisUtil.expire(city, tokenExpire, tokenExpireTimeUnit);
        } catch (Exception e) {
            weather = JSON.parseObject("{\"temperature\":\"未知\",\"direct\":\"未知\",\"humidity\":\"未知\",\"power\":\"未知\",\"info\":\"未知\"}");
        } finally {
            if (in != null) {
                in.close();
            }
        }


        return weather;
    }
}