package br.ueg.tcc.bookway.view.composer;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Button;

import br.com.vexillum.util.HibernateUtils;
import br.com.vexillum.util.ReflectionUtils;
import br.com.vexillum.util.Return;
import br.com.vexillum.util.SpringFactory;
import br.ueg.tcc.bookway.control.MarkingControl;
import br.ueg.tcc.bookway.control.NavigationStudyControl;
import br.ueg.tcc.bookway.model.ElementText;
import br.ueg.tcc.bookway.model.ItemNavigationStudy;
import br.ueg.tcc.bookway.model.MarkingOfUser;
import br.ueg.tcc.bookway.model.Study;
import br.ueg.tcc.bookway.model.UserBookway;

/**
 * Classe controladora do ambiente de Navega��o de Texto
 * 
 * @author Pedro
 * 
 */
@SuppressWarnings("serial")
@org.springframework.stereotype.Component
@Scope("prototype")
public class NavigationStudyComposer
		extends
		BaseNavigationStudyComposer<ItemNavigationStudy, NavigationStudyControl> {

	/**
	 * Lista de marca��es do usu�rio
	 */
	private List<MarkingOfUser> markingOfUsers;

	public List<MarkingOfUser> getMarkingOfUsers() {
		return markingOfUsers;
	}

	public void setMarkingOfUsers(List<MarkingOfUser> markingOfUsers) {
		this.markingOfUsers = markingOfUsers;
	}

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);

		study = (Study) session.getAttribute("study");
		continueStudy = (Boolean) session.getAttribute("continueStudy");
		isTextReferenceMode = (Boolean) session
				.getAttribute("isTextReferenceMode");

		if (isTextReferenceMode == null)
			isTextReferenceMode = false;

		if (study != null)
			createAmbientStudy(HibernateUtils.materializeProxy(getControl()
					.getLevelText(study.getText().getRootLevelText().getId())));

		if (!isTextReferenceMode) {
			loadMarkingsOfUser();
			checkButtonMarking();
		}
		loadBinder();
	}

	/**
	 * Faz chamada ao controle para carregar todas as marca��es do usu�rio
	 */
	@SuppressWarnings("unchecked")
	private void loadMarkingsOfUser() {
		Return ret = getMarkingControl().getMarkingOfUser(
				(UserBookway) getUserLogged());
		if (ret.isValid()) {
			session.setAttribute("markingOfUsers",
					(List<MarkingOfUser>) ret.getList());
			setMarkingOfUsers((List<MarkingOfUser>) ret.getList());
		}
	}

	/**
	 * Verifica se existe marca��es criada para o usu�rio e se n�o existir
	 * desabilita o bot�o de inser��o de marca��es
	 */
	private void checkButtonMarking() {
		Button buttonMarking = (Button) getComponentById("fldMarking");
		if (!getMarkingOfUsers().isEmpty()) {
			if (buttonMarking != null)
				buttonMarking.setDisabled(false);
		} else if (buttonMarking != null)
			buttonMarking.setDisabled(true);
	}

	/**
	 * Instancia do controlador do caso de uso de Marca��es
	 * 
	 * @return nova inst�ncia do controlador {@link MarkingControl}
	 */
	public MarkingControl getMarkingControl() {
		return SpringFactory.getController("markingControl",
				MarkingControl.class, null);
	}

	@Override
	public NavigationStudyControl getControl() {
		return SpringFactory.getController("navigationStudyControl",
				NavigationStudyControl.class,
				ReflectionUtils.prepareDataForPersistence(this));
	}

	@Override
	public ItemNavigationStudy getEntityObject() {
		return new ItemNavigationStudy();
	}

	/**
	 * Realiza a chamada ao m�todo de compartilhamento no Facebook passando um
	 * conte�do a ser compartilhado
	 */
	public void shareInFacebook() {
		String message = "Est� estudando os itens \""
				+ extractContentForShare() + "\" do texto \""
				+ study.getText().getTitle() + "\"";
		postStatusMessageFacebook(message);
		getComponentById("selectSocialWeb").detach();
	}

	/**
	 * M�todo que extrai dos itens selecionados o texto que dever� ser
	 * compartilhado
	 * 
	 * @return texto a ser compartilhado
	 */
	private String extractContentForShare() {
		String itensSharing = "";
		for (ElementText elem : itensSelected) {
			itensSharing += elem.getValue() + " ";
		}
		return itensSharing;
	}

	/**
	 * Faz a chamada a um m�todo para compartilhamento no Twitter
	 */
	public void shareInTwitter() {
		String message = "Est� estudando os itens \""
				+ extractContentForShare() + "\" do texto \""
				+ study.getText().getTitle() + "\"";
		if (message.length() > 140)
			message.substring(0, 139);
		updateStatusTwitter(message);
		getComponentById("selectSocialWeb").detach();
	}

	@Override
	protected String getUpdatePage() {
		return null;
	}

	@Override
	protected String getDeactivationMessage() {
		return null;
	}
}
