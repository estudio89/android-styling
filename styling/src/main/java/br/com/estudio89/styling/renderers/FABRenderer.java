package br.com.estudio89.styling.renderers;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;

/**
 * Created by luccascorrea on 12/9/16.
 */
public class FABRenderer extends AbstractViewRenderer {
    @Override
    public Class getRenderedClass() {
        return FloatingActionButton.class;
    }

    @Override
    public boolean renderParam(Object view, String param, String value) {
        FloatingActionButton fab = (FloatingActionButton) view;

        if ("background".equals(param)) {
            int color = Color.parseColor(value);
            fab.setBackgroundTintList(ColorStateList.valueOf(color));
            return true;
        }
        return false;
    }
}
