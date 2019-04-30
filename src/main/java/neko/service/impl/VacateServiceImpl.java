package neko.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import neko.entity.Vacate;
import neko.entity.vo.AuditVacateByTeacher;
import neko.entity.vo.TeacherRollCall;
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
        if (this.baseMapper.cancelVacate(vid, uid) == 0) {
            map = generalMethod.getErrorMap();
        }

        return map;
    }

    @Override
    public Map<String, String> getDetails(int vid, int uid) {
        Map<String, String> map = generalMethod.getSuccessMap();
        map.put("data", JSON.toJSONString(this.baseMapper.getDetails(vid, uid)));
        return map;
    }

    @Override
    public Map<String, String> getDetailsByTeacher(int vid) {
        Map<String, String> map = generalMethod.getSuccessMap();
        map.put("data", JSON.toJSONString(this.baseMapper.getDetailsByTeacher(vid)));
        return map;
    }

    @Override
    public List<AuditVacateByTeacher> auditVacateByTeacher(String nowdate, int uid) {
        return this.baseMapper.auditVacateByTeacher(nowdate, uid);
    }

    @Override
    public List<AuditVacateByTeacher> VacateList(int uid, int state) {
        return this.baseMapper.VacateList(uid, state);
    }

    @Override
    public Map<String, String> auditVacate(Integer vid, Integer state, String remark) {

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("vid", vid);
        Vacate vacate = this.baseMapper.selectOne(queryWrapper);
        vacate.setRemark(remark).setState(state);
        if (this.baseMapper.updateById(vacate) == 0) {
            return generalMethod.getErrorMap();
        } else {
            Map<String, String> map = generalMethod.getSuccessMap();
            map.put("msg", "请假申请处理成功");
            return map;
        }
    }

    @Override
    public List<VacateWithTeacherName> getMyVacateByCourse(Integer uid, String vcourse) {
        return this.baseMapper.getMyVacateByCourse(uid, vcourse);
    }

    @Override
    public List<TeacherRollCall> getCourseStudentWhoVacate(int courseid) {
        return this.baseMapper.getCourseStudentWhoVacate(courseid);
    }
}
