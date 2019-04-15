package neko.neko;

import neko.service.IVacateService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NekoApplicationTests {
    @Autowired
    private IVacateService vacateService;

    @Test
    public void contextLoads() {
        System.out.println(vacateService.auditVacateByTeacher("2019-04-15 16:34", 1).size());
    }

}

