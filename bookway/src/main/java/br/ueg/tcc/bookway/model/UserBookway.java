package br.ueg.tcc.bookway.model;

import java.util.Date;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import br.com.vexillum.model.UserBasic;
import br.com.vexillum.model.annotations.ValidatorClass;
import br.ueg.tcc.bookway.model.enums.AreaOfInterest;
import br.ueg.tcc.bookway.model.enums.State;

@ValidatorClass(validatorClass = "br.ueg.tcc.bookway.control.validator.UserValidator")
@SuppressWarnings("serial")
@DiscriminatorValue("1")
@Entity
public class UserBookway extends UserBasic {

	private String city;

	@Enumerated(EnumType.STRING)
	private State state;

	private String profession;
	private String graduation;

	@Enumerated(EnumType.STRING)
	private AreaOfInterest areaOfInterest;

	private boolean validAccount;
	private Date solicitationExclusion;

	@Transient
	private String confirmPassword;

	@OneToMany(mappedBy = "userOwning", fetch = FetchType.LAZY)
	private List<Text> texts;

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

	public boolean isValidAccount() {
		return validAccount;
	}

	public void setValidAccount(boolean validAccount) {
		this.validAccount = validAccount;
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

}
