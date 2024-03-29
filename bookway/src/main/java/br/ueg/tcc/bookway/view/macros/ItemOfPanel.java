package br.ueg.tcc.bookway.view.macros;

import org.zkoss.zk.ui.HtmlMacroComponent;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;

/**Componente utilizando para criar itens do painel de itens de estudo durante a navega��o de estudo
 * 
 * @author pedro
 *
 */
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
	
//	@Wire
//	private Button btnEditRelationship;
	
//	@Wire
//	private Button btnExcludeRelationship;
	
	@Wire
	private Div marking;
	
	@Wire
	private Div annotation;
	
	@Wire
	private Div relationship;
	
	@Wire
	private Label idItem;

	@Wire
	private Label nameUser;
	
	public ItemOfPanel() {
		compose();
	}
	
	/**Construtor padr�o
	 * @param title, t�tulo que o item dever� ter
	 * @param description, descri��o que ser� exibida no item
	 */
	public ItemOfPanel(String title, String description) {
		compose();
		setTitle(title);
		setDescription(description);
	}
	
	/** Contrutor do componente com op��es de tipagem para os elementos
	 * @param typeItem, valor correspondente ao tipo do item "MarkingOfUser" ou "Annotaion" ou "Relationship"
	 * @param title, t�tulo que ser� exibido no item
	 * @param description, descri��o do item
	 * @param backgroundColor, cor de fundo do item caso exista
	 * @param titleVisible, verdadeiro se o t�tulo � vis�vel
	 * @param nameMarkingVisible, verdadeiro se o nome da marca��o � vis�vel
	 * @param descriptionVisible, verdadeiro se a descri��o � vis�vel
	 * @param btnExcludeMarkingVisible, verdadeiro se o bot�o de exclus�o da marca��o � vis�vel
	 * @param btnEditAnnotVisible, verdadeiro se o bot�o de edit�o da anota��o � vis�vel
	 * @param btnExcludeAnnotVisible, verdadeiro se o bot�o de exclus�o da anota��o � vis�vel
	 * @param idItem, c�digo do item de estudo 
	 * @param nameUser, nome do usu�rio a quem o item pertence
	 */
	public ItemOfPanel(String typeItem, String title, String description, String backgroundColor, boolean titleVisible, boolean nameMarkingVisible,
			boolean descriptionVisible, boolean btnExcludeMarkingVisible,  boolean btnEditAnnotVisible, boolean btnExcludeAnnotVisible,
			String idItem, String nameUser) {
		compose();
		this.idItem.setValue(idItem);
		this.nameUser.setValue(nameUser);
		if (typeItem.equalsIgnoreCase("MarkingOfUser")) {
			marking.setVisible(true);
			annotation.setVisible(false);
			relationship.setVisible(false);
			if(backgroundColor != null)
				marking.setStyle("background-color: " + backgroundColor);
			setMarking(title);
			nameMarking.setVisible(nameMarkingVisible);
			btnExcludeMarking.setVisible(btnExcludeMarkingVisible);
		}
		
		if (typeItem.equalsIgnoreCase("Annotation")) {
			annotation.setVisible(true);
			marking.setVisible(false);
			relationship.setVisible(false);
			setTitle(title);
			titleItem.setVisible(titleVisible);
			setDescription(description);
			descriptionItem.setVisible(descriptionVisible);
			btnEditAnnot.setVisible(btnEditAnnotVisible);
			btnExcludeAnnot.setVisible(btnExcludeAnnotVisible);
		}
		
		if (typeItem.equalsIgnoreCase("Relationship")){
			annotation.setVisible(true);
			marking.setVisible(false);
			relationship.setVisible(false);
			setDescription(description);
			descriptionItem.setVisible(descriptionVisible);
//			btnEditRelationship.setVisible(true);
//			btnExcludeRelationship.setVisible(true);
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
