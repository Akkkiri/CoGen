package ewha.backend.domain.feed.repository;

import static ewha.backend.domain.category.entity.QCategory.*;
import static ewha.backend.domain.comment.entity.QComment.*;
import static ewha.backend.domain.feed.entity.QFeed.*;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import ewha.backend.domain.feed.entity.Feed;
import ewha.backend.domain.user.entity.User;
import ewha.backend.global.config.CustomPage;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import ewha.backend.domain.bookmark.entity.QBookmark;
import ewha.backend.domain.category.entity.QCategory;
import ewha.backend.domain.feed.entity.QFeed;
import ewha.backend.domain.like.entity.QFeedLike;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class FeedQueryRepository {
	private final JPAQueryFactory jpaQueryFactory;

	public Page<Feed> findMyBookmarks(User user, Pageable pageable) {

		List<Feed> feedList = jpaQueryFactory
			.selectFrom(feed)
			.join(feed.bookmarks, QBookmark.bookmark)
			.where(QBookmark.bookmark.user.eq(user))
			.orderBy(feed.createdAt.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		Long total = jpaQueryFactory
			.select(feed.count())
			.from(feed)
			.join(feed.bookmarks, QBookmark.bookmark)
			.where(QBookmark.bookmark.user.eq(user))
			.fetchOne();

		return new PageImpl<>(feedList, pageable, total);
	}

	public Page<Feed> findFeedPageByUser(User user, Pageable pageable) {

		List<Feed> feedList = jpaQueryFactory
			.select(feed)
			.from(feed)
			.where(feed.user.eq(user))
			.orderBy(feed.createdAt.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		Long total = jpaQueryFactory
			.select(feed.count())
			.from(feed)
			.where(feed.user.eq(user))
			.fetchOne();

		return new PageImpl<>(feedList, pageable, total);
	}

	public Page<Feed> findLikedFeedListByUser(User user, Pageable pageable) {

		List<Feed> feedList = jpaQueryFactory
			.selectFrom(feed)
			.join(feed.feedLikes, QFeedLike.feedLike)
			.where(QFeedLike.feedLike.user.eq(user))
			.orderBy(feed.createdAt.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		Long total = jpaQueryFactory
			.select(feed.count())
			.from(feed)
			.join(feed.feedLikes, QFeedLike.feedLike)
			.where(QFeedLike.feedLike.user.eq(user))
			.fetchOne();

		return new PageImpl<>(feedList, pageable, total);
	}

	public CustomPage<Feed> findNewestFeedList(Pageable pageable) {

		List<Feed> feedList = jpaQueryFactory
			.selectFrom(feed)
			.orderBy(feed.createdAt.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		Long total = jpaQueryFactory
			.select(feed.count())
			.from(feed)
			.fetchOne();

		System.out.println(total);

		return new CustomPage<>(feedList, pageable, total);
	}

	public Page<Feed> findCategoryFeedList(String categoryName, String sort, Pageable pageable) {

		List<Feed> feedList = new ArrayList<>();

		if (sort.equals("likes")) {
			feedList = jpaQueryFactory
				.selectFrom(feed)
				.join(feed.category, category)
				.where(category.categoryType.stringValue().eq(categoryName))
				.orderBy(feed.likeCount.desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();
		} else if (sort.equals("view")) {
			feedList = jpaQueryFactory
				.selectFrom(feed)
				.join(feed.category, category)
				.where(category.categoryType.stringValue().eq(categoryName))
				.orderBy(feed.viewCount.desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();
		}

		Long total = jpaQueryFactory
			.select(feed.count())
			.from(feed)
			.join(feed.category, category)
			.where(category.categoryType.stringValue().eq(categoryName))
			.fetchOne();

		return new PageImpl<>(feedList, pageable, total);
	}

	public Page<Feed> findAllSearchResultPage(String sort, String queryParam, Pageable pageable) {

		List<Feed> feedList = new ArrayList<>();

		JPAQuery<Feed> basicResult = jpaQueryFactory
			.selectFrom(feed)
			.where(feed.title.contains(queryParam).or(feed.body.contains(queryParam)));

		if (sort.equals("new")){
			feedList = basicResult
				.orderBy(feed.createdAt.desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();
		} else if (sort.equals("likes")) {
			feedList = basicResult
				.orderBy(feed.likeCount.desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();
		} else {
			feedList = basicResult
				.orderBy(feed.viewCount.desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();
		}

		// List<Feed> feedList = jpaQueryFactory
		// 	.selectFrom(feed)
		// 	.where(feed.title.contains(queryParam).or(feed.body.contains(queryParam)))
		// 	.orderBy(feed.createdAt.desc())
		// 	.offset(pageable.getOffset())
		// 	.limit(pageable.getPageSize())
		// 	.fetch();

		Long total = jpaQueryFactory
			.select(feed.count())
			.from(feed)
			.where(feed.title.contains(queryParam).or(feed.body.contains(queryParam)))
			.fetchOne();

		return new PageImpl<>(feedList, pageable, total);
	}

	public Page<Feed> findCategorySearchResultPage(String categoryParam, String sort, String queryParam, Pageable pageable) {

		List<Feed> feedList = new ArrayList<>();

		JPAQuery<Feed> basicResult = jpaQueryFactory
			.selectFrom(feed)
			.join(feed.category, category)
			.where(category.categoryType.stringValue().eq(categoryParam)
			.and(feed.title.contains(queryParam).or(feed.body.contains(queryParam))));

		if (sort.equals("new")){
			feedList = basicResult
				.orderBy(feed.createdAt.desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();
		} else if (sort.equals("likes")) {
			feedList = basicResult
				.orderBy(feed.likeCount.desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();
		} else if (sort.equals("view")){
			feedList = basicResult
				.orderBy(feed.viewCount.desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();
		} else {
			feedList = basicResult
				.orderBy(feed.createdAt.desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();
		}

		// List<Feed> feedList = jpaQueryFactory
		// 	.selectFrom(feed)
		// 	.join(feed.category, QCategory.category)
		// 	.where(QCategory.category.categoryType.stringValue().eq(categoryParam))
		// 	.where(feed.title.contains(queryParam).or(feed.body.contains(queryParam)))
		// 	.orderBy(feed.createdAt.desc())
		// 	.offset(pageable.getOffset())
		// 	.limit(pageable.getPageSize())
		// 	.fetch();

		Long total = jpaQueryFactory
			.select(feed.count())
			.from(feed)
			.where(category.categoryType.stringValue().eq(categoryParam)
			.and(feed.title.contains(queryParam).or(feed.body.contains(queryParam))))
			.fetchOne();

		return new PageImpl<>(feedList, pageable, total);
	}

	public List<Feed> findWeeklyBestFeedList() {

		LocalDateTime mondayMidnight = LocalDateTime.now()
			.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
			.toLocalDate()
			.atStartOfDay();

		return jpaQueryFactory
			.selectFrom(feed)
			.where(feed.createdAt.after(mondayMidnight))
			.orderBy(feed.likeCount.desc())
			.limit(5)
			.fetch();
	}

	public void deleteAllByUser(User findUser) {
		jpaQueryFactory.delete(feed)
			.where(feed.user.eq(findUser))
			.execute();
	}
}
