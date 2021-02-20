package com.github.dreamroute.util;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static com.github.dreamroute.util.ClassUtil.getAllSuperInterface;
import static com.google.common.collect.Lists.newArrayList;

class ClassUtilTest {

    interface X {}
    interface A extends X {}
    interface B {}
    interface C extends A, B {}
    interface D extends C {}

    @Test
    void getAllSuperInterfaceTest() {
        Set<Class<?>> superInterface = getAllSuperInterface(D.class);
        List<Class<?>> result = newArrayList(superInterface);
        List<Class<?>> expected = newArrayList(X.class, A.class, B.class, C.class);
    }

}
