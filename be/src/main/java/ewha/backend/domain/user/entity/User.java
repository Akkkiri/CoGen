package ewha.backend.domain.user.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.lang.Nullable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import ewha.backend.domain.bookmark.entity.Bookmark;
import ewha.backend.domain.comment.entity.Comment;
import ewha.backend.domain.feed.entity.Feed;
import ewha.backend.domain.follow.entity.Follow;
import ewha.backend.domain.image.entity.Image;
import ewha.backend.domain.like.entity.AnswerLike;
import ewha.backend.domain.like.entity.CommentLike;
import ewha.backend.domain.like.entity.FeedLike;
import ewha.backend.domain.notification.entity.Notification;
import ewha.backend.domain.question.entity.Answer;
import ewha.backend.domain.report.entity.AnswerReport;
import ewha.backend.domain.report.entity.CommentReport;
import ewha.backend.domain.report.entity.FeedReport;
import ewha.backend.domain.user.dto.UserDto;
import ewha.backend.domain.user.entity.enums.AgeType;
import ewha.backend.domain.user.entity.enums.GenderType;
import ewha.backend.global.BaseTimeEntity;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Builder
@DynamicInsert
@Table(name = "users")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id", updatable = false)
	private Long id;
	@Column(name = "phone_number_id", nullable = false, unique = true)
	private String userId;
	@Column(nullable = false)
	private Integer ariFactor;
	@Column
	@ColumnDefault("1")
	private Integer level;
	@Column(nullable = false)
	private Boolean isFirstLogin;
	@Column(nullable = false)
	private String password;
	@Column(nullable = false)
	private String nickname;
	@Column
	private String profileImage; // 프로필 이미지 경로
	@Column
	private String thumbnailPath;
	@Enumerated(EnumType.STRING)
	private GenderType genderType;
	@Enumerated(EnumType.STRING)
	private AgeType ageType;
	@Column
	@ColumnDefault("0")
	private Long followerCount;
	@Column
	@ColumnDefault("0")
	private Long followingCount;
	@Column
	private String provider;    // oauth2를 이용할 경우 어떤 플랫폼을 이용하는지
	@Column
	private String providerId;  // oauth2를 이용할 경우 아이디 값
	// @Column
	// private String email; // OAuth의 경우 이메일이 존재할 가능성 있음
	@Column
	@Builder.Default
	private Integer dailyFeedCount = 0;
	@Column
	@Builder.Default
	private Integer dailyCommentCount = 0;
	@Column
	@Builder.Default
	private Boolean hasQuiz = false;
	@Column
	@Builder.Default
	private Integer weeklyQuizCount = 0;
	@Column
	@Builder.Default
	private Boolean hasQuestion = false;
	@Column
	@Builder.Default
	private Integer weeklyQuestionCount = 0;
	@Column
	@Builder.Default
	private Boolean hasTenFollowing = false;
	@Column
	@Builder.Default
	private Boolean hasThirtyFollowing = false;
	@Column
	@Builder.Default
	private Boolean hasFiftyFollowing = false;
	@ElementCollection
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<String> role = new ArrayList<>();

	@Nullable
	@JsonManagedReference
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Image> images = new ArrayList<>();

	@JsonManagedReference
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<UserQna> userQnas = new ArrayList<>();

	@JsonManagedReference
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Feed> feeds = new ArrayList<>();

	@JsonManagedReference
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Bookmark> bookmarks = new ArrayList<>();

	@JsonManagedReference
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Comment> comments = new ArrayList<>();

	@JsonManagedReference
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Answer> answers = new ArrayList<>();

	// @JsonManagedReference
	// @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	// private List<Like> likes = new ArrayList<>();

	@JsonManagedReference
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<FeedLike> feedLikes = new ArrayList<>();

	@JsonManagedReference
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<CommentLike> commentLikes = new ArrayList<>();

	@JsonManagedReference
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<AnswerLike> answerLikes = new ArrayList<>();

	@JsonManagedReference
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<FeedReport> feedReports = new ArrayList<>();

	@JsonManagedReference
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<CommentReport> commentReports = new ArrayList<>();

	@JsonManagedReference
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<AnswerReport> answerReports = new ArrayList<>();

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Notification> notifications = new ArrayList<>();

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "following_user_id")
	private List<Follow> followerList;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "followed_user_id")
	private List<Follow> followingList;

	//    -----------------------------------------------------------------------------------------------------------
	public void addUserQna(UserQna userQna) {
		this.userQnas.add(userQna);
		if (userQna.getUser() != this) {
			userQna.addUser(this);
		}
	}

	public void addFeed(Feed feed) {
		this.feeds.add(feed);
		if (feed.getUser() != this) {
			feed.addUser(this);
		}
	}

	public void addBookmark(Bookmark bookmark) {
		this.bookmarks.add(bookmark);
		if (bookmark.getUser() != this) {
			bookmark.addUser(this);
		}
	}

	public void addComment(Comment comment) {
		this.comments.add(comment);
		if (comment.getUser() != this) {
			comment.addUser(this);
		}
	}

	public void addAnswer(Answer answer) {
		this.answers.add(answer);
		if (answer.getUser() != this) {
			answer.addUser(this);
		}
	}

	public void addFeedLike(FeedLike feedLike) {
		this.feedLikes.add(feedLike);
		if (feedLike.getUser() != this) {
			feedLike.addUser(this);
		}
	}

	public void addCommentLike(CommentLike commentLike) {
		this.commentLikes.add(commentLike);
		if (commentLike.getUser() != this) {
			commentLike.addUser(this);
		}
	}

	public void addAnswerLike(AnswerLike answerLike) {
		this.answerLikes.add(answerLike);
		if (answerLike.getUser() != this) {
			answerLike.addUser(this);
		}
	}

	public void addCommentReport(CommentReport commentReport) {
		this.commentReports.add(commentReport);
		if (commentReport.getUser() != this) {
			commentReport.addUser(this);
		}
	}

	public void addAnswerReport(AnswerReport answerReport) {
		this.answerReports.add(answerReport);
		if (answerReport.getUser() != this) {
			answerReport.addUser(this);
		}
	}

	public void addFeedReport(FeedReport feedReport) {
		this.feedReports.add(feedReport);
		if (feedReport.getUser() != this) {
			feedReport.addUser(this);
		}
	}

	public void addNotification(Notification notification) {
		this.notifications.add(notification);
		if (notification.getUser() != this) {
			notification.addUser(this);
		}
	}
	//    -----------------------------------------------------------------------------------------------------------

	@Builder(builderClassName = "OAuth2Register", builderMethodName = "oauth2Register")
	public User(String nickname, String password, String email, Integer ariFactor, List<String> role, String provider,
		String providerId) {
		this.nickname = nickname;
		this.password = password;
		this.ariFactor = ariFactor;
		//        this.email = email;
		this.role = role;
		this.provider = provider;
		this.providerId = providerId;
	}

	public void setUser(User user) {
		this.id = user.getId();
		this.userId = user.getUserId();
		this.isFirstLogin = user.getIsFirstLogin();
		this.password = user.getPassword();
		this.role = user.getRole();
		this.nickname = user.getNickname();
		this.ariFactor = user.getAriFactor();
		this.genderType = user.getGenderType();
		this.ageType = user.getAgeType();
	}

	public void onFirstLogin(GenderType genderType, AgeType ageType) {
		this.genderType = genderType;
		this.ageType = ageType;
	}

	public void setIsFirstLogin(Boolean isFirstLogin) {
		this.isFirstLogin = isFirstLogin;
	}

	public void setProfileImage(String profileImagePath) {
		this.profileImage = profileImagePath;
	}

	public void setThumbnailPath(String thumbnailPath) {
		this.thumbnailPath = thumbnailPath;
	}

	public void updatePassword(String password) {
		this.password = password;
	}

	public void updateOAuthInfo(String email, String picture, String nickname) {

		// if (!this.email.equals(email)) {
		// 	this.email = email;
		// }
		if (!this.profileImage.equals(picture)) {
			this.profileImage = picture;
		}
		if (!this.nickname.equals(nickname)) {
			this.nickname = nickname;
		}
	}

	public void addFollower() {
		this.followerCount++;
	}

	public void removeFollower() {
		if (this.followerCount > 0) {
			this.followerCount--;
		}
	}

	public void addFollowing() {
		this.followingCount++;
	}

	public void removeFollowing() {
		if (this.followingCount > 0) {
			this.followingCount--;
		}
	}

	public void addAriFactor(Integer point) {
		this.ariFactor += point;
	}

	public void removeAriFactor() {
		if (ariFactor > 0) {
			this.ariFactor--;
		}
	}

	public void addLevel() {
		this.level++;
		this.ariFactor = 0;
	}

	public void addDailyFeedCount() {
		this.dailyFeedCount++;
	}

	public void addDailyCommentCount() {
		this.dailyCommentCount++;
	}

	public void addWeeklyQuizCount() {
		this.weeklyQuizCount++;
	}

	public void addWeeklyQuestionCount() {
		weeklyQuestionCount++;
	}

	public User oauthUpdate(String nickname, String email) {
		if (!this.nickname.equals(nickname)) {
			this.nickname = nickname;
		}
		// if (!this.email.equals(email)) {
		// 	this.email = email;
		// }
		return this;
	}

	public User oauthUpdate(String nickname) {
		if (!this.nickname.equals(nickname)) {
			this.nickname = nickname;
		}
		return this;
	}

	public void updateUserInfo(UserDto.UserInfo userInfo) {
		this.nickname = userInfo.getNickname();
		this.profileImage = userInfo.getProfileImage();
		this.genderType = userInfo.getGenderType();
		this.ageType = userInfo.getAgeType();
	}

	public void setHasQuiz(Boolean hasQuiz) {
		this.hasQuiz = hasQuiz;
	}

	public void setHasQuestion(Boolean hasQuestion) {
		this.hasQuestion = hasQuestion;
	}

	public void setWeeklyQuizCount(Integer weeklyQuizCount) {
		this.weeklyQuizCount = weeklyQuizCount;
	}

	public void setWeeklyQuestionCount(Integer weeklyQuestionCount) {
		this.weeklyQuestionCount = weeklyQuestionCount;
	}

	public void setHasTenFollowing(Boolean hasTenFollowing) {
		this.hasTenFollowing = hasTenFollowing;
	}

	public void setHasThirtyFollowing(Boolean hasThirtyFollowing) {
		this.hasThirtyFollowing = hasThirtyFollowing;
	}

	public void setHasFiftyFollowing(Boolean hasFiftyFollowing) {
		this.hasFiftyFollowing = hasFiftyFollowing;
	}

	public Boolean verifyPassword(BCryptPasswordEncoder bCryptPasswordEncoder, String password) {
		return bCryptPasswordEncoder.matches(password, this.password);
	}
}
