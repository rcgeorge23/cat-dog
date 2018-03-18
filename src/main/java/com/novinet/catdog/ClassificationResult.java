package com.novinet.catdog;

import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

public class ClassificationResult {

    private double certainty;
    private ClassificationLabel classificationLabel;

    public ClassificationResult(double certainty, ClassificationLabel classificationLabel) {
        this.certainty = certainty;
        this.classificationLabel = classificationLabel;
    }

    public double getCertainty() {
        return certainty;
    }

    public ClassificationLabel getClassificationLabel() {
        return classificationLabel;
    }

    @Override
    public String toString() {
        return reflectionToString(this);
    }
}
