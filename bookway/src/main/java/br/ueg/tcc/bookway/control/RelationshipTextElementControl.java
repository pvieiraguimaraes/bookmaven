package br.ueg.tcc.bookway.control;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import br.com.vexillum.control.GenericControl;
import br.ueg.tcc.bookway.model.RelationshipTextElement;

@Service
@Scope("prototype")
public class RelationshipTextElementControl extends GenericControl<RelationshipTextElement> {

	public RelationshipTextElementControl() {
		super(RelationshipTextElement.class);
	}

}
