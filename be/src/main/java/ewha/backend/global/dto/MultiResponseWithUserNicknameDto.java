package ewha.backend.global.dto;

import java.util.List;

import org.springframework.data.domain.Page;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MultiResponseWithUserNicknameDto<T> {

	private List<T> data;
	private PageInfoWithUserNickname pageInfo;

	public MultiResponseWithUserNicknameDto(List<T> data, Page page, String nickname) {

		String[] nick = nickname.split("#");
		String nickPre = nick[0];
		String nickSuf = "#" + nick[1];

		this.data = data;
		this.pageInfo = new PageInfoWithUserNickname(nickPre, page.getNumber() + 1,
			page.getSize(), page.getTotalElements(), page.getTotalPages());
	}
}
