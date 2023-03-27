package ewha.backend.domain.feed.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import ewha.backend.domain.feed.entity.Feed;
import ewha.backend.domain.user.entity.User;
import ewha.backend.global.config.CustomPage;
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
			.selectFrom(QFeed.feed)
			.join(QFeed.feed.bookmarks, QBookmark.bookmark)
			.where(QBookmark.bookmark.user.eq(user))
			.orderBy(QFeed.feed.createdAt.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		Long total = jpaQueryFactory
			.select(QFeed.feed.count())
			.from(QFeed.feed)
			.join(QFeed.feed.bookmarks, QBookmark.bookmark)
			.where(QBookmark.bookmark.user.eq(user))
			.fetchOne();

		return new PageImpl<>(feedList, pageable, total);
	}

	public Page<Feed> findFeedPageByUser(User user, Pageable pageable) {

		List<Feed> feedList = jpaQueryFactory
			.select(QFeed.feed)
			.from(QFeed.feed)
			.where(QFeed.feed.user.eq(user))
			.orderBy(QFeed.feed.createdAt.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		Long total = jpaQueryFactory
			.select(QFeed.feed.count())
			.from(QFeed.feed)
			.where(QFeed.feed.user.eq(user))
			.fetchOne();

		return new PageImpl<>(feedList, pageable, total);
	}

	public Page<Feed> findLikedFeedListByUser(User user, Pageable pageable) {

		List<Feed> feedList = jpaQueryFactory
			.selectFrom(QFeed.feed)
			.join(QFeed.feed.feedLikes, QFeedLike.feedLike)
			.where(QFeedLike.feedLike.user.eq(user))
			.orderBy(QFeed.feed.createdAt.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		return new PageImpl<>(feedList, pageable, feedList.size());
	}

	public CustomPage<Feed> findNewestFeedList(Pageable pageable) {

		List<Feed> feedList = jpaQueryFactory
			.selectFrom(QFeed.feed)
			.orderBy(QFeed.feed.createdAt.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		Long total = jpaQueryFactory
			.select(QFeed.feed.count())
			.from(QFeed.feed)
			.fetchOne();

		System.out.println(total);

		return new CustomPage<>(feedList, pageable, total);
	}

	public Page<Feed> findCategoryFeedList(String categoryName, Pageable pageable) {

		List<Feed> feedList = jpaQueryFactory
			.selectFrom(QFeed.feed)
			.join(QFeed.feed.category, QCategory.category)
			.where(QCategory.category.categoryType.stringValue().eq(categoryName))
			.orderBy(QFeed.feed.createdAt.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		Long total = jpaQueryFactory
			.select(QFeed.feed.count())
			.from(QFeed.feed)
			.join(QFeed.feed.category, QCategory.category)
			.where(QCategory.category.categoryType.stringValue().eq(categoryName))
			.fetchOne();

		return new PageImpl<>(feedList, pageable, total);
	}

	public Page<Feed> findAllSearchResultPage(String queryParam, Pageable pageable) {

		List<Feed> feedList = jpaQueryFactory
			.selectFrom(QFeed.feed)
			.where(QFeed.feed.title.contains(queryParam).or(QFeed.feed.body.contains(queryParam)))
			.orderBy(QFeed.feed.createdAt.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		Long total = jpaQueryFactory
			.select(QFeed.feed.count())
			.from(QFeed.feed)
			.where(QFeed.feed.title.contains(queryParam).or(QFeed.feed.body.contains(queryParam)))
			.fetchOne();

		return new PageImpl<>(feedList, pageable, total);
	}

	public Page<Feed> findCategorySearchResultPage(String categoryParam, String queryParam, Pageable pageable) {

		List<Feed> feedList = jpaQueryFactory
			.selectFrom(QFeed.feed)
			.join(QFeed.feed.category, QCategory.category)
			.where(QCategory.category.categoryType.stringValue().eq(categoryParam))
			.where(QFeed.feed.title.contains(queryParam).or(QFeed.feed.body.contains(queryParam)))
			.orderBy(QFeed.feed.createdAt.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		Long total = jpaQueryFactory
			.select(QFeed.feed.count())
			.from(QFeed.feed)
			.where(QCategory.category.categoryType.stringValue().eq(categoryParam))
			.where(QFeed.feed.title.contains(queryParam).or(QFeed.feed.body.contains(queryParam)))
			.fetchOne();

		return new PageImpl<>(feedList, pageable, total);
	}

	public void deleteAllByUser(User findUser) {
		jpaQueryFactory.delete(QFeed.feed)
			.where(QFeed.feed.user.eq(findUser))
			.execute();
	}

}
