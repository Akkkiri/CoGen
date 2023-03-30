package ewha.backend.domain.qna.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import ewha.backend.domain.user.entity.UserQna;
import ewha.backend.global.BaseTimeEntity;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@Builder
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Qna extends BaseTimeEntity {

	@Id
	@Column(name = "user_qna_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column
	private String content;

	@JsonManagedReference
	@OneToMany(mappedBy = "qna", cascade = CascadeType.PERSIST)
	private List<UserQna> userQnas = new ArrayList<>();

	public void addUserQna(UserQna userQna) {
		this.userQnas.add(userQna);
		if (userQna.getQna() != this) {
			userQna.addQna(this);
		}
	}

}
