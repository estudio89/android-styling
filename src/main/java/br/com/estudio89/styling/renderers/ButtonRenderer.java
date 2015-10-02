package br.com.estudio89.styling.renderers;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.widget.Button;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by luccascorrea on 10/1/15.
 */
public class ButtonRenderer extends AbstractViewRenderer {


    @Override
    public Class getRenderedClass() {
        return Button.class;
    }

    @Override
    public boolean renderParam(Object view, String param, String value) {
        Button button = (Button) view;

        if (("textColor").equals(param)) {
            int color = Color.parseColor(value);
            button.setTextColor(color);
            return true;
        } else if (("drawable").equals(param)) {
            Context context = button.getContext();
            Drawable drawable = button.getBackground();
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
