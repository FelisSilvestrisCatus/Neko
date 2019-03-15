package neko.mapper;

import neko.entity.Class;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author z9961
 * @since 2019-02-28
 */
public interface ClassMapper extends BaseMapper<Class> {
   public   List<Class> getallclass();

}
