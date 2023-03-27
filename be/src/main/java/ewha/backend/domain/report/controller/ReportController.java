package ewha.backend.domain.report.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ewha.backend.domain.report.service.ReportService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class ReportController {

	private final ReportService reportService;

	@PatchMapping("/feeds/{feed_id}/report")
	public ResponseEntity<String> feedReport(@PathVariable("feed_id") Long feedId) {

		String response = reportService.feedReport(feedId);

		return ResponseEntity.ok(response);
	}

	@PatchMapping("/comments/{comment_id}/report")
	public ResponseEntity<String> commentReport(@PathVariable("comment_id") Long commentId) {

		String response = reportService.commentReport(commentId);

		return ResponseEntity.ok(response);
	}
}
