package br.ueg.tcc.bookway.view.composer;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Tabpanels;
import org.zkoss.zul.Tabs;

import br.com.vexillum.control.GenericControl;
import br.com.vexillum.model.ICommonEntity;
import br.com.vexillum.util.HibernateUtils;
import br.com.vexillum.util.Return;
import br.ueg.tcc.bookway.model.ElementText;
import br.ueg.tcc.bookway.model.ElementsItensStudy;
import br.ueg.tcc.bookway.model.ItensOfStudy;
import br.ueg.tcc.bookway.model.LevelText;
import br.ueg.tcc.bookway.model.MarkingOfUser;
import br.ueg.tcc.bookway.model.MarkingUsed;
import br.ueg.tcc.bookway.model.Study;
import br.ueg.tcc.bookway.view.macros.ItemStudy;

@SuppressWarnings("serial")
public abstract class BaseNavigationStudyComposer<E extends ICommonEntity, G extends GenericControl<E>>
		extends BaseComposer<E, G> {
	
	private NavigationStudyComposer navigationStudyComposer;

	public NavigationStudyComposer getNavigationStudyComposer() {
		return navigationStudyComposer;
	}

	public void setNavigationStudyComposer(
			NavigationStudyComposer navigationStudyComposer) {
		this.navigationStudyComposer = navigationStudyComposer;
	}
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		if(parentComposer instanceof NavigationStudyComposer)
			setNavigationStudyComposer((NavigationStudyComposer) parentComposer);
		loadBinder();
	}
	
	public void addItemStudySelected(ItemStudy itemStudy) {
		if (getItemStudiesSelected().contains(itemStudy))
			getItemStudiesSelected().remove(itemStudy);
		else
			getItemStudiesSelected().add(itemStudy);
	}
	
	public void addItemStudySelectedForReference(ItemStudy itemStudy) {
		if (getItemStudiesSelectedForReference().contains(itemStudy))
			getItemStudiesSelectedForReference().remove(itemStudy);
		else
			getItemStudiesSelectedForReference().add(itemStudy);
	}
	
	public void addElementTextSelected(ItemStudy itemStudy, ElementText elementText) {
		if (getItensSelected().contains(elementText))
			getItensSelected().remove(elementText);
		else
			getItensSelected().add(elementText);
	}
	
	public void addElementTextSelectedForReference(ItemStudy itemStudy, ElementText elementText) {
		if (getItensSelectedForReference().contains(elementText))
			getItensSelectedForReference().remove(elementText);
		else
			getItensSelectedForReference().add(elementText);
	}

	public void createAmbientStudy(LevelText levelRoot) {
		Tabbox tabbox = (Tabbox) getComponentById("panelStudy");
		if (tabbox != null) {
			Tabs tabs = tabbox.getTabs();
			if (tabs == null)
				tabs = new Tabs();
			tabs.appendChild(createTabWithName(getStudy().getText().getTitle()));
			Tabpanels tabpanels = tabbox.getTabpanels();
			if (tabpanels == null)
				tabpanels = new Tabpanels();
			tabpanels.appendChild(createTabPanelStudy(getStudy(), levelRoot));
			tabbox.appendChild(tabs);
			tabbox.appendChild(tabpanels);
		}

	}
	
	public Component createTabPanelStudy(Study study, LevelText rootLevel) {
		Tabpanel tabpanel = new Tabpanel();
		tabpanel.setSclass("tabpanelstudy");
		tabpanel.setStyle("text-align: justify;");
		
//		if (continueStudy) {
//			LevelText page = getPageElement(study.getLastElementStop().getIdLevel());
//
//			rootLevel = HibernateUtils.materializeProxy(getControl()
//					.getLevelText(page.getId()));
//		} else
//			rootLevel = HibernateUtils.materializeProxy(getControl()
//					.getLevelText(study.getText().getRootLevelText().getId()));

		tabpanel = (Tabpanel) mappingElementsAndChildren(rootLevel, tabpanel);

		return tabpanel;
	}
	
	@SuppressWarnings("unchecked")
	public Component mappingElementsAndChildren(LevelText levelChild,
			Component comp) {
		if (levelChild == null)
			return comp;
		else {
			List<ElementText> elements = HibernateUtils
					.transaformBagInList(levelChild.getElements());
				if(elements != null && !elements.isEmpty())
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
	public Component addEnventForNavigationReference(ItemStudy itemStudy, final ElementText elementText){
		itemStudy.addEventListener(Events.ON_CLICK,
				new EventListener() {
					@Override
					public void onEvent(Event event)
							throws Exception {
						if (event.getTarget() != null) {
							ItemStudy item = (ItemStudy) event
									.getTarget();
							addElementTextSelectedForReference(item, elementText);
							changeItemStyle(item);
							addItemStudySelectedForReference(item);
						}
					}

				});
		return itemStudy;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Component addEnventForNavigationStudy(ItemStudy itemStudy, final ElementText elementText){
		itemStudy.addEventListener(Events.ON_CLICK,
				new EventListener() {
					@Override
					public void onEvent(Event event)
							throws Exception {
						if (event.getTarget() != null) {
							ItemStudy item = (ItemStudy) event
									.getTarget();
							addElementTextSelected(item, elementText);
							changeItemStyle(item);
							addItemStudySelected(item);
							checkPanelActionVisibility();
						}
					}

				});
		return itemStudy;
	}
	
	public MarkingOfUser checkExistingMarking(List<ElementsItensStudy> list) {
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
	
	public Return checkExistingItensStudies(ElementText elementText, Study study) {
		newMap.put("checkElementText", HibernateUtils.materializeProxy(elementText));
		newMap.put("checkStudy", study);
		return getElementsItensStudyControl(newMap).searchExistingItensStudies();
	}
	
	@SuppressWarnings({ "unchecked" })
	private Component createItemElementForStudy(List<ElementText> elements,
			Component compMaster) {
		ItemStudy itemStudy;
		Return retAux;
		boolean hasItensStudies = false;
		MarkingOfUser mark = null;
		
		if (elements != null) {
			//TODO Aqui deve ser feito a verifica��o do tipo do ElementText e conforme diferen�a de tipo exibir diferente
			for (ElementText elementText : elements) {
				
				if(!elementText.getIdLevel().getName().equalsIgnoreCase("pagina")){
				
					Long idText = HibernateUtils.materializeProxy(elementText
							.getIdLevel().getIdText().getId());
					Long idLevel = HibernateUtils.materializeProxy(elementText.getId());
					itemStudy = new ItemStudy();
	
					itemStudy.setContent(elementText.getValue());
					itemStudy.setIdText(idText.toString());
					itemStudy.setIdElement(idLevel.toString());
					
					//TODO Colocar aqui a configura��o de estudo de acordo com as op��es do usu�rio
					if (elementText.getName().equalsIgnoreCase("titulo")) {
						itemStudy.setStyle("width: 100%; text-align: center;");
						itemStudy.contentElement.setStyle("font-size: 18px; width: 100%; font-weight: bold;");
					}
	
					if (elementText.getName().equalsIgnoreCase("valor")) {
						if (isTextReferenceMode) {
							itemStudy = (ItemStudy) addEnventForNavigationReference(itemStudy, elementText);
						} else {
							itemStudy = (ItemStudy) addEnventForNavigationStudy(itemStudy, elementText);
						}
						
						retAux = checkExistingItensStudies(elementText, getStudy());
						if(retAux.isValid() && !retAux.getList().isEmpty())
							hasItensStudies = true;
						else
							hasItensStudies = false;
						
						if(hasItensStudies)
							mark = checkExistingMarking((List<ElementsItensStudy>)retAux.getList());
						
						if(mark != null)
							changeStyleMarkingThisItem(itemStudy, mark.getColor());
						
						if(!isTextReferenceMode)
							itemStudy = insertIconStudy(itemStudy, idLevel, hasItensStudies);
						
						itemStudies.add(itemStudy);
					}
					
					compMaster.appendChild(itemStudy);
				}
			}
		}
		return compMaster;
	}

}