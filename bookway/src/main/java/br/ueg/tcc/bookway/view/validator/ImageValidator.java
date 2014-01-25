package br.ueg.tcc.bookway.view.validator;

import org.zkoss.image.AImage;
import org.zkoss.util.media.Media;

import br.com.vexillum.control.manager.ExceptionManager;
import br.com.vexillum.util.Message;
import br.com.vexillum.util.Return;
import br.com.vexillum.util.ZKUtils;

public class ImageValidator{
	
	Media media;

	public ImageValidator(Media media) {
		this.media = media;
	}
	
	public Return upload(){
		Return ret = new Return(true);
		ret.concat(isImage());
		if(ret.isValid()){
			ret.concat(maxSize());
			ret.concat(contentType());
//			ret.concat(maxHeightAndWidth());
		}
		return ret;
	}
	
	private Return isImage(){
		Return ret = new Return(true);
		if(media.getContentType().indexOf("image") == -1){
			ret.concat(new Return(false, new Message(null, "Você apenas pode fazer o upload de imagens!")));
		}
		return ret;
	}
	
	protected Return maxSize(){
		Return ret = new Return(true);
		if(media.getByteData().length > 1000*500){
			ret.concat(new Return(false, new Message(null, "Imagem não poder ter tamanho superior a 500KB!")));
		}
		return ret;
	}
	
	private Return contentType(){
		Return ret = new Return(true);
		if(!media.getFormat().equalsIgnoreCase("png") && !media.getFormat().equalsIgnoreCase("gif") && !media.getFormat().equalsIgnoreCase("jpg") && !media.getFormat().equalsIgnoreCase("jpeg")){
			ret.concat(new Return(false, new Message(null, "Imagem não suportada! A imagem dever ser JPG, PNG ou GIF!")));
		}
		return ret;
	}
	
	protected Return maxHeightAndWidth(){
		Return ret = new Return(true);
		try {
			AImage img = (media instanceof AImage) ? (AImage) media : new AImage("photo", ZKUtils.mediaToStream(media));
			if(img.getHeight() > 150 || img.getWidth() > 150){
				ret.concat(new Return(false, new Message(null, "Imagem não poder ter resolução superior a 150x150!")));
			}
		} catch (Exception e) {
			ret.concat(new ExceptionManager(e).treatException());
		}
		return ret;
	}	

}
