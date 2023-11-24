package github.wx.v4;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * @author wx
 * @description
 * @date 2023/11/15 11:41
 */
@EnableCaching
@SpringBootApplication
public class CRV4Application {
    public static void main(String[] args) {
        SpringApplication.run(CRV4Application.class);
    }
}
