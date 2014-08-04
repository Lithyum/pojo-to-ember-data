package com.worldline.ember.converter;

import com.google.common.base.Function;
import com.google.common.base.Strings;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;

public class Config {

    public static final String EMBER_NAMESPACE = "DS";
    public static final String EMBER_MODEL_DEFAULT_SUPERCLASS = EMBER_NAMESPACE + ".Model";
    public static final String DEFAULT_OUTPUT_DIR = "generated_models";

    private String[] packagesToScan;
    private String emberModelNamespace;
    private String outputDir;
    private Boolean writeFileHeader = Boolean.TRUE;

    private Map<Field, String> customFieldNames = new HashMap<>();
    private Map<Class, String> customClassNames = new HashMap<>();
    private Map<Class, Function<EmberAttribute, EmberAttribute>> customFieldTypeMappings = new HashMap<>();
    private Map<Class, Function<EmberModel, EmberModel>> customModelMappings = new HashMap<>();

    public Config(String[] packagesToScan, String emberModelNamespace) {
        this(packagesToScan, emberModelNamespace, DEFAULT_OUTPUT_DIR);
    }

    public Config(String[] packagesToScan, String emberModelNamespace, String outputDir) {

        checkArgument(packagesToScan != null && packagesToScan.length > 0);
        checkArgument(!Strings.isNullOrEmpty(emberModelNamespace));
        checkArgument(!Strings.isNullOrEmpty(outputDir));

        this.packagesToScan = packagesToScan;
        this.emberModelNamespace = emberModelNamespace;
        this.outputDir = outputDir;
    }

    public void customFieldName(Class clazz, String fieldName, String customFieldName) throws NoSuchFieldException {

        checkArgument(clazz != null);
        checkArgument(!Strings.isNullOrEmpty(fieldName));

        Field field = clazz.getDeclaredField(fieldName);
        customFieldNames.put(field, customFieldName);
    }

    public void customClassName(Class clazz, String className) {

        checkArgument(clazz != null);
        checkArgument(!Strings.isNullOrEmpty(className));

        customClassNames.put(clazz, className);
    }

    public void customModelMapping(Class clazz, Function<EmberModel, EmberModel> replacement) {

        checkArgument(clazz != null);
        checkArgument(replacement != null);

        customModelMappings.put(clazz, replacement);
    }

    public void customAttributeMapping(Class fieldType, Function<EmberAttribute, EmberAttribute> replacement) {

        checkArgument(fieldType != null);
        checkArgument(replacement != null);

        customFieldTypeMappings.put(fieldType, replacement);
    }

    public String getCustomClassName(Class clazz, String defaultClassName) {
        if (customClassNames.containsKey(clazz)) {
            return customClassNames.get(clazz);
        } else {
            return defaultClassName;
        }
    }

    public String getCustomFieldName(Field field, String defaultFieldName) {
        if (customFieldNames.containsKey(field)) {
            return customFieldNames.get(field);
        } else {
            return defaultFieldName;
        }
    }

    public String[] getPackagesToScan() {
        return packagesToScan;
    }

    public String getEmberModelNamespace() {
        return emberModelNamespace;
    }

    public String getOutputDir() {
        return outputDir;
    }

    public Map<Field, String> getCustomFieldNames() {
        return customFieldNames;
    }

    public Map<Class, String> getCustomClassNames() {
        return customClassNames;
    }

    public Map<Class, Function<EmberAttribute, EmberAttribute>> getCustomFieldTypeMappings() {
        return customFieldTypeMappings;
    }

    public Map<Class, Function<EmberModel, EmberModel>> getCustomModelMappings() {
        return customModelMappings;
    }

    public void writeFileHeader(Boolean enable) {

        checkArgument(enable != null);

        writeFileHeader = enable;
    }

    public Boolean getWriteFileHeader() {
        return writeFileHeader;
    }
}
