package br.ueg.tcc.bookway.control;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import br.com.vexillum.control.GenericControl;
import br.ueg.tcc.bookway.model.Annotation;

@Service
@Scope("prototype")
public class AnnotationControl extends GenericControl<Annotation>{

	public AnnotationControl() {
		super(Annotation.class);
	}

}
