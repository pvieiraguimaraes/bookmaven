package br.ueg.tcc.bookway.control;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import br.com.vexillum.control.GenericControl;
import br.com.vexillum.util.Return;
import br.ueg.tcc.bookway.model.ElementText;
import br.ueg.tcc.bookway.model.ElementsItensStudy;
import br.ueg.tcc.bookway.model.Study;

@Service
@Scope("prototype")
public class ElementsItensStudyControl extends GenericControl<ElementsItensStudy> {

	public ElementsItensStudyControl() {
		super(ElementsItensStudy.class);
	}
	
	public Return searchExistingItensStudies(){
		ElementText elementText = (ElementText) data.get("checkElementText");
		Study study = (Study) data.get("checkStudy"); 
		
		String sql = "FROM ElementsItensStudy WHERE id_elementtext = '" + elementText.getId() + "' AND id_study = '" + study.getId() + "'";
		data.put("sql", sql);
				
		return searchByHQL();
	}

}
