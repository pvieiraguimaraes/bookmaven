package br.ueg.tcc.bookway.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import br.com.vexillum.model.CommonEntity;
import br.com.vexillum.model.annotations.Validate;
import br.com.vexillum.model.annotations.ValidatorClass;
import br.ueg.tcc.bookway.model.enums.TypeText;

@ValidatorClass(validatorClass = "br.ueg.tcc.bookway.control.validator.AnnotationValidator")
@SuppressWarnings("serial")
@Entity
public class Annotation extends CommonEntity {

	@Validate(max = 200, min = 3, notNull = true)
	private String name;

	@Validate(max = 2000, min = 3, notNull = true)
	private String content;

	@Validate(notNull = true)
	@Enumerated(EnumType.STRING)
	private TypeText typeText;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public TypeText getTypeText() {
		return typeText;
	}

	public void setTypeText(TypeText typeText) {
		this.typeText = typeText;
	}

}
