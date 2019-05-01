package neko.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import neko.entity.Usermessage;
import neko.entity.vo.UsersMsg;
import neko.mapper.UsermessageMapper;
import neko.service.IUsermessageService;
import neko.utils.generalMethod;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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
    public Map<String, String> getMsg(Integer uid) {
        Map<String, String> map = generalMethod.getSuccessMap();
        List<UsersMsg> unreadmsgList = this.baseMapper.getMsg(uid, 0);
        map.put("unread", JSON.toJSONString(unreadmsgList));
        List<UsersMsg> readmsgList = this.baseMapper.getMsg(uid, 1);
        map.put("read", JSON.toJSONString(readmsgList));
        return map;
    }

    @Override
    public Map<String, String> setMsgRead(Integer uid, int umid) {
        Map<String, String> map = generalMethod.getSuccessMap();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("umid", umid);
        Usermessage usermessage = this.baseMapper.selectOne(queryWrapper);
        usermessage.setMstate(1);
        this.baseMapper.updateById(usermessage);
        return map;
    }

    @Override
    public Map<String, String> setAllMsgRead(Integer uid) {
        this.baseMapper.setAllMsgRead(uid);
        return generalMethod.getSuccessMap();
    }
}
