package br.ueg.tcc.bookway.view.composer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Checkbox;

import br.com.vexillum.model.Friendship;
import br.com.vexillum.util.ReflectionUtils;
import br.com.vexillum.util.Return;
import br.com.vexillum.util.SpringFactory;
import br.ueg.tcc.bookway.control.FriendshipBookwayControl;
import br.ueg.tcc.bookway.model.UserBookway;

@SuppressWarnings({ "serial" })
@org.springframework.stereotype.Component
@Scope("prototype")
public class FriendshipBookwayComposer extends
		InitComposer<Friendship, FriendshipBookwayControl> {

	@Wire
	public Checkbox myFriends;

	private String searchKey;

	public String getSearchKey() {
		return searchKey;
	}

	public void setSearchKey(String searchKey) {
		this.searchKey = searchKey;
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

	@SuppressWarnings("unchecked")
	public void searchFriends() {
		Return ret = new Return(true);
		List<Friendship> friends = null;
		setUser((UserBookway) getUserLogged());
		setSearchKey(searchKey == null ? "" : searchKey);
		if(!myFriends.isChecked()){
			ret.concat(getControl().searchFriends());
			if(ret.isValid()){
				friends = new ArrayList<Friendship>();
				friends = (List<Friendship>) ret.getList();
			}
		} else
			friends = getAllMyFriends();
			
		if(friends != null){
			setUpListFriendshipInComponent(friends, "resultSearch", component, "ItemFriend", null, myFriends.isChecked());
		}
		loadBinder();
	}
	
	@Override
	public FriendshipBookwayControl getControl() {
		return SpringFactory.getController("friendshipBookwayControl",
				FriendshipBookwayControl.class,
				ReflectionUtils.prepareDataForPersistence(this));
	}
}
