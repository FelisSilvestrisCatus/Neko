package neko.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import neko.entity.Rollcalldetails;
import neko.entity.vo.StudentRollcall;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author z9961
 * @since 2019-03-03
 */
public interface RollcalldetailsMapper extends BaseMapper<Rollcalldetails> {

    IPage<StudentRollcall> myAttendance(Page page, @Param("uid") Integer uid,
                                        @Param("datetimeBegin") String datetimeBegin,
                                        @Param("datetimeEnd") String datetimeEnd,
                                        @Param("course") String course);
}
