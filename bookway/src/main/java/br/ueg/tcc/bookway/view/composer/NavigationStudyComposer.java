package br.ueg.tcc.bookway.view.composer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Tabpanels;
import org.zkoss.zul.Tabs;

import br.com.vexillum.util.HibernateUtils;
import br.com.vexillum.util.ReflectionUtils;
import br.com.vexillum.util.Return;
import br.com.vexillum.util.SpringFactory;
import br.ueg.tcc.bookway.control.MarkingControl;
import br.ueg.tcc.bookway.control.NavigationStudyControl;
import br.ueg.tcc.bookway.model.ElementText;
import br.ueg.tcc.bookway.model.ElementsItensStudy;
import br.ueg.tcc.bookway.model.ItemNavigationStudy;
import br.ueg.tcc.bookway.model.ItensOfStudy;
import br.ueg.tcc.bookway.model.LevelText;
import br.ueg.tcc.bookway.model.MarkingOfUser;
import br.ueg.tcc.bookway.model.MarkingUsed;
import br.ueg.tcc.bookway.model.Study;
import br.ueg.tcc.bookway.model.Text;
import br.ueg.tcc.bookway.model.UserBookway;
import br.ueg.tcc.bookway.view.macros.ItemStudy;

/**
 * @author Pedro
 * 
 */
@SuppressWarnings("serial")
@org.springframework.stereotype.Component
@Scope("prototype")
public class NavigationStudyComposer extends
		BaseComposer<ItemNavigationStudy, NavigationStudyControl> {

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
		
		if (study != null)
			createAmbientStudy();
		loadMarkingsOfUser();
		checkButtonMarking();
		loadBinder();
	}

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

	private void checkButtonMarking() {
		Button buttonMarking = (Button) getComponentById("fldMarking");
		if (!getMarkingOfUsers().isEmpty()) {
			if (buttonMarking != null)
				buttonMarking.setDisabled(false);
		} else if (buttonMarking != null)
			buttonMarking.setDisabled(true);
	}

	public MarkingControl getMarkingControl() {
		return SpringFactory.getController("markingControl",
				MarkingControl.class, null);
	}

	private void createAmbientStudy() {
		Tabbox tabbox = (Tabbox) getComponentById("panelStudy");
		if (tabbox != null) {
			Tabs tabs = tabbox.getTabs();
			if (tabs == null)
				tabs = new Tabs();
			tabs.appendChild(createTabStudy(getStudy().getText()));
			Tabpanels tabpanels = tabbox.getTabpanels();
			if (tabpanels == null)
				tabpanels = new Tabpanels();
			tabpanels.appendChild(createTabPanelStudy(getStudy()));
			tabbox.appendChild(tabs);
			tabbox.appendChild(tabpanels);
		}

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

	private Component createTabStudy(Text text) {
		Tab tab = new Tab();
		tab.setLabel(text.getTitle());
		return tab;
	}

	private Component createTabPanelStudy(Study study) {
		Tabpanel tabpanel = new Tabpanel();
		tabpanel.setSclass("tabpanelstudy");
		tabpanel.setStyle("text-align: justify;");
		
		LevelText rootLevel;
		//TODO Ver a maneira de continuar o estudo do ultmo elemento que parou, talvez seja relacionado com ScroolEvent
//		if (continueStudy)
//			rootLevel = HibernateUtils.materializeProxy(getControl()
//					.getLevelText(
//							study.getLastElementStop().getIdLevel().getId()));
//		else
			rootLevel = HibernateUtils.materializeProxy(getControl()
					.getLevelText(study.getText().getRootLevelText().getId()));
		
		tabpanel = (Tabpanel) mappingElementsAndChildren(rootLevel, tabpanel);

		// Slider slideStudy = new Slider();
		// slideStudy.appendChild(tabpanel);
		//
		// slideStudy.addEventListener(Events.ON_SCROLL, new EventListener() {
		// @Override
		// public void onEvent(Event event) throws Exception {
		//
		// System.out.println("Rolando");
		// }
		// });
		// TODO Ver como colocar os eventos ScroolEvent para funcionar aqui.
		List<Component> children = tabpanel.getChildren();
		return tabpanel;
	}

	@SuppressWarnings("unchecked")
	private Component mappingElementsAndChildren(LevelText levelChild,
			Component comp) {
		if (levelChild == null)
			return comp;
		else {
			List<ElementText> elements = HibernateUtils
					.transaformBagInList(levelChild.getElements());
				if(!elements.isEmpty())
					comp = createItemElementForStudy(elements, comp);
			List<LevelText> childs = HibernateUtils
					.transaformBagInList(levelChild.getLevelsChildren());
			for (LevelText levelText : childs) {
				comp = mappingElementsAndChildren(levelText, comp);
			}
		}
		return comp;

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Component createItemElementForStudy(List<ElementText> elements,
			Component compMaster) {
		ItemStudy itemStudy;
		Return retAux;
		boolean hasItensStudies = false;
		MarkingOfUser mark = null;
		
		if (elements != null) {
			//TODO Aqui deve ser feito a verifica��o do tipo do ElementText e conforme diferen�a de tipo exibir diferente
			for (ElementText elementText : elements) {
				Long idText = HibernateUtils.materializeProxy(elementText
						.getIdLevel().getIdText().getId());
				Long idLevel = HibernateUtils.materializeProxy(elementText.getId());
				itemStudy = new ItemStudy();


				itemStudy.setIdText(idText.toString());
				itemStudy.setIdElement(idLevel.toString());
				
				//TODO Colocar aqui a configura��o de estudo de acordo com as op��es do usu�rio
				if (elementText.getName().equalsIgnoreCase("titulo")) {
					itemStudy.setStyle("width: 100%; text-align: center;");
					itemStudy.contentElement.setStyle("font-size: 18px; width: 100%; font-weight: bold;");
				}

				if (elementText.getName().equalsIgnoreCase("valor")) { //Mapeia somente os elementos do n�vel que representam valor
					itemStudy.addEventListener(Events.ON_CLICK,
							new EventListener() {
								@Override
								public void onEvent(Event event)
										throws Exception {
									if (event.getTarget() != null) {
										ItemStudy item = (ItemStudy) event.getTarget();
										
										addElementTextSelected(item);
										changeItemStyle(item);
										addItemStudySelected(item);
										checkPanelActionVisibility();
									}
								}

							});
					
					retAux = checkExistingItensStudies(elementText, getStudy());
					if(retAux.isValid() && !retAux.getList().isEmpty())
						hasItensStudies = true;
					else
						hasItensStudies = false;
					
					if(hasItensStudies)
						mark = checkExistingMarking((List<ElementsItensStudy>)retAux.getList());
					
					if(mark != null)
						changeStyleMarkingThisItem(itemStudy, mark.getColor());
					
					itemStudy = insertIconStudy(itemStudy, idLevel, hasItensStudies);
					itemStudies.add(itemStudy);
				}
				
				compMaster.appendChild(itemStudy);
			}
		}
		return compMaster;
	}

	private Return checkExistingItensStudies(ElementText elementText, Study study) {
		newMap.put("checkElementText", HibernateUtils.materializeProxy(elementText));
		newMap.put("checkStudy", study);
		return getElementsItensStudyControl(newMap).searchExistingItensStudies();
	}

	private MarkingOfUser checkExistingMarking(List<ElementsItensStudy> list) {
		List<MarkingUsed> markingUseds = new ArrayList<MarkingUsed>();
		MarkingUsed resultMark = null;
		for (ElementsItensStudy elementsItensStudy : list) {
			ItensOfStudy thisMark = HibernateUtils.materializeProxy(elementsItensStudy.getItemOfStudy());
			if(thisMark instanceof MarkingUsed)
				markingUseds.add((MarkingUsed) thisMark);
		}
		if(!markingUseds.isEmpty()){
			resultMark = markingUseds.get(0);
			for (int i = 1; i < markingUseds.size(); i++) {
				if(markingUseds.get(i).getDateItem().after(resultMark.getDateItem()))
					resultMark = markingUseds.get(i);
			}
			if(resultMark != null)
				return HibernateUtils.materializeProxy(resultMark.getMarkingOfUser());
		}
		return null;
	}

	private void addItemStudySelected(ItemStudy itemStudy) {
		if (getItemStudiesSelected().contains(itemStudy))
			getItemStudiesSelected().remove(itemStudy);
		else
			getItemStudiesSelected().add(itemStudy);
	}
	
	private void addElementTextSelected(ItemStudy itemStudy) {
		ElementText elementText = getControl().getElementText(
				Long.parseLong(itemStudy.getIdElement()));
		if (getItensSelected().contains(elementText))
			getItensSelected().remove(elementText);
		else
			getItensSelected().add(elementText);
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
