package br.ueg.tcc.bookway.view.macros;

import org.zkoss.zk.ui.HtmlMacroComponent;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;

import br.ueg.tcc.bookway.model.Text;
import br.ueg.tcc.bookway.model.UserBookway;

/**
 * Componente utilizado para exibir os textos no caso de uso Manter Textos
 * 
 * @author pedro
 * 
 */
@SuppressWarnings("serial")
@VariableResolver(value = { org.zkoss.zkplus.spring.DelegatingVariableResolver.class })
public class ItemText extends HtmlMacroComponent {

	@Wire
	private Label userOwner;
	@Wire
	private Label titleText;
	@Wire
	private Label descriptionText;
	@Wire
	private Label idText;
	@Wire
	private Button btnStudy;
	@Wire
	private Button btnEdit;
	@Wire
	private Button btnExclude;
	@Wire
	private Button btnAdd;
	@Wire
	private Button btnAcquire;
	@Wire
	private Button btnRemove;
	@Wire
	private Button btnOpen;

	/**
	 * Construtor padr�o do componente
	 * 
	 * @param user
	 *            , usu�rio em quest�o
	 * @param text
	 *            , texto em quest�o
	 * @param has
	 *            , verdadeiro se o usu�rio j� possui o texto
	 * @param isTextReferenceMode
	 *            , verdadeiro se � modalidade de refer�ncia
	 */
	public ItemText(UserBookway user, Text text, boolean has,
			Boolean isTextReferenceMode) {
		compose();
		configureButtonsInComponent(user, text, has, isTextReferenceMode);
	}

	/**
	 * Configura a visibilidade os elementos do componente
	 * 
	 * @param user
	 *            , usu�rio em quest�o
	 * @param text
	 *            , texto de onde ser�o obtidos os dados
	 * @param has
	 *            , verdadeiro se o texto pertence ao usu�rio
	 * @param isTextReferenceMode
	 *            , verdadeiro se � modalidade de refer�ncia
	 */
	private void configureButtonsInComponent(UserBookway user, Text text,
			boolean has, Boolean isTextReferenceMode) {
		if (isTextReferenceMode != null && isTextReferenceMode)
			btnOpen.setVisible(true);
		else {
			if (text.getUserOwning() == null)
				btnAcquire.setVisible(true);
			if (user == text.getUserOwning()) {
				btnExclude.setVisible(true);
				btnEdit.setVisible(true);
				btnStudy.setVisible(true);
			} else {
				if (has) {
					btnStudy.setVisible(true);
					btnRemove.setVisible(true);
				} else
					btnAdd.setVisible(true);
			}
		}
	}

	public String getUser() {
		return userOwner.getValue();
	}

	public void setUser(String user) {
		userOwner.setValue(user);
	}

	public String getTitle() {
		return titleText.getValue();
	}

	public void setTitle(String title) {
		titleText.setValue(title);
	}

	public String getDescription() {
		return descriptionText.getValue();
	}

	public void setDescription(String description) {
		descriptionText.setValue(description);
	}

	public String getIdText() {
		return idText.getValue();
	}

	public void setIdText(String id) {
		idText.setValue(id);
	}
}
