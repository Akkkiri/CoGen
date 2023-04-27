package ewha.backend.domain.search.controller;

import java.util.ArrayList;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ewha.backend.domain.feed.dto.FeedDto.ListResponse;
import ewha.backend.domain.feed.entity.Feed;
import ewha.backend.domain.feed.mapper.FeedMapper;
import ewha.backend.domain.follow.repository.FollowQueryRepository;
import ewha.backend.domain.search.service.SearchService;
import ewha.backend.domain.user.dto.UserDto;
import ewha.backend.domain.user.entity.User;
import ewha.backend.domain.user.mapper.UserMapper;
import ewha.backend.domain.user.service.UserService;
import ewha.backend.global.dto.MultiResponseDto;

import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {

	private final SearchService searchService;
	private final FeedMapper feedMapper;
	private final UserService userService;
	private final UserMapper userMapper;
	private final FollowQueryRepository followQueryRepository;

	@GetMapping
	public ResponseEntity<MultiResponseDto<ListResponse>> getSearchResult(
		@RequestParam(name = "category", defaultValue = "ALL") String category,
		@RequestParam("query") String query,
		@RequestParam(name = "sort", defaultValue = "new") String sort,
		@RequestParam(name = "page", defaultValue = "1") Integer page) {

		Page<Feed> feedPage = new PageImpl<>(new ArrayList<>());

		if (category == null || category.equals("ALL")) {
			feedPage = searchService.findAllFeedsPageByQueryParam(sort, query, page);
		} else {
			feedPage = searchService.findCategoryFeedsPageByQueryParam(category, sort, query, page);
		}

		PageImpl<ListResponse> responses = feedMapper.feedsToPageResponse(feedPage);

		return ResponseEntity.ok(new MultiResponseDto<>(responses.getContent(), feedPage));
	}

	@GetMapping("/user")
	public ResponseEntity<MultiResponseDto<UserDto.UserSearchResponse>> getUserSearchResult(
		@RequestParam("query") String query, @RequestParam(name = "page", defaultValue = "1") Integer page) {

		Page<User> userPage = searchService.findUserPageByQuery(query, page);
		PageImpl<UserDto.UserSearchResponse> responsePage;

		if (userService.getLoginUserReturnNull() != null) {

			User findUser = userService.getLoginUser();
			Long userId = findUser.getId();

			responsePage = userMapper.userPageToUserSearchResponse(userPage, followQueryRepository, userId);

			return ResponseEntity.ok(new MultiResponseDto<>(responsePage.getContent(), userPage));
		} else {

			responsePage = userMapper.userPageToUserSearchResponse(userPage);

			return ResponseEntity.ok(new MultiResponseDto<>(responsePage.getContent(), userPage));
		}
	}
}
