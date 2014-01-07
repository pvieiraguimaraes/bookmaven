package br.ueg.tcc.bookway.control;

import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import br.com.vexillum.control.GenericControl;
import br.ueg.tcc.bookway.model.Study;
import br.ueg.tcc.bookway.model.UserBookway;

@Service
@Scope("prototype")
public class StudyControl extends GenericControl<Study> {

	public StudyControl() {
		super(Study.class);
	}

	public Study getThisStudy(Study study) {
		SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
		String sql = "FROM Study where dateStudy =  '" + spf.format(study.getDateStudy())
				+ "' and text = '" + study.getText().getId() + "' and userBookway = '"
				+ study.getUserBookway().getId() + "'";
		data.put("sql", sql);
		Study stu = (Study) searchByHQL().getList().get(0);
		return stu;
	}
	
	public Study getThisStudy() {
		String sql = "FROM Study where text = '" + entity.getText().getId() + "' and userBookway = '"
				+ entity.getUserBookway().getId() + "'";
		data.put("sql", sql);
		Study stu = (Study) searchByHQL().getList().get(0);
		return stu;
	}
	
	@SuppressWarnings("unchecked")
	public List<Study> getMyStudies(UserBookway user){
		String sql = "FROM Study WHERE id_user = '" + user.getId() + "'";
		data.put("sql", sql);
		return (List<Study>) searchByHQL().getList();
	}
}
