package br.ueg.tcc.bookway.view.macros;

import org.zkoss.zk.ui.HtmlMacroComponent;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Label;

@SuppressWarnings("serial")
@VariableResolver(value = {org.zkoss.zkplus.spring.DelegatingVariableResolver.class})
public class ItemStudy extends HtmlMacroComponent {

	@Wire
	private Label contentElement;
	
	public ItemStudy() {
		compose();
	}

	public String getContent() {
		return this.contentElement.getValue();
	}

	public void setContent(String contentElement) {
		this.contentElement.setValue(contentElement);
	}

}

