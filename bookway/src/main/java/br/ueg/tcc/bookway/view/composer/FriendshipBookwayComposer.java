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

/**
 * Classe responsável pelas funcionalidades da camada de visão do caso de uso
 * Manter Amizades
 * 
 * @author pedro
 * 
 */
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

	/**
	 * Fornece chamada para o controlador para remover ou cancelar um convite
	 * enviado
	 * 
	 * @param idUser
	 *            , código do usuário
	 * @param comp
	 *            , elemento que deverá ser fechado na tela.
	 */
	public void removeFriendOrCancelInvite(String idUser, Component comp) {
		setDataForFriendship(idUser);
		treatReturn(getControl().doAction("delete"));
		comp.detach();
		loadBinder();
	}

	/**
	 * Realiza chamada para o controle para adição de um usuário
	 * 
	 * @param idUser
	 *            , código do usuário
	 * @param comp
	 *            , componente que deverá ser removido da tela.
	 */
	public void addFriend(String idUser, Component comp) {
		setDataForFriendship(idUser);
		treatReturn(getControl().doAction("save"));
		comp.detach();
		loadBinder();
	}

	/**
	 * Realiza a interação com o controlador para consolidar uma amizade com
	 * aceitação de um convite
	 * 
	 * @param idUser
	 *            , código do usuário
	 * @param comp
	 *            , componente que deverá ser fechado
	 */
	public void aceptInvite(String idUser, Component comp) {
		setDataForFriendship(idUser);
		setUser((UserBookway) getUserLogged());
		entity = (Friendship) getControl().searchMyPendentRequests().getList()
				.get(0);
		treatReturn(getControl().doAction("activeFriend"));
		comp.detach();
		loadBinder();
	}

	/**
	 * Seta os objetos para criação do relacionamento entre usuário
	 * 
	 * @param idUser
	 *            , codigo do usuário
	 */
	private void setDataForFriendship(String idUser) {
		setOwner((UserBookway) getUserLogged());
		setFriend(getUserControl().getUserById(idUser));
	}

	/**
	 * Faz chamada para o controle e busca todos os amigos do usuário
	 */
	@SuppressWarnings("unchecked")
	public void searchFriends() {
		resetResultListSearch();
		Return ret = new Return(true);
		setUser((UserBookway) getUserLogged());
		setSearchKey(searchKey == null ? "" : searchKey);
		if (!myFriends.isChecked()) {
			ret.concat(getControl().searchUserForFriendShip());
			if (ret.isValid() && !ret.getList().isEmpty()) {
				setUpListFriendshipInComponent(
						convertListUsersInPossibleFriendships((List<UserBookway>) ret
								.getList()), "resultSearch", component,
						"ItemFriend", null, false);
			}
		} else
			setUpListFriendshipInComponent(
					extractUsersFriends(getAllMyFriends()), "resultSearch",
					component, "ItemFriend", null, true);

		loadBinder();
	}

	/**
	 * Busca todos os usuários que ainda não são amigos do usuário logado
	 * 
	 * @param users
	 *            , lista com os usuários
	 * @return lista de usuários que podem se tornar amigos do usuário logado
	 */
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

	/**
	 * Retorna uma nova instância do controlador de usuários
	 * 
	 * @return nova instância do controle {@link UserBookwayControl}
	 */
	public UserBookwayControl getUserControl() {
		return SpringFactory.getController("userBookwayControl",
				UserBookwayControl.class,
				ReflectionUtils.prepareDataForPersistence(this));
	}
}
