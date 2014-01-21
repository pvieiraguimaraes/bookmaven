package br.ueg.tcc.bookway.view.composer;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Tabpanels;
import org.zkoss.zul.Tabs;
import org.zkoss.zul.Vlayout;
import org.zkoss.zul.Window;

import br.com.vexillum.util.HibernateUtils;
import br.com.vexillum.util.ReflectionUtils;
import br.com.vexillum.util.Return;
import br.com.vexillum.util.SpringFactory;
import br.ueg.tcc.bookway.control.ElementsItensStudyControl;
import br.ueg.tcc.bookway.model.Annotation;
import br.ueg.tcc.bookway.model.ElementText;
import br.ueg.tcc.bookway.model.ElementsItensStudy;
import br.ueg.tcc.bookway.model.ItensOfStudy;
import br.ueg.tcc.bookway.model.MarkingUsed;
import br.ueg.tcc.bookway.model.Study;
import br.ueg.tcc.bookway.view.macros.ItemOfPanel;

@SuppressWarnings("serial")
public class ElementsItensStudyComposer extends
		BaseComposer<ElementsItensStudy, ElementsItensStudyControl> {

	private ElementText checkElementText;

	private Study checkStudy;
	
	private List<Annotation> annotations;
	
	private List<MarkingUsed> markingUseds;

	public List<Annotation> getAnnotations() {
		return annotations;
	}

	public void setAnnotations(List<Annotation> annotations) {
		this.annotations = annotations;
	}

	public List<MarkingUsed> getMarkingUseds() {
		return markingUseds;
	}

	public void setMarkingUseds(List<MarkingUsed> markingUseds) {
		this.markingUseds = markingUseds;
	}

	public ElementText getCheckElementText() {
		return checkElementText;
	}

	public void setCheckElementText(ElementText checkElementText) {
		this.checkElementText = checkElementText;
	}

	public Study getCheckStudy() {
		return checkStudy;
	}

	public void setCheckStudy(Study checkStudy) {
		this.checkStudy = checkStudy;
	}

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		String idElementText = (String) session.getAttribute("idElementText");

		if (idElementText != null)
			createPanelItensStudy(idElementText);
	}

	@SuppressWarnings("unchecked")
	private void createPanelItensStudy(String idElementText) {
		Window win = (Window) getComponentById("panelItensStudy");

		setCheckElementText(getNavigationStudyControl(null)
				.getElementText(Long.parseLong(idElementText)));
		setCheckStudy(study);

		Return ret = getControl().searchExistingItensStudies();
		if(ret.isValid() && !ret.getList().isEmpty()){
			
			List<ElementsItensStudy> list = (List<ElementsItensStudy>) ret.getList();
			win.appendChild(createComponentsInPanel(list));
		}
	}

	private Component createComponentsInPanel(List<ElementsItensStudy> list) {
		separateItensStudyList(list);
		
		Tabbox tabbox = new Tabbox();
		Tabs tabs = new Tabs();
		Tabpanels tabpanels = new Tabpanels();
		
		tabbox.appendChild(tabs);
		tabbox.appendChild(tabpanels);
		
		if (!getAnnotations().isEmpty()) {
			tabs.appendChild(createItemPanelAnnotation(tabs, tabpanels));
		}
		
		if (!getMarkingUseds().isEmpty()) {

		}
		
		((Tab)tabbox.getTabs().getFirstChild()).setSelected(true);
		
		return tabbox;
	}

	private Tab createItemPanelAnnotation(Tabs tabs, Tabpanels tabpanels) {
		Tab tab = new Tab("Anotações");
		Tabpanel tabpanel = new Tabpanel();
		Vlayout vlayout = new Vlayout();
		
		tabs.appendChild(tab);
		
		vlayout.setHeight("400px");
		vlayout.setStyle("overflow-y: scroll;");

		tabpanel.appendChild(vlayout);
		tabpanels.appendChild(tabpanel);
		
		for (Annotation annotation : getAnnotations()) {
			vlayout.appendChild(new ItemOfPanel(annotation.getTitle(), annotation.getContent()));
		}
		
		return tab;
	}

	private void separateItensStudyList(List<ElementsItensStudy> list) {
		resetListsItens();
		for (ElementsItensStudy elementsItensStudy : list) {
			ItensOfStudy itemOfStudy = HibernateUtils
					.materializeProxy(elementsItensStudy.getItemOfStudy());
			if (itemOfStudy instanceof Annotation)
				annotations.add((Annotation) itemOfStudy);
			if(itemOfStudy instanceof MarkingUsed)
				markingUseds.add((MarkingUsed) itemOfStudy);
		}
	}
	
	private void resetListsItens(){
		setAnnotations(new ArrayList<Annotation>());
		setMarkingUseds(new ArrayList<MarkingUsed>());
	}

	private Component createItemPanel(ElementText elem) {
		ItemOfPanel itemOfPanel = new ItemOfPanel();

		return null;
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
	public ElementsItensStudyControl getControl() {
		return SpringFactory.getController("elementsItensStudyControl",
				ElementsItensStudyControl.class,
				ReflectionUtils.prepareDataForPersistence(this));
	}

	@Override
	public ElementsItensStudy getEntityObject() {
		return new ElementsItensStudy();
	}

}
