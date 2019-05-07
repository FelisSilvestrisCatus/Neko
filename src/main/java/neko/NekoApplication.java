package neko;

import neko.utils.face.Face;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@MapperScan("neko.mapper") //扫描mapper
@ServletComponentScan  //扫描filter
public class NekoApplication {

    public static void main(String[] args) {
        SpringApplication.run(NekoApplication.class, args);

        String temppath = "C:\\vfiles\\test\\ww.png";
        String uid = "2";
        Face.facedetection(temppath, uid, "0");
    }

}

