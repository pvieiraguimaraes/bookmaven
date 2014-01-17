package br.ueg.tcc.bookway.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import br.com.vexillum.model.CommonEntity;
import br.com.vexillum.model.annotations.ValidatorClass;

@ValidatorClass(validatorClass = "br.ueg.tcc.bookway.control.validator.StudyValidator")
@SuppressWarnings("serial")
@Entity
public class Study extends CommonEntity {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_text", insertable = true, updatable = false, nullable = false)
	private Text text;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_user", insertable = true, updatable = false, nullable = false)
	private UserBookway userBookway;

	private Date dateStudy;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_lastelementstop", insertable = true, updatable = true, nullable = true)
	private ElementText lastElementStop;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "study", cascade = CascadeType.ALL)
	private List<ItensOfStudy> itensOfStudies;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "study", cascade = CascadeType.ALL)
	private List<ElementsItensStudy> elementsItensStudies;
	
	public List<ElementsItensStudy> getElementsItensStudies() {
		return elementsItensStudies;
	}

	public void setElementsItensStudies(
			List<ElementsItensStudy> elementsItensStudies) {
		this.elementsItensStudies = elementsItensStudies;
	}

	public Text getText() {
		return text;
	}

	public void setText(Text text) {
		this.text = text;
	}

	public UserBookway getUserBookway() {
		return userBookway;
	}

	public void setUserBookway(UserBookway userBookway) {
		this.userBookway = userBookway;
	}

	public Date getDateStudy() {
		return dateStudy;
	}

	public void setDateStudy(Date dateStudy) {
		this.dateStudy = dateStudy;
	}

	public ElementText getLastElementStop() {
		return lastElementStop;
	}

	public void setLastElementStop(ElementText lastElementStop) {
		this.lastElementStop = lastElementStop;
	}

	public List<ItensOfStudy> getItensOfStudies() {
		return itensOfStudies;
	}

	public void setItensOfStudies(List<ItensOfStudy> itensOfStudies) {
		this.itensOfStudies = itensOfStudies;
	}

}
