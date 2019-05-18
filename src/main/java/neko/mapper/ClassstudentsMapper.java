package neko.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import neko.entity.Classstudents;
import neko.entity.vo.StudentRollcall;
import org.apache.ibatis.annotations.Param;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author z9961
 * @since 2019-02-28
 */
public interface ClassstudentsMapper extends BaseMapper<Classstudents> {
    //(HttpSession session, Integer currentPage, String cid, String state)
    IPage<Classstudents> getJoinedStudent(Page page, @Param("currentPage") Integer currentPage,
                                          @Param("cid") String cid,
                                          @Param("state") String state,
                                          @Param("uid_list") List<Integer> uid_list);
}
