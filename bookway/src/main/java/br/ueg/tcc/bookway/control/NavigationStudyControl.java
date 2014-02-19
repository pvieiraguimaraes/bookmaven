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

/**
 * Controle respons�vel por gerir as regras da navega��o de estudo e as suas
 * regras de neg�cio.
 * 
 * @author pedro
 * 
 */
@Service
@Scope("prototype")
public class NavigationStudyControl extends GenericControl<ItemNavigationStudy> {

	protected Properties configuration;

	public NavigationStudyControl() {
		super(ItemNavigationStudy.class);
		configuration = SpringFactory.getInstance().getBean("configProperties",
				Properties.class);
	}

	/**
	 * Utilizado para inst�nciar um controlador do caso de uso de Manter Texto
	 * 
	 * @return Retorna uma inst�ncia do controlador {@link TextControl}
	 */
	public TextControl getTextControl() {
		return SpringFactory.getController("textControl", TextControl.class,
				ReflectionUtils.prepareDataForPersistence(this.data));
	}

	/**
	 * Busca um texto atrav�s de um c�digo passado
	 * 
	 * @param idText
	 *            , c�digo do texto a ser buscado
	 * @return {@link Text}
	 */
	public Text getTextById(Long idText) {
		return getTextControl().getTextById(idText);
	}

	/**
	 * Busca um elemento de um texto de acordo com um id passado
	 * 
	 * @param idElement
	 *            , c�gido de um elemento em quest�o
	 * @return {@link ElementText}
	 */
	public ElementText getElementText(Long idElement) {
		String sql = "FROM ElementText WHERE id = '" + idElement + "'";
		data.put("sql", sql);
		Return ret = searchByHQL();
		return (ElementText) ret.getList().get(0);
	}

	/**
	 * Realiza a busca de um n�vel de um texto de acordo com um c�digo passado.
	 * 
	 * @param idLevel
	 *            , c�digo do n�vel a ser buscado
	 * @return {@link LevelText}
	 */
	public LevelText getLevelText(Long idLevel) {
		String sql = "FROM LevelText WHERE id = '" + idLevel + "'";
		data.put("sql", sql);
		Return ret = searchByHQL();
		return (LevelText) ret.getList().get(0);
	}

	/**
	 * Realiza a busca de todos n�veis "filhos" de um n�vel dado, ou seja, de
	 * todos os n�veis que est�o posicionados hierarquicamente abaixo
	 * 
	 * @param levelText
	 *            , n�vel que servir� como raiz para a busca
	 * @return lista de n�veis {@link LevelText} contendo todos os "fihos" de um
	 *         dado n�vel.
	 */
	@SuppressWarnings("unchecked")
	public List<LevelText> getLevelsChildren(LevelText levelText) {
		String sql = "FROM LevelText WHERE id_parentLevel = '"
				+ levelText.getId() + "'";
		data.put("sql", sql);
		Return ret = searchByHQL();
		return (List<LevelText>) ret.getList();
	}

	/**
	 * Retorna a lista de elementos de texto pertencentes a um dado n�vel sendo
	 * que um n�vel poder� conter v�rios elementos
	 * 
	 * @param levelText
	 *            , n�vel o qual se quer todos os elementos
	 * @return lista dos elementos de um dados n�vel {@link ElementText}
	 */
	@SuppressWarnings("unchecked")
	public List<ElementText> getElementsLevel(LevelText levelText) {
		String sql = "FROM ElementText WHERE id_level = '" + levelText.getId()
				+ "'";
		data.put("sql", sql);
		Return ret = searchByHQL();
		return (List<ElementText>) ret.getList();
	}
}
