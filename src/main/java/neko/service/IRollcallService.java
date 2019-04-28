package neko.service;

import com.baomidou.mybatisplus.extension.service.IService;
import neko.entity.Rollcall;
import neko.entity.vo.StudentCourseName;

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
}
