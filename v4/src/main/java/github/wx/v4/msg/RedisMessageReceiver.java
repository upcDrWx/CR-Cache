package github.wx.v4.msg;

import github.wx.v4.cache.DoubleCache;
import github.wx.v4.cache.DoubleCacheManager;
import github.wx.v4.util.MessageSourceUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.net.UnknownHostException;

/**
 * @author wx
 * @description   Redis 消息接收处理类
 * @date 2023/11/16 11:32
 */
@Slf4j
@Component
@AllArgsConstructor
public class RedisMessageReceiver {
    private final RedisTemplate redisTemplate;
    private final DoubleCacheManager manager;

    public void receive(String message) throws UnknownHostException {
        CacheMessage msg  = (CacheMessage)redisTemplate.getValueSerializer().deserialize(message.getBytes());
        log.info(msg.toString());

        if (msg.getMsgSource().equals(MessageSourceUtil.getMsgSource())) {
            log.info("收到本机发出的消息，不做处理");
            return;
        }

        DoubleCache cache = (DoubleCache)manager.getCache(msg.getCacheName());
        if (msg.getType() == CacheMsgType.UPDATE) {
            cache.updateL1Cache(msg.getKey(), msg.getValue());
            log.info("更新本地缓存");
        }

        if (msg.getType() == CacheMsgType.DELETE) {
            log.info("删除本地缓存");
            cache.evictL1Cache(msg.getKey());
        }

    }

}
