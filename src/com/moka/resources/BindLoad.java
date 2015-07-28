package com.moka.resources;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface BindLoad
{
    enum Loader
    {
        TEXTURE,
        PREFAB,
    }

    boolean skip() default false;
    String path();
    String extension();
    Loader loader();
}
