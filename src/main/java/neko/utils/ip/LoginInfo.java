package neko.utils.ip;


//import net.sf.json.JSONObject;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;

@Component
public class LoginInfo {


    /**
     * @parameter:HttpServletRequest request
     * @return:ip 实现获取登录ip地址 以及 尝试使用dns域名解析
     */
    public String getIpAddr(HttpServletRequest request) {
        //获取用户客户端真实ip地址
        //x-forwarded-for 记录正向代理的真实浏览器地址以及之间的代理服务器地址
        String ip = request.getHeader("x-forwarded-for");
        String getRemoteIp = "";
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            //X-Real-IP 记录一次真实的浏览器地址
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Forwarded-For");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            //判断是不是本地ip
            if (ip.equals("127.0.0.1") || ip.equals("0:0:0:0:0:0:0:1")) {
                //根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                ip = inet.getHostAddress();
            }
        }
        if (ip != null && ip.length() > 15) { //"***.***.***.***".length() = 15  
            if (ip.indexOf(",") > 0) {
                ip = ip.split(",")[ip.split(",").length - 1];
                System.out.println("通过x-forwarded-for记录的正向代理ip中离目标服务器最近的ip地址" + ip);
                //为了减少ip篡改的可能性 比较一下离目标最近的地址ip
                if (!ip.equalsIgnoreCase(request.getRemoteAddr())) {
                    System.out.println("x-forwarded-for被篡改或者服务器不支持可能性很大 但不影响ip的测定");
                }
            }
            //如果最后服务器之间没有x-forwarded-for规范 则以getRemoteAddr为准
            return ip;
        }

        return ip;
    }


    /**
     * @param urlStr  请求的地址
     * @param content 请求的参数 格式为：ip=""
     * @return
     */
    private String getResult(String urlStr, String content) {
        System.out.println("要查询的ip" + content);
        URL url = null;
        HttpURLConnection connection = null;
        try {
            System.out.println("网站接口" + urlStr + "?ip=" + content);
            url = new URL(urlStr + "?ip=" + content);
            connection = (HttpURLConnection) url.openConnection();// 新建连接实例
            connection.setConnectTimeout(500);// 设置连接超时时间，单位毫秒
            connection.setReadTimeout(500);// 设置读取数据超时时间，单位毫秒
            connection.setDoOutput(true);// 是否打开输出流 true|false

            connection.setRequestMethod("GET");// 提交方法POST|GET
            connection.setUseCaches(false);// 是否缓存true|false
            connection.connect();// 打开连接端口
            DataOutputStream out = new DataOutputStream(connection
                    .getOutputStream());// 打开输出流往对端服务器写数据

            out.flush();// 刷新
            out.close();// 关闭输出流
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    connection.getInputStream(), "utf-8"));// 往对端写完数据对端服务器返回数据
            // ,以BufferedReader流来读取
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = reader.readLine()) != null) {
                buffer.append(line);

            }
            reader.close();

            return buffer.toString();
        } catch (Exception e) {

        } finally {
            if (connection != null) {
                connection.disconnect();// 关闭连接
            }
        }
        return null;
    }

    /**
     * unicode 转换成 中文
     *
     * @param theString
     * @return
     * @author fanhui 2007-3-15
     */
    public String decodeUnicode(String theString) {
        char aChar;
        int len = theString.length();
        StringBuffer outBuffer = new StringBuffer(len);
        for (int x = 0; x < len; ) {
            aChar = theString.charAt(x++);
            if (aChar == '\\') {
                aChar = theString.charAt(x++);
                if (aChar == 'u') {
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = theString.charAt(x++);
                        switch (aChar) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + aChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + aChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + aChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException(
                                        "Malformed      encoding.");
                        }
                    }
                    outBuffer.append((char) value);
                } else {
                    if (aChar == 't') {
                        aChar = '\t';
                    } else if (aChar == 'r') {
                        aChar = '\r';
                    } else if (aChar == 'n') {
                        aChar = '\n';
                    } else if (aChar == 'f') {
                        aChar = '\f';
                    }
                    outBuffer.append(aChar);
                }
            } else {
                outBuffer.append(aChar);
            }
        }
        return outBuffer.toString();
    }


    /**
     * @param :content =ip
     * @return :iplocation      根据登录ip获取归属地
     */
    public String getIpLocation(String content) {

        if (content.contains("192.168"))
            return "局域网地址";
        // 这里调用淘宝API
        String urlStr = "http://ip.taobao.com/service/getIpInfo.php";
        String returnStr = null;
        try {
            returnStr = getResult(urlStr, content);
        } catch (Exception e) {
            return "未知地址";
        }
        if (returnStr != null) {
            // 处理返回的省市区信息

            returnStr = decodeUnicode(returnStr);

            String[] temp = returnStr.split(",");
            if (temp.length < 3) {
                return "0";//无效IP，局域网测试
            } else {
                //解析json数据 生成指定数据呈现样式
                JSONObject json = JSONObject.parseObject(returnStr);
                System.out.println("json数据： " + json);
                String country = JSONObject.parseObject(json.getString("data")).get("country").toString();
                String region = JSONObject.parseObject(json.getString("data")).get("region").toString();
                String city = JSONObject.parseObject(json.getString("data")).get("city").toString();
                String county = JSONObject.parseObject(json.getString("data")).get("county").toString();
                String isp = JSONObject.parseObject(json.getString("data")).get("isp").toString();
                String area = JSONObject.parseObject(json.getString("data")).get("area").toString();
                String address;
                address = country + region + city + county + isp;
                return address;
            }

        }
        return "未知地址";
    }


}
