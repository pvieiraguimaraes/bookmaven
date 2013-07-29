package br.ueg.tcc.bookway.view.composer;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.zkoss.zk.ui.Component;

import br.com.vexillum.util.ReflectionUtils;
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
		entity = (UserBookway) getUserInSession();
	}

	public void updateAccount() {
		treatReturn(getControl().updateAccount());
	}

	public void deleteAccount() {
		treatReturn(getControl().deleteAccount());
	}//TODO talvez será deslocado para o ConfigurationComposer
	
	@Override
	public UserBookway getEntityObject() {
		return new UserBookway();
	}

	@Override
	public UserBookwayControl getControl() {
		return SpringFactory.getController("userBookwayControl", UserBookwayControl.class, ReflectionUtils.prepareDataForPersistence(this));
	}

}
