package br.ueg.tcc.bookway.view.macros;

import org.zkoss.zk.ui.HtmlMacroComponent;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Label;

@SuppressWarnings("serial")
@VariableResolver(value = {org.zkoss.zkplus.spring.DelegatingVariableResolver.class})
public class MyText extends HtmlMacroComponent {

	@Wire
	private Label titleText;
	@Wire
	private Label descriptionText;
	
	public MyText() {
		compose();
	}

	public String getTitle() {
		return titleText.getValue();
	}

	public void setTitle(String title) {
		titleText.setValue(title);
	}

	public String getDescription() {
		return descriptionText.getValue();
	}

	public void setDescription(String description) {
		descriptionText.setValue(description);
	}
}
