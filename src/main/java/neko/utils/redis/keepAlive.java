package neko.utils.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Configuration
@EnableScheduling
public class keepAlive {

    @Autowired
    private RedisUtil redisUtil;

    @Scheduled(cron = "0/5 * * * * ?")
    private void keepRedisAlive() {
        try {
            redisUtil.set("time", LocalDateTime.now().toString());
        } catch (Exception e) {
            System.out.println("Redis连接丢失");
            keepRedisAlive();
        }

    }
}