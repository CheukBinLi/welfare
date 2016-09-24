package com.welfare.core.fundraising.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.cache.annotation.Cacheable;

import com.welfare.core.base.entity.BaseEntity;

@Entity(name = "BIZ_FUNDRAISING")
@Cacheable(value = "FundraisingCache")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Fundraising extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String title; // 筹款标题
	@Column(columnDefinition = "text", name = "CREATE_USER")
	private int createUser;// 作者
	@Column(columnDefinition = "text")
	private String content;// 活动内容
	@Column(name = "THEME_URL")
	private String themeUrl;// 主题背景图
	@Column(name = "TITLE_CONTENT")
	private String shareTitle;// 分享标题
	@Column(columnDefinition = "text", name = "SHARE_CONTENT")
	private String shareContent;// 分享内容
	private int maxAmount;
	private int mixAmount;
	private int defaultFirstAmount;
	private int defaultSecondAmount;
	@Column(name = "FROM_DATE")
	private Date fromDate;// 开始日期
	@Column(name = "THRU_DATE")
	private Date thruDate;// 结束日期

	public int getId() {
		return id;
	}

	public Fundraising setId(int id) {
		this.id = id;
		return this;
	}

	public String getTitle() {
		return title;
	}

	public Fundraising setTitle(String title) {
		this.title = title;
		return this;
	}

	public int getCreateUser() {
		return createUser;
	}

	public Fundraising setCreateUser(int createUser) {
		this.createUser = createUser;
		return this;
	}

	public String getContent() {
		return content;
	}

	public Fundraising setContent(String content) {
		this.content = content;
		return this;
	}

	public String getThemeUrl() {
		return themeUrl;
	}

	public Fundraising setThemeUrl(String themeUrl) {
		this.themeUrl = themeUrl;
		return this;
	}

	public String getShareTitle() {
		return shareTitle;
	}

	public Fundraising setShareTitle(String shareTitle) {
		this.shareTitle = shareTitle;
		return this;
	}

	public String getShareContent() {
		return shareContent;
	}

	public Fundraising setShareContent(String shareContent) {
		this.shareContent = shareContent;
		return this;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public Fundraising setFromDate(Date fromDate) {
		this.fromDate = fromDate;
		return this;
	}

	public Date getThruDate() {
		return thruDate;
	}

	public Fundraising setThruDate(Date thruDate) {
		this.thruDate = thruDate;
		return this;
	}

	public int getMaxAmount() {
		return maxAmount;
	}

	public Fundraising setMaxAmount(int maxAmount) {
		this.maxAmount = maxAmount;
		return this;
	}

	public int getMixAmount() {
		return mixAmount;
	}

	public Fundraising setMixAmount(int mixAmount) {
		this.mixAmount = mixAmount;
		return this;
	}

	public int getDefaultFirstAmount() {
		return defaultFirstAmount;
	}

	public Fundraising setDefaultFirstAmount(int defaultFirstAmount) {
		this.defaultFirstAmount = defaultFirstAmount;
		return this;
	}

	public int getDefaultSecondAmount() {
		return defaultSecondAmount;
	}

	public Fundraising setDefaultSecondAmount(int defaultSecondAmount) {
		this.defaultSecondAmount = defaultSecondAmount;
		return this;
	}

	public Fundraising(int id) {
		super();
		this.id = id;
	}

	public Fundraising() {
		super();
	}

}
