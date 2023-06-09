package ewha.backend.global.config;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

// import com.ewha.back.domain.chat.service.RedisSubscriber;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import io.lettuce.core.ReadFrom;
import io.lettuce.core.RedisURI;
import io.lettuce.core.resource.ClientResources;

@EnableCaching
@Configuration
public class RedisConfig extends CachingConfigurerSupport {

	@Value("${spring.redis.host}")
	private String redisHost;

	@Value("${spring.redis.port}")
	private int redisPort;


	@Bean
	public LettuceConnectionFactory lettuceConnectionFactory() {
		RedisURI redisUri = RedisURI.builder()
			.withHost(redisHost)
			.withPort(redisPort)
			.build();

		ClientResources clientResources = ClientResources.builder().build();
		LettuceClientConfiguration lettuceClientConfiguration = LettuceClientConfiguration.builder()
			.readFrom(ReadFrom.MASTER)
			.clientResources(clientResources)
			.build();

		LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisHost, redisPort);
		lettuceConnectionFactory.setShareNativeConnection(false);
		lettuceConnectionFactory.afterPropertiesSet();
		return lettuceConnectionFactory;
	}

	/*
	 * Redis Server와 상호작용 하기 위한 설정. 직렬화 설정 추가
	 */
	@Bean
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(connectionFactory);
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(String.class));
		redisTemplate.setEnableTransactionSupport(true);
		redisTemplate.setExposeConnection(true);
		redisTemplate.afterPropertiesSet();
		return redisTemplate;
	}

	// @Bean
	// public RedisConnectionFactory redisConnectionFactory() {
	// 	return new LettuceConnectionFactory(redisHost, redisPort);
	// }

	// @Bean
	public RedisCacheManager redisCacheManager() {
		RedisCacheConfiguration redisCacheConfig = RedisCacheConfiguration
			.defaultCacheConfig()
			.disableCachingNullValues()
			.serializeKeysWith(RedisSerializationContext
				.SerializationPair
				.fromSerializer(new StringRedisSerializer()))
			.serializeValuesWith(RedisSerializationContext
				.SerializationPair
				.fromSerializer(new GenericJackson2JsonRedisSerializer()));

		Map<String, RedisCacheConfiguration> cacheConfiguration = new HashMap<>();

		cacheConfiguration.put(CacheConstant.NEWEST_FEEDS, redisCacheConfig.entryTtl(Duration.ofMinutes(3)));
		cacheConfiguration.put(CacheConstant.FEED_COMMENTS, redisCacheConfig.entryTtl(Duration.ofMinutes(3)));
		cacheConfiguration.put(CacheConstant.CATEGORY_FEEDS, redisCacheConfig.entryTtl(Duration.ofMinutes(3)));

		return RedisCacheManager.RedisCacheManagerBuilder
			.fromConnectionFactory(lettuceConnectionFactory())
			.cacheDefaults(redisCacheConfig)
			.build();
	}

	@Bean
	public ObjectMapper objectMapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // timestamp 형식 안따르도록 설정
		mapper.registerModules(new JavaTimeModule(), new Jdk8Module()); // LocalDateTime 매핑을 위해 모듈 활성화
		return mapper;
	}

	/*
	 * 단일 Topic 사용을 위해 Channel Topic 빈으로 등록
	 */
	@Bean
	public ChannelTopic channelTopic() {
		return new ChannelTopic("chatroom");
	}

	/*
	 * Redis Channel(Topic)으로부터 메시지를 받고 주입된 Listener에게 비동기적으로 발송하는 역할 수행
	 */
	// @Bean
	// public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory connectionFactory,
	// 	MessageListenerAdapter listenerAdapter,
	// 	ChannelTopic channelTopic) {
	// 	RedisMessageListenerContainer container = new RedisMessageListenerContainer();
	// 	container.addMessageListener(listenerAdapter, channelTopic);
	// 	container.setConnectionFactory(connectionFactory);
	// 	return container;
	// }

	/*
	 * Container로부터 메시지를 받아 실제 처리하는 로직
	 */
	// @Bean
	// public MessageListenerAdapter messageListenerAdapter(RedisSubscriber redisSubscriber) {
	// 	return new MessageListenerAdapter(redisSubscriber, "sendMessage");
	// }
}
