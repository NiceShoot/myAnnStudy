package com.example.myannprocessor.demo;

import java.lang.annotation.*;

@Retention(value = RetentionPolicy.SOURCE)
@Documented
@Target({ElementType.METHOD})
public @interface MyAnnotation {

}
