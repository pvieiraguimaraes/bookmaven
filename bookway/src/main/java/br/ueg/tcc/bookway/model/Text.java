package br.ueg.tcc.bookway.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import br.com.vexillum.model.CommonEntity;
import br.com.vexillum.model.annotations.ValidatorClass;
import br.ueg.tcc.bookway.model.enums.TypeText;

@ValidatorClass(validatorClass = "br.ueg.tcc.bookway.control.validator.TextValidator")
@Entity
public class Text extends CommonEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String title;
	private String description;
	private Date insertDate;

	@Enumerated(EnumType.STRING)
	private TypeText typeText;
	private boolean community;
	private String filePath;

	@ManyToOne(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name = "elementRoot_id")
	private LevelText levelsText;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_userOwning", insertable = true, updatable = true)
	private UserBookway userOwning;
	
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

	public TypeText getTypeText() {
		return typeText;
	}

	public void setTypeText(TypeText typeText) {
		this.typeText = typeText;
	}

	public boolean isCommunity() {
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

	public LevelText getLevelsText() {
		return levelsText;
	}

	public void setLevelsText(LevelText levelsText) {
		this.levelsText = levelsText;
	}

	public UserBookway getUserOwning() {
		return userOwning;
	}

	public void setUserOwning(UserBookway userOwning) {
		this.userOwning = userOwning;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
