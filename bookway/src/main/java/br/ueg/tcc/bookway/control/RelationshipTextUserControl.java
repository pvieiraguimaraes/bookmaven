package br.ueg.tcc.bookway.control;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import br.com.vexillum.control.GenericControl;
import br.com.vexillum.util.HibernateUtils;
import br.com.vexillum.util.ReflectionUtils;
import br.com.vexillum.util.Return;
import br.com.vexillum.util.SpringFactory;
import br.ueg.tcc.bookway.model.RelationshipTextUser;
import br.ueg.tcc.bookway.model.Text;
import br.ueg.tcc.bookway.model.UserBookway;

/**
 * Controlador relacionado as a��es que envolvem os usu�rios e os textos, onde
 * cont�m as a��es envolvendo a lista de textos dos usu�rios
 * 
 * @author pedro
 * 
 */
@Service
@Scope("prototype")
public class RelationshipTextUserControl extends
		GenericControl<RelationshipTextUser> {

	public RelationshipTextUserControl() {
		super(RelationshipTextUser.class);
	}

	/**
	 * Realiza a adi��o ou a remo��o de um relacionamento entre o texo e o
	 * usu�rio, dependendo do par�metro passado
	 * 
	 * @param action
	 *            , a��o que deseja executar
	 * @return {@link Return}
	 */
	public Return addOrRemoveText(String action) {
		Return ret = new Return(true);
		Text text = HibernateUtils.materializeProxy((Text) data
				.get("selectedText"));
		entity.setText(text);
		entity.setUserBookway((UserBookway) data.get("userLogged"));
		if (action.equalsIgnoreCase("add"))
			ret.concat(doAction("saveEntity"));
		else if (action.equalsIgnoreCase("remove"))
			ret.concat(doAction("deleteRelationshipText"));
		return ret;
	}

	/**
	 * Realiza a dele��o do relacionamento para um texto e um usu�rio dados.
	 * 
	 * @return {@link Return}
	 */
	public Return deleteRelationshipText() {
		Long userId = entity.getUserBookway().getId();
		Long textId = entity.getText().getId();
		String sql = "DELETE FROM RelationshipTextUser WHERE userBookway ='"
				+ userId + "' AND text = '" + textId + "'";
		data.put("sql", sql);
		Return ret = executeByHQL();
		return ret;
	}

	/**
	 * M�todo que verifica se o usu�rio possui um determinado texto, ou seja se
	 * existe relacionamento entre o texto dado e o usu�rio.
	 * 
	 * @return verdadeiro se o usu�rio possui
	 */
	public boolean verifyUserHasText() {
		Return ret = new Return(true);
		Text text = (Text) data.get("selectedText");
		Long userId = ((UserBookway) data.get("userLogged")).getId();
		String hql = "FROM RelationshipTextUser WHERE userBookway ='" + userId
				+ "' AND text = '" + text.getId() + "'";
		data.put("sql", hql);
		ret.concat(searchByHQL());
		if (ret.getList() != null && !ret.getList().isEmpty())
			return true;
		return false;
	}

	/**
	 * Verifica se um dado texto pertence a algum usu�rio do sistema
	 * 
	 * @return verdadeiro se o texto pertencer a algum usu�rio
	 */
	public boolean verifyTextBelongsAnyUser() {
		Text text = (Text) data.get("selectedText");
		String hql = "FROM RelationshipTextUser WHERE text ='" + text.getId()
				+ "'";
		data.put("sql", hql);
		Return ret = searchByHQL();
		if (ret.getList() != null && !ret.getList().isEmpty())
			return true;
		return false;
	}

	/**
	 * M�todo que retorna a lista de textos adicionados pelo usu�rio
	 * 
	 * @return {@link Return}
	 */
	@SuppressWarnings("unchecked")
	public Return listTextAddOfUser() {
		Return ret = new Return(true);
		Long userId = ((UserBookway) data.get("userLogged")).getId();
		String hql = "FROM RelationshipTextUser WHERE userBookway ='" + userId
				+ "'";
		data.put("sql", hql);
		ret.concat(searchByHQL());
		ret.setList(converterRelationshipTextInText((List<RelationshipTextUser>) ret
				.getList()));
		return ret;
	}

	/**
	 * Extrai todos os textos de uma lista de relacionamento de usu�rios e texto
	 * 
	 * @param list
	 *            , lista de relacionamentos
	 * @return lista de {@link Text}
	 */
	private List<Text> converterRelationshipTextInText(
			List<RelationshipTextUser> list) {
		List<Text> listText = new ArrayList<>();
		Text text;
		for (RelationshipTextUser relationshipTextUser : list) {
			text = new Text();
			text = getTextControl().getTextById(
					relationshipTextUser.getText().getId());
			listText.add(text);
		}
		return listText;
	}

	/**
	 * Cria uma inst�ncia do controlador de texto
	 * 
	 * @return nova inst�ncia do controle {@link TextControl}
	 */
	public TextControl getTextControl() {
		return SpringFactory.getController("textControl", TextControl.class,
				ReflectionUtils.prepareDataForPersistence(this.data));
	}

}
