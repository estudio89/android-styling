package br.com.estudio89.styling;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
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

    public StylesProcessor(InputStream stylesDefinition, InputStream colorsDefinition, InputStream viewsStyles) {
        this.stylesDefinition = inputStreamToJSON(stylesDefinition);
        this.colorsDefinition = inputStreamToJSON(colorsDefinition);
        this.viewsStyles = inputStreamToJSON(viewsStyles);
    }

    protected void processColorDefinitions () {
        processDefinitions(this.colorsDefinition);
    }

    protected void processStylesDefinition() {
        processDefinitions(this.stylesDefinition);
    }

    protected void processViewStylesDefinition () {
        processDefinitions(this.viewsStyles);
    }

    public void fullProcess() {
        processColorDefinitions();
        processStylesDefinition();
        processViewStylesDefinition();
    }


    protected void processDefinitions (JSONObject definitions) {
        Iterator<String> itemNames = definitions.keys();
        while(itemNames.hasNext()) {
            String name = itemNames.next();
            String value = null;
            JSONObject nestedObj;
            try {
                nestedObj = definitions.getJSONObject(name);
                processDefinitions(nestedObj);
                definitions.put(name, nestedObj);
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
                        throw new RuntimeException(e1);
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


}
