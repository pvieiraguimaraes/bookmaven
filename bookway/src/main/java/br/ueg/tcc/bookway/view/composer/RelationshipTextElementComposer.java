package br.ueg.tcc.bookway.view.composer;

import br.com.vexillum.util.ReflectionUtils;
import br.com.vexillum.util.SpringFactory;
import br.ueg.tcc.bookway.control.RelationshipTextElementControl;
import br.ueg.tcc.bookway.model.RelationshipTextElement;

@SuppressWarnings("serial")
public class RelationshipTextElementComposer extends
		BaseComposer<RelationshipTextElement, RelationshipTextElementControl> {

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

}
