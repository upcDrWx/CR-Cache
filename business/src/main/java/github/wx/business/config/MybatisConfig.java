package github.wx.business.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author wx
 * @description
 * @date 2023/11/13 16:53
 */
@MapperScan("github.wx.business.mapper")
@Configuration
public class MybatisConfig {
}
