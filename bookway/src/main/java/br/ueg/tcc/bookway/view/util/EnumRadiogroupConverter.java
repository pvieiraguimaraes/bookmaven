package br.ueg.tcc.bookway.view.util;

import org.zkoss.zk.ui.Component;
import org.zkoss.zkplus.databind.RadiogroupSelectedItemConverter;

/**
 * Classe que contém os métodos para conversão Enum em Radio buttons
 * 
 * @author pedro
 * 
 * @param <T>
 */
@SuppressWarnings({ "serial", "rawtypes" })
public class EnumRadiogroupConverter<T extends Enum> extends
		RadiogroupSelectedItemConverter {
	private final Class<T> className;

	public EnumRadiogroupConverter(Class<T> className) {
		this.className = className;
	}

	@Override
	public Object coerceToUi(Object val, Component component) {
		if (val instanceof Enum) {
			Enum value = (Enum) val;
			String constanteName = value.name();
			return super.coerceToUi(constanteName, component);
		} else if (val == null) {
			return null;
		} else {
			throw new IllegalArgumentException("val Object must be an Enum");
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object coerceToBean(Object val, Component component) {
		String enumName = (String) super.coerceToBean(val, component);
		return Enum.valueOf(className, enumName);
	}
}
