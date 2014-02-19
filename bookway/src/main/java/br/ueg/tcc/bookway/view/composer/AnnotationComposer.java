package br.ueg.tcc.bookway.view.composer;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;

import br.com.vexillum.util.HibernateUtils;
import br.com.vexillum.util.ReflectionUtils;
import br.com.vexillum.util.Return;
import br.com.vexillum.util.SpringFactory;
import br.ueg.tcc.bookway.control.AnnotationControl;
import br.ueg.tcc.bookway.control.StudyControl;
import br.ueg.tcc.bookway.model.Annotation;
import br.ueg.tcc.bookway.model.ElementText;
import br.ueg.tcc.bookway.model.ElementsItensStudy;
import br.ueg.tcc.bookway.model.Study;
import br.ueg.tcc.bookway.model.UserBookway;
import br.ueg.tcc.bookway.model.enums.TypePrivacy;

/**
 * Classe que manipula os objetos na camada de vis�o do caso de uso Manter
 * Anota��es
 * 
 * @author pedro
 * 
 */
@SuppressWarnings("serial")
@org.springframework.stereotype.Component
@Scope("prototype")
public class AnnotationComposer extends
		InitComposer<Annotation, AnnotationControl> {

	private String title;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		setMyStudies(getStudyControl().getMyStudies(
				(UserBookway) getUserLogged()));
		String anote = (String) session.getAttribute("editThisNote");
		if (anote != null) {
			ElementsItensStudy ele = getElementsItensStudyControl(null)
					.searchExistingItensById(anote);
			if (ele != null)
				selectedEntity = (Annotation) HibernateUtils
						.materializeProxy(ele.getItemOfStudy());
		} else
			getSelectedEntityFromListbox();
		loadBinder();
	}

	@Override
	public AnnotationControl getControl() {
		return SpringFactory.getController("annotationControl",
				AnnotationControl.class,
				ReflectionUtils.prepareDataForPersistence(this));
	}

	@Override
	public Annotation getEntityObject() {
		return new Annotation();
	}

	public List<TypePrivacy> getListTypesPrivacy() {
		return Arrays.asList(TypePrivacy.values());
	}

	@Override
	public Return saveEntity() {
		Return ret = new Return(true), retAux = new Return(true);
		Annotation annotation;

		if (!update && entity.getStudy() == null) {
			ElementsItensStudy elementsItensStudy;

			entity.setStudy((Study) session.getAttribute("study"));
			entity.setDateItem(new Date());
			annotation = entity;

			ret.concat(super.saveEntity());

			if (ret.isValid() && !update) {
				entity = getAnnotation(annotation);

				for (ElementText elementText : itensSelected) {
					elementsItensStudy = new ElementsItensStudy();
					elementsItensStudy.setElementText(elementText);
					elementsItensStudy.setItemOfStudy(entity);
					elementsItensStudy.setStudy(study);
					elementsItensStudies.add(elementsItensStudy);
				}

				retAux.concat(saveElementsItensStudy());

				if (retAux.isValid()) {
					createIconsStudy(getItemStudiesSelected());
				}

				getComponentById("winAnnotation").detach();
				((BaseComposer<Annotation, AnnotationControl>) getParentComposer())
						.resetStyleItens();
				getParentComposer().loadBinder();
				loadBinder();
			}
		} else if (update) {
			ret.concat(getControl().doAction("update"));
			getComponentById("modalWindow").detach();
			loadBinder();
		} else
			ret.setValid(false);
		return ret;
	}

	/**
	 * Realiza a chamada do m�todo que busca uma anota��o para o controlador do
	 * caso de uso
	 * 
	 * @param annot
	 *            , cont�m dados do objeto que se deseja buscar
	 * @return {@link Annotation}
	 */
	private Annotation getAnnotation(Annotation annot) {
		Return ret = new Return(true);
		ret.concat(getControl().searchThisAnnotation(annot));
		if (ret.isValid())
			return (Annotation) ret.getList().get(0);
		return null;
	}

	/**
	 * Retorna uma nova instancia do controlador do ambiente de estudo
	 * 
	 * @return {@link StudyControl}
	 */
	private StudyControl getStudyControl() {
		StudyControl control = SpringFactory.getController("studyControl",
				StudyControl.class,
				ReflectionUtils.prepareDataForPersistence(this));
		return control;
	}

	/**
	 * Faz a chamada ao controlador da busca pelas anota��oes de acordo com os
	 * par�metros inseridos na tela
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Return searchAnnotation() {
		Return ret = getControl().doAction("searchAnnotation");
		if (ret.isValid() && !ret.getList().isEmpty()) {
			setListEntity((List<Annotation>) ret.getList());
			getComponentById("resultList").setVisible(true);
			loadBinder();
		}
		return ret;
	}

	/**
	 * Faz uma chamada de um objeto selecionado na lista para edi��o no
	 * formul�rio de altera��o
	 */
	public void editAnnotation() {
		getSelectedEntityFromListbox();
		callModalWindow("/pages/annotation/modalAnnotation.zul");
		loadBinder();
	}

	/**
	 * Faz uma chamada de um objeto selecionado na lista para dele��o, com uma
	 * chamada para confirma��o da a��o em quest�o
	 */
	public void deleteAnnotation() {
		getSelectedEntityFromListbox();
		entity = getSelectedEntity();
		showActionConfirmation(getDeactivationMessage(), "deleteThisAnnotation");
		loadBinder();
	}

	/**
	 * Faz a chamada ao m�todo do controlador que realiza a exclus�o de uma
	 * anota��o espec�fica
	 * 
	 * @return {@link Return}
	 */
	public Return deleteThisAnnotation() {
		Annotation thisAnot = entity;
		Return ret = getControl().doAction("delete");
		if (ret.isValid())
			listEntity.remove(thisAnot);
		loadBinder();
		return ret;
	}

	/**
	 * Atribui um objeto da lista como a entidade selecionada para realizar as
	 * intera��es
	 */
	private void getSelectedEntityFromListbox() {
		Listbox listbox = (Listbox) getComponentById("resultList");
		int index = 0;
		if (listbox != null) {
			Listitem selectedItem = listbox.getSelectedItem();
			if (selectedItem != null) {
				index = selectedItem.getIndex();
				Annotation ant = (Annotation) listbox.getModel().getElementAt(
						index);
				selectedEntity = ant;
			}
		}
	}

	@Override
	protected String getUpdatePage() {
		return "/pages/annotation/modalAnnotation.zul";
	}

	@Override
	protected String getDeactivationMessage() {
		return "Deseja realmente excluir esta anota��o?";
	}
}
