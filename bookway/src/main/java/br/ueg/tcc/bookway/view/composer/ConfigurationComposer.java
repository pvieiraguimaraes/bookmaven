package br.ueg.tcc.bookway.view.composer;

import org.springframework.context.annotation.Scope;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Messagebox;

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
		showConfirmationDelete();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void showConfirmationDelete() {
		EventListener evt = new EventListener() {
			public void onEvent(Event evt) throws InterruptedException {
				if (evt.getName().equals("onYes")) {
					efectiveDeleteAccount();
				}
			}
		};
		String mess = messages.getKey("account_deletion_confirmation");
		Messagebox.show(mess, "Confirmação", Messagebox.YES | Messagebox.NO,
				Messagebox.QUESTION, evt);
	}
	
	private void efectiveDeleteAccount(){
		Return ret = new Return(true);
		UserBookwayControl control = SpringFactory.getController(
				"userBookwayControl", UserBookwayControl.class,
				ReflectionUtils.prepareDataForPersistence(this));
		ret.concat(control.doAction("deleteAccount"));
		if(ret.isValid())
			Executions.sendRedirect("/j_spring_security_logout");
	}
}
