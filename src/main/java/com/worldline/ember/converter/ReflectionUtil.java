package com.worldline.ember.converter;

import com.google.common.base.Optional;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.WildcardType;
import java.util.Date;

public class ReflectionUtil {

    /**
     * Returns this field's + getter/setters annotation for the specified type if
     * such an annotation is present, else null.
     *
     * @return Null if we couldn't read the getter/setters. Optional otherwise.
     */
    public static Optional<Annotation> getAnnotation(Field field, Class<? extends Annotation> annotationClass) {

        if (field.getAnnotation(annotationClass) != null) {
            return Optional.fromNullable(field.getAnnotation(annotationClass));
        }

        Class declaringClass = field.getDeclaringClass();
        PropertyDescriptor propertyDescriptor;
        try {
            propertyDescriptor = new PropertyDescriptor(field.getName(), declaringClass);
        } catch (IntrospectionException e) {
            return null;
        }

        Method getter = propertyDescriptor.getReadMethod();
        if (getter != null) {
            if (getter.getAnnotation(annotationClass) != null) {
                return Optional.fromNullable(getter.getAnnotation(annotationClass));
            }
        }

        Method setter = propertyDescriptor.getWriteMethod();
        if (setter != null) {
            if (setter.getAnnotation(annotationClass) != null) {
                return Optional.fromNullable(setter.getAnnotation(annotationClass));
            }
        }

        return Optional.absent();
    }

    /**
     * Returns the component type of an array or the first generic type of a generic class.
     * eg. String[] returns String, List<Int> returns Int
     */
    public static Class getComponentType(Field attr) {

        Class fieldType = attr.getType();
        if (fieldType.isArray()) {
            return fieldType.getComponentType();
        }

        ParameterizedType stringListType = (ParameterizedType) attr.getGenericType();
        Object genericType = stringListType.getActualTypeArguments()[0];
        Class genericClass;

        if (genericType instanceof WildcardType) {
            WildcardType wildcardType = (WildcardType) genericType;
            if (wildcardType.getLowerBounds().length > 0) {
                genericClass = (Class) wildcardType.getLowerBounds()[0];
            } else {
                genericClass = (Class) wildcardType.getUpperBounds()[0];
            }
        } else {
            genericClass = (Class) genericType;
        }
        return genericClass;
    }

    public static boolean isEmberDataType(Class fieldType) {
        return isAString(fieldType) || isANumber(fieldType) || isABoolean(fieldType)
                || Date.class.isAssignableFrom(fieldType) || fieldType.isArray();
    }

    public static boolean isAString(Class attrClazz) {
        return CharSequence.class.isAssignableFrom(attrClazz) || attrClazz == char.class || attrClazz.isEnum();
    }

    public static boolean isABoolean(Class fieldType) {
        return Boolean.class.isAssignableFrom(fieldType) || fieldType == boolean.class;
    }

    public static boolean isANumber(Class fieldType) {
        return Number.class.isAssignableFrom(fieldType) ||
                fieldType == byte.class || fieldType == short.class ||
                fieldType == int.class || fieldType == long.class ||
                fieldType == double.class || fieldType == float.class;
    }
}
