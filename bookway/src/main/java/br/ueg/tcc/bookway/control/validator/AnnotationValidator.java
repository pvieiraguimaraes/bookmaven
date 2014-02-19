package br.ueg.tcc.bookway.control.validator;

import java.util.Map;

import br.com.vexillum.control.validator.Validator;
import br.com.vexillum.util.Return;
import br.ueg.tcc.bookway.model.Text;

/**
 * Classe responsável pela validação da entidade {@link Annotation} do sistema
 * 
 * @author pedro
 * 
 */
public class AnnotationValidator extends Validator {

	public AnnotationValidator(Map<String, Object> mapData) {
		super(mapData);
	}

	/**
	 * valida se os parâmetros mínimos para realizar uma busca de uma anotação
	 * estão preenchidos
	 * 
	 * @return {@link Return}
	 */
	public Return validateSearchAnnotation() {
		Return ret = new Return(true);

		String title = (String) mapData.get("title");
		Text selectedText = (Text) mapData.get("selectedText");

		if (title == null && selectedText == null)
			ret.concat(creatReturn(null,
					getValidationMessage("paramsnull", "search", false)));
		return ret;
	}

}
