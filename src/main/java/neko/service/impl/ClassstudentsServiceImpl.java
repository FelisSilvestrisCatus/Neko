package neko.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import neko.entity.Classstudents;
import neko.entity.vo.StudentRollcall;
import neko.mapper.ClassstudentsMapper;
import neko.service.IClassstudentsService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author z9961
 * @since 2019-02-28
 */
@Service
public class ClassstudentsServiceImpl extends ServiceImpl<ClassstudentsMapper, Classstudents> implements IClassstudentsService {
    @Override
    public IPage<Classstudents> getJoinedStudent(Integer currentPage, String cid, String state, List<Integer> uid_list) {
        Page<StudentRollcall> page = new Page<>(currentPage, 10);
        return this.baseMapper.getJoinedStudent(page, currentPage, cid, state,uid_list);
    }
}
