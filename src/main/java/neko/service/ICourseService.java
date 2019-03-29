package neko.service;

import com.baomidou.mybatisplus.extension.service.IService;
import neko.entity.Course;
import neko.entity.vo.StudentCourseName;

import java.util.List;

/**
 * <p>
 * 课程表 服务类
 * </p>
 *
 * @author z9961
 * @since 2019-03-03
 */
public interface ICourseService extends IService<Course> {

    //学生的课程
    List<StudentCourseName> getMyCourse(int uid);
}
