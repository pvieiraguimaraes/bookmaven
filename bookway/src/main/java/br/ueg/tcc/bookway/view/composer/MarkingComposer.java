package br.ueg.tcc.bookway.view.composer;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.zkoss.zk.ui.Component;
import org.zkoss.zkex.zul.Colorbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Window;

import br.com.vexillum.util.Message;
import br.com.vexillum.util.ReflectionUtils;
import br.com.vexillum.util.Return;
import br.com.vexillum.util.SpringFactory;
import br.ueg.tcc.bookway.control.MarkingControl;
import br.ueg.tcc.bookway.control.MarkingUsedControl;
import br.ueg.tcc.bookway.model.ElementText;
import br.ueg.tcc.bookway.model.ElementsItensStudy;
import br.ueg.tcc.bookway.model.MarkingOfUser;
import br.ueg.tcc.bookway.model.MarkingUsed;
import br.ueg.tcc.bookway.model.TagsOfMarking;
import br.ueg.tcc.bookway.model.UserBookway;

/**
 * Classe de controle das funcionalidades relacionadas ao caso de uso Manter
 * Marcações
 * 
 * @author pedro
 * 
 */
@SuppressWarnings("serial")
@org.springframework.stereotype.Component
@Scope("prototype")
public class MarkingComposer extends
		BaseComposer<MarkingOfUser, MarkingControl> {

	/**
	 * Valor da tag
	 */
	private String tagValue;

	/**
	 * Tag selecionada
	 */
	private TagsOfMarking tagSelected;

	public TagsOfMarking getTagSelected() {
		return tagSelected;
	}

	public void setTagSelected(TagsOfMarking tagSelected) {
		this.tagSelected = tagSelected;
	}

	public String getTagValue() {
		return tagValue;
	}

	public void setTagValue(String tagValue) {
		this.tagValue = tagValue;
	}

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		checkTagsOfMarking();
		checkListEntity();
		loadMarkingsOfUser();
		loadBinder();
	}

	/**
	 * Busca as marcações do usuário logado na sessão
	 */
	@SuppressWarnings("unchecked")
	private void loadMarkingsOfUser() {
		List<MarkingOfUser> list = (List<MarkingOfUser>) session
				.getAttribute("markingOfUsers");
		setListEntity(list);
	}

	@Override
	protected String getUpdatePage() {
		return "/pages/marking/modalMarking.zul";
	}

	@Override
	protected String getDeactivationMessage() {
		return "Deseja realmente excluir esta marcação e suas tags?";
	}

	@Override
	public MarkingControl getControl() {
		return SpringFactory.getController("markingControl",
				MarkingControl.class,
				ReflectionUtils.prepareDataForPersistence(this));
	}

	@Override
	public MarkingOfUser getEntityObject() {
		return new MarkingOfUser();
	}

	/**
	 * Faz chamada para um método de busca de marcações do controlador
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Return searchMarking() {
		Return ret = getControl().doAction("searchMarking");
		if (ret.isValid() && !ret.getList().isEmpty()) {
			setListEntity((List<MarkingOfUser>) ret.getList());
			getComponentById("resultList").setVisible(true);
			loadBinder();
		}
		return ret;
	}

	/**
	 * Chama o formulário para edição de uma marcação selecionada em lista
	 */
	public void editMarking() {
		getSelectedEntityFromListbox();
		callModalWindow("/pages/marking/modalMarking.zul");
		loadBinder();
	}

	/**
	 * Faz chamada para janela de confirmação de exlusão de uma marcação
	 */
	public void deleteMarking() {
		getSelectedEntityFromListbox();
		entity = getSelectedEntity();
		showActionConfirmation(getDeactivationMessage(), "deleteThisMarking");
		loadBinder();
	}

	/**
	 * Realiza chamda a camada de controle para exclusão de uma marcação
	 * selecionada
	 * 
	 * @return {@link Return}
	 */
	public Return deleteThisMarking() {
		MarkingOfUser markingOfUser = entity;
		Return ret = getControl().doAction("delete");
		if (ret.isValid()) {
			listEntity.remove(markingOfUser);
			resetForm();
		}
		checkListEntity();
		loadBinder();
		return ret;
	}

	@Override
	public void resetForm() {
		tagValue = null;
		selectedEntity = null;
		getParentComposer().setEntity(getEntityObject());
		getParentComposer().setSelectedEntity(null);
		super.resetForm();
	}

	/**
	 * Seta como a entidade do controle a entidade selecionada na camada de
	 * visão
	 */
	private void getSelectedEntityFromListbox() {
		Listbox listbox = (Listbox) getComponentById("resultList");
		int index = 0;
		if (listbox != null) {
			Listitem selectedItem = listbox.getSelectedItem();
			if (selectedItem != null) {
				index = selectedItem.getIndex();
				MarkingOfUser mark = (MarkingOfUser) listbox.getModel()
						.getElementAt(index);
				selectedEntity = mark;
			}
		}
	}

	/**
	 * Remove uma tag selecionada da marcação
	 */
	public void removeTagFromMarking() {
		Listbox listbox = (Listbox) getComponentById("fldlstTags");
		int index = 0;
		if (listbox != null) {
			Listitem selectedItem = listbox.getSelectedItem();
			if (selectedItem != null) {
				index = selectedItem.getIndex();
				TagsOfMarking tag = (TagsOfMarking) listbox.getModel()
						.getElementAt(index);
				entity.getTagsOfMarkings().remove(tag);
			}
		}
		checkTagsOfMarking();
		loadBinder();
	}

	/**
	 * Adiciona uma tag a lista de tags da marcação de acordo com os dados
	 * preenchidos do usuário
	 * 
	 * @return {@link Return}
	 */
	public Return addTagInList() {
		Return ret = new Return(true);
		TagsOfMarking tag;
		if (tagValue != null && !tagValue.equalsIgnoreCase("")) {
			List<TagsOfMarking> tags = entity.getTagsOfMarkings();
			if (!tags.isEmpty()) {
				boolean flag = false;
				for (TagsOfMarking tagsOfMarking : tags) {
					if (tagsOfMarking.getName().equalsIgnoreCase(tagValue)) {
						flag = true;
					}
				}
				if (!flag) {
					tag = new TagsOfMarking();
					tag.setName(tagValue);
					tag.setMarking(entity);
					tag.setUserBookway((UserBookway) getUserLogged());
					tags.add(tag);
					checkTagsOfMarking();
					return ret;
				} else {
					ret.setValid(false);
					ret.addMessage(new Message("tag", messages
							.getKey("tag_exists_in_marking_false")));
				}

			} else {
				tag = new TagsOfMarking();
				tag.setName(tagValue);
				tags.add(tag);
				tag.setMarking(entity);
				checkTagsOfMarking();
				return ret;
			}
		} else {
			ret.setValid(false);
			ret.addMessage(new Message("tag", messages.getKey("notnull_false")));
		}
		return ret;
	}

	/**
	 * Verifica se existem tags para marcação dada, caso exista exibe todas em
	 * uma lista
	 */
	private void checkTagsOfMarking() {
		List<TagsOfMarking> tagsOfMarkings = entity.getTagsOfMarkings();
		setTagValue(null);
		Component comp = getComponentById("fldlstTags");
		if (comp != null) {
			if (!tagsOfMarkings.isEmpty())
				comp.setVisible(true);
			else
				comp.setVisible(false);
		}
	}

	/**
	 * Verifica se a lista resultada da busca é vazia, caso não seja, exibe a
	 * lista de acordo com os resultaddos da busca
	 */
	private void checkListEntity() {
		Component comp = getComponentById("resultList");
		if (comp != null && listEntity != null) {
			if (listEntity.isEmpty())
				comp.setVisible(false);
			else
				comp.setVisible(true);
		}
	}

	@Override
	public Return saveEntity() {
		Colorbox colorbox = (Colorbox) getComponentById("fldColor");
		entity.setColor(colorbox.getColor());
		entity.setUserBookway((UserBookway) getUserLogged());
		for (TagsOfMarking tag : entity.getTagsOfMarkings()) {
			if (tag.getUserBookway() == null)
				tag.setUserBookway((UserBookway) getUserLogged());
		}
		Return ret = super.saveEntity();
		if (ret.isValid())
			((Window) getComponentById(component, "modalWindow")).detach();
		loadBinder();
		resetForm();
		treatReturn(ret);
		return ret;
	}

	/**
	 * Atribui aos elementos selecionados uma marcação escolhida alterando o
	 * estilos dos elementos da tela
	 * 
	 * @return {@link Return}
	 */
	public Return putMarkingInStudy() {
		Return retAux, ret = new Return(true);
		retAux = createMarkingInItens(itensSelected);
		if (retAux.isValid()) {
			changeStyleMarkingInItens(itemStudiesSelected,
					selectedEntity.getColor());
			((Window) getComponentById("modalWindow")).detach();
		} else
			ret.setValid(false);

		return ret;
	}

	/**
	 * Realiza as chamadas a camada de controle para criação de uma marcação em
	 * um elemento de texto para um dado estudo
	 * 
	 * @param itensSelected
	 *            , lista de elementos do texto selecionados
	 * @return {@link Return}
	 */
	private Return createMarkingInItens(List<ElementText> itensSelected) {
		Return ret = new Return(true);
		ElementsItensStudy elementsItensStudy;
		MarkingUsed markingUsed;

		if (selectedEntity != null) {
			markingUsed = new MarkingUsed();
			markingUsed.setTypePrivacy(getTypePrivacy());
			markingUsed.setMarkingOfUser(selectedEntity);
			markingUsed.setStudy(study);
			markingUsed.setDateItem(new Date());

			ret.concat(saveMarkingUsed(markingUsed));

			if (ret.isValid()) {
				markingUsed = getMarkingUsed(markingUsed);

				for (ElementText elementText : itensSelected) {
					elementsItensStudy = new ElementsItensStudy();
					elementsItensStudy.setElementText(elementText);
					elementsItensStudy.setItemOfStudy(markingUsed);
					elementsItensStudy.setStudy(study);
					elementsItensStudies.add(elementsItensStudy);
				}

				ret.concat(saveElementsItensStudy());

				if (ret.isValid()) {
					createIconsStudy(getItemStudiesSelected());
				}

			}
		} else
			ret.setValid(false);

		return ret;
	}

	/**
	 * Acessa o controlador para buscar uma marcação de acordo com os dados
	 * informados
	 * 
	 * @param markingUsed
	 *            , objeto de marcação utilizado
	 * @return objeto {@link MarkingUsed}
	 */
	private MarkingUsed getMarkingUsed(MarkingUsed markingUsed) {
		Return ret = new Return(true);
		newMap.put("markingUsed", markingUsed);
		ret.concat(getMarkingUsedControl(newMap).searchThisMarkingUsed());
		if (ret.isValid())
			return (MarkingUsed) ret.getList().get(0);
		return null;
	}

	/**
	 * Retorna uma nova instância do controlador
	 * 
	 * @param newMap
	 *            , objetos para manipulação no controlador
	 * @return nova instância do controlador {@link MarkingUsedControl}
	 */
	public MarkingUsedControl getMarkingUsedControl(
			HashMap<String, Object> newMap) {
		return SpringFactory.getController("markingUsedControl",
				MarkingUsedControl.class, newMap);
	}

	/**
	 * Faz chamada ao controlador para salvar o uso de uma marcação em um estudo
	 * 
	 * @param marking
	 *            , marcação que será salva
	 * @return {@link Return}
	 */
	private Return saveMarkingUsed(MarkingUsed marking) {
		Return ret = new Return(true);

		newMap.put("entity", marking);
		ret.concat(getMarkingUsedControl(newMap).doAction("save"));

		return ret;
	}

	/**
	 * Seta a cor no campo de cor do formulário de seleção de marcação para ser
	 * usada, para que o usuário possa identificar qual marcação deseja utilizar
	 * de acordo com a cor
	 */
	public void changeColorbox() {
		Colorbox colorbox = (Colorbox) getComponentById("clrbx");
		if (colorbox != null)
			colorbox.setValue(getSelectedEntity().getColor());
		loadBinder();
	}
}
