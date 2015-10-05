package br.com.estudio89.styling;

import android.app.Activity;
import android.view.View;
import br.com.estudio89.styling.renderers.AbstractViewRenderer;

/**
 * Created by luccascorrea on 10/2/15.
 */
public class NullStylesRenderer extends StylesRenderer {

    public NullStylesRenderer(StylesManager stylesManager) {
        super(stylesManager);
    }

    @Override
    public void renderLayout(String viewXml, View parentView) {}

    @Override
    public void addCustomRenderer(AbstractViewRenderer renderer) {}

    @Override
    public void renderView(String styleName, Object view) {}

    @Override
    public void renderLayout(String viewXml, Activity activity) {}

}
