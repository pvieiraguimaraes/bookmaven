package br.ueg.tcc.bookway.control;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import br.com.vexillum.control.GenericControl;
import br.ueg.tcc.bookway.model.ItemNavigationStudy;

@Service
@Scope("prototype")
public class NavigationStudyControl extends GenericControl<ItemNavigationStudy>{

	public NavigationStudyControl() {
		super(ItemNavigationStudy.class);
	}


}
