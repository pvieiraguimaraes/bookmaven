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
 * Controle responsável por gerir as regras da navegação de estudo e as suas
 * regras de negócio.
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
	 * Utilizado para instânciar um controlador do caso de uso de Manter Texto
	 * 
	 * @return Retorna uma instância do controlador {@link TextControl}
	 */
	public TextControl getTextControl() {
		return SpringFactory.getController("textControl", TextControl.class,
				ReflectionUtils.prepareDataForPersistence(this.data));
	}

	/**
	 * Busca um texto através de um código passado
	 * 
	 * @param idText
	 *            , código do texto a ser buscado
	 * @return {@link Text}
	 */
	public Text getTextById(Long idText) {
		return getTextControl().getTextById(idText);
	}

	/**
	 * Busca um elemento de um texto de acordo com um id passado
	 * 
	 * @param idElement
	 *            , cógido de um elemento em questão
	 * @return {@link ElementText}
	 */
	public ElementText getElementText(Long idElement) {
		String sql = "FROM ElementText WHERE id = '" + idElement + "'";
		data.put("sql", sql);
		Return ret = searchByHQL();
		return (ElementText) ret.getList().get(0);
	}

	/**
	 * Realiza a busca de um nível de um texto de acordo com um código passado.
	 * 
	 * @param idLevel
	 *            , código do nível a ser buscado
	 * @return {@link LevelText}
	 */
	public LevelText getLevelText(Long idLevel) {
		String sql = "FROM LevelText WHERE id = '" + idLevel + "'";
		data.put("sql", sql);
		Return ret = searchByHQL();
		return (LevelText) ret.getList().get(0);
	}

	/**
	 * Realiza a busca de todos níveis "filhos" de um nível dado, ou seja, de
	 * todos os níveis que estão posicionados hierarquicamente abaixo
	 * 
	 * @param levelText
	 *            , nível que servirá como raiz para a busca
	 * @return lista de níveis {@link LevelText} contendo todos os "fihos" de um
	 *         dado nível.
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
	 * Retorna a lista de elementos de texto pertencentes a um dado nível sendo
	 * que um nível poderá conter vários elementos
	 * 
	 * @param levelText
	 *            , nível o qual se quer todos os elementos
	 * @return lista dos elementos de um dados nível {@link ElementText}
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
