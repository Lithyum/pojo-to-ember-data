package com.worldline.ember.converter;

import com.worldline.ember.converter.models.User;
import com.worldline.ember.converter.util.FileUtils;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

public class TestCustomClassName extends AbstractTest {

    protected POJO2EmberData POJO2EmberData;

    @BeforeMethod
    protected void doInitialization() {
        Config config = new Config(PACKAGES_TO_SCAN, EMBER_MODEL_NAMESPACE, TEMP_OUTPUT_DIR);
        config.writeFileHeader(false);
        config.customClassName(User.class, "Customer");
        this.POJO2EmberData = new POJO2EmberData(config);
    }

    @AfterMethod
    protected void doCleanup() throws IOException {
        File dir = new File(TEMP_OUTPUT_DIR);
        FileUtils.deleteDirectory(dir);
    }

    @Test
    public void testClassCustomName() {
        this.POJO2EmberData.convert();

        compareModelFiles(getClass(), "administrator.js");
        compareModelFiles(getClass(), "group.js");
        compareModelFiles(getClass(), "customer.js");
    }

}
