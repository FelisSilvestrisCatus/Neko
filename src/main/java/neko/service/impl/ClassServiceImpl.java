package neko.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import neko.entity.Class;
import neko.entity.vo.ClassWithTeacherName;
import neko.mapper.ClassMapper;
import neko.service.IClassService;
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
public class ClassServiceImpl extends ServiceImpl<ClassMapper, Class> implements IClassService {

    @Override
    public List<ClassWithTeacherName> getAllclass() {

        return this.baseMapper.getallclass();
    }

    @Override
    public List<ClassWithTeacherName> getJoinedclass(int uid) {
        return this.baseMapper.getJoinedclass(uid);
    }
}
