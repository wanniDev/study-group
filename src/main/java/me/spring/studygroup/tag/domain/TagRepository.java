package me.spring.studygroup.tag.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TagRepository extends JpaRepository<Long, Tag> {
	@Query("select t.title from Tag t")
	List<String> findTagsTitle();
}
