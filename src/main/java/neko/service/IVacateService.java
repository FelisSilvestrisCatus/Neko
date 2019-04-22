package neko.service;

import com.baomidou.mybatisplus.extension.service.IService;
import neko.entity.Vacate;
import neko.entity.vo.AuditVacateByTeacher;
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

    Map<String, String> cancelVacate(int vid, int uid);

    Map<String, String> getDetails(int vid, int uid);

    Map<String, String> getDetailsByTeacher(int vid);

    List<AuditVacateByTeacher> auditVacateByTeacher(String nowdate, int uid);

    //显示老师uid下所有的请假记录
    List<AuditVacateByTeacher> VacateList(int uid, int state);
}
