package br.ueg.tcc.bookway.control;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import br.com.vexillum.control.GenericControl;
import br.ueg.tcc.bookway.model.TagsOfMarking;

/**
 * Controlador utilizado para manter as Tags das Marcações
 * 
 * @author pedro
 * 
 */
@Service
@Scope("prototype")
public class TagsOfMarkingControl extends GenericControl<TagsOfMarking> {

	public TagsOfMarkingControl() {
		super(TagsOfMarking.class);
	}

}
