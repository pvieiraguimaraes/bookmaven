package br.ueg.tcc.bookway.control;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import br.com.vexillum.control.ConfigurationController;
import br.com.vexillum.util.Return;

/**
 * Controlador para os casos de uso de Configurações Personalizadas
 * 
 * @author pedro
 * 
 */
@Service
@Scope("prototype")
public class ConfigurationControl extends ConfigurationController {

	/**
	 * Busca uma categoria de propriedade do usuário de acordo com um id passado
	 * 
	 * @param id
	 *            , código da categoria a ser buscada.
	 * @return {@link Return}
	 */
	public Return getCategoryById(Long id) {
		String sql = "FROM UserPropertiesCategory WHERE id = '" + id + "'";
		data.put("sql", sql);
		return searchByHQL();
	}
}
