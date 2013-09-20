package br.ueg.tcc.bookway.model;

import br.com.vexillum.model.CommonEntity;

@SuppressWarnings("serial")
public class ItemNavigationStudy extends CommonEntity{

	private LevelText levelText;
	private ElementText elementText;

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
