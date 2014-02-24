package br.ueg.tcc.bookway.view.validator;

import org.zkoss.image.AImage;
import org.zkoss.util.media.Media;

import br.com.vexillum.control.manager.ExceptionManager;
import br.com.vexillum.util.Message;
import br.com.vexillum.util.Return;
import br.com.vexillum.util.ZKUtils;

/**
 * Classe que faz valida��o do upload de imagens
 * 
 * @author pedro
 * 
 */
public class ImageValidator {

	Media media;

	/**
	 * Construtor padr�o
	 * 
	 * @param media
	 */
	public ImageValidator(Media media) {
		this.media = media;
	}

	/**
	 * M�todo que faz o upload da imagem
	 * 
	 * @return {@link Return}
	 */
	public Return upload() {
		Return ret = new Return(true);
		ret.concat(isImage());
		if (ret.isValid()) {
			ret.concat(maxSize());
			ret.concat(contentType());
			// ret.concat(maxHeightAndWidth());
		}
		return ret;
	}

	/**
	 * Valida se o arquivo � uma imagem
	 * 
	 * @return {@link Return}
	 */
	private Return isImage() {
		Return ret = new Return(true);
		if (media.getContentType().indexOf("image") == -1) {
			ret.concat(new Return(false, new Message(null,
					"Insira um arquivo v�lido!")));
		}
		return ret;
	}

	/**
	 * Valida o tamanho m�ximo permitido para carregamento da imagem
	 * 
	 * @return {@link Return}
	 */
	protected Return maxSize() {
		Return ret = new Return(true);
		if (media.getByteData().length > 1000 * 500) {
			ret.concat(new Return(false, new Message(null,
					"Imagem n�o poder ter tamanho superior a 500KB!")));
		}
		return ret;
	}

	/**
	 * Valida o tipo de arquivo submetido ao carregamento
	 * 
	 * @return {@link Return}
	 */
	private Return contentType() {
		Return ret = new Return(true);
		if (!media.getFormat().equalsIgnoreCase("png")
				&& !media.getFormat().equalsIgnoreCase("gif")
				&& !media.getFormat().equalsIgnoreCase("jpg")
				&& !media.getFormat().equalsIgnoreCase("jpeg")) {
			ret.concat(new Return(
					false,
					new Message(null,
							"Imagem n�o suportada! A imagem dever ser JPG, PNG ou GIF!")));
		}
		return ret;
	}

	/**
	 * Valida a altura e largura m�ximas permitidas
	 * 
	 * @return {@link Return}
	 */
	protected Return maxHeightAndWidth() {
		Return ret = new Return(true);
		try {
			AImage img = (media instanceof AImage) ? (AImage) media
					: new AImage("photo", ZKUtils.mediaToStream(media));
			if (img.getHeight() > 150 || img.getWidth() > 150) {
				ret.concat(new Return(false, new Message(null,
						"Imagem n�o poder ter resolu��o superior a 150x150!")));
			}
		} catch (Exception e) {
			ret.concat(new ExceptionManager(e).treatException());
		}
		return ret;
	}

}
