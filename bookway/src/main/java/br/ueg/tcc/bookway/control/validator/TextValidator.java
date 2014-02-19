package br.ueg.tcc.bookway.control.validator;

import java.util.ArrayList;
import java.util.Map;

import br.com.vexillum.control.validator.Validator;
import br.com.vexillum.util.Return;

/**
 * Classe responsável pela validação da entidade {@link Text} do sistema
 * 
 * @author pedro
 * 
 */
public class TextValidator extends Validator {

	public TextValidator(Map<String, Object> mapData) {
		super(mapData);
	}

	/**
	 * Método que valida os dados para a criação do texto e importação do
	 * arquivo para o sistema
	 * 
	 * @return {@link Return}
	 */
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

	/**
	 * Valida se o conteúdo do arquivo não é vazio
	 * 
	 * @param stream
	 * @return {@link Return}
	 */
	private Return validateStream(String stream) {
		Return retStream = new Return(true);
		if (stream == null || stream.equalsIgnoreCase(""))
			retStream.concat(creatReturn(null,
					getValidationMessage("stream", "upload", false)));
		return retStream;
	}

	/**
	 * Valida a importação do arquivo
	 * 
	 * @param arrayList
	 *            , lista com os níveis informados para mapeamento personalidade
	 *            se existir
	 * @param countLevels
	 *            , quantidade de níveis informados
	 * @param pagesForChapter
	 *            , quantidade de páginas por capítulo
	 * @param linesForPage
	 *            , quantidade de linhas por página
	 * @param simple
	 *            , verdadeiro se a importação for na modalidade simples
	 * @param avanced
	 *            , verdadeiro se a importação for na modalidade avançada
	 * @return {@link Return}
	 */
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

	/**
	 * Validação da importação da na modalidade avançada
	 * 
	 * @param arrayList
	 *            , lista de níveis que serão utilizados na validação
	 * @param countLevels
	 *            , quantidade de níveis a serem validados.
	 * @return {@link Return}
	 */
	private Return validateAvancedImportData(ArrayList<String> arrayList,
			Integer countLevels) {
		Return retAvancedData = new Return(true);
		if (arrayList != null && countLevels != null) {
			retAvancedData.concat(validatePrivateLevels(arrayList));
		}
		if (arrayList == null && countLevels == null) {
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

	/**
	 * Validação da importação na modalidade avançada
	 * 
	 * @param pagesForChapter
	 *            , número de páginas por capítulo
	 * @param linesForPage
	 *            , quantidade de linhas por página
	 * @return {@link Return}
	 */
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

	/**
	 * Validação dos níveis que são privados do sistema e não poderão ser
	 * utilizados nas importações pois o sistema faz uso deles.
	 * 
	 * @param arrayList
	 *            , lista de níveis a serem validados
	 * @return {@link Return}
	 */
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

	/**
	 * Validação de exclusão do texto do sistema, onde deverá ser checado se o
	 * texto não pertence a nenhum outro usuário.
	 * 
	 * @return {@link Return}
	 */
	public Return validateDeleteText() {
		Return retDelete = new Return(true);
		boolean belong = (boolean) mapData.get("textBelongsAnyUser");
		retDelete.setValid(!belong);
		return retDelete;
	}
}
