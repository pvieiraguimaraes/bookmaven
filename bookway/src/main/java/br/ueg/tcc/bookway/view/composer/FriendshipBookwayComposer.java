package br.ueg.tcc.bookway.view.composer;

import org.springframework.context.annotation.Scope;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Checkbox;

import br.com.vexillum.model.Friendship;
import br.ueg.tcc.bookway.control.FriendshipBookwayControl;

@SuppressWarnings({ "serial" })
@org.springframework.stereotype.Component
@Scope("prototype")
public class FriendshipBookwayComposer extends InitComposer<Friendship, FriendshipBookwayControl> {

	@Wire
	public Checkbox myFriends;
	
	private String searcKeyUser;
	
	public String getSearcKeyUser() {
		return searcKeyUser;
	}

	public void setSearcKeyUser(String searcKeyUser) {
		this.searcKeyUser = searcKeyUser;
	}

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		createListFriendshipUser();
		loadBinder();
	}
	
	public void removeFriend(String idUser) {

	}

	public void addFriend(String idUser) {

	}
	
	public void searchFriends(){
		
	}
}
