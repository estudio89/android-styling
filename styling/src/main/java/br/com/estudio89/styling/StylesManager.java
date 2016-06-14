package br.com.estudio89.styling;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by luccascorrea on 10/1/15.
 */
public class StylesManager {
    private StylesProcessor processor;
    private String applicationPackage;

    public static StylesManager getInstance() {
        return StylesInjection.get(StylesManager.class);
    }

    public StylesManager(StylesProcessor processor, String applicationPackage) {

        this.processor = processor;
        this.applicationPackage = applicationPackage;
    }

    public JSONObject getStyles(String viewId) throws NoStyleDefinitionException {
        JSONObject styles = this.processor.getViewStyles();
        try {
            return styles.getJSONObject(viewId);
        } catch (JSONException e) {
            throw new NoStyleDefinitionException("No styles are defined for the view " + viewId);
        }
    }

    public JSONObject getStyle(String styleName) {
        JSONObject styles = this.processor.getStylesDefinition();
        try {
            return styles.getJSONObject(styleName);
        } catch (JSONException e) {
            throw new UnknownStyleException("No styles are defined with name " + styleName);
        }
    }

    public String getColor(String colorName) {
        JSONObject colors = this.processor.getColorsDefinition();
        try {
            return colors.getString(colorName);
        } catch (JSONException e) {
            throw new UnknownColorException("No color definition found for name " + colorName);
        }

    }

    public String getApplicationPackage() {
        return applicationPackage;
    }

    public static class NoStyleDefinitionException extends RuntimeException {

        public NoStyleDefinitionException(Throwable throwable) {
            super(throwable);
        }

        public NoStyleDefinitionException(String s) {
            super(s);
        }

        public NoStyleDefinitionException(String s, Throwable throwable) {
            super(s, throwable);
        }
    }

    public static class NoStyleDefinitionForLayoutException extends NoStyleDefinitionException {

        public NoStyleDefinitionForLayoutException(Throwable throwable) {
            super(throwable);
        }

        public NoStyleDefinitionForLayoutException(String s) {
            super(s);
        }

        public NoStyleDefinitionForLayoutException(String s, Throwable throwable) {
            super(s, throwable);
        }
    }

    public static class UnknownColorException extends RuntimeException {
        public UnknownColorException(String s) {
            super(s);
        }

        public UnknownColorException(String s, Throwable throwable) {
            super(s, throwable);
        }

        public UnknownColorException(Throwable throwable) {
            super(throwable);
        }
    }

    public static class UnknownStyleException extends RuntimeException {

        public UnknownStyleException(String s) {
            super(s);
        }

        public UnknownStyleException(String s, Throwable throwable) {
            super(s, throwable);
        }
    }

}
