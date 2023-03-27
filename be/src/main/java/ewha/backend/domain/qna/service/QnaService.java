package ewha.backend.domain.qna.service;

import org.springframework.stereotype.Service;

import ewha.backend.domain.qna.entity.Qna;
import ewha.backend.domain.qna.repository.QnaRepository;
import ewha.backend.global.exception.BusinessLogicException;
import ewha.backend.global.exception.ExceptionCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QnaService {

	private final QnaRepository qnaRepository;

	public Qna findVerifiedQna(Long qnaId) {
		return qnaRepository.findById(qnaId)
			.orElseThrow(() -> new BusinessLogicException(ExceptionCode.QNA_NOT_FOUND));
	}
}
