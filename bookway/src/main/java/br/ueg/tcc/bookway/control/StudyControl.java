package br.ueg.tcc.bookway.control;

import java.text.SimpleDateFormat;

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

	public Study getThisStudy(Study study) {
		SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
		String sql = "FROM Study where dateStudy =  '" + spf.format(study.getDateStudy())
				+ "' and text = '" + study.getText().getId() + "' and userBookway = '"
				+ study.getUserBookway().getId() + "'";
		data.put("sql", sql);
		Study stu = (Study) searchByHQL().getList().get(0);
		return stu;
	}
}