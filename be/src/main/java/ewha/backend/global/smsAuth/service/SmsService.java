package ewha.backend.global.smsAuth.service;

import static ewha.backend.global.smsAuth.controller.SmsConstant.*;

import java.util.HashMap;
import java.util.Random;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;

import ewha.backend.domain.user.entity.User;
import ewha.backend.domain.user.repository.UserRepository;
import ewha.backend.domain.user.service.UserService;
import ewha.backend.global.smsAuth.dto.SmsDto;
import ewha.backend.global.smsAuth.repository.SmsRedisRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class SmsService {

	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	private final UserService userService;
	private final UserRepository userRepository;
	private final SmsRedisRepository smsRedisRepository;

	public void sendSms(String phoneNumber) throws CoolsmsException {

		Message coolsms = new Message(API_KEY, API_SECRET);

		Random rand = new Random();
		StringBuilder certificationNumber = new StringBuilder();
		for (int i = 0; i < 6; i++) {
			String ran = Integer.toString(rand.nextInt(10));
			certificationNumber.append(ran);
		}

		HashMap<String, String> params = new HashMap<>();

		params.put("to", phoneNumber);
		params.put("from", "01030380831");
		params.put("type", "SMS");
		params.put("text", "우리 함께 CoGen: 인증번호는" + "[" + certificationNumber + "]" + "입니다.");
		params.put("app_version", "test app 1.0");

		coolsms.send(params);

		smsRedisRepository.createCertification(phoneNumber, certificationNumber.toString());
	}

	public void sendTempPassSms(String phoneNumber) throws CoolsmsException {

		Message coolsms = new Message(API_KEY, API_SECRET);

		Random rand = new Random();
		StringBuilder tempPassword = new StringBuilder();
		for (int i = 0; i < 8; i++) {
			String ran = Integer.toString(rand.nextInt(10));
			tempPassword.append(ran);
		}

		User findUser = userService.findByUserId(phoneNumber);
		findUser.updatePassword(bCryptPasswordEncoder.encode(tempPassword));
		userRepository.save(findUser);

		HashMap<String, String> params = new HashMap<>();

		params.put("to", phoneNumber);
		params.put("from", "01030380831");
		params.put("type", "SMS");
		params.put("text", "우리 함께 CoGen: 임시 비밀번호는" + "[" + tempPassword + "]" + "입니다. 절대 외부로 유출하지 마세요.");
		params.put("app_version", "test app 1.0");

		coolsms.send(params);
	}

	public String verifyCertification(SmsDto.CertificationRequest request) {
		if (!isVerified(request)) {
			throw new AuthenticationCredentialsNotFoundException("인증번호가 일치하지 않습니다.");
		}
		return "인증번호 일치";
	}

	public String verifyCertification(SmsDto.FindPasswordCertificationRequest request) {
		if (!isVerified(request)) {
			throw new AuthenticationCredentialsNotFoundException("인증번호가 일치하지 않습니다.");
		}
		return "인증번호 일치";
	}

	public String findVerifyCertification(SmsDto.FindCertificationRequest request) {
		if (!isFindVerified(request)) {
			throw new AuthenticationCredentialsNotFoundException("인증번호가 일치하지 않습니다.");
		}
		return "인증번호 일치";
	}

	public String findPasswordVerifyCertification(SmsDto.FindPasswordCertificationRequest request) {
		if (!isFindPasswordVerified(request)) {
			throw new AuthenticationCredentialsNotFoundException("인증번호가 일치하지 않습니다.");
		}
		return "인증번호 일치";
	}

	public Boolean isVerified(SmsDto.CertificationRequest request) {
		return (smsRedisRepository.hasKey(request.getPhoneNumber()))
			&& smsRedisRepository.getCertification(request.getPhoneNumber()).equals(request.getCertificationNumber());
	}

	public Boolean isVerified(SmsDto.FindPasswordCertificationRequest request) {
		return (smsRedisRepository.hasKey(request.getPhoneNumber()))
			&& smsRedisRepository.getCertification(request.getPhoneNumber()).equals(request.getCertificationNumber());
	}

	public Boolean isFindVerified(SmsDto.FindCertificationRequest request) {
		return (smsRedisRepository.hasKey(request.getPhoneNumber()))
			&& smsRedisRepository.getCertification(request.getPhoneNumber()).equals(request.getCertificationNumber());
	}

	public Boolean isFindPasswordVerified(SmsDto.FindPasswordCertificationRequest request) {
		return (smsRedisRepository.hasKey(request.getPhoneNumber()))
			&& smsRedisRepository.getCertification(request.getPhoneNumber()).equals(request.getCertificationNumber());
	}
}
