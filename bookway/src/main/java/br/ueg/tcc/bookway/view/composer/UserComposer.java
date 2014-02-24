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

/**
 * Classe responsável pelas funcionalidades relacionadas a camada de controle do
 * caso de uso Manter Usuários
 * 
 * @author pedro
 * 
 */
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

	/**
	 * Faz chamada a alguns métodos de inicialização de dados do usuário
	 */
	public void init() {
		// checkUsersInactives30DaysLeft();
		setUserProfile();
		initLists();
	}

	/**
	 * Faz chamada ao controle para verificação de usuários com contas que
	 * deverão ser excluídas
	 */
	@SuppressWarnings("unchecked")
	private void checkUsersInactives30DaysLeft() {
		Return ret = getControl().doAction("chechUsersFordelete");
		List<UserBookway> users = (List<UserBookway>) ret.getList();
		if (ret.isValid() && !users.isEmpty()) {
			for (UserBookway userBookway : users) {
				entity = userBookway;
				getControl().deleteAllElementsUser();
			}
		}
	}

	/**
	 * Inicializa lista de Estados e Áreas de interesse
	 */
	private void initLists() {
		setListAreaOfInterest(getControl().initListAreaOfInterest());
		setListState(getControl().initListState());
	}

	/**
	 * Seta o usuário da sessão
	 */
	private void setUserProfile() {
		entity = (UserBookway) getUserLogged();
		// showUserBookwayPhoto((Image) getComponentById("photoUserbookway"),
		// getEntity());
	}

	/**
	 * Faz chamada aos métodos do controle para alterações de dados da conta do
	 * usuário
	 */
	public void updateAccount() {
		if (photoUserBookway != null) {
			uploadUserBookwayImage(photoUserBookway, entity);
			session.setAttribute("userProfile", entity);
		}
		UserBookway user = getControl().getUserById(entity.getId().toString());
		entity.setPassword(user.getPassword());
		entity.setConfirmPassword(user.getPassword());
		treatReturn(getControl().doAction("updateAccount"));
		loadBinder();
	}

	/**
	 * Faz chamada ao controle para exclusão da conta do usuário
	 */
	public void deleteAccount() {
		Return ret = new Return(true);
		ret = getControl().doAction("deleteAccount");
		treatReturn(ret);
	}

	/**
	 * Método que carrega foto do perfil do usuário
	 * 
	 * @param file
	 *            , arquivo com a imagem para o perfil
	 * @param userBookway
	 *            , usuário em questão
	 * @return {@link Return}
	 */
	public Return uploadUserBookwayImage(Media file, UserBookway userBookway) {
		Return ret = new Return(true);
		if (file != null) {
			AttachmentMedia att = new AttachmentMedia();
			try {
				att.uploadAttachment(file, "photo", userBookway);
			} catch (Exception e) {
				ret.concat(new ExceptionManager(e).treatException());
			}
		}
		return ret;
	}

	/**
	 * Altera a foto do perfil do usuário
	 * 
	 * @param event
	 *            , evento de upload
	 */
	public void changeUserBookwayImage(UploadEvent event) {
		Media media = event.getMedia();
		ImageValidator val = new ImageValidator(media);
		Return ret = val.upload();
		if (ret.isValid()) {
			try {
				AImage image = (AImage) media;
				InputStream image1 = ZKUtils.mediaToStream(media);
				if (image.getHeight() > 117 || image.getWidth() > 114) {
					image1 = ImageUtils.scaleImage(image1, 114, 117,
							image.getFormat());
				}
				image = new AImage("photo", image1);
				setPhotoUserBookway(image);

				((Image) getComponentById("photoPerfilUser")).setContent(image);
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
		return SpringFactory.getController("userBookwayControl",
				UserBookwayControl.class,
				ReflectionUtils.prepareDataForPersistence(this));
	}

	/**
	 * Seta a foto do usuário no painel de foto
	 */
	public void checkUserPhoto() {
		Image image = (Image) getComponentById("photoPerfilUser");
		showUserBookwayPhoto(image, (UserBookway) getUserInSession());
	}

	/**
	 * Faz chamada ao controle para alteração de senha
	 */
	public void changePasswordUser() {
		Return ret = new Return(true);
		ret.concat(getControl().doAction("changePasswordUser"));
		if (ret.isValid())
			getComponentById(getComponent(), "frmChangePassword").detach();
		treatReturn(ret);
	}
}
