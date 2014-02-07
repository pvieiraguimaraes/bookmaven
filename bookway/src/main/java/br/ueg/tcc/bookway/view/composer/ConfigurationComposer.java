package br.ueg.tcc.bookway.view.composer;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;

import br.com.vexillum.model.Configuration;
import br.com.vexillum.model.UserPropertiesCategory;
import br.com.vexillum.util.ReflectionUtils;
import br.com.vexillum.util.Return;
import br.com.vexillum.util.SpringFactory;
import br.com.vexillum.view.PageConfigurationComposer;
import br.ueg.tcc.bookway.control.ConfigurationControl;
import br.ueg.tcc.bookway.control.UserBookwayControl;
import br.ueg.tcc.bookway.model.TypePrivacyEntity;
import br.ueg.tcc.bookway.model.enums.TypePrivacyItem;

@SuppressWarnings("serial")
@org.springframework.stereotype.Component
@Scope("prototype")
public class ConfigurationComposer extends PageConfigurationComposer {
	
	private TypePrivacyEntity typePrivacyEntity;

	public TypePrivacyEntity getTypePrivacyEntity() {
		return typePrivacyEntity;
	}

	public void setTypePrivacyEntity(TypePrivacyEntity typePrivacyEntity) {
		this.typePrivacyEntity = typePrivacyEntity;
	}

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
	
	public List<TypePrivacyItem> getListTypesPrivacyItem() {
		return Arrays.asList(TypePrivacyItem.values());
	}

	@Override
	public Configuration getEntityObject() {
		return new Configuration();
	}

	public void editDataUser() {
		Executions.sendRedirect("./perfil.zul");
	}

	public void deleteAccount() {
		showActionConfirmation(messages.getKey("account_deletion_confirmation"), "deleteAccountUser");
	}

	public void deleteAccountUser() {
		Return ret = new Return(true);
		UserBookwayControl control = SpringFactory.getController(
				"userBookwayControl", UserBookwayControl.class,
				ReflectionUtils.prepareDataForPersistence(this));
		ret.concat(control.doAction("deleteAccount"));
		if(ret.isValid())
			Executions.sendRedirect("/j_spring_security_logout");
	}
	
	@Override
	public List<UserPropertiesCategory> getUserPropertiesCategory() {
		List<UserPropertiesCategory> list = super.getUserPropertiesCategory();
		UserPropertiesCategory category = null;
		Return ret = getControl().getCategoryById((long) 2);
		if(ret.isValid())
			category = (UserPropertiesCategory) ret.getList().get(0);
		if(category != null && list.contains(category))
			list.remove(category);
		return list;
	}
	
	public void saveVisibilityAccount(){
		
	}
}
