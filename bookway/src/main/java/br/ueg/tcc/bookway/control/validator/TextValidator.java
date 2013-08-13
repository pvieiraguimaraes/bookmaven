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
		Integer pagesForChapter = (Integer) mapData.get("pagesForChapter");
		Integer linesForPage = (Integer) mapData.get("linesForPage");
		String stream = (String) mapData.get("stream");
		boolean simple = (boolean) mapData.get("simple");
		boolean avanced = (boolean) mapData.get("avanced");
		Return retLevels = new Return(true);
		retLevels.concat(validateStream(stream));
		retLevels.concat(validateImport(arrayList, countLevels,
				pagesForChapter, linesForPage, simple, avanced));
		retLevels.concat(validateModel());
		return retLevels;
	}

	private Return validateStream(String stream) {
		Return retStream = new Return(true);
		if (stream == null || stream.equalsIgnoreCase(""))
			retStream.concat(creatReturn(null,
					getValidationMessage("stream", "upload", false)));
		return retStream;
	}

	public Return validateImport(ArrayList<String> arrayList,
			Integer countLevels, Integer pagesForChapter, Integer linesForPage,
			boolean simple, boolean avanced) {
		Return retLevels = new Return(true);
		if (simple)
			retLevels.concat(validateSimpleImportData(pagesForChapter,
					linesForPage));
		if (avanced)
			retLevels.concat(validateAvancedImportData(arrayList, countLevels));
		return retLevels;
	}

	private Return validateAvancedImportData(ArrayList<String> arrayList,
			Integer countLevels) {
		Return retAvancedData = new Return(true);
		if (arrayList != null && countLevels != null) {
			retAvancedData.concat(validatePrivateLevels(arrayList));
		}
		if (arrayList == null) {
			retAvancedData.setValid(false);
			retAvancedData.concat(creatReturn("level",
					getValidationMessage("level", "save", false)));
		}
		if (countLevels == null) {
			retAvancedData.setValid(false);
			retAvancedData.concat(creatReturn("countlevels",
					getValidationMessage("countlevels", "save", false)));
		}
		return retAvancedData;
	}

	private Return validateSimpleImportData(Integer pagesForChapter,
			Integer linesForPage) {
		Return retSimpleData = new Return(true);
		if (pagesForChapter == null) {
			retSimpleData.setValid(false);
			retSimpleData.concat(creatReturn("pagesForChapter",
					getValidationMessage("pagesForChapter", "save", false)));
		}
		if (linesForPage == null) {
			retSimpleData.setValid(false);
			retSimpleData.concat(creatReturn("linesForPage",
					getValidationMessage("linesForPage", "save", false)));
		}
		return retSimpleData;
	}

	private Return validatePrivateLevels(ArrayList<String> arrayList) {
		Return retPrivate = new Return(true);
		for (String string : arrayList) {
			if (string.equalsIgnoreCase("documento")
					|| string.equalsIgnoreCase("conteudo")
					|| string.equalsIgnoreCase("referencia")) {
				retPrivate.setValid(false);
				retPrivate.concat(creatReturn("lstlevel" + string,
						getValidationMessage("lstlevel", "save", false)
								+ string));
			}
		}
		return retPrivate;
	}

	public Return validateDeleteText() {
		Return retDelete = new Return(true);
		// TODO Verificar se existem usuário com este texto em seus estudos, se
		// existir não permitir que seja excluido
		return retDelete;
	}
}
