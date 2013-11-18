package br.ueg.tcc.bookway.view.macros;

import org.zkoss.zk.ui.HtmlMacroComponent;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Label;

@SuppressWarnings("serial")
@VariableResolver(value = {org.zkoss.zkplus.spring.DelegatingVariableResolver.class})
public class ItemStudy extends HtmlMacroComponent {

	@Wire
	public Label contentElement;
	
	@Wire
	private Label idText;
	
	@Wire
	private Label idLevel;
	
	public ItemStudy() {
		compose();
	}
	
	public String getIdText() {
		return this.idText.getValue();
	}

	public void setIdText(String idText) {
		this.idText.setValue(idText);
	}

	public String getIdLevel() {
		return this.idLevel.getValue();
	}

	public void setIdLevel(String idLevel) {
		this.idLevel.setValue(idLevel);
	}


	public String getContent() {
		return this.contentElement.getValue();
	}

	public void setContent(String contentElement) {
		this.contentElement.setValue(contentElement);
	}

}

