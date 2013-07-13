package br.ueg.tcc.bookway.control.textfactory;

import java.io.IOException;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
//TODO Classe que não está sendo usada, era uma maneira de realizar a validação do texto
public class TextResolver implements EntityResolver{

	@Override
	public InputSource resolveEntity(String publicId, String systemId)
			throws SAXException, IOException {
		return new InputSource();
	}
}
