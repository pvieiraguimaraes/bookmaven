package br.ueg.tcc.bookway.control;

import java.util.ArrayList;
import java.util.Date;
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
import br.ueg.tcc.bookway.model.enums.TypeText;

@Service
@Scope("prototype")
public class TextControl extends GenericControl<Text> {
	
	public TextControl() {
		super();
		classEntity = Text.class;
	}

	public Return insertTextIntoRepository(List<?> list) {
		Return retRepository = new Return(true);
		TextWriter txtWriter = new TextWriter();
		retRepository.concat(txtWriter.insertTextIntoRepository(list));
		if(!retRepository.isValid())
			retRepository.addMessage(new Message(null, "Erro no repositório!"));
		return retRepository;
	}

	public Return mapedTextForDataBase(List<?> list) {
		Return retMaping = new Return(true);
		TextReader txtReader = new TextReader();
		retMaping.concat(txtReader.mappingText(list));
		if(!retMaping.isValid())
			retMaping.addMessage(new Message(null, "Erro no mapeamento do texto!"));
		return retMaping;
	}

	@SuppressWarnings("unchecked")
	public Return uploadValidText() {
		Integer countLevels = (Integer) data.get("countLevels");
		String stream = (String) data.get("stream");
		ArrayList<String> levels = (ArrayList<String>) data.get("levels");
		Text text = (Text) data.get("entity");
		text.setInsertDate(new Date());
		
		Return retUpload = new Return(true);
		TextReader txtReader = new TextReader();
		String dtd = "";

		if (levels != null && levels.isEmpty()) {
			dtd = txtReader.createDTDValidator(levels);
		} else
			dtd = txtReader.createDTDValidator(txtReader.getDefaultLevels(countLevels));

		retUpload.concat(txtReader.createText(dtd, stream));		
		if (retUpload.isValid()){
			List<Object> list = (List<Object>) retUpload.getList();
			list.add(text);
			retUpload.setList(list);
			retUpload.addMessage(new Message(null, "e lido com sucesso!"));
			retUpload.concat(afterUploadText(list));
		}

		return retUpload;
	}

	/**Este método armazenará o arquivo no repositório e também mapeará no banco de dados.
	 * @param list
	 * @return Return
	 */
	private Return afterUploadText(List<?> list) {
		Return retAfterUpload = new Return(true);
		retAfterUpload.concat(insertTextIntoRepository(list));
		if(retAfterUpload.isValid())
			retAfterUpload.concat(mapedTextForDataBase(list));
		if(retAfterUpload.isValid()){
			entity = (Text) retAfterUpload.getList().get(0);
			retAfterUpload.concat(insertTextIntoDataBase());
		}
		return retAfterUpload;
	}

	public Return insertTextIntoDataBase() {
		Return retInsert = new Return(true);
		retInsert.concat(doAction("save"));
		if(!retInsert.isValid())
			retInsert.addMessage(new Message(null, "Erro ao inserir no Banco de Dados!"));
		return retInsert;
	}
	
	public Return deleteText(){
		Return retDelete = new Return(true);
		retDelete.concat(doAction("delete"));
		if(!retDelete.isValid())
			retDelete.addMessage(new Message(null, "Erro ao deletar o arquivo!"));
		else retDelete.addMessage(new Message(null, "Texto excluído com sucesso!"));
		return retDelete;
	}

	public List<TypeText> initTypesText() {
		return EnumUtils.getEnumList(TypeText.class);
	}
}
