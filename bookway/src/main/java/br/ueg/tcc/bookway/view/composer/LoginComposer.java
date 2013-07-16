package br.ueg.tcc.bookway.view.composer;

import org.springframework.context.annotation.Scope;
import org.zkoss.zk.ui.Component;

import br.com.vexillum.util.ReflectionUtils;
import br.com.vexillum.util.Return;
import br.com.vexillum.util.SpringFactory;
import br.com.vexillum.view.CRUDComposer;
import br.ueg.tcc.bookway.control.UserControl;
import br.ueg.tcc.bookway.model.UserBookway;

@SuppressWarnings("serial")
@org.springframework.stereotype.Component
@Scope("prototype")
public class LoginComposer extends CRUDComposer<UserBookway, UserControl> {

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		loadBinder();
	}	
	
	@Override
	public UserControl getControl() {
		return SpringFactory.getController("userControl", UserControl.class, ReflectionUtils.prepareDataForPersistence(this));
	}

	@Override
	public UserBookway getEntityObject() {
		return new UserBookway();
	}
	
	public void register() {
		Return retRegister = new Return(true);
		retRegister = getControl().doAction("registerUser");
		if(retRegister.isValid()) resetEntity();
		treatReturn(retRegister);
	}

	public void resetEntity(){
		entity = null;
		binder.loadAll();
	}

}
