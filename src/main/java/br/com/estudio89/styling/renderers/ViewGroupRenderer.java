package br.com.estudio89.styling.renderers;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.ViewGroup;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by luccascorrea on 10/1/15.
 */
public class ViewGroupRenderer extends AbstractViewRenderer {
    @Override
    public Class getRenderedClass() {
        return ViewGroup.class;
    }

    @Override
    public boolean renderParam(Object view, String param, String value) {
        ViewGroup vg = (ViewGroup) view;
        if ("background".equals(param)) {
            vg.setBackgroundColor(Color.parseColor(value));
            return true;
        } else if (("drawable").equals(param)) {
            Context context = vg.getContext();
            Drawable drawable = vg.getBackground();
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
