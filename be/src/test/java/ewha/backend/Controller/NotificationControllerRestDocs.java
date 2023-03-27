package ewha.backend.Controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.anyList;
import static org.mockito.BDDMockito.anyString;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import ewha.backend.domain.notification.entity.Notification;
import ewha.backend.domain.notification.mapper.NotificationMapper;
import ewha.backend.domain.notification.service.NotificationService;
import com.google.gson.Gson;

import ewha.backend.Controller.constant.NotificationControllerConstant;
import ewha.backend.Controller.utils.ApiDocumentUtils;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
public class NotificationControllerRestDocs {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private Gson gson;
	@MockBean
	private NotificationService notificationService;
	@MockBean
	private NotificationMapper notificationMapper;

	@Test
	void subscribeTest() throws Exception {

		String lastEventId = "";

		given(notificationService.connect(anyString())).willReturn(new SseEmitter());

		ResultActions actions =
			mockMvc.perform(
				RestDocumentationRequestBuilders.get("/subscribe")
					.header("Last-Event-ID", lastEventId)
					.accept(MediaType.TEXT_EVENT_STREAM)
			);

		actions
			.andExpect(status().isOk())
			.andDo(document(
				"Subscribe"
			));
	}

	@Test
	void getMyNotificationsTest() throws Exception {

		given(notificationService.getMyNotifications()).willReturn(new ArrayList<>());
		given(notificationMapper.myNotificationsToResponses(anyList())).willReturn(
			NotificationControllerConstant.NOTIFICATION_RESPONSE_LIST);

		ResultActions actions =
			mockMvc.perform(
				RestDocumentationRequestBuilders.get("/notifications")
					.accept(MediaType.APPLICATION_JSON)
			);

		actions
			.andExpect(status().isOk())
			.andDo(MockMvcRestDocumentation.document(
				"Get_My_Notifications",
				ApiDocumentUtils.getDocumentResponse(),
				responseFields(
					List.of(
						fieldWithPath("data.").type(JsonFieldType.ARRAY).description("결과 데이터"),
						fieldWithPath(".data.[].notificationId").type(JsonFieldType.NUMBER).description("알림 번호"),
						fieldWithPath(".data.[].type").type(JsonFieldType.STRING).description("알림 타입"),
						fieldWithPath(".data.[].receiverBody").type(JsonFieldType.STRING).description("알림 내용"),
						fieldWithPath(".data.[].isRead").type(JsonFieldType.BOOLEAN).description("확인 여부"),
						fieldWithPath(".data.[].createdAt").type(JsonFieldType.STRING).description("생성 날짜")
					)
				)));
	}

	@Test
	void getMyNotificationTest() throws Exception {

		Long notificationId = 1L;

		given(notificationService.getMyNotification(anyLong())).willReturn(Notification.builder().build());
		given(notificationMapper.myNotificationToResponse(Mockito.any(Notification.class)))
			.willReturn(NotificationControllerConstant.NOTIFICATION_RESPONSE_DTO);

		ResultActions actions =
			mockMvc.perform(
				RestDocumentationRequestBuilders.get("/notifications/{notification_id}", notificationId)
					.accept(MediaType.APPLICATION_JSON)
			);

		actions
			.andExpect(status().isOk())
			.andDo(MockMvcRestDocumentation.document(
				"Get_My_Notification",
				ApiDocumentUtils.getDocumentResponse(),
				pathParameters(
					parameterWithName("notification_id").description("알림 번호")
				),
				responseFields(
					List.of(
						fieldWithPath(".notificationId").type(JsonFieldType.NUMBER).description("알림 번호"),
						fieldWithPath(".type").type(JsonFieldType.STRING).description("알림 타입"),
						fieldWithPath(".receiverBody").type(JsonFieldType.STRING).description("알림 내용"),
						fieldWithPath(".isRead").type(JsonFieldType.BOOLEAN).description("확인 여부"),
						fieldWithPath(".createdAt").type(JsonFieldType.STRING).description("생성 날짜")
					)
				)));
	}

	@Test
	void checkIfNotReadNotificationsTest() throws Exception {

		given(notificationService.findIfNotReadNotifications()).willReturn(Boolean.TRUE);

		ResultActions actions =
			mockMvc.perform(
				RestDocumentationRequestBuilders.get("/notifications/check")
					.accept(MediaType.APPLICATION_JSON)
			);

		actions
			.andExpect(status().isOk())
			.andDo(document(
				"Check_If_Not_Read_Notification"
			));
	}

	@Test
	void deleteNotificationTest() throws Exception {

		Long notificationId = 1L;

		doNothing().when(notificationService).deleteNotification(anyLong());

		mockMvc.perform(
				RestDocumentationRequestBuilders.delete("/notifications/{notification_id}/delete", notificationId)
			)
			.andExpect(status().isNoContent())
			.andDo(
				document(
					"Delete_Notification",
					pathParameters(
						parameterWithName("notification_id").description("알림 번호")
					)
				)
			);
	}

	@Test
	void deleteAllMyNotificationsTest() throws Exception {

		doNothing().when(notificationService).deleteAllMyNotifications();

		mockMvc.perform(
				RestDocumentationRequestBuilders.delete("/notifications/delete")
			)
			.andExpect(status().isNoContent())
			.andDo(
				document(
					"Delete_All_Notification"
				)
			);
	}
}
