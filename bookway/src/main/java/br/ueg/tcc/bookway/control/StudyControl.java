package br.ueg.tcc.bookway.control;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import br.com.vexillum.control.GenericControl;
import br.ueg.tcc.bookway.model.Study;

@Service
@Scope("prototype")
public class StudyControl extends GenericControl<Study> {

	public StudyControl() {
		super(Study.class);
	}
}
