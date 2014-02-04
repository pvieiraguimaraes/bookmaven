package br.ueg.tcc.bookway.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.com.vexillum.model.CommonEntity;

@SuppressWarnings("serial")
@Entity
@Table(name = "relationship_texts")
public class RelationshipTextUser extends CommonEntity {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_text", insertable = true, updatable = true, nullable = false)
	private Text text;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_user", insertable = true, updatable = true, nullable = false)
	private UserBookway userBookway;

	public Text getText() {
		return text;
	}

	public void setText(Text text) {
		this.text = text;
	}

	public UserBookway getUserBookway() {
		return userBookway;
	}

	public void setUserBookway(UserBookway userBookway) {
		this.userBookway = userBookway;
	}
	
}
