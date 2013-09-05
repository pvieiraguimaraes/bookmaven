package br.ueg.tcc.bookway.view.composer;

import org.springframework.context.annotation.Scope;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;

import br.com.vexillum.model.Configuration;
import br.com.vexillum.util.ReflectionUtils;
import br.com.vexillum.util.Return;
import br.com.vexillum.util.SpringFactory;
import br.com.vexillum.view.CRUDComposer;
import br.ueg.tcc.bookway.control.ConfigurationControl;
import br.ueg.tcc.bookway.control.UserBookwayControl;

@SuppressWarnings("serial")
@org.springframework.stereotype.Component
@Scope("prototype")
public class ConfigurationComposer extends
		CRUDComposer<Configuration, ConfigurationControl> {

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		loadBinder();
	}

	@Override
	public ConfigurationControl getControl() {
		return SpringFactory.getController("configurationControl",
				ConfigurationControl.class,
				ReflectionUtils.prepareDataForPersistence(this));
	}

	@Override
	public Configuration getEntityObject() {
		return new Configuration();
	}

	public void editDataUser() {
		Executions.sendRedirect("./perfil.zul");
	}

	public void deleteAccount() {
		showConfirmationDelete(messages.getKey("account_deletion_confirmation"));
	}

	@Override
	public void efectiveDeleteAction() {
		Return ret = new Return(true);
		UserBookwayControl control = SpringFactory.getController(
				"userBookwayControl", UserBookwayControl.class,
				ReflectionUtils.prepareDataForPersistence(this));
		ret.concat(control.doAction("deleteAccount"));
		if(ret.isValid())
			Executions.sendRedirect("/j_spring_security_logout");
	}
}
