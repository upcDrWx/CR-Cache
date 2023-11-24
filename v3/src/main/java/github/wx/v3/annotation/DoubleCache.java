package github.wx.v3.annotation;

import java.lang.annotation.*;

/**
 * @author wx
 * @description
 * @date 2023/11/14 15:16
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DoubleCache {
    String cacheName();
    String key();
    long l2TimeOut() default 120;
    CacheType type() default CacheType.FULL;
}
