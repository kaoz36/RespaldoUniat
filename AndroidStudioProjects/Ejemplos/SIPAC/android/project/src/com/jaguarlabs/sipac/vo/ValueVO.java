package com.jaguarlabs.sipac.vo;

public class ValueVO<valuetype> {
	private valuetype value;
	private String label;
	
	
	public ValueVO(valuetype value, String label) {
		super();
		this.value = value;
		this.label = label;
	}


	public valuetype getValue() {
		return value;
	}


	public void setValue(valuetype value) {
		this.value = value;
	}


	public String getLabel() {
		return label;
	}


	public void setLabel(String label) {
		this.label = label;
	}


	@Override
	public String toString() {
		return  label;
	}
	
	

}
