package ewha.backend.domain.bookmark.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import ewha.backend.domain.feed.entity.Feed;
import ewha.backend.domain.user.entity.User;

import com.fasterxml.jackson.annotation.JsonBackReference;

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
public class Bookmark {

	@Id
	@Column(name = "bookmark_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@JsonBackReference
	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "feed_id")
	private Feed feed;

	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "user_id")
	private User user;

	public void addFeed(Feed feed) {
		this.feed = feed;
		if (!this.feed.getBookmarks().contains(this)) {
			this.feed.getBookmarks().add(this);
		}
	}

	public void addUser(User user) {
		this.user = user;
		if (!this.user.getBookmarks().contains(this)) {
			this.user.getBookmarks().add(this);
		}
	}
}
