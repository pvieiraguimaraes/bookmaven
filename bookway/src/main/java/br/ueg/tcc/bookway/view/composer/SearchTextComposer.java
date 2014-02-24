package br.ueg.tcc.bookway.view.composer;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Checkbox;

import br.com.vexillum.util.ReflectionUtils;
import br.com.vexillum.util.Return;
import br.com.vexillum.util.SpringFactory;
import br.ueg.tcc.bookway.control.RelationshipTextUserControl;
import br.ueg.tcc.bookway.control.TextControl;
import br.ueg.tcc.bookway.model.Study;
import br.ueg.tcc.bookway.model.Text;
import br.ueg.tcc.bookway.model.UserBookway;
import br.ueg.tcc.bookway.model.enums.TypePrivacy;

/**
 * Classe responsável pela pesquisa de textos no sistema.
 * 
 * @author Pedro
 * 
 */
@SuppressWarnings("serial")
@org.springframework.stereotype.Component
@Scope("prototype")
public class SearchTextComposer extends InitComposer<Text, TextControl> {

	/**
	 * Se marcado sinaliza que a busca deverá ser realizada no textos
	 * pertencentes ao usuário
	 */
	@Wire
	public Checkbox myTexts;

	private boolean checkMyTexts;
	private boolean community;

	public boolean getCommunity() {
		return community;
	}

	public void setCommunity(boolean community) {
		this.community = community;
	}

	public boolean getCheckMyTexts() {
		return checkMyTexts;
	}

	public void setCheckMyTexts(boolean checkMyTexts) {
		this.checkMyTexts = checkMyTexts;
	}

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		String page = Executions.getCurrent().getDesktop().getRequestPath();
		if (myTexts != null
				&& (page.equalsIgnoreCase("/pages/user/alltexts.zul") || page
						.equalsIgnoreCase("/pages/user/study.zul"))) {
			myTexts.setChecked(true);
			// session.setAttribute("isTextReferenceMode", true);
			// setIsTextReferenceMode((Boolean)
			// session.getAttribute("isTextReferenceMode"));
			searchText();
		}
		createListTextUser();
		loadBinder();
	}

	@Override
	public TextControl getControl() {
		return SpringFactory.getController("textControl", TextControl.class,
				ReflectionUtils.prepareDataForPersistence(this));
	}

	@Override
	public Text getEntityObject() {
		return new Text();
	}

	/**
	 * Faz chamada ao controlador para pesquisar um texto de acordo com os
	 * parâmetros de busca informados
	 */
	@SuppressWarnings("unchecked")
	public void searchText() {
		Return ret = new Return(true);
		resetResultListSearch();
		if (myTexts.isChecked()) {
			setCheckMyTexts(true);
			if (chckCommunity.isChecked())
				setCommunity(true);
			else
				setCommunity(false);
			ret.concat(getControl().doAction("searchTexts"));
			ret.concat(getRelationControl().listTextAddOfUser());
			setUpListTextInComponent((List<Text>) ret.getList(),
					"resultSearch", getComponent(), "ItemText", false, null);
		} else {
			setCheckMyTexts(false);
			if (entity.getTypeText() == null)
				entity.setTypeText(TypePrivacy.PUBLICO);
			ret.concat(getControl().doAction("searchTexts"));
			setUpListTextInComponent((List<Text>) ret.getList(),
					"resultSearch", getComponent(), "ItemText", false, null);
		}
	}

	/**
	 * Abre tela para edição do texto em questão
	 * 
	 * @param id
	 *            , do texto
	 */
	public void editText(String id) {
		Text text = getControl().getTextById(Long.parseLong(id));
		session.setAttribute("thisEditText", text);
		callModalWindow("/pages/user/frmtext.zul");
	}

	/**
	 * Método que captura o texto que será estudado pelo usuário
	 * 
	 * @param id
	 *            , do texto
	 */
	public void studyText(String id) {
		Text text = getControl().getTextById(Long.parseLong(id));
		setStudy(checksExistenceStudy(text));

		if (study != null) {
			if (study.getLastElementStop() != null) {
				showActionConfirmationYesOrNo(
						"Deseja continuar o estudo de onde parou?",
						"continueStudy", "noContinueStudy");
			} else {
				putValuesInSession(getStudy(), getContinueStudy(), false);
				Executions.sendRedirect("/pages/user/study.zul");
			}
		} else {
			setStudy(createStudy(text));
			putValuesInSession(getStudy(), getContinueStudy(), false);
			Executions.sendRedirect("/pages/user/study.zul");
		}
	}

	/**
	 * Verifica se existe um estudo do usuário para aquele texto
	 * 
	 * @param text
	 * @return
	 */
	private Study checksExistenceStudy(Text text) {
		if (study == null)
			study = new Study();
		study.setText(text);
		study.setUserBookway((UserBookway) getUserLogged());

		return getStudyControl(null).checksExistenceStudy(getStudy());
	}

	/**
	 * Ação que será executada caso o usuário opte por continuar o estudo de
	 * onde parou
	 * 
	 * @return
	 */
	public Return continueStudy() {
		setContinueStudy(true);
		putValuesInSession(getStudy(), getContinueStudy(), false);
		Executions.sendRedirect("/pages/user/study.zul");
		return new Return(true);
	}

	/**
	 * Ação que será executada caso o usuário opte por não continuar o estudo de
	 * onde parou
	 * 
	 * @return
	 */
	public Return noContinueStudy() {
		setContinueStudy(false);
		putValuesInSession(getStudy(), getContinueStudy(), false);
		Executions.sendRedirect("/pages/user/study.zul");
		return new Return(true);
	}

	/**
	 * Coloca os atributos necessário na sessão para que possam ser passados
	 * para execução do próximo composer.
	 * 
	 * @param study
	 * @param continueStudy
	 * @param isTextReferenceMode
	 */
	private void putValuesInSession(Study study, Boolean continueStudy,
			Boolean isTextReferenceMode) {
		session.setAttribute("study", study);
		session.setAttribute("continueStudy", continueStudy);
		session.setAttribute("isTextReferenceMode", isTextReferenceMode);
	}

	/**
	 * Exibe mensagem de confirmação de exclusão, e em caso afirmativo exclui o
	 * texto do usuário removendo todos os itens relacionados a ele.
	 * 
	 * @param id
	 *            , do texto
	 */
	public void excludeText(String id) {
		Text text = getControl().getTextById(Long.parseLong(id));
		setSelectedText(text);
		boolean has = getRelationControl().verifyTextBelongsAnyUser();
		entity = text;
		if (!has)
			showActionConfirmation(
					messages.getKey("text_deletion_confirmation"), "deleteText");
		else
			showActionConfirmation(
					messages.getKey("text_disconnection_confirmation"),
					"disconnectionUserOfText");
	}

	/**
	 * Verifica se o checkbox meus textos está marcado e caso esteja habilita a
	 * visualização de filtros de busca adicionais
	 */
	public void checkIfMyTexts() {
		if (myTexts.isChecked()) {
			chckTypeText.setVisible(false);
			chckCommunity.setVisible(false);
		} else {
			chckTypeText.setVisible(true);
			chckCommunity.setVisible(true);
		}
	}

	/**
	 * Faz chamada ao controlador para excluir o texto
	 * 
	 * @return {@link Return}
	 */
	public Return deleteText() {
		Return retDelete = new Return(true);
		retDelete.concat(getControl().doAction("deleteText"));
		return retDelete;
	}

	/**
	 * Remove um relacionamento existente entre o usuário e um texto
	 * 
	 * @return {@link Return}
	 */
	public Return disconnectionUserOfText() {
		Return ret = getControl().doAction("disconnectionUserOfText");
		return ret;
	}

	/**
	 * Acessa o controlador e realiza um relacionamento entre o usuário e um
	 * texto
	 * 
	 * @param id
	 *            , código do texto alvo do relacionamento
	 */
	public void acquireText(String id) {
		entity = getControl().getTextById(Long.parseLong(id));
		treatReturn(getControl().doAction("updateUserOwnerText"));
	}

	/**
	 * Obtém uma instância do controlador de relacionamento entre textos e
	 * usuários
	 * 
	 * @return nova instância do controle {@link RelationshipTextUserControl}
	 */
	public RelationshipTextUserControl getRelationControl() {
		return SpringFactory.getController("relationshipTextUserControl",
				RelationshipTextUserControl.class,
				ReflectionUtils.prepareDataForPersistence(this));
	}

}
