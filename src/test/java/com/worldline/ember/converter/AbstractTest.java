package com.worldline.ember.converter;

import com.worldline.ember.converter.util.FileUtils;

import java.io.File;

import static org.testng.AssertJUnit.assertTrue;

public abstract class AbstractTest {

    protected static final String TEMP_OUTPUT_DIR = "tmp_tests_output";
    protected static final String EMBER_MODEL_NAMESPACE = "App";
    protected static final String[] PACKAGES_TO_SCAN = {"com.worldline.ember.converter.models"};

    protected void compareModelFiles(Class<?> testClass, String fileName) {
        File generated = new File(TEMP_OUTPUT_DIR + '/' + fileName);
        assertTrue(generated.exists());

        File expected = new File(getClass().getClassLoader().getResource(testClass.getSimpleName() + '/' + fileName).getFile());

        assertTrue(FileUtils.areIdentical(generated, expected));
    }

}
