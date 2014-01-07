package br.ueg.tcc.bookway.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import br.com.vexillum.model.CommonEntity;
import br.com.vexillum.model.annotations.ValidatorClass;

@ValidatorClass(validatorClass = "br.ueg.tcc.bookway.control.validator.TagsOfMarkingValidator")
@SuppressWarnings("serial")
@Entity
public class TagsOfMarking extends CommonEntity {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_marking", insertable = true, updatable = false, nullable = false)
	private MarkingOfUser marking;

	private String name;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_user", insertable = true, updatable = false, nullable = true)
	private UserBookway userBookway;
	
	public UserBookway getUserBookway() {
		return userBookway;
	}

	public void setUserBookway(UserBookway userBookway) {
		this.userBookway = userBookway;
	}

	public MarkingOfUser getMarking() {
		return marking;
	}

	public void setMarking(MarkingOfUser marking) {
		this.marking = marking;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
