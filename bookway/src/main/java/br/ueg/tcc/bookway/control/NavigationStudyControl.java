package br.ueg.tcc.bookway.control;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import br.com.vexillum.configuration.Properties;
import br.com.vexillum.control.GenericControl;
import br.com.vexillum.util.ReflectionUtils;
import br.com.vexillum.util.Return;
import br.com.vexillum.util.SpringFactory;
import br.ueg.tcc.bookway.model.ElementText;
import br.ueg.tcc.bookway.model.ItemNavigationStudy;
import br.ueg.tcc.bookway.model.LevelText;
import br.ueg.tcc.bookway.model.Text;

@Service
@Scope("prototype")
public class NavigationStudyControl extends GenericControl<ItemNavigationStudy> {

	protected Properties configuration;
	
	public NavigationStudyControl() {
		super(ItemNavigationStudy.class);
		configuration = SpringFactory.getInstance().getBean("configProperties",
				Properties.class);
	}

	public TextControl getTextControl() {
		return SpringFactory.getController("textControl", TextControl.class,
				ReflectionUtils.prepareDataForPersistence(this.data));
	}

	public Text getTextById(Long idText) {
		return getTextControl().getTextById(idText);
	}
	
	public ElementText getElementText(Long idElement){
		String sql = "FROM ElementText WHERE id = '" + idElement + "'";
		data.put("sql", sql);
		Return ret = searchByHQL();
		return (ElementText) ret.getList().get(0);
	}
	
	public LevelText getLevelText(Long idLevel){
		String sql = "FROM LevelText WHERE id = '" + idLevel + "'";
		data.put("sql", sql);
		Return ret = searchByHQL();
		return (LevelText) ret.getList().get(0);
	}
	
	@SuppressWarnings("unchecked")
	public List<LevelText> getLevelsChildren(LevelText levelText){
		String sql = "FROM LevelText WHERE id_parentLevel = '" + levelText.getId() + "'";
		data.put("sql", sql);
		Return ret = searchByHQL();
		return (List<LevelText>) ret.getList();
	}
	
	@SuppressWarnings("unchecked")
	public List<ElementText> getElementsLevel(LevelText levelText){
		String sql = "FROM ElementText WHERE id_level = '" + levelText.getId() + "'";
		data.put("sql", sql);
		Return ret = searchByHQL();
		return (List<ElementText>) ret.getList();
	}
}
