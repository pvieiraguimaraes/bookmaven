package br.ueg.tcc.bookway.view.composer;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.zkoss.zk.ui.Component;
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

	private Text textStudy;

	public Text getTextStudy() {
		return textStudy;
	}

	public void setTextStudy(Text textStudy) {
		this.textStudy = textStudy;
	}

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		textStudy = (Text) session.getAttribute("textStudy");
		if (textStudy != null)
			createAmbientStudy();
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
		LevelText rootLevel = HibernateUtils.materializeProxy(getControl()
				.getLevelText(text.getRootLevelText().getId()));

		tabpanel = (Tabpanel) mappingElementsAndChildren(rootLevel, tabpanel);

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

	private Component createItemElementForStudy(List<ElementText> elements,
			Component compMaster) {
		ItemStudy itemStudy;
		if (elements != null) {
			for (ElementText elementText : elements) {
				itemStudy = new ItemStudy();
				itemStudy.setContent(elementText.getValue());
				compMaster.appendChild(itemStudy);
			}
		}
		return compMaster;
	}

}
