package br.ueg.tcc.bookway.control;

import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.EnumUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import br.com.vexillum.control.UserBasicControl;
import br.com.vexillum.control.manager.EmailManager;
import br.com.vexillum.model.Category;
import br.com.vexillum.util.EncryptUtils;
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
		String encriptyPass = EncryptUtils.encryptOnSHA512(password);
		if (user != null && user.getPassword().equals(encriptyPass)
				&& user.isValidAccount())
			return user;
		return null;
	}
	
	public UserBookway getUserByCode(String code){
		String hql = "from UserBookway where verificationCode ='" + code + "'";
		data.put("sql", hql);
		Return retCode = searchByHQL();
		if(retCode.getList() != null)
			return (UserBookway) retCode.getList().get(0);
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
			sendValidationEmailAccount(entity.getEmail(), entity.getName(), entity.getVerificationCode());
		}
		return retRegister;
	}

	private UserBookway createUserBookway() {
		UserBookway user = entity;
		user.setPassword(EncryptUtils.encryptOnSHA512(entity.getPassword()));
		user.setCategory(getCategoryByName("ROLE_USER"));
		user.setVerificationCode(generateCodeUserAction());
		return user;
	}

	private Category getCategoryByName(String name) {
		String hql = "from Category where name ='" + name + "'";
		data.put("sql", hql);
		Return retCategory = searchByHQL();
		return (Category) retCategory.getList().get(0);
	}

	private void sendValidationEmailAccount(String emailAdres, String nameUser, String code) {
		EmailManager email = new EmailManager("HtmlEmail");
		String subject = "Conta criada com sucesso!!";
		String link = "";
		String message = "<div><table width='650' align='center' style='font-size:14px' cellpadding='0' cellspacing='0'><tbody><tr>" +
				"<td height='50' bgcolor='#f4faff' align='center'><table width='95%'><tbody><tr><td align='left'>" +
				"<img src='http://premiumportal.com.br/images/bookway.png'></td></tr></tbody></table>" +
				"</td></tr><tr><td bgcolor='#f4faff' align='center'><table width='95%' cellpadding='30'><tbody><tr><td align='left'>" +
				"<font face='Lucida Grande, Segoe UI, Arial, Verdana, Lucida Sans Unicode, Tahoma, Sans Serif'>" +
				"Parabéns,<br><br>" + nameUser + ", agora você faz parte da melhor Rede Social do Brasil.<br>" +
				"<br>Clique <a href='localhost:8080/bookway/validate.zul?code=" + code + "' target='_blank'>aqui</a> para ativar sua conta.<br><br>Bons Estudos com o " +
				"<span class='il'>Bookway</span>!<br>- A equipe <span class='il'><br></font>" +
				"</td></tr><tr><td align='right'><span style='font-family:'Lucida Grande','Segoe UI',Arial,Verdana,'Lucida Sans Unicode',Tahoma" +
				",'Sans Serif';font-size:11px;color:#888'>&nbsp;2013&nbsp;<span class='il'>Bookway</span></span></td></tr></tbody></table></td>" +
				"</tr></tbody></table></div>";
		email.sendEmail(emailAdres, subject, message);
	}//TODO ALterar o caminho do logo quando estiver hospedado

	public Return updateAccount() {
		Return retUpdate = new Return(true);
		retUpdate.concat(doAction("saveEntity"));
		return retUpdate;
	}// TODO Testar a msg de alterado com sucesso

	public List<AreaOfInterest> initListAreaOfInterest() {
		return EnumUtils.getEnumList(AreaOfInterest.class);
	}

	public List<State> initListState() {
		return EnumUtils.getEnumList(State.class);
	}
	
	private String generateCodeUserAction(){
		Random rand = new Random();
		String code = "";
		do {
			for (int i=0; i < 10; i++) {
				int number = rand.nextInt(10);
				code += number;
			}
		} while(getUserByCode(code) != null);
		return code;
	}
	
	public Return validateAccountUser(String code){
		Return retValid = new Return(true);
		UserBookway user = new UserBookway();
		user = getUserByCode(code);
		user.setVerificationCode(null);
		user.setValidAccount(true);
		entity = user;
		retValid = doAction("saveEntity");
		return retValid;
	}

}
