package br.ueg.tcc.bookway.control.validator;

import java.util.ArrayList;
import java.util.Map;

import br.com.vexillum.control.validator.Validator;
import br.com.vexillum.util.Return;

public class TextValidator extends Validator {

	public TextValidator(Map<String, Object> mapData) {
		super(mapData);
	}

	@SuppressWarnings("unchecked")
	public Return validateCreateText() {
		ArrayList<String> arrayList = (ArrayList<String>) mapData.get("levels");
		Integer countLevels = (Integer) mapData.get("countLevels");
		String stream = (String) mapData.get("stream");
		Return retLevels = new Return(true);
		retLevels.concat(validateStream(stream));
		retLevels.concat(validateLevels(arrayList, countLevels));
		retLevels.concat(validateModel());
		return retLevels;
	}
	
	private Return validateStream(String stream) {
		Return retStream = new Return(true);
		if(stream == null || stream.equalsIgnoreCase(""))
			retStream.concat(creatReturn("stream", getValidationMessage("stream", "upload", false)));
		return retStream;
	}

	public Return validateLevels(ArrayList<String> arrayList, Integer countLevels) {
		Return retLevels = new Return(true);
		if(arrayList == null && countLevels == null){
			retLevels.setValid(false);
			retLevels.concat(creatReturn("level", getValidationMessage("level", "save", false)));
		}
		if(countLevels == null){
			retLevels.setValid(false);
			retLevels.concat(creatReturn("countlevels", getValidationMessage("countlevels", "save", false)));
		}
		if (arrayList != null && countLevels != null) {
			for (String string : arrayList) {
				if(string.equalsIgnoreCase("documento") || string.equalsIgnoreCase("conteudo") || string.equalsIgnoreCase("referencia")){
					retLevels.setValid(false);
					retLevels.concat(creatReturn("lstlevel" + string, getValidationMessage("lstlevel", "save", false) + string));
				}
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
