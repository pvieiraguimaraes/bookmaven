package br.ueg.tcc.bookway.control;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import br.com.vexillum.control.GenericControl;
import br.com.vexillum.util.Return;
import br.ueg.tcc.bookway.model.MarkingOfUser;
import br.ueg.tcc.bookway.model.TagsOfMarking;

@Service
@Scope("prototype")
public class MarkingControl extends GenericControl<MarkingOfUser> {

	public MarkingControl() {
		super(MarkingOfUser.class);
	}

	@SuppressWarnings("unchecked")
	public Return searchMarking() {
		String name = entity.getName();
		String tagValue = (String) data.get("tagValue");

		List<MarkingOfUser> markings = new ArrayList<MarkingOfUser>();
		List<TagsOfMarking> tags = new ArrayList<TagsOfMarking>();
		
		String sql = "FROM Marking WHERE ";

		if (name != null) {
			sql += "name LIKE '%" + name + "%'";
			data.put("sql", sql);
			markings.addAll((List<MarkingOfUser>) searchByHQL().getList());
		}
		
		if (tagValue != null){
			sql = "FROM TagsOfMarking WHERE name LIKE '%" + tagValue + "%'";
			data.put("sql", sql);
			tags.addAll((List<TagsOfMarking>)searchByHQL().getList());
		}
		
		if(tags != null && !tags.isEmpty()){
			for (TagsOfMarking tagsOfMarking : tags) {
				MarkingOfUser mark = tagsOfMarking.getMarking();
				if(!markings.contains(mark))
					markings.add(mark);
			}
		}

		return new Return(true, markings);
	}

}
