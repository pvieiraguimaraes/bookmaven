package br.ueg.tcc.bookway.control;

import java.util.ArrayList;
import java.util.Collection;
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
	private Return searchAnnotation() {
		Text text = (Text) data.get("selectedText");
		List<Study> studies = (List<Study>) data.get("myStudies");
		String title = entity.getTitle();
		
		List<Annotation> annotations = new ArrayList<Annotation>();
		String sql = "FROM Annotation WHERE";
		
		if(text != null){
//			sql += " id_study = '" + thisStudy.getId() + "'";
			if(!title.equalsIgnoreCase(""))
				sql += " AND title like '%" + title + "'";
		} else {
			for (Study study : studies) {
				sql += study.getId() +"' AND title like '%" + title + "%'";
				data.put("sql", sql);
				annotations.addAll((List<Annotation>) searchByHQL().getList());
			}
		}
		
			
			
		
		if (!title.equalsIgnoreCase("")) 
		if(text != null)
			sql += " AND id_text ";
		
		return null;
	}
}
