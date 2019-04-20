package neko.service;

import com.baomidou.mybatisplus.extension.service.IService;
import neko.entity.Class;
import neko.entity.vo.ClassWithTeacherName;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author z9961
 * @since 2019-02-28
 */
public interface IClassService extends IService<Class> {
    List<ClassWithTeacherName> getAllclass(int uid);

    List<ClassWithTeacherName> getJoinedclass(int uid);
    //获取老师id下所有班级的信息（含学生人数）
    List<ClassWithTeacherName> getClassStudent(int tid);
}
