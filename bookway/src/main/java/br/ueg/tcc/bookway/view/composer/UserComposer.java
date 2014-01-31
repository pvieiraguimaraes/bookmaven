package br.ueg.tcc.bookway.view.composer;

import java.io.InputStream;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.zkoss.image.AImage;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zul.Image;

import br.com.vexillum.control.manager.ExceptionManager;
import br.com.vexillum.util.EncryptUtils;
import br.com.vexillum.util.ImageUtils;
import br.com.vexillum.util.ReflectionUtils;
import br.com.vexillum.util.Return;
import br.com.vexillum.util.SpringFactory;
import br.com.vexillum.util.ZKUtils;
import br.ueg.tcc.bookway.control.UserBookwayControl;
import br.ueg.tcc.bookway.model.UserBookway;
import br.ueg.tcc.bookway.model.enums.AreaOfInterest;
import br.ueg.tcc.bookway.model.enums.State;
import br.ueg.tcc.bookway.utils.AttachmentMedia;
import br.ueg.tcc.bookway.view.validator.ImageValidator;

@SuppressWarnings("serial")
@org.springframework.stereotype.Component
@Scope("prototype")
public class UserComposer extends InitComposer<UserBookway, UserBookwayControl> {
	
	private List<AreaOfInterest> listAreaOfInterest;
	private List<State> listState;
	
	private String actualPassword;
	private String newPassword;
	private String confirmNewPassword;
	private Media photoUserBookway;
	
	public Media getPhotoUserBookway() {
		return photoUserBookway;
	}

	public void setPhotoUserBookway(Media photoUserBookway) {
		this.photoUserBookway = photoUserBookway;
	}

	public String getActualPassword() {
		return actualPassword;
	}

	public void setActualPassword(String actualPassword) {
		this.actualPassword = EncryptUtils.encryptOnSHA512(actualPassword);
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getConfirmNewPassword() {
		return confirmNewPassword;
	}

	public void setConfirmNewPassword(String confirmNewPassword) {
		this.confirmNewPassword = confirmNewPassword;
	}

	public List<AreaOfInterest> getListAreaOfInterest() {
		return listAreaOfInterest;
	}

	public void setListAreaOfInterest(List<AreaOfInterest> listAreaOfInterest) {
		this.listAreaOfInterest = listAreaOfInterest;
	}

	public List<State> getListState() {
		return listState;
	}

	public void setListState(List<State> listState) {
		this.listState = listState;
	}

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		init();
		loadBinder();
	}

	public void init() {
		setUserProfile();
		initLists();
	}

	private void initLists() {
		setListAreaOfInterest(getControl().initListAreaOfInterest());
		setListState(getControl().initListState());
	}

	private void setUserProfile() {
		entity = (UserBookway) getUserLogged();
//		showUserBookwayPhoto((Image) getComponentById("photoUserbookway"), getEntity());
	}

	public void updateAccount() {
		if(photoUserBookway != null){
			uploadUserBookwayImage(photoUserBookway, entity);
			session.setAttribute("userProfile", entity);
		}
		UserBookway user = getControl().getUserById(entity.getId().toString());
		entity.setPassword(user.getPassword());
		entity.setConfirmPassword(user.getPassword());
		treatReturn(getControl().doAction("updateAccount"));
	}

	public void deleteAccount() {
		Return ret = new Return(true);
		ret = getControl().doAction("deleteAccount");
		treatReturn(ret);
	}
	
	public Return uploadUserBookwayImage(Media file, UserBookway userBookway){
		Return ret = new Return(true);
		if(file != null){
			AttachmentMedia att = new AttachmentMedia();
			try {
				att.uploadAttachment(file, "photo", userBookway);
			} catch (Exception e) {
				ret.concat(new ExceptionManager(e).treatException());
			}
		}
		return ret;
	}
	
	public void changeUserBookwayImage(UploadEvent event){
        Media media = event.getMedia();
        ImageValidator val = new ImageValidator(media);
        Return ret = val.upload();
        if(ret.isValid()){
        	try {
        		AImage image = (AImage) media;
        		InputStream image1 = ZKUtils.mediaToStream(media);
    			if(image.getHeight() > 117 || image.getWidth() > 114){
    				image1 = ImageUtils.scaleImage(image1, 114, 117, image.getFormat());
    			}
    			image = new AImage("photo", image1);
				setPhotoUserBookway(image);
				
				//TODO Ver como ficará o carregamento dessa imagem
				((Image)getComponentById("photoPerfilUser")).setContent(image);
			} catch (Exception e) {
				ret.concat(new ExceptionManager(e).treatException());
			}
        }
        treatReturn(ret);
	}
	
	@Override
	public UserBookway getEntityObject() {
		return new UserBookway();
	}

	@Override
	public UserBookwayControl getControl() {
		return SpringFactory.getController("userBookwayControl", UserBookwayControl.class, ReflectionUtils.prepareDataForPersistence(this));
	}
	
	public void checkUserPhoto(){
		Image image = (Image) getComponentById("photoPerfilUser");
		showUserBookwayPhoto(image, (UserBookway) getUserInSession());
	}
	
	public void changePasswordUser(){
		Return ret = new Return(true);
		ret.concat(getControl().doAction("changePasswordUser"));
		if(ret.isValid())
			getComponentById(getComponent(), "frmChangePassword").detach();
		treatReturn(ret);
	}
}
