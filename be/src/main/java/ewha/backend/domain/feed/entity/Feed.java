package ewha.backend.domain.feed.entity;

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
import javax.persistence.OneToOne;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import ewha.backend.domain.bookmark.entity.Bookmark;
import ewha.backend.domain.category.entity.Category;
import ewha.backend.domain.comment.entity.Comment;
import ewha.backend.domain.image.entity.Image;
import ewha.backend.domain.like.entity.FeedLike;
import ewha.backend.domain.report.entity.FeedReport;
import ewha.backend.domain.user.entity.User;
import ewha.backend.global.BaseTimeEntity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@DynamicInsert
@AllArgsConstructor
@NoArgsConstructor
public class Feed extends BaseTimeEntity implements Serializable {

	private static final long serialVersionUID = 6494678977089006639L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "feed_id")
	private Long id;
	@Column
	private String imagePath;
	@Column
	private String thumbnailPath;
	@Column(nullable = false, length = 30)
	private String title;
	@Column(columnDefinition = "VARCHAR(1000)")
	private String body;
	@Column
	@ColumnDefault("0")
	private Long commentCount;
	@Column
	@ColumnDefault("0")
	private Long likeCount;
	@Column
	@ColumnDefault("0")
	private Long viewCount;
	@Column
	@ColumnDefault("0")
	private Long reportCount;
	@Column
	@Builder.Default
	private Boolean isBlocked = false;

	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id")
	private Category category;

	@JsonManagedReference
	@OneToOne(fetch = FetchType.EAGER, mappedBy = "feed", cascade = CascadeType.PERSIST, orphanRemoval = true)
	private Image image;
	@JsonManagedReference
	@OneToMany(mappedBy = "feed", cascade = CascadeType.PERSIST, orphanRemoval = true)
	@LazyCollection(LazyCollectionOption.TRUE)
	private List<Comment> comments;
	@JsonManagedReference
	@OneToMany(mappedBy = "feed", cascade = CascadeType.PERSIST, orphanRemoval = true)
	@LazyCollection(LazyCollectionOption.TRUE)
	private List<Bookmark> bookmarks;
	@JsonManagedReference
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<FeedReport> feedReports = new ArrayList<>();

	// @JsonManagedReference
	// @OneToMany(mappedBy = "feed", cascade = CascadeType.REMOVE)
	// @LazyCollection(LazyCollectionOption.FALSE)
	// @NotFound(action = NotFoundAction.IGNORE)
	// private List<Like> likes = new ArrayList<>();

	@JsonManagedReference
	@OneToMany(mappedBy = "feed", cascade = CascadeType.PERSIST, orphanRemoval = true)
	@LazyCollection(LazyCollectionOption.FALSE)
	@NotFound(action = NotFoundAction.IGNORE)
	private List<FeedLike> feedLikes = new ArrayList<>();

	//    -----------------------------------------------------------------------------------------------------------

	public void addUser(User user) {
		this.user = user;
		if (!this.user.getFeeds().contains(this)) {
			this.user.getFeeds().add(this);
		}
	}

	public void addCategory(Category category) {
		this.category = category;
		if (!this.category.getFeeds().contains(this)) {
			this.category.getFeeds().add(this);
		}
	}

	public void addFeedLike(FeedLike feedLike) {
		this.feedLikes.add(feedLike);
		if (feedLike.getFeed() != this) {
			feedLike.addFeed(this);
		}
	}

	public void addComment(Comment comment) {
		this.comments.add(comment);
		if (comment.getFeed() != this) {
			comment.addFeed(this);
		}
	}

	public void addBookmark(Bookmark bookmark) {
		this.bookmarks.add(bookmark);
		if (bookmark.getFeed() != this) {
			bookmark.addFeed(this);
		}
	}

	public void addFeedReport(FeedReport feedReport) {
		this.feedReports.add(feedReport);
		if (feedReport.getFeed() != this) {
			feedReport.addFeed(this);
		}
	}
	//    -----------------------------------------------------------------------------------------------------------

	public void updateFeed(Feed feed) {
		this.title = feed.getTitle();
		this.body = feed.getBody();
		this.category = feed.getCategory();
		this.imagePath = feed.getImagePath();
		this.thumbnailPath = feed.getThumbnailPath();
	}

	public void addImagePaths(String fullPath, String thumbnailPath) {
		this.imagePath = fullPath;
		this.thumbnailPath = thumbnailPath;
	}

	public void addView() {
		if (viewCount == null) {
			this.viewCount = 1L;
		} else {
			this.viewCount = viewCount + 1;
		}
	}

	public void addLike() {
		if (likeCount == null) {
			this.likeCount = 1L;
		} else {
			this.likeCount = likeCount + 1;
		}
	}

	public void removeLike() {
		if (likeCount > 0) {
			this.likeCount = likeCount - 1;
		}
	}

	public void addCommentCount() {
		this.commentCount++;
	}

	public void removeCommentCount() {
		if (likeCount > 0) {
			this.likeCount--;
		}
	}

	public void setComments(List<Comment> isLikedComments) {
		this.comments = isLikedComments;
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
