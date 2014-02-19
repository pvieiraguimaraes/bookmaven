package br.ueg.tcc.bookway.control.textfactory;

import java.io.Serializable;
import java.util.HashMap;

import br.com.vexillum.configuration.Properties;
import br.com.vexillum.util.SpringFactory;

/**
 * Super classe utilizada para leitura e escrita de arquivos do texto.
 * 
 * @author pedro
 *
 */
@SuppressWarnings("serial")
public abstract class TextBookwayIO implements Serializable {

	/**
	 * Properties com as configurações padrões dos níveis do texto.
	 */
	protected Properties configuration;
	
	/**
	 * Map utilizado para repassar objetos via controle.
	 */
	protected HashMap<String, Object> textData;

	public TextBookwayIO() {
		configuration = SpringFactory.getInstance().getBean("configProperties",
				Properties.class);
		textData = new HashMap<>();
	}

}
