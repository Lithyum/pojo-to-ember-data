package com.worldline.ember.converter;

import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;

import java.io.File;
import java.io.IOException;

import static com.google.common.base.Preconditions.checkArgument;

public class POJO2EmberData {

    private ClassPath classPath;
    private Config config;

    public POJO2EmberData(Config config) {

        checkArgument(config != null);

        try {
            this.classPath = ClassPath.from(ClassLoader.getSystemClassLoader());
        } catch (IOException e) {
            Throwables.propagate(e);
        }

        this.config = config;
    }

    public void convert() {

        createOutputDirectory();

        String[] packagesToScan = config.getPackagesToScan();
        for (String currentPackage : packagesToScan) {
            convert(currentPackage);
        }

    }

    private void convert(String currentPackage) {
        ImmutableSet<ClassPath.ClassInfo> classes = classPath.getTopLevelClasses(currentPackage);
        for (ClassPath.ClassInfo classInfo : classes) {

            Class clazz = classInfo.load();

            if (shouldIgnoreClass(clazz)) {
                continue;
            }

            ClassConverter classConverter = new ClassConverter(clazz, config);

            try {
                classConverter.convert();
            } catch (IOException e) {
                Throwables.propagate(e);
            }
        }
    }

    private boolean shouldIgnoreClass(Class clazz) {
        return clazz.isEnum();
    }

    private void createOutputDirectory() {
        String outputDir = config.getOutputDir();
        File dir = new File(outputDir);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                Throwables.propagate(new IOException("Could not create directory : " + outputDir));
            }
        }
    }
}