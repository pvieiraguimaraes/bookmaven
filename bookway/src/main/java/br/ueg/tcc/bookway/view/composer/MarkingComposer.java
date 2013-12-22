package br.ueg.tcc.bookway.view.composer;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.zkoss.zk.ui.Component;

import br.com.vexillum.util.ReflectionUtils;
import br.com.vexillum.util.Return;
import br.com.vexillum.util.SpringFactory;
import br.ueg.tcc.bookway.control.MarkingControl;
import br.ueg.tcc.bookway.model.Marking;

@SuppressWarnings("serial")
@org.springframework.stereotype.Component
@Scope("prototype")
public class MarkingComposer extends BaseComposer<Marking, MarkingControl> {

	private String tagValue;

	public String getTagValue() {
		return tagValue;
	}

	public void setTagValue(String tagValue) {
		this.tagValue = tagValue;
	}

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
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
	public MarkingControl getControl() {
		return SpringFactory.getController("markingControl",
				MarkingControl.class,
				ReflectionUtils.prepareDataForPersistence(this));
	}

	@Override
	public Marking getEntityObject() {
		return new Marking();
	}

	@SuppressWarnings("unchecked")
	public Return searchMarking() {
		Return ret = getControl().doAction("searchMarking");
		if (ret.isValid() && !ret.getList().isEmpty()) {
			setListEntity((List<Marking>) ret.getList());
			getComponentById("resultList").setVisible(true);
			loadBinder();
		}
		return ret;
	}

	public void editMarking() {

	}

	public void deleteMarking() {

	}
}
