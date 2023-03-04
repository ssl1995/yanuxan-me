package com.msb.framework.common.utils;

import com.msb.framework.common.model.TreeNode;
import lombok.experimental.UtilityClass;
import org.apache.commons.collections4.CollectionUtils;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * List工具类
 *
 * @author luozhan
 */
@UtilityClass
public class ListUtil {
    /**
     * 匹配两个list集合相同元素，并执行匹配后的动作
     * 适用于两个集合泛型不一致
     *
     * @param list1    第一个集合
     * @param list2    第二个集合
     * @param field1   第一个集合中判定元素相同的属性，如：SomeBean::getId
     * @param field2   第二个集合中判定元素相同的属性，如：OtherBean::getId
     * @param operator 匹配后执行的动作，入参为A、B集合中的相同元素，如：(a, b)->a.setXx(b.getXx())
     * @param <T>      第一个集合的泛型
     * @param <K>      第二个集合的泛型
     */
    public static <T, K> void match(Collection<T> list1, Collection<K> list2, Function<T, ?> field1, Function<K, ?> field2, BiConsumer<T, K> operator) {
        Map<?, K> map = list2.stream().collect(Collectors.toMap(field2, Function.identity()));
        list1.forEach(item1 -> Optional.ofNullable(map.get(field1.apply(item1))).ifPresent(item2 -> operator.accept(item1, item2)));
    }

    /**
     * 匹配两个list集合中的相同元素，并执行匹配后的动作
     * 适用于两个集合的泛型一致
     *
     * @param list1      第一个集合
     * @param list2      第二个集合
     * @param equalField 判定元素相同的属性，如Student::getId
     * @param operator   匹配后执行的动作，入参为两个集合中的相同元素，如：(a, b)->a.setXx(b.getXx())
     * @param <T>        集合泛型
     */
    public static <T> void match(Collection<T> list1, Collection<T> list2, Function<T, ?> equalField, BiConsumer<T, T> operator) {
        match(list1, list2, equalField, equalField, operator);
    }

    /**
     * 转换List
     *
     * @param list      原集合
     * @param converter 转换函数
     * @return 转换后的函数
     */
    public static <T, R> List<R> convert(List<T> list, Function<T, R> converter) {
        return list.stream().map(converter).collect(Collectors.toList());
    }

    /**
     * 转换List，转换之后去重
     *
     * @param list      原集合
     * @param converter 转换函数
     * @return 转换后的函数
     */
    public static <T, R> List<R> convertDistinct(List<T> list, Function<T, R> converter) {
        return list.stream().map(converter).distinct().collect(Collectors.toList());
    }

    /**
     * List转为树
     *
     * @param source list
     * @param rootId 指定树的根节点id，一般为0
     * @return 树化后的List
     */
    public static <T extends TreeNode<T>> List<T> toTree(List<T> source, Long rootId) {

        if (CollectionUtils.isEmpty(source)) {
            return Collections.emptyList();
        }
        List<T> result = new ArrayList<>();
        Map<Object, T> map = new HashMap<>(source.size());
        source.forEach(node -> {
            if (Objects.equals(rootId, node.getParentId())) {
                result.add(node);
            }
            map.put(node.getId(), node);
        });
        source.forEach(node -> map.computeIfPresent(node.getParentId(), (parentId, parentNode) -> {
            Optional.ofNullable(parentNode.getChildList()).orElseGet(() -> {
                List<T> list = new ArrayList<>();
                parentNode.setChildList(list);
                return list;
            }).add(node);
            return parentNode;
        }));
        return result;
    }

    /**
     * List转为Map
     * <p>
     * - map的key通过指定函数生成，value为list中的元素
     * - 如果List中含有相同元素，转成map后会只留一个
     *
     * @param source       list
     * @param keyGenerator map中key的生成函数
     * @return map
     */
    public static <K, V> Map<K, V> toMap(List<V> source, Function<V, K> keyGenerator) {
        return source.stream().collect(Collectors.toMap(keyGenerator, v -> v, (v1, v2) -> v2, () -> new HashMap<>((int) (source.size() / 0.75))));
    }

    /**
     * 排序，值为空的排在最后
     *
     * @param source
     * @param keyExtractor
     * @param <T>
     * @param <U>
     */
    public static <T, U extends Comparable<U>> void sort(List<T> source, Function<? super T, ? extends U> keyExtractor) {
        Comparator<T> comparing = Comparator.nullsLast(Comparator.comparing(keyExtractor));
        source.sort(comparing);
    }
}
