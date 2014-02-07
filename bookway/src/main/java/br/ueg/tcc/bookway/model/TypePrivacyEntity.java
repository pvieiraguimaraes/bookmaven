package br.ueg.tcc.bookway.model;

import br.ueg.tcc.bookway.model.enums.TypePrivacyItem;

/**
 * Objeto utilizado para setar se os dados do perfil do usuário são visíveis ou
 * não para outros usuários na rede.
 * 
 * @author pedro
 * 
 */
public class TypePrivacyEntity {

	private TypePrivacyItem email;

	private TypePrivacyItem birthDate;

	private TypePrivacyItem sex;

	private TypePrivacyItem state;

	private TypePrivacyItem city;

	private TypePrivacyItem profession;

	private TypePrivacyItem areaOfInterest;

	private TypePrivacyItem graduation;

	public TypePrivacyItem getEmail() {
		return email;
	}

	public void setEmail(TypePrivacyItem email) {
		this.email = email;
	}

	public TypePrivacyItem getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(TypePrivacyItem birthDate) {
		this.birthDate = birthDate;
	}

	public TypePrivacyItem getSex() {
		return sex;
	}

	public void setSex(TypePrivacyItem sex) {
		this.sex = sex;
	}

	public TypePrivacyItem getState() {
		return state;
	}

	public void setState(TypePrivacyItem state) {
		this.state = state;
	}

	public TypePrivacyItem getCity() {
		return city;
	}

	public void setCity(TypePrivacyItem city) {
		this.city = city;
	}

	public TypePrivacyItem getProfession() {
		return profession;
	}

	public void setProfession(TypePrivacyItem profession) {
		this.profession = profession;
	}

	public TypePrivacyItem getAreaOfInterest() {
		return areaOfInterest;
	}

	public void setAreaOfInterest(TypePrivacyItem areaOfInterest) {
		this.areaOfInterest = areaOfInterest;
	}

	public TypePrivacyItem getGraduation() {
		return graduation;
	}

	public void setGraduation(TypePrivacyItem graduation) {
		this.graduation = graduation;
	}

}
