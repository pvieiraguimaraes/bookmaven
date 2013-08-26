package br.ueg.tcc.bookway.view.composer;

import org.springframework.context.annotation.Scope;
import org.zkoss.zk.ui.Executions;

import br.com.vexillum.model.Configuration;
import br.com.vexillum.util.ReflectionUtils;
import br.com.vexillum.util.SpringFactory;
import br.com.vexillum.view.CRUDComposer;
import br.ueg.tcc.bookway.control.ConfigurationControl;

@SuppressWarnings("serial")
@org.springframework.stereotype.Component
@Scope("prototype")
public class ConfigurationComposer extends CRUDComposer<Configuration, ConfigurationControl>{

	@Override
	public ConfigurationControl getControl() {
		return SpringFactory.getController("configurationControl", ConfigurationControl.class, ReflectionUtils.prepareDataForPersistence(this));
	}

	@Override
	public Configuration getEntityObject() {
		return new Configuration();
	}
	
	public void editDataUser(){
		Executions.sendRedirect("./perfil.zul");
	}
	//TODO Este composer deverá ser o responsavel por controlar as configurações apartir do configuration.zil do bookway
}
