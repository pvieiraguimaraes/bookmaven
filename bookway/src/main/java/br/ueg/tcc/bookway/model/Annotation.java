package br.ueg.tcc.bookway.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import br.com.vexillum.model.annotations.Validate;
import br.com.vexillum.model.annotations.ValidatorClass;

/**
 * Entidade modelo de neg�cio utilizada no caso de uso Manter Anota��es
 * 
 * @author pedro
 * 
 */
@ValidatorClass(validatorClass = "br.ueg.tcc.bookway.control.validator.AnnotationValidator")
@SuppressWarnings("serial")
@DiscriminatorValue("A")
@Entity
public class Annotation extends ItensOfStudy {

	@Validate(max = 200, min = 3)
	private String title;

	@Validate(max = 2000, min = 3)
	private String content;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
