package br.ueg.tcc.bookway.view.composer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.Clients;

import br.com.vexillum.util.Message;
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

	private String nivel1;
	private String nivel2;
	private String nivel3;
	private String nivel4;
	
	private String linesForPage;
	private String pagesForChapter;
	
	private Integer countLevels;
	
	private String stream;
	private ArrayList<String> levels;

	private List<TypeText> listTypesText;
	//TODO Fazer o combobox do texto funcionar, e colocar uma validação para o tipo privado desabilitar o checkbox
	
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

	public String getNivel1() {
		return nivel1;
	}

	public void setNivel1(String nivel1) {
		this.nivel1 = nivel1;
	}

	public String getNivel2() {
		return nivel2;
	}

	public void setNivel2(String nivel2) {
		this.nivel2 = nivel2;
	}

	public String getNivel3() {
		return nivel3;
	}

	public void setNivel3(String nivel3) {
		this.nivel3 = nivel3;
	}

	public String getNivel4() {
		return nivel4;
	}

	public void setNivel4(String nivel4) {
		this.nivel4 = nivel4;
	}
	
	public Integer getCountLevels() {
		return countLevels;
	}

	public void setCountLevels(Integer countLevels) {
		this.countLevels = countLevels;
	}
	
	public void initListTypeText(){
		setListTypesText(getControl().initTypesText());
	}

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		initListTypeText();
		super.doAfterCompose(comp);
		loadBinder();
	}

	public void uploadTxt(Media media) {
		Return retUploadText = new Return(true);
		retUploadText.concat(upload(media, "txt"));
		treatReturn(retUploadText);
	}
	
	public void uploadXml(Media media) {
		Return retUploadText = new Return(true);
		retUploadText.concat(upload(media, "xml"));
		treatReturn(retUploadText);
	}


	private void getLevelsName() {
		ArrayList<String> levelsMap = new ArrayList<>();
		levelsMap.add(getNivel1());
		levelsMap.add(getNivel2());
		levelsMap.add(getNivel3());
		levelsMap.add(getNivel4());
			if(levelsMap.get(0) == null) setLevels(null);
			else setLevels(levelsMap);
	}
	
	public Return upload(Media media, String type) {
		Return retUpload = new Return(true);
		if (media != null && media.getFormat().equalsIgnoreCase(type)) {
			showBusyServer(null, "Lendo o arquivo");
			setStream(media.getStringData());
			getLevelsName();
			confirmCountLevels();
			Clients.clearBusy();
		} else {
			retUpload.setValid(false);
			retUpload
					.addMessage(new Message(null, "Erro na leitura do arquivo"));
		}
		return retUpload;
	}
	
	public void saveText(){
		Return retSave = new Return(true);
		//TODO O correto é uploadValidText, usando insertTextIntoDataBase somente para testes
		retSave.concat(getControl().doAction("uploadValidText"));
		if(!retSave.isValid())
			retSave.addMessage(new Message(null, "Erro ao salvar o texto!"));
		treatReturn(retSave);
	}
	
	public void deleteText(){
		Return retDelete = new Return(true);
		somenteParaTeste();
		retDelete.concat(getControl().doAction("deleteText"));
		if(!retDelete.isValid()){
			retDelete.addMessage(new Message(null, "Erro ao deletar arquivo"));
		}
	}
	
	//TODO Remover após conseguir pegar o texto dos estudos
	private void somenteParaTeste() {
		entity.setId((long)1);
		searchEntitys();
		entity = getListEntity().get(0);
	}

	private void confirmCountLevels() {
		if(getLevels() != null)
			setCountLevels(getLevels().size());		
	}

	@Override
	public TextControl getControl() {
		return SpringFactory.getController("textControl", TextControl.class, ReflectionUtils.prepareDataForPersistence(this));
	}

	@Override
	public Text getEntityObject() {
		return new Text();
	}

}
