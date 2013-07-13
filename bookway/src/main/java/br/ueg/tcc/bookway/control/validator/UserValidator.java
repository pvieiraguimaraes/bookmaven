package br.ueg.tcc.bookway.control.validator;

import java.util.HashMap;
import java.util.Map;

import br.com.vexillum.control.validator.Validator;
import br.com.vexillum.util.Return;
import br.com.vexillum.util.SpringFactory;
import br.ueg.tcc.bookway.control.UserControl;
import br.ueg.tcc.bookway.model.UserBookway;

public class UserValidator extends Validator {

	public UserValidator(Map<String, Object> mapData) {
		super(mapData);
	}

	private Return existsEmail() {
		Return ret = new Return(true);
		HashMap<String, Object> data = new HashMap<String, Object>();

		data.put("sql", "FROM User u WHERE u.email = '"
				+ ((UserBookway) entity).getEmail() + "'");

		UserControl controller = SpringFactory.getController("userControl",
				UserControl.class, data);
		if (!controller.searchByHQL().getList().isEmpty()) {
			ret.concat(creatReturn("email",
					getValidationMessage("email", "exists", true)));
		}
		return ret;
	}

	private Return equalsSenha() {
		Return ret = new Return(true);
		if (!equalsFields(((UserBookway) entity).getPassword(),
				(String) mapData.get("cSenha")).isValid()) {
			ret.concat(creatReturn("cSenha",
					getValidationMessage("senha", "equals", false)));
		}
		return ret;
	}

}