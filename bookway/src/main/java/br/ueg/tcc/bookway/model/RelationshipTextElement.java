package br.ueg.tcc.bookway.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import br.com.vexillum.model.annotations.ValidatorClass;

//@ValidatorClass(validatorClass = "br.ueg.tcc.bookway.control.validator.RelationshipTextElementValidator")
//@SuppressWarnings("serial")
//@Entity
//@DiscriminatorValue("R")
public class RelationshipTextElement extends ItensOfStudy {

	//TODO Ver como ficará a questão dos itens do relacionamento
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_text_origin", insertable = true, updatable = false, nullable = false)
	private Text textOrigin;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_text_destiny", insertable = true, updatable = false, nullable = false)
	private Text textDestiny;
	
	private ElementText elementTextOrigin;
	
}
