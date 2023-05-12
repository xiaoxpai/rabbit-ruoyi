package com.ruoyi.common.utils;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.lang.Nullable;

import java.util.Map;


/**
 * @author Alex
 * @date 2023年5月12日
 */
public class ObjectUtil {

    public static Map convert2Map(@Nullable Object obj) {
        return (Map) JSON.toJSON(obj);
    }

    public static boolean isNull(@Nullable Object obj) {
        return obj == null;
    }

    public static boolean isNotNull(@Nullable Object obj) {
        return obj != null;
    }

    /**
     * Copy the property values of the given source bean into the target bean.
     * <p>Note: The source and target classes do not have to match or even be derived
     * from each other, as long as the properties match. Any bean properties that the
     * source bean exposes but the target bean does not will silently be ignored.
     * <p>This is just a convenience method. For more complex transfer needs,
     * consider using a full BeanWrapper.
     * @param source the source bean
     * @param target the target bean
     */
    public static void copyProperties(Object source, Object target) {
        BeanUtils.copyProperties(source, target);
    }

    /**
     * Copy the property values of the given source bean into the given target bean,
     * only setting properties defined in the given "editable" class (or interface).
     * <p>Note: The source and target classes do not have to match or even be derived
     * from each other, as long as the properties match. Any bean properties that the
     * source bean exposes but the target bean does not will silently be ignored.
     * <p>This is just a convenience method. For more complex transfer needs,
     * consider using a full BeanWrapper.
     * @param source the source bean
     * @param target the target bean
     * @param editable the class (or interface) to restrict property setting to
     */
    public static void copyProperties(Object source, Object target, Class<?> editable) throws BeansException {
        BeanUtils.copyProperties(source, target, editable);
    }

    /**
     * 内部通过 equals 比较 obj1 与 obj2 的值是否相同
     *
     * <pre>
     *     ObjectUtil.equals(null, null) = true
     *     ObjectUtil.equals(null, obj2) = false
     *     ObjectUtil.equals(obj1, null) = false
     *     ObjectUtil.equals(obj1, obj2) = obj1.equals(obj2)
     * </pre>
     * @param obj1
     * @param obj2
     * @return
     */
    public static boolean equals(Object obj1, Object obj2) {
        if (obj1 == null && obj2 == null) {
            return true;
        }

        if (obj1 == null) {
            return false;
        }

        if (obj2 == null) {
            return false;
        }

        return obj1.equals(obj2);
    }

}
