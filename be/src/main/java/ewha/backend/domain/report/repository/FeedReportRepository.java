package ewha.backend.domain.report.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ewha.backend.domain.report.entity.FeedReport;

public interface FeedReportRepository extends JpaRepository<FeedReport, Long> {
}
