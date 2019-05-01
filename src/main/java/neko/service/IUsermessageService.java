package neko.service;

import com.baomidou.mybatisplus.extension.service.IService;
import neko.entity.Usermessage;
import neko.entity.vo.UsersMsg;

import java.util.List;

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

    List<UsersMsg> getMsg(Integer uid);
}
