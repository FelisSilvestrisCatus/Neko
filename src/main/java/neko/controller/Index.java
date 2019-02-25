package neko.controller;

//import neko.entity.Users;
//import neko.mapper.UsersMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
public class Index {

//    @Autowired
//    private UsersMapper userMapper;
//
//    public void testSelect() {
//        System.out.println(("----- selectAll method test ------"));
//        List<Users> userList = userMapper.selectList(null);
//        Assert.assertEquals(5, userList.size());
//        userList.forEach(System.out::println);
//    }

    @RequestMapping(value = "/")
    public String index(){

//        testSelect();
        return "hello world";
    }


}
