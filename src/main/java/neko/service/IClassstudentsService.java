package neko.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import neko.entity.Classstudents;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author z9961
 * @since 2019-02-28
 */
public interface IClassstudentsService extends IService<Classstudents> {
    public IPage<Classstudents> getJoinedStudent(Integer currentPage,
                                                 String cid,
                                                 String state,
                                                 List<Integer> uid_list);
}
