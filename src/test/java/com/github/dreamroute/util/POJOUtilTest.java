package com.github.dreamroute.util;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import static com.github.dreamroute.util.POJOUtil.getAllFields;
import static com.github.dreamroute.util.POJOUtil.getAllGetterMethod;
import static com.github.dreamroute.util.POJOUtil.getAllSetterMethod;
import static com.github.dreamroute.util.POJOUtil.getAllSuperClass;
import static com.google.common.collect.Lists.newArrayList;
import static java.util.Comparator.comparing;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

class POJOUtilTest {

    @Data
    static class Parent {
        private Long id;
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    static class Sub extends Parent implements Serializable {
        private static final long serialVersionUID = -4356251564587313232L;
        private String name;
    }

    @Test
    void getAllSuperClassTest() {
        assertIterableEquals(newArrayList(), getAllSuperClass(null));
        assertIterableEquals(newArrayList(), getAllSuperClass(Parent.class));
        assertIterableEquals(newArrayList(Parent.class), getAllSuperClass(Sub.class));
    }

    @Test
    void getAllFieldsTest() {
        List<Field> fields = getAllFields(Sub.class);
        List<String> names = fields.stream().map(Field::getName).sorted(comparing(identity())).collect(toList());

        List<String> expected = newArrayList("id", "name");
        expected.sort(comparing(identity()));

        assertIterableEquals(expected, names);
    }

    @Test
    void getAllGetterMethodTest() {
        Map<String, Method> getterMethod = getAllGetterMethod(Sub.class);
        List<String> result = newArrayList(getterMethod.keySet());
        result.sort(comparing(identity()));

        List<String> expected = newArrayList("getId", "getName");
        expected.sort(comparing(identity()));

        assertIterableEquals(expected, result);
    }

    @Test
    void getAllSetterMethodTest() {
        Map<String, Method> setterMethod = getAllSetterMethod(Sub.class);
        List<String> result = newArrayList(setterMethod.keySet());
        result.sort(comparing(identity()));

        List<String> expected = newArrayList("setId", "setName");
        expected.sort(comparing(identity()));

        assertIterableEquals(expected, result);
    }
}

