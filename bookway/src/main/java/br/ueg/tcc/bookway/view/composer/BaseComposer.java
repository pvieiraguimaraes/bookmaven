package br.ueg.tcc.bookway.view.composer;


import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Window;

import br.com.vexillum.control.GenericControl;
import br.com.vexillum.model.ICommonEntity;
import br.com.vexillum.util.Return;

@SuppressWarnings("serial")
public abstract class BaseComposer<E extends ICommonEntity, G extends GenericControl<E>>
		extends InitComposer<E, G> {

	protected BaseComposer<E, G> parentComposer;

	public BaseComposer<E, G> getParentComposer() {
		return parentComposer;
	}

	public void setParentComposer(BaseComposer<E, G> parentComposer) {
		this.parentComposer = parentComposer;
	}

	@SuppressWarnings("unchecked")
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		initUpdatePage();
		setParentComposer((BaseComposer<E, G>) arg.get("thisComposer"));
	}
	
	@SuppressWarnings("unchecked")
	protected void initUpdatePage() {
		if (arg.get("selectedEntity") != null) {
			entity = (E) ((E) arg.get("selectedEntity")).cloneEntity();
			update = true;
		}
	}

	public void callUpdateModal() {
		Return ret = validateSelectedEntity();
		if (ret.isValid()) {
			super.callModalWindow(getUpdatePage());
		}
	}

	public void callDeactivationConfirmation() {
		super.showDeactivateConfirmation(getDeactivationMessage());
	}

	public void callActivationConfirmation() {
		super.showActivateConfirmation(getActivationMessage());
	}

	protected abstract String getUpdatePage();

	protected abstract String getDeactivationMessage();

	protected String getActivationMessage() {
		return "Você tem certeza que deseja ativar?";
	}

	public void loadBinderParentComposer() {
		getParentComposer().loadBinder();
	}

	public void callDeletionConfirmation() {
		super.showDeleteConfirmation(getDeleteMessage());
	}

	protected String getDeleteMessage() {
		return "Confirma exclusão?";
	}

	@Override
	public Return saveEntity() {
		Return ret = super.saveEntity();
		if (ret.isValid() && getUpdate()) {
			getParentComposer().loadBinder();
			((Window) getComponentById(component, "modalWindow")).detach();
		} else if (ret.isValid()) {
			clearForm();
		}
		loadBinder();
		return ret;
	}

	public void resetForm() {
		clearForm();
		loadBinder();
	}
	
}
