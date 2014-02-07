package br.ueg.tcc.bookway.view.composer;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;

import br.com.vexillum.util.HibernateUtils;
import br.com.vexillum.util.ReflectionUtils;
import br.com.vexillum.util.Return;
import br.com.vexillum.util.SpringFactory;
import br.ueg.tcc.bookway.control.RelationshipTextElementControl;
import br.ueg.tcc.bookway.model.ElementText;
import br.ueg.tcc.bookway.model.ElementsItensStudy;
import br.ueg.tcc.bookway.model.ItemRelationshipTextElement;
import br.ueg.tcc.bookway.model.RelationshipTextElement;
import br.ueg.tcc.bookway.model.Study;
import br.ueg.tcc.bookway.model.Text;
import br.ueg.tcc.bookway.model.UserBookway;
import br.ueg.tcc.bookway.model.enums.TypePrivacy;
import br.ueg.tcc.bookway.view.macros.ItemStudy;

@SuppressWarnings("serial")
public class RelationshipTextElementComposer
		extends
		BaseNavigationStudyComposer<RelationshipTextElement, RelationshipTextElementControl> {

	private String titleText;
	
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
							.getLevelText(study.getText().getRootLevelText().getId())));
		
//		List<ItemRelationshipTextElement> itens = (List<ItemRelationshipTextElement>) session.getAttribute("yetSelectedRelationshipItens");
//		if(itens != null && !itens.isEmpty())
//			
//			changeItemStyle(itemStudy);
			
		loadBinder();
	}

	@Override
	protected String getUpdatePage() {
		return null;
	}

	@Override
	protected String getDeactivationMessage() {
		return "Deseja realmente excluir esta referência?";
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
		Return ret = new Return(true), retAux = new Return(true);
		RelationshipTextElement relationshipTextElement;
		
		entity.setDateItem(new Date());
		extractDataForEntity();
		List<ElementText> elementsOrign = extractElementsText(itemStudiesSelected);
		List<ElementText> elementsDestuny = extractElementsText(itemStudiesSelectedForReference);
		List<ItemRelationshipTextElement> itensRelation = generateItensRelationshipTextElement(elementsOrign, elementsDestuny);
		entity.setItemRelationshipTextElements(itensRelation);
		relationshipTextElement = entity;
		
		ret.concat(getControl().doAction("save"));
		if(ret.isValid()){
			entity = getRelationshipTextElement(relationshipTextElement);
			retAux.concat(createElementsItensStudy(elementsOrign, entity, entity.getStudy()));		
			retAux.concat(createElementsItensStudy(elementsDestuny, entity, (Study) session.getAttribute("studyDestiny")));
		}
		
		if(retAux.isValid()){
			createIconsStudy(itemStudiesSelected);
			createIconsStudy(itemStudiesSelectedForReference);
		}
		
		detachPanels();
		resetStyleAllItens();
		treatReturn(ret);
		loadBinder();
	}

	@SuppressWarnings("rawtypes")
	private void resetStyleAllItens() {
		resetStyleItens();
		BaseComposer composer = getParentComposer();
		if(composer != null)
			composer.resetStyleItens();
		composer = composer.getParentComposer();
		if(composer != null)
			composer.resetStyleItens();
		composer = getNavigationStudyComposer();
		if(composer != null)
			composer.resetStyleItens();
	}

	private void detachPanels() {
		Component component = getComponentById("textReference");
		if (component != null)
			component.detach();
		component = getComponentById("panelSearchText");
		if (component != null)
			component.detach();
		component = getComponentById("selectTextReference");
		if (component != null)
			component.detach();
		
		component = getParentComposer().getComponentById("panelSearchText");
		if (component != null)
			component.detach();
	}

	private RelationshipTextElement getRelationshipTextElement(
			RelationshipTextElement relationshipTextElement) {
		Return ret = new Return(true);
		ret.concat(getControl().searchThisRelationship(relationshipTextElement));
		if(ret.isValid())
			return (RelationshipTextElement) ret.getList().get(0);
		return null;
	}

	private Return createElementsItensStudy(List<ElementText> list, RelationshipTextElement relation, Study study) {
		ElementsItensStudy elementsItensStudy;
		
		for (ElementText elementText : list) {
			elementsItensStudy = new ElementsItensStudy();
			elementsItensStudy.setElementText(elementText);
			elementsItensStudy.setItemOfStudy(relation);
			elementsItensStudy.setStudy(study);
			elementsItensStudies.add(elementsItensStudy);
		}
		
		return saveElementsItensStudy();
	}

	private List<ItemRelationshipTextElement> generateItensRelationshipTextElement(
			List<ElementText> elementsOrign, List<ElementText> elementsDestuny) {
		List<ItemRelationshipTextElement> list = new ArrayList<ItemRelationshipTextElement>();
		ItemRelationshipTextElement item;
		for (ElementText elementOrign : elementsOrign) {
			for (ElementText elementDestiny : elementsDestuny) {
				item = new ItemRelationshipTextElement();
				item.setElementTextOrign(elementOrign);
				item.setElementTextDestiny(elementDestiny);
				item.setRelationshipTextElement(entity);
				list.add(item);
			}
		}
		return list;
	}

	private List<ElementText> extractElementsText(List<ItemStudy> itens) {
		List<ElementText> list = new ArrayList<ElementText>();
		ElementText elementText;
		for (ItemStudy itemStudy : itens) {
			elementText = new ElementText();
			elementText = getNavigationStudyControl(null).getElementText(
					Long.parseLong(itemStudy.getIdElement()));
			list.add(HibernateUtils.materializeProxy(elementText));
		}
		return list;
	}

	private void extractDataForEntity() {
		entity.setStudy((Study) session.getAttribute("studyOrign"));
		entity.setTextOrigin((Text) session.getAttribute("textOrign"));
		entity.setTextDestiny((Text) session.getAttribute("textDestiny"));
		entity.setTypePrivacy((TypePrivacy) session.getAttribute("typePrivacy"));
	}

	public void callThisTextForReference() {
		session.setAttribute("studyOrign", study);
		session.setAttribute("studyDestiny", study);
		session.setAttribute("textOrign", HibernateUtils.materializeProxy(study.getText()));
		session.setAttribute("textDestiny", HibernateUtils.materializeProxy(study.getText()));
		session.setAttribute("typePrivacy", getTypePrivacy());
		openTextForStudy();
	}

	private void openTextForStudy() {
		session.setAttribute("isTextReferenceMode", true);
		session.setAttribute("study", study);
		callModalWindow("/template/frms/frmTextReference.zul");
	}
	
	public void callSearchTextForReference() {
		session.setAttribute("isTextReferenceMode", true);
		session.setAttribute("itensStudiesTextOrigin", itemStudiesSelected);
		session.setAttribute("studyOrign", study);
		session.setAttribute("textOrign", HibernateUtils.materializeProxy(study.getText()));
		session.setAttribute("typePrivacy", getTypePrivacy());
		callModalWindow("/template/frms/panelSearchText.zul");
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
		Study study = new Study();
		study.setUserBookway((UserBookway) getUserLogged());
		study.setText(text);
		study = getStudyControl(null).checksExistenceStudy(study);
		if (study.getId() != null)
			setStudy(study);
		else
			setStudy(createStudy(text));
		session.setAttribute("studyDestiny", getStudy());
		session.setAttribute("textDestiny", text);
		openTextForStudy();
	}
	
	@SuppressWarnings("unchecked")
	public Return searchRelationship(){
		Return ret = getControl().doAction("searchRelationship");
		List<RelationshipTextElement> resultList = null;
		if (ret.isValid() && !ret.getList().isEmpty()) {
			resultList  = (List<RelationshipTextElement>) ret.getList();
			resultList = generateListRelationship(resultList);
			setListEntity(resultList);
			getComponentById("resultList").setVisible(true);
			loadBinder();
		}
		return ret;
	}
	
	private List<RelationshipTextElement> generateListRelationship(
			List<RelationshipTextElement> resultList) {
		String auxDest = "", auxOrig = "";
		for (RelationshipTextElement relationshipTextElement : resultList) {
			List<ItemRelationshipTextElement> itemRelationshipTextElements = relationshipTextElement
					.getItemRelationshipTextElements();
			if (itemRelationshipTextElements != null
					&& !itemRelationshipTextElements.isEmpty()) {
				for (ItemRelationshipTextElement itemRelationshipTextElement : itemRelationshipTextElements) {
					auxOrig += itemRelationshipTextElement.getElementTextOrign().getValue() + " ";
					auxDest += itemRelationshipTextElement.getElementTextDestiny().getValue() + " ";
				}
				relationshipTextElement.setContentItensRelationship(auxOrig + auxDest);
				auxOrig = ""; auxDest = "";
			}
		}

		return resultList;
	}

	private void getSelectedEntityFromListbox() {
		Listbox listbox = (Listbox) getComponentById("resultList");
		int index = 0;
		if (listbox != null) {
			Listitem selectedItem = listbox.getSelectedItem();
			if (selectedItem != null) {
				index = selectedItem.getIndex();
				RelationshipTextElement rel = (RelationshipTextElement) listbox.getModel().getElementAt(
						index);
				selectedEntity = rel;
			}
		}
	}
	
	public void editRelationship(){
		getSelectedEntityFromListbox();
		setStudy(selectedEntity.getStudy());
		extractElementsAndItensStudy(selectedEntity.getItemRelationshipTextElements());
		openTextForStudy();
	}
	
	
	private void extractElementsAndItensStudy(List<ItemRelationshipTextElement> itemRelationshipTextElements) {
		List<ElementText> elementTextsOrign = new ArrayList<ElementText>(), elementTextsDestiny = new ArrayList<ElementText>();
		List<String> itemStudiesOrign = new ArrayList<String>(), itemStudiesDestiny = new ArrayList<String>();
		
		for (ItemRelationshipTextElement itemRelationshipTextElement : itemRelationshipTextElements) {
			ElementText itemDestiny = HibernateUtils.materializeProxy(itemRelationshipTextElement.getElementTextDestiny());
			ElementText itemOrign = HibernateUtils.materializeProxy(itemRelationshipTextElement.getElementTextOrign());

			elementTextsDestiny.add(itemDestiny);
			itemStudiesDestiny.add(itemDestiny.getId().toString());
			
			elementTextsOrign.add(itemOrign);
			itemStudiesOrign.add(itemOrign.getId().toString());
		}
		
		session.setAttribute("yetSelectedRelationshipItens", HibernateUtils.materializeProxy(selectedEntity.getItemRelationshipTextElements()));
	}

	public void deleteRelationship(){
		getSelectedEntityFromListbox();
		entity = getSelectedEntity();
		showActionConfirmation(getDeactivationMessage(), "deleteThisRelationship");
		loadBinder();
	}
	
	public Return deleteThisRelationship(){
		RelationshipTextElement thisRel = entity;
		Return ret = getControl().doAction("delete");
		if(ret.isValid())
			listEntity.remove(thisRel);
		loadBinder();
		return ret;
	}
}
