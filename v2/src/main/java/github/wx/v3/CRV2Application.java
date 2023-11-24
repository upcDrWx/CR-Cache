package github.wx.v3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * @author wx
 * @description
 * @date 2023/11/14 11:06
 */
@SpringBootApplication
@EnableCaching
//@MapperScan("github.wx.business.mapper")
public class CRV2Application {
    public static void main(String[] args) {
        SpringApplication.run(CRV2Application.class);
    }
}
