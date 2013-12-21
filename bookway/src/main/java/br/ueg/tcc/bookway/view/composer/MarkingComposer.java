package br.ueg.tcc.bookway.view.composer;

import org.springframework.context.annotation.Scope;

import br.ueg.tcc.bookway.control.MarkingControl;
import br.ueg.tcc.bookway.model.Marking;

@SuppressWarnings("serial")
@org.springframework.stereotype.Component
@Scope("prototype")
public class MarkingComposer extends BaseComposer<Marking, MarkingControl> {

	@Override
	protected String getUpdatePage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getDeactivationMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MarkingControl getControl() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Marking getEntityObject() {
		// TODO Auto-generated method stub
		return null;
	}

}
