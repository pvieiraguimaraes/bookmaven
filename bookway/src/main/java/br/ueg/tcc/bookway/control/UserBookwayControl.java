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

/**Controlador utilizado no caso de uso de Manter Pessoas, herda as funcionalidades do usu�rio b�sico da arquitetura
 * @author pedro
 *
 */
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
		if(ret.getList().isEmpty())
			return null;
		return (UserBookway) ret.getList().get(0);
	}

	/**M�todo utilizado para encontrar um usu�rio atrav�s de um c�digo �nico
	 * gerado para validar sua conta de usu�rio.
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
	
	/**M�todo utilizado para localizar um usu�rio atrav�s de seu ID
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

	/**Coloca a conta do usu�rio logado em estado inativo
	 * @return {@link Return}
	 */
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

	/**Deleta todos os elementos do usu�rio
	 * @return {@link Return}
	 */
	public Return deleteAllElementsUser() {
		Return ret = new Return(true);
		ret.concat(deletePermitedTexts());
		//TODO COlocar aqui todos os outros elementos que podem ser deletados.		
		ret.concat(doAction("delete"));
		return ret;
	}
	
	/** Verifica se exitem us�arios com pendentes de exlus�o de suas contas
	 * @return {@link Return}
	 */
	public Return chechUsersFordelete(){
		GregorianCalendar today = new GregorianCalendar();  
		today.add(Calendar.DAY_OF_MONTH, -30); 
		Date thisDay = today.getTime();
		
		String sql = "FROM UserBookway WHERE active = true AND solicitationExclusion <= '" + thisDay + "'";
		data.put("sql", sql);
		
		return searchByHQL();
	}

	/**Realiza a chamada do controlador de textos para remover somente os textos que s�o permitidos
	 * @return {@link Return}
	 */
	private Return deletePermitedTexts() {
		Return ret = new Return(true);
		TextControl control = SpringFactory.getController("textControl",
				TextControl.class, this.data);
		ret.concat(control.deleteElementsTextsOfUser());
		return ret;
	}

	/**Registra uma conta de usu�rio no sistema.
 	 * @return {@link Return}
	 */
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

	/**Cria um novo usu�rio padr�o para uso do sistema
	 * @return {@link UserBookway}
	 */
	private UserBookway createUserBookway() {
		UserBookway user = entity;
		user.setPassword(EncryptUtils.encryptOnSHA512(entity.getPassword()));
		user.setCategory(getCategoryByName("ROLE_USER"));
		user.setVerificationCode(generateCodeUserAction());
		return user;
	}

	/**Busca uma categoria de usu�rio
	 * @param name, nome par�metro da busca
	 * @return {@link Category}
	 */
	private Category getCategoryByName(String name) {
		String hql = "from Category where name ='" + name + "'";
		data.put("sql", hql);
		Return retCategory = searchByHQL();
		return (Category) retCategory.getList().get(0);
	}

	/**Cria uma mensagem HMTL para envio de um e-mail de acordo com os par�metros passados
	 * @param vocative, utilizado para iniciar o di�logo
	 * @param nameUser, nome da pessoa que deseja contactar
	 * @param message, mensagem que dever� ser transmitida
	 * @param link, um link para acesso
	 * @param action, a��o relacionada a mensagem que dever� ser enviada
	 * @param end, sauda��o do fim da mensagem
	 * @return uma mensagem de acordo com os par�metros passados
	 */
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

	/**Envia um email com uma mensagem de recebimento de solicita��o de exclus�o de conta
	 * @param emailAdres, email em quest�o
	 * @param nameUser, nome do usu�rio que se deseja contactar
	 * @param code, c�digo que ser� utilizado para valida��o
	 */
	private void sendDeleteAccountEmail(String emailAdres, String nameUser,
			String code) {
		EmailManager email = new EmailManager("HtmlEmail");
		String subject = "Solicita��o de exclus�o de conta Bookway recebida";
		String link = "<a href='http://localhost:8080/bookway/pages/validate.zul?code="
				+ code + "'>aqui</a>";
		String message = createMessageEmail(
				"Aten��o!",
				nameUser,
				"sua solicita��o de exclus�o de conta foi recebida e sua conta permanecer� inativada por 30 dias, " +
				"ap�s o t�rmino deste prazo todos os seus dados ser�o exclu�dos e n�o poder�o ser recuperados. ",
				link, "para recuperar sua conta",
				"Obrigado por utilizar os servi�os");
		email.sendEmail(emailAdres, subject, message);
	}

	/**Emite uma mensagem para valida��o de uma conta criada no sistema
	 * @param emailAdres, email para onde dever� ser enviado
	 * @param nameUser, nome do usu�rio que criou a conta
	 * @param code, c�digo para intera��o
	 */
	private void sendValidationEmailAccount(String emailAdres, String nameUser,
			String code) {
		EmailManager email = new EmailManager("HtmlEmail");
		String subject = "Conta criada com sucesso, Bem vindo(a) a Bookway!";
		String link = "<a href='http://localhost:8080/bookway/pages/validate.zul?code="
				+ code + "'>aqui</a>";
		String message = createMessageEmail("Parab�ns", nameUser,
				"agora voc� faz parte da melhor Rede Social do Brasil", link,
				"para ativar sua conta", "Bons Estudos com o");
		email.sendEmail(emailAdres, subject, message);
	}

	/**Atualiza a conta de um usu�rio
	 * @return {@link Return}
	 */
	public Return updateAccount() {
		Return retUpdate = new Return(true);
		retUpdate.concat(update());
		return retUpdate;
	}

	/**Lista os tipos de �rea de interesse que envolve a {@link AreaOfInterest}
	 * @return lista de {@link AreaOfInterest}
	 */
	public List<AreaOfInterest> initListAreaOfInterest() {
		return EnumUtils.getEnumList(AreaOfInterest.class);
	}

	/**Lista os estados existends
	 * @return lista de {@link State}
	 */
	public List<State> initListState() {
		return EnumUtils.getEnumList(State.class);
	}

	/**Gera um c�digo aleatorio para ser utilizado em opera��es diversas envolvendo usu�rios
	 * @return o codigo em quest�o
	 */
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

	/**Valida a conta do usu�rio de acordo com um codigo passado
	 * @return {@link Return}
	 */
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

	/**M�todo que realiza a troca de senha do usu�rio
	 * @return {@link Return}
	 */
	public Return changePasswordUser() {
		Return ret = new Return(true);
		entity.setPassword(EncryptUtils.encryptOnSHA512((String) data.get("newPassword")));
		ret.concat(update());
		return ret;
	}
}
