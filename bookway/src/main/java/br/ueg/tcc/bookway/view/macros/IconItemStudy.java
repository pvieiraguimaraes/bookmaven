package br.ueg.tcc.bookway.view.macros;

import org.zkoss.zk.ui.HtmlMacroComponent;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Label;

/**
 * Macro criado para representar a existência de itens de estudo e um
 * determinado elemento do texto
 * 
 * @author pedro
 * 
 */
@SuppressWarnings("serial")
@VariableResolver(value = { org.zkoss.zkplus.spring.DelegatingVariableResolver.class })
public class IconItemStudy extends HtmlMacroComponent {

	/**
	 * Código do elemento do texto
	 */
	@Wire
	private Label idElementText;

	public IconItemStudy() {
		compose();
	}

	public String getIdElementText() {
		return idElementText.getValue();
	}

	public void setIdElementText(String idElementText) {
		this.idElementText.setValue(idElementText);
	}
}
