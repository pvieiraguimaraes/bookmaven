package br.ueg.tcc.bookway.control;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.EnumUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import br.com.vexillum.control.GenericControl;
import br.com.vexillum.util.Message;
import br.com.vexillum.util.Return;
import br.ueg.tcc.bookway.control.textfactory.TextReader;
import br.ueg.tcc.bookway.control.textfactory.TextWriter;
import br.ueg.tcc.bookway.model.Text;
import br.ueg.tcc.bookway.model.UserBookway;
import br.ueg.tcc.bookway.model.enums.TypeText;

@Service
@Scope("prototype")
public class TextControl extends GenericControl<Text> {
	
	private TextWriter txtWriter;
	private TextReader txtReader;
	HashMap<String, Object> map;
	
	public TextControl() {
		super(Text.class);
		map = new HashMap<>();
		txtWriter = new TextWriter(map);
		txtReader = new TextReader(map);
	}

	public Return insertTextIntoRepository() {
		Return retRepository = new Return(true);
		UserBookway user = ((UserBookway) data.get("userLogged"));
		map.put("user", user);
		retRepository.concat(txtWriter.insertTextIntoRepository());
		if(!retRepository.isValid())
			retRepository.addMessage(new Message(null, "Erro no repositório!"));
		return retRepository;
	}

	public Return mapedTextForDataBase() {
		Return retMaping = new Return(true);
		retMaping.concat(txtReader.mappingText());
		if(!retMaping.isValid())
			retMaping.addMessage(new Message(null, "Erro no mapeamento do texto!"));
		return retMaping;
	}
	
	public Return createText() {
		String stream = (String) data.get("stream");
		String type = (String) data.get("type");
		Integer linesForPage = (Integer) data.get("linesForPage");
		Integer pagesForChapter = (Integer) data.get("pagesForChapter");
		Return retUpload = new Return(true);
		map.put("text", createObjectText());
		retUpload.concat(txtReader.createText(createDTDValidator(), stream, type, linesForPage, pagesForChapter));		
		if (retUpload.isValid())
			retUpload.concat(afterCreateText());
		return retUpload;
	}
	
	private Text createObjectText(){
		Text text = (Text) data.get("entity");
		UserBookway user = (UserBookway) data.get("userLogged");
		text.setInsertDate(new Date());
		text.setUserOwning(user);
		return text;
	}
	
	@SuppressWarnings("unchecked")
	private String createDTDValidator(){
		ArrayList<String> levels = (ArrayList<String>) data.get("levels");
		Integer countLevels = (Integer) data.get("countLevels");
		if (levels != null && levels.isEmpty())
			return txtReader.createDTDValidator(levels);
		if (countLevels != null)
			return txtReader.createDTDValidator(txtReader.getDefaultLevels(countLevels));
		return null;
	}

	/**Este método armazenará o arquivo no repositório e também mapeará no banco de dados.
	 * @param list
	 * @return Return
	 */
	private Return afterCreateText() {
		Return retAfterUpload = new Return(true);
		retAfterUpload.concat(insertTextIntoRepository());
		if(retAfterUpload.isValid())
			entity.setFilePath((String) retAfterUpload.getList().get(0));
			retAfterUpload.concat(mapedTextForDataBase());
		if(retAfterUpload.isValid())
			retAfterUpload.concat(insertTextIntoDataBase());
		return retAfterUpload;
	}

	public Return insertTextIntoDataBase() {
		Return retInsert = new Return(true);
		retInsert.concat(doAction("save"));
		return retInsert;
	}
	
	public Return deleteText(){
		Return retDelete = new Return(true);
		retDelete.concat(doAction("delete"));
		if(!retDelete.isValid())
			retDelete.addMessage(new Message(null, "Erro ao deletar o arquivo!"));
		else retDelete.addMessage(new Message(null, "Texto excluído com sucesso!"));
		return retDelete;
	}//TODO Colocar pra chamar a msg do properties

	public List<TypeText> initTypesText() {
		return EnumUtils.getEnumList(TypeText.class);
	}
	
	@SuppressWarnings("unchecked")
	public List<Text> searchTextsUser(){
		Return ret = new Return(true);
		UserBookway user = (UserBookway) data.get("userLogged");
		String sql = "FROM Text where userOwning = '" + user.getId() + "'";
		data.put("sql", sql);
		ret.concat(searchByHQL());
		return (List<Text>) ret.getList();
	}
}
