package br.ueg.tcc.bookway.control;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import br.com.vexillum.control.GenericControl;
import br.ueg.tcc.bookway.model.ElementsItensStudy;

@Service
@Scope("prototype")
public class ElementsItensStudyControl extends GenericControl<ElementsItensStudy> {

	public ElementsItensStudyControl() {
		super(ElementsItensStudy.class);
	}

}
