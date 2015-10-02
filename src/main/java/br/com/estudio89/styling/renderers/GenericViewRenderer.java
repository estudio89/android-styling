package br.com.estudio89.styling.renderers;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by luccascorrea on 10/1/15.
 */
public class GenericViewRenderer extends AbstractViewRenderer {
    @Override
    public Class getRenderedClass() {
        return View.class;
    }

    @Override
    public boolean renderParam(Object view, String param, String value) {
        View viewObj = (View) view;

        if (("drawable").equals(param)) {
            Context context = viewObj.getContext();
            Drawable drawable = viewObj.getBackground();
            JSONObject drawableParams = null;
            try {
                drawableParams = new JSONObject(value);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            DrawableRenderer renderer = new DrawableRenderer();
            renderer.setContext(context);
            renderer.render(drawable, drawableParams);

            return true;
        }
        return false;
    }
}
