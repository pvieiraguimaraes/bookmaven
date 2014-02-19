package br.ueg.tcc.bookway.control;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import br.com.vexillum.control.GenericControl;
import br.com.vexillum.util.Return;
import br.ueg.tcc.bookway.model.ElementText;
import br.ueg.tcc.bookway.model.ElementsItensStudy;
import br.ueg.tcc.bookway.model.Study;

/**
 * Controlador para os Elementos inseridos no estudo
 * 
 * @author pedro
 * 
 */
@Service
@Scope("prototype")
public class ElementsItensStudyControl extends
		GenericControl<ElementsItensStudy> {

	public ElementsItensStudyControl() {
		super(ElementsItensStudy.class);
	}

	/**
	 * Método que localiza os elementos de estudo inseridos para um determinado
	 * elemento de texto em um estudo específico.
	 * 
	 * @return {@link Return}
	 */
	public Return searchExistingItensStudies() {
		ElementText elementText = (ElementText) data.get("checkElementText");
		Study study = (Study) data.get("checkStudy");

		String sql = "FROM ElementsItensStudy WHERE id_elementtext = '"
				+ elementText.getId() + "' AND id_study = '" + study.getId()
				+ "'";
		data.put("sql", sql);

		return searchByHQL();
	}

	/**
	 * Busca o elemento de estudo de acordo com um id oferecido do item de
	 * estudo correspondente.
	 * 
	 * @param id
	 *            , código do item de estudo específico
	 * @return {@link ElementsItensStudy} relacionado com o item de estudo em
	 *         questão.
	 */
	public ElementsItensStudy searchExistingItensById(String id) {
		String sql = "FROM ElementsItensStudy WHERE id_itemofstudy = '" + id
				+ "'";
		data.put("sql", sql);
		Return ret = searchByHQL();
		if (ret.isValid() && !ret.getList().isEmpty())
			return (ElementsItensStudy) ret.getList().get(0);
		return null;
	}

}
