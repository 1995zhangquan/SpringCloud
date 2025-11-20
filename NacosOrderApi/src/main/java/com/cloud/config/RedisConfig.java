package com.cloud.config;

import com.cloud.attr.RedisStatic;
import com.cloud.message.PrintMessageReceiver;
import com.cloud.message.RedisMessageListener;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/*
* 配置redis的key的序列化和反序列化策略
* */
@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        //key序列化方式;（不然会出现乱码;）,但是如果方法上有Long等非String类型的话，会报类型转换错误；
        //   所以在没有自己定义key生成策略的时候，以下这个代码建议不要这么写，可以不配置或者自己实现ObjectRedisSerializer
        //   或者JdkSerializationRedisSerializer序列化方式;
        template.setConnectionFactory(redisConnectionFactory);

        //template.setKeySerializer(new StringRedisSerializer()); //key字符串序列化
        //template.setHashKeySerializer(new GenericJackson2JsonRedisSerializer()); //设置value json序列化方式

        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(Object.class);
        //创建ObjectMapper对象，用于配置JSON序列化规则
        ObjectMapper objectMapper = new ObjectMapper();
        //设置所有属性访问器的可见性为ANY，允许序列化所有字段
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        //启用默认类型推断，支持非final类型的对象序列化
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

        //key采用string的序列化
        RedisSerializer<?> stringRedisSerializer = new StringRedisSerializer();

        template.setKeySerializer(stringRedisSerializer);
        //hash的key采用string的序列化
        template.setHashKeySerializer(stringRedisSerializer);
        //value序列化采用jackson

        template.setValueSerializer(jackson2JsonRedisSerializer);
        //hash的value序列化方式采用jackson
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }

    //为了注解形式使用 Spring 缓存功能，需要在项目中添加 spring-boot-starter-cache 依赖，并在配置类中配置缓存管理器。以下是配置缓存管理器的
  /*  @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMillis(30*1000)) //缓存有效期30秒
                .disableCachingNullValues() // 不缓存null值
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
        return RedisCacheManager.builder(factory).cacheDefaults(config).build();
    }*/


    //消息监听容器
    @Bean
    public RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory, RedisMessageListener listener, MessageListenerAdapter adapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        //所有的订阅消息，都需要在这里进行注册绑定,new PatternTopic(TOPIC_NAME1)表示发布的主题信息
        // 可以添加多个 messageListener，配置不同的通道
        container.addMessageListener(listener, new PatternTopic(RedisStatic.CHANNEL_NAME_ORDER));
        container.addMessageListener(adapter, new PatternTopic(RedisStatic.CHANNEL_NAME_PERMIT));
        /**
         * 设置序列化对象
         * 特别注意：1. 发布的时候需要设置序列化；订阅方也需要设置序列化
         *         2. 设置序列化对象必须放在[加入消息监听器]这一步后面，否则会导致接收器接收不到消息
         */
        Jackson2JsonRedisSerializer seria = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        seria.setObjectMapper(objectMapper);
        container.setTopicSerializer(seria);

        return container;
    }

    /**
     * 这个地方是给messageListenerAdapter 传入一个消息接受的处理器，利用反射的方法调用“receiveMessage”
     * 也有好几个重载方法，这边默认调用处理器的方法 叫OnMessage
     *
     * @param printMessageReceiver
     * @return
     */
    @Bean
    public MessageListenerAdapter listenerAdapter(PrintMessageReceiver printMessageReceiver) {
        MessageListenerAdapter receiveMessage = new MessageListenerAdapter(printMessageReceiver, "receiveMessage");

        Jackson2JsonRedisSerializer serializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        serializer.setObjectMapper(objectMapper);
        receiveMessage.setSerializer(serializer);
        return receiveMessage;
    }
}
