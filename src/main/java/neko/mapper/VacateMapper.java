package neko.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import neko.entity.Vacate;
import neko.entity.vo.VacateWithTeacherName;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 请假表 Mapper 接口
 * </p>
 *
 * @author z9961
 * @since 2019-03-03
 */
public interface VacateMapper extends BaseMapper<Vacate> {

    @Select("select c.cname,v.vname,v.vtime,v.vtype,v.state,u.uname from vacate v,course c," +
            "classteacher t,users u where v.uid=#{uid} and v.courseid=c.courseid and c.cid=t.cid and t.uid=u.uid")
    List<VacateWithTeacherName> getMyVacate(@Param("uid") int uid);
}
