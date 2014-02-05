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
import br.com.vexillum.view.CRUDComposer;
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
		extends CRUDComposer<E, G> {
	
	private final String UNDER_ON = "text-decoration: underline;";
	private final String UNDER_OFF = "text-decoration: none;";
	
	@Wire
	public Checkbox myTexts;

	protected BaseComposer<E, G> parentComposer;

	protected List<ElementText> itensSelected;

	/**
	 * Lista que representa os Elementos de Texto que participará da referência
	 */
	protected List<ElementText> itensSelectedForReference;
	
	protected Study study;
	
	protected Boolean continueStudy;
	
	protected Boolean isTextReferenceMode;
	
	protected HashMap<String, Object> newMap;
	
	protected List<ItemStudy> itemStudies;
	
	protected List<ItemStudy> itemStudiesSelected;

	protected List<ItemStudy> itemStudiesSelectedForReference;
	/**
	 * Lista que representa o relacionamento entre os elementos do texto e os itens de estudo
	 */
	protected List<ElementsItensStudy> elementsItensStudies;
	
	/**
	 * Utilizado nos casos de uso dos Itens De Estudo, onde é necessário informar o tipo de visualização PUBLICO / PRIVADO
	 */
	protected TypePrivacy typePrivacy = TypePrivacy.PRIVADO;
	
	private Text selectedText;
	
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

	@SuppressWarnings("unchecked")
	private void initObjects() {
		if (arg.get("itensSelected") == null)
			itensSelected = new ArrayList<>();
		else
			itensSelected = (List<ElementText>) arg.get("itensSelected");
		
		if (arg.get("itensSelectedForReference") == null)
			itensSelectedForReference = new ArrayList<>();
		else
			itensSelectedForReference = (List<ElementText>) arg.get("itensSelectedForReference");

		if (arg.get("itemStudies") == null)
			itemStudies = new ArrayList<>();
		else
			itemStudies = (List<ItemStudy>) arg.get("itemStudies");
		
		if (arg.get("itemStudiesSelected") == null)
			itemStudiesSelected = new ArrayList<>();
		else
			itemStudiesSelected = (List<ItemStudy>) arg.get("itemStudiesSelected");
		
		if (itemStudiesSelectedForReference == null)
			itemStudiesSelectedForReference = new ArrayList<>();
		else
			itemStudiesSelectedForReference = (List<ItemStudy>) arg.get("itemStudiesSelectedForReference");
		
		if (arg.get("elementsItensStudies") == null)
			elementsItensStudies = new ArrayList<>();
		else
			elementsItensStudies = (List<ElementsItensStudy>) arg.get("elementsItensStudies");
		
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

	@SuppressWarnings("unchecked")
	protected void initUpdatePage() {
		if (arg.get("selectedEntity") != null) {
			entity = (E) ((E) arg.get("selectedEntity")).cloneEntity();
			update = true;
		}
	}

	public void callUpdateModal() {
		Return ret = validateSelectedEntity();
		if (ret.isValid()) {
			super.callModalWindow(getUpdatePage());
		}
	}

	public void callDeactivationConfirmation() {
		super.showDeactivateConfirmation(getDeactivationMessage());
	}

	public void callActivationConfirmation() {
		super.showActivateConfirmation(getActivationMessage());
	}

	protected abstract String getUpdatePage();

	protected abstract String getDeactivationMessage();

	protected String getActivationMessage() {
		return "Você tem certeza que deseja ativar?";
	}

	public void loadBinderParentComposer() {
		getParentComposer().loadBinder();
	}

	public void callDeletionConfirmation() {
		super.showDeleteConfirmation(getDeleteMessage());
	}

	protected String getDeleteMessage() {
		return "Confirma exclusão?";
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

	private ElementText getMaxItemFromItensSelected(List<ElementText> itensSelected) {
		ElementText aux, levelText = itensSelected.get(0);
		for (int i = 1; i < itensSelected.size(); i++) {
			aux = itensSelected.get(i);
			if(aux.getId() > levelText.getId())
				levelText = aux;
		}
		return levelText;
	}

	public Return saveOrUpdateStudy() {
		Return ret = new Return(true);
		newMap.put("entity", getStudy());

		ret.concat(getStudyControl(newMap).doAction("save"));
		return ret;
	}
	
	public Return saveElementsItensStudy(){
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
	
	public void createIconsStudy(List<ItemStudy> list) {
		for (ItemStudy itemStudy : list) {
			Component comp = createComponentIconStudy(itemStudy);
			if (comp != null)
				itemStudy.appendChild(comp);
		}
	}
	
	public StudyControl getStudyControl(HashMap<String, Object> newMap) {
		return SpringFactory.getController("studyControl", StudyControl.class,
				newMap);
	}
	
	public ElementsItensStudyControl getElementsItensStudyControl(HashMap<String, Object> newMap) {
		return SpringFactory.getController("elementsItensStudyControl", ElementsItensStudyControl.class,
				newMap);
	}
	
	public NavigationStudyControl getNavigationStudyControl(HashMap<String, Object> newMap) {
		return SpringFactory.getController("navigationStudyControl", NavigationStudyControl.class,
				newMap);
	}
	
	public TextControl getTextControl(HashMap<String, Object> newMap) {
		return SpringFactory.getController("textControl", TextControl.class,
				newMap);
	}
	
	public void changeItemStyle(ItemStudy itemStudy) {
		String style = itemStudy.contentElement.getStyle();
		if (style == null)
			itemStudy.contentElement.setStyle(UNDER_ON);
		else {
			if (style.equalsIgnoreCase("") || style.contains(UNDER_OFF))
				itemStudy.contentElement.setStyle(style.replace(UNDER_OFF, UNDER_ON));
			else if (style.contains(UNDER_ON))
				itemStudy.contentElement.setStyle(style.replace(UNDER_ON, UNDER_OFF));
			else
				itemStudy.contentElement.setStyle(style.concat(UNDER_ON));
		}
	}

	public void resetStyleItens() {
		List<ItemStudy> list = getItemStudiesSelected();
		for (ItemStudy itemStudy : list) {
			changeItemStyle(itemStudy);
		}
		itemStudiesSelected = new ArrayList<>();
		setItensSelected(new ArrayList<ElementText>());
		checkPanelActionVisibility();
	}
	
	public void changeStyleMarkingInItens(List<ItemStudy> itemStudiesSelected, String color) {
		for (ItemStudy itemStudy : itemStudiesSelected) {
			changeStyleMarkingThisItem(itemStudy, color);
		}
		parentComposer.setItemStudiesSelected(new ArrayList<ItemStudy>());
		parentComposer.setItensSelected(new ArrayList<ElementText>());
		parentComposer.checkPanelActionVisibility();
	}
	
	public void changeStyleMarkingThisItem(ItemStudy itemStudy, String color){
		String style = "background-color: " + color + ";";
		itemStudy.contentElement.setStyle(style);
	}

	public void checkPanelActionVisibility() {
		if (getItemStudiesSelected().isEmpty())
			changeVisibilityPanelAction(false);
		else
			changeVisibilityPanelAction(true);
	}

	public void changeVisibilityPanelAction(boolean visibility) {
		Component panel = getComponentById(component, "panelActions");
		if (panel != null)
			panel.setVisible(visibility);
		else {
			if(parentComposer != null)
				panel = parentComposer.getComponentById(component, "panelActions");
			if (panel != null)
				panel.setVisible(visibility);
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ItemStudy insertIconStudy(ItemStudy itemStudy, Long idLevel, boolean hasItensStudies) {
		itemStudy.setIdIconStudy("idIconStudy" + idLevel.toString());

		if(hasItensStudies)
			itemStudy.appendChild(createComponentIconStudy(itemStudy));

		itemStudy.addEventListener(Events.ON_MOUSE_OUT, new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getTarget() != null) {
					ItemStudy item = (ItemStudy) event.getTarget();

					Image imageChild = ((Image) getComponentById(item
							.getIdIconStudy()));
					if(imageChild != null)
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
					
					if(imageChild != null)
						imageChild.setVisible(true);
				}
			}

		});
		
		return itemStudy;
	}
	
	public Component createTabWithName(String nameTab) {
		Tab tab = new Tab();
		tab.setLabel(nameTab);
		return tab;
	}

	public Component createComponentIconStudy(ItemStudy itemStudy) {
		newMap.put("idIconStudy", itemStudy.getIdIconStudy());
		newMap.put("idElementText", itemStudy.getIdElement());
		List<Component> children = itemStudy.getChildren();
		for (Component component : children) {
			if(component instanceof ItemStudy)
				return null;
		}
		return Executions.createComponents("/template/component/iconItemStudy.zul", itemStudy, newMap);
	}
	
	public List<TypePrivacy> getListTypesPrivacy() {
		return Arrays.asList(TypePrivacy.values());
	}
	
	public void resetResultListSearch() {
		Component resultSearch = getComponentById(getComponent(),
				"resultSearch");
		if (resultSearch != null)
			Components.removeAllChildren(resultSearch);
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
	
	/**Método que cria o objeto de estudo caso não existe salva no banco e
	 * retorna ele para o usuário, para se relacionar com os outros objetos
	 * @param text
	 * @return
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
	
	private ItemText createItemText(Text text) {
		setSelectedText(text);
		boolean has = getRelationshipTextUserControl().verifyUserHasText();
		ItemText item = new ItemText((UserBookway) userLogged, text, has, isTextReferenceMode);
		String userOwning = text.getUserOwning() != null ? text.getUserOwning()
				.getName() : "";
		item.setUser(userOwning);
		item.setTitle(text.getTitle());
		item.setDescription(text.getDescription());
		item.setIdText(String.valueOf(text.getId()));
		return item;
	}

	private MyText createMyText(Text text) {
		MyText myText = new MyText();
		myText.setTitle(text.getTitle());
		myText.setDescription(text.getDescription());
		return myText;
	}
	
	public RelationshipTextUserControl getRelationshipTextUserControl() {
		return SpringFactory.getController("relationshipTextUserControl",
				RelationshipTextUserControl.class,
				ReflectionUtils.prepareDataForPersistence(this));
	}
}
