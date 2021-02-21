package com.github.dreamroute.util;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.github.dreamroute.util.ReflectUtil.getAllFields;
import static com.github.dreamroute.util.ReflectUtil.getAllGetterMethod;
import static com.github.dreamroute.util.ReflectUtil.getAllSetterMethod;
import static com.github.dreamroute.util.ReflectUtil.getAllSuperClass;
import static com.github.dreamroute.util.ReflectUtil.getAllSuperInterface;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.difference;
import static com.google.common.collect.Sets.newHashSet;
import static java.util.Comparator.comparing;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

class ReflectUtilTest {

    interface X {}
    interface A extends X {}
    interface B {}
    interface C extends A, B {}
    interface D extends C {}

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
    void getAllSuperInterfaceTest() {
        Set<Class<?>> superInterface = getAllSuperInterface(D.class);
        Set<Class<?>> expected = newHashSet(X.class, A.class, B.class, C.class);
        Set<Class<?>> difference = difference(superInterface, expected);
        assertEquals(0, difference.size());
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
        List<String> names = fields.stream().map(Field::getName).collect(toList());

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
