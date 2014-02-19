package br.ueg.tcc.bookway.control;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import br.com.vexillum.control.FriendshipController;
import br.com.vexillum.model.UserBasic;
import br.com.vexillum.util.Return;
import br.ueg.tcc.bookway.model.UserBookway;

/**
 * Controlador que herda as funcionalidades do controle gen�rico da arquitetura
 * e relaciona as regras de neg�cio referentes ao caso de uso Manter Amizades
 * 
 * @author pedro
 * 
 */
@Service
@Scope("prototype")
public class FriendshipBookwayControl extends FriendshipController {

	public FriendshipBookwayControl() {
		super();
	}

	/**
	 * M�todo implementado para buscar todos os usu�rios menos o usuario logado
	 * servir� para fornecer a lista de amigos para o user logado.
	 * 
	 * @return {@link Return}
	 */
	public Return searchUserForFriendShip() {
		UserBookway user = (UserBookway) data.get("user");
		String searchKey = (String) data.get("searchKey");

		String sql = "FROM UserBookway u WHERE u.active = true AND "
				+ "u.name like '%" + searchKey + "%' AND u.id != '"
				+ user.getId() + "'";
		data.put("sql", sql);
		return searchByHQL();
	}

	/**
	 * Busca por requisi��es pendentes para o usu�rio
	 * 
	 * @return {@link Return}
	 */
	public Return searchExistRequest() {
		UserBasic user = (UserBasic) data.get("user");
		String sql = "FROM Friendship f " + "WHERE f.friend = '" + user.getId()
				+ "' AND f.active = false";
		data.put("sql", sql);
		return searchByHQL();
	}

	/**
	 * Busca todos as requisi��es enviadas para o usu�rio em quest�o
	 * 
	 * @return {@link Return}
	 */
	public Return searchMyPendentRequests() {
		UserBasic user = (UserBasic) data.get("user");
		UserBookway friend = (UserBookway) data.get("friend");

		String sql = "FROM Friendship f WHERE f.owner = '" + friend.getId()
				+ "' AND f.friend = '" + user.getId()
				+ "' AND f.active = false";

		data.put("sql", sql);

		return searchByHQL();
	}

	/**
	 * Busca todos os convites feitos pelo usu�rio a outros usu�rios
	 * 
	 * @return {@link Return}
	 */
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
