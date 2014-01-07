package br.ueg.tcc.bookway.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import br.com.vexillum.model.UserBasic;
import br.com.vexillum.model.annotations.Validate;
import br.com.vexillum.model.annotations.ValidatorClass;
import br.ueg.tcc.bookway.model.enums.AreaOfInterest;
import br.ueg.tcc.bookway.model.enums.State;

@ValidatorClass(validatorClass = "br.ueg.tcc.bookway.control.validator.UserValidator")
@SuppressWarnings("serial")
@DiscriminatorValue("1")
@Entity
public class UserBookway extends UserBasic {

	public UserBookway() {
		setActive(false);
	}

	private String city;

	@Enumerated(EnumType.STRING)
	private State state;

	private String profession;
	private String graduation;

	@Enumerated(EnumType.STRING)
	private AreaOfInterest areaOfInterest;

	private Date solicitationExclusion;

	@Validate(notNull = true, min = 6)
	@Transient
	private String confirmPassword;

	@OneToMany(mappedBy = "userOwning", fetch = FetchType.LAZY)
	private List<Text> texts;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "userBookway", cascade = CascadeType.ALL)
	private List<RelationshipTextUser> textUser;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "userBookway", cascade = CascadeType.ALL)
	private List<Study> studys;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "userBookway", cascade = CascadeType.ALL)
	private List<MarkingOfUser> markings;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "userBookway", cascade = CascadeType.ALL)
	private List<TagsOfMarking> tagsOfMarkings;

	public List<MarkingOfUser> getMarkings() {
		return markings;
	}

	public void setMarkings(List<MarkingOfUser> markings) {
		this.markings = markings;
	}

	public List<TagsOfMarking> getTagsOfMarkings() {
		return tagsOfMarkings;
	}

	public void setTagsOfMarkings(List<TagsOfMarking> tagsOfMarkings) {
		this.tagsOfMarkings = tagsOfMarkings;
	}

	public List<RelationshipTextUser> getTextUser() {
		return textUser;
	}

	public void setTextUser(List<RelationshipTextUser> textUser) {
		this.textUser = textUser;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public String getProfession() {
		return profession;
	}

	public void setProfession(String profession) {
		this.profession = profession;
	}

	public String getGraduation() {
		return graduation;
	}

	public void setGraduation(String graduation) {
		this.graduation = graduation;
	}

	public AreaOfInterest getAreaOfInterest() {
		return areaOfInterest;
	}

	public void setAreaOfInterest(AreaOfInterest areaOfInterest) {
		this.areaOfInterest = areaOfInterest;
	}

	public Date getSolicitationExclusion() {
		return solicitationExclusion;
	}

	public void setSolicitationExclusion(Date solicitationExclusion) {
		this.solicitationExclusion = solicitationExclusion;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public List<Text> getTexts() {
		return texts;
	}

	public void setTexts(List<Text> texts) {
		this.texts = texts;
	}

	public List<Study> getStudys() {
		return studys;
	}

	public void setStudys(List<Study> studys) {
		this.studys = studys;
	}

}
