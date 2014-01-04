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
import br.ueg.tcc.bookway.model.UserBookway;

@SuppressWarnings("rawtypes")
public class BookwayAuthenticatorProvider extends AuthenticatorProvider
		implements AuthenticationProvider {

	@Override
	public Authentication authenticate(Authentication auth)
			throws AuthenticationException {
		UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) auth;
		String userName = token.getName();
		String password = token.getCredentials() != null ? token
				.getCredentials().toString() : null;
		UserBookway user = (UserBookway) control.getUser(userName, EncryptUtils.encryptOnSHA512(password));
		if (user == null)
			return null;
		// TODO Tratar permissoes depois sem ser como categoria
		// List<String> permissions = control.getPermissionsUser(user);
		List<Category> userCategories = new ArrayList<Category>();
		userCategories.add(control.getCategoryUser(user));
		ManagerAuthenticator<UserBookway, Category> manager = new ManagerAuthenticator<UserBookway, Category>(
				HibernateUtils.materializeProxy(user), userCategories);
		manager.setAuthenticated(user != null);
		return manager;
	}

}
