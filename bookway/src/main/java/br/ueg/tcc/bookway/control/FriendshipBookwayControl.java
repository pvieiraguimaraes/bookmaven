package br.ueg.tcc.bookway.control;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import br.com.vexillum.control.FriendshipController;
import br.com.vexillum.model.UserBasic;
import br.com.vexillum.util.Return;
import br.ueg.tcc.bookway.model.UserBookway;

@Service
@Scope("prototype")
public class FriendshipBookwayControl extends FriendshipController {
	
	public FriendshipBookwayControl() {
		super();
	}

	/**Método implementado para buscar todos os usuários menos o usuario logad
	 * servirá para fornecer a lista de amigos para o user logado.
	 * @return
	 */
	public Return searchUserForFriendShip(){
		UserBookway user = (UserBookway) data.get("user");
		String searchKey = (String) data.get("searchKey");
		
		String sql = "FROM UserBookway u WHERE u.active = true AND "
				+ "u.name like '%" + searchKey + "%' AND u.id != '" + user.getId() + "'";
		data.put("sql", sql);
		return searchByHQL();
	}
	
	public Return searchExistRequest(){
		UserBasic user = (UserBasic) data.get("user");
		String sql = "FROM Friendship f "
					+ "WHERE f.friend = '" + user.getId() + "' AND f.active = false";
		data.put("sql", sql);
		return searchByHQL();
	}

	public Return searchMyPendentRequests() {
		UserBasic user = (UserBasic) data.get("user");
		UserBookway friend = (UserBookway) data.get("friend");
		
		String sql = "FROM Friendship f WHERE f.owner = '" + friend.getId()
				+ "' AND f.friend = '" + user.getId()
				+ "' AND f.active = false";
		
		data.put("sql", sql);
		
		return searchByHQL();
	}

	public Return searchMyPendentInvites() {
		UserBookway user = (UserBookway) data.get("user");
		UserBookway friend = (UserBookway) data.get("friend");

		String sql = "FROM Friendship f WHERE f.owner = '" + user.getId()
				+ "' AND f.friend = '" + friend.getId()
				+ "' AND f.active = false";
		data.put("sql", sql);
		
		return searchByHQL();
	}
}
