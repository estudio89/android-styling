package br.com.estudio89.styling;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;

/**
 * Created by luccascorrea on 10/1/15.
 */
public class StylesProcessor {

    protected JSONObject stylesDefinition;
    protected JSONObject colorsDefinition;
    protected JSONObject viewsStyles;

    public StylesProcessor(File stylesDefinition, File colorsDefinition, File viewsStyles) {
        this.stylesDefinition = fileToJSON(stylesDefinition);
        this.colorsDefinition = fileToJSON(colorsDefinition);
        this.viewsStyles = fileToJSON(viewsStyles);
    }
    public StylesProcessor(byte[] stylesDefinition, byte[] colorsDefinition, byte[] viewsStyles) {
        this.stylesDefinition = bytesToJSON(stylesDefinition);
        this.colorsDefinition = bytesToJSON(colorsDefinition);
        this.viewsStyles = bytesToJSON(viewsStyles);
    }

    public StylesProcessor(InputStream stylesDefinition, InputStream colorsDefinition, InputStream viewsStyles) {
        this.stylesDefinition = inputStreamToJSON(stylesDefinition);
        this.colorsDefinition = inputStreamToJSON(colorsDefinition);
        this.viewsStyles = inputStreamToJSON(viewsStyles);
    }

    public StylesProcessor(JSONObject stylesObject, JSONObject colorsObject, JSONObject viewsStylesObject) {
        this.stylesDefinition = stylesObject;
        this.colorsDefinition = colorsObject;
        this.viewsStyles = viewsStylesObject;
    }

    protected boolean processColorDefinitions () {
        return processDefinitions(this.colorsDefinition);
    }

    protected boolean processStylesDefinition() {
        return processDefinitions(this.stylesDefinition);
    }

    protected boolean processViewStylesDefinition () {
        return processDefinitions(this.viewsStyles);
    }

    public boolean fullProcess() {
        long startTime = System.currentTimeMillis();
        boolean hasUndefinedColors = processColorDefinitions();
        boolean hasUndefinedStyles = processStylesDefinition();
        boolean hasUndefinedViewStyles = processViewStylesDefinition();

        long endTime = System.currentTimeMillis();
//        Log.d("StylesProcessor", "Total time to process styles: " + (endTime - startTime) + " ms.");
        return hasUndefinedColors || hasUndefinedStyles ||  hasUndefinedViewStyles;
    }


    protected boolean processDefinitions (JSONObject definitions) {
        boolean hasUndefinedVariables = false;

        Iterator<String> itemNames = definitions.keys();
        while(itemNames.hasNext()) {
            String name = itemNames.next();
            String value = null;
            JSONObject nestedObj;
            try {
                nestedObj = definitions.getJSONObject(name);
                boolean hasUndefinedNestedVariables = processDefinitions(nestedObj);
                definitions.put(name, nestedObj);
                if (hasUndefinedNestedVariables) {
                    hasUndefinedVariables = true;
                }
                continue;

            } catch (JSONException e) {
                try {
                    value = definitions.getString(name);
                } catch (JSONException e1) {
                    throw new RuntimeException(e1);
                }

            }
            while (isVariableName(value)) {
                String variableName = getVariableName(value);
                JSONObject variableSource = getVariableSource(value);
                try {
                    nestedObj = variableSource.getJSONObject(variableName);
                    definitions.put(name, nestedObj);
                    value = null;
                    break;

                } catch (JSONException e) {
                    try {
                        value = variableSource.getString(variableName);
                    } catch (JSONException e1) {
                        Log.e("Styling", "Error processing variable with name \"" + variableName + "\". There may be a color missing from the definition. COLORS FILE SHOULD BE UPDATED! ");
                        hasUndefinedVariables = true;
                        break;
                    }
                }
            }
            if (value != null) {
                try {
                    definitions.put(name, value);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

        }

        return hasUndefinedVariables;
    }

    public JSONObject getColorsDefinition() {
        return colorsDefinition;
    }

    public JSONObject getStylesDefinition() {
        return stylesDefinition;
    }

    public JSONObject getViewStyles() {
        return this.viewsStyles;
    }

    private boolean isVariableName(String name) {
        return name.startsWith("@");
    }

    private JSONObject getVariableSource(String name) {
        if (name.startsWith("@color/")) {
            return this.colorsDefinition;
        } else if (name.startsWith("@style/")) {
            return this.stylesDefinition;
        } else {
            throw new IllegalArgumentException("The variable name " + name + " is invalid.");
        }
    }

    private String getVariableName(String name) {
        return name.replaceAll("^\\@\\w+\\/", "").trim();
    }

    private JSONObject fileToJSON(File file) {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return inputStreamToJSON(inputStream);
    }

    private JSONObject inputStreamToJSON(InputStream inputStream) {

        int size;
        byte[] buffer;
        try {
            size = inputStream.available();
            buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String jsonString = null;
        try {
            jsonString = new String(buffer, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonString);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        return jsonObject;
    }

    private JSONObject bytesToJSON(byte[] bytes) {
        String jsonString = null;
        try {
            jsonString = new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonString);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        return jsonObject;
    }

}
