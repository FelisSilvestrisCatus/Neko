package neko.service;

import com.baomidou.mybatisplus.extension.service.IService;
import neko.entity.Vacate;
import neko.entity.vo.VacateWithTeacherName;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 请假表 服务类
 * </p>
 *
 * @author z9961
 * @since 2019-03-03
 */
public interface IVacateService extends IService<Vacate> {
    List<VacateWithTeacherName> getMyVacate(int uid);

    Map<String, String> cancelVacate(String vid, int uid);
}
