package br.ueg.tcc.bookway.view.composer;

import org.springframework.context.annotation.Scope;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Label;
import org.zkoss.zul.Window;

import br.com.vexillum.util.ReflectionUtils;
import br.com.vexillum.util.SpringFactory;
import br.com.vexillum.view.CRUDComposer;
import br.ueg.tcc.bookway.control.NavigationStudyControl;
import br.ueg.tcc.bookway.model.ItemNavigationStudy;
import br.ueg.tcc.bookway.view.events.NavigationStudyListener;

@SuppressWarnings("serial")
@org.springframework.stereotype.Component
@Scope("prototype")
public class NavigationStudyComposer extends
		CRUDComposer<ItemNavigationStudy, NavigationStudyControl> {

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		loadBinder();
	}

	private void initPanelStudy() {
		getComponentById("panelStudy").addEventListener(
				"onScroll",
				new NavigationStudyListener());

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

	public void addItemStudy() {
		System.out.println("Entrou no metodo");
		Window win = new Window();
		win.appendChild(new Label("Teste Agora"));
		getComponentById("panelStudy").appendChild(win);
		System.out.println("Saiu do metodo");
	}
	
	public void teste(){
		System.out.println("funciona cara!");
	}

}
