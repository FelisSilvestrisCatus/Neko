package neko.utils.ip;

//import net.sf.json.JSONObject;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

@Component
public class Juhe {
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
        String key = "f5e272f5f52ffb93a0902474025efb16";
        String city = getValueOnlyCity(ip);
        if (city.contains("内网") || city.contains("未知")) {
            city = "北京";

        }
        System.out.println(city);
        URL u = new URL("http://apis.juhe.cn/simpleWeather/query?city=" + city + "&key=" + key);

        InputStream in = u.openStream();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            byte buf[] = new byte[1024];
            int read = 0;
            while ((read = in.read(buf)) > 0) {
                out.write(buf, 0, read);
            }
        } finally {
            if (in != null) {
                in.close();
            }
        }
        byte b[] = out.toByteArray();
        String result = new String(b, "utf-8");

        //可以使用fastjson解析

        JSONObject json = JSONObject.parseObject(result);

        System.out.println(json);
        String now = JSONObject.parseObject(json.getString("result")).getString("realtime");
        JSONObject now_ = JSONObject.parseObject(now);
        now_.remove("wid");
        now_.remove("aqi");

        return now_;
    }
}