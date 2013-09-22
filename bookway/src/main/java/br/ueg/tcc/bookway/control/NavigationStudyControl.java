package br.ueg.tcc.bookway.control;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import br.com.vexillum.control.GenericControl;
import br.com.vexillum.util.ReflectionUtils;
import br.com.vexillum.util.SpringFactory;
import br.ueg.tcc.bookway.model.ItemNavigationStudy;
import br.ueg.tcc.bookway.model.LevelText;
import br.ueg.tcc.bookway.model.Text;

@Service
@Scope("prototype")
public class NavigationStudyControl extends GenericControl<ItemNavigationStudy> {

	public NavigationStudyControl() {
		super(ItemNavigationStudy.class);
	}

	public TextControl getTextControl() {
		return SpringFactory.getController("textControl", TextControl.class,
				ReflectionUtils.prepareDataForPersistence(this.data));
	}

	public Text getTextById(Long idText) {
		return getTextControl().getTextById(idText);
	}
	
	public void getLevelsChildren(LevelText levelText){
		//TODO Buscar todos os níveis filhos de um nível
	}
	
	public void getElementsLevel(LevelText levelText){
		//TODO Buscar todos os elementos de um nível
	}
}
