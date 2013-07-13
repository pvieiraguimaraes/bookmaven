package br.ueg.tcc.bookway.view.composer;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;

import br.com.vexillum.util.ReflectionUtils;
import br.com.vexillum.util.Return;
import br.com.vexillum.util.SpringFactory;
import br.com.vexillum.view.CRUDComposer;
import br.ueg.tcc.bookway.control.UserControl;
import br.ueg.tcc.bookway.model.UserBookway;
import br.ueg.tcc.bookway.model.enums.AreaOfInterest;
import br.ueg.tcc.bookway.model.enums.State;

@SuppressWarnings("serial")
@org.springframework.stereotype.Component
@Scope("prototype")//poderá ser prototype
public class UserComposer extends CRUDComposer<UserBookway, UserControl> {

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
		loadBinder();
	}
	
	public void register() {
		Return retRegister = new Return(true);
		retRegister = getControl().registerUser();
		if(retRegister.isValid()) resetEntity(); //TODO Arrumar uma maneira para resetar o objeto
		treatReturn(retRegister);
	}

	public void resetEntity(){
		entity = null;
		binder.loadAll();
	}
	
	public void redirectPerfil() {
		Executions.sendRedirect("/pages/user/perfil.zul");
	}

	/**
	 * feito só pra testar se o editar esta funcionando
	 */
	//TODO Remover após conseguir pegar o usuário da sessão corretamente
	public void testeEditar() {
		somenteParaTeste();
		initLists();
		binder.loadAll();
	}

	private void initLists() {
		setListAreaOfInterest(getControl().initListAreaOfInterest());
		setListState(getControl().initListState());
		
	}

	//TODO Remover após conseguir pegar o usuário da sessão corretamente
	private void somenteParaTeste() {
//		entity.setName("Juliana");
//		searchEntitys();
//		entity = getListEntity().get(0);
		entity = (UserBookway) getUserInSession();
	}

	public void updateAccount() {
		treatReturn(getControl().updateAccount());
	}

	public void deleteAccount() {
		somenteParaTeste();
		treatReturn(getControl().deleteAccount());
	}
	
	@Override
	public UserBookway getEntityObject() {
		return new UserBookway();
	}

	@Override
	public UserControl getControl() {
		return SpringFactory.getController("userControl", UserControl.class, ReflectionUtils.prepareDataForPersistence(this));
	}

}
