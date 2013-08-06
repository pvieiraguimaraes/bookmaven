package br.ueg.tcc.bookway.control.textfactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jdom2.Attribute;
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

@Service
@Scope("prototype")
@SuppressWarnings("serial")
public class TextReader extends TextBookwayIO {
	
	public TextReader(HashMap<String, Object> map) {
		textData = map;
	}
	
	protected Properties elements;
	
	public String createDTDValidator(ArrayList<String> arrayList) {
		ArrayList<String> levelsMapped = addBannedLevels(arrayList);
		String docType = "<!DOCTYPE documento [";
		String elementsValidation = "";
		elements = SpringFactory.getInstance().getBean("textProperties", Properties.class);
		for (int i = 0; i < levelsMapped.size(); i++) {
			String child = getChild(levelsMapped, levelsMapped.get(i));
			docType += "<!ELEMENT " + levelsMapped.get(i) + " (" + child + ") >";
			int x = 1, y = i+1;
			String elementLevel = getElement(y, x);
			while (!elementLevel.isEmpty()) {
				elementsValidation += "<!ATTLIST " + levelsMapped.get(y) + " " + elementLevel + " CDATA #IMPLIED>";
				x += 1; elementLevel = getElement(y, x);
			}
		}
		docType += elementsValidation;
		return docType + " ]>";
	}
	
	protected String getElement(int levelNumber, int elementNumber){
		return elements.getKey("nivel"+levelNumber+"_element"+elementNumber);
	}

	public ArrayList<String> getDefaultLevels(Integer quantLevels) {
		ArrayList<String> defaultLevels = new ArrayList<>();
		for (int i = 1; i <= quantLevels; i++) {
			defaultLevels.add(configuration.getKey("LEVEL_DEFAULT")+i);
		}
		return defaultLevels;
	}

	public ArrayList<String> addBannedLevels(ArrayList<String> arrayList) {
		ArrayList<String> levels = new ArrayList<>();
		levels.add(configuration.getKey("LEVEL_ROOT"));
		levels.addAll(arrayList);
		levels.add(configuration.getKey("LEVEL_CONTENT"));
		levels.add(configuration.getKey("LEVEL_REFERENCE"));
		return levels;
	}

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
		if(child.equalsIgnoreCase(configuration.getKey("LEVEL_CONTENT"))) 
			return configuration.getKey("LEVEL_CONTENT") + "," + configuration.getKey("LEVEL_REFERENCE") + "*";
		if (child.equalsIgnoreCase(configuration.getKey("LEVEL_REFERENCE")) || child.equalsIgnoreCase(""))
			return "#PCDATA";
		return child + "*";
	}
	
	/**
	 * Este método servirá para validação do arquivo XML a nível de sua
	 * hierarquia Através do arquivo em String e o DTD, que define a estrutura
	 * válida para o arquivo.
	 * 
	 * @param docType
	 * @param str
	 * @return
	 */
	public Return createText(String docTypeValid, String source) {
		Return retHierarchy = new Return(true);
		SAXBuilder saxBuilder = new SAXBuilder(XMLReaders.DTDVALIDATING);
		try {
			StringReader characterStream = new StringReader(docTypeValid
					+ source);
			Document document = saxBuilder.build(characterStream);
			if (document != null){
				textData.put("document", document);
				retHierarchy.addMessage(new Message(null,
						"Arquivo validado"));
			}
			else {
				retHierarchy.setValid(false);
				retHierarchy.addMessage(new Message(null,
						"Ocorreu uma falha na validação do arquivo!"));
			}
			return retHierarchy;
		} catch (IOException e) {
			retHierarchy.setValid(false);
			retHierarchy.addMessage(new Message(null,
					"O mapeamento do arquivo está incorreto! "
							+ "Houve falha na leitura!"));
		} catch (JDOMException e) {
			retHierarchy.setValid(false);
			retHierarchy.addMessage(new Message(null,
					"O mapeamento do arquivo está incorreto! "
							+ "Por favor leia o manual de inserção de aquivo, "
							+ "Modalidade Avançada"));
		}
		return retHierarchy;
		//TODO Resolver as menssagens deste metodo
	}

	public Return mappingText() {
		Return retMapp = new Return(true);
		try {
			Document document = (Document) textData.get("document");
			Text text = (Text) textData.get("text");
			Element rootElement = document.getRootElement();
			text.setLevelsText(extractLevelsText(rootElement, text));
			textData.put("text", text);
		} catch (Exception e) {
			retMapp.setValid(false);
			new ExceptionManager(e).treatException();
		}
		return retMapp;
	}
	
	private LevelText extractLevelsText(Element rootElement, Text text){
		LevelText level = extractLevel(rootElement, text);
		for (Element child : rootElement.getChildren()) {
			level.getLevelsChildren().add(extractLevelsText(child, text));
		}
		return level;
	}

	private LevelText extractLevel(Element element, Text text){
		LevelText levelText = new LevelText();
		String nameElement = element.getName();
		levelText.setName(nameElement);
		levelText.setIdText(text);
		levelText.setElements(extractElements(element, levelText));
		return levelText;
	}
	
	
	
	/**Este método extrai os elementos de um dado nível do texto
	 * @param element, que representa o nível do texto
	 * @return uma lista de elementos pertencentes aquele nível
	 */
	private List<ElementText> extractElements(Element element, LevelText level){
		List<ElementText> listElementsText = new ArrayList<>();
		List<Attribute> listAttributes = element.getAttributes();
		ElementText elementText = new ElementText();
		for(int i = 0; i < listAttributes.size(); i++){
			elementText.setName(listAttributes.get(i).getName());
			elementText.setValue(listAttributes.get(i).getValue());
			elementText.setIdLevel(level);
			listElementsText.add(elementText);
		}
		if(element.getName().equalsIgnoreCase(configuration.getKey("LEVEL_CONTENT")) || element.getName().equalsIgnoreCase(configuration.getKey("LEVEL_REFERENCE"))){
			elementText.setIdLevel(level);
			elementText.setName(configuration.getKey("LEVEL_VALUE"));
			elementText.setValue(element.getText());
			listElementsText.add(elementText);
		}
		if(listElementsText.isEmpty())
			return null;
		return listElementsText;
	}
	
}
