package br.ueg.tcc.bookway.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import br.com.vexillum.model.CommonEntity;

@Entity
public class LevelText extends CommonEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String name;

	@OneToMany(fetch = FetchType.LAZY, targetEntity = LevelText.class)
	@Cascade(CascadeType.ALL)
	@JoinColumn(name = "id_parentLevel")
	private List<LevelText> levelsChildren;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_text", insertable = true, updatable = true)
	private Text idText;

	@OneToMany(mappedBy = "idLevel", fetch = FetchType.LAZY)
	@Cascade(CascadeType.ALL)
	private List<ElementText> elements;
	

	public LevelText() {
		levelsChildren = new ArrayList<>();
	}

	public List<LevelText> getLevelsChildren() {
		return levelsChildren;
	}

	public void setLevelsChildren(List<LevelText> levelsChildren) {
		this.levelsChildren = levelsChildren;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Text getIdText() {
		return idText;
	}

	public void setIdText(Text idText) {
		this.idText = idText;
	}

	public List<ElementText> getElements() {
		return elements;
	}

	public void setElements(List<ElementText> elements) {
		this.elements = elements;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
