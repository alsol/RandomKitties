package services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

/**
 * @author alexander.solovyov
 * @since 13.07.17
 */
public class StaticContext {

    private static final HashMap<Class, Object> instanceMap = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(StaticContext.class);

    public static <BEAN> BEAN getBean(final Class<BEAN> beanClass) {
        if (!instanceMap.containsKey(beanClass)) {
            try {
                Constructor<BEAN> constructor = (Constructor<BEAN>) beanClass.getDeclaredConstructors()[0];
                constructor.setAccessible(true);
                instanceMap.put(beanClass, constructor.newInstance());
            } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
                log.error("Failed to instantiate object of class " + beanClass.getSimpleName());
                throw new RuntimeException(e);
            }
        }
        return (BEAN) instanceMap.get(beanClass);
    }

}
