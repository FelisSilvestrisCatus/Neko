package neko.service;

import com.baomidou.mybatisplus.extension.service.IService;
import neko.entity.Rollcall;
import neko.entity.vo.StudentCourseName;
import neko.entity.vo.TeacherRollCall;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author z9961
 * @since 2019-03-03
 */
public interface IRollcallService extends IService<Rollcall> {
    //根据老师 uid  以及 周几 来确定今天的点名课程
     List<StudentCourseName> getCourseToday(int  uid, int cday);
    //根据课程id获取要点名的同学（不含有请假成功的同学）
    List<TeacherRollCall> getCourseStudentWithoutVacate(int courseid);
    //根据课程id虎丘所有的历史出勤信息（达到人数 逃课人数 请假人数）
    List<StudentCourseName> getLastRollCallInfo(int courseid);
}
