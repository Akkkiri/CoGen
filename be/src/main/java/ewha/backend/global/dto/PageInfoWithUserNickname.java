package ewha.backend.global.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PageInfoWithUserNickname {
	private String nickname;
	private int page;
	private int size;
	private long totalElements;
	private int totalPages;
}
