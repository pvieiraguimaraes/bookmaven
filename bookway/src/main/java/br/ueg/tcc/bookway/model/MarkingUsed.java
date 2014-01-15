package br.ueg.tcc.bookway.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@SuppressWarnings("serial")
@Entity
@DiscriminatorValue("M")
public class MarkingUsed extends ItensOfStudy {
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_markingofuser", insertable = true, updatable = true, nullable = true)
	private MarkingOfUser markingOfUser;

	public MarkingOfUser getMarkingOfUser() {
		return markingOfUser;
	}

	public void setMarkingOfUser(MarkingOfUser markingOfUser) {
		this.markingOfUser = markingOfUser;
	}

}
