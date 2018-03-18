package com.novinet.catdog;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

public class ClassificationLabel {
    private String label;
    private double networkOutput;

    public ClassificationLabel(String label, double networkOutput) {
        this.label = label;
        this.networkOutput = networkOutput;
    }

    public double getNetworkOutput() {
        return networkOutput;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public boolean equals(Object obj) {
        return reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return reflectionToString(this);
    }
}
