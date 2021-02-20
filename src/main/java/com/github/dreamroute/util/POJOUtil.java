package com.github.dreamroute.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

/**
 * 此工具类本质上是一个反射工具类，与普通的反射工具类的区别在于此类只处理POJO类型的类，
 * POJO一般意义上的规范：1、属性都是private的；2、属性都有一对get/set方法；3、有一个公共的无参构造方法
 *
 * @author w.dehi
 */
public class POJOUtil {
    private POJOUtil() {}

    // Field cache
    private static final ConcurrentHashMap<Class<?>, List<Field>> FIELD_CACHE = new ConcurrentHashMap<>();
    // Super class cache
    private static final ConcurrentHashMap<Class<?>, List<Class<?>>> SUPER_CLASS_CACHE = new ConcurrentHashMap<>();
    // Getter method cache
    private static final ConcurrentHashMap<Class<?>, Map<String, Method>> GETTER_CACHE = new ConcurrentHashMap<>();

    /**
     * 获取POJO的所有父类，不包括Object.class，不包括自己
     */
    public static List<Class<?>> getAllSuperClass(Class<?> cls) {
        if (cls == null)
            return new ArrayList<>();

        return SUPER_CLASS_CACHE.computeIfAbsent(cls, key -> {
            final List<Class<?>> classes = new ArrayList<>();
            Class<?> superclass = key.getSuperclass();
            while (superclass != Object.class) {
                classes.add(superclass);
                superclass = superclass.getSuperclass();
            }
            return classes;
        });
    }

    /**
     * 获取POJO的所有Field，包括父类
     */
    public static List<Field> getAllFields(Class<?> cls) {
        if (cls == null)
            return new ArrayList<>();

        return FIELD_CACHE.computeIfAbsent(cls, key -> {
            List<Class<?>> superClass = getAllSuperClass(key);
            superClass.add(key);
            return superClass.stream()
                    .map(Class::getDeclaredFields)
                    .flatMap(Arrays::stream)
                    .filter(field -> !field.getName().startsWith("serialVersionUID"))
                    .collect(toList());
        });
    }

    /**
     * 获取所有Getter方法，包括父类
     */
    public static Map<String, Method> getAllGetterMethod(Class<?> cls) {
        if (cls == null)
            return new HashMap<>();

        return GETTER_CACHE.computeIfAbsent(cls, key -> {
                    List<Class<?>> superClass = getAllSuperClass(key);
                    superClass.add(key);
                    return superClass.stream()
                            .map(Class::getDeclaredMethods)
                            .flatMap(Arrays::stream)
                            .filter(m -> m.getName().startsWith("get") || m.getName().startsWith("is"))
                            .collect(toMap(Method::getName, identity()));
                }
        );
    }
}
