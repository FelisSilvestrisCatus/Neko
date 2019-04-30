package neko.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import neko.entity.Rollcall;
import neko.entity.vo.StudentCourseName;
import neko.entity.vo.TeacherRollCall;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.StatementType;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author z9961
 * @since 2019-03-03
 */
public interface RollcallMapper extends BaseMapper<Rollcall> {
    @Select("call getCourseInfo(#{uid},#{cday})")
    @Options(statementType = StatementType.CALLABLE)
    List<StudentCourseName> getCourseToday(int uid, int cday);


    @Select("call getCourseStudentWithoutVacate(#{courseid})")
    @Options(statementType = StatementType.CALLABLE)
    List<TeacherRollCall> getCourseStudentWithoutVacate(int courseid);


    @Select("call getLastRollCallInfo(#{courseid})")
    @Options(statementType = StatementType.CALLABLE)
    List<StudentCourseName> getLastRollCallInfo(int courseid);
}
