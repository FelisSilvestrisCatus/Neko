package neko.controller;

import neko.utils.ip.LoginInfo;
import neko.utils.redis.RedisUtil;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@RestController
@RequestMapping("/")
public class Index {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private LoginInfo loginInfo;

    @RequestMapping(value = "/")
    public String index(HttpServletRequest request) {
        return "此路不通\n" + "Your ip:" + loginInfo.getIpAddr(request) + "\n" + loginInfo.getIpLocation(loginInfo.getIpAddr(request));
    }

    //测试可用性
    @RequestMapping(value = "/alive")
    public String alive(HttpServletRequest request) {
        return "200";
    }


    /*
     * shiro
     * */
    @RequiresPermissions("student")
    @RequestMapping(value = "/ss")
    public String student(HttpServletRequest request) {
        return "200+stu";
    }


    @RequiresPermissions("teacher")
    @RequestMapping(value = "/st")
    public String teacger(HttpServletRequest request) {
        return "200+tea";
    }

    @RequestMapping(value = "/s")
    public String s(HttpServletRequest request) {
        return "200+tea";
    }

    @RequestMapping(value = "/nekoface")
    public String nekoface(HttpServletRequest request) {

        // 获得Http客户端
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        // 参数
        StringBuffer params = new StringBuffer();
        try {
            // 字符数据最好encoding以下;这样一来，某些特殊字符才能传过去(如:某人的名字就是“&”,不encoding的话,传不过去)
            params.append("temppath=" + URLEncoder.encode("C:\\vfiles\\photo_login\\2\\20190507194827.png", "utf-8"));
            params.append("&");
            params.append("uid=" + URLEncoder.encode("2", "utf-8"));
            params.append("&");
            params.append("type=" + URLEncoder.encode("0", "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // 创建Post请求
        HttpPost httpPost = new HttpPost("http://localhost:58886/facedetection" + "?" + params);

        // 设置ContentType(注:如果只是传普通参数的话,ContentType不一定非要用application/json)
        httpPost.setHeader("Content-Type", "application/json;charset=utf8");

        // 响应模型
        CloseableHttpResponse response = null;
        try {
            // 由客户端执行(发送)Post请求
            response = httpClient.execute(httpPost);
            // 从响应模型中获取响应实体
            HttpEntity responseEntity = response.getEntity();

            System.out.println("响应状态为:" + response.getStatusLine());
            if (responseEntity != null) {
                System.out.println("响应内容长度为:" + responseEntity.getContentLength());
                System.out.println("响应内容为:" + EntityUtils.toString(responseEntity));
            }
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // 释放资源
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        return "200+tea";
    }

}
