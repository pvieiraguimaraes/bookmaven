package br.ueg.tcc.bookway.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import br.com.vexillum.model.CommonEntity;

@Entity
public class ElementText extends CommonEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String name;
	
	@Column(length = 4000)
	private String value;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_level", insertable = true, updatable = true)
	private LevelText idLevel;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "lastElementStop", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Study> studys;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "elementText", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ElementsItensStudy> elementsItensStudies;

	public List<ElementsItensStudy> getElementsItensStudies() {
		return elementsItensStudies;
	}

	public void setElementsItensStudies(
			List<ElementsItensStudy> elementsItensStudies) {
		this.elementsItensStudies = elementsItensStudies;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public LevelText getIdLevel() {
		return idLevel;
	}

	public void setIdLevel(LevelText idLevel) {
		this.idLevel = idLevel;
	}

	public List<Study> getStudys() {
		return studys;
	}

	public void setStudys(List<Study> studys) {
		this.studys = studys;
	}

}
