package br.ueg.tcc.bookway.control.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import br.com.vexillum.control.security.ManagerAuthenticator;
import br.com.vexillum.model.Category;
import br.com.vexillum.util.ReflectionUtils;
import br.com.vexillum.util.SpringFactory;
import br.ueg.tcc.bookway.control.UserBookwayControl;
import br.ueg.tcc.bookway.model.UserBookway;


public class BookwayProvider implements AuthenticationProvider {
	
	private UserBookwayControl controlBookway;
	
	public BookwayProvider() {
		controlBookway = SpringFactory.getController(
				"userBookwayControl", UserBookwayControl.class,
				ReflectionUtils.prepareDataForPersistence(this));
	}

	@Override
	public Authentication authenticate(Authentication auth)
			throws AuthenticationException {
		UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) auth;
		String userName = token.getName();
		String password = token.getCredentials() != null ? token.getCredentials().toString() : null;
		UserBookway user = (UserBookway) controlBookway.getUser(userName, password);
		if (user == null)
			return null;
		//TODO Tratar permissoes depois sem ser como categoria
		//List<String> permissions = control.getPermissionsUser(user);
		List<Category> userCategories = new ArrayList<Category>();
		userCategories.add(controlBookway.getCategoryUser(user));
		ManagerAuthenticator<UserBookway, Category> manager = new ManagerAuthenticator<UserBookway, Category>(user, userCategories);
		manager.setAuthenticated(user != null);
		return manager;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return (UsernamePasswordAuthenticationToken.class
				.isAssignableFrom(authentication));
	}

}
