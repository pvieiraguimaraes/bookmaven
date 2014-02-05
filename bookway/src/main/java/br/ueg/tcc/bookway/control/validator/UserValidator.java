package br.ueg.tcc.bookway.control.validator;

import java.util.HashMap;
import java.util.Map;

import br.com.vexillum.control.validator.Validator;
import br.com.vexillum.util.DateCalc;
import br.com.vexillum.util.Return;
import br.com.vexillum.util.SpringFactory;
import br.ueg.tcc.bookway.control.UserBookwayControl;
import br.ueg.tcc.bookway.model.UserBookway;

public class UserValidator extends Validator {

	public UserValidator(Map<String, Object> mapData) {
		super(mapData);
	}

	public Return validateRegisterUser() {
		Return retCheck = new Return(true);
		UserBookway user = (UserBookway) entity;
		retCheck.concat(validateModel());
		retCheck.concat(equalsSenha());
		retCheck.concat(existsEmail(user.getEmail()));
		return retCheck;
	}
	
	@Override
	protected Return validateModel() {
		Return ret = super.validateModel();
		UserBookway user = (UserBookway) entity;
		if (user.getBirthDate() != null)
			ret.concat(permitedAge());
		return ret;
	}
	
	public Return validateUpdateAccount(){
		return validateModel();
	}

	private Return existsEmail(String email) {
		Return ret = new Return(true);
		HashMap<String, Object> data = new HashMap<String, Object>();

		data.put("sql", "FROM UserBookway u WHERE u.email = '"
				+ email + "'");

		UserBookwayControl controller = getUserController(data);
		if (!controller.searchByHQL().getList().isEmpty()) {
			ret.concat(creatReturn("email",
					getValidationMessage("email", "exists", true)));
		}
		return ret;
	}

	private UserBookwayControl getUserController(HashMap<String, Object> data) {
		UserBookwayControl controller = SpringFactory.getController(
				"userBookwayControl", UserBookwayControl.class, data);
		return controller;
	}
	
	private Return permitedAge(){
		Return ret = new Return(true);
		
		Integer age = DateCalc.calculateAge(((UserBookway) entity).getBirthDate());
		if(age < 18)
			ret.concat(creatReturn("birthDate",
					getValidationMessage("birthdate", "permited", false)));
		return ret;
	}

	private Return equalsSenha() {
		Return ret = new Return(true);
		String password = ((UserBookway) entity).getPassword();
		String confirmPassword = ((UserBookway) entity).getConfirmPassword();
		if (!equalsFields(password, confirmPassword).isValid()) {
			ret.concat(creatReturn("confirmPassword",
					getValidationMessage("password", "equals", false)));
		}
		return ret;
	}

	public Return validateChangePasswordUser() {
		Return ret = new Return(true);
		String password = ((UserBookway) entity).getPassword();
		String actualPassword = (String) mapData.get("actualPassword");
		String newPassword = (String) mapData.get("newPassword");
		String confirmNewPassword = (String) mapData.get("confirmNewPassword");
		
				
		if (actualPassword == null || actualPassword.equalsIgnoreCase(""))
			ret.concat(creatReturn("actualPassword",
					getValidationMessage("", "notnull", false)));
		if (newPassword == null || newPassword.equalsIgnoreCase(""))
			ret.concat(creatReturn("newPassword",
					getValidationMessage("", "notnull", false)));
		if (confirmNewPassword == null || confirmNewPassword.equalsIgnoreCase(""))
			ret.concat(creatReturn("confirmNewPassword",
					getValidationMessage("", "notnull", false)));
		if (!equalsFields(password, actualPassword).isValid()) 
				ret.concat(creatReturn("actualPassword",
						getValidationMessage("actualPassword", "equals", false)));
		if (!equalsFields(newPassword, confirmNewPassword).isValid())
				ret.concat(creatReturn("confirmNewPassword",
						getValidationMessage("password", "equals", false)));
	
		if(newPassword != null && newPassword.length() < 6)
			ret.concat(creatReturn("newPassword",
					getValidationMessage("password", "min", false) + " 6"));
		if(confirmNewPassword != null && confirmNewPassword.length() < 6)
			ret.concat(creatReturn("confirmNewPassword",
					getValidationMessage("password", "min", false) + " 6"));
		return ret;
	}
}