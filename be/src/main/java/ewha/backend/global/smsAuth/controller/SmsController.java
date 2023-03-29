package ewha.backend.global.smsAuth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.nurigo.java_sdk.exceptions.CoolsmsException;

import ewha.backend.domain.user.entity.User;
import ewha.backend.domain.user.service.UserService;
import ewha.backend.global.smsAuth.dto.SmsDto;
import ewha.backend.global.smsAuth.service.SmsService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SmsController {
	private final SmsService smsService;
	private final UserService userService;

	@PostMapping("/sms/send")
	public ResponseEntity<String> smsCertification(@RequestBody SmsDto.NumberRequest request) throws CoolsmsException {

		System.out.println("인증 요청 번호: " + request.getPhoneNumber());

		userService.verifyUserId(request.getPhoneNumber());
		smsService.sendSms(request.getPhoneNumber());

		return ResponseEntity.ok("Request Certification Number");
	}

	@PostMapping("/sms/verification")
	public ResponseEntity<String> smsVerification(@RequestBody SmsDto.CertificationRequest request) {

		String response = smsService.verifyCertification(request);

		return ResponseEntity.ok(request.getPhoneNumber());
	}

	@PostMapping("/find/id/sms/send")
	public ResponseEntity<String> findMyIdRequest(@RequestBody SmsDto.FindRequest request) throws CoolsmsException {

		System.out.println("인증 요청 번호: " + request.getPhoneNumber());

		userService.verifyNicknameAndPhoneNumber(request.getNickname(), request.getPhoneNumber());

		smsService.sendSms(request.getPhoneNumber());

		return ResponseEntity.ok("Request Certification Number");
	}

	@PostMapping("/find/id/sms/verification")
	public ResponseEntity<String> findMyIdVerification(@RequestBody SmsDto.FindCertificationRequest request) {

		smsService.findVerifyCertification(request);

		User findUser = userService.findByNickname(request.getNickname());

		return ResponseEntity.ok(findUser.getUserId());
	}

	@PostMapping("/find/password/sms/send")
	public ResponseEntity<String> findMyPasswordRequest(@RequestBody SmsDto.FindPasswordRequest request) throws CoolsmsException {

		System.out.println("인증 요청 번호: " + request.getPhoneNumber());

		userService.verifyUserIdAndPhoneNumber(request.getUserId(), request.getPhoneNumber());

		smsService.sendSms(request.getPhoneNumber());

		return ResponseEntity.ok("Request Certification Number");
	}

	@PostMapping("/find/password/sms/verification")
	public ResponseEntity<String> findMyPasswordVerification(@RequestBody SmsDto.FindPasswordCertificationRequest request) {

		smsService.findPasswordVerifyCertification(request);

		User findUser = userService.findByUserId(request.getUserId());

		return ResponseEntity.ok(findUser.getPassword());
	}
}
