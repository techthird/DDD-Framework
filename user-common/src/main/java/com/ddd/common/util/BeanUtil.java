package com.ddd.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.beans.BeanCopier;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Bean对象复制工具类
 *
 */
@Slf4j
public class BeanUtil {


    /**
     * 对source进行拷贝，如果遇到属性相同，则进行浅拷贝
     *
     * @param source
     * @param target
     * @param <K>
     * @param <T>
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static <K, T> T copy(K source, Class<T> target) {
        if(Objects.isNull(source) || Objects.isNull(target)) {
            return null;
        }
        BeanCopier copier = BeanCopier.create(source.getClass(), target, false);
        try {
            T res = target.newInstance();
            copier.copy(source, res, null);
            return res;
        } catch (Exception e) {
            log.error("Bean Copy error:", e);
        }
        return null;
    }


    /**
     * 对集合进行拷贝
     *
     * @param srcList
     * @param clz
     * @param <T>
     * @param <V>
     * @return
     */
    public static <T, V> List<T> copyList(List<V> srcList, Class<T> clz) {
        if(Objects.isNull(srcList) || Objects.isNull(clz)) {
            return null;
        }
        return copyList(srcList, var -> copy(var, clz));
    }

    /**
     *
     * @param srcList
     * @param mapper
     * @return
     * @param <T>
     * @param <V>
     */
    private static <T, V> List<T> copyList(List<V> srcList, Function<? super V, ? extends T> mapper) {
        if(Objects.isNull(srcList) || Objects.isNull(mapper)) {
            return null;
        }
        if (srcList != null && srcList.size() > 0) {
            return srcList.stream().map(mapper::apply).collect(Collectors.toList());
        }
        return null;
    }


    /**
     * 获取指定字段的值（通过PropertyDescriptor）
     */
    public static <T> T getFieldValueByProperty(Object obj, String fieldName) {
      try {
          PropertyDescriptor pd = new PropertyDescriptor(fieldName, obj.getClass());
          Method readMethod = pd.getReadMethod();
          if (readMethod != null) {
              return (T) readMethod.invoke(obj);
          }
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
      return null;
    }

}
