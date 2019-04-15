package neko.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import neko.entity.Course;
import neko.entity.vo.StudentCourseName;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.StatementType;

import java.util.List;

/**
 * <p>
 * 课程表 Mapper 接口
 * </p>
 *
 * @author z9961
 * @since 2019-03-03
 */
public interface CourseMapper extends BaseMapper<Course> {

    @Select("call getMyCourse(#{uid})")
    @Options(statementType = StatementType.CALLABLE)
    List<StudentCourseName> getMyCourse(@Param("uid") int uid);
}
