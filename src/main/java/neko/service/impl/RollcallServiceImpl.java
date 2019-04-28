package neko.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import neko.entity.Rollcall;
import neko.entity.vo.StudentCourseName;
import neko.mapper.RollcallMapper;
import neko.service.IRollcallService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author z9961
 * @since 2019-03-03
 */
@Service
public class RollcallServiceImpl extends ServiceImpl<RollcallMapper, Rollcall> implements IRollcallService {

    @Override
    public List<StudentCourseName> getCourseToday(int uid, int cday) {
        return this.baseMapper.getCourseToday(uid, cday);
    }
}
