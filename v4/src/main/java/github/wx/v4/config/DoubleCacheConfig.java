package github.wx.v4.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author wx
 * @description
 * @date 2023/11/15 15:45
 */
@Data
@Component
@ConfigurationProperties(prefix = "doublecache")
public class DoubleCacheConfig {
    private Boolean allowNull = true;
    private Integer init = 100;
    private Integer max = 1000;
    private Long expireAfterWrite ;
    private Long expireAfterAccess;
    private Long refreshAfterWrite;
    private Long redisExpire;
}
