package com.moka.resources;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface BindLoad
{
    /**
     * Path for the files to load. It is relative to the root and any other
     * outer class with the {@link BindLoad} annotation.
     */
    String path();

    /**
     * Extension for the files to load.
     */
    String extension();

    /**
     * Does not load the resources, useful when dependencies are needed, but you still want to
     * use the resource automated system.
     */
    boolean skip() default false;

    /**
     * Delays the load to after everything is loaded.
     */
    boolean delay() default false;
}
