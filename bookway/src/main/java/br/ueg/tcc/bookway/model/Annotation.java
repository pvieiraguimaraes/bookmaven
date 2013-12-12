package br.ueg.tcc.bookway.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import br.com.vexillum.model.annotations.Validate;
import br.com.vexillum.model.annotations.ValidatorClass;

@ValidatorClass(validatorClass = "br.ueg.tcc.bookway.control.validator.AnnotationValidator")
@SuppressWarnings("serial")
@DiscriminatorValue("A")
@Entity
public class Annotation extends ItensOfStudy {

	@Validate(max = 200, min = 3, notNull = true)
	private String title;

	@Validate(max = 2000, min = 3, notNull = true)
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
