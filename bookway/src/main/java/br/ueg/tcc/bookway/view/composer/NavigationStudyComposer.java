package br.ueg.tcc.bookway.view.composer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Slider;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Tabpanels;
import org.zkoss.zul.Tabs;

import br.com.vexillum.util.HibernateUtils;
import br.com.vexillum.util.ReflectionUtils;
import br.com.vexillum.util.SpringFactory;
import br.com.vexillum.view.CRUDComposer;
import br.ueg.tcc.bookway.control.NavigationStudyControl;
import br.ueg.tcc.bookway.model.ElementText;
import br.ueg.tcc.bookway.model.ItemNavigationStudy;
import br.ueg.tcc.bookway.model.LevelText;
import br.ueg.tcc.bookway.model.Text;
import br.ueg.tcc.bookway.view.macros.ItemStudy;

/**
 * @author Pedro
 * 
 */
@SuppressWarnings("serial")
@org.springframework.stereotype.Component
@Scope("prototype")
public class NavigationStudyComposer extends
		CRUDComposer<ItemNavigationStudy, NavigationStudyControl> {

	private final String UNDER_ON = "text-decoration: underline;";
	private final String UNDER_OFF = "text-decoration: none;";

	private Text textStudy;

	private List<LevelText> itensSelected;

	public Text getTextStudy() {
		return textStudy;
	}

	public void setTextStudy(Text textStudy) {
		this.textStudy = textStudy;
	}

	public List<LevelText> getItensSelected() {
		return itensSelected;
	}

	public void setItensSelected(List<LevelText> itensSelected) {
		this.itensSelected = itensSelected;
	}

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		textStudy = (Text) session.getAttribute("textStudy");
		// TODO Verificar aqui depois a continuação do estudo do texto
		if (textStudy != null)
			createAmbientStudy();
		if (itensSelected == null)
			itensSelected = new ArrayList<>();
		loadBinder();
	}

	private void createAmbientStudy() {
		Tabbox tabbox = (Tabbox) getComponentById(thisComposer.getComponent(),
				"panelStudy");
		// System.out.println(tabbox.getHeight()); Pensar em uma maneira de
		// manipular o acesso aos dados de acordo com o tamanho da tela.
		Tabs tabs = tabbox.getTabs();
		if (tabs == null)
			tabs = new Tabs();
		tabs.appendChild(createTabStudy(getTextStudy()));
		Tabpanels tabpanels = tabbox.getTabpanels();
		if (tabpanels == null)
			tabpanels = new Tabpanels();
		tabpanels.appendChild(createTabPanelStudy(getTextStudy()));
		tabbox.appendChild(tabs);
		tabbox.appendChild(tabpanels);

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

	private Component createTabPanelStudy(Text text) {
		Tabpanel tabpanel = new Tabpanel();
		tabpanel.setSclass("tabpanelstudy");
		tabpanel.setStyle("text-align: justify;");
		LevelText rootLevel = HibernateUtils.materializeProxy(getControl()
				.getLevelText(text.getRootLevelText().getId()));

		tabpanel = (Tabpanel) mappingElementsAndChildren(rootLevel, tabpanel);
		
//		Slider slideStudy = new Slider();
//		slideStudy.appendChild(tabpanel);
//		
//		slideStudy.addEventListener(Events.ON_SCROLL, new EventListener() {
//			@Override
//			public void onEvent(Event event) throws Exception {
//				
//				System.out.println("Rolando");
//			}
//		});
		//TODO Ver como colocar o eventos ScroolEvent para funcionar aqui.
		
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
		if (elements != null) {
			for (ElementText elementText : elements) {
				Long idText = HibernateUtils.materializeProxy(elementText
						.getIdLevel().getIdText().getId());
				Long idLevel = HibernateUtils.materializeProxy(elementText
						.getIdLevel().getId());
				itemStudy = new ItemStudy();
				itemStudy.setContent(elementText.getValue());
				itemStudy.setIdText(idText.toString());
				itemStudy.setIdLevel(idLevel.toString());

				itemStudy.addEventListener(Events.ON_CLICK,
						new EventListener() {
							@Override
							public void onEvent(Event event) throws Exception {
								if (event.getTarget() != null) {
									addItemInListItensSelected((ItemStudy) event
											.getTarget());
									changeItemStyle((ItemStudy) event
											.getTarget());
									checkPanelActionVisibility();
								}
							}

						});

				compMaster.appendChild(itemStudy);
			}
		}
		return compMaster;
	}

	private void changeItemStyle(ItemStudy itemStudy) {
		String style = itemStudy.contentElement.getStyle();
		if (style == null)
			itemStudy.contentElement.setStyle(UNDER_ON);
		else {
			if (style.equalsIgnoreCase("") || style.equalsIgnoreCase(UNDER_OFF))
				itemStudy.contentElement.setStyle(UNDER_ON);
			else
				itemStudy.contentElement.setStyle(UNDER_OFF);
		}
	}

	private void addItemInListItensSelected(ItemStudy itemStudy) {
		LevelText levelText = getControl().getLevelText(
				Long.parseLong(itemStudy.getIdLevel()));
		if (getItensSelected().contains(levelText))
			getItensSelected().remove(levelText);
		else
			getItensSelected().add(levelText);

	}

	private void checkPanelActionVisibility() {
		if (getItensSelected().isEmpty())
			changeVisibilityPanelAction(false);
		else
			changeVisibilityPanelAction(true);
	}

	private void changeVisibilityPanelAction(boolean visibility) {
		Component panel = getComponentById(component, "panelActions");
		panel.setVisible(visibility);
	}
}
