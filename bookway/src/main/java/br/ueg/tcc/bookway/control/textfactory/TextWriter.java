package br.ueg.tcc.bookway.control.textfactory;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.jdom2.Document;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import br.com.vexillum.util.Message;
import br.com.vexillum.util.Return;
import br.ueg.tcc.bookway.model.ElementText;
import br.ueg.tcc.bookway.model.LevelText;
import br.ueg.tcc.bookway.model.Text;
import br.ueg.tcc.bookway.model.UserBookway;
import br.ueg.tcc.bookway.model.enums.TypeText;

public class TextWriter {

	public Return insertTextIntoRepository(List<?> list) {
		Return retTxtInsert = new Return(true);
		try {
			XMLOutputter outputter = new XMLOutputter();
			outputter.setFormat(Format.getPrettyFormat().setOmitEncoding(true)
					.setOmitDeclaration(true));
			// TODO Teste para gerar o arquivo, até poder pegar o usuário
			// logado.
			Random randomGenerator = new Random();
			int randomInt = randomGenerator.nextInt(100);
			// TODO Pegar o caminho do repositório por properties.
			String path = "D:/Bookway/Repository/user" + randomInt + ".xml";
			Document doc = (Document) list.get(0);
			doc.setDocType(null);
			outputter.output(doc, new FileWriter(path));
		} catch (Exception e) {
			retTxtInsert.setValid(false);
			retTxtInsert.addMessage(new Message(null,
					"Não foi possível inserir arquivo no repositório!"));

		}
		return retTxtInsert;
	}
	
	//TODO Remover este metodo, apenas para testes
	public List<ElementText> getElements(String level, String cont, LevelText levelText){
		ElementText element1 = new ElementText();
		element1.setName(level+cont);
		element1.setValue(level+cont);
		element1.setIdLevel(levelText);
		
		
		ElementText element2 = new ElementText();
		element2.setName(level+cont);
		element2.setValue(level+cont);
		element2.setIdLevel(levelText);
		
		ElementText element3 = new ElementText();
		element3.setName(level+cont);
		element3.setValue(level+cont);
		element3.setIdLevel(levelText);
		
		ElementText element4 = new ElementText();
		element4.setName(level+cont);
		element4.setValue(level+cont);
		element4.setIdLevel(levelText);
		
		ElementText element5 = new ElementText();
		element5.setName(level+cont);
		element5.setValue(level+cont);
		element5.setIdLevel(levelText);
		
		ElementText element6 = new ElementText();
		element6.setName(level+cont);
		element6.setValue(level+cont);
		element6.setIdLevel(levelText);
		
		List<ElementText> elements = new ArrayList<>();
		elements.add(element1);
		elements.add(element2);
		elements.add(element3);
		elements.add(element4);
		elements.add(element5);
		elements.add(element6);
		
		return elements;
	}
	
	//TODO Remover este metodo, apenas para testes
	public Text testInsertText(){
		Text text = new Text();
		text.setCommunity(true);
		text.setDescription("Teste de Inserção");
		text.setFilePath("C:/teste");
		text.setInsertDate(new Date());
		
		LevelText documento = new LevelText();
		documento.setIdText(text);
		documento.setName("documento");
		documento.setElements(getElements("documento", "1", documento));
		
		LevelText parte = new LevelText();
		parte.setIdText(text);
		parte.setName("parte");
		parte.setElements(getElements("parte", "2", parte));		
		
		LevelText livro = new LevelText();
		livro.setIdText(text);
		livro.setName("livro");
		livro.setElements(getElements("livro", "3", livro));
		
		LevelText capitulo = new LevelText();
		capitulo.setIdText(text);
		capitulo.setName("capitulo");
		capitulo.setElements(getElements("capitulo", "4", capitulo));
		
		LevelText versiculo = new LevelText();
		versiculo.setIdText(text);
		versiculo.setName("versiculo");
		versiculo.setElements(getElements("versiculo", "5", versiculo));
		
		LevelText conteudo = new LevelText();
		conteudo.setIdText(text);
		conteudo.setName("conteudo");
		conteudo.setElements(getElements("conteudo", "6", conteudo));
		
		LevelText referencia = new LevelText();
		referencia.setIdText(text);
		referencia.setName("referencia");
		referencia.setElements(getElements("referencia", "7", referencia));
		
		List<LevelText> levelsText = new ArrayList<>();
		levelsText.add(documento);
		levelsText.add(parte);
		levelsText.add(livro);
		levelsText.add(capitulo);
		levelsText.add(versiculo);
		levelsText.add(conteudo);
		levelsText.add(referencia);
		
		text.setTitle("Teste para inserir");
		text.setTypeText(TypeText.PUBLICO);
		
		
		text.setUserOwning(getUser());
		
		
		return text;
	}
	
	//TODO Remover este metodo, apenas para testes
	private UserBookway getUser(){
		UserBookway user = new UserBookway();
		user.setId((long) 1);
		return user;
	}

}
