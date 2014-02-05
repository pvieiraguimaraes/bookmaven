package br.ueg.tcc.bookway.view.composer;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.zkoss.zk.ui.Component;

import br.com.vexillum.util.HibernateUtils;
import br.com.vexillum.util.ReflectionUtils;
import br.com.vexillum.util.Return;
import br.com.vexillum.util.SpringFactory;
import br.ueg.tcc.bookway.control.RelationshipTextElementControl;
import br.ueg.tcc.bookway.model.RelationshipTextElement;
import br.ueg.tcc.bookway.model.Study;
import br.ueg.tcc.bookway.model.Text;
import br.ueg.tcc.bookway.model.UserBookway;
import br.ueg.tcc.bookway.view.macros.ItemStudy;

@SuppressWarnings("serial")
public class RelationshipTextElementComposer
		extends
		BaseNavigationStudyComposer<RelationshipTextElement, RelationshipTextElementControl> {

	private String titleText;
	
	private Study studyOrign;
	
	public Study getStudyOrign() {
		return studyOrign;
	}

	public void setStudyOrign(Study studyOrign) {
		this.studyOrign = studyOrign;
	}

	public String getTitleText() {
		return titleText;
	}

	public void setTitleText(String titleText) {
		this.titleText = titleText;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);

		if(myTexts != null){
			myTexts.setChecked(true);
			myTexts.setDisabled(true);
		}
		
		if(itemStudiesSelected != null && itemStudiesSelected.isEmpty())
			itemStudiesSelected = (List<ItemStudy>) session.getAttribute("itensStudiesTextOrigin");
		
		study = (Study) session.getAttribute("study");
		continueStudy = (Boolean) session.getAttribute("continueStudy");
		isTextReferenceMode = (Boolean) session
				.getAttribute("isTextReferenceMode");

		if (isTextReferenceMode == null)
			isTextReferenceMode = false;

		if (study != null)
			createAmbientStudy(HibernateUtils
					.materializeProxy(getNavigationStudyControl(null)
							.getLevelText(
									study.getText().getRootLevelText().getId())));

		loadBinder();
	}

	@Override
	protected String getUpdatePage() {
		return null;
	}

	@Override
	protected String getDeactivationMessage() {
		return null;
	}

	@Override
	public RelationshipTextElementControl getControl() {
		return SpringFactory.getController("relationshipTextElementControl",
				RelationshipTextElementControl.class,
				ReflectionUtils.prepareDataForPersistence(this));
	}

	@Override
	public RelationshipTextElement getEntityObject() {
		return new RelationshipTextElement();
	}

	public void saveRelationship() {
		extractDataForEntity();
		entity.setDateItem(new Date());
//		ElementText
//		List<ItemStudy> itensOrign = itemStudiesSelected;
//		for (ItemStudy itemStudy : itensOrign) {
//			itemStudy.getIdElement()
//		}
//		List<ItemStudy> itensDestiny = itemStudiesSelectedForReference;

	}

	public void callThisTextForReference() {
		session.setAttribute("isTextReferenceMode", true);
		session.setAttribute("study", study);
		Text text = HibernateUtils.materializeProxy(study.getText());
		setTextOrign(text);
		setTextDestiny(text);
		setStudyOrign(study);
		callModalWindow("/template/frms/frmTextReference.zul");
	}
	
	public void callSearchTextForReference() {
		session.setAttribute("isTextReferenceMode", true);
		session.setAttribute("itensStudiesTextOrigin", itemStudiesSelected);
		setStudyOrign(study);
		callModalWindow("/template/frms/panelSearchText.zul");
	}

	private void extractDataForEntity() {
		entity.setStudy(getStudyOrign());
		entity.setTextOrigin(getTextOrign());
		entity.setTextDestiny(getTextDestiny());
	}
	
	@SuppressWarnings("unchecked")
	public void searchText() {
		Return ret = new Return(true);
		resetResultListSearch();
		Text text = new Text();
		text.setTitle(titleText);
		newMap = new HashMap<String, Object>();
		newMap.put("userLogged", getUserLogged());
		newMap.put("entity", text);
		newMap.put("checkMyTexts", true);
		newMap.put("community", false);
		ret.concat(getTextControl(newMap).doAction("searchTexts"));
		setUpListTextInComponent((List<Text>) ret.getList(), "resultSearch",
				getComponent(), "ItemText", false, null);
	}

	
	public void openText(String idText) {
		Text text = getTextControl(null).getTextById(Long.parseLong(idText));
		entity.setTextDestiny(text);
		Study study = new Study();
		study.setUserBookway((UserBookway) getUserLogged());
		study.setText(text);
		study = getStudyControl(null).checksExistenceStudy(study);
		if (study.getId() != null)
			setStudy(study);
		else
			setStudy(createStudy(text));
		setTextDestiny(HibernateUtils.materializeProxy(getStudy().getText()));
		callThisTextForReference();
	}

}
