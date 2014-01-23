package br.ueg.tcc.bookway.view.macros;

import org.zkoss.zk.ui.HtmlMacroComponent;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Image;

@SuppressWarnings("serial")
@VariableResolver(value = {org.zkoss.zkplus.spring.DelegatingVariableResolver.class})
public class MyFriend extends HtmlMacroComponent {

	@Wire
	private Image imageUser;
	
	public MyFriend() {
		compose();
	}
	
	public MyFriend(String imageSrc, String tooltipoImage) {
		compose();
		setImageUser(imageSrc);
		imageUser.setTooltiptext(tooltipoImage);
	}

	public String getImageUser() {
		return imageUser.getSrc();
	}

	public void setImageUser(String imageSrc) {
		imageUser.setSrc(imageSrc);
	}
}
