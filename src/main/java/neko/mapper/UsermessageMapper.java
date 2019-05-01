package neko.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import neko.entity.Usermessage;
import neko.entity.vo.UsersMsg;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author z9961
 * @since 2019-04-20
 */
public interface UsermessageMapper extends BaseMapper<Usermessage> {

    @Select("select ")
    List<UsersMsg> getMsg();
}
