package ewha.backend.domain.report.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ewha.backend.domain.report.entity.CommentReport;

public interface CommentReportRepository extends JpaRepository<CommentReport, Long> {
}
