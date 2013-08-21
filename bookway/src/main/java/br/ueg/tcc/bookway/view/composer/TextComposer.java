package br.ueg.tcc.bookway.view.composer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;

import br.com.vexillum.util.ReflectionUtils;
import br.com.vexillum.util.Return;
import br.com.vexillum.util.SpringFactory;
import br.com.vexillum.view.CRUDComposer;
import br.ueg.tcc.bookway.control.TextControl;
import br.ueg.tcc.bookway.model.Text;
import br.ueg.tcc.bookway.model.enums.TypeText;
import br.ueg.tcc.bookway.view.macros.ItemText;

/**
 * @author Pedro
 * 
 */
@SuppressWarnings("serial")
@org.springframework.stereotype.Component
@Scope("prototype")
public class TextComposer extends CRUDComposer<Text, TextControl> {

	@Wire
	protected Textbox fldLevel;

	@Wire
	protected Listbox fldlstlevel;
	private Integer i = 1;

	@Wire
	protected Intbox fldCountlevels;

	@Wire
	protected Checkbox chckCommunity;

	@Wire
	protected Combobox fldTypeText;

	@Wire
	protected Label upLabelTxt;

	@Wire
	protected Label upLabelXml;

	@Wire
	protected Groupbox simpleImport;

	private boolean simple;

	@Wire
	protected Groupbox avancedImport;

	@Wire
	protected Checkbox myTexts;

	private boolean avanced;

	private Integer linesForPage;
	private Integer pagesForChapter;

	private Integer countLevels;

	private String stream;
	private ArrayList<String> levels;

	private List<TypeText> listTypesText;

	private List<Text> allMyTexts;

	private String type;

	public boolean getSimple() {
		return simple;
	}

	public void setSimple(boolean simple) {
		this.simple = simple;
	}

	public boolean getAvanced() {
		return avanced;
	}

	public void setAvanced(boolean avanced) {
		this.avanced = avanced;
	}

	public Integer getLinesForPage() {
		return linesForPage;
	}

	public void setLinesForPage(Integer linesForPage) {
		this.linesForPage = linesForPage;
	}

	public Integer getPagesForChapter() {
		return pagesForChapter;
	}

	public void setPagesForChapter(Integer pagesForChapter) {
		this.pagesForChapter = pagesForChapter;
	}

	public List<TypeText> getListTypesText() {
		return listTypesText;
	}

	public void setListTypesText(List<TypeText> listTypesText) {
		this.listTypesText = listTypesText;
	}

	public ArrayList<String> getLevels() {
		return levels;
	}

	public void setLevels(ArrayList<String> levels) {
		this.levels = levels;
	}

	public String getStream() {
		return stream;
	}

	public void setStream(String stream) {
		this.stream = stream;
	}

	public Integer getCountLevels() {
		return countLevels;
	}

	public void setCountLevels(Integer countLevels) {
		this.countLevels = countLevels;
	}

	public void initListTypeText() {
		setListTypesText(getControl().initTypesText());
	}

	public List<Text> getAllMyTexts() {
		return allMyTexts;
	}

	public void setAllMyTexts(List<Text> allMyTexts) {
		this.allMyTexts = allMyTexts;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		initListTypeText();
		super.doAfterCompose(comp);
		createListTextUser();
		// setUpListTextInComponent(getListTextUser(), "resultSearch",
		// component);
		loadBinder();
	}

	@SuppressWarnings("unchecked")
	private void createListTextUser() {
		Return retListText = new Return(true);
		retListText.concat(getControl().doAction("listTextsUser"));
		if (retListText.isValid())
			retListText.concat(getControl().doAction("listTextsAddedByUser"));
		if (retListText.isValid())
			setAllMyTexts((List<Text>) retListText.getList());
	}

	public void upload(Media media, String type) {
		if (media != null && media.getFormat().equalsIgnoreCase(type)) {
			showBusyServer(null, "Lendo o arquivo");
			setStream(media.getStringData());
			setType(media.getFormat());
			Clients.clearBusy();
		} else if (media == null)
			showNotification("Insira um arquivo para Upload!", "error");
	}

	private void prepareDataText() {
		getLevelsName();
		confirmCountLevels();
		confirmPanelImport();
	}

	private void confirmPanelImport() {
		setSimple(simpleImport.isOpen());
		setAvanced(avancedImport.isOpen());
	}

	private void confirmCountLevels() {
		if (getLevels() != null)
			setCountLevels(getLevels().size());
	}

	public void saveText() {
		Return retSave = new Return(true);
		prepareDataText();
		retSave.concat(getControl().doAction("createText"));
		treatReturn(retSave);
		if (retSave.isValid())
			resetEntity();
	}

	private void resetEntity() {
		entity = getEntityObject();
		resetFormSimpleImport();
		resetFormAvancedImport();
		loadBinder();
	}

	private void resetFormSimpleImport() {
		upLabelTxt.setValue(null);
		setPagesForChapter(null);
		setLinesForPage(null);
	}

	private void resetFormAvancedImport() {
		upLabelXml.setValue(null);
		fldCountlevels.setValue(null);
		countLevels = null;
		if (fldlstlevel.getItems() != null && !fldlstlevel.getItems().isEmpty())
			removeListLevelOfListBox();
		fldLevel.setValue(null);
	}

	public void deleteText() {
		Return retDelete = new Return(true);
		retDelete.concat(getControl().doAction("deleteText"));
	}

	private void getLevelsName() {
		ArrayList<String> levelsMap = new ArrayList<>();
		levelsMap = createListLevelByListBox();
		if (levelsMap == null)
			setLevels(null);
		else
			setLevels(levelsMap);
	}

	private ArrayList<String> createListLevelByListBox() {
		ArrayList<String> list = new ArrayList<String>();
		for (Listitem item : fldlstlevel.getItems()) {
			for (Component cell : item.getChildren()) {
				for (Component child : cell.getChildren()) {
					if (child instanceof Textbox)
						list.add(((Textbox) child).getValue());
				}
			}
		}
		return list.isEmpty() ? null : list;
	}

	private void removeListLevelOfListBox() {
		for (int i = fldlstlevel.getItemCount() - 1; i >= 0; i--) {
			fldlstlevel.removeItemAt(i);
		}
		checkListBoxLevel();
	}

	@Override
	public TextControl getControl() {
		return SpringFactory.getController("textControl", TextControl.class,
				ReflectionUtils.prepareDataForPersistence(this));
	}

	@Override
	public Text getEntityObject() {
		return new Text();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void addLevelInList() {
		if (fldLevel.getValue() != "" && !checkListBoxLevelNames()) {
			Listitem item = new Listitem();
			Listcell cell1 = new Listcell("Nível " + i++), cell2 = new Listcell(), cell3 = new Listcell();
			String nameLevel = fldLevel.getValue();
			Textbox txt = new Textbox(nameLevel);
			txt.setWidth("180px");
			cell2.appendChild(txt);
			cell3.setImage("/images/close.png");
			cell3.addEventListener(Events.ON_CLICK, new EventListener() {
				@Override
				public void onEvent(Event event) throws Exception {
					if (event.getTarget().getParent().getParent()
							.getLastChild() == event.getTarget().getParent()) {
						fldlstlevel.removeChild(event.getTarget().getParent());
						checkListBoxLevel();
						--i;
					}
				}
			});
			cell1.setParent(item);
			cell2.setParent(item);
			cell3.setParent(item);
			item.setId("fldLstlevel" + nameLevel);
			item.setParent(fldlstlevel);
			fldLevel.setValue("");
			checkListBoxLevel();
		}
	}

	private void checkListBoxLevel() {
		if (fldlstlevel.getItems().isEmpty() || fldlstlevel.getItems() == null) {
			fldlstlevel.setVisible(false);
			fldCountlevels.setValue(null);
			fldCountlevels.setDisabled(false);
		} else {
			fldlstlevel.setVisible(true);
			fldCountlevels.setValue(fldlstlevel.getItems().size());
			fldCountlevels.setDisabled(true);
		}
	}

	private boolean checkListBoxLevelNames() {
		List<String> levels = createListLevelByListBox();
		if (levels != null && levels.contains(fldLevel.getValue())) {
			showNotification("Não poderá conter níveis repetidos!", "error");
			return true;
		}
		return false;
	}

	public void checkComboType() {
		if (fldTypeText.getSelectedItem().getValue().equals(TypeText.PRIVADO)) {
			chckCommunity.setChecked(false);
			chckCommunity.setDisabled(true);
		} else
			chckCommunity.setDisabled(false);
	}

	public void checkFormImport() {
		setStream(null);
		if (!simpleImport.isOpen())
			resetFormSimpleImport();
		if (!avancedImport.isOpen())
			resetFormAvancedImport();
		loadBinder();
	}

	@SuppressWarnings("unchecked")
	public void searchText() {
		if (myTexts.isChecked()) {
			setUpListTextInComponent(getAllMyTexts(), "resultSearch", component);
		} else {
			Return ret = new Return(true);
			entity.setCommunity(false);
			entity.setTypeText(TypeText.PUBLICO);
			ret.concat(getControl().doAction("searchTexts"));
			List<Text> list = getControl().substractListText(getAllMyTexts(), (List<Text>) ret.getList());
			setUpListTextInComponent(list, "resultSearch", component);
		}
	}

	private void setUpListTextInComponent(List<Text> textsUser,
			String idParent, Component comp) {
		for (Text text : textsUser) {
			getComponentById(comp, idParent).appendChild(createItemText(text));
		}
	}

	private ItemText createItemText(Text text) {
		ItemText item = new ItemText();
		item.setUser(text.getUserOwning().getName());
		item.setTitle(text.getTitle());
		item.setDescription(text.getDescription());
		return item;
	}

}
