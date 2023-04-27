// package com.ewha.back.global.config.temp;
//
// import org.springframework.context.annotation.Configuration;
// import org.springframework.web.socket.config.annotation.EnableWebSocket;
// import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
// import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
//
// import com.ewha.back.domain.chat.controller.ChatController;
//
// import lombok.RequiredArgsConstructor;
//
// @Configuration
// @EnableWebSocket
// @RequiredArgsConstructor
// public class WebSocketConfig implements WebSocketConfigurer {
//
// 	private final WebSocketHandler webSocketHandler;
// 	@Override
// 	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
// 		registry.addHandler(webSocketHandler, "/ws").setAllowedOrigins("*");
// 	}
// }
