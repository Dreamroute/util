package com.github.dreamroute.util;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;

import static com.github.dreamroute.util.POJOUtil.getAllFields;
import static com.github.dreamroute.util.POJOUtil.getAllSuperClass;
import static com.google.common.collect.Lists.newArrayList;
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

    // TODO 还需要重写getAllFields
    @Test
    void getAllFieldsTest() {
        List<Field> fields = getAllFields(Sub.class);
        System.err.println(fields);
    }
}

