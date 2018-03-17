package com.novinet.catdog;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

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
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
