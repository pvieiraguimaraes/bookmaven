package br.ueg.tcc.bookway.view.composer;

import org.springframework.context.annotation.Scope;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;

import br.com.vexillum.util.ReflectionUtils;
import br.com.vexillum.util.Return;
import br.com.vexillum.util.SpringFactory;
import br.com.vexillum.view.CRUDComposer;
import br.ueg.tcc.bookway.control.UserBookwayControl;
import br.ueg.tcc.bookway.model.UserBookway;

@SuppressWarnings("serial")
@org.springframework.stereotype.Component
@Scope("prototype")
public class LoginComposer extends CRUDComposer<UserBookway, UserBookwayControl> {
	
	private String code;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		code = Executions.getCurrent().getParameter("code");
		super.doAfterCompose(comp);
		if(code != null)
			validateAccount();
		loadBinder();
	}	
	
	private void validateAccount() {
		if(getControl().doAction("validateAccountUser").isValid()){
			showNotification("Conta validada com sucesso", "info");
			Executions.sendRedirect("/");
		}		
	}

	@Override
	public UserBookwayControl getControl() {
		return SpringFactory.getController("userBookwayControl", UserBookwayControl.class, ReflectionUtils.prepareDataForPersistence(this));
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
		entity = getEntityObject();
		loadBinder();
	}

}
