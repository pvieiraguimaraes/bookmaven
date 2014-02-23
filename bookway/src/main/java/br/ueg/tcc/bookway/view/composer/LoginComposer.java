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

/**
 * Classe que realiza a comunicação entre a camada de visão e a camada de
 * controle para as funcionalidades envolvidas com a verificação de conta do
 * usuário
 * 
 * @author pedro
 * 
 */
@SuppressWarnings("serial")
@org.springframework.stereotype.Component
@Scope("prototype")
public class LoginComposer extends
		CRUDComposer<UserBookway, UserBookwayControl> {

	/**
	 * Código passado para validação de usuário
	 */
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
		String page = Executions.getCurrent().getDesktop().getRequestPath();
		super.doAfterCompose(comp);
		if (code != null)
			validateAccount();
		if (page.equalsIgnoreCase("/pages/validate.zul"))
			Executions.sendRedirect("/pages/login.zul");
		loadBinder();
	}

	/**
	 * Método que realiza a chamada do controlador para validação de conta do
	 * usuário
	 */
	private void validateAccount() {
		if (getControl().doAction("validateAccountUser").isValid())
			showNotification("Conta validada com sucesso", "info");
	}

	@Override
	public UserBookwayControl getControl() {
		return SpringFactory.getController("userBookwayControl",
				UserBookwayControl.class,
				ReflectionUtils.prepareDataForPersistence(this));
	}

	@Override
	public UserBookway getEntityObject() {
		return new UserBookway();
	}

	/**
	 * Acessa o controle para uma registro de um novo usuário
	 */
	public void register() {
		Return retRegister = new Return(true);
		retRegister = getControl().doAction("registerUser");
		if (retRegister.isValid())
			resetEntity();
		treatReturn(retRegister);
	}

	/**
	 * Limpa os dados do objeto na visão e recarrega os campos dos formulários
	 */
	public void resetEntity() {
		entity = getEntityObject();
		loadBinder();
	}

}
