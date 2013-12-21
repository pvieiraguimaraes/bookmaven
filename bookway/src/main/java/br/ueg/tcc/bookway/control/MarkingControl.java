package br.ueg.tcc.bookway.control;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import br.com.vexillum.control.GenericControl;
import br.ueg.tcc.bookway.model.Marking;

@Service
@Scope("prototype")
public class MarkingControl extends GenericControl<Marking> {

	public MarkingControl() {
		super(Marking.class);
	}

}
