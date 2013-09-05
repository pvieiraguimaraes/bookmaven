package br.ueg.tcc.bookway.control;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import br.com.vexillum.control.GenericControl;
import br.com.vexillum.util.Return;
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
		UserBookway user = (UserBookway) data.get("userLogged");
		Text text = (Text) data.get("selectedText");
		entity.setText(text);
		entity.setUserBookway(user);
		if (action.equalsIgnoreCase("add"))
			ret.concat(save());
		else if (action.equalsIgnoreCase("remove"))
			ret.concat(delete());
		return ret;
	}

	public boolean verifyUserHasText() {
		return false;
	}
}
