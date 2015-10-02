package br.com.estudio89.styling;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by luccascorrea on 10/1/15.
 */
public class StylesManager {
    private StylesProcessor processor;

    public static StylesManager getInstance() {
        return StylesInjection.get(StylesManager.class);
    }

    public StylesManager(StylesProcessor processor) {
        this.processor = processor;
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
            throw new UnknownStyleException("No styles are defined whith name " + styleName);
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

    public static class NoStyleDefinitionException extends Exception {

        public NoStyleDefinitionException(String s) {
            super(s);
        }

        public NoStyleDefinitionException(String s, Throwable throwable) {
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
