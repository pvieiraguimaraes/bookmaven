package br.ueg.tcc.bookway.view.macros;

import org.zkoss.zk.ui.HtmlMacroComponent;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;

import br.ueg.tcc.bookway.model.UserBookway;

/**Componente utilizado para exibir os convites na p�gina inicial do usu�rio
 * @author pedro
 *
 */
@SuppressWarnings("serial")
@VariableResolver(value = { org.zkoss.zkplus.spring.DelegatingVariableResolver.class })
public class ItemFriend extends HtmlMacroComponent {

	@Wire
	private Label userName;
	
	@Wire
	private Label idUser;
	
	@Wire
	private Button btnAdd;
	
	@Wire
	private Button btnRemove;
	
	@Wire
	private Button btnCancelInvite;
	
	@Wire
	private Button btnAceptInvite;
	
	@Wire
	public Image imageFriend; 

	/**Contrutor padr�o
	 * @param user, usu�rio do convite
	 * @param has, verdadeiro caso exista amizade entre o usu�rio
	 * @param existInvite, o tipo de convite existente dever� ser "OTHER_INVITE" ou "MY_INVITE"
	 */
	public ItemFriend(UserBookway user, boolean has, String existInvite) {
		compose();
		setUser(user.getName());
		setIdUser(user.getId().toString());
		configureButtonsInComponent(user, has, existInvite);
	}

	/**Configura a visibilidade dos bot�es de acordo com os par�metros passados
	 * @param user, usu�rio em quest�o
	 * @param has, verdadeiro caso exista a rela��o de amizade
	 * @param existInvite, especif�ca o tipo do convite 
	 */
	private void configureButtonsInComponent(UserBookway user, boolean has, String existInvite) {
		if (has) {
			btnRemove.setVisible(true);
		} else {
			if (existInvite.equalsIgnoreCase("OTHER_INVITE")) {
				btnAceptInvite.setVisible(true);
			} else if (existInvite.equalsIgnoreCase("MY_INVITE")){
				btnCancelInvite.setVisible(true);
			} else
				btnAdd.setVisible(true);
		}
		
	}

	public String getUser() {
		return userName.getValue();
	}

	public void setUser(String user) {
		userName.setValue(user);
	}

	public String getIdUser() {
		return idUser.getValue();
	}

	public void setIdUser(String id) {
		idUser.setValue(id);
	}
}
