package neko.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import neko.entity.Rollcalldetails;
import neko.entity.Users;
import neko.entity.vo.ClassWithTeacherName;
import neko.entity.vo.StudentCourseName;
import neko.mapper.RollcalldetailsMapper;
import neko.service.IClassService;
import neko.service.ICourseService;
import neko.service.IRollcalldetailsService;
import neko.utils.generalMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author z9961
 * @since 2019-03-03
 */
@Service
public class RollcalldetailsServiceImpl extends ServiceImpl<RollcalldetailsMapper, Rollcalldetails> implements IRollcalldetailsService {

    @Autowired
    private IClassService classService;
    @Autowired
    private ICourseService courseService;


    @Override
    public Map<String, String> myInfo(Users users) {
        Map<String, String> map = generalMethod.getSuccessMap();

        //存放需要的数据
        Map<String, String> data = new HashMap<>();

        //加入的班级
        List<ClassWithTeacherName> classes = classService.getJoinedclass(users.getUid());
        data.put("我加入的班级", classes.size() + "");

        //我的课程
        List<StudentCourseName> course = courseService.getMyCourse(users.getUid());
        data.put("我的课程", course.size() + "");


        map.put("data", JSON.toJSONString(data));
        return map;
    }
}
