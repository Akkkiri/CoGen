// package com.ewha.back.domain.chat.service;
//
// import org.springframework.data.redis.core.RedisTemplate;
// import org.springframework.data.redis.listener.ChannelTopic;
// import org.springframework.stereotype.Service;
//
// import com.ewha.back.domain.chat.entity.RedisChat;
//
// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;
//
// @Slf4j
// @Service
// @RequiredArgsConstructor
// public class RedisPublisher {
// 	private final RedisTemplate<String, Object> redisTemplate;
//
// 	public void publish(ChannelTopic topic, RedisChat message) {
// 		redisTemplate.convertAndSend(topic.getTopic(), message);
// 		log.info("Sent to Redis");
// 	}
// }
