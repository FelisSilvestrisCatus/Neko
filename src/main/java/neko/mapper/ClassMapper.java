package neko.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import neko.entity.Class;
import neko.entity.vo.ClassWithTeacherName;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author z9961
 * @since 2019-02-28
 */
public interface ClassMapper extends BaseMapper<Class> {

    @Select("select u.uname,c.cid,c.cname,c.cstate" +
            " from users u,class c,classteacher t" +
            " where t.cid=c.cid and u.uid=t.uid;")
    List<ClassWithTeacherName> getallclass();
}
