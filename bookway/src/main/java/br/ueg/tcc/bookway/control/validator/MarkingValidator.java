package br.ueg.tcc.bookway.control.validator;

import java.util.Map;

import br.com.vexillum.control.validator.Validator;
import br.com.vexillum.util.Message;
import br.com.vexillum.util.Return;
import br.ueg.tcc.bookway.model.MarkingOfUser;

/**Classe responsável pela validação da entidade {@link Marking} do sistema
 * 
 * @author pedro
 *
 */
public class MarkingValidator extends Validator {

	public MarkingValidator(Map<String, Object> mapData) {
		super(mapData);
	}

	@Override
	public Return validateSave() {
		Return ret = super.validateSave();
		MarkingOfUser marking = (MarkingOfUser) entity;
		
		if(marking.getName().length() > 20)
			ret.concat(creatReturn("name",
					getValidationMessage("name", "max", false) + " 20"));
		
		if(marking.getName().length() < 2)
			ret.concat(creatReturn("name",
					getValidationMessage("name", "min", false) + " 2"));
		
		if (marking.getName() == null)
			ret.addMessage(new Message("name", messages.getKey("notnull_false")));
		return ret;
	}

}
