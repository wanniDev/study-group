package me.spring.studygroup.zone.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me.spring.studygroup.zone.domain.Zone;
import me.spring.studygroup.zone.domain.ZoneRepository;
import me.spring.studygroup.zone.presentation.form.ZoneForm;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ZoneInfoFinderService {
	private final ZoneRepository zoneRepository;

	public Zone findByCityAndProvince(ZoneForm zoneForm) {
		return zoneRepository
			.findByCityAndProvince(zoneForm.getCityName(), zoneForm.getProvinceName()).orElseThrow();
	}

	public List<String> findZonesWhiteList() {
		return zoneRepository.findAll().stream().map(Zone::toString).collect(Collectors.toList());
	}
}
