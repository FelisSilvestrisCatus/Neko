package neko.service;

import neko.entity.Class;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author z9961
 * @since 2019-02-28
 */
public interface IClassService extends IService<Class> {
    public List<Class> getallclass();

}
