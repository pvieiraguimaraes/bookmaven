package br.ueg.tcc.bookway.model;

import javax.persistence.DiscriminatorValue;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import br.com.vexillum.model.annotations.Validate;
import br.com.vexillum.model.annotations.ValidatorClass;

@ValidatorClass(validatorClass = "br.ueg.tcc.bookway.control.validator.MarkingValidator")
@SuppressWarnings("serial")
@Entity
@DiscriminatorValue("M")
public class Marking extends ItensOfStudy {

	@Validate(notNull = true, max = 20, min = 2)
	private String name;

	@Validate(notNull = true)
	private String color;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_user", insertable = true, updatable = false, nullable = false)
	private UserBookway userBookway;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public UserBookway getUserBookway() {
		return userBookway;
	}

	public void setUserBookway(UserBookway userBookway) {
		this.userBookway = userBookway;
	}

}
