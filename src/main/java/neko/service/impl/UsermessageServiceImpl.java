package neko.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import neko.entity.Usermessage;
import neko.entity.vo.UsersMsg;
import neko.mapper.UsermessageMapper;
import neko.service.IUsermessageService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author z9961
 * @since 2019-04-20
 */
@Service
public class UsermessageServiceImpl extends ServiceImpl<UsermessageMapper, Usermessage> implements IUsermessageService {


    @Override
    public boolean sendMessage(Integer uid, Integer sourceid, String message) {
        Usermessage usermessage = new Usermessage();
        usermessage.setUid(uid).setSourceid(sourceid).setMessage(message);
        return this.baseMapper.insert(usermessage) > 0;
    }

    @Override
    public List<UsersMsg> getMsg(Integer uid) {
        List<UsersMsg> msgList = this.baseMapper.getMsg();
        return msgList;
    }
}
