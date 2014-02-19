package br.ueg.tcc.bookway.utils;

import java.io.File;
import java.io.InputStream;

import org.zkoss.io.Files;
import org.zkoss.util.media.Media;

import br.com.vexillum.configuration.Properties;
import br.com.vexillum.control.manager.ExceptionManager;
import br.com.vexillum.control.util.Attachment;
import br.com.vexillum.util.Return;
import br.com.vexillum.util.SpringFactory;
import br.com.vexillum.util.ZKUtils;
import br.ueg.tcc.bookway.model.UserBookway;

/**
 * Classe que manipula os objetos relacionados com o upload de arquivos
 * 
 * @author pedro
 * 
 */
public class AttachmentMedia implements Attachment<Media, UserBookway> {

	protected Properties configuration;

	public AttachmentMedia() {
		configuration = SpringFactory.getInstance().getBean("configProperties",
				Properties.class);
	}

	@Override
	public Return uploadAttachment(Media file, String name, UserBookway user) {
		Return ret = new Return(true);
		try {
			InputStream in = ZKUtils.mediaToStream(file);
			Files.copy(new File(configuration.getKey("PATH_PHOTO") + "\\"
					+ user.getId() + "\\" + name), in);
		} catch (Exception e) {
			ret.concat(new ExceptionManager(e).treatException());
		}
		return ret;
	}

	@Override
	public Return deleteAttachment(String name, UserBookway user) {
		Return ret = new Return(true);
		try {
			File f = getAttachment(name, user);
			if (f == null || f.exists()) {
				f.delete();
			}
		} catch (Exception e) {
			ret.concat(new ExceptionManager(e).treatException());
		}
		return ret;
	}

	@Override
	public File getAttachment(String name, UserBookway user) {
		File f = new File(configuration.getKey("PATH_PHOTO") + "\\"
				+ user.getId() + "\\" + name);
		if (!f.exists())
			return null;
		return f;
	}

}
