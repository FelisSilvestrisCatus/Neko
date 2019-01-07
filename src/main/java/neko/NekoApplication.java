package neko;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("neko.mapper")
public class NekoApplication {

    public static void main(String[] args) {
        SpringApplication.run(NekoApplication.class, args);

    }

}

