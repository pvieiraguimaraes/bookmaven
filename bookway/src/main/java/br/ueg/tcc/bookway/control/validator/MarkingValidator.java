package br.ueg.tcc.bookway.control.validator;

import java.util.Map;

import br.com.vexillum.control.validator.Validator;
import br.com.vexillum.util.Message;
import br.com.vexillum.util.Return;
import br.ueg.tcc.bookway.model.MarkingOfUser;

public class MarkingValidator extends Validator {

	public MarkingValidator(Map<String, Object> mapData) {
		super(mapData);
	}

	@Override
	public Return validateSave() {
		Return ret = super.validateSave();
		MarkingOfUser marking = (MarkingOfUser) entity;
		if (marking.getName() == null)
			ret.addMessage(new Message("name", messages.getKey("notnull_false")));
		return ret;
	}

}
