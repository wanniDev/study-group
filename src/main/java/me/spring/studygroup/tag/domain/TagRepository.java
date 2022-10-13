package me.spring.studygroup.tag.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TagRepository extends JpaRepository<Tag, Long> {
	@Query("select t.title from Tag t")
	List<String> findTagsTitle();
	Optional<Tag> findByTitle(String title);
}
