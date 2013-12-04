package br.ueg.tcc.bookway.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import br.com.vexillum.model.CommonEntity;

@SuppressWarnings("serial")
@Entity
public class Study extends CommonEntity {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_text", insertable = true, updatable = false, nullable = false)
	private Text text;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_user", insertable = true, updatable = false, nullable = false)
	private UserBookway userBookway;

	private Date dateStudy;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_lastelementstop", insertable = true, updatable = false, nullable = true)
	private ElementText lastElementStop;

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

	public Date getDateStudy() {
		return dateStudy;
	}

	public void setDateStudy(Date dateStudy) {
		this.dateStudy = dateStudy;
	}

	public ElementText getLastElementStop() {
		return lastElementStop;
	}

	public void setLastElementStop(ElementText lastElementStop) {
		this.lastElementStop = lastElementStop;
	}

}
