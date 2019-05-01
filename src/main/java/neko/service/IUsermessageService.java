package neko.service;

import com.baomidou.mybatisplus.extension.service.IService;
import neko.entity.Usermessage;

import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author z9961
 * @since 2019-04-20
 */
public interface IUsermessageService extends IService<Usermessage> {


    boolean sendMessage(Integer uid, Integer sourceid, String message);

    Map<String, String> getMsg(Integer uid);

    Map<String, String> setMsgRead(Integer uid, int umid);

    Map<String, String> setAllMsgRead(Integer uid);
}
