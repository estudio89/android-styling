package br.com.estudio89.styling.renderers;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;


/**
 * Created by luccascorrea on 10/1/15.
 */
public class ActionBarRenderer extends AbstractViewRenderer {
    @Override
    public Class getRenderedClass() {
        return ActionBar.class;
    }

    @Override
    public boolean renderParam(Object view, String param, String value) {
        ActionBar actionBar = (ActionBar) view;
        if ("background".equals(param)) {
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(value)));
            return true;
        }
        return false;
    }
}
