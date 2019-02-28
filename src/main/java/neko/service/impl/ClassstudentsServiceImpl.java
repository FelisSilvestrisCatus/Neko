package neko.service.impl;

import neko.entity.Classstudents;
import neko.mapper.ClassstudentsMapper;
import neko.service.IClassstudentsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author z9961
 * @since 2019-02-28
 */
@Service
public class ClassstudentsServiceImpl extends ServiceImpl<ClassstudentsMapper, Classstudents> implements IClassstudentsService {

}
