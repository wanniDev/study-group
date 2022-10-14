package me.spring.studygroup.study.presentation.form;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import me.spring.studygroup.study.domain.Study;

public interface StudyRepository extends JpaRepository<Study, Long> {
	boolean existsByPath(String path);
	Optional<Study> findByPath(String path);
}
