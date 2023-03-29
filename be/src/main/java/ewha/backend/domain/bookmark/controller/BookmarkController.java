package ewha.backend.domain.bookmark.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ewha.backend.domain.bookmark.service.BookmarkService;
import ewha.backend.domain.user.service.UserService;

import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BookmarkController {

	private final UserService userService;
	private final BookmarkService bookmarkService;

	@PatchMapping("/feeds/{feed_id}/save")
	public ResponseEntity<String> saveFeed(@PathVariable("feed_id") Long feedId) {

		String response = bookmarkService.saveFeed(feedId);

		return ResponseEntity.ok().body(response);
	}
}
