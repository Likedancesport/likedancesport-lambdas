package com.likedancesport.common.utils.collection;

import com.likedancesport.common.model.IOrderableEntity;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class CollectionUtils {
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * Sets order of the entity equal to its index in the list
     * @param orderables
     * @param <T>
     */
    public static <T extends IOrderableEntity> void orderByIndex(List<T> orderables) {
        orderables.forEach(orderable -> {
            int index = orderables.indexOf(orderable);
            if (orderable.getOrder() == index) {
                return;
            }
            orderable.setOrder(index);
        });
    }

    /**
     * Sets entity order according to ordering map
     * @param orderables list of entities
     * @param orderingMap map of Ids to Ordering Numbers of entities
     */
    public static <T extends IOrderableEntity> void reorderByMap(List<T> orderables, Map<Long, Integer> orderingMap) {
        if (orderingMap.size() - orderables.size() != 0) {
            throw new IllegalArgumentException("Invalid ordering");
        }
        orderables.forEach(orderable -> orderable.setOrder(orderingMap.get(orderable.getId())));
    }
}
