package br.ueg.tcc.bookway.control.textfactory;

import java.io.Serializable;
import java.util.HashMap;

import br.com.vexillum.configuration.Properties;
import br.com.vexillum.util.SpringFactory;

@SuppressWarnings("serial")
public abstract class TextBookwayIO implements Serializable {

	protected Properties configuration;
	protected HashMap<String, Object> textData;

	public TextBookwayIO() {
		configuration = SpringFactory.getInstance().getBean("configProperties",
				Properties.class);
		textData = new HashMap<>();
	}

}
