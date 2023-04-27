package ewha.backend.domain.report.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ewha.backend.domain.report.entity.AnswerReport;

public interface AnswerReportRepository extends JpaRepository<AnswerReport, Long> {
}
