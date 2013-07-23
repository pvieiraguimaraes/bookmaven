package br.ueg.tcc.bookway.control;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.EnumUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import br.com.vexillum.control.UserBasicControl;
import br.com.vexillum.model.Category;
import br.com.vexillum.util.Message;
import br.com.vexillum.util.Return;
import br.ueg.tcc.bookway.model.UserBookway;
import br.ueg.tcc.bookway.model.enums.AreaOfInterest;
import br.ueg.tcc.bookway.model.enums.State;

@Service
@Scope("prototype")
public class UserBookwayControl extends UserBasicControl<UserBookway> {

	public UserBookwayControl() {
		super();
		classEntity = UserBookway.class;
	}

	@Override
	public UserBookway getUser(String name, String password) {
		UserBookway user = new UserBookway();
		user = (UserBookway) getUserByMail(name);
		if (user != null && user.getPassword().equals(password)
				&& user.isValidAccount())
			return user;
		return null;
	}

	public Return deleteAccount() {
		Return retDelete = new Return(true);
		entity.setSolicitationExclusion(new Date());
		retDelete.concat(doAction("save"));
		if (retDelete.isValid())
			retDelete.addMessage(new Message(null,
					"Conta Inativada com sucesso"));
		return retDelete;
	}

	public Return registerUser() {
		Return retRegister = new Return(true);
		entity = createUserBookway();
		retRegister.concat(doAction("save"));
		if (retRegister.isValid()) {
			sendValidationEmailAccount(entity.getEmail());
		}
		return retRegister;
	}

	private UserBookway createUserBookway() {
		UserBookway user = entity;
		user.setCategory(getCategoryByName("ROLE_USER"));
		return user;
	}

	private Category getCategoryByName(String name) {
		String hql = "from Category where name ='" + name + "'";
		data.put("sql", hql);
		Return retCategory = searchByHQL();
		return (Category) retCategory.getList().get(0);
	}

	private void sendValidationEmailAccount(String emailAdres) {
		// EmailManager email = new EmailManager("SimpleEmail");
		// email.sendEmail(emailAdres, "Ativação conta Bookway",
		// "Ative sua conta");
		// TODO Fazer com que o link de ativação seja enviado com um número de
		// ativação gerado.
	}

	public Return updateAccount() {
		Return retUpdate = new Return(true);
		retUpdate.concat(doAction("update"));
		return retUpdate;
	}// TODO Testar a msg de alterado com sucesso

	public List<AreaOfInterest> initListAreaOfInterest() {
		return EnumUtils.getEnumList(AreaOfInterest.class);
	}

	public List<State> initListState() {
		return EnumUtils.getEnumList(State.class);
	}

}
