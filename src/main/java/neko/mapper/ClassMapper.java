package neko.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import neko.entity.Class;
import neko.entity.vo.ClassWithTeacherName;
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
 * @since 2019-02-28
 */
public interface ClassMapper extends BaseMapper<Class> {

    @Select("select * from classWithTeacherName")
    List<ClassWithTeacherName> getallclass();

    @Select("call JoinedClass(#{uid})")
    @Options(statementType = StatementType.CALLABLE)
    List<ClassWithTeacherName> getJoinedclass(@Param("uid") int uid);
}
