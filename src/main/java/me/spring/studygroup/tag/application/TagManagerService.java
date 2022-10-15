package me.spring.studygroup.tag.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me.spring.studygroup.tag.domain.Tag;
import me.spring.studygroup.tag.domain.TagRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class TagManagerService {
	private final TagRepository tagRepository;

	public Tag findOrCreateNew(String tagTitle) {
		return tagRepository.findByTitle(tagTitle)
			.orElseGet(() -> tagRepository.save(Tag.builder().title(tagTitle).build()));
	}
}
