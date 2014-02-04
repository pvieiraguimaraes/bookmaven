package br.ueg.tcc.bookway.view.composer;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Button;

import br.com.vexillum.util.HibernateUtils;
import br.com.vexillum.util.ReflectionUtils;
import br.com.vexillum.util.Return;
import br.com.vexillum.util.SpringFactory;
import br.ueg.tcc.bookway.control.MarkingControl;
import br.ueg.tcc.bookway.control.NavigationStudyControl;
import br.ueg.tcc.bookway.model.ElementText;
import br.ueg.tcc.bookway.model.ItemNavigationStudy;
import br.ueg.tcc.bookway.model.LevelText;
import br.ueg.tcc.bookway.model.MarkingOfUser;
import br.ueg.tcc.bookway.model.Study;
import br.ueg.tcc.bookway.model.UserBookway;

/**
 * @author Pedro
 * 
 */
@SuppressWarnings("serial")
@org.springframework.stereotype.Component
@Scope("prototype")
public class NavigationStudyComposer extends
		BaseNavigationStudyComposer<ItemNavigationStudy, NavigationStudyControl> {

	private List<MarkingOfUser> markingOfUsers;
	
	public List<MarkingOfUser> getMarkingOfUsers() {
		return markingOfUsers;
	}

	public void setMarkingOfUsers(List<MarkingOfUser> markingOfUsers) {
		this.markingOfUsers = markingOfUsers;
	}

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		study = (Study) session.getAttribute("study");
		continueStudy = (Boolean) session.getAttribute("continueStudy");
		isTextReferenceMode = (Boolean) session.getAttribute("isTextReferenceMode");
		
		if(isTextReferenceMode == null)
			isTextReferenceMode = false;
		
		if (study != null)
			createAmbientStudy(HibernateUtils.materializeProxy(getControl()
					.getLevelText(study.getText().getRootLevelText().getId())));
		
		if (!isTextReferenceMode) {
			loadMarkingsOfUser();
			checkButtonMarking();
		}
		loadBinder();
	}

	@SuppressWarnings("unchecked")
	private void loadMarkingsOfUser() {
		Return ret = getMarkingControl().getMarkingOfUser(
				(UserBookway) getUserLogged());
		if (ret.isValid()) {
			session.setAttribute("markingOfUsers",
					(List<MarkingOfUser>) ret.getList());
			setMarkingOfUsers((List<MarkingOfUser>) ret.getList());
		}
	}

	private void checkButtonMarking() {
		Button buttonMarking = (Button) getComponentById("fldMarking");
		if (!getMarkingOfUsers().isEmpty()) {
			if (buttonMarking != null)
				buttonMarking.setDisabled(false);
		} else if (buttonMarking != null)
			buttonMarking.setDisabled(true);
	}

	public MarkingControl getMarkingControl() {
		return SpringFactory.getController("markingControl",
				MarkingControl.class, null);
	}

	

	@Override
	public NavigationStudyControl getControl() {
		return SpringFactory.getController("navigationStudyControl",
				NavigationStudyControl.class,
				ReflectionUtils.prepareDataForPersistence(this));
	}

	@Override
	public ItemNavigationStudy getEntityObject() {
		return new ItemNavigationStudy();
	}

	

	private LevelText getPageElement(LevelText level) {
		 HibernateUtils.materializeProxy(level.getElements());
		ElementText page;
		
		return null;
	}

	@Override
	protected String getUpdatePage() {
		return null;
	}

	@Override
	protected String getDeactivationMessage() {
		return null;
	}
}
