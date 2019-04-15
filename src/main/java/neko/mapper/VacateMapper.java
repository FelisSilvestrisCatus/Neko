package neko.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import neko.entity.Vacate;
import neko.entity.vo.AuditVacateByTeacher;
import neko.entity.vo.VacateWithTeacherName;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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

    @Select("select v.vid,c.cname,v.vname,v.vtime,v.vtype,v.state,u.uname from vacate v,course c," +
            "classteacher t,users u where v.uid=#{uid} and v.courseid=c.courseid and c.cid=t.cid and t.uid=u.uid")
    List<VacateWithTeacherName> getMyVacate(@Param("uid") int uid);

    @Update("update vacate set state = -1 where vid =#{vid} and uid =#{uid};")
    int cancelVacate(@Param("vid") int vid, @Param("uid") int uid);

    @Select("select v.vid, c.cname, v.vname, v.vtime, v.vtype, v.state, u.uname " +
            "from vacate v,course c,classteacher t,users u " +
            "where v.uid =#{uid} and v.courseid=c.courseid and c.cid=t.cid and t.uid=u.uid and vid=#{vid}")
    VacateWithTeacherName getDetails(@Param("vid") int vid, @Param("uid") int uid);

    @Select("select vacate.vid, users.uname,users.phone,class.cname,course.cname as coursename,vacate.vtype,vacate.vtime,vacate.vname from vacate\n" +
            "   vacate left join  users  users on users.uid=vacate.uid  left join  class class on class.cid=vacate.courseid,course course left join class class1 on course.cid=class1.cid where SUBSTR(vacate.vtime FROM 1 FOR 16)<'2019-04-15 12:34'and\n" +
            "    SUBSTR(vacate.vtime FROM 20 FOR 35)>#{nowdate} and vacate.courseid in (select  course.courseid from course where tid=#{uid})")
    List<AuditVacateByTeacher> auditVacateByTeacher(@Param("nowdate") String nowdate, @Param("uid") int uid);
}
