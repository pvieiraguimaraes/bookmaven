package br.ueg.tcc.bookway.view.components;

import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.event.PagingEvent;
import org.zkoss.zul.event.ZulEvents;

/**
 * Classe que herda as funcionalidade da classe tabpanel do framework de visão
 * ZK, e é utilizada para realizar o mapeamento dos componentes na visão do
 * ambiente de estudo
 * 
 * @author pedro
 * 
 */
@SuppressWarnings("serial")
public class BookwayTabpanel extends Tabpanel {

	@Wire
	private Tabpanel tabpanel = new Tabpanel();

	@Wire
	private Paging paging;

	public BookwayTabpanel() {
		super();

		tabpanel.addEventListener(ZulEvents.ON_PAGING,
				new EventListener<PagingEvent>() {
					@Override
					public void onEvent(PagingEvent event) throws Exception {
						try {

						} catch (Throwable e) {
							throw new Exception(e);
						}
					}
				});
	}
}
