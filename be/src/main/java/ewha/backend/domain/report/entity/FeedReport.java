package ewha.backend.domain.report.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import ewha.backend.domain.feed.entity.Feed;
import ewha.backend.domain.user.entity.User;
import ewha.backend.global.BaseTimeEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FeedReport extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "feed_report_id")
	private Long id;
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ReportType reportType;

	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;
	@JsonBackReference
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "feed_id")
	private Feed feed;

	public void addUser(User user) {
		this.user = user;
		if (!this.user.getFeedReports().contains(this)) {
			this.user.getFeedReports().add(this);
		}
	}

	public void addFeed(Feed feed) {
		this.feed = feed;
		if (!this.feed.getFeedReports().contains(this)) {
			this.feed.getFeedReports().add(this);
		}
	}
}
