package br.ueg.tcc.bookway.model;

import javax.persistence.Entity;

import br.com.vexillum.model.annotations.Validate;
import br.com.vexillum.model.annotations.ValidatorClass;

@ValidatorClass(validatorClass = "br.ueg.tcc.bookway.control.validator.AnnotationValidator")
@SuppressWarnings("serial")
@Entity
public class Annotation extends ItensOfStudy {

	@Validate(max = 200, min = 3, notNull = true)
	private String title;

	@Validate(max = 2000, min = 3, notNull = true)
	private String content;

	public String getName() {
		return title;
	}

	public void setName(String name) {
		this.title = name;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
