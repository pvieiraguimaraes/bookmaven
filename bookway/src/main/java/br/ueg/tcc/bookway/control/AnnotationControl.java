package br.ueg.tcc.bookway.control;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import br.com.vexillum.control.GenericControl;
import br.com.vexillum.util.Return;
import br.ueg.tcc.bookway.model.Annotation;
import br.ueg.tcc.bookway.model.Study;
import br.ueg.tcc.bookway.model.Text;

@Service
@Scope("prototype")
public class AnnotationControl extends GenericControl<Annotation> {

	public AnnotationControl() {
		super(Annotation.class);
	}

	@SuppressWarnings("unchecked")
	public Return searchAnnotation() {
		Text text = (Text) data.get("selectedText");
		List<Study> studies = (List<Study>) data.get("myStudies");
		String title = (String) data.get("title");

		List<Annotation> annotations = new ArrayList<Annotation>();
		String sql = "";

		if (text != null) {
			for (Study study : studies) {
				if (study.getText() != text) {
					studies.remove(study);
				}
			}
			for (Study study : studies) {
				sql = "FROM Annotation WHERE id_study = '" + study.getId()
						+ "'";
				if (title != null)
					sql += " AND title like '%" + title + "%'";
				data.put("sql", sql);
				annotations.addAll((List<Annotation>) searchByHQL().getList());
			}
		} else if (text == null & text != null) {
			sql += "FROM Annotation WHERE title like '%" + title + "%'";
			data.put("sql", sql);
			annotations.addAll((List<Annotation>) searchByHQL().getList());
		}

		return new Return(true, annotations);
	}
}
