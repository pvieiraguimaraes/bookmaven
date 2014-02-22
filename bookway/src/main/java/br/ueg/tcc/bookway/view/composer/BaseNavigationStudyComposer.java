package br.ueg.tcc.bookway.view.composer;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Tabpanels;
import org.zkoss.zul.Tabs;

import br.com.vexillum.control.GenericControl;
import br.com.vexillum.control.manager.ConfigurationManager;
import br.com.vexillum.model.Configuration;
import br.com.vexillum.model.ICommonEntity;
import br.com.vexillum.util.HibernateUtils;
import br.com.vexillum.util.Return;
import br.ueg.tcc.bookway.model.ElementText;
import br.ueg.tcc.bookway.model.ElementsItensStudy;
import br.ueg.tcc.bookway.model.ItensOfStudy;
import br.ueg.tcc.bookway.model.LevelText;
import br.ueg.tcc.bookway.model.MarkingOfUser;
import br.ueg.tcc.bookway.model.MarkingUsed;
import br.ueg.tcc.bookway.model.Study;
import br.ueg.tcc.bookway.view.macros.ItemStudy;

/**
 * Classe que contém as funcionalidades genericas para a criação do ambiente de
 * estudo
 * 
 * @author pedro
 * 
 * @param <E>
 * @param <G>
 */
@SuppressWarnings("serial")
public abstract class BaseNavigationStudyComposer<E extends ICommonEntity, G extends GenericControl<E>>
		extends BaseComposer<E, G> {

	private NavigationStudyComposer navigationStudyComposer;

	public NavigationStudyComposer getNavigationStudyComposer() {
		return navigationStudyComposer;
	}

	public void setNavigationStudyComposer(
			NavigationStudyComposer navigationStudyComposer) {
		this.navigationStudyComposer = navigationStudyComposer;
	}

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		if (parentComposer instanceof NavigationStudyComposer)
			setNavigationStudyComposer((NavigationStudyComposer) parentComposer);
		loadBinder();
	}

	/**
	 * Adiciona ou remove um item de estudo selecionado
	 * 
	 * @param itemStudy
	 *            , a ser adicionado ou removido
	 */
	public void addItemStudySelected(ItemStudy itemStudy) {
		if (getItemStudiesSelected().contains(itemStudy))
			getItemStudiesSelected().remove(itemStudy);
		else
			getItemStudiesSelected().add(itemStudy);
	}

	/**
	 * Adiciona ou remove um item de estudo selecionado para realizar referência
	 * 
	 * @param itemStudy
	 *            , item em questão
	 */
	public void addItemStudySelectedForReference(ItemStudy itemStudy) {
		if (getItemStudiesSelectedForReference().contains(itemStudy))
			getItemStudiesSelectedForReference().remove(itemStudy);
		else
			getItemStudiesSelectedForReference().add(itemStudy);
	}

	/**
	 * Adiciona ou remove um elemento a lista de elementos de texto selecionados
	 * durante o estudo de um texto
	 * 
	 * @param itemStudy
	 *            , o item que deverá ser removido ou adicionado
	 * @param elementText
	 *            , elemento que deverá ser removido ou adicionado
	 */
	public void addElementTextSelected(ItemStudy itemStudy,
			ElementText elementText) {
		if (getItensSelected().contains(elementText))
			getItensSelected().remove(elementText);
		else
			getItensSelected().add(elementText);
	}

	/**
	 * Remove ou adiciona um Elemento do texto a lista de elementos do textos
	 * selecionados para realizar a referência
	 * 
	 * @param itemStudy
	 *            , item que deverá ser adicionado ou removido
	 * @param elementTextm
	 *            , elemento que deverá ser adicionado ou removidoo
	 */
	public void addElementTextSelectedForReference(ItemStudy itemStudy,
			ElementText elementText) {
		if (getItensSelectedForReference().contains(elementText))
			getItensSelectedForReference().remove(elementText);
		else
			getItensSelectedForReference().add(elementText);
	}

	/**
	 * Realiza as chamadas aos métodos para criação do painel ambiente de estudo
	 * do texto
	 * 
	 * @param levelRoot
	 *            , nível que será estabelecido como a raíz para realizar o
	 *            mapeamento dos objetos
	 */
	public void createAmbientStudy(LevelText levelRoot) {
		Tabbox tabbox = (Tabbox) getComponentById("panelStudy");
		if (tabbox != null) {
			Tabs tabs = tabbox.getTabs();
			if (tabs == null)
				tabs = new Tabs();
			tabs.appendChild(createTabWithName(getStudy().getText().getTitle()));
			Tabpanels tabpanels = tabbox.getTabpanels();
			if (tabpanels == null)
				tabpanels = new Tabpanels();
			tabpanels.appendChild(createTabPanelStudy(getStudy(), levelRoot));
			tabbox.appendChild(tabs);
			tabbox.appendChild(tabpanels);
		}

	}

	/**
	 * Cria o painel de estudo onde serão adicionados os itens de estudo do
	 * texto
	 * 
	 * @param study
	 *            , estudo em questão
	 * @param rootLevel
	 *            , nível raíz que será utilizado para o mapeamento
	 * @return Componente painel para a exibição na tela
	 */
	public Component createTabPanelStudy(Study study, LevelText rootLevel) {
		Tabpanel tabpanel = new Tabpanel();
		tabpanel.setSclass("tabpanelstudy");
		tabpanel.setStyle("text-align: justify; background-color: "
				+ getThisConfigurationValue("color_background").getValue()
				+ ";");

		// if (continueStudy) {
		// LevelText page =
		// getPageElement(study.getLastElementStop().getIdLevel());
		//
		// rootLevel = HibernateUtils.materializeProxy(getControl()
		// .getLevelText(page.getId()));
		// } else
		// rootLevel = HibernateUtils.materializeProxy(getControl()
		// .getLevelText(study.getText().getRootLevelText().getId()));

		tabpanel = (Tabpanel) mappingElementsAndChildren(rootLevel, tabpanel);

		return tabpanel;
	}

	/**
	 * Método recursivo que varre o conteúdo do texto e realiza chamadas para
	 * criação dos item de estudo para serem adicionados ao painel de estudo do
	 * texto
	 * 
	 * @param levelChild
	 *            , nível do texto de onde partirão os itens a serem mapeados
	 * @param comp
	 *            , componente que receberá os itens mapeamentos ao fim da
	 *            execução
	 * @return comp, com todos os iten mapeados.
	 */
	@SuppressWarnings("unchecked")
	public Component mappingElementsAndChildren(LevelText levelChild,
			Component comp) {
		if (levelChild == null)
			return comp;
		else {
			List<ElementText> elements = HibernateUtils
					.transaformBagInList(levelChild.getElements());
			if (elements != null && !elements.isEmpty())
				comp = createItemElementForStudy(
						HibernateUtils.materializeProxy(elements), comp);
			List<LevelText> childs = HibernateUtils
					.transaformBagInList(levelChild.getLevelsChildren());
			for (LevelText levelText : childs) {
				comp = mappingElementsAndChildren(levelText, comp);
			}
		}
		return comp;
	}

	/**
	 * Adiciona os eventos pertinentes a utilização do texto para realizar
	 * referência
	 * 
	 * @param itemStudy
	 *            , item de estudo que receberá os item com os eventsos
	 * @param elementText
	 *            , elemento do texto que será armazenado para criação do
	 *            relacionamento entre elementos do texto e o relacioanemnto)
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Component addEnventForNavigationReference(ItemStudy itemStudy,
			final ElementText elementText) {
		itemStudy.addEventListener(Events.ON_CLICK, new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getTarget() != null) {
					ItemStudy item = (ItemStudy) event.getTarget();
					addElementTextSelectedForReference(item, elementText);
					changeItemStyle(item);
					addItemStudySelectedForReference(item);
				}
			}

		});
		return itemStudy;
	}

	/**
	 * Adiciona um evento a um item de estudo para que ele possa ser navegável e
	 * interagir com o ambiente
	 * 
	 * @param itemStudy
	 *            , item de estudo que recebeá o evento
	 * @param elementText
	 *            , elemento que o item possui e se relacioa com o texto.
	 * @return componente mapeado com o evento para navegação de estudo na tela.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Component addEnventForNavigationStudy(ItemStudy itemStudy,
			final ElementText elementText) {
		itemStudy.addEventListener(Events.ON_CLICK, new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getTarget() != null) {
					ItemStudy item = (ItemStudy) event.getTarget();
					addElementTextSelected(item, elementText);
					changeItemStyle(item);
					addItemStudySelected(item);
					checkPanelActionVisibility();
				}
			}

		});
		return itemStudy;
	}

	/**
	 * Carrega uma configuração do usuário específica de acordo com a chave de
	 * busca
	 * 
	 * @param key
	 *            , código da configuração desejaa
	 * @return {@link Configuration} caso exista uma par a o novo.
	 */
	private Configuration getThisConfigurationValue(String key) {
		return ConfigurationManager.getManager().getConfigurationInstance(key,
				getUserLogged());
	}

	/**
	 * Verifica se existem marações no texto
	 * 
	 * @param list
	 * @return, texto de elementos de
	 */
	public MarkingOfUser checkExistingMarking(List<ElementsItensStudy> list) {
		List<MarkingUsed> markingUseds = new ArrayList<MarkingUsed>();
		MarkingUsed resultMark = null;
		for (ElementsItensStudy elementsItensStudy : list) {
			ItensOfStudy thisMark = HibernateUtils
					.materializeProxy(elementsItensStudy.getItemOfStudy());
			if (thisMark instanceof MarkingUsed)
				markingUseds.add((MarkingUsed) thisMark);
		}
		if (!markingUseds.isEmpty()) {
			resultMark = markingUseds.get(0);
			for (int i = 1; i < markingUseds.size(); i++) {
				if (markingUseds.get(i).getDateItem()
						.after(resultMark.getDateItem()))
					resultMark = markingUseds.get(i);
			}
			if (resultMark != null)
				return HibernateUtils.materializeProxy(resultMark
						.getMarkingOfUser());
		}
		return null;
	}

	/**
	 * Método que verifica se existe itens de estudo para um dado elemento em um
	 * estudo específico
	 * 
	 * @param elementText
	 *            , elemento do texto a ser buscado os elementos de estudo
	 * @param study
	 *            , estudo que será realizado a verificação da existência de
	 *            itens
	 * @return {@link Return}
	 */
	public Return checkExistingItensStudies(ElementText elementText, Study study) {
		newMap.put("checkElementText",
				HibernateUtils.materializeProxy(elementText));
		newMap.put("checkStudy", study);
		return getElementsItensStudyControl(newMap)
				.searchExistingItensStudies();
	}

	/**
	 * Método que cria o item de estudo que será alvo das interações de estudo
	 * 
	 * @param elements
	 *            , lista de elementos do texto a ser mapeado.
	 * @param compMaster
	 *            , componente onde todos os itens serão adicionados.
	 * @return componente pai com todos os itens adicionados.
	 */
	@SuppressWarnings({ "unchecked" })
	private Component createItemElementForStudy(List<ElementText> elements,
			Component compMaster) {
		ItemStudy itemStudy;
		Return retAux;
		boolean hasItensStudies = false;
		MarkingOfUser mark = null;

		if (elements != null) {
			for (ElementText elementText : elements) {

				LevelText level = HibernateUtils.materializeProxy(elementText
						.getIdLevel());
				if (!level.getName().equalsIgnoreCase("pagina")) {

					Long idText = HibernateUtils.materializeProxy(level
							.getIdText().getId());
					Long idLevel = HibernateUtils.materializeProxy(elementText
							.getId());
					itemStudy = new ItemStudy();

					itemStudy.setContent(elementText.getValue());
					itemStudy.setIdText(idText.toString());
					itemStudy.setIdElement(idLevel.toString());

					// TODO Colocar aqui a configuração de estudo de acordo com
					// as opções do usuário
					if (elementText.getName().equalsIgnoreCase("titulo")) {
						itemStudy.setStyle("width: 100%; text-align: center;");
						itemStudy.contentElement
								.setStyle("font-size: 18px; width: 100%; font-weight: bold;");
					}

					if (elementText.getName().equalsIgnoreCase("valor")) {
						if (isTextReferenceMode) {
							itemStudy = (ItemStudy) addEnventForNavigationReference(
									itemStudy, elementText);
						} else {
							itemStudy = (ItemStudy) addEnventForNavigationStudy(
									itemStudy, elementText);
						}

						itemStudy.contentElement.setStyle("font-size: "
								+ getThisConfigurationValue("size_font")
										.getValue() + "px;");
						String sty = itemStudy.contentElement.getStyle();
						itemStudy.contentElement.setStyle(sty
								+ "color:"
								+ getThisConfigurationValue("color_font")
										.getValue() + ";");

						retAux = checkExistingItensStudies(elementText,
								getStudy());
						if (retAux.isValid() && !retAux.getList().isEmpty())
							hasItensStudies = true;
						else
							hasItensStudies = false;

						if (hasItensStudies)
							mark = checkExistingMarking((List<ElementsItensStudy>) retAux
									.getList());

						if (mark != null)
							changeStyleMarkingThisItem(itemStudy,
									mark.getColor());

						if (!isTextReferenceMode)
							itemStudy = insertIconStudy(itemStudy, idLevel,
									hasItensStudies);

						itemStudies.add(itemStudy);
					}

					compMaster.appendChild(itemStudy);
				}
			}
		}
		return compMaster;
	}

}
