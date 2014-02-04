package br.ueg.tcc.bookway.view.composer;

import java.util.List;

import org.apache.commons.collections.ListUtils;
import org.springframework.context.annotation.Scope;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Checkbox;

import br.com.vexillum.model.Friendship;
import br.com.vexillum.util.ReflectionUtils;
import br.com.vexillum.util.Return;
import br.com.vexillum.util.SpringFactory;
import br.ueg.tcc.bookway.control.FriendshipBookwayControl;
import br.ueg.tcc.bookway.control.UserBookwayControl;
import br.ueg.tcc.bookway.model.UserBookway;

@SuppressWarnings({ "serial" })
@org.springframework.stereotype.Component
@Scope("prototype")
public class FriendshipBookwayComposer extends InitComposer<Friendship, FriendshipBookwayControl> {

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

	public void removeFriendOrCancelInvite(String idUser, Component comp) {
		setDataForFriendship(idUser);
		treatReturn(getControl().doAction("delete"));
		comp.detach();
		loadBinder();
	}

	public void addFriend(String idUser, Component comp) {
		setDataForFriendship(idUser);
		treatReturn(getControl().doAction("save"));
		comp.detach();
		loadBinder();
	}
	
	public void aceptInvite(String idUser, Component comp) {
		setDataForFriendship(idUser);
		setUser((UserBookway) getUserLogged());
		entity = (Friendship) getControl().searchMyPendentRequests().getList().get(0);
		treatReturn(getControl().doAction("activeFriend"));
		comp.detach();
		loadBinder();
	}
	
	private void setDataForFriendship(String idUser) {
		setOwner((UserBookway) getUserLogged());
		setFriend(getUserControl().getUserById(idUser));
	}

	@SuppressWarnings("unchecked")
	public void searchFriends() {
		resetResultListSearch();
		Return ret = new Return(true);
		setUser((UserBookway) getUserLogged());
		setSearchKey(searchKey == null ? "" : searchKey);
		if(!myFriends.isChecked()){
			ret.concat(getControl().searchUserForFriendShip());
			if(ret.isValid() && !ret.getList().isEmpty()){
				setUpListFriendshipInComponent(convertListUsersInPossibleFriendships((List<UserBookway>) ret.getList()), "resultSearch", component, "ItemFriend", null, false);				
			}
		} else
			setUpListFriendshipInComponent(extractUsersFriends(getAllMyFriends()), "resultSearch", component, "ItemFriend", null, true);
		
		loadBinder();
	}
	
	@SuppressWarnings("unchecked")
	private List<UserBookway> convertListUsersInPossibleFriendships(
			List<UserBookway> users) {
		List<UserBookway> actualUsersFriends = extractUsersFriends(getAllMyFriends());
		return ListUtils.removeAll(users, actualUsersFriends);
	}

	@Override
	public FriendshipBookwayControl getControl() {
		return SpringFactory.getController("friendshipBookwayControl",
				FriendshipBookwayControl.class,
				ReflectionUtils.prepareDataForPersistence(this));
	}
	
	public UserBookwayControl getUserControl() {
		return SpringFactory.getController("userBookwayControl",
				UserBookwayControl.class,
				ReflectionUtils.prepareDataForPersistence(this));
	}
}
