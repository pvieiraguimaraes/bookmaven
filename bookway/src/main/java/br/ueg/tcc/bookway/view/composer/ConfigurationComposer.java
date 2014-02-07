package br.ueg.tcc.bookway.view.composer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;

import br.com.vexillum.control.manager.ConfigurationManager;
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
		if(typePrivacyEntity == null) typePrivacyEntity = new TypePrivacyEntity();
		loadConfigurationsVisibilityUser();
		loadBinder();
	}

	/**
	 * Carrega no combobox o valor correto determinado pelo usuário e se não existir
	 * trás o valor padrão
	 */
	private void loadConfigurationsVisibilityUser() {
		typePrivacyEntity.setEmail(TypePrivacyItem.getTypePrivacyById(Integer
				.parseInt(getThisConfigurationValue("view_email").getValue())));
		typePrivacyEntity.setBirthDate(TypePrivacyItem
				.getTypePrivacyById(Integer.parseInt(getThisConfigurationValue(
						"view_birthdate").getValue())));
		typePrivacyEntity.setSex(TypePrivacyItem.getTypePrivacyById(Integer
				.parseInt(getThisConfigurationValue("view_sex").getValue())));
		typePrivacyEntity.setState(TypePrivacyItem.getTypePrivacyById(Integer
				.parseInt(getThisConfigurationValue("view_state").getValue())));
		typePrivacyEntity.setCity(TypePrivacyItem.getTypePrivacyById(Integer
				.parseInt(getThisConfigurationValue("view_city").getValue())));
		typePrivacyEntity.setProfession(TypePrivacyItem
				.getTypePrivacyById(Integer.parseInt(getThisConfigurationValue(
						"view_profession").getValue())));
		typePrivacyEntity.setAreaOfInterest(TypePrivacyItem
				.getTypePrivacyById(Integer.parseInt(getThisConfigurationValue(
						"view_areaofinterest").getValue())));
		typePrivacyEntity.setGraduation(TypePrivacyItem
				.getTypePrivacyById(Integer.parseInt(getThisConfigurationValue(
						"view_graduation").getValue())));
	}
	
	private Configuration getThisConfigurationValue(String key) {
		return ConfigurationManager.getManager().getConfigurationInstance(key,
				getUserLogged());
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
		UserPropertiesCategory category = getCategoryVisibilityPerfil();
		if(category != null && list.contains(category))
			list.remove(category);
		return list;
	}

	/**Carrega a categoria de visibilidade de itens de perfil
	 * se existir, caso contrário null
	 * @return categoria de visibilidade de itens de perfil
	 */
	private UserPropertiesCategory getCategoryVisibilityPerfil() {
		UserPropertiesCategory category = null;
		Return ret = getControl().getCategoryById((long) 2);
		if(ret.isValid())
			category = (UserPropertiesCategory) ret.getList().get(0);
		return category;
	}
	
	public void saveVisibilityAccount() {
		Return ret = new Return(true);
		List<Configuration> configs = new ArrayList<>();
		configs.add(changeValueConfiguration("view_email",
				String.valueOf(typePrivacyEntity.getEmail().ordinal())));
		configs.add(changeValueConfiguration("view_birthdate",
				String.valueOf(typePrivacyEntity.getBirthDate().ordinal())));
		configs.add(changeValueConfiguration("view_sex",
				String.valueOf(typePrivacyEntity.getSex().ordinal())));
		configs.add(changeValueConfiguration("view_state",
				String.valueOf(typePrivacyEntity.getState().ordinal())));
		configs.add(changeValueConfiguration("view_city",
				String.valueOf(typePrivacyEntity.getCity().ordinal())));
		configs.add(changeValueConfiguration("view_profession",
				String.valueOf(typePrivacyEntity.getProfession().ordinal())));
		configs.add(changeValueConfiguration("view_areaofinterest",
				String.valueOf(typePrivacyEntity.getAreaOfInterest().ordinal())));
		configs.add(changeValueConfiguration("view_graduation",
				String.valueOf(typePrivacyEntity.getGraduation().ordinal())));
		for (Configuration configuration : configs) {
			setEntity(configuration);
			ret = getControl().doAction("save");
		}
		treatReturn(ret);
		loadBinder();
	}
	
	private Configuration changeValueConfiguration(String key, String value){
		Configuration config = getThisConfigurationValue(key);
		config.setValue(value);
		return config;
	}
}
