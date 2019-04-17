package neko.neko;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import neko.entity.Class;
import neko.entity.Classteacher;
import neko.service.IClassService;
import neko.service.IClassteacherService;
import neko.service.IVacateService;
import neko.service.IVacatefilesService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NekoApplicationTests {
    @Autowired
    private IClassService classService;
    @Autowired
    private IClassteacherService classteacherServic;
    @Autowired
    private IVacateService vacateService;
    @Autowired
    private IVacatefilesService vacatefilesService;


    @Test
    public void contextLoads() {


        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("uid",1);
        //获取老师创建的班级
        List<Classteacher> classlistbyuid=classteacherServic.list(queryWrapper);
        Iterator it = classlistbyuid.iterator();
        List<Integer> cid_list=new ArrayList<>();
        while(it.hasNext()) {
            int cid= ( (Classteacher)it.next()).getCid();
            cid_list.add(cid);
        }
        System.out.println("该老师开的总共课程个数"+cid_list.size());
        QueryWrapper queryWrapper1 = new QueryWrapper();
        queryWrapper1.in("cid",cid_list);
        List<Class> classList=classService.list(queryWrapper1);
        System.out.println("打印"+classList.size());

    }

}

