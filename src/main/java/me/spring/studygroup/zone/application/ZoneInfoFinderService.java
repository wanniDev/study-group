package me.spring.studygroup.zone.application;

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
}
