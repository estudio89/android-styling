package br.com.estudio89.styling.renderers;

import android.graphics.Color;
import android.widget.CheckBox;

/**
 * Created by luccascorrea on 10/1/15.
 */
public class CheckBoxRenderer extends AbstractViewRenderer {
    @Override
    public Class getRenderedClass() {
        return CheckBox.class;
    }

    @Override
    public boolean renderParam(Object view, String param, String value) {
        CheckBox cb = (CheckBox) view;
        if (param.equals("textColor")) {
            cb.setTextColor(Color.parseColor(value));
            return true;
        }
        return false;
    }
}
