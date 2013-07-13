package br.ueg.tcc.bookway.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;


import br.com.vexillum.model.CommonEntity;

@Entity
public class ElementText extends CommonEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String name;
	private String value;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_level", insertable = true, updatable = true)
	private LevelText idLevel;

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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
