package br.ueg.tcc.bookway.view.macros;

import org.zkoss.zk.ui.HtmlMacroComponent;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;

@SuppressWarnings("serial")
@VariableResolver(value = { org.zkoss.zkplus.spring.DelegatingVariableResolver.class })
public class ItemOfPanel extends HtmlMacroComponent {

	private Label titleItem;

	private Textbox descriptionItem;

	private Boolean titleItemVisible;

	private Boolean descriptionItemVisible;

	private Boolean panelButtonsItemVisible;

	public Label getTitleItem() {
		return titleItem;
	}

	public void setTitleItem(String titleItem) {
		this.titleItem.setValue(titleItem);
	}

	public String getDescriptionItem() {
		return descriptionItem.getValue();
	}

	public void setDescriptionItem(String descriptionItem) {
		this.descriptionItem.setValue(descriptionItem);
	}

	public Boolean getDescriptionItemVisible() {
		return descriptionItemVisible;
	}

	public void setDescriptionItemVisible(Boolean descriptionItemVisible) {
		this.descriptionItemVisible = descriptionItemVisible;
	}

	public Boolean getPanelButtonsItemVisible() {
		return panelButtonsItemVisible;
	}

	public void setPanelButtonsItemVisible(Boolean panelButtonsItemVisible) {
		this.panelButtonsItemVisible = panelButtonsItemVisible;
	}

	public Boolean getTitleItemVisible() {
		return titleItemVisible;
	}

	public void setTitleItemVisible(Boolean titleItemVisible) {
		this.titleItemVisible = titleItemVisible;
	}

}
