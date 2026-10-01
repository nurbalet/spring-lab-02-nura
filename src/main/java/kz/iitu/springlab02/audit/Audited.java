package kz.iitu.springlab02.audit;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)   // без RUNTIME @annotation(audited) ничего не найдёт
@Documented
public @interface Audited {
    String action();
    boolean logArguments() default false;
}