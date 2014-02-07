package br.ueg.tcc.bookway.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import br.com.vexillum.model.annotations.ValidatorClass;

@ValidatorClass(validatorClass = "br.ueg.tcc.bookway.control.validator.RelationshipTextElementValidator")
@SuppressWarnings("serial")
@Entity
@DiscriminatorValue("R")
public class RelationshipTextElement extends ItensOfStudy {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_text_origin", insertable = true, updatable = false, nullable = true)
	private Text textOrigin;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_text_destiny", insertable = true, updatable = false, nullable = true)
	private Text textDestiny;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "relationshipTextElement", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ItemRelationshipTextElement> itemRelationshipTextElements;

	@Transient
	private String contentItensRelationship;

	public RelationshipTextElement() {
		contentItensRelationship = "";
	}
	
	public String getContentItensRelationship() {
		return contentItensRelationship;
	}

	public void setContentItensRelationship(String contentItensRelationship) {
		this.contentItensRelationship = contentItensRelationship;
	}

	public Text getTextOrigin() {
		return textOrigin;
	}

	public void setTextOrigin(Text textOrigin) {
		this.textOrigin = textOrigin;
	}

	public Text getTextDestiny() {
		return textDestiny;
	}

	public void setTextDestiny(Text textDestiny) {
		this.textDestiny = textDestiny;
	}

	public List<ItemRelationshipTextElement> getItemRelationshipTextElements() {
		return itemRelationshipTextElements;
	}

	public void setItemRelationshipTextElements(
			List<ItemRelationshipTextElement> itemRelationshipTextElements) {
	}

}
