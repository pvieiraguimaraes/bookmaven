package br.ueg.tcc.bookway.control;

import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import br.com.vexillum.control.GenericControl;
import br.ueg.tcc.bookway.model.Study;
import br.ueg.tcc.bookway.model.UserBookway;

/**
 * Controle utilizado para executar as tarefas relativas ao caso de uso de Estudar um texto
 * @author pedro
 *
 */
@Service
@Scope("prototype")
public class StudyControl extends GenericControl<Study> {

	public StudyControl() {
		super(Study.class);
	}

	/**Realiza a busca de um estudo no sistema de acordo com os dados passados como par�metro
	 * @param study, entidade com dados preenchidos que servir� de par�metro para a busca de um estudo
	 * @return  {@link Study}
	 */
	public Study getThisStudy(Study study) {
		SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
		String sql = "FROM Study where dateStudy =  '" + spf.format(study.getDateStudy())
				+ "' and text = '" + study.getText().getId() + "' and userBookway = '"
				+ study.getUserBookway().getId() + "'";
		data.put("sql", sql);
		Study stu = (Study) searchByHQL().getList().get(0);
		return stu;
	}
	
	/**Verifica a exist�ncia de um estudo para um determinado texto realizado por um determinado usu�rio
	 * @param study, estudo que se deseja pesquisar sobre a exist�ncia
	 * @return  {@link Study}
	 */
	public Study checksExistenceStudy(Study study) {
		String sql = "FROM Study where text = '" + study.getText().getId()
				+ "' and userBookway = '" + study.getUserBookway().getId()
				+ "'";
		data.put("sql", sql);
		List<?> list = searchByHQL().getList();
		if (!list.isEmpty())
			return (Study) list.get(0);
		return null;
	}
	
	/**Busca todos os estudos realizados por um usu�rio em quest�o
	 * @param user, usu�rio par�metro da busca
	 * @return lista de  {@link Study}
	 */
	@SuppressWarnings("unchecked")
	public List<Study> getMyStudies(UserBookway user){
		String sql = "FROM Study WHERE id_user = '" + user.getId() + "'";
		data.put("sql", sql);
		return (List<Study>) searchByHQL().getList();
	}
}
