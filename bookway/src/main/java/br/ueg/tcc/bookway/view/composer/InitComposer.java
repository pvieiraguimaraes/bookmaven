package br.ueg.tcc.bookway.view.composer;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.zkoss.image.AImage;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Image;
import org.zkoss.zul.Separator;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Tabpanels;
import org.zkoss.zul.Tabs;

import br.com.vexillum.control.GenericControl;
import br.com.vexillum.control.manager.ExceptionManager;
import br.com.vexillum.control.util.Attachment;
import br.com.vexillum.model.Friendship;
import br.com.vexillum.model.ICommonEntity;
import br.com.vexillum.util.HibernateUtils;
import br.com.vexillum.util.ReflectionUtils;
import br.com.vexillum.util.Return;
import br.com.vexillum.util.SpringFactory;
import br.ueg.tcc.bookway.control.FriendshipBookwayControl;
import br.ueg.tcc.bookway.control.RelationshipTextUserControl;
import br.ueg.tcc.bookway.control.TextControl;
import br.ueg.tcc.bookway.model.Study;
import br.ueg.tcc.bookway.model.Text;
import br.ueg.tcc.bookway.model.UserBookway;
import br.ueg.tcc.bookway.model.enums.TypePrivacy;
import br.ueg.tcc.bookway.utils.AttachmentMedia;
import br.ueg.tcc.bookway.view.macros.ItemFriend;
import br.ueg.tcc.bookway.view.macros.ItemText;
import br.ueg.tcc.bookway.view.macros.MyFriend;
import br.ueg.tcc.bookway.view.macros.MyText;

@SuppressWarnings({ "serial" })
@org.springframework.stereotype.Component
@Scope("prototype")
public class InitComposer<E extends ICommonEntity, G extends GenericControl<E>>
		extends BaseComposer<E, G> {

	@Wire
	protected Checkbox chckCommunity;
	@Wire
	protected Checkbox chckTypeText;
	@Wire
	protected Combobox fldTypeText;
	
	private List<Text> allMyTexts;
	
	private Text selectedText;
	
	private List<Study> myStudies;
	
	private List<Friendship> allMyFriends;
	
	private List<Friendship> invitesList;
	
	private List<Friendship> requestsList;
	
	public List<Friendship> getInvitesList() {
		return invitesList;
	}

	public void setInvitesList(List<Friendship> invitesList) {
		this.invitesList = invitesList;
	}

	public List<Friendship> getRequestsList() {
		return requestsList;
	}

	public void setRequestsList(List<Friendship> requestsList) {
		this.requestsList = requestsList;
	}

	/**
	 * Fornece a lista de tipo de texto, Público ou Privado
	 */
	private List<TypePrivacy> listTypesText;
	
	public List<TypePrivacy> getListTypesText() {
		return listTypesText;
	}

	public void setListTypesText(List<TypePrivacy> listTypesText) {
		this.listTypesText = listTypesText;
	}

	private UserBookway user;
	
	protected UserBookway owner;
	
	protected UserBookway friend;

	public UserBookway getOwner() {
		return owner;
	}

	public void setOwner(UserBookway owner) {
		this.owner = owner;
	}

	public UserBookway getFriend() {
		return friend;
	}

	public void setFriend(UserBookway friend) {
		this.friend = friend;
		
	}
	
	public UserBookway getUser() {
		return user;
	}

	public void setUser(UserBookway user) {
		this.user = user;
	}

	public List<Friendship> getAllMyFriends() {
		return allMyFriends;
	}

	public void setAllMyFriends(List<Friendship> allMyFriends) {
		this.allMyFriends = allMyFriends;
	}

	public List<Study> getMyStudies() {
		return myStudies;
	}

	public void setMyStudies(List<Study> myStudies) {
		this.myStudies = myStudies;
	}

	public Text getSelectedText() {
		return selectedText;
	}

	public void setSelectedText(Text selectedText) {
		this.selectedText = selectedText;
	}

	public List<Text> getAllMyTexts() {
		return allMyTexts;
	}

	public void setAllMyTexts(List<Text> allMyTexts) {
		this.allMyTexts = allMyTexts;
	}

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		String page = Executions.getCurrent().getDesktop().getRequestPath();
		super.doAfterCompose(comp);
		if(page.equalsIgnoreCase("/pages/user/index.zul")){
			loadRequestAndInvites();
		}
		initUserData();
	}

	private void loadRequestAndInvites() {
		loadListRequestsInvites();
		createElementsInvitesRequests();
	}

	@SuppressWarnings("unchecked")
	private void loadListRequestsInvites() {
		setUser((UserBookway) getUserLogged());
		Return ret = getFriendshipControl().searchPendentInvites();
		if(ret.isValid())
			setInvitesList((List<Friendship>) ret.getList());
		ret = new Return(true);
		ret.concat(getFriendshipControl().searchFriendRequests());
		if(ret.isValid())
			setRequestsList((List<Friendship>) ret.getList());
		
	}

	private void createElementsInvitesRequests() {
		Div div = (Div) getComponentById(component, "panelRequestsInvites");
		Tabbox tabbox =  new Tabbox();
		if(div != null){
			div.appendChild(tabbox);
			Tabs tabs = tabbox.getTabs();
			Tabpanels tabpanels = tabbox.getTabpanels();
			Separator separator = new Separator();
			separator.setOrient("horizontal");
			separator.setWidth("30px");
			if (tabs == null)
				tabs = new Tabs();
			if (tabpanels == null)
				tabpanels = new Tabpanels();
			if (!invitesList.isEmpty()) {
				tabs.appendChild(createTabWithName("Convites Enviados"));
				Tabpanel tabpanelInvitess = new Tabpanel();
				tabpanelInvitess.appendChild(separator);
				setUpListFriendshipInComponent(extractUsersFriends(invitesList), null, tabpanelInvitess, "ItemFriend", null, false);
				tabpanelInvitess.appendChild(separator);
				tabpanels.appendChild(tabpanelInvitess);
			}
			if (!requestsList.isEmpty()) {
				tabs.appendChild(createTabWithName("Solicitações Pendentes"));
				Tabpanel tabpanelRequests = new Tabpanel();
				tabpanelRequests.appendChild(separator);
				setUpListFriendshipInComponent(extractUsersFriends(requestsList), null, tabpanelRequests, "ItemFriend", null, false);
				tabpanelRequests.appendChild(separator);
				tabpanels.appendChild(tabpanelRequests);
			}
			tabbox.appendChild(tabs);
			tabbox.appendChild(tabpanels);
		}
	}

	private void initUserData() {
		Image image = (Image) getComponentById("photoUserbookway");
		UserBookway userProfile = (UserBookway) getUserLogged();
		if(image != null && userProfile != null)
			showUserBookwayPhoto(image, userProfile);
		createListTextUser();
		createListFriendshipUser();
		initListTypeText();
	}
	
	public void initListTypeText() {
		setListTypesText(getControlText().initTypesText());
	}

	@SuppressWarnings("unchecked")
	public void createListTextUser() {
		Return retListText = new Return(true);
		List<Text> listTextUser = new ArrayList<>();
		retListText.concat(getControlText().listTextsUser());
		if (retListText.isValid()) {
			listTextUser = (List<Text>) retListText.getList();
			retListText = new Return(true);
			retListText.concat(getRelationControl().listTextAddOfUser());
		}
		if (retListText.isValid())
			listTextUser.addAll((List<Text>) retListText.getList());
		setAllMyTexts(listTextUser);
		setUpListTextInComponent(getAllMyTexts(), "panelMyTexts", getComponent(),
				"MyText", true, 4);
	}
	
	@SuppressWarnings("unchecked")
	public void createListFriendshipUser() {
		Return ret = new Return(true);
		setUser((UserBookway) getUserLogged());
		ret.concat(getFriendshipControl().searchAllFriends());
		if(ret.isValid() && ret.getList() != null && !ret.getList().isEmpty()){
			setAllMyFriends((List<Friendship>) ret.getList());
			setUpListFriendshipInComponent(extractUsersFriends(getAllMyFriends()), "panelMyFriends", getComponent(),	"MyFriend", 8, true);
		}
	}
	
	public List<UserBookway> extractUsersFriends(List<Friendship> friendships) {
		List<UserBookway> friends = new ArrayList<UserBookway>();
		UserBookway uo = new UserBookway(), uf = new UserBookway();
		if(friendships != null){
			for (Friendship friendship : friendships) {
				uf = (UserBookway) HibernateUtils.materializeProxy(friendship.getFriend());
				uo = (UserBookway) HibernateUtils.materializeProxy(friendship.getOwner());
				if (getUserLogged() != uf)
					friends.add(uf);
				if (getUserLogged() != uo)
					friends.add(uo);
			}
		}
		return friends;
	}

	private TextControl getControlText() {
		return SpringFactory.getController("textControl", TextControl.class,
				ReflectionUtils.prepareDataForPersistence(this));
	}

	private RelationshipTextUserControl getRelationControl() {
		return SpringFactory.getController("relationshipTextUserControl",
				RelationshipTextUserControl.class,
				ReflectionUtils.prepareDataForPersistence(this));
	}
	
	private FriendshipBookwayControl getFriendshipControl() {
		return SpringFactory.getController("friendshipBookwayControl",
				FriendshipBookwayControl.class,
				ReflectionUtils.prepareDataForPersistence(this));
	}

	/**
	 * Método para setar a lista de elementos visual que represetarão o texto
	 * que serao criados pela pesquisa ou na inicialização, listandos todos os
	 * textos do usuário
	 * 
	 * @param textsUser
	 *            , lista de textos que serão representados
	 * @param idParent
	 *            , component que deverá ser o pai
	 * @param comp
	 *            , component da execução do composer
	 * @param nameComp
	 *            , nome do component que será criado
	 * @param sorted
	 *            , se os elementos que irão compor a lista serão sorteados
	 * @param numberOfElements
	 *            , numero de elementos que comporão a lista
	 */
	public void setUpListTextInComponent(List<Text> textsUser, String idParent,
			Component comp, String nameComp, boolean sorted,
			Integer numberOfElements) {
		Component componentParent = getComponentById(comp, idParent);
		if (sorted)
			Collections.shuffle(textsUser);
		if (numberOfElements != null && textsUser.size() > numberOfElements)
			textsUser = textsUser.subList(0, numberOfElements);
		if (textsUser != null && componentParent != null) {
			for (Text text : textsUser) {
				if (nameComp.equalsIgnoreCase("ItemText"))
					componentParent.appendChild(createItemText(text));
				else
					componentParent.appendChild(createMyText(text));

			}
		}
	}
	
	/**Método responsável por criar os componentes que envolvem a amizade, seja os elementos
	 * da pesquisa de ou os amigos na página inicial. Os components criados serão filhos do component
	 * informado em idParent que caso seja null poderá ser substituido por comp.
	 * @param list, a lista das amizades a serem mapeados
	 * @param idParent, id do component pai
	 * @param comp, instancia do component para o mapeamento
	 * @param nameComp, nome do componente a ser mapeado
	 * @param numberOfElements, número dos elementos a serem mapeados
	 * @param hasFriend, se a amizade já pertence ao usuário.
	 */
	public void setUpListFriendshipInComponent(List<UserBookway> list,
			String idParent, Component comp, String nameComp,
			Integer numberOfElements, boolean hasFriend) {
		Component componentParent;
		if (idParent != null)
			componentParent = getComponentById(idParent);
		else
			componentParent = comp;

		String existInvite = "";
		if (componentParent != null) {
			if (numberOfElements != null && list.size() > numberOfElements)
				list = list.subList(0, numberOfElements);
			if (list != null && componentParent != null) {
				for (UserBookway friend : list) {
					if (nameComp.equalsIgnoreCase("ItemFriend")) {
						existInvite = checkInvitation(friend);
						componentParent.appendChild(createItemFriend(friend,
								hasFriend, existInvite));
					} else
						componentParent.appendChild(createMyFriend(friend));

				}
			}
		}
	}

	private String checkInvitation(UserBookway friend) {
		setOwner((UserBookway) getUserLogged());
		setFriend(friend);
		Return ret = getFriendshipControl().searchMyPendentInvites();
		if(ret.isValid() && !ret.getList().isEmpty())
			return "MY_INVITE";
		ret.concat(getFriendshipControl().searchMyPendentRequests());
		if(ret.isValid() && !ret.getList().isEmpty())
			return "OTHER_INVITE";
		return "";
	}

	private ItemText createItemText(Text text) {
		setSelectedText(text);
		boolean has = getRelationshipTextUserControl().verifyUserHasText();
		ItemText item = new ItemText((UserBookway) userLogged, text, has);
		String userOwning = text.getUserOwning() != null ? text.getUserOwning()
				.getName() : "";
		item.setUser(userOwning);
		item.setTitle(text.getTitle());
		item.setDescription(text.getDescription());
		item.setIdText(String.valueOf(text.getId()));
		return item;
	}
	
	private ItemFriend createItemFriend(UserBookway friendship, boolean hasFriend, String existInvite) {
		ItemFriend item = new ItemFriend(friendship, hasFriend, existInvite);
		showUserBookwayPhoto(item.imageFriend, friendship);
		return item;
	}

	private MyText createMyText(Text text) {
		MyText myText = new MyText();
		myText.setTitle(text.getTitle());
		myText.setDescription(text.getDescription());
		return myText;
	}

	private MyFriend createMyFriend(UserBookway friendship) {
		MyFriend myFriend = new MyFriend();
		showUserBookwayPhoto(myFriend.imageUser, friendship);
		myFriend.setTooltiptext(friendship.getName());
		return myFriend;
	}
	
	private RelationshipTextUserControl getRelationshipTextUserControl() {
		return SpringFactory.getController("relationshipTextUserControl",
				RelationshipTextUserControl.class,
				ReflectionUtils.prepareDataForPersistence(this));
	}
	
	public void resetResultListSearch() {
		Component resultSearch = getComponentById(getComponent(),
				"resultSearch");
		if (resultSearch != null)
			Components.removeAllChildren(resultSearch);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void showUserBookwayPhoto(Image comp, UserBookway userBookway) {
		Attachment att = new AttachmentMedia();
		try {
			File image = att.getAttachment("photo", userBookway);
			if (image != null)
				comp.setContent(new AImage(image));
		} catch (Exception e) {
			new ExceptionManager(e).treatException();
		}
	}

	@Override
	protected String getUpdatePage() {
		return null;
	}

	@Override
	protected String getDeactivationMessage() {
		return null;
	}

	@Override
	public G getControl() {
		return null;
	}

	@Override
	public E getEntityObject() {
		return null;
	}

}
