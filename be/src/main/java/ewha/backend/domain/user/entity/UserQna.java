package ewha.backend.domain.user.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import ewha.backend.domain.qna.entity.Qna;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class UserQna {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_qna_id")
	private Long id;
	@Column
	private String answerBody;

	@ManyToOne
	@JoinColumn(name = "qna_id")
	private Qna qna;

	@ManyToOne
	@JoinColumn(name = "user_id")
	@NotFound(action = NotFoundAction.IGNORE)
	private User user;

	public void addQna(Qna qna) {
		this.qna = qna;
		if (!this.qna.getUserQnas().contains(this)) {
			this.qna.getUserQnas().add(this);
		}
	}

	public void addUser(User user) {
		this.user = user;
		if (!this.user.getUserQnas().contains(this)) {
			this.user.getUserQnas().add(this);
		}
	}
}
