package neko.service.impl;

import neko.entity.Class;
import neko.mapper.ClassMapper;
import neko.service.IClassService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import neko.service.IUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author z9961
 * @since 2019-02-28
 */
@Service
public class ClassServiceImpl extends ServiceImpl<ClassMapper, Class> implements IClassService {


    @Override
    public List<Class> getallclass() {
        return this.baseMapper.getallclass();
    }
}
