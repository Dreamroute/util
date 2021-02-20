package com.github.dreamroute.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static com.google.common.collect.Sets.newHashSet;

/**
 * Class工具类
 */
public class ClassUtil {
    private ClassUtil() {}

    private static final ConcurrentHashMap<Class<?>, Set<Class<?>>> SUPER_INTERFACE_CACHE = new ConcurrentHashMap<>();

    /**
     * 获取cls的所有父接口
     */
    public static Set<Class<?>> getAllSuperInterface(Class<?> cls) {
        return newHashSet(SUPER_INTERFACE_CACHE.computeIfAbsent(cls, key -> {
            Set<Class<?>> result = new HashSet<>();
            recursiveCls(cls, result);
            return result;
        }));
    }

    private static void recursiveCls(Class<?> cls, Set<Class<?>> result) {
        Class<?>[] interfaces = cls.getInterfaces();
        if (interfaces.length > 0) {
            result.addAll(Arrays.asList(interfaces));
            Arrays.stream(interfaces).forEach(inter -> recursiveCls(inter, result));
        }
    }
}
