package br.ueg.tcc.bookway.view.composer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.zkoss.zk.ui.Component;

import br.com.vexillum.control.GenericControl;
import br.com.vexillum.model.Friendship;
import br.com.vexillum.model.ICommonEntity;
import br.com.vexillum.util.ReflectionUtils;
import br.com.vexillum.util.Return;
import br.com.vexillum.util.SpringFactory;
import br.ueg.tcc.bookway.control.FriendshipBookwayControl;
import br.ueg.tcc.bookway.control.RelationshipTextUserControl;
import br.ueg.tcc.bookway.control.TextControl;
import br.ueg.tcc.bookway.model.Study;
import br.ueg.tcc.bookway.model.Text;
import br.ueg.tcc.bookway.model.UserBookway;
import br.ueg.tcc.bookway.view.macros.ItemFriend;
import br.ueg.tcc.bookway.view.macros.ItemText;
import br.ueg.tcc.bookway.view.macros.MyFriend;
import br.ueg.tcc.bookway.view.macros.MyText;

@SuppressWarnings({ "serial" })
@org.springframework.stereotype.Component
@Scope("prototype")
public class InitComposer<E extends ICommonEntity, G extends GenericControl<E>>
		extends BaseComposer<E, G> {

	private List<Text> allMyTexts;
	private Text selectedText;
	private List<Study> myStudies;
	private List<Friendship> allMyFriends;
	private UserBookway user;
	
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
		super.doAfterCompose(comp);
		initUserData();
	}

	private void initUserData() {
		createListTextUser();
		createListFriendshipUser();
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
			setUpListFriendshipInComponent(getAllMyFriends(), "panelMyFriends", getComponent(),
					"MyFriend", 8, true);
		}
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
	 * M�todo para setar a lista de elementos visual que represetar�o o texto
	 * que serao criados pela pesquisa ou na inicializa��o, listandos todos os
	 * textos do usu�rio
	 * 
	 * @param textsUser
	 *            , lista de textos que ser�o representados
	 * @param idParent
	 *            , component que dever� ser o pai
	 * @param comp
	 *            , component da execu��o do composer
	 * @param nameComp
	 *            , nome do component que ser� criado
	 * @param sorted
	 *            , se os elementos que ir�o compor a lista ser�o sorteados
	 * @param numberOfElements
	 *            , numero de elementos que compor�o a lista
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
	
	/**M�todo respons�vel por criar os componentes que envolvem a amizade, seja os elementos
	 * da pesquisa de ou os amigos na p�gina inicial.
	 * @param list, a lista das amizades a serem mapeados
	 * @param idParent, id do component pai
	 * @param comp, instancia do component para o mapeamento
	 * @param nameComp, nome do componente a ser mapeado
	 * @param numberOfElements, n�mero dos elementos a serem mapeados
	 * @param hasFriend, se a amizade j� pertence ao usu�rio.
	 */
	public void setUpListFriendshipInComponent(List<Friendship> list, String idParent,
			Component comp, String nameComp, Integer numberOfElements, boolean hasFriend) {
		Component componentParent = getComponentById(idParent);
		if (numberOfElements != null && list.size() > numberOfElements)
			list = list.subList(0, numberOfElements);
		if (list != null && componentParent != null) {
			for (Friendship friend : list) {
				if (nameComp.equalsIgnoreCase("ItemFriend"))
					componentParent.appendChild(createItemFriend(friend, hasFriend));
				else
					componentParent.appendChild(createMyFriend(friend));

			}
		}
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
	
	private ItemFriend createItemFriend(Friendship friendship, boolean hasFriend) {
		return new ItemFriend((UserBookway) getUserLogged(), hasFriend);
	}

	private MyText createMyText(Text text) {
		MyText myText = new MyText();
		myText.setTitle(text.getTitle());
		myText.setDescription(text.getDescription());
		return myText;
	}

	private MyFriend createMyFriend(Friendship friendship) {
		MyFriend myFriend = new MyFriend();
		//TODO Colocar para carregar a foto do usu�rio, na pasta.
		myFriend.setImageUser("/images/noimage.png");
		myFriend.setTooltiptext(friendship.getFriend().getName());
		return myFriend;
	}
	
	private RelationshipTextUserControl getRelationshipTextUserControl() {
		return SpringFactory.getController("relationshipTextUserControl",
				RelationshipTextUserControl.class,
				ReflectionUtils.prepareDataForPersistence(this));
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
