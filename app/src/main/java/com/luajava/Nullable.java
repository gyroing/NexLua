package com.luajava;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 表示该元素（返回值、参数、字段或类型）可以为 null。
 */
@Retention(RetentionPolicy.CLASS)
// 新增 ElementType.TYPE_USE
@Target({
        ElementType.METHOD,
        ElementType.PARAMETER,
        ElementType.FIELD,
        ElementType.TYPE_USE
})
public @interface Nullable {}