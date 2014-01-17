package br.ueg.tcc.bookway.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.com.vexillum.model.CommonEntity;

@SuppressWarnings("serial")
@Entity
@Table(name = "elements_itensstudy")
public class ElementsItensStudy extends CommonEntity {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_itemofstudy", insertable = true, updatable = true, nullable = false)
	private ItensOfStudy itemOfStudy;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_elementtext", insertable = true, updatable = true, nullable = false)
	private ElementText elementText;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_study", insertable = true, updatable = true, nullable = false)
	private Study study;

	public Study getStudy() {
		return study;
	}

	public void setStudy(Study study) {
		this.study = study;
	}

	public ItensOfStudy getItemOfStudy() {
		return itemOfStudy;
	}

	public void setItemOfStudy(ItensOfStudy itemOfStudy) {
		this.itemOfStudy = itemOfStudy;
	}

	public ElementText getElementText() {
		return elementText;
	}

	public void setElementText(ElementText elementText) {
		this.elementText = elementText;
	}

}
