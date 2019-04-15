package neko.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import neko.entity.Classteacher;
import neko.mapper.ClassteacherMapper;
import neko.service.IClassteacherService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author z9961
 * @since 2019-02-28
 */
@Service
public class ClassteacherServiceImpl extends ServiceImpl<ClassteacherMapper, Classteacher> implements IClassteacherService {

}
