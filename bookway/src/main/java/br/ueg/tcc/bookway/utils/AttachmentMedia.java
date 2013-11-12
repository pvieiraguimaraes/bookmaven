package br.ueg.tcc.bookway.utils;

import java.io.File;
import java.io.InputStream;

import org.zkoss.io.Files;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Executions;

import br.com.vexillum.control.manager.ExceptionManager;
import br.com.vexillum.control.util.Attachment;
import br.com.vexillum.util.Return;
import br.com.vexillum.util.ZKUtils;
import br.ueg.tcc.bookway.model.UserBookway;

public class AttachmentMedia implements Attachment<Media, UserBookway> {

	final String FOLDERATTACHMENTS = "profiles";
	final String PATH = Executions.getCurrent().getDesktop().getWebApp().getRealPath("/");
	
	@Override
	public Return uploadAttachment(Media file, String name, UserBookway user) {
		Return ret = new Return(true);
		try {
			InputStream in = ZKUtils.mediaToStream(file);
			Files.copy(new File(PATH + "\\" + FOLDERATTACHMENTS + "\\" + user.getId() + "\\" + name), in);
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
			if(f == null || f.exists()){
				f.delete();
			}	
		} catch (Exception e) {
			ret.concat(new ExceptionManager(e).treatException());
		}
		return ret;
	}

	@Override
	public File getAttachment(String name, UserBookway user) {
		File f = new File(PATH + "\\" + FOLDERATTACHMENTS + "\\" + user.getId() + "\\" + name); 
		if(!f.exists()) return null;
		return f;
	}

}
