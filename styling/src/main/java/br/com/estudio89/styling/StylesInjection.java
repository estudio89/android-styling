package br.com.estudio89.styling;

import android.content.Context;

import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by luccascorrea on 10/1/15.
 */
public class StylesInjection {

    private static HashMap<Class, Object> graph = new HashMap<Class, Object>();

    public static boolean init(Context context, InputStream stylesFile, InputStream colorsFile, InputStream viewStylesFile, String applicationPackage) {
        return executeInjection(context, stylesFile, colorsFile, viewStylesFile, applicationPackage);
    }

    private static boolean executeInjection(Context context, InputStream stylesFile, InputStream colorsFile, InputStream viewStylesFile, String applicationPackage) {

        StylesCacheManager stylesCacheManager = new StylesCacheManager(context);
        boolean hasUndefinedVariables = stylesCacheManager.setupProcessor(stylesFile, colorsFile, viewStylesFile);
        StylesProcessor processor = stylesCacheManager.getStylesProcessor();

        StylesManager manager = new StylesManager(processor, applicationPackage);
        StylesRenderer renderer = new StylesRenderer(manager);

        graph.put(StylesProcessor.class, processor);
        graph.put(StylesManager.class, manager);
        graph.put(StylesRenderer.class, renderer);

        return hasUndefinedVariables;
    }

    public static <E> E get(Class<?> klass) {
        E item = (E) graph.get(klass);

        if (item == null && klass == StylesRenderer.class) {
            return (E) new NullStylesRenderer(null);
        }
        return item;
    }
}
