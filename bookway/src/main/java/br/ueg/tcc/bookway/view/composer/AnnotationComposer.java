package br.ueg.tcc.bookway.view.composer;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.zkoss.zk.ui.Component;

import br.com.vexillum.util.ReflectionUtils;
import br.com.vexillum.util.Return;
import br.com.vexillum.util.SpringFactory;
import br.ueg.tcc.bookway.control.AnnotationControl;
import br.ueg.tcc.bookway.control.StudyControl;
import br.ueg.tcc.bookway.model.Annotation;
import br.ueg.tcc.bookway.model.Study;
import br.ueg.tcc.bookway.model.Text;
import br.ueg.tcc.bookway.model.UserBookway;
import br.ueg.tcc.bookway.model.enums.TypePrivacy;

@SuppressWarnings("serial")
@org.springframework.stereotype.Component
@Scope("prototype")
public class AnnotationComposer extends
		InitComposer<Annotation, AnnotationControl> {

	private String title;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		setMyStudies(getStudyControl().getMyStudies(
				(UserBookway) getUserLogged()));
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

	public List<TypePrivacy> getListTypesPrivacy() {
		return Arrays.asList(TypePrivacy.values());
	}

	@Override
	public Return saveEntity() {
		Study study = (Study) session.getAttribute("study");
		entity.setStudy(study);
		Return ret = super.saveEntity();
		if (ret.isValid())
			getComponentById("frmAnnotation").detach();
		return ret;
	}

	public void loadListText() {
		List<Study> studies = getMyStudies();
		List<Text> texts = getAllMyTexts();

		for (Study study : studies) {
			Text text = study.getText();
			if (!texts.contains(text)) {
				texts.remove(text);
			}
		}

		setAllMyTexts(texts);
	}

	private StudyControl getStudyControl() {
		StudyControl control = SpringFactory.getController("studyControl",
				StudyControl.class,
				ReflectionUtils.prepareDataForPersistence(this));
		return control;
	}

	@SuppressWarnings("unchecked")
	public Return searchAnnotation() {
		Return ret = getControl().doAction("searchAnnotation");
		if(ret.isValid() && !ret.getList().isEmpty()){
			setListEntity((List<Annotation>) ret.getList());
			getComponentById("resultList").setVisible(true);
			loadBinder();
		}
		return ret;
	}

}
