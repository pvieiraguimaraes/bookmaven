package br.ueg.tcc.bookway.view.composer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Image;
import org.zkoss.zul.Window;

import br.com.vexillum.control.GenericControl;
import br.com.vexillum.model.ICommonEntity;
import br.com.vexillum.util.Return;
import br.com.vexillum.util.SpringFactory;
import br.com.vexillum.view.CRUDComposer;
import br.ueg.tcc.bookway.control.ElementsItensStudyControl;
import br.ueg.tcc.bookway.control.NavigationStudyControl;
import br.ueg.tcc.bookway.control.StudyControl;
import br.ueg.tcc.bookway.model.ElementText;
import br.ueg.tcc.bookway.model.ElementsItensStudy;
import br.ueg.tcc.bookway.model.Study;
import br.ueg.tcc.bookway.view.macros.ItemStudy;

@SuppressWarnings("serial")
public abstract class BaseComposer<E extends ICommonEntity, G extends GenericControl<E>>
		extends CRUDComposer<E, G> {
	
	private final String UNDER_ON = "text-decoration: underline;";
	private final String UNDER_OFF = "text-decoration: none;";

	protected BaseComposer<E, G> parentComposer;

	protected List<ElementText> itensSelected;

	protected Study study;

	protected Boolean continueStudy;
	
	protected HashMap<String, Object> newMap;
	
	protected List<ItemStudy> itemStudies;
	
	protected List<ItemStudy> itemStudiesSelected;

	/**
	 * Lista que representa o relacionamento entre os elementos do texto e os itens de estudo
	 */
	protected List<ElementsItensStudy> elementsItensStudies;
	
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

		if (arg.get("itemStudies") == null)
			itemStudies = new ArrayList<>();
		else
			itemStudies = (List<ItemStudy>) arg.get("itemStudies");
		
		if (arg.get("itemStudiesSelected") == null)
			itemStudiesSelected = new ArrayList<>();
		else
			itemStudiesSelected = (List<ItemStudy>) arg.get("itemStudiesSelected");
		
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
		String style = "background-color: " + color + ";";
		for (ItemStudy itemStudy : itemStudiesSelected) {
			itemStudy.contentElement.setStyle(style);
		}
		parentComposer.setItemStudiesSelected(new ArrayList<ItemStudy>());
		parentComposer.setItensSelected(new ArrayList<ElementText>());
		parentComposer.checkPanelActionVisibility();
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
}
