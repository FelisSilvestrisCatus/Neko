package neko.utils;







import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.*;
import net.sf.json.JSONObject;

public class LoginRequestHeaderMessage {


    /**
     * @parameter:HttpServletRequest request
     * @return:ip
     * 实现获取登录ip地址 以及 尝试使用dns域名解析
     *
     */
    public String getIpAddr(HttpServletRequest request) {
        // 取用户客户端真实ip地址
        //x-forwarded-for 记录正向代理的真实浏览器地址以及之间的代理服务器地址
        String ip = request.getHeader("x-forwarded-for");
        String getRemoteIp="";
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            //X-Real-IP 记录一次真实的浏览器地址
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Forwarded-For");
        }

        //　X-Forwarded-For  WL-Proxy-Client-IP都是apache的请求头
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
                ip = ip.split(",")[ ip.split(",").length-1];
                System.out.println("通过x-forwarded-for记录的正向代理ip中离目标服务器最近的ip地址"+ ip );
                //为了减少ip篡改的可能性 比较一下离目标最近的地址ip
                if(!ip.equalsIgnoreCase(request.getRemoteAddr())){
                    System.out.println("x-forwarded-for被篡改或者服务器不支持可能性很大 但不影响ip的测定");

                }

        }


            //如果最后服务器之间没有x-forwarded-for规范 则以getRemoteAddr为准
            return ip;
        }

        return "ip无法获取(服务器可不允许获取ip)";
    }


    /**
     * @param urlStr
     *            请求的地址
     * @param content
     *            请求的参数 格式为：ip=""
     * @param encoding
     *            服务器端请求编码。如GBK,UTF-8等
     * @return
     */
    private  String getResult(String urlStr, String content, String encoding) throws IOException {
        URL url = null;
        HttpURLConnection connection = null;
        try {
            url = new URL(urlStr);
            connection = (HttpURLConnection) url.openConnection();// 新建连接实例
            connection.setConnectTimeout(2000);// 设置连接超时时间，单位毫秒
            connection.setReadTimeout(2000);// 设置读取数据超时时间，单位毫秒
            connection.setDoOutput(true);// 是否打开输出流 true|false
            connection.setDoInput(true);// 是否打开输入流true|false
            connection.setRequestMethod("POST");// 提交方法POST|GET
            connection.setUseCaches(false);// 是否缓存true|false
            connection.connect();// 打开连接端口
            DataOutputStream out = new DataOutputStream(connection
                    .getOutputStream());// 打开输出流往对端服务器写数据
            out.writeBytes(content);// 写数据,也就是提交你的表单 name=xxx&pwd=xxx
            out.flush();// 刷新
            out.close();// 关闭输出流
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    connection.getInputStream(), encoding));// 往对端写完数据对端服务器返回数据
            // ,以BufferedReader流来读取
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            reader.close();
            return buffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
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
     * @author fanhui 2007-3-15
     * @param theString
     * @return
     */
    public String decodeUnicode(String theString) {
        char aChar;
        int len = theString.length();
        StringBuffer outBuffer = new StringBuffer(len);
        for (int x = 0; x < len;) {
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
     * @param :content ="ip"+ip
     * @param :cencodingString ="utf-8/GBK"  编码格式
     * 根据登录ip获取归属地
     * @return :iplocation
     */
    public String getIpLocation(String content,String encodingString)
            throws IOException {
        // 这里调用淘宝API
        String urlStr = "http://ip.taobao.com/service/getIpInfo.php";
        // 从http://whois.pconline.com.cn取得IP所在的省市区信息
        String returnStr = getResult(urlStr, content, encodingString);
        if (returnStr != null) {
            // 处理返回的省市区信息
            System.out.println("(1) unicode转换成中文前的returnStr : " + returnStr);
            returnStr = decodeUnicode(returnStr);
            System.out.println("(2) unicode转换成中文后的returnStr : " + returnStr);
            String[] temp = returnStr.split(",");
            if(temp.length<3){
                return "0";//无效IP，局域网测试
            }else{


                //解析json数据 生成指定数据呈现样式
                JSONObject json = JSONObject.fromObject(returnStr);
                System.out.println("json数据： " + json);
                String country = JSONObject.fromObject(json.get("data")).get("country").toString();
                String region = JSONObject.fromObject(json.get("data")).get("region").toString();
                String city = JSONObject.fromObject(json.get("data")).get("city").toString();
                String county = JSONObject.fromObject(json.get("data")).get("county").toString();
                String isp = JSONObject.fromObject(json.get("data")).get("isp").toString();
                String area = JSONObject.fromObject(json.get("data")).get("area").toString();
                System.out.println("国家： " + country);
                System.out.println("地区： " + area);
                System.out.println("省份: " + region);
                System.out.println("城市： " + city);
                System.out.println("区/县： " + county);
                System.out.println("互联网服务提供商： " + isp);

                String address = country + "/";
                address += region + "/";
                address += city + "/";
                address += county;
                System.out.println(address);
                return returnStr;
            }

        }
        return "1";//不是局域网  但查询不到登陆点
    }


}
