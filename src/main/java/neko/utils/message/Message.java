package neko.utils.message;

import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class Message {

    public int getCode(String userphone) {

        //获取的code验证
        int code = (int) (Math.random() * 9000 + 1000);
        int appid = 1400086573;
        String appkey = "8251f605677bef6805bca1df9c4a4d63";
        String smsSign = "境界的彼方";
        String msg = "尊敬的用户" + ",您的验证码为" + code + "，请于30分钟内填写。如非本人操作，请忽略本短信。";

        Map<String, String> map = new HashMap<>();

        try {
            //指定模板单发短信；
            SmsSingleSender ssender = new SmsSingleSender(appid, appkey);
            SmsSingleSenderResult result = ssender.send(0, "86", userphone,
                    msg, "", "");
            return code;
        } catch (Exception e) {
            return 0;
        }

    }
}
