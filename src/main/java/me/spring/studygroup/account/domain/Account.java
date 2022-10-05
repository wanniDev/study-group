package me.spring.studygroup.account.domain;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Data
@Builder
@EqualsAndHashCode(of = "id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Account {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true)
	private String email;

	@Column(unique = true)
	private String nickname;

	private String password;

	private boolean emailVerified;

	private String emailCheckToken;

	private LocalDateTime emailCheckTokenGeneratedAt;

	private LocalDateTime joinedAt;

	private String bio;

	private String url;

	private String occupation;

	private String location;

	@Lob @Basic(fetch = FetchType.EAGER)
	private String profileImage;

	private boolean studyCreatedByWeb;

	private boolean studyEnrollmentResultByEmail;

	private boolean studyEnrollmentResultByWeb;

	private boolean studyUpdatedByEmail;

	private boolean studyUpdatedByWeb;

	public void generateEmailCheckToken() {
		this.emailCheckToken = UUID.randomUUID().toString();
		this.emailCheckTokenGeneratedAt = LocalDateTime.now();
	}

	public boolean isValidToken(String token) {
		return this.emailCheckToken.equals(token);
	}

	public void completeSignUp() {
		this.emailVerified = true;
		this.joinedAt = LocalDateTime.now();
	}

	public boolean canSendConfirmEmail() {
		return this.emailCheckTokenGeneratedAt.isBefore(LocalDateTime.now().minusHours(1));
	}
}
