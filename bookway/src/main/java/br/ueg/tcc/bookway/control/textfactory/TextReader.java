package br.ueg.tcc.bookway.control.textfactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jdom2.Attribute;
import org.jdom2.Content;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.input.sax.XMLReaders;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import br.com.vexillum.configuration.Properties;
import br.com.vexillum.control.manager.ExceptionManager;
import br.com.vexillum.util.Message;
import br.com.vexillum.util.Return;
import br.com.vexillum.util.SpringFactory;
import br.ueg.tcc.bookway.model.ElementText;
import br.ueg.tcc.bookway.model.LevelText;
import br.ueg.tcc.bookway.model.Text;

/**
 * Classe respons�vel pelas a��es de leitura dos arquivos do texto.
 * 
 * @author pedro
 * 
 */
@Service
@Scope("prototype")
@SuppressWarnings("serial")
public class TextReader extends TextBookwayIO {

	public TextReader(HashMap<String, Object> map) {
		textData = map;
	}

	/**
	 * Elementos padr�o do sistema para o mapeamento do texto.
	 */
	protected Properties elements;

	/**
	 * Metodo que cria o arquivo de valida��o do arquivo xml inserido
	 * 
	 * @param arrayList
	 *            , Vetor de string contendo os n�veis para cria��o do script.
	 * @return script validador da estrutura XML do arquivo de texto.
	 */
	public String createDTDValidator(ArrayList<String> arrayList) {
		ArrayList<String> levelsMapped = addBannedLevels(arrayList);
		String docType = "<!DOCTYPE " + configuration.getKey("LEVEL_ROOT")
				+ " [";
		String elementsValidation = "";
		elements = SpringFactory.getInstance().getBean("textProperties",
				Properties.class);
		for (int i = 0; i < levelsMapped.size(); i++) {
			String child = getChild(levelsMapped, levelsMapped.get(i));
			docType += "<!ELEMENT " + levelsMapped.get(i) + " (" + child
					+ ") >";
			int x = 1, y = i + 1;
			String elementLevel = getElement(y, x);
			while (!elementLevel.isEmpty()) {
				elementsValidation += "<!ATTLIST " + levelsMapped.get(y) + " "
						+ elementLevel + " CDATA #IMPLIED>";
				x += 1;
				elementLevel = getElement(y, x);
			}
		}
		docType += elementsValidation;
		return docType + " ]>";
	}

	/**
	 * @param levelNumber
	 *            , numero do n�vel
	 * @param elementNumber
	 *            , numero do elemento
	 * @return Valor do elemento espec�fico do arquivo properties dos elementos
	 *         padr�o para mapeamento do texto.
	 */
	protected String getElement(int levelNumber, int elementNumber) {
		return elements.getKey("nivel" + levelNumber + "_element"
				+ elementNumber);
	}

	/**
	 * M�todo que retorna um vetor com os levels padr�o do sistema
	 * 
	 * @param quantLevels
	 *            , quantidade de levels desejado
	 * @return vetor de string com os n�veis padr�o do sistema.
	 */
	public ArrayList<String> getDefaultLevels(Integer quantLevels) {
		ArrayList<String> defaultLevels = new ArrayList<>();
		for (int i = 1; i <= quantLevels; i++) {
			defaultLevels.add(configuration.getKey("LEVEL_DEFAULT") + i);
		}
		return defaultLevels;
	}

	/**
	 * @param arrayList
	 *            , vetor de string com os n�veis que dever�o ser banidos
	 * @return vetor de string com os n�veis banidos para o mapeamento do texto.
	 */
	public ArrayList<String> addBannedLevels(ArrayList<String> arrayList) {
		ArrayList<String> levels = new ArrayList<>();
		levels.add(configuration.getKey("LEVEL_ROOT"));
		levels.addAll(arrayList);
		levels.add(configuration.getKey("LEVEL_PAGE"));
		levels.add(configuration.getKey("LEVEL_CONTENT"));
		levels.add(configuration.getKey("LEVEL_REFERENCE"));
		return levels;
	}

	/**
	 * M�todo respons�vel por devolver o valor poss�vel para os elementos da
	 * valida��o DTD sendo que para cada n�vel do vetor existem de 0 a N
	 * "filhos" poss�veis.
	 * 
	 * @param arrayList
	 *            , vetor de string que ser� varrido em busca dos elementos
	 * @param selectedLevel
	 *            , n�vel o qual ser� retornado o "filho" para valida��o.
	 * @return o valor String necess�rio para mapeamento do validador DTD
	 */
	public String getChild(ArrayList<String> arrayList, String selectedLevel) {
		String child = "";
		int p = 0;
		for (int i = 0; i < arrayList.size(); i++) {
			if (arrayList.get(i).equalsIgnoreCase(selectedLevel)) {
				p = i + 1;
				child += p < arrayList.size() ? arrayList.get(p) : "";
				break;
			}
		}
		if (child.equalsIgnoreCase(configuration.getKey("LEVEL_PAGE")))
			return configuration.getKey("LEVEL_PAGE") + ","
					+ configuration.getKey("LEVEL_CONTENT") + ","
					+ configuration.getKey("LEVEL_REFERENCE") + "*";
		if (child.equalsIgnoreCase(configuration.getKey("LEVEL_REFERENCE"))
				|| child.equalsIgnoreCase(""))
			return "#PCDATA";
		if (child.equalsIgnoreCase(configuration.getKey("LEVEL_CONTENT")))
			return "#PCDATA";
		return child + "*";
	}

	/**
	 * M�todo que verifica o tipo do documento inserido e direciona para outro
	 * m�todo respons�vel pela cria��o do arquivo de texto a ser inserido no
	 * sistema, que poder� ser realizado de duas formas, via TXT ou via XML.
	 * 
	 * @param docTypeValid
	 *            , script de valida��o do arquivo de XML
	 * @param source
	 *            , conte�do do texto a ser inserido
	 * @param type
	 *            , tipo do texto em quest�o TXT ou XML
	 * @param linesForPage
	 *            , quantidade de linhas por p�ginas para o mapeamento txt
	 * @param pagesForChapter
	 *            , quantidade de p�ginas por cap�tulo do mapeamento txt.
	 * @return {@link Return}
	 */
	public Return createText(String docTypeValid, String source, String type,
			Integer linesForPage, Integer pagesForChapter) {
		Return retCreate = new Return(true);
		if (type.equalsIgnoreCase("xml"))
			retCreate.concat(createDocumentByXML(docTypeValid, source));
		else
			retCreate.concat(createDocumentByTXT(source, linesForPage,
					pagesForChapter));
		return retCreate;
	}

	/**
	 * Cria o arquivo de XML apartir de um arquivo TXT inserido onde ser�o
	 * mapeados os n�veis para inser��o do texto.
	 * 
	 * @param source
	 *            , conte�do do arquivo
	 * @param linesForPage
	 *            , quantidade de linhas por cada p�gina para o mapeamento
	 * @param pagesForChapter
	 *            , quantidade de p�ginas por cada cap�tulo para o mapeamento
	 * @return {@link Return}
	 */
	private Return createDocumentByTXT(String source, Integer linesForPage,
			Integer pagesForChapter) {
		Return retTxt = new Return(true);
		StringReader stringReader = new StringReader(source);
		BufferedReader buffer = new BufferedReader(stringReader);
		Document doc = new Document();
		try {
			String thisLine;
			List<String> linesOfPage = new ArrayList<String>();
			while ((thisLine = buffer.readLine()) != null) {
				linesOfPage.add(thisLine);
			}
			HashMap<String, Element> mapPages = mappingPages(linesOfPage,
					linesForPage);
			HashMap<String, Element> mapChapters = mappingChapters(mapPages,
					pagesForChapter);
			int i = 1;
			String key = "chapter" + i++;
			Element root = createElementForXML(
					configuration.getKey("LEVEL_ROOT"), null), chapter = mapChapters
					.get(key);
			while (chapter != null) {
				root.addContent(chapter);
				key = "chapter" + i++;
				chapter = mapChapters.get(key);
			}
			doc.addContent(root);
			textData.put("document", doc);
		} catch (IOException e) {
			retTxt.setValid(false);
			new ExceptionManager(e).treatException();
		}
		return retTxt;
	}

	/**
	 * Mapeia os cap�tulos de acordo com as p�ginas mapeadas e com a quantidade
	 * informada da quantidade de p�ginas por cap�tulo.
	 * 
	 * @param mapPages
	 *            , mapa contendo as p�ginas mapeadas do arquivo
	 * @param pagesForChapter
	 *            , quantidade de p�ginas por cap�tulo.
	 * @return Mapa com os cap�tulos mapeados.
	 */
	private HashMap<String, Element> mappingChapters(
			HashMap<String, Element> mapPages, Integer pagesForChapter) {
		HashMap<String, Element> mapChapter = new HashMap<String, Element>();
		int countPages = 1, numberChapter = 1, i = 1, x = 1;
		String key = "page" + i++;
		Element chapter = createTXTElement(numberChapter++, null, "chapter"), page = mapPages
				.get(key);
		while (page != null) {
			if (countPages > pagesForChapter) {
				mapChapter.put("chapter" + x++, chapter);
				chapter = createTXTElement(numberChapter++, page, "chapter");
				key = "page" + i++;
				page = mapPages.get(key);
				countPages = 2;
			}
			if (page != null)
				chapter.addContent(page);
			key = "page" + i++;
			page = mapPages.get(key);
			if (page == null)
				mapChapter.put("chapter" + x++, chapter);
			countPages += 1;
		}
		return mapChapter;
	}

	/**
	 * Mapeia as p�ginas de acordo com uma lista das linhas lidas no arquivo e
	 * com a quantidade informada de linhas por p�gina.
	 * 
	 * @param linesOfPage
	 *            , lista de linhas lidas
	 * @param linesForPage
	 *            , quantidade de linhas que dever� existir por p�gina
	 * @return um mapa com as p�ginas mapeadas.
	 */
	private HashMap<String, Element> mappingPages(List<String> linesOfPage,
			Integer linesForPage) {
		HashMap<String, Element> mapPages = new HashMap<String, Element>();
		List<String> list = new ArrayList<String>();
		Element element = null, page = null;
		String content;
		int initPage = 0, endPage = linesForPage, countPage = 1, aux;
		for (int i = 0; i < linesOfPage.size() / linesForPage + 1; i++) {
			list = linesOfPage.subList(initPage, endPage);
			if (!list.isEmpty()) {
				content = extractListString(list);
				element = addContentInPage(content);
				page = createTXTElement(countPage, element, "page");
				mapPages.put("page" + countPage++, page);
				initPage = endPage;
				aux = endPage += linesForPage;
				endPage = aux < linesOfPage.size() ? aux : linesOfPage.size();
			}
		}
		return mapPages;
	}

	/**
	 * M�todo que contatena uma lista de String em uma �nica String
	 * 
	 * @param list
	 *            , lista a ser concatenada
	 * @return String com todas as strings concatenadas
	 */
	private String extractListString(List<String> list) {
		String aux = "";
		for (int i = 0; i < list.size(); i++) {
			aux += list.get(i);
		}
		return aux;
	}

	/**
	 * Cria os elementos necess�rios para mapeamento do Texto em Document
	 * 
	 * @param number
	 *            , sequencia do elemento
	 * @param cont
	 *            , conteudo se houver
	 * @param type
	 *            , poder� se chapter ou page
	 * @return retorna um novo elemento Element para adicionar ao Document
	 */
	private Element createTXTElement(Integer number, Content cont, String type) {
		if (type.equalsIgnoreCase("page"))
			return createElementForXML(configuration.getKey("LEVEL_PAGE")
					+ number, cont);
		return createElementForXML(configuration.getKey("LEVEL_CHAPTER")
				+ number, cont);
	}

	/**
	 * Cria um elemento com uma lista de filhos
	 * 
	 * @param name
	 *            , nome do elemento
	 * @param content
	 *            , lista de filhos
	 * @return o novo elemento
	 */
	private Element createElementForXML(String name, Content con) {
		Element element = new Element(name);
		if (con != null)
			element.addContent(con);
		return element;
	}

	/**
	 * Cria o elemento conteudo para o mapeamento do TXT
	 * 
	 * @param content
	 *            , o conteudo do elemento
	 * @return um novo elemento com nome de conteudo e valor content
	 */
	private Element addContentInPage(String content) {
		Element element = new Element(configuration.getKey("LEVEL_CONTENT"));
		element.setText(content);
		return element;
	}

	/**
	 * Este m�todo servir� para valida��o do arquivo XML a n�vel de sua
	 * hierarquia Atrav�s do arquivo em String e o DTD, que define a estrutura
	 * v�lida para o arquivo.
	 * 
	 * @param docType
	 * @param str
	 * @return
	 */
	private Return createDocumentByXML(String docTypeValid, String source) {
		Return retHierarchy = new Return(true);
		SAXBuilder saxBuilder = new SAXBuilder(XMLReaders.DTDVALIDATING);
		try {
			StringReader characterStream = new StringReader(docTypeValid
					+ source);
			Document document = saxBuilder.build(characterStream);
			if (document != null) {
				textData.put("document", document);
				retHierarchy.addMessage(new Message(null, "Arquivo validado"));
			} else {
				retHierarchy.setValid(false);
				retHierarchy.addMessage(new Message(null,
						"Ocorreu uma falha na valida��o do arquivo!"));
			}
			return retHierarchy;
		} catch (IOException e) {
			retHierarchy.setValid(false);
			retHierarchy.addMessage(new Message(null,
					"O mapeamento do arquivo est� incorreto! "
							+ "Houve falha na leitura!"));
		} catch (JDOMException e) {
			retHierarchy.setValid(false);
			retHierarchy.addMessage(new Message(null,
					"O mapeamento do arquivo est� incorreto! "
							+ "Por favor leia o manual de inser��o de aquivo, "
							+ "Modalidade Avan�ada"));
		}
		return retHierarchy;
	}

	/**M�todo que mapeia os dados dos do Documento criado pela leitura XML
	 * da classe {@link Document} em um objeto do tipo texto para ser persistido
	 * no sistema.
	 * @return {@link Return}
	 */
	public Return mappingText() {
		Return retMapp = new Return(true);
		try {
			Document document = (Document) textData.get("document");
			Text text = (Text) textData.get("text");
			Element rootElement = document.getRootElement();
			text.setRootLevelText(extractLevelsText(rootElement, text));
			textData.put("text", text);
		} catch (Exception e) {
			retMapp.setValid(false);
			new ExceptionManager(e).treatException();
		}
		return retMapp;
	}

	/**M�todo recursivo respons�vel pelo mapeamento dos n�veis do texto em um �nico
	 * objeto do tipo texto
	 * @param rootElement, elemento raiz por onde ser� iniciado o mapeamento
	 * @param text, objeto final onde ser�o reunidos todos os n�veis mapeados
	 * @return {@link Return}
	 */
	private LevelText extractLevelsText(Element rootElement, Text text) {
		LevelText level = extractLevel(rootElement, text);
		for (Element child : rootElement.getChildren()) {
			level.getLevelsChildren().add(extractLevelsText(child, text));
		}
		return level;
	}

	/** Extrai o conte�do de um elemento do tipo {@link Element} mapeado no mapeamento
	 * XML em um objeto do tipo n�vel do texto
	 * @param element, elemento do qual ser�o extraidas as informa��es
	 * @param text, o texto que receber� os n�veis compat�veis mapeados
	 * @return {@link LevelText} de acordo com as extra��o em quest�o.
	 */
	private LevelText extractLevel(Element element, Text text) {
		LevelText levelText = new LevelText();
		String nameElement = element.getName();
		levelText.setName(nameElement);
		levelText.setIdText(text);
		levelText.setElements(extractElements(element, levelText));
		return levelText;
	}

	/**
	 * Este m�todo extrai os elementos de um dado n�vel do texto
	 * 
	 * @param element
	 *            , que representa o n�vel do texto
	 * @return uma lista de elementos pertencentes aquele n�vel
	 */
	private List<ElementText> extractElements(Element element, LevelText level) {
		List<ElementText> listElementsText = new ArrayList<>();
		List<Attribute> listAttributes = element.getAttributes();
		ElementText elementText = new ElementText();
		for (int i = 0; i < listAttributes.size(); i++) {
			elementText.setName(listAttributes.get(i).getName());
			elementText.setValue(listAttributes.get(i).getValue());
			elementText.setIdLevel(level);
			listElementsText.add(elementText);
		}
		if (element.getName().equalsIgnoreCase(
				configuration.getKey("LEVEL_CONTENT"))
				|| element.getName().equalsIgnoreCase(
						configuration.getKey("LEVEL_PAGE"))
				|| element.getName().equalsIgnoreCase(
						configuration.getKey("LEVEL_REFERENCE"))) {
			elementText.setIdLevel(level);
			if (element.getName().equalsIgnoreCase(
					configuration.getKey("LEVEL_PAGE")))
				elementText.setName(configuration.getKey("LEVEL_PAGE"));
			else
				elementText.setName(configuration.getKey("LEVEL_VALUE"));
			elementText.setValue(element.getText());
			listElementsText.add(elementText);
		}
		if (listElementsText.isEmpty())
			return null;
		return listElementsText;
	}

}
