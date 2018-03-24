package com.novinet.catdog;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClassificationServiceTest {
    private ClassificationService classificationService;

    @Test
    public void classifyCanCorrectlyClassify32by32Image() throws IOException {
        this.classificationService = new ClassificationService();
        ClassificationResult classificationResult = classificationService.classify(this.getClass().getResourceAsStream("/test-cat.jpg"), this.getClass().getResourceAsStream("/naivenetwork_test.eg"));
        assertEquals(0.6032417038996352d, classificationResult.getCertainty());
        assertEquals("cat", classificationResult.getClassificationLabel().getLabel());
    }
}
