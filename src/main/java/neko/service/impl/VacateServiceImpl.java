package neko.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import neko.entity.Vacate;
import neko.entity.vo.VacateWithTeacherName;
import neko.mapper.VacateMapper;
import neko.service.IVacateService;
import neko.utils.generalMethod;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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


    @Override
    public List<VacateWithTeacherName> getMyVacate(int uid) {
        return this.baseMapper.getMyVacate(uid);
    }

    @Override
    public Map<String, String> cancelVacate(int vid, int uid) {
        Map<String, String> map = generalMethod.getSuccessMap();
        if (this.baseMapper.cancelVacate(vid,uid) == 0) {
            map = generalMethod.getErrorMap();
        }

        return map;
    }
}
