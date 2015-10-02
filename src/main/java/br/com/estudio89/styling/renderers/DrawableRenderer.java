package br.com.estudio89.styling.renderers;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.*;
import android.util.TypedValue;
import org.json.JSONObject;

/**
 * Created by luccascorrea on 10/1/15.
 */
public class DrawableRenderer extends AbstractViewRenderer {
    private Context context;

    public void setContext(Context context) {
        this.context = context;
    }
    @Override
    public Class getRenderedClass() {
        return Drawable.class;
    }

    @Override
    public boolean renderParam(Object view, String param, String value) {
        return false;
    }


    @Override
    public void render(Object view, JSONObject params) {
        Drawable drawable = (Drawable) view;

        String layerId = params.optString("layerId");
        if (!"".equals(layerId)) {
            GradientDrawable shape = null;
            int resId = context.getResources().getIdentifier(layerId, "id", context.getPackageName());
            try {
                LayerDrawable layers = (LayerDrawable) drawable;
                try {
                    shape = (GradientDrawable) layers.findDrawableByLayerId(resId);
                } catch (ClassCastException e) {
                    shape = (GradientDrawable) ((RotateDrawable) layers.findDrawableByLayerId(resId)).getDrawable();
                }
            } catch (ClassCastException e) {
                StateListDrawable layers = (StateListDrawable) drawable;
                shape = (GradientDrawable) layers.getCurrent();
            }

            renderShape(shape, params);

        } else {
            renderShape((GradientDrawable) drawable, params);
        }
    }

    public void renderShape(GradientDrawable shape, JSONObject params) {
        String solid = params.optString("solid");
        if (!"".equals(solid)) {
            shape.setColor(Color.parseColor(solid));
        }

        String stroke = params.optString("stroke");
        if (!"".equals(stroke)) {
            Resources r = context.getResources();

            int px = 0;
            String strokeWidth = params.optString("strokeWidth");
            if (!"".equals(strokeWidth)) {
                float width = Float.parseFloat(strokeWidth.replace("dp",""));
                px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, width, r.getDisplayMetrics());
            } else {
                px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, r.getDisplayMetrics());
            }
            shape.setStroke(px, Color.parseColor(stroke));
        }
    }
}
