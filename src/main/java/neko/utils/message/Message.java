package neko.utils.message;

import com.alibaba.fastjson.JSONException;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
@Component
public class Message {
    //获取短信的手机号
    private  String  phone="";
    //获取的code验证
    private  int code = (int) (Math.random() * 9000 + 1000);
    private  int appid = 1400086573;
    private  String appkey = "8251f605677bef6805bca1df9c4a4d63";
    private  String smsSign = "境界的彼方";
    String msg ="您的验证码为" + code + "，请于30分钟内填写。如非本人操作，请忽略本短信。";

    public Map<String, String> getCode(String  userphone){

        Map<String, String> map = new HashMap<>();

        try {
            //指定模板单发短信；
            SmsSingleSender ssender = new SmsSingleSender(appid, appkey);
            SmsSingleSenderResult result = ssender.send(0, "86", phone,
                    msg, "", "");
            System.out.print(result);
            System.out.println("获取的code"+code);
           map.put("state","0");
            map.put("data",""+code);
        } catch (HTTPException e) {
            System.out.println("HTTP响应码错误");
            map.put("state","1");
            map.put("data","HTTP响应码错误");

        } catch (JSONException e) {
            System.out.println("json解析错误");
            map.put("state","1");
            map.put("data","json解析错误");
        } catch (IOException e) {
            System.out.println("网络IO错误");
            map.put("state","1");
            map.put("data","网络IO错误");
        }







        return map;

    }

}
