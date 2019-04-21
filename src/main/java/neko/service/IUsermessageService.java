package neko.service;

import neko.entity.Usermessage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author z9961
 * @since 2019-04-20
 */
public interface IUsermessageService extends IService<Usermessage> {

/*

* */
    public boolean sendMessage(Integer uid,Integer sourceid,String message);
}
