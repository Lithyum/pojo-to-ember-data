package com.worldline.ember.converter;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Function;

import java.beans.Introspector;
import java.io.IOException;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

public class EmberAttribute {

    public enum AssociationType {
        ATTR("attr"), BELONGSTO("belongsTo"), HASMANY("hasMany");
        private final String value;

        AssociationType(String value) {
            this.value = value;
        }
    }

    public enum EmberType {
        ARRAY("array"), STRING("string"), BOOLEAN("boolean"), NUMBER("number"), DATE("date");

        private final String value;

        public String getValue() {
            return value;
        }

        EmberType(String value) {
            this.value = value;
        }
    }

    private String name;
    private AssociationType associationType;
    private String type;

    public EmberAttribute(String name, AssociationType associationType, String type) {
        this.name = name;
        this.associationType = associationType;
        this.type = type;
    }

    public void write(Writer writer) throws IOException {
        writer.append(name).append(": ").append(Config.EMBER_NAMESPACE).append(".").append(associationType.value)
                .append("('").append(type).append("')");
    }

    public String getName() {
        return name;
    }

    public AssociationType getAssociationType() {
        return associationType;
    }

    public String getType() {
        return type;
    }

    public static EmberAttribute fromField(Config config, Field field) {
        String attributeName = getEmberAttributeName(config, field);

        if(attributeName == null) {
            return null;
        }

        String type;
        EmberAttribute.AssociationType associationType;

        Class fieldType = field.getType();

        if (Collection.class.isAssignableFrom(fieldType) || fieldType.isArray()) {
            fieldType = ReflectionUtil.getComponentType(field);

            if (ReflectionUtil.isEmberDataType(fieldType)) {
                associationType = EmberAttribute.AssociationType.ATTR;
                type = EmberAttribute.EmberType.ARRAY.value;
            } else {
                associationType = EmberAttribute.AssociationType.HASMANY;
                type = Introspector.decapitalize(config.getCustomClassName(fieldType, fieldType.getSimpleName()));
            }

        } else if (ReflectionUtil.isAString(fieldType)) {
            associationType = EmberAttribute.AssociationType.ATTR;
            type = EmberAttribute.EmberType.STRING.value;
        } else if (ReflectionUtil.isANumber(fieldType)) {
            associationType = EmberAttribute.AssociationType.ATTR;
            type = EmberAttribute.EmberType.NUMBER.value;
        } else if (ReflectionUtil.isABoolean(fieldType)) {
            associationType = EmberAttribute.AssociationType.ATTR;
            type = EmberAttribute.EmberType.BOOLEAN.value;
        } else if (Date.class.isAssignableFrom(fieldType)) {
            associationType = EmberAttribute.AssociationType.ATTR;
            type = EmberAttribute.EmberType.DATE.value;
        } else {
            associationType = EmberAttribute.AssociationType.BELONGSTO;
            type = Introspector.decapitalize(config.getCustomClassName(fieldType, fieldType.getSimpleName()));
        }

        EmberAttribute emberAttribute = new EmberAttribute(attributeName, associationType, type);

        Map<Class, Function<EmberAttribute, EmberAttribute>> customFieldTypeMappings = config.getCustomFieldTypeMappings();
        if (customFieldTypeMappings.containsKey(fieldType)) {
            emberAttribute = customFieldTypeMappings.get(fieldType).apply(emberAttribute);
        }

        return emberAttribute;
    }

    private static String getEmberAttributeName(Config config, Field attr) {
        String attributeName = attr.getName();
        Annotation jsonProperty = ReflectionUtil.getAnnotation(attr, JsonProperty.class).orNull();
        if (jsonProperty != null && ((JsonProperty) jsonProperty).value() != null) {
            attributeName = ((JsonProperty) jsonProperty).value();
        }

        return config.getCustomFieldName(attr, attributeName);
    }
}
