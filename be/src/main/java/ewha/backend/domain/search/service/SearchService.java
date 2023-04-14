package ewha.backend.domain.search.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ewha.backend.domain.feed.entity.Feed;
import ewha.backend.domain.feed.repository.FeedQueryRepository;

import ewha.backend.domain.user.entity.User;
import ewha.backend.domain.user.repository.UserQueryRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class SearchService {

	private final FeedQueryRepository feedQueryRepository;
	private final UserQueryRepository userQueryRepository;

	public Page<Feed> findAllFeedsPageByQueryParam(String sort, String queryParam, Integer page) {

		PageRequest pageRequest = PageRequest.of(page - 1, 10);

		return feedQueryRepository.findAllSearchResultPage(sort, queryParam, pageRequest);
	}

	public Page<Feed> findCategoryFeedsPageByQueryParam(String category, String sort, String queryParam, Integer page) {

		PageRequest pageRequest = PageRequest.of(page - 1, 10);

		return feedQueryRepository.findCategorySearchResultPage(category, sort, queryParam, pageRequest);
	}

	public Page<User> findUserPageByQuery(String query, Integer page) {

		PageRequest pageRequest = PageRequest.of(page - 1, 10);

		Page<User> userPage;

		if (query.charAt(0) == '#') {
			userPage = userQueryRepository.findUserPageByHashcode(query.substring(1), pageRequest);
		} else {
			userPage = userQueryRepository.findUserPageByNickname(query, pageRequest);
		}

		return userPage;
	}
}
