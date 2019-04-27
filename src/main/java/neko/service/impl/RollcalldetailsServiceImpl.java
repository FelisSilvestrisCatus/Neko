package neko.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import neko.entity.Rollcalldetails;
import neko.entity.Users;
import neko.entity.vo.ClassWithTeacherName;
import neko.entity.vo.StudentCourseName;
import neko.entity.vo.VacateWithTeacherName;
import neko.mapper.RollcalldetailsMapper;
import neko.service.IClassService;
import neko.service.ICourseService;
import neko.service.IRollcalldetailsService;
import neko.service.IVacateService;
import neko.utils.generalMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    @Autowired
    private IVacateService vacateService;

    @Override
    public Map<String, String> myInfo(Users users) {
        Map<String, String> map = generalMethod.getSuccessMap();

        //存放需要的数据
        Map<String, String> data = new HashMap<>();
        List<Map<String, String>> datalist = new ArrayList<>();


        //加入的班级
        List<ClassWithTeacherName> classes = classService.getJoinedclass(users.getUid());
        data = new HashMap<>();
        data.put("name", "我加入的班级");
        data.put("number", classes.size() + "");
        datalist.add(data);

        //我的课程
        List<StudentCourseName> course = courseService.getMyCourse(users.getUid());
        data = new HashMap<>();
        data.put("name", "我的课程");
        data.put("number", course.size() + "");
        datalist.add(data);

        //我的请假
        List<VacateWithTeacherName> vacates = vacateService.getMyVacate(users.getUid());
        data = new HashMap<>();
        data.put("name", "我的请假");
        data.put("number", vacates.size() + "");
        datalist.add(data);


        map.put("data", JSON.toJSONString(datalist));
        return map;
    }
}
