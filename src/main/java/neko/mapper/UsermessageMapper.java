package neko.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import neko.entity.Usermessage;
import neko.entity.vo.UsersMsg;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author z9961
 * @since 2019-04-20
 */
public interface UsermessageMapper extends BaseMapper<Usermessage> {

    @Select("select m.umid, m.message, m.mtime, m.mstate, u.uname " +
            "from usermessage m left join users u on m.sourceid = u.uid " +
            "where m.uid=#{uid} and m.mstate=#{state}")
    List<UsersMsg> getMsg(@Param("uid") Integer uid, @Param("state") Integer state);

    @Update("update usermessage set mstate=1 where uid=#{uid}")
    void setAllMsgRead(@Param("uid") Integer uid);
}
