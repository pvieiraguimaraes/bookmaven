package br.ueg.tcc.bookway.view.macros;

import org.zkoss.zk.ui.HtmlMacroComponent;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Label;

@SuppressWarnings("serial")
@VariableResolver(value = { org.zkoss.zkplus.spring.DelegatingVariableResolver.class })
public class ItemOfPanel extends HtmlMacroComponent {

	@Wire
	private Label titleItem;

	@Wire
	private Label descriptionItem;

	public ItemOfPanel() {
		compose();
	}
	
	public ItemOfPanel(String title, String description) {
		compose();
		setTitle(title);
		setDescription(description);
	}

	public String getTitle() {
		return titleItem.getValue();
	}

	public void setTitle(String titleItem) {
		this.titleItem.setValue(titleItem);
	}

	public String getDescription() {
		return descriptionItem.getValue();
	}

	public void setDescription(String descriptionItem) {
		this.descriptionItem.setValue(descriptionItem);
	}

}
