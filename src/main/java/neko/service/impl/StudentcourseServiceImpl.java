package neko.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import neko.entity.Studentcourse;
import neko.mapper.StudentcourseMapper;
import neko.service.IStudentcourseService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author z9961
 * @since 2019-03-27
 */
@Service
public class StudentcourseServiceImpl extends ServiceImpl<StudentcourseMapper, Studentcourse> implements IStudentcourseService {

}
