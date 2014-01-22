package br.ueg.tcc.bookway.view.macros;

import org.zkoss.zk.ui.HtmlMacroComponent;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;

@SuppressWarnings("serial")
@VariableResolver(value = { org.zkoss.zkplus.spring.DelegatingVariableResolver.class })
public class ItemOfPanel extends HtmlMacroComponent {

	@Wire
	private Label titleItem;

	@Wire
	private Label descriptionItem;
	
	@Wire
	private Label nameMarking;
	
	@Wire
	private Button btnExcludeMarking;
	
	@Wire
	private Button btnEditAnnot;
	
	@Wire
	private Button btnExcludeAnnot;
	
	@Wire
	private Div marking;
	
	@Wire
	private Div annotation;

	public ItemOfPanel() {
		compose();
	}
	
	public ItemOfPanel(String title, String description) {
		compose();
		setTitle(title);
		setDescription(description);
	}
	
	public ItemOfPanel(String typeItem, String title, String description, String backgroundColor, boolean titleVisible, boolean nameMarkingVisible,
			boolean descriptionVisible, boolean btnExcludeMarkingVisible,  boolean btnEditAnnotVisible, boolean btnExcludeAnnotVisible) {
		compose();
		
		if (typeItem.equalsIgnoreCase("MarkingOfUser")) {
			marking.setVisible(true);
			annotation.setVisible(false);
			if(backgroundColor != null)
				marking.setStyle("background-color: " + backgroundColor);
			setMarking(title);
			nameMarking.setVisible(nameMarkingVisible);
			btnExcludeMarking.setVisible(btnExcludeMarkingVisible);
		}
		
		if (typeItem.equalsIgnoreCase("Annotation")) {
			annotation.setVisible(true);
			marking.setVisible(false);
			setTitle(title);
			titleItem.setVisible(titleVisible);
			setDescription(description);
			descriptionItem.setVisible(descriptionVisible);
			btnEditAnnot.setVisible(btnEditAnnotVisible);
			btnExcludeAnnot.setVisible(btnExcludeAnnotVisible);
		}
	}
	
	public String getMarking() {
		return nameMarking.getValue();
	}

	public void setMarking(String titleItem) {
		this.nameMarking.setValue(titleItem);
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
