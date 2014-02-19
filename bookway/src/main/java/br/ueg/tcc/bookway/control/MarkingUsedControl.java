package br.ueg.tcc.bookway.control;

import java.text.SimpleDateFormat;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import br.com.vexillum.control.GenericControl;
import br.com.vexillum.control.IGenericControl;
import br.com.vexillum.util.Return;
import br.ueg.tcc.bookway.model.MarkingUsed;

/**
 * Controle utilizado para realizar as regras do uso das marca��es em um estudo
 * em quest�o
 * 
 * @author pedro
 * 
 */
@Service
@Scope("prototype")
public class MarkingUsedControl extends GenericControl<MarkingUsed> implements
		IGenericControl<MarkingUsed> {

	public MarkingUsedControl() {
		super(MarkingUsed.class);
	}

	/**
	 * Busca uma marca��o que foi utilizada em um momento espec�fico de acordo
	 * com os par�metros passados pelo o mapa do controlador
	 * 
	 * @return {@link Return}
	 */
	public Return searchThisMarkingUsed() {
		MarkingUsed mark = (MarkingUsed) data.get("markingUsed");
		SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
		String sql = "FROM MarkingUsed WHERE id_markingofuser = '"
				+ mark.getMarkingOfUser().getId() + "' AND id_study = '"
				+ mark.getStudy().getId() + "' AND dateItem = '"
				+ spf.format(mark.getDateItem()) + "'";
		data.put("sql", sql);
		return searchByHQL();
	}

}
