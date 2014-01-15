package br.ueg.tcc.bookway.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import br.com.vexillum.model.CommonEntity;
import br.com.vexillum.model.annotations.Validate;
import br.ueg.tcc.bookway.model.enums.TypePrivacy;

/**
 * @author pedro, Classe da qual deriva todos os itens utilizados para o estudo,
 *         Anotação Marcação, Relacionamentos entre textos entre outros.
 * 
 */
@SuppressWarnings("serial")
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
@Table(name = "itens_of_study")
public abstract class ItensOfStudy extends CommonEntity {

	@Validate(notNull = true)
	@Enumerated(EnumType.STRING)
	private TypePrivacy typePrivacy;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_study", insertable = true, updatable = false, nullable = true)
	private Study study;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "itemOfStudy", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ElementsItensStudy> elementsItensStudies;
	
	private Date dateItem;
	
	public Date getDateItem() {
		return dateItem;
	}

	public void setDateItem(Date dateItem) {
		this.dateItem = dateItem;
	}

	public List<ElementsItensStudy> getElementsItensStudies() {
		return elementsItensStudies;
	}

	public void setElementsItensStudies(
			List<ElementsItensStudy> elementsItensStudies) {
		this.elementsItensStudies = elementsItensStudies;
	}

	public TypePrivacy getTypePrivacy() {
		return typePrivacy;
	}

	public void setTypePrivacy(TypePrivacy typePrivacy) {
		this.typePrivacy = typePrivacy;
	}

	public Study getStudy() {
		return study;
	}

	public void setStudy(Study study) {
		this.study = study;
	}

}
