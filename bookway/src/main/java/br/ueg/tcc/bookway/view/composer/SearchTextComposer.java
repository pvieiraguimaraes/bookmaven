package br.ueg.tcc.bookway.view.composer;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Checkbox;

import br.com.vexillum.util.ReflectionUtils;
import br.com.vexillum.util.Return;
import br.com.vexillum.util.SpringFactory;
import br.ueg.tcc.bookway.control.RelationshipTextUserControl;
import br.ueg.tcc.bookway.control.StudyControl;
import br.ueg.tcc.bookway.control.TextControl;
import br.ueg.tcc.bookway.model.Study;
import br.ueg.tcc.bookway.model.Text;
import br.ueg.tcc.bookway.model.UserBookway;
import br.ueg.tcc.bookway.model.enums.TypePrivacy;

/**
 * @author Pedro
 * 
 */
@SuppressWarnings("serial")
@org.springframework.stereotype.Component
@Scope("prototype")
public class SearchTextComposer extends InitComposer<Text, TextControl> {

	@Wire
	public Checkbox myTexts;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		String page = Executions.getCurrent().getDesktop().getRequestPath();
		if (myTexts != null
				&& page.equalsIgnoreCase("/pages/user/alltexts.zul")) {
			myTexts.setChecked(true);
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

	@SuppressWarnings("unchecked")
	public void searchText() {
		resetResultListText();
		if (myTexts.isChecked()) {
			setUpListTextInComponent(getAllMyTexts(), "resultSearch",
					getComponent(), "ItemText", false, null);
		} else {
			Return ret = new Return(true);
			entity.setCommunity(false);
			entity.setTypeText(TypePrivacy.PUBLICO);
			ret.concat(getControl().doAction("searchTexts"));
			List<Text> list = getControl().substractListText(getAllMyTexts(),
					(List<Text>) ret.getList());
			setUpListTextInComponent(list, "resultSearch", getComponent(),
					"ItemText", false, null);
		}
	}

	private void resetResultListText() {
		Component resultSearch = getComponentById(getComponent(),
				"resultSearch");
		if (resultSearch != null)
			Components.removeAllChildren(resultSearch);
	}

	/**
	 * Abre tela para edição do texto em questão
	 * 
	 * @param id
	 *            , do texto
	 */
	public void editText(String id) {
		Text text = getControl().getTextById(Long.parseLong(id));
		setSelectedText(text);
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
		session.setAttribute("textStudy", text);
		createObjectStudy(text);
		Executions.sendRedirect("/pages/user/study.zul");
	}

	private void createObjectStudy(Text text) {
		Study study = new Study();
		study.setDateStudy(new Date());
		study.setText(text);
		study.setUserBookway((UserBookway) getUserLogged());
		
		HashMap<String, Object> newMap = new HashMap<>();
		newMap.put("entity", study);
		
		StudyControl control = SpringFactory.getController("studyControl",
				StudyControl.class, newMap);
		
		Return ret = control.doAction("save");
		
		study = control.getThisStudy(study);
		
		if(ret.isValid())
			putStudyInSession(study);
	}

	private void putStudyInSession(Study study) {
		session.setAttribute("study", study);
	}

	/**
	 * Exibe mensagem de confirmação de exclusão, e em caso afirmativo exclui o
	 * texto do usuário removendo todos os itens relacionados a ele.
	 * 
	 * @param id, do texto
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

	public Return deleteText() {
		Return retDelete = new Return(true);
		retDelete.concat(getControl().doAction("deleteText"));
		return retDelete;
	}

	public Return disconnectionUserOfText() {
		Return ret = getControl().doAction("disconnectionUserOfText");
		return ret;
	}

	public void acquireText(String id) {
		entity = getControl().getTextById(Long.parseLong(id));
		treatReturn(getControl().doAction("updateUserOwnerText"));
	}

	public RelationshipTextUserControl getRelationControl() {
		return SpringFactory.getController("relationshipTextUserControl",
				RelationshipTextUserControl.class,
				ReflectionUtils.prepareDataForPersistence(this));
	}
}
