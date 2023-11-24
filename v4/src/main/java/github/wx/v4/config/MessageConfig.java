package github.wx.v4.config;

import github.wx.v4.msg.RedisMessageReceiver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

/**
 * @author wx
 * @description  Redis 消息配置类
 * @date 2023/11/16 11:26
 */
@Configuration
public class MessageConfig {

    public static final String TOPIC = "cache.msg";

    /**
     * 一个可以为消息监听器提供异步行为的容器，并且提供消息转换和分派等底层功能
     */
    @Bean
    RedisMessageListenerContainer container(MessageListenerAdapter listenerAdapter,
                                            RedisConnectionFactory redisConnectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        container.addMessageListener(listenerAdapter, new PatternTopic(TOPIC));
        return container;
    }

    /**
     * 消息监听适配器，可以在其中指定自定义的监听代理类，并且可以自定义使用哪个方法处理监听逻辑
     */
    @Bean
    MessageListenerAdapter adapter(RedisMessageReceiver receiver) {
        return new MessageListenerAdapter(receiver, "receive");
    }
}
