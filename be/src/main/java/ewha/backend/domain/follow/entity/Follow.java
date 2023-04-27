package ewha.backend.domain.follow.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.DynamicInsert;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ewha.backend.domain.user.entity.User;
import ewha.backend.global.BaseTimeEntity;
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
@DynamicInsert
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "FOLLOW",
	uniqueConstraints = {
		@UniqueConstraint(name = "follow_following", columnNames = {"following_user_id", "followed_user_id"})})
public class Follow extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "follow_id", updatable = false)
	private Long followId;

	@JsonIgnoreProperties
	@ManyToOne
	@JoinColumn(name = "following_user_id") // 팔로우 하는 유저 아이디
	private User followingUser;

	@JsonIgnoreProperties
	@ManyToOne
	@JoinColumn(name = "followed_user_id") // 팔로잉 당하는 유저 아이디
	private User followedUser;
}
