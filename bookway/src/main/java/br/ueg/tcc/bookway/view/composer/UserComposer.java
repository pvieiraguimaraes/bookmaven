package br.ueg.tcc.bookway.view.composer;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.zkoss.zk.ui.Component;

import br.com.vexillum.util.EncryptUtils;
import br.com.vexillum.util.ReflectionUtils;
import br.com.vexillum.util.Return;
import br.com.vexillum.util.SpringFactory;
import br.com.vexillum.view.CRUDComposer;
import br.ueg.tcc.bookway.control.UserBookwayControl;
import br.ueg.tcc.bookway.model.UserBookway;
import br.ueg.tcc.bookway.model.enums.AreaOfInterest;
import br.ueg.tcc.bookway.model.enums.State;

@SuppressWarnings("serial")
@org.springframework.stereotype.Component
@Scope("prototype")
public class UserComposer extends CRUDComposer<UserBookway, UserBookwayControl> {
	
	private List<AreaOfInterest> listAreaOfInterest;
	private List<State> listState;
	
	private String actualPassword;
	private String newPassword;
	private String confirmNewPassword;
	
	public String getActualPassword() {
		return actualPassword;
	}

	public void setActualPassword(String actualPassword) {
		this.actualPassword = EncryptUtils.encryptOnSHA512(actualPassword);
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = EncryptUtils.encryptOnSHA512(newPassword);
	}

	public String getConfirmNewPassword() {
		return confirmNewPassword;
	}

	public void setConfirmNewPassword(String confirmNewPassword) {
		this.confirmNewPassword = EncryptUtils.encryptOnSHA512(confirmNewPassword);
	}

	public List<AreaOfInterest> getListAreaOfInterest() {
		return listAreaOfInterest;
	}

	public void setListAreaOfInterest(List<AreaOfInterest> listAreaOfInterest) {
		this.listAreaOfInterest = listAreaOfInterest;
	}

	public List<State> getListState() {
		return listState;
	}

	public void setListState(List<State> listState) {
		this.listState = listState;
	}

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		init();
		loadBinder();
	}

	public void init() {
		setUserProfile();
		initLists();
	}

	private void initLists() {
		setListAreaOfInterest(getControl().initListAreaOfInterest());
		setListState(getControl().initListState());
	}

	private void setUserProfile() {
		entity = (UserBookway) getUserLogged();
	}

	public void updateAccount() {
		treatReturn(getControl().doAction("updateAccount"));
	}

	public void deleteAccount() {
		Return ret = new Return(true);
		ret = getControl().doAction("deleteAccount");
		if(ret.isValid())
			removeUserOfSession();			
		treatReturn(ret);
	}
	
	private void removeUserOfSession() {
		// TODO Auto-generated method stub
	}

	@Override
	public UserBookway getEntityObject() {
		return new UserBookway();
	}

	@Override
	public UserBookwayControl getControl() {
		return SpringFactory.getController("userBookwayControl", UserBookwayControl.class, ReflectionUtils.prepareDataForPersistence(this));
	}
	
	public void changePasswordUser(){
		Return ret = new Return(true);
		ret.concat(getControl().doAction("changePasswordUser"));
		if(ret.isValid())
			getComponentById(getComponent(), "frmChangePassword").detach();
		treatReturn(ret);
	}
}
