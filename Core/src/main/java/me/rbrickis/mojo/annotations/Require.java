package me.rbrickis.mojo.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Require {
    /**
     * @return The permission required to perform the command
     */
    String value();

    /**
     * Optional: The permission message.
     */
    String message() default "";
}
