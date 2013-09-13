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

@Service
@Scope("prototype")
public class RelationshipTextUserControl extends
		GenericControl<RelationshipTextUser> {

	public RelationshipTextUserControl() {
		super(RelationshipTextUser.class);
	}

	public Return addOrRemoveText(String action) {
		Return ret = new Return(true);
		entity.setText((Text) data.get("selectedText"));
		entity.setUserBookway((UserBookway) data.get("userLogged"));
		if (action.equalsIgnoreCase("add"))
			ret.concat(doAction("saveEntity"));
		else if (action.equalsIgnoreCase("remove"))
			ret.concat(doAction("delete"));
		return ret;
	}

	/**Método que verifica se o usuário possui um determinado texto, ou seja
	 * se existe relacionamento entre o texto dado e o usuário.
	 * @return
	 */
	public boolean verifyUserHasText() {
		Return ret = new Return(true);
		Text text = (Text) data.get("selectedText");
		Long userId = ((UserBookway) data.get("userLogged")).getId();
		String hql = "FROM RelationshipTextUser WHERE userBookway ='" + userId
				+ "' AND text = '" + text.getId() + "'";
		data.put("sql", hql);
		ret.concat(searchByHQL());
		if(ret.getList() != null)
			return true;
		return false;
	}
	
	public boolean verifyTextBelongsAnyUser(){
		Text text = (Text) data.get("selectedText");
		String hql = "FROM RelationshipTextUser WHERE text ='" + text.getId() + "'";
		data.put("sql", hql);
		Return ret = searchByHQL();
		if(ret.getList() != null && !ret.getList().isEmpty())
			return true;
		return false;
	}

	/**Método que retorna a lista de textos adicionados pelo usuário
	 * @return
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

	private List<Text> converterRelationshipTextInText(
			List<RelationshipTextUser> list) {
		List<Text> listText = new ArrayList<>();
		Text text;
		for (RelationshipTextUser relationshipTextUser : list) {
			text = new Text();
			text = getTextControl().getTextById(relationshipTextUser.getText().getId());
			text = HibernateUtils.materializeProxy(text);
			listText.add(text);
		}
		return listText;
	}

	public TextControl getTextControl() {
		return SpringFactory.getController("textControl", TextControl.class,
				ReflectionUtils.prepareDataForPersistence(this.data));
	}
	
}
