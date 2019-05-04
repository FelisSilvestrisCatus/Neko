package neko.utils.ip;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;

import static neko.utils.ip.Juhe.net;

@Component
public class Geocoder {

    //根据经纬度获取地址
    public static String getAddrInfo(String lat, String lng) {
        String result;
        String url = "http://api.map.baidu.com/geocoder/v2/?callback=renderReverse&location=" +
                lat +
                "," +
                lng +
                "&output=json&latest_admin=1&callback=&" +
                "ak=q8xGTMGQvt1RnyNQqky4glDF";

        try {
            result = net(url, "GET");
            JSONObject object = JSON.parseObject(result);
            JSONObject resultInfo = object.getJSONObject("result");
            return resultInfo.getString("formatted_address");
        } catch (Exception e) {
            return "定位失败";
        }
    }
}
