package br.ueg.tcc.bookway.view.composer;

import org.springframework.context.annotation.Scope;
import org.zkoss.zk.ui.Component;

import br.com.vexillum.util.ReflectionUtils;
import br.com.vexillum.util.SpringFactory;
import br.com.vexillum.view.CRUDComposer;
import br.ueg.tcc.bookway.control.AnnotationControl;
import br.ueg.tcc.bookway.model.Annotation;

@SuppressWarnings("serial")
@org.springframework.stereotype.Component
@Scope("prototype")
public class AnnotationComposer extends
		CRUDComposer<Annotation, AnnotationControl> {

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		loadBinder();
	}

	@Override
	public AnnotationControl getControl() {
		return SpringFactory.getController("annotationControl",
				AnnotationControl.class,
				ReflectionUtils.prepareDataForPersistence(this));
	}

	@Override
	public Annotation getEntityObject() {
		return new Annotation();
	}

}
