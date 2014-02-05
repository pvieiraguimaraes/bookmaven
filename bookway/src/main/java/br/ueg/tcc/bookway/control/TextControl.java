package br.ueg.tcc.bookway.control;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.EnumUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import br.com.vexillum.configuration.Properties;
import br.com.vexillum.control.GenericControl;
import br.com.vexillum.util.HibernateUtils;
import br.com.vexillum.util.Message;
import br.com.vexillum.util.Return;
import br.com.vexillum.util.SpringFactory;
import br.ueg.tcc.bookway.control.textfactory.TextReader;
import br.ueg.tcc.bookway.control.textfactory.TextWriter;
import br.ueg.tcc.bookway.model.Text;
import br.ueg.tcc.bookway.model.UserBookway;
import br.ueg.tcc.bookway.model.enums.TypePrivacy;
import br.ueg.tcc.bookway.utils.FolderUtils;

@Service
@Scope("prototype")
public class TextControl extends GenericControl<Text> {

	private TextWriter txtWriter;
	private TextReader txtReader;
	HashMap<String, Object> map;
	private Properties configuration;

	public TextControl() {
		super(Text.class);
		map = new HashMap<>();
		configuration = SpringFactory.getInstance().getBean("configProperties",
				Properties.class);
		txtWriter = new TextWriter(map);
		txtReader = new TextReader(map);
	}

	public Return insertTextIntoRepository() {
		Return retRepository = new Return(true);
		UserBookway user = ((UserBookway) data.get("userLogged"));
		map.put("user", user);
		retRepository.concat(txtWriter.insertTextIntoRepository());
		if (!retRepository.isValid())
			retRepository.addMessage(new Message(null, "Erro no repositório!"));
		return retRepository;
	}

	public Return mapedTextForDataBase() {
		Return retMaping = new Return(true);
		retMaping.concat(txtReader.mappingText());
		if (!retMaping.isValid())
			retMaping.addMessage(new Message(null,
					"Erro no mapeamento do texto!"));
		return retMaping;
	}

	public Return createText() {
		String stream = (String) data.get("stream");
		String type = (String) data.get("type");
		Integer linesForPage = (Integer) data.get("linesForPage");
		Integer pagesForChapter = (Integer) data.get("pagesForChapter");
		Return retUpload = new Return(true);
		map.put("text", createObjectText());
		retUpload.concat(txtReader.createText(createDTDValidator(), stream,
				type, linesForPage, pagesForChapter));
		if (retUpload.isValid())
			retUpload.concat(afterCreateText());
		return retUpload;
	}

	private Text createObjectText() {
		Text text = (Text) data.get("entity");
		UserBookway user = (UserBookway) data.get("userLogged");
		text.setInsertDate(new Date());
		text.setUserOwning(user);
		return text;
	}

	@SuppressWarnings("unchecked")
	private String createDTDValidator() {
		ArrayList<String> levels = (ArrayList<String>) data.get("levels");
		Integer countLevels = (Integer) data.get("countLevels");
		if (levels != null || countLevels != null) {
			if (levels != null && !levels.isEmpty())
				return txtReader.createDTDValidator(levels);
			else
				return txtReader.createDTDValidator(txtReader
						.getDefaultLevels(countLevels));
		}
		return null;
	}

	/**
	 * Este método armazenará o arquivo no repositório e também mapeará no banco
	 * de dados.
	 * 
	 * @param list
	 * @return Return
	 */
	private Return afterCreateText() {
		Return retAfterUpload = new Return(true);
		retAfterUpload.concat(insertTextIntoRepository());
		if (retAfterUpload.isValid())
			entity.setFilePath((String) retAfterUpload.getList().get(0));
		retAfterUpload.concat(mapedTextForDataBase());
		if (retAfterUpload.isValid())
			retAfterUpload.concat(insertTextIntoDataBase());
		return retAfterUpload;
	}

	public Return insertTextIntoDataBase() {
		Return retInsert = new Return(true);
		retInsert.concat(save());
		return retInsert;
	}

	public Return deleteText() {
		Return retDelete = new Return(true);
		String filePath = entity.getFilePath();
		retDelete.concat(delete());
		if (retDelete.isValid())
			retDelete.concat(removeTextFromRepository(filePath));
		return retDelete;
	}
	
	public Text getTextById(Long id){
		Return ret = new Return(true);
		String sql = "FROM Text where id = '" + id + "'";
		data.put("sql", sql);
		ret.concat(searchByHQL());
		Text text = HibernateUtils.materializeProxy((Text) ret.getList().get(0));
		return text;
	}

	public List<TypePrivacy> initTypesText() {
		return EnumUtils.getEnumList(TypePrivacy.class);
	}

	/**
	 * Método retorna todos os textos criados pelo usuário logado
	 * 
	 * @return lista com todos os textos do usuário logado
	 */
	public Return listTextsUser() {
		Return ret = new Return(true);
		String title = ""; Text thisText = null;
		UserBookway user = (UserBookway) data.get("userLogged");
		Object text = (Object) data.get("entity");
		
		if(text instanceof Text)
			thisText = (Text) text;
		
		if (thisText != null)
			title = thisText.getTitle() == null ? "" : thisText.getTitle();

		String sql = "FROM Text WHERE userOwning = '" + user.getId() + "'";
		if (!title.equalsIgnoreCase(""))
			sql += " AND title like '%" + title + "%'";

		data.put("sql", sql);
		ret.concat(searchByHQL());
		return ret;
	}

	/**
	 * Método retorna lista de acordo com titulo dado para todos os textos que
	 * não são do usuário
	 * 
	 * @return lista com textos que não são do usuário
	 */
	public Return searchTexts() {
		Return ret = new Return(true);
		
		UserBookway user = (UserBookway) data.get("userLogged");
		Text text = (Text) data.get("entity");
		String title = text.getTitle() == null ? "" : text.getTitle();
		boolean myTexts = (boolean) data.get("checkMyTexts");
		boolean community = (boolean) data.get("community");
		
		TypePrivacy typeText = text.getTypeText();
		
		String sql = "FROM Text WHERE title like '%" + title + "%'";
		
		if(community)
			sql += " AND community = '" + community + "'";
		
		if(typeText != null)
			sql += " AND typeText = '" + typeText.ordinal() + "'";
		
		if(myTexts)
			sql += " AND userOwning = '" + user.getId() + "'";
		
		data.put("sql", sql);
		ret.concat(searchByHQL());
		return ret;
	}

	public Return deleteElementsTextsOfUser() {
		Return ret = new Return(true);
		ret.concat(removePermitedTexts());
		return ret;
	}

	@SuppressWarnings("unchecked")
	private Return removePermitedTexts() {
		Return ret = new Return(true);
		List<Text> listText = (List<Text>) listTextsUser().getList();
		if (listText != null) {
			for (Text text : listText) {
				entity = text;
				ret.concat(deleteText());
				ret.setMessages(null);
			}
		}
		return ret;
	}

	private Return removeTextFromRepository(String filePath) {
		Return ret = new Return(true);
		UserBookway user = ((UserBookway) data.get("userLogged"));
		map.put("user", user);
		ret.concat(txtWriter.removeTextFromRepository(filePath));
		return ret;
	}
	
	public Return disconnectionUserOfText(){
		entity.setUserOwning(null);
		String localeText = entity.getFilePath();
		String filePath = getFilePath(entity);
		entity.setFilePath(filePath);
		Return ret = update();
		if(ret.isValid())
			relocaleTextRepository(localeText, getFilePath(entity));
		return ret;
	}

	private Return relocaleTextRepository(String srcPath, String dstPath) {
		Return ret = new Return(true);
		ret.concat(txtWriter.moveFile(srcPath, dstPath));
		return ret;
	}

	public Return searchFriendTexts() {
		// TODO Servirá para buscar todos os textos do amigos.
		return new Return(true);
	}
	
	public Return updateUserOwnerText(){
		String localeText = getFilePath(entity);
		entity.setUserOwning((UserBookway) data.get("userLogged"));
		entity.setFilePath(getFilePath(entity));
		Return ret = update();
		if(ret.isValid()){
			FolderUtils.createFolder(
					configuration.getKey("PATH_REPOSITORY_TEXT"), entity.getUserOwning().getEmail());
			relocaleTextRepository(localeText, entity.getFilePath());
		}
		return ret;
	}
	
	private String getNameText(Text text, String separator){
		String aux = (text.getFilePath()).substring((text.getFilePath()).lastIndexOf(separator), text.getFilePath().length());
		return aux;
	}
	
	private String getFilePath(Text text){
		String separator = System.getProperty("file.separator");
		String path = text.getUserOwning() != null ? text.getUserOwning().getEmail() : "public";
		String name = configuration.getKey("PATH_REPOSITORY_TEXT") + separator + path  + getNameText(text, separator); 
		return name;
	}

}
