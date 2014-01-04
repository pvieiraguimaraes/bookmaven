package br.ueg.tcc.bookway.view.composer;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.zkoss.zk.ui.Component;
import org.zkoss.zkex.zul.Colorbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;

import br.com.vexillum.util.Message;
import br.com.vexillum.util.ReflectionUtils;
import br.com.vexillum.util.Return;
import br.com.vexillum.util.SpringFactory;
import br.ueg.tcc.bookway.control.MarkingControl;
import br.ueg.tcc.bookway.model.MarkingOfUser;
import br.ueg.tcc.bookway.model.TagsOfMarking;
import br.ueg.tcc.bookway.model.UserBookway;
import br.ueg.tcc.bookway.model.enums.TypePrivacy;

@SuppressWarnings("serial")
@org.springframework.stereotype.Component
@Scope("prototype")
public class MarkingComposer extends BaseComposer<MarkingOfUser, MarkingControl> {

	private String tagValue;

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

	public List<TypePrivacy> getListTypesPrivacy() {
		return Arrays.asList(TypePrivacy.values());
	}
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		loadBinder();
	}

	@Override
	protected String getUpdatePage() {
		return null;
	}

	@Override
	protected String getDeactivationMessage() {
		return null;
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

	public void editMarking() {

	}

	public void deleteMarking() {

	}

	public void removeTagFromMarking() {
		Listbox listbox = (Listbox) getComponentById("fldlstTags");
		int index = 0;
		if (listbox != null) {
			Listitem selectedItem = listbox.getSelectedItem();
			if (selectedItem != null) {
				index = selectedItem.getIndex();
				TagsOfMarking tag = (TagsOfMarking) listbox.getModel().getElementAt(
						index);
				entity.getTagsOfMarkings().remove(tag);
			}
		}
		checkTagsOfMarking();
		loadBinder();
	}

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

	private void checkTagsOfMarking() {
		List<TagsOfMarking> tagsOfMarkings = entity.getTagsOfMarkings();
		setTagValue(null);
		if (!tagsOfMarkings.isEmpty())
			getComponentById("fldlstTags").setVisible(true);
		else
			getComponentById("fldlstTags").setVisible(false);
	}
	
	@Override
	public Return saveEntity() {
		Colorbox colorbox = (Colorbox) getComponentById("fldColor");
		entity.setColor(colorbox.getColor());
		entity.setUserBookway((UserBookway) getUserLogged());
		return super.saveEntity();
	}
}
