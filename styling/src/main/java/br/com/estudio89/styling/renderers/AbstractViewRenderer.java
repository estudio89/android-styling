package br.com.estudio89.styling.renderers;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by luccascorrea on 10/1/15.
 */
public abstract class AbstractViewRenderer {

    public abstract Class getRenderedClass();
    public abstract boolean renderParam(Object view, String param, String value);

    public void render(Object view, JSONObject params) {
        Iterator<String> paramNames = params.keys();

        while(paramNames.hasNext()) {
            String param = paramNames.next();
            String value = null;
            try {
                value = params.getString(param);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            boolean result = renderParam(view, param, value);
            if (!result) {
                throw new UnsupportedParamException("The param " + param + " is not supported for view of class " + view.getClass().getSimpleName());
            }
        }
    }

    public static class UnsupportedParamException extends IllegalArgumentException {
        public UnsupportedParamException(String s) {
            super(s);
        }

        public UnsupportedParamException(String s, Throwable throwable) {
            super(s, throwable);
        }

        public UnsupportedParamException(Throwable throwable) {
            super(throwable);
        }
    }

}
