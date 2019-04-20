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

    @Select("call classwithteachername(#{uid})")
    @Options(statementType = StatementType.CALLABLE)
    List<ClassWithTeacherName> getallclass(@Param("uid") int uid);

    @Select("call JoinedClass(#{uid})")
    @Options(statementType = StatementType.CALLABLE)
    List<ClassWithTeacherName> getJoinedclass(@Param("uid") int uid);


    @Select("select class.cid,class.cname,(select  count(*) from classstudents where cid=class.cid ) as stunum from class\n" +
            "where  class.cid in ( select cid from  classteacher where uid=#{tid} )")
    List<ClassWithTeacherName> getClassStudent(int tid);
}
