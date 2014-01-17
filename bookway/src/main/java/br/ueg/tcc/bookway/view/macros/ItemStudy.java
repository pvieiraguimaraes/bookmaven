package br.ueg.tcc.bookway.view.macros;

import org.zkoss.zk.ui.HtmlMacroComponent;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Label;

@SuppressWarnings("serial")
@VariableResolver(value = { org.zkoss.zkplus.spring.DelegatingVariableResolver.class })
public class ItemStudy extends HtmlMacroComponent {

	@Wire
	public Label contentElement;

	@Wire
	private Label idText;

	@Wire
	private Label idElement;

	@Wire
	private Label idIconStudy;
	
	public String getIdIconStudy() {
		return this.idIconStudy.getValue();
	}

	public void setIdIconStudy(String idIconStudy) {
		this.idIconStudy.setValue(idIconStudy);
	}

	public ItemStudy() {
		compose();
	}

	public String getIdText() {
		return this.idText.getValue();
	}

	public void setIdText(String idText) {
		this.idText.setValue(idText);
	}

	public String getIdElement() {
		return this.idElement.getValue();
	}

	public void setIdElement(String idElement) {
		this.idElement.setValue(idElement);
	}

	public String getContent() {
		return this.contentElement.getValue();
	}

	public void setContent(String contentElement) {
		this.contentElement.setValue(contentElement);
	}
}
