package br.ueg.tcc.bookway.control;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.EnumUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import br.com.vexillum.control.GenericControl;
import br.com.vexillum.util.Message;
import br.com.vexillum.util.Return;
import br.ueg.tcc.bookway.model.UserBookway;
import br.ueg.tcc.bookway.model.enums.AreaOfInterest;
import br.ueg.tcc.bookway.model.enums.State;

@Service
@Scope("prototype")
public class UserControl extends GenericControl<UserBookway> {

	public UserControl() {
		super();
		classEntity = UserBookway.class;
	}

	public Return deleteAccount() {
		Return retDelete = new Return(true);
		entity.setSolicitationExclusion(new Date());
		retDelete.concat(doAction("save"));
		if(retDelete.isValid())
			retDelete.addMessage(new Message(null, "Conta Inativada com sucesso"));
		return retDelete;
	}

	public Return registerUser() {
		//TODO Chamar a valida��o de email �nico aqui, e se o email � valido
		//TODO Chamar a valida��o para confirmar a senha aqui
		Return retRegister = new Return(true);
		retRegister.concat(doAction("save")); 
		if (retRegister.isValid()){
			sendValidationEmailAccount(entity.getEmail());
		}
		return retRegister;
	}

	private void sendValidationEmailAccount(String emailAdres) {
//		EmailManager email = new EmailManager("SimpleEmail");
//		email.sendEmail(emailAdres, "Ativa��o conta Bookway", "Ative sua conta");
		//TODO Fazer com que o link de ativa��o seja enviado com um n�mero de ativa��o gerado. 
	}

	public Return updateAccount(){
		Return retUpdate = new Return(true);
		retUpdate.concat(doAction("update"));
		if(retUpdate.isValid())
			retUpdate.addMessage(new Message(null, "Dados alterados com sucesso"));
		return retUpdate;
	}

	public List<AreaOfInterest> initListAreaOfInterest() {
		return EnumUtils.getEnumList(AreaOfInterest.class);
	}
	
	public List<State> initListState(){
		return EnumUtils.getEnumList(State.class);
	}
	
}
