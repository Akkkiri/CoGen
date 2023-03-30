package ewha.backend.domain.comment.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import ewha.backend.domain.feed.entity.Feed;
import ewha.backend.domain.like.entity.CommentLike;
import ewha.backend.domain.report.entity.CommentReport;
import ewha.backend.domain.user.entity.User;
import ewha.backend.global.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@DynamicInsert
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseTimeEntity implements Serializable {

	private static final long serialVersionUID = -4038131162139964754L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "comment_id")
	private Long id;
	@Column(nullable = false, length = 250)
	private String body;
	@Column
	@ColumnDefault("0")
	private Long likeCount;
	@Column
	@ColumnDefault("0")
	private Long reportCount;
	@Column
	private Boolean isBlocked;

	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;
	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "feed_id")
	private Feed feed;

	// @JsonManagedReference
	// @OneToMany(mappedBy = "comment", cascade = CascadeType.REMOVE)
	// @LazyCollection(LazyCollectionOption.FALSE)
	// @NotFound(action = NotFoundAction.IGNORE)
	// private final List<Like> likes = new ArrayList<>();

	@JsonManagedReference
	@OneToMany(mappedBy = "comment", cascade = CascadeType.PERSIST, orphanRemoval = true)
	@LazyCollection(LazyCollectionOption.FALSE)
	@NotFound(action = NotFoundAction.IGNORE)
	private final List<CommentLike> commentLikes = new ArrayList<>();

	@JsonManagedReference
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<CommentReport> commentReports = new ArrayList<>();

	//    -----------------------------------------------------------------------------------------------------------
	public void addUser(User user) {
		this.user = user;
		if (!this.user.getComments().contains(this)) {
			this.user.getComments().add(this);
		}
	}

	public void addFeed(Feed feed) {
		this.feed = feed;
		if (!this.feed.getComments().contains(this)) {
			this.feed.getComments().add(this);
		}
	}

	public void addCommentLike(CommentLike commentLike) {
		this.commentLikes.add(commentLike);
		if (commentLike.getComment() != this) {
			commentLike.addComment(this);
		}
	}

	public void addFeedReport(CommentReport commentReport) {
		this.commentReports.add(commentReport);
		if (commentReport.getComment() != this) {
			commentReport.addComment(this);
		}
	}
	//    -----------------------------------------------------------------------------------------------------------

	public void updateComment(Comment comment) {
		this.body = comment.getBody();
	}

	public void addLike() {
		if (this.likeCount == null) {
			this.likeCount = 1L;
		} else {
			this.likeCount = likeCount + 1;
		}
	}

	public void removeLike() {
		if (this.likeCount > 0) {
			this.likeCount = likeCount - 1;
		} else {
			this.likeCount = 0L;
		}
	}

	public void addReportCount() {
		this.reportCount++;
	}

	public void removeReportCount() {
		if (this.reportCount > 0) {
			this.reportCount--;
		}
	}

	public void setIsBlocked() {
		this.isBlocked = true;
	}
}
