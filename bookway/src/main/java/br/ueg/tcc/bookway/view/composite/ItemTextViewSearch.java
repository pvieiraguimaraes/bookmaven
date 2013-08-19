package br.ueg.tcc.bookway.view.composite;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;

@SuppressWarnings("serial")
public class ItemTextViewSearch extends Div implements IdSpace {
	
	@Wire
	private Label userowner, titletext, descriptiontext;

	public ItemTextViewSearch() {
		Executions.createComponents("/template/itemtext.zul", this, null);
	}

	public String getUserOwner() {
		return userowner.getValue();
	}

	public void setUserOwner(String userOwner) {
		this.userowner.setValue(userOwner);
	}

	public String getTitleText() {
		return titletext.getValue();
	}

	public void setTitleText(String titleText) {
		this.titletext.setValue(titleText);
	}

	public String getDescriptionText() {
		return descriptiontext.getValue();
	}

	public void setDescriptionText(String descriptionText) {
		this.descriptiontext.setValue(descriptionText);
	}
}
