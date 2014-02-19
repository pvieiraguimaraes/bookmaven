package br.ueg.tcc.bookway.control;

import java.text.SimpleDateFormat;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import br.com.vexillum.control.GenericControl;
import br.com.vexillum.util.Return;
import br.ueg.tcc.bookway.model.RelationshipTextElement;
import br.ueg.tcc.bookway.model.Text;

/**
 * Controlador responsável por tratar todas as regras de negócio envolvendo o
 * caso de uso Relacionamento entre trechos e textos
 * 
 * @author pedro
 * 
 */
@Service
@Scope("prototype")
public class RelationshipTextElementControl extends
		GenericControl<RelationshipTextElement> {

	public RelationshipTextElementControl() {
		super(RelationshipTextElement.class);
	}

	/**
	 * Realiza a busca de um relacionamento de um texto em questão
	 * 
	 * @param relationshipTextElement
	 *            , dados de um relacionamento que se quer buscar
	 * @return {@link Return}
	 */
	public Return searchThisRelationship(
			RelationshipTextElement relationshipTextElement) {
		SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
		String sql = "FROM RelationshipTextElement WHERE id_study = '"
				+ relationshipTextElement.getStudy().getId()
				+ "' AND dateItem = '"
				+ spf.format(relationshipTextElement.getDateItem()) + "'";
		data.put("sql", sql);
		return searchByHQL();
	}

	/**
	 * Busca um relacionamento realizado para ou de um texto dado.
	 * 
	 * @return {@link Return}
	 */
	public Return searchRelationship() {
		Text text = (Text) data.get("selectedText");
		String sql = "FROM RelationshipTextElement WHERE id_text_origin = '"
				+ text.getId() + "' OR id_text_destiny = '" + text.getId()
				+ "'";
		data.put("sql", sql);
		return searchByHQL();
	}

}
