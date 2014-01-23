package br.ueg.tcc.bookway.view.macros;

import org.zkoss.zk.ui.HtmlMacroComponent;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;

import br.ueg.tcc.bookway.model.UserBookway;

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

	public ItemFriend(UserBookway user, boolean has) {
		compose();
		configureButtonsInComponent(user, has);
	}

	private void configureButtonsInComponent(UserBookway user, boolean has) {
		if (has) {
			btnRemove.setVisible(true);
		} else
			btnAdd.setVisible(true);
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
