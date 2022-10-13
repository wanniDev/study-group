package me.spring.studygroup.account.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.spring.studygroup.tag.domain.Tag;

@Entity
@Table(name = "account_tag")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccountTag {
	@Id
	@Column(name = "account_tag_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="account_id")
	private Account account;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tag_id")
	private Tag tag;

	private void setAccount(Account account) {
		this.account = account;
	}

	private void setTag(Tag tag) {
		this.tag = tag;
	}

	public static AccountTag createNewAccountTag(Account account, Tag tag) {
		AccountTag accountTag = new AccountTag();
		accountTag.setAccount(account);
		accountTag.setTag(tag);
		return accountTag;
	}
}
