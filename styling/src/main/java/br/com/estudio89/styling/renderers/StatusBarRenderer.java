package br.com.estudio89.styling.renderers;

import android.graphics.Color;
import android.view.Window;

/**
 * Created by luccascorrea on 10/1/15.
 */
public class StatusBarRenderer extends AbstractViewRenderer {

    @Override
    public Class getRenderedClass() {
        return Window.class;
    }

    @Override
    public boolean renderParam(Object view, String param, String value) {
        Window window = (Window) view;
        if ("color".equals(param)){
            window.setStatusBarColor(Color.parseColor(value));
            return true;
        }
        return false;
    }
}
