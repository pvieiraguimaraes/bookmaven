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
import br.ueg.tcc.bookway.control.TextControl;
import br.ueg.tcc.bookway.model.Text;
import br.ueg.tcc.bookway.model.enums.TypePrivacy;

/**
 * Classe que contém as funcionalidades envolvidas no caso de uso de Manter
 * Texto
 * 
 * @author Pedro
 * 
 */
@SuppressWarnings("serial")
@org.springframework.stereotype.Component
@Scope("prototype")
public class TextComposer extends InitComposer<Text, TextControl> {

	@Wire
	protected Textbox fldLevel;

	@Wire
	protected Listbox fldlstlevel;
	private Integer i = 1;

	@Wire
	protected Intbox fldCountlevels;

	@Wire
	protected Label upLabelTxt;

	@Wire
	protected Label upLabelXml;

	@Wire
	protected Groupbox simpleImport;

	private boolean simple;

	@Wire
	protected Groupbox avancedImport;

	private boolean avanced;

	private Integer linesForPage;
	private Integer pagesForChapter;

	private Integer countLevels;

	private String stream;
	private ArrayList<String> levels;

	private String type;

	private boolean updateText = false;

	public boolean isUpdateText() {
		return updateText;
	}

	public void setUpdateText(boolean updateText) {
		this.updateText = updateText;
	}

	private boolean textBelongsAnyUser;

	public boolean getTextBelongsAnyUser() {
		return textBelongsAnyUser;
	}

	public void setTextBelongsAnyUser(boolean textBelongsAnyUser) {
		this.textBelongsAnyUser = textBelongsAnyUser;
	}

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Text text = (Text) session.getAttribute("thisEditText");
		if (text != null)
			entity = text;
		else {
			initListTypeText();

		}
		loadBinder();
	}

	/**
	 * Verifica o tipo selecionado e habilita a visibilidade de itens
	 */
	public void checkComboType() {
		if (fldTypeText.getSelectedItem().getValue()
				.equals(TypePrivacy.PRIVADO)) {
			chckCommunity.setChecked(false);
			chckCommunity.setDisabled(true);
		} else
			chckCommunity.setDisabled(false);
	}

	/**
	 * Realiza o upload do arquivo de texto
	 * 
	 * @param media
	 *            , arquivo a ser carregado
	 * @param type
	 *            , tipo do arquivo
	 */
	public void upload(Media media, String type) {
		if (media != null && media.getFormat().equalsIgnoreCase(type)) {
			showBusyServer(null, "Lendo o arquivo");
			setStream(media.getStringData());
			setType(media.getFormat());
			Clients.clearBusy();
		} else if (media == null)
			showNotification("Insira um arquivo para Upload!", "error");
	}

	/**
	 * Método que faz chamadas a outros métodos para setar informações no texto
	 */
	private void prepareDataText() {
		getLevelsName();
		confirmCountLevels();
		confirmPanelImport();
	}

	/**
	 * Verifica o tipo de importação através do painel de importação aberto
	 */
	private void confirmPanelImport() {
		setSimple(simpleImport.isOpen());
		setAvanced(avancedImport.isOpen());
	}

	/**
	 * Confirma a quantidade de níveis do texto que está sendo carregado
	 */
	private void confirmCountLevels() {
		if (getLevels() != null)
			setCountLevels(getLevels().size());
	}

	/**
	 * Faz chamada ao controlador para salvar um texto
	 */
	public void saveText() {
		Return retSave = new Return(true);
		prepareDataText();
		retSave.concat(getControl().doAction("createText"));
		treatReturn(retSave);
		if (retSave.isValid())
			resetEntity();
		createListTextUser();
		loadBinder();
	}

	/**
	 * Executa chamada ao controle para realizar alteração de um texto
	 */
	public void updateText() {
		Return ret = new Return(true);
		ret.concat(getControl().doAction("update"));
		if (ret.isValid())
			getComponentById("frmText").detach();
		treatReturn(ret);
		loadBinder();
	}

	/**
	 * Inicializa todos os formulários
	 */
	private void resetEntity() {
		entity = getEntityObject();
		resetFormSimpleImport();
		resetFormAvancedImport();
		loadBinder();
	}

	/**
	 * Inicializa o formulário de importação simples
	 */
	private void resetFormSimpleImport() {
		upLabelTxt.setValue(null);
		setPagesForChapter(null);
		setLinesForPage(null);
	}

	/**
	 * Inicializa o formulário de importação avançada
	 */
	private void resetFormAvancedImport() {
		upLabelXml.setValue(null);
		fldCountlevels.setValue(null);
		countLevels = null;
		if (fldlstlevel.getItems() != null && !fldlstlevel.getItems().isEmpty())
			removeListLevelOfListBox();
		fldLevel.setValue(null);
	}

	/**
	 * Obtém os valores dos nomes dos níveis
	 */
	private void getLevelsName() {
		ArrayList<String> levelsMap = new ArrayList<>();
		levelsMap = createListLevelByListBox();
		if (levelsMap == null)
			setLevels(null);
		else
			setLevels(levelsMap);
	}

	/**
	 * Cria lista com os nomes dos níveis de acordo com os itens inseridos na
	 * lista de níveis no painel de importação
	 * 
	 * @return lista com os nomes dos níveis
	 */
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

	/**
	 * Remove os elementos da lista de níveis
	 */
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

	/**
	 * Adiciona um novo nível a lista de níveis do painel de importação avançada
	 */
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

	/**
	 * Verifica a visibilidade da lista de níveis do painel de importação
	 */
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

	/**
	 * Verifica a existência de níveis repetidos na lista de níveis
	 * 
	 * @return verdadeiro caso exista níveis repetidos
	 */
	private boolean checkListBoxLevelNames() {
		List<String> levels = createListLevelByListBox();
		if (levels != null && levels.contains(fldLevel.getValue())) {
			showNotification("Não poderá conter níveis repetidos!", "error");
			return true;
		}
		return false;
	}

	/**
	 * Verifica o tipo de painel utilizado e inicializa os campos
	 */
	public void checkFormImport() {
		setStream(null);
		if (!simpleImport.isOpen())
			resetFormSimpleImport();
		if (!avancedImport.isOpen())
			resetFormAvancedImport();
		loadBinder();
	}

}
