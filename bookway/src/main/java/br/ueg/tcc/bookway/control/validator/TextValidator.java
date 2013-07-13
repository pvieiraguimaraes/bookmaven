package br.ueg.tcc.bookway.control.validator;

import java.util.ArrayList;
import java.util.Map;

import br.com.vexillum.control.validator.Validator;
import br.com.vexillum.util.Message;
import br.com.vexillum.util.Return;

public class TextValidator extends Validator {

	public TextValidator(Map<String, Object> mapData) {
		super(mapData);
	}

	@SuppressWarnings("unchecked")
	public Return validateUploadValidText() {
		ArrayList<String> arrayList = (ArrayList<String>) mapData.get("levels");
		Return retLevels = new Return(true);
		retLevels.concat(validateLevels(arrayList));
		if(!retLevels.isValid())
			retLevels.addMessage(new Message(null, "Uso de níveis protegidos, vide manual importação avançada!"));
		return retLevels;
	}
	
	public Return validateLevels(ArrayList<String> arrayList) {
		Return retLevels = new Return(true);
		if(arrayList == null) return retLevels;
		for (String string : arrayList) {
			if(string.equalsIgnoreCase("documento") || string.equalsIgnoreCase("conteudo") || string.equalsIgnoreCase("referencia")){
				retLevels.setValid(false);
				retLevels.addMessage(new Message(null, "Uso de algum dos níveis: documento, conteudo ou referencia"));
			}
		}
		return retLevels;
	}
	
	public Return validateDeleteText(){
		Return retDelete = new Return(true);
		//TODO Verificar se existem usuário com este texto em seus estudos, se existir não permitir que seja excluido
		return retDelete;
	}
}
