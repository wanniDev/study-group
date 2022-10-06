package me.spring.studygroup.account.infrastructure.security;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "persistent_logins")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class PersistentLogins {

	@Id
	@Column(length = 64)
	private String series;

	@Column(nullable = false, length = 64)
	private String username;

	@Column(nullable = false, length = 64)
	private String token;

	@Column(name = "last_used", nullable = false, length = 64)
	private LocalDateTime lastUsed;

}