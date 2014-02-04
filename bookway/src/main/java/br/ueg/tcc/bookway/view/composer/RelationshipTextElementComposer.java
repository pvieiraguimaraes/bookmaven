package br.ueg.tcc.bookway.view.composer;

import org.zkoss.zk.ui.Component;

import br.com.vexillum.util.HibernateUtils;
import br.com.vexillum.util.ReflectionUtils;
import br.com.vexillum.util.SpringFactory;
import br.ueg.tcc.bookway.control.RelationshipTextElementControl;
import br.ueg.tcc.bookway.model.RelationshipTextElement;
import br.ueg.tcc.bookway.model.Study;

@SuppressWarnings("serial")
public class RelationshipTextElementComposer
		extends
		BaseNavigationStudyComposer<RelationshipTextElement, RelationshipTextElementControl> {

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);

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

	}

	public void callThisTextForReference() {
		session.setAttribute("study", study);
		session.setAttribute("isTextReferenceMode", true);
		callModalWindow("/template/frms/frmTextReference.zul");
	}
	
	public void callSearchTextForReference() {
		callModalWindow("/template/frms/panelSearchText.zul");
	}

}
