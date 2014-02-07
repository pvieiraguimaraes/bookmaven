package br.ueg.tcc.bookway.control;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.EnumUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import br.com.vexillum.control.UserBasicControl;
import br.com.vexillum.control.manager.EmailManager;
import br.com.vexillum.model.Category;
import br.com.vexillum.model.UserBasic;
import br.com.vexillum.util.EncryptUtils;
import br.com.vexillum.util.Return;
import br.com.vexillum.util.SpringFactory;
import br.ueg.tcc.bookway.model.UserBookway;
import br.ueg.tcc.bookway.model.enums.AreaOfInterest;
import br.ueg.tcc.bookway.model.enums.State;

@Service
@Scope("prototype")
public class UserBookwayControl extends UserBasicControl<UserBookway> {

	public UserBookwayControl() {
		super(UserBookway.class);
	}
	
	@Override
	public UserBookway getUser(String name, String password) {
		UserBookway user = new UserBookway();
		user = getUserByMail(name);
		if (user != null && user.getPassword().equals(password) && user.getActive())
			return user;
		return null;
	}
	
	@Override
	public Category getCategoryUser(UserBasic user) {
		String hql = "FROM Category WHERE id in (select u.category.id from UserBookway as u where id ='"
				+ user.getId() + "')";
		data.put("sql", hql);
		Return ret = searchByHQL();
		return (Category) ret.getList().get(0);
	}
	
	@Override
	public UserBookway getUserByMail(String email) {
		String hql = "FROM UserBookway as u where email ='"
				+ email + "'";
		data.put("sql", hql);
		Return ret = searchByHQL();
		return (UserBookway) ret.getList().get(0);
	}

	/**Método utilizado para encontrar um usuário através de um código único
	 * gerado para validar sua conta de usuário.
	 * @param code
	 * @return
	 */
	public UserBookway getUserByCode(String code) {
		String hql = "from UserBookway where verificationCode ='" + code + "'";
		data.put("sql", hql);
		Return retCode = searchByHQL();
		if (retCode.getList() != null && !retCode.getList().isEmpty())
			return (UserBookway) retCode.getList().get(0);
		return null;
	}
	
	/**Método utilizado para localizar um usuário através de seu ID
	 * @param id
	 * @return
	 */
	public UserBookway getUserById(String id) {
		String hql = "from UserBookway where id ='" + id + "'";
		data.put("sql", hql);
		Return retCode = searchByHQL();
		if (retCode.getList() != null && !retCode.getList().isEmpty())
			return (UserBookway) retCode.getList().get(0);
		return null;
	}

	public Return deleteAccount() {
		Return retDelete = new Return(true);
		entity = (UserBookway) data.get("userLogged");
		entity.setSolicitationExclusion(new Date());
		entity.setVerificationCode(generateCodeUserAction());
		entity.setActive(false);
		retDelete.concat(update());
		
		if (retDelete.isValid()) {
			sendDeleteAccountEmail(entity.getEmail(), entity.getName(), entity.getVerificationCode());
		}
		return retDelete;
	}

	public Return deleteAllElementsUser() {
		Return ret = new Return(true);
		ret.concat(deletePermitedTexts());
		//TODO COlocar aqui todos os outros elementos que podem ser deletados.		
		ret.concat(doAction("delete"));
		return ret;
	}
	
	public Return chechUsersFordelete(){
		GregorianCalendar today = new GregorianCalendar();  
		today.add(Calendar.DAY_OF_MONTH, -30); 
		Date thisDay = today.getTime();
		
		String sql = "FROM UserBookway WHERE active = true AND solicitationExclusion <= '" + thisDay + "'";
		data.put("sql", sql);
		
		return searchByHQL();
	}

	private Return deletePermitedTexts() {
		Return ret = new Return(true);
		TextControl control = SpringFactory.getController("textControl",
				TextControl.class, this.data);
		ret.concat(control.deleteElementsTextsOfUser());
		return ret;
	}

	public Return registerUser() {
		Return retRegister = new Return(true);
		entity = createUserBookway();
		retRegister.concat(save());
		if (retRegister.isValid()) {
			sendValidationEmailAccount(entity.getEmail(), entity.getName(),
					entity.getVerificationCode());
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

	private String createMessageEmail(String vocative, String nameUser,
			String message, String link, String action, String end) {
		String text = "<div><table width='650' align='center' style='font-size:14px' cellpadding='0' cellspacing='0'><tbody><tr>"
				+ "<td height='50' bgcolor='#f4faff' align='center'><table width='95%'><tbody><tr><td align='left'>"
				+ "<img src='http://premiumportal.com.br/images/bookway.png'></td></tr></tbody></table>"
				+ "</td></tr><tr><td bgcolor='#f4faff' align='center'><table width='95%' cellpadding='30'><tbody><tr><td align='left'>"
				+ "<font face='Lucida Grande, Segoe UI, Arial, Verdana, Lucida Sans Unicode, Tahoma, Sans Serif'>"
				+ vocative
				+ ",<br><br>"
				+ nameUser
				+ ", "
				+ message
				+ ".<br>"
				+ "<br>Clique "
				+ link + " "
				+ action
				+ " .<br><br> "
				+ end
				+ " <span class='il'>Bookway</span>!<br>- A equipe <span class='il'><br></font>"
				+ "</td></tr><tr><td align='right'><span style='font-family:'Lucida Grande','Segoe UI',Arial,Verdana,'Lucida Sans Unicode',Tahoma"
				+ ",'Sans Serif';font-size:11px;color:#888'>&nbsp;2014&nbsp;<span class='il'>Bookway</span></span></td></tr></tbody></table></td>"
				+ "</tr></tbody></table></div>";
		return text;
	}

	private void sendDeleteAccountEmail(String emailAdres, String nameUser,
			String code) {
		EmailManager email = new EmailManager("HtmlEmail");
		String subject = "Solicitação de exclusão de conta Bookway recebida";
		String link = "<a href='http://localhost:8080/bookway/pages/validate.zul?code="
				+ code + "'>aqui</a>";
		String message = createMessageEmail(
				"Atenção!",
				nameUser,
				"sua solicitação de exclusão de conta foi recebida e sua conta permanecerá inativada por 30 dias, " +
				"após o término deste prazo todos os seus dados serão excluídos e não poderão ser recuperados. ",
				link, "para recuperar sua conta",
				"Obrigado por utilizar os serviços");
		email.sendEmail(emailAdres, subject, message);
	}

	private void sendValidationEmailAccount(String emailAdres, String nameUser,
			String code) {
		EmailManager email = new EmailManager("HtmlEmail");
		String subject = "Conta criada com sucesso, Bem vindo(a) a Bookway!";
		String link = "<a href='http://localhost:8080/bookway/pages/validate.zul?code="
				+ code + "'>aqui</a>";
		String message = createMessageEmail("Parabéns", nameUser,
				"agora você faz parte da melhor Rede Social do Brasil", link,
				"para ativar sua conta", "Bons Estudos com o");
		email.sendEmail(emailAdres, subject, message);
	}

	public Return updateAccount() {
		Return retUpdate = new Return(true);
		retUpdate.concat(update());
		return retUpdate;
	}

	public List<AreaOfInterest> initListAreaOfInterest() {
		return EnumUtils.getEnumList(AreaOfInterest.class);
	}

	public List<State> initListState() {
		return EnumUtils.getEnumList(State.class);
	}

	private String generateCodeUserAction() {
		Random rand = new Random();
		String code = "";
		do {
			for (int i = 0; i < 10; i++) {
				int number = rand.nextInt(10);
				code += number;
			}
		} while (getUserByCode(code) != null);
		return code;
	}

	public Return validateAccountUser() {
		String code = (String) data.get("code");
		Return retValid = new Return(true);
		entity = getUserByCode(code);
		if(entity == null){
			retValid.setValid(false);
			return retValid;
		}
		entity.setVerificationCode(null);
		entity.setActive(true);
		retValid.concat(update());
		return retValid;
	}

	public Return changePasswordUser() {
		Return ret = new Return(true);
		entity.setPassword(EncryptUtils.encryptOnSHA512((String) data.get("newPassword")));
		ret.concat(update());
		return ret;
	}
}
