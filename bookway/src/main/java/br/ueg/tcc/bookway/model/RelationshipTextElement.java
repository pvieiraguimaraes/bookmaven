package br.ueg.tcc.bookway.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import br.com.vexillum.model.annotations.ValidatorClass;

@ValidatorClass(validatorClass = "br.ueg.tcc.bookway.control.validator.RelationshipTextElementValidator")
@SuppressWarnings("serial")
@Entity
@DiscriminatorValue("R")
public class RelationshipTextElement extends ItensOfStudy {

	private Text textOrigin;
	private Text textDestiny;
	private ElementText elementTextOrigin;
	
}
