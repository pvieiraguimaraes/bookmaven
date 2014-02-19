package br.ueg.tcc.bookway.control;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import br.com.vexillum.control.FriendshipController;
import br.com.vexillum.model.UserBasic;
import br.com.vexillum.util.Return;
import br.ueg.tcc.bookway.model.UserBookway;

/**
 * Controlador que herda as funcionalidades do controle genérico da arquitetura
 * e relaciona as regras de negócio referentes ao caso de uso Manter Amizades
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
	 * Método implementado para buscar todos os usuários menos o usuario logado
	 * servirá para fornecer a lista de amigos para o user logado.
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
	 * Busca por requisições pendentes para o usuário
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
	 * Busca todos as requisições enviadas para o usuário em questão
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
	 * Busca todos os convites feitos pelo usuário a outros usuários
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
