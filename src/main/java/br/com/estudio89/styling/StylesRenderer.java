package br.com.estudio89.styling;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import br.com.estudio89.styling.renderers.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by luccascorrea on 10/1/15.
 */
public class StylesRenderer {
    private StylesManager stylesManager;
    private List<AbstractViewRenderer> renderers = new ArrayList<AbstractViewRenderer>();

    public StylesRenderer(StylesManager stylesManager) {
        this.stylesManager = stylesManager;

        this.renderers.add(new ButtonRenderer());
        this.renderers.add(new TextViewRenderer());
        this.renderers.add(new ActionBarRenderer());
        this.renderers.add(new StatusBarRenderer());
        this.renderers.add(new ViewGroupRenderer());
        this.renderers.add(new CheckBoxRenderer());
        this.renderers.add(new GenericViewRenderer());
    }

    public void addCustomRenderer(AbstractViewRenderer renderer) {
        renderers.add(0, renderer);
    }

    public static StylesRenderer getInstance() {
        return StylesInjection.get(StylesRenderer.class);
    }

    public void renderLayout(String viewXml, Activity activity) {
        renderLayout(viewXml, null, activity);
    }

    public void renderLayout(String viewXml, View parentView) {
        renderLayout(viewXml, parentView, null);
    }

    public void renderLayout(String viewXml, View parentView, Activity activity) {
        Context context = null;
        if (parentView != null) {
            context = parentView.getContext();
        } else {
            context = activity;
        }

        JSONObject parentViewStyles = null;
        try {
            parentViewStyles = this.stylesManager.getStyles(viewXml);
        } catch (StylesManager.NoStyleDefinitionException e) {
            throw new RuntimeException(e);
        }

        Iterator<String> viewIds = parentViewStyles.keys();

        while (viewIds.hasNext()) {
            String viewId = viewIds.next();
            JSONObject viewStyles = null;
            try {
                viewStyles = parentViewStyles.getJSONObject(viewId);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            int resId = context.getResources().getIdentifier(viewId, "id", context.getPackageName());
            View view = null;

            if (parentView != null) {
                view = parentView.findViewById(resId);
            } else {
                view = activity.findViewById(resId);
            }

            if (view == null) {
                throw new IllegalArgumentException("A view with id " + viewId + " in the layout definition file " + viewXml + " was not found");
            }

            AbstractViewRenderer renderer = null;
            try {
                renderer = getRenderer(view);
            } catch (RendererNotFoundException e) {
                throw new RuntimeException("Could not find a suitable renderer for class " + view.getClass().getSimpleName() + " of object with id " + viewId + " in the layout definition " +
                        "file " + viewXml);
            }
            renderer.render(view, viewStyles);

        }
    }

    public void renderView(String styleName, Object view) {
        JSONObject style = this.stylesManager.getStyle(styleName);
        AbstractViewRenderer renderer = null;
        try {
            renderer = getRenderer(view);
        } catch (RendererNotFoundException e) {
            throw new RuntimeException("Could not find a suitable renderer for class " + view.getClass().getSimpleName());
        }
        renderer.render(view, style);
    }

    protected AbstractViewRenderer getRenderer(Object view) throws RendererNotFoundException {
        for (AbstractViewRenderer renderer:renderers) {
            if (renderer.getRenderedClass().isAssignableFrom(view.getClass())) {
                return renderer;
            }

        }
        throw new RendererNotFoundException("Could not find renderer for class " + view.getClass().getSimpleName());
    }

    public static class RendererNotFoundException extends Exception {

        public RendererNotFoundException(String s) {
            super(s);
        }

        public RendererNotFoundException(String s, Throwable throwable) {
            super(s, throwable);
        }

        public RendererNotFoundException(Throwable throwable) {
            super(throwable);
        }
    }
}