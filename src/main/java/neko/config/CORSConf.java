package neko.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CORSConf {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")//匹配所有的请求
                        .allowedHeaders("*")    //请求头
                        .allowedMethods("*")    //post,get
                        .allowedOrigins("*")    //允许所有来源
                        .allowCredentials(true); //携带cookie
            }
        };
    }
}
