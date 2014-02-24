package br.ueg.tcc.bookway.view.composer;

import org.springframework.context.annotation.Scope;

import br.com.vexillum.util.ReflectionUtils;
import br.com.vexillum.util.Return;
import br.com.vexillum.util.SpringFactory;
import br.ueg.tcc.bookway.control.RelationshipTextUserControl;
import br.ueg.tcc.bookway.model.RelationshipTextUser;
import br.ueg.tcc.bookway.model.UserBookway;

/**
 * Controlador da visão que contém as funcionalidades relacionadas com os
 * relacionamentos dos textos.
 * 
 * @author pedro
 * 
 */
@SuppressWarnings("serial")
@org.springframework.stereotype.Component
@Scope("prototype")
public class RelationshipTextComposer extends
		InitComposer<RelationshipTextUser, RelationshipTextUserControl> {

	@Override
	public RelationshipTextUserControl getControl() {
		return SpringFactory.getController("relationshipTextUserControl",
				RelationshipTextUserControl.class,
				ReflectionUtils.prepareDataForPersistence(this));
	}

	@Override
	public RelationshipTextUser getEntityObject() {
		return new RelationshipTextUser();
	}

	/**
	 * Cria o relacionamento entre o texto e o usuário, dessa maneira o usuário
	 * passa a ter acesso ao texto e poderá estuda-lo
	 * 
	 * @param id
	 *            , do texto
	 */
	public void addText(String id) {
		Return ret = new Return(true);
		entity.setText(getControl().getTextControl().getTextById(
				Long.parseLong(id)));
		entity.setUserBookway((UserBookway) getUserLogged());
		ret.concat(saveEntity());
		treatReturn(ret);
	}

	/**
	 * Remove o texto do relacionamento com usuário e o usuário deixa de ter
	 * permissão de acesso ao texto.
	 * 
	 * @param id
	 *            , do texto
	 */
	public void removeText(String id) {
		Return ret = new Return(true);
		setSelectedText(getControl().getTextControl().getTextById(
				Long.parseLong(id)));
		ret.concat(getControl().addOrRemoveText("remove"));
		treatReturn(ret);
	}

}
