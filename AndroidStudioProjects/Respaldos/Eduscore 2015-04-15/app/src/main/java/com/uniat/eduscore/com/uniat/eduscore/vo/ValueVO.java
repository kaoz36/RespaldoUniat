package com.uniat.eduscore.com.uniat.eduscore.vo;

/**
 * Created by Admin on 14/04/2015.
 */
public class ValueVO<valueType> {

    private valueType value;
    private String label;

    public ValueVO(valueType value, String label) {
        super();
        this.value = value;
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public valueType getValue() {
        return value;
    }

    public void setValue(valueType value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return label;
    }
}
