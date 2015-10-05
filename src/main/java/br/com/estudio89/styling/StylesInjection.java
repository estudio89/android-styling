package br.com.estudio89.styling;

import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by luccascorrea on 10/1/15.
 */
public class StylesInjection {
    private static HashMap<Class, Object> graph = new HashMap<Class, Object>();

    public static void init(InputStream stylesFile, InputStream colorsFile, InputStream viewStylesFile) {
        executeInjection(stylesFile, colorsFile, viewStylesFile);
    }

    private static void executeInjection(InputStream stylesFile, InputStream colorsFile, InputStream viewStylesFile) {
        StylesProcessor processor = new StylesProcessor(stylesFile, colorsFile, viewStylesFile);
        processor.fullProcess();

        StylesManager manager = new StylesManager(processor);
        StylesRenderer renderer = new StylesRenderer(manager);

        graph.put(StylesProcessor.class, processor);
        graph.put(StylesManager.class, manager);
        graph.put(StylesRenderer.class, renderer);
    }

    public static <E> E get(Class<?> klass) {
        E item = (E) graph.get(klass);

        if (item == null && klass == StylesRenderer.class) {
            return (E) new NullStylesRenderer(null);
        }
        return item;
    }
}
