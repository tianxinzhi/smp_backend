package com.pccw.backend.util;

import java.util.List;
import java.util.Map;

/**
 * @desc  map/list链式添加
 * @author liujie
 * @date 2020/1/16
 */
public class CollectionBuilder {

    /**
     * map的build方法
     *
     * @param map
     * @return
     * @date 2020/1/16
     */
    public static <K, V> MapBuilder<K, V> builder(Map<K, V> map) {
        return new MapBuilder<>(map);
    }

    /**
     * list的build方法
     *
     * @param list
     * @return
     * @date 2020/1/16
     */
    public static <V> ListBuilder<V> builder(List<V> list) {
        return new ListBuilder<>(list);
    }

    public static class MapBuilder<K, V> {

        private Map<K, V> map;

        private MapBuilder(Map<K, V> map) {
            this.map = map;
        }

        public MapBuilder<K, V> put(K k, V v) {
            map.put(k, v);
            return this;
        }
        public Map<K, V> build() {
            return map;
        }
    }

    public static class ListBuilder<V> {

        private List<V> list;

        private ListBuilder(List<V> list) {
            this.list = list;
        }

        public ListBuilder<V> add(V v) {
            list.add(v);
            return this;
        }
        public List<V> build() {
            return list;
        }
    }

}
