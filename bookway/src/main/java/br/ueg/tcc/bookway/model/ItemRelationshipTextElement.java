package br.ueg.tcc.bookway.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import br.com.vexillum.model.CommonEntity;
import br.com.vexillum.model.annotations.Validate;
import br.com.vexillum.model.annotations.ValidatorClass;

/**
 * Entidade modelo de negócio utilizada no caso de uso Manter Relacionamento
 * entre trechos e textos, onde representa cada item do relacionamento relacionando-se com a classe
 * {@link RelationshipTextElement}
 * 
 * @author pedro
 * 
 */
@ValidatorClass(validatorClass = "br.ueg.tcc.bookway.control.validator.ItemRelationshipTextElementValidator")
@Entity
@SuppressWarnings("serial")
public class ItemRelationshipTextElement extends CommonEntity {

	@Validate(notNull = true)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_elementtext_origin", nullable = false)
	private ElementText elementTextOrign;

	@Validate(notNull = true)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_elementtext_destiny", nullable = false)
	private ElementText elementTextDestiny;

	@Validate(notNull = true)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_relationshiptextelement", nullable = false)
	private RelationshipTextElement relationshipTextElement;

	public RelationshipTextElement getRelationshipTextElement() {
		return relationshipTextElement;
	}

	public void setRelationshipTextElement(
			RelationshipTextElement relationshipTextElement) {
		this.relationshipTextElement = relationshipTextElement;
	}

	public ElementText getElementTextOrign() {
		return elementTextOrign;
	}

	public void setElementTextOrign(ElementText elementTextOrign) {
		this.elementTextOrign = elementTextOrign;
	}

	public ElementText getElementTextDestiny() {
		return elementTextDestiny;
	}

	public void setElementTextDestiny(ElementText elementTextDestiny) {
		this.elementTextDestiny = elementTextDestiny;
	}

}
