package br.ueg.tcc.bookway.model;

import br.com.vexillum.model.CommonEntity;

/**
 * Define a entidade modelo para a navegação de texto
 * 
 * @author pedro
 * 
 */
@SuppressWarnings("serial")
public class ItemNavigationStudy extends CommonEntity {

	private Text text;
	private LevelText levelText;
	private ElementText elementText;

	public Text getText() {
		return text;
	}

	public void setText(Text text) {
		this.text = text;
	}

	public LevelText getLevelText() {
		return levelText;
	}

	public void setLevelText(LevelText levelText) {
		this.levelText = levelText;
	}

	public ElementText getElementText() {
		return elementText;
	}

	public void setElementText(ElementText elementText) {
		this.elementText = elementText;
	}

}
