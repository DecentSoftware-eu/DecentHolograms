package eu.decentsoftware.holograms.api.utils.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Key {

    String value();

    double min() default - Double.MAX_VALUE;

    double max() default Double.MAX_VALUE;

}
