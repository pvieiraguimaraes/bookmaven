package br.ueg.tcc.bookway.control;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import br.com.vexillum.control.GenericControl;
import br.com.vexillum.util.Return;
import br.ueg.tcc.bookway.model.Annotation;
import br.ueg.tcc.bookway.model.Study;
import br.ueg.tcc.bookway.model.Text;

/**
 * Controlador responsável pelas regras de negócio do caso de uso Manter
 * Anotações.
 * 
 * @author pedro
 * 
 */
@Service
@Scope("prototype")
public class AnnotationControl extends GenericControl<Annotation> {

	public AnnotationControl() {
		super(Annotation.class);
	}

	/**
	 * Metodo que realiza a busca de uma anotação de acordo com o texto
	 * selecionado e com o título da anotação inserido para a busca
	 * 
	 * @return {@link Return}
	 */
	@SuppressWarnings("unchecked")
	public Return searchAnnotation() {
		Text text = (Text) data.get("selectedText");
		List<Study> studyAux = new ArrayList<>(), studies = (List<Study>) data
				.get("myStudies");
		String title = (String) data.get("title");

		List<Annotation> annotations = new ArrayList<Annotation>();
		String sql = "";

		if (text != null) {
			for (Study study : studies) {
				if (study.getText() == text)
					studyAux.add(study);
			}
			for (Study study : studyAux) {
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

	/**
	 * Seleciona uma anotação de acordo com os dados da {@link Annotation}
	 * 
	 * @param annotation
	 *            , a ser buscada no banco de dados
	 * @return {@link Return}
	 */
	public Return searchThisAnnotation(Annotation annotation) {
		SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
		String sql = "FROM Annotation WHERE id_study = '"
				+ annotation.getStudy().getId() + "' AND dateItem = '"
				+ spf.format(annotation.getDateItem()) + "'";
		data.put("sql", sql);
		return searchByHQL();
	}
}
