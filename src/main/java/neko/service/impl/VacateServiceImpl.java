package neko.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import neko.entity.Vacate;
import neko.mapper.VacateMapper;
import neko.service.IVacateService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 请假表 服务实现类
 * </p>
 *
 * @author z9961
 * @since 2019-03-03
 */
@Service
public class VacateServiceImpl extends ServiceImpl<VacateMapper, Vacate> implements IVacateService {

}
