package com.worldline.ember.converter;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Function;
import com.google.common.base.Optional;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ClassConverter {

    private Class clazz;
    private Config config;

    public ClassConverter(Class clazz, Config config) {
        this.clazz = clazz;
        this.config = config;
    }

    public void convert() throws IOException {

        String className = config.getCustomClassName(clazz, clazz.getSimpleName());
        String classNameWithNamespace = config.getEmberModelNamespace() + '.' + className;

        String superClassNameWithNamespace = getSuperClassName();
        List<EmberAttribute> emberAttributes = createEmberAttributes();

        EmberModel emberModel = new EmberModel(classNameWithNamespace, superClassNameWithNamespace,
                emberAttributes, config.getWriteFileHeader());

        Map<Class, Function<EmberModel, EmberModel>> customModelMappings = config.getCustomModelMappings();
        if (customModelMappings.containsKey(clazz)) {
            emberModel = customModelMappings.get(clazz).apply(emberModel);
        }

        File modelFile = createModelFile(className);
        FileWriter fileWriter = new FileWriter(modelFile);
        BufferedWriter writer = new BufferedWriter(fileWriter);
        emberModel.write(writer);
    }

    private List<EmberAttribute> createEmberAttributes() {
        Field[] fields = clazz.getDeclaredFields();
        List<EmberAttribute> emberAttributes = new ArrayList<>(fields.length);

        for (Field field : fields) {
            if (!shouldIgnoreField(field)) {
                EmberAttribute emberAttribute = EmberAttribute.fromField(config, field);
                if(emberAttribute != null) {
                    emberAttributes.add(emberAttribute);
                }
            }
        }
        return emberAttributes;
    }

    private String getSuperClassName() {
        String superClassStr;
        Class superClass = clazz.getSuperclass();
        if (superClass != null && isInPackagesToScan(superClass)) {
            superClassStr = config.getEmberModelNamespace() + "." +
                    config.getCustomClassName(superClass, superClass.getSimpleName());
        } else {
            superClassStr = Config.EMBER_MODEL_DEFAULT_SUPERCLASS;
        }
        return superClassStr;
    }

    private File createModelFile(String modelName) throws IOException {
        File modelFile = new File(config.getOutputDir(), modelName.toLowerCase() + ".js");
        if (!modelFile.exists()) {
            if (!modelFile.createNewFile())
                throw new IOException("Could not create model file " + modelName.toLowerCase() + ".js");
        }
        return modelFile;
    }

    private boolean shouldIgnoreField(Field field) {
        Class fieldType = field.getType();

        Optional<Annotation> jsonIgnore = ReflectionUtil.getAnnotation(field, JsonIgnore.class);
        Optional<Annotation> jsonBackRef = ReflectionUtil.getAnnotation(field, JsonBackReference.class);
        if (jsonIgnore == null || jsonBackRef == null) {
            return true;
        } else if (jsonIgnore.isPresent() || jsonBackRef.isPresent()) {
            return true;
        }

        if (field.getName().equals("id")) {
            return true;
        }

        if (Collection.class.isAssignableFrom(fieldType)) {
            fieldType = ReflectionUtil.getComponentType(field);
        }

        if (!ReflectionUtil.isEmberDataType(fieldType) && !isInPackagesToScan(fieldType)) {
            return true;
        }

        return false;

    }

    private boolean isInPackagesToScan(Class fieldType) {
        boolean inPackages = false;
        for (String pck : config.getPackagesToScan()) {
            if (fieldType.getPackage().getName().equals(pck)) {
                inPackages = true;
            }
        }
        return inPackages;
    }

}