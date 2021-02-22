package com.github.dreamroute.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Optional.ofNullable;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

/**
 * 树结构转换工具类
 *
 * <pre>
 * 1.如果有如下类：
 * public class Node {
 *     private Long id;
 *     private Long parentId;
 *     private Number nodeOrder;
 *     private List<Node> children;
 * }
 * 2.调用buildTree方法，会返回一颗多叉树
 * 3.调用sort方法，传入一棵树，返回排好序的树
 *
 * </pre>
 *
 * @author w.dehai
 */

public class TreeUtil {

    private TreeUtil() {}

    /**
     * 将节点列表构建成树形结构
     *
     * @param nodes 节点列表
     * @param idName 节点id名称，例如：id
     * @param parentIdName 父节点id名称，例如：parentId
     * @param childrenName 子节点列表名称，例如：children
     * @param isSort 是否排序，如果此处是false（也就是不需要排序），那么参数sortName就不生效，传入任何值都不影响
     * @return 返回树结构
     */
    public static <T> List<T> buildTree(List<T> nodes, String idName, String parentIdName, String childrenName, boolean isSort, String sortName) {

        // 将List转成map，map的key是id，value是node
        Map<Long, T> resources = ofNullable(nodes).orElseGet(ArrayList::new)
                .stream()
                .collect(toMap(node -> ReflectUtil.getFieldValue(node, idName), identity()));

        List<T> result = new ArrayList<>();
        resources.forEach((id, node) -> {
            Long parentId = ReflectUtil.getFieldValue(node, parentIdName);
            if (parentId == null || parentId == 0L) {
                result.add(node);
            } else {
                T parent = resources.get(parentId);
                List<T> children = ReflectUtil.getFieldValue(parent, childrenName);
                if (children == null) {
                    children = new ArrayList<>();
                    ReflectUtil.setFieldValue(parent, childrenName, children);
                }
                children.add(node);
            }
        });

        if (isSort)
            sortTree(result, sortName, childrenName);

        return result;
    }

    /**
     * 对tree进行排序
     */
    private static <T> void sortTree(List<T> list, String sortName, String childrenName) {
        list.sort((o1, o2) -> {
            Integer v1 = ReflectUtil.getFieldValue(o1, sortName);
            Integer v2 = ReflectUtil.getFieldValue(o2, sortName);
            return v1 - v2;
        });
        list.forEach(node -> {
            List<T> children = ReflectUtil.getFieldValue(node, childrenName);
            if (children != null && !children.isEmpty()) {
                sortTree(children, sortName, childrenName);
            }
        });
    }
}
