package br.ueg.tcc.bookway.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import br.com.vexillum.control.security.AuthenticatorProvider;
import br.com.vexillum.control.security.ManagerAuthenticator;
import br.com.vexillum.model.Category;
import br.com.vexillum.util.EncryptUtils;
import br.com.vexillum.util.HibernateUtils;
import br.com.vexillum.util.ReflectionUtils;
import br.com.vexillum.util.SpringFactory;
import br.ueg.tcc.bookway.control.UserBookwayControl;
import br.ueg.tcc.bookway.model.UserBookway;

/**
 * Autenticador utilizado para realizar a verificação de autenticação e
 * segurança do filtro do framework Spring Security, contém as regras principais
 * para atender as necessidades de segurança específica do sistema e herda todas
 * as funcionalidades do autenticador padrão da arquitetura
 * 
 * @author pedro
 * 
 */
@SuppressWarnings("rawtypes")
public class BookwayAuthenticatorProvider extends AuthenticatorProvider
		implements AuthenticationProvider {

	/**
	 * Controlador específico para acessar a camada de negócio específica.
	 */
	protected UserBookwayControl bookwayControl;

	public BookwayAuthenticatorProvider() {
		bookwayControl = SpringFactory.getController("userBookwayControl",
				UserBookwayControl.class,
				ReflectionUtils.prepareDataForPersistence(this));
	}

	@Override
	public Authentication authenticate(Authentication auth)
			throws AuthenticationException {
		UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) auth;
		String userName = token.getName();
		String password = token.getCredentials() != null ? token
				.getCredentials().toString() : null;
		UserBookway user = bookwayControl.getUser(userName,
				EncryptUtils.encryptOnSHA512(password));
		if (user == null)
			return null;
		// TODO Tratar permissoes depois sem ser como categoria
		// List<String> permissions = control.getPermissionsUser(user);
		List<Category> userCategories = new ArrayList<Category>();
		userCategories.add(bookwayControl.getCategoryUser(user));
		ManagerAuthenticator<UserBookway, Category> manager = new ManagerAuthenticator<UserBookway, Category>(
				HibernateUtils.materializeProxy(user), userCategories);
		manager.setAuthenticated(user != null);
		return manager;
	}

}
