package br.ueg.tcc.bookway.view.composer;

import org.zkoss.zk.ui.Component;

import br.com.vexillum.util.ReflectionUtils;
import br.com.vexillum.util.SpringFactory;
import br.ueg.tcc.bookway.control.ElementsItensStudyControl;
import br.ueg.tcc.bookway.model.ElementsItensStudy;
import br.ueg.tcc.bookway.view.macros.ItemStudy;

@SuppressWarnings("serial")
public class ElementsItensStudyComposer extends BaseComposer<ElementsItensStudy, ElementsItensStudyControl> {

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		ItemStudy item = (ItemStudy) session.getAttribute("itemStudySelected");
		if(item != null)
			createPanelItensStudy(item);
	}
	
	private void createPanelItensStudy(ItemStudy item) {
		
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
