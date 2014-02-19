package br.ueg.tcc.bookway.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import br.com.vexillum.model.CommonEntity;
import br.com.vexillum.model.ICommonEntity;
import br.com.vexillum.model.annotations.Validate;
import br.com.vexillum.model.annotations.ValidatorClass;

/**
 * Entidade modelo de negócio utilizada no caso de uso Manter Marcações
 * 
 * @author pedro
 * 
 */
@ValidatorClass(validatorClass = "br.ueg.tcc.bookway.control.validator.MarkingValidator")
@SuppressWarnings("serial")
@Entity
public class MarkingOfUser extends CommonEntity implements ICommonEntity {

	@Validate(max = 20, min = 2)
	private String name;

	private String color;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_user", insertable = true, updatable = false, nullable = true)
	private UserBookway userBookway;

	@Cascade(CascadeType.ALL)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "marking", orphanRemoval = true)
	private List<TagsOfMarking> tagsOfMarkings;

	@Cascade(CascadeType.ALL)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "markingOfUser", orphanRemoval = true)
	private List<MarkingUsed> markingUseds;

	public MarkingOfUser() {
		tagsOfMarkings = new ArrayList<TagsOfMarking>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public UserBookway getUserBookway() {
		return userBookway;
	}

	public void setUserBookway(UserBookway userBookway) {
		this.userBookway = userBookway;
	}

	public List<TagsOfMarking> getTagsOfMarkings() {
		return tagsOfMarkings;
	}

	public void setTagsOfMarkings(List<TagsOfMarking> tagsOfMarkings) {
		this.tagsOfMarkings = tagsOfMarkings;
	}

	public List<MarkingUsed> getMarkingUseds() {
		return markingUseds;
	}

	public void setMarkingUseds(List<MarkingUsed> markingUseds) {
		this.markingUseds = markingUseds;
	}

	@Override
	public String toString() {
		return this.name;
	}
}
