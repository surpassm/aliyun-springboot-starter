package com.github.surpassm.aliyun;


import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author AOC
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({AliyunConfiguration.class})
public @interface EnableAliyun {
}
