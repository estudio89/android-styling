package br.com.estudio89.styling;

import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;

/**
 * Created by luccascorrea on 10/1/15.
 */
public class TestProcessor extends StylesProcessor {


    public TestProcessor(File stylesDefinition, File colorsDefinition, File viewsStyles) {
        super(stylesDefinition, colorsDefinition, viewsStyles);
    }

    public TestProcessor(InputStream stylesDefinition, InputStream colorsDefinition, InputStream viewsStyles) {
        super(stylesDefinition, colorsDefinition, viewsStyles);
    }

    public JSONObject getColorsDefinition() {
        return this.colorsDefinition;
    }

    public JSONObject getViewStyles(){
        return this.viewsStyles;
    }

    public JSONObject getStylesDefinition() {
        return this.stylesDefinition;
    }

    public void setColorsDefinition(JSONObject jsonObject) {
        this.colorsDefinition = jsonObject;
    }

    public void setStylesDefinition(JSONObject jsonObject) {
        this.stylesDefinition = jsonObject;
    }

}
