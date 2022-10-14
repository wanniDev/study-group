package me.spring.studygroup.zone.domain;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ZoneRepository extends JpaRepository<Zone, Long> {

	Optional<Zone> findByCityAndProvince(String city, String province);
}
