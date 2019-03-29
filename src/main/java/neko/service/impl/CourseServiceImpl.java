package neko.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import neko.entity.Course;
import neko.entity.vo.StudentCourseName;
import neko.mapper.CourseMapper;
import neko.service.ICourseService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 课程表 服务实现类
 * </p>
 *
 * @author z9961
 * @since 2019-03-03
 */
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements ICourseService {

    @Override
    public List<StudentCourseName> getMyCourse(int uid) {
        return this.baseMapper.getMyCourse(uid);
    }
}
