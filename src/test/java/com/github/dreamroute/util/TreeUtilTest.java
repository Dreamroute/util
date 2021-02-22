package com.github.dreamroute.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.alibaba.fastjson.JSON.toJSONString;
import static com.github.dreamroute.util.TreeUtil.buildTree;
import static com.google.common.collect.Lists.newArrayList;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

class TreeUtilTest {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    static class Node {
        private Long id;
        private Long parentId;
        private Number order;
        private List<Node> children;
    }

    /**
     *                  1
     *               /    \
     *              3     2
     *            /|\    / \
     *          7 6 8 5  4
     */
    @Test
    void buildTreeTest() {

        List<Node> list = newArrayList(
                // root
                Node.builder().id(1L).parentId(0L).order(1).build(),

                // lv1
                Node.builder().id(2L).parentId(1L).order(2).build(),
                Node.builder().id(3L).parentId(1L).order(1).build(),

                // lv2
                Node.builder().id(4L).parentId(2L).order(2).build(),
                Node.builder().id(5L).parentId(2L).order(1).build(),

                Node.builder().id(6L).parentId(3L).order(2).build(),
                Node.builder().id(7L).parentId(3L).order(1).build(),
                Node.builder().id(8L).parentId(3L).order(3).build()
        );

        Node result = buildTree(list, "id", "parentId", "children", true, "order");
        System.err.println(toJSONString(result, true));

        List<Long> ids = newArrayList(
                result.getId(),

                result.getChildren().get(0).getId(),
                result.getChildren().get(1).getId(),

                result.getChildren().get(0).getChildren().get(0).getId(),
                result.getChildren().get(0).getChildren().get(1).getId(),
                result.getChildren().get(0).getChildren().get(2).getId(),

                result.getChildren().get(1).getChildren().get(0).getId(),
                result.getChildren().get(1).getChildren().get(1).getId()
        );

        assertIterableEquals(newArrayList(1L, 3L, 2L, 7L, 6L, 8L, 5L, 4L), ids);

    }
}
