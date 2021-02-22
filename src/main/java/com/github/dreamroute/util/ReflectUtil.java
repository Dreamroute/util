package com.github.dreamroute.util;

import lombok.SneakyThrows;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static com.google.common.collect.Sets.newHashSet;
import static java.util.Locale.ENGLISH;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

/**
 * 反射工具类，加入强引用缓存提高性能
 *
 * @author w.dehi
 */
public class ReflectUtil {
    private ReflectUtil() {}

    // Super interface cache
    private static final ConcurrentHashMap<Class<?>, Set<Class<?>>> SUPER_INTERFACE_CACHE = new ConcurrentHashMap<>();
    // Super class cache
    private static final ConcurrentHashMap<Class<?>, List<Class<?>>> SUPER_CLASS_CACHE = new ConcurrentHashMap<>();
    // Field cache
    private static final ConcurrentHashMap<Class<?>, Map<String, Field>> FIELD_CACHE = new ConcurrentHashMap<>();
    // Getter method cache
    private static final ConcurrentHashMap<Class<?>, Map<String, Method>> GETTER_CACHE = new ConcurrentHashMap<>();
    // Setter method cache
    private static final ConcurrentHashMap<Class<?>, Map<String, Method>> SETTER_CACHE = new ConcurrentHashMap<>();

    /**
     * 获取class的所有父接口
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

    /**
     * 获取class的所有父类，不包括Object.class，不包括自己
     */
    public static List<Class<?>> getAllSuperClass(Class<?> cls) {
        if (cls == null)
            return new ArrayList<>();

        return newArrayList(SUPER_CLASS_CACHE.computeIfAbsent(cls, key -> {
            final List<Class<?>> classes = new ArrayList<>();
            Class<?> superclass = key.getSuperclass();
            while (superclass != Object.class) {
                classes.add(superclass);
                superclass = superclass.getSuperclass();
            }
            return classes;
        }));
    }

    /**
     * 获取POJO的所有Field，包括父类
     */
    public static Map<String, Field> getAllFields(Class<?> cls) {
        if (cls == null)
            return newHashMap();

        return newHashMap(FIELD_CACHE.computeIfAbsent(cls, key -> {
            List<Class<?>> superClass = getAllSuperClass(key);
            superClass.add(key);
            List<String> setNames = getAllSetterMethod(key).keySet().stream().map(name -> name.substring(3, 4).toLowerCase(ENGLISH) + name.substring(4)).collect(toList());
            List<String> getNames = getAllGetterMethod(key).keySet().stream().map(name -> name.substring(3, 4).toLowerCase(ENGLISH) + name.substring(4)).collect(toList());
            return superClass.stream()
                    .map(Class::getDeclaredFields)
                    .flatMap(Arrays::stream)
                    .filter(field -> setNames.contains(field.getName()) && getNames.contains(field.getName()))
                    .collect(toMap(Field::getName, identity()));
        }));
    }

    /**
     * 获取所有Getter方法，包括父类
     */
    public static Map<String, Method> getAllGetterMethod(Class<?> cls) {
        if (cls == null)
            return new HashMap<>();

        return newHashMap(GETTER_CACHE.computeIfAbsent(cls, key -> {
                    List<Class<?>> superClass = getAllSuperClass(key);
                    superClass.add(key);
                    return superClass.stream()
                            .map(Class::getDeclaredMethods)
                            .flatMap(Arrays::stream)
                            .filter(m -> m.getName().startsWith("get") || m.getName().startsWith("is"))
                            .collect(toMap(Method::getName, identity()));
                }
        ));
    }

    /**
     * 获取所有Setter方法，包括父类
     */
    public static Map<String, Method> getAllSetterMethod(Class<?> cls) {
        if (cls == null)
            return new HashMap<>();

        return newHashMap(SETTER_CACHE.computeIfAbsent(cls, key -> {
                    List<Class<?>> superClass = getAllSuperClass(key);
                    superClass.add(key);
                    return superClass.stream()
                            .map(Class::getDeclaredMethods)
                            .flatMap(Arrays::stream)
                            .filter(m -> m.getName().startsWith("set"))
                            .collect(toMap(Method::getName, identity()));
                }
        ));
    }

    /**
     * 获取默认构造方法，如果不存在默认构造方法，那么抛出异常
     */
    public static Constructor<?> getDefaultConstructor(Class<?> cls) {
        Constructor<?>[] constructors = cls.getDeclaredConstructors();
        return Arrays.stream(constructors)
                .filter(constructor -> constructor.getParameterTypes().length == 0)
                .findAny().orElseThrow(() -> new RuntimeException(cls.getName() + "必须包含默认构造方法!"));
    }

    /**
     * 获取属性值
     */
    @SneakyThrows
    public static <T, V> V getFieldValue(T obj, String name) {
        Field field = getAllFields(obj.getClass()).get(name);
        if (!field.isAccessible())
            field.setAccessible(true);
        return (V) field.get(obj);
    }

    /**
     * 获取属性值
     */
    @SneakyThrows
    public static <T, V> V getFieldValue(T obj, Field field) {
        return getFieldValue(obj, field.getName());
    }

    /**
     * 设置属性值
     */
    @SneakyThrows
    public static <T, V> void setFieldValue(T obj, String fieldName, V value) {
        Field field = getAllFields(obj.getClass()).get(fieldName);
        if (!field.isAccessible())
            field.setAccessible(true);
        field.set(obj, value);
    }

    /**
     * 设置属性值
     */
    public static <T, V> void setFieldValue(T obj, Field field, V value) {
        setFieldValue(obj, field.getName(), value);
    }

}
