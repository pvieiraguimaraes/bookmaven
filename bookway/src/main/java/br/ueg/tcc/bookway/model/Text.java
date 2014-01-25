package br.ueg.tcc.bookway.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import br.com.vexillum.model.CommonEntity;
import br.com.vexillum.model.annotations.SearchField;
import br.com.vexillum.model.annotations.Validate;
import br.com.vexillum.model.annotations.ValidatorClass;
import br.ueg.tcc.bookway.model.enums.TypePrivacy;

@ValidatorClass(validatorClass = "br.ueg.tcc.bookway.control.validator.TextValidator")
@Entity
public class Text extends CommonEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@SearchField
	@Validate(notNull = true, min = 5, max = 500)
	private String title;
	private String description;
	private Date insertDate;

	@Validate(notNull = true)
	@Enumerated(EnumType.ORDINAL)
	private TypePrivacy typeText;
	private boolean community;
	private String filePath;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "elementRoot_id")
	private LevelText rootLevelText;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_userOwning", insertable = true, updatable = true)
	private UserBookway userOwning;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "text", cascade = CascadeType.ALL)
	private List<RelationshipTextUser> textUser;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "text", cascade = CascadeType.ALL)
	private List<Study> study;

	public List<RelationshipTextUser> getTextUser() {
		return textUser;
	}

	public void setTextUser(List<RelationshipTextUser> textUser) {
		this.textUser = textUser;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getInsertDate() {
		return insertDate;
	}

	public void setInsertDate(Date insertDate) {
		this.insertDate = insertDate;
	}

	public TypePrivacy getTypeText() {
		return typeText;
	}

	public void setTypeText(TypePrivacy typeText) {
		this.typeText = typeText;
	}

	public boolean getCommunity() {
		return community;
	}

	public void setCommunity(boolean community) {
		this.community = community;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public LevelText getRootLevelText() {
		return rootLevelText;
	}

	public void setRootLevelText(LevelText rootLevelText) {
		this.rootLevelText = rootLevelText;
	}

	public UserBookway getUserOwning() {
		return userOwning;
	}

	public void setUserOwning(UserBookway userOwning) {
		this.userOwning = userOwning;
	}

	public List<Study> getStudy() {
		return study;
	}

	public void setStudy(List<Study> study) {
		this.study = study;
	}
	
	@Override
	public String toString() {
		return this.getUserOwning().getName() + " - " + this.getTitle();
	}

}
