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
    List<ClassWithTeacherName> getAllclass();

    List<ClassWithTeacherName> getJoinedclass(int uid);
}
