package ewha.backend.Controller.constant;

import ewha.backend.domain.user.dto.UserDto;
import ewha.backend.global.smsAuth.dto.SmsDto;

public class SmsControllerConstant {

	public static final SmsDto.NumberRequest NUMBER_REQUEST_DTO =
		SmsDto.NumberRequest.builder()
			.phoneNumber("01012345678")
			.build();

	public static final SmsDto.CertificationRequest SMS_CERTIFICATION_REQUEST_DTO =
		SmsDto.CertificationRequest.builder()
			.phoneNumber("01012345678")
			.certificationNumber("123456")
			.build();

	public static final SmsDto.FindRequest FIND_ID_REQUEST_DTO =
		SmsDto.FindRequest.builder()
			.nickname("닉네임")
			.phoneNumber("01012345678")
			.build();

	public static final SmsDto.FindCertificationRequest FIND_ID_CERTIFICATION_REQUEST_DTO =
		SmsDto.FindCertificationRequest.builder()
			.nickname("닉네임")
			.phoneNumber("01012345678")
			.certificationNumber("123456")
			.build();

	public static final SmsDto.FindPasswordRequest FIND_PASSWORD_REQUEST_DTO =
		SmsDto.FindPasswordRequest.builder()
			.phoneNumber("01012345678")
			.build();

	public static final SmsDto.FindPasswordCertificationRequest FIND_PASSWORD_CERTIFICATION_REQUEST_DTO =
		SmsDto.FindPasswordCertificationRequest.builder()
			.phoneNumber("01012345678")
			.certificationNumber("123456")
			.build();

}
