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

@org.springframework.stereotype.Component
@Scope("prototype")
public class TextComposer extends CRUDComposer<Text, TextControl> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Wire
	protected Textbox fldLevel;

	@Wire
	protected Listbox fldlstlevel;
	private Integer i = 1;

	@Wire
	protected Textbox fldCountlevels;

	@Wire
	protected Checkbox chckCommunity;

	@Wire
	protected Combobox fldTypeText;

	private String linesForPage;
	private String pagesForChapter;

	private Integer countLevels;

	private String stream;
	private ArrayList<String> levels;

	private List<TypeText> listTypesText;

	public String getLinesForPage() {
		return linesForPage;
	}

	public void setLinesForPage(String linesForPage) {
		this.linesForPage = linesForPage;
	}

	public String getPagesForChapter() {
		return pagesForChapter;
	}

	public void setPagesForChapter(String pagesForChapter) {
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

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		initListTypeText();
		super.doAfterCompose(comp);
		loadBinder();
	}

	public void upload(Media media, String type) {
		if (media != null && media.getFormat().equalsIgnoreCase(type)) {
			showBusyServer(null, "Lendo o arquivo");
			setStream(media.getStringData());
			Clients.clearBusy();
		} else if (media == null)
			showNotification("Insira um arquivo para Upload!", "error");
	}

	private void prepareDataText() {
		getLevelsName();
		confirmCountLevels();
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
	}

	public void deleteText() {
		Return retDelete = new Return(true);
		somenteParaTeste();
		retDelete.concat(getControl().doAction("deleteText"));
	}

	// TODO Remover após conseguir pegar o texto dos estudos
	private void somenteParaTeste() {
		entity.setId((long) 1);
		searchEntitys();
		entity = getListEntity().get(0);
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
		if (fldLevel.getValue() != "") {
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
			int s = fldlstlevel.getItems().size();
			fldCountlevels.setValue(String.valueOf(s));
			fldCountlevels.setDisabled(true);
		}
	}

	public void checkComboType() {
		if (fldTypeText.getSelectedItem().getValue().equals(TypeText.PRIVADO)) {
			chckCommunity.setChecked(false);
			chckCommunity.setDisabled(true);
		} else
			chckCommunity.setDisabled(false);
	}
}
