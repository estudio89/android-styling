package br.com.estudio89.styling;

import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by luccascorrea on 10/1/15.
 */
public class StylesInjection {
    private static HashMap<Class, Object> graph = new HashMap<Class, Object>();

    public static boolean init(InputStream stylesFile, InputStream colorsFile, InputStream viewStylesFile, String applicationPackage) {
        return executeInjection(stylesFile, colorsFile, viewStylesFile, applicationPackage);
    }

    private static boolean executeInjection(InputStream stylesFile, InputStream colorsFile, InputStream viewStylesFile, String applicationPackage) {
        StylesProcessor processor = new StylesProcessor(stylesFile, colorsFile, viewStylesFile);
        boolean hasUndefinedVariables = processor.fullProcess();

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
