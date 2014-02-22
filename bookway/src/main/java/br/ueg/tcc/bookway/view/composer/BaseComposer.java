package br.ueg.tcc.bookway.view.composer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Window;

import br.com.vexillum.control.GenericControl;
import br.com.vexillum.model.ICommonEntity;
import br.com.vexillum.util.ReflectionUtils;
import br.com.vexillum.util.Return;
import br.com.vexillum.util.SpringFactory;
import br.com.vexillum.vexsocial.view.composer.SocialComposer;
import br.ueg.tcc.bookway.control.ElementsItensStudyControl;
import br.ueg.tcc.bookway.control.NavigationStudyControl;
import br.ueg.tcc.bookway.control.RelationshipTextUserControl;
import br.ueg.tcc.bookway.control.StudyControl;
import br.ueg.tcc.bookway.control.TextControl;
import br.ueg.tcc.bookway.model.ElementText;
import br.ueg.tcc.bookway.model.ElementsItensStudy;
import br.ueg.tcc.bookway.model.Study;
import br.ueg.tcc.bookway.model.Text;
import br.ueg.tcc.bookway.model.UserBookway;
import br.ueg.tcc.bookway.model.enums.TypePrivacy;
import br.ueg.tcc.bookway.view.macros.ItemStudy;
import br.ueg.tcc.bookway.view.macros.ItemText;
import br.ueg.tcc.bookway.view.macros.MyText;

@SuppressWarnings("serial")
public abstract class BaseComposer<E extends ICommonEntity, G extends GenericControl<E>>
		extends SocialComposer<E, G> {

	/**
	 * Define o estilo do elmento como sublinhado
	 */
	private final String UNDER_ON = "text-decoration: underline;";

	/**
	 * Remove o estilo sublinhado de um elemento
	 */
	private final String UNDER_OFF = "text-decoration: none;";

	/**
	 * Checkbox que se for marcado a intera��o ser� realizada de acordo com a
	 * lista de textos do usu�rio
	 */
	@Wire
	public Checkbox myTexts;

	/**
	 * Composer executado anteriormente ao atual
	 */
	protected BaseComposer<E, G> parentComposer;

	/**
	 * Lista de elementos de texto selecionados
	 */
	protected List<ElementText> itensSelected;

	/**
	 * Lista que representa os Elementos de Texto que participar� da refer�ncia
	 */
	protected List<ElementText> itensSelectedForReference;

	/**
	 * Objeto de estudo na execu��o
	 */
	protected Study study;

	/**
	 * Lista de todos os textos do usu�rio
	 */
	private List<Text> allMyTexts;

	/**
	 * Lista de todos os estudos do usu�rio
	 */
	private List<Study> myStudies;

	/**
	 * Se verdadeiro determina que dever� ser continuado o estudo do �ltimo
	 * elemento que parou
	 */
	protected Boolean continueStudy;

	/**
	 * Verdadeiro se a execu��o da cria��o do ambiente de estudo � em modo de
	 * refer�ncia textual
	 */
	protected Boolean isTextReferenceMode;

	/**
	 * Mapa de objetos utilizado para transferir dados para a camada de controle
	 */
	protected HashMap<String, Object> newMap;

	/**
	 * Lista com todos os elementos de estudo mapeados para o estudo
	 */
	protected List<ItemStudy> itemStudies;

	/**
	 * Lista com todos os elementos de estudo selecionados
	 */
	protected List<ItemStudy> itemStudiesSelected;

	/**
	 * Lista de elementos selecionados no ambiente que ser�o utilizados para
	 * realizar a refer�ncia
	 */
	protected List<ItemStudy> itemStudiesSelectedForReference;
	/**
	 * Lista que representa o relacionamento entre os elementos do texto e os
	 * itens de estudo
	 */
	protected List<ElementsItensStudy> elementsItensStudies;

	/**
	 * Utilizado nos casos de uso dos Itens De Estudo, onde � necess�rio
	 * informar o tipo de visualiza��o PUBLICO / PRIVADO
	 */
	protected TypePrivacy typePrivacy = TypePrivacy.PRIVADO;

	/**
	 * Objeto texto selecionado
	 */
	private Text selectedText;

	public List<Text> getAllMyTexts() {
		return allMyTexts;
	}

	public List<Study> getMyStudies() {
		return myStudies;
	}

	public void setMyStudies(List<Study> myStudies) {
		this.myStudies = myStudies;
	}

	public void setAllMyTexts(List<Text> allMyTexts) {
		this.allMyTexts = allMyTexts;
	}

	public List<ElementText> getItensSelectedForReference() {
		return itensSelectedForReference;
	}

	public void setItensSelectedForReference(
			List<ElementText> itensSelectedForReference) {
		this.itensSelectedForReference = itensSelectedForReference;
	}

	public Text getSelectedText() {
		return selectedText;
	}

	public void setSelectedText(Text selectedText) {
		this.selectedText = selectedText;
	}

	public TypePrivacy getTypePrivacy() {
		return typePrivacy;
	}

	public List<ItemStudy> getItemStudiesSelectedForReference() {
		return itemStudiesSelectedForReference;
	}

	public void setItemStudiesSelectedForReference(
			List<ItemStudy> itemStudiesSelectedForReference) {
		this.itemStudiesSelectedForReference = itemStudiesSelectedForReference;
	}

	public Boolean getIsTextReferenceMode() {
		return isTextReferenceMode;
	}

	public void setIsTextReferenceMode(Boolean isTextReferenceMode) {
		this.isTextReferenceMode = isTextReferenceMode;
	}

	public void setTypePrivacy(TypePrivacy typePrivacy) {
		this.typePrivacy = typePrivacy;
	}

	public List<ElementsItensStudy> getElementsItensStudies() {
		return elementsItensStudies;
	}

	public void setElementsItensStudies(
			List<ElementsItensStudy> elementsItensStudies) {
		this.elementsItensStudies = elementsItensStudies;
	}

	public List<ItemStudy> getItemStudiesSelected() {
		return itemStudiesSelected;
	}

	public void setItemStudiesSelected(List<ItemStudy> itemStudiesSelected) {
		this.itemStudiesSelected = itemStudiesSelected;
	}

	public List<ItemStudy> getItemStudies() {
		return itemStudies;
	}

	public void setItemStudies(List<ItemStudy> itemStudies) {
		this.itemStudies = itemStudies;
	}

	public Boolean getContinueStudy() {
		return continueStudy;
	}

	public List<ElementText> getItensSelected() {
		return itensSelected;
	}

	public void setItensSelected(List<ElementText> itensSelected) {
		this.itensSelected = itensSelected;
	}

	public void setContinueStudy(Boolean continueStudy) {
		this.continueStudy = continueStudy;
	}

	public Study getStudy() {
		return study;
	}

	public void setStudy(Study study) {
		this.study = study;
	}

	public BaseComposer<E, G> getParentComposer() {
		return parentComposer;
	}

	public void setParentComposer(BaseComposer<E, G> parentComposer) {
		this.parentComposer = parentComposer;
	}

	public HashMap<String, Object> getNewMap() {
		return newMap;
	}

	public void setNewMap(HashMap<String, Object> newMap) {
		this.newMap = newMap;
	}

	@SuppressWarnings("unchecked")
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		initObjects();
		initUpdatePage();
		setParentComposer((BaseComposer<E, G>) arg.get("thisComposer"));
	}

	/**
	 * M�todo que realiza chamadas a camada de controladores e busca todos os
	 * textos do usu�rio.
	 * 
	 * @return lista de {@link Text} do usu�rio
	 */
	@SuppressWarnings("unchecked")
	public List<Text> getAllTextsOfUser() {
		Return retListText = new Return(true);
		List<Text> listTextUser = new ArrayList<>();
		newMap.put("userLogged", getUserLogged());
		retListText.concat(getTextControl(newMap).listTextsUser());
		if (retListText.isValid()) {
			listTextUser = (List<Text>) retListText.getList();
			retListText = new Return(true);
			retListText.concat(getRelationshipTextUserControl()
					.listTextAddOfUser());
		}
		if (retListText.isValid())
			listTextUser.addAll((List<Text>) retListText.getList());
		return listTextUser;
	}

	/**
	 * Carrega todos os textos do usu�rio
	 */
	public void loadListText() {
		List<Study> studies = getStudyControl(null).getMyStudies(
				(UserBookway) getUserLogged());
		List<Text> texts = getAllTextsOfUser();

		for (Study study : studies) {
			Text text = study.getText();
			if (!texts.contains(text)) {
				texts.remove(text);
			}
		}

		setAllMyTexts(texts);
		loadBinder();
	}

	/**
	 * M�todo que inicializa os objetos de acordo com execu��es de objetos na
	 * sess�o, onde faz a verifica��o da exist�ncia dos objetos em execu��es
	 * anteriores a atual.
	 */
	@SuppressWarnings("unchecked")
	private void initObjects() {
		if (arg.get("itensSelected") == null)
			itensSelected = new ArrayList<>();
		else
			itensSelected = (List<ElementText>) arg.get("itensSelected");

		if (arg.get("itensSelectedForReference") == null)
			itensSelectedForReference = new ArrayList<>();
		else
			itensSelectedForReference = (List<ElementText>) arg
					.get("itensSelectedForReference");

		if (arg.get("itemStudies") == null)
			itemStudies = new ArrayList<>();
		else
			itemStudies = (List<ItemStudy>) arg.get("itemStudies");

		if (arg.get("itemStudiesSelected") == null)
			itemStudiesSelected = new ArrayList<>();
		else
			itemStudiesSelected = (List<ItemStudy>) arg
					.get("itemStudiesSelected");

		if (itemStudiesSelectedForReference == null)
			itemStudiesSelectedForReference = new ArrayList<>();
		else
			itemStudiesSelectedForReference = (List<ItemStudy>) arg
					.get("itemStudiesSelectedForReference");

		if (arg.get("elementsItensStudies") == null)
			elementsItensStudies = new ArrayList<>();
		else
			elementsItensStudies = (List<ElementsItensStudy>) arg
					.get("elementsItensStudies");

		if (arg.get("study") == null)
			study = new Study();
		else
			study = (Study) arg.get("study");

		if (arg.get("continueStudy") == null)
			continueStudy = false;
		else
			continueStudy = (Boolean) arg.get("continueStudy");

		if (arg.get("isTextReferenceMode") == null)
			isTextReferenceMode = false;
		else
			isTextReferenceMode = (Boolean) arg.get("isTextReferenceMode");

		if (arg.get("newMap") == null)
			newMap = new HashMap<>();
		else
			newMap = (HashMap<String, Object>) arg.get("newMap");
	}

	/**
	 * Inicializa a p�gina de altera��o de entidade
	 */
	@SuppressWarnings("unchecked")
	protected void initUpdatePage() {
		if (arg.get("selectedEntity") != null) {
			entity = (E) ((E) arg.get("selectedEntity")).cloneEntity();
			update = true;
		}
	}

	/**
	 * Faz chamada da p�gina de altera��o de uma entidade
	 */
	public void callUpdateModal() {
		Return ret = validateSelectedEntity();
		if (ret.isValid()) {
			super.callModalWindow(getUpdatePage());
		}
	}

	/**
	 * Faz chamada da mensagem de desativa��o
	 */
	public void callDeactivationConfirmation() {
		super.showDeactivateConfirmation(getDeactivationMessage());
	}

	/**
	 * Faz chamada da mensagem de ativa��o
	 */
	public void callActivationConfirmation() {
		super.showActivateConfirmation(getActivationMessage());
	}

	/**
	 * M�todo que dever� ser implementado para definir a p�gina de altera��o de
	 * entidade
	 * 
	 * @return caminho da p�gina que dever� ser executada para altera��o
	 */
	protected abstract String getUpdatePage();

	/**
	 * Ao ser implementado retornar� a mensagem padronizada para a confirma��o
	 * da desativa��o de uma entidade
	 * 
	 * @return mensagem que ser� exibida para confirma��o de desativa��o
	 */
	protected abstract String getDeactivationMessage();

	/**
	 * Mensagem padronizada para ativa��o de uma entidade
	 * 
	 * @return mensagem em quest�o
	 */
	protected String getActivationMessage() {
		return "Voc� tem certeza que deseja ativar?";
	}

	/**
	 * Carrega os componentes presentes na tela do composer pai da execu��o
	 * atual
	 */
	public void loadBinderParentComposer() {
		getParentComposer().loadBinder();
	}

	/**
	 * Realiza a chamada da mensagem de confirma��o de exclus�o
	 */
	public void callDeletionConfirmation() {
		super.showDeleteConfirmation(getDeleteMessage());
	}

	/**
	 * Mensagem padr�o para confirmar a exclus�o
	 * 
	 * @return mensagem em quest�o
	 */
	protected String getDeleteMessage() {
		return "Confirma exclus�o?";
	}

	@Override
	public Return saveEntity() {
		Return ret = super.saveEntity();
		if (ret.isValid() && getUpdate()) {
			getParentComposer().loadBinder();
			((Window) getComponentById(component, "modalWindow")).detach();
		} else if (ret.isValid()) {
			clearForm();
		}
		loadBinder();
		return ret;
	}

	/**
	 * Limpa o formul�rio
	 */
	public void resetForm() {
		clearForm();
		loadBinder();
	}

	@Override
	public boolean doAction(String action) {
		if (!itensSelected.isEmpty() && study != null) {
			study.setLastElementStop(getMaxItemFromItensSelected(itensSelected));
			saveOrUpdateStudy();
		}
		return super.doAction(action);
	}

	/**
	 * Busca o "maior" item de uma lista de itens selecionados para ser definido
	 * como o �ltimo para a intera��o da parada de estudo.
	 * 
	 * @param itensSelected
	 *            , lista de itens selecionados
	 * @return o {@link ElementText} de maior ID para armazenamento.
	 */
	private ElementText getMaxItemFromItensSelected(
			List<ElementText> itensSelected) {
		ElementText aux, levelText = itensSelected.get(0);
		for (int i = 1; i < itensSelected.size(); i++) {
			aux = itensSelected.get(i);
			if (aux.getId() > levelText.getId())
				levelText = aux;
		}
		return levelText;
	}

	/**
	 * Faz a chamada o controle de Estudo para salvar ou atualizar o �ltimo
	 * elemento de parada
	 * 
	 * @return {@link Return}
	 */
	public Return saveOrUpdateStudy() {
		Return ret = new Return(true);
		newMap.put("entity", getStudy());

		ret.concat(getStudyControl(newMap).doAction("save"));
		return ret;
	}

	/**Realiza a chamada para a camada de controle para criar os relacionamentos necess�rios
	 * entre os elementos do texto e o estudo em quest�o
	 * @return {@link Return}
	 */
	public Return saveElementsItensStudy() {
		Return ret = new Return(true);
		List<ElementsItensStudy> list = getElementsItensStudies();

		if (list != null) {
			for (ElementsItensStudy elementsItensStudy : list) {
				newMap.put("entity", elementsItensStudy);
				ret.concat(getElementsItensStudyControl(newMap)
						.doAction("save"));
				elementsItensStudies = new ArrayList<ElementsItensStudy>();
				parentComposer.setElementsItensStudies(null);
			}
		} else
			ret.setValid(false);

		return ret;
	}

	/**Cria os �cones de estudo de acordo para uma lista de elementos informada
	 * @param list, lista de elementos que dever� ser inserido o �cone de estudo
	 */
	public void createIconsStudy(List<ItemStudy> list) {
		for (ItemStudy itemStudy : list) {
			Component comp = createComponentIconStudy(itemStudy);
			if (comp != null)
				itemStudy.appendChild(comp);
		}
	}

	/** Instancia o controle de {@link StudyControl}
	 * @param newMap
	 * @return nova inst�ncia do controlador {@link StudyControl}
	 */
	public StudyControl getStudyControl(HashMap<String, Object> newMap) {
		return SpringFactory.getController("studyControl", StudyControl.class,
				newMap);
	}

	/** Instancia o controle de {@link StudyControl}
	 * @param newMap
	 * @return nova inst�ncia do controlador {@link StudyControl}
	 */
	public ElementsItensStudyControl getElementsItensStudyControl(
			HashMap<String, Object> newMap) {
		return SpringFactory.getController("elementsItensStudyControl",
				ElementsItensStudyControl.class, newMap);
	}

	/** Instancia o controle de {@link StudyControl}
	 * @param newMap
	 * @return nova inst�ncia do controlador {@link StudyControl}
	 */
	public NavigationStudyControl getNavigationStudyControl(
			HashMap<String, Object> newMap) {
		return SpringFactory.getController("navigationStudyControl",
				NavigationStudyControl.class, newMap);
	}

	/** Instancia o controle de {@link StudyControl}
	 * @param newMap
	 * @return nova inst�ncia do controlador {@link StudyControl}
	 */
	public TextControl getTextControl(HashMap<String, Object> newMap) {
		return SpringFactory.getController("textControl", TextControl.class,
				newMap);
	}

	/**Altera o estilo de um item de estudo
	 * @param itemStudy, item que deve ser alterado seu estilo
	 */
	public void changeItemStyle(ItemStudy itemStudy) {
		String style = itemStudy.contentElement.getStyle();
		if (style == null)
			itemStudy.contentElement.setStyle(UNDER_ON);
		else {
			if (style.equalsIgnoreCase("") || style.contains(UNDER_OFF))
				itemStudy.contentElement.setStyle(style.replace(UNDER_OFF,
						UNDER_ON));
			else if (style.contains(UNDER_ON))
				itemStudy.contentElement.setStyle(style.replace(UNDER_ON,
						UNDER_OFF));
			else
				itemStudy.contentElement.setStyle(style.concat(UNDER_ON));
		}
	}

	/**
	 * Remove a sela��o dos itens de estudo selecionados
	 */
	public void resetStyleItens() {
		List<ItemStudy> list = getItemStudiesSelected();
		for (ItemStudy itemStudy : list) {
			changeItemStyle(itemStudy);
		}
		itemStudiesSelected = new ArrayList<>();
		setItensSelected(new ArrayList<ElementText>());
		checkPanelActionVisibility();
	}

	/**Verifica se existem itens selecionados para serem marcados e direciona para executar a marca��o
	 * @param itemStudiesSelected, lista de itens selecionados durante o estudo
	 * @param color, cor deseja para a marca��o
	 */
	public void changeStyleMarkingInItens(List<ItemStudy> itemStudiesSelected,
			String color) {
		for (ItemStudy itemStudy : itemStudiesSelected) {
			changeStyleMarkingThisItem(itemStudy, color);
		}
		parentComposer.setItemStudiesSelected(new ArrayList<ItemStudy>());
		parentComposer.setItensSelected(new ArrayList<ElementText>());
		parentComposer.checkPanelActionVisibility();
	}

	/**Altera o cor de fundo de um elemento de acordo com a anota��o inserida
	 * @param itemStudy, item que dever� ser colocado a anota��o
	 * @param color, cor que ser� representada o item.
	 */
	public void changeStyleMarkingThisItem(ItemStudy itemStudy, String color) {
		String style = "background-color: " + color + ";";
		String styleItem = itemStudy.contentElement.getStyle();
		if (!styleItem.isEmpty())
			style += styleItem;
		itemStudy.contentElement.setStyle(style);
	}

	/**
	 * Verica se existem itens de estudo selecionados e caso exista faz chamada do m�todo para alterar a visibilidade
	 */
	public void checkPanelActionVisibility() {
		if (getItemStudiesSelected().isEmpty())
			changeVisibilityPanelAction(false);
		else
			changeVisibilityPanelAction(true);
	}

	/**Altera a visibilidado do painel de a��es caso exista na p�gina
	 * @param visibility, visibilidade que se deseja para o painel
	 */
	public void changeVisibilityPanelAction(boolean visibility) {
		Component panel = getComponentById(component, "panelActions");
		if (panel != null)
			panel.setVisible(visibility);
		else {
			if (parentComposer != null)
				panel = parentComposer.getComponentById(component,
						"panelActions");
			if (panel != null)
				panel.setVisible(visibility);
		}
	}

	/**Adiciona um icone de estudo a um item do estudo.
	 * @param itemStudy, item que ser� adicionado
	 * @param idLevel, c�digo que representa o elemento do texto interado
	 * @param hasItensStudies, se verdadeiro exitem elementos de estudo no item espec�ficado denvendo ent�o ser mapeando um �cone para representar essa exist�ncia 
	 * @return {@link ItemStudy} com informa��es dos elementos setadas e pronto para intera��o do usu�rio
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ItemStudy insertIconStudy(ItemStudy itemStudy, Long idLevel,
			boolean hasItensStudies) {
		itemStudy.setIdIconStudy("idIconStudy" + idLevel.toString());

		if (hasItensStudies)
			itemStudy.appendChild(createComponentIconStudy(itemStudy));

		itemStudy.addEventListener(Events.ON_MOUSE_OUT, new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getTarget() != null) {
					ItemStudy item = (ItemStudy) event.getTarget();

					Image imageChild = ((Image) getComponentById(item
							.getIdIconStudy()));
					if (imageChild != null)
						imageChild.setVisible(false);
				}
			}

		});

		itemStudy.addEventListener(Events.ON_MOUSE_OVER, new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getTarget() != null) {
					ItemStudy item = (ItemStudy) event.getTarget();

					Image imageChild = ((Image) getComponentById(item
							.getIdIconStudy()));

					if (imageChild != null)
						imageChild.setVisible(true);
				}
			}

		});

		return itemStudy;
	}

	/**Cria uma tab de acordo com um nome passado
	 * @param nameTab, nome que ser� utiizado na cria��o
	 * @return component tab criado
	 */
	public Component createTabWithName(String nameTab) {
		Tab tab = new Tab();
		tab.setLabel(nameTab);
		return tab;
	}

	/**Utilizado para criar um novo componente do tipo �cone de estudo que ser� utilizado para repreentar
	 * a exist�ncia de elementos de estudo inseridos no estudo.
	 * @param itemStudy, que ser� utilizado para obter as informa��es necess�rias
	 * @return um componente mapeado para o ambiente de estudo.
	 */
	public Component createComponentIconStudy(ItemStudy itemStudy) {
		newMap.put("idIconStudy", itemStudy.getIdIconStudy());
		newMap.put("idElementText", itemStudy.getIdElement());
		List<Component> children = itemStudy.getChildren();
		for (Component component : children) {
			if (component instanceof ItemStudy)
				return null;
		}
		return Executions.createComponents(
				"/template/component/iconItemStudy.zul", itemStudy, newMap);
	}

	/**Devolve uma lista de tipos de privacidade
	 * @return lista com os tipos {@link TypePrivacy}
	 */
	public List<TypePrivacy> getListTypesPrivacy() {
		return Arrays.asList(TypePrivacy.values());
	}

	/**
	 * Remove todos os componentes "filhos" do painel de resultado de busca.
	 */
	public void resetResultListSearch() {
		Component resultSearch = getComponentById(getComponent(),
				"resultSearch");
		if (resultSearch != null)
			Components.removeAllChildren(resultSearch);
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

	/**
	 * M�todo que cria o objeto de estudo caso n�o existe salva no banco e
	 * retorna ele para o usu�rio, para se relacionar com os outros objetos
	 * 
	 * @param text
	 * @return {@link Study} que est� sendo realizado
	 */
	public Study createStudy(Text text) {
		study = new Study();
		study.setDateStudy(new Date());
		study.setText(text);
		study.setUserBookway((UserBookway) getUserLogged());

		Return ret = saveOrUpdateStudy();

		if (ret.isValid())
			return getStudyControl(null).getThisStudy(study);
		return null;
	}

	/**Cria um item que representar� o texto na vis�o na tela de busca de textos 
	 * @param text, texto de onde ser�o retiradas as informa��es
	 * @return {@link ItemText} com os dados do usu�rio preenchidos.
	 */
	private ItemText createItemText(Text text) {
		setSelectedText(text);
		boolean has = getRelationshipTextUserControl().verifyUserHasText();
		ItemText item = new ItemText((UserBookway) userLogged, text, has,
				isTextReferenceMode);
		String userOwning = text.getUserOwning() != null ? text.getUserOwning()
				.getName() : "";
		item.setUser(userOwning);
		item.setTitle(text.getTitle());
		item.setDescription(text.getDescription());
		item.setIdText(String.valueOf(text.getId()));
		return item;
	}

	/**Cria o componento de representa��o do texto no painel dos meus textos
	 * @param text, objeto de onde ser�o obtidas todas as informa��es
	 * @return {@link MyText} com os dados do texto preenchidos
	 */
	private MyText createMyText(Text text) {
		MyText myText = new MyText();
		myText.setTitle(text.getTitle());
		myText.setDescription(text.getDescription());
		return myText;
	}

	/**Retorna uma inst�ncia do coontrolador do caso de uso que manter relacionamento entre 
	 * os textos e os usu�rios.
	 * @return {@link RelationshipTextUserControl} uma nova inst�ncia dessse controlador
	 */
	public RelationshipTextUserControl getRelationshipTextUserControl() {
		return SpringFactory.getController("relationshipTextUserControl",
				RelationshipTextUserControl.class,
				ReflectionUtils.prepareDataForPersistence(this));
	}
}
