package br.ueg.tcc.bookway.view.events;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Window;

public class NavigationStudyListener implements EventListener<Event> {

	@Override
	public void onEvent(Event event) throws Exception {
		Component parent = createItemStudy();
		Executions.createComponents("/pages/user/study.zul", parent, null);
	}

	private Component createItemStudy() {
		Window window = new Window();
		window.setTitle("Teste");
		window.setWidth("200px");
		window.setHeight("50px");
		return window;
	}

}
