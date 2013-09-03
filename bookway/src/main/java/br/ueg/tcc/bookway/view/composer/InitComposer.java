package br.ueg.tcc.bookway.view.composer;

import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.zkoss.zk.ui.Component;

import br.com.vexillum.control.GenericControl;
import br.com.vexillum.model.ICommonEntity;
import br.com.vexillum.util.ReflectionUtils;
import br.com.vexillum.util.Return;
import br.com.vexillum.util.SpringFactory;
import br.com.vexillum.view.CRUDComposer;
import br.ueg.tcc.bookway.control.TextControl;
import br.ueg.tcc.bookway.model.Text;
import br.ueg.tcc.bookway.view.macros.ItemText;
import br.ueg.tcc.bookway.view.macros.MyText;

@SuppressWarnings({ "serial" })
@org.springframework.stereotype.Component
@Scope("prototype")
public class InitComposer<E extends ICommonEntity, G extends GenericControl<E>>
		extends CRUDComposer<E, G> {

	private List<Text> allMyTexts;

	public List<Text> getAllMyTexts() {
		return allMyTexts;
	}

	public void setAllMyTexts(List<Text> allMyTexts) {
		this.allMyTexts = allMyTexts;
	}

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		initUserData();
	}

	private void initUserData() {
		createListTextUser();
	}

	@SuppressWarnings("unchecked")
	public void createListTextUser() {
		Return retListText = new Return(true);
		retListText.concat(getControlText().doAction("listTextsUser"));
		if (retListText.isValid())
			retListText.concat(getControlText()
					.doAction("listTextsAddedByUser"));
		if (retListText.isValid())
			setAllMyTexts((List<Text>) retListText.getList());
		setUpListTextInComponent(getAllMyTexts(), "panelMyTexts", component,
				"MyText", true, 3);
	}

	private TextControl getControlText() {
		return SpringFactory.getController("textControl", TextControl.class,
				ReflectionUtils.prepareDataForPersistence(this));
	}

	/**
	 * Método para setar a lista de elementos visual que represetarão o texto
	 * que serao criados pela pesquisa ou na inicialização, listandos todos os
	 * textos do usuário
	 * 
	 * @param textsUser
	 *            , lista de textos que serão representados
	 * @param idParent
	 *            , component que deverá ser o pai
	 * @param comp
	 *            , component da execução do composer
	 * @param nameComp
	 *            , nome do component que será criado
	 * @param sorted, se os elementos que irão compor a lista serão sorteados
	 * @param numberOfElements, numero de elementos que comporão a lista
	 */
	public void setUpListTextInComponent(List<Text> textsUser, String idParent,
			Component comp, String nameComp, boolean sorted, Integer numberOfElements) {
		Component componentParent = getComponentById(comp, idParent);
		if(sorted)
			Collections.shuffle(textsUser);
		if(numberOfElements != null)
			textsUser = textsUser.subList(0, numberOfElements);
		if (textsUser != null && componentParent != null) {
			for (Text text : textsUser) {
				if (nameComp.equalsIgnoreCase("ItemText"))
					componentParent.appendChild(createItemText(text));
				else
					componentParent.appendChild(createMyText(text));
			}
		}
	}

	private ItemText createItemText(Text text) {
		ItemText item = new ItemText();
		item.setUser(text.getUserOwning().getName());
		item.setTitle(text.getTitle());
		item.setDescription(text.getDescription());
		return item;
	}

	private MyText createMyText(Text text) {
		MyText myText = new MyText();
		myText.setTitle(text.getTitle());
		myText.setDescription(text.getDescription());
		return myText;
	}

	@Override
	public G getControl() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public E getEntityObject() {
		// TODO Auto-generated method stub
		return null;
	}

}
