package br.ueg.tcc.bookway.view.composer;

import java.util.List;

import org.hibernate.Hibernate;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabs;
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
import br.ueg.tcc.bookway.model.Study;
import br.ueg.tcc.bookway.view.macros.ItemOfPanel;

@SuppressWarnings("serial")
public class ElementsItensStudyComposer extends
		BaseComposer<ElementsItensStudy, ElementsItensStudyControl> {

	private ElementText checkElementText;

	private Study checkStudy;

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
//		<tabbox>
	//		<tabs>
	//		 	<tab/>
	//		</tabs>
	//		<tabpanels>
	//			<tabpanel/>
	//		</tabpanels>
//		</tabbox>
		
		Tabbox tabbox = new Tabbox();
		Tabs tabs = new Tabs();
		Tab tab = new Tab();
		
		for (ElementsItensStudy item : list) {
			ItensOfStudy itemOfStudy = HibernateUtils.materializeProxy(item.getItemOfStudy());
			
		}
		
		return null;
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
