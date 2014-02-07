package br.ueg.tcc.bookway.control;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import br.com.vexillum.control.ConfigurationController;
import br.com.vexillum.util.Return;

@Service
@Scope("prototype")
public class ConfigurationControl extends ConfigurationController {

	public Return getCategoryById(Long id){
		String sql = "FROM UserPropertiesCategory WHERE id = '" + id + "'";
		data.put("sql", sql);
		return searchByHQL();
	}
}
