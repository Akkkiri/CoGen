// package com.ewha.back.domain.chat.entity;
//
// import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
// import com.fasterxml.jackson.databind.annotation.JsonSerialize;
// import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
// import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
// import lombok.AllArgsConstructor;
// import lombok.Getter;
// import lombok.NoArgsConstructor;
//
// import javax.validation.constraints.NotBlank;
// import javax.validation.constraints.NotNull;
// import java.time.LocalDateTime;
//
// @Getter
// @NoArgsConstructor
// @AllArgsConstructor
// public class RedisChat {
//
//     @NotNull
//     private Long roomId;
//
//     @NotNull
//     private Long senderId;
//
//     @NotBlank
//     private String message;
//
//     @JsonSerialize(using = LocalDateTimeSerializer.class)
//     @JsonDeserialize(using = LocalDateTimeDeserializer.class)
//     private LocalDateTime createdAt;
// }
