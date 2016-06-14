package br.com.estudio89.styling;

import junit.framework.Assert;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

/**
 * Created by luccascorrea on 10/1/15.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class StyleProcessorTests {

    @Test
    public void testProcessColors() throws Exception {
        InputStream colors = loadResource("colors-demo.json");
        TestProcessor processor = new TestProcessor(colors, colors, colors);
        processor.processColorDefinitions();

        JSONObject definitions = processor.getColorsDefinition();

        String mainColor = definitions.getString("main_color");
        String nada = definitions.getString("nada");
        String myColor = definitions.getString("nada");

        Assert.assertEquals(mainColor, nada);
        Assert.assertEquals(mainColor, "#ff0000");
        Assert.assertEquals(nada, "#ff0000");
        Assert.assertEquals(myColor, "#ff00ff");

    }

    @Test
    public void testProcessStyles() throws Exception {
        InputStream styles = loadResource("styles-demo.json");
        TestProcessor processor = new TestProcessor(styles, styles, styles);

        JSONObject colorsDef = new JSONObject();
        colorsDef.put("main_color","#ff0000");
        colorsDef.put("nada","#ff0000");
        colorsDef.put("my_color","#ff00ff");

        processor.setColorsDefinition(colorsDef);
        processor.processStylesDefinition();

        JSONObject definitions = processor.getStylesDefinition();

        JSONObject estilo1 = definitions.getJSONObject("estilo1");
        String background1 = estilo1.getString("background");
        String textColor1 = estilo1.getString("text_color");

        JSONObject estilo2 = definitions.getJSONObject("estilo1");
        String background2 = estilo1.getString("background");
        String textColor2 = estilo1.getString("text_color");

        Assert.assertEquals(background1, "#ff0000");
        Assert.assertEquals(textColor1, "#ff0000");
        Assert.assertEquals(background2, "#ff00ff");
        Assert.assertEquals(textColor2, "#ff00ff");
    }

    @Test
    public void testProcessViewStyles() throws Exception {
        InputStream viewStyles = loadResource("view-styles-demo.json");
        TestProcessor processor = new TestProcessor(viewStyles, viewStyles, viewStyles);

        JSONObject stylesDef = new JSONObject();
        JSONObject style1 = new JSONObject();
        style1.put("background", "#ff0000");
        style1.put("text_color", "#ff0000");

        JSONObject style2 = new JSONObject();
        style2.put("background", "#ff0000");
        style2.put("text_color", "#ff0000");

        stylesDef.put("estilo1", style1);
        stylesDef.put("estilo2", style2);

        processor.setStylesDefinition(stylesDef);
        processor.fullProcess();

        JSONObject definitions = processor.getViewStyles();


        JSONObject loginActivity = definitions.getJSONObject("login_activity");
        JSONObject btnLogin = loginActivity.getJSONObject("btnLogin");

        String background = btnLogin.getString("background");
        String textColor = btnLogin.getString("text_color");

        Assert.assertEquals(background, "#ff0000");
        Assert.assertEquals(textColor, "#ff00ff");
    }

    @Test
    public void testFullProcessing() throws Exception {
        InputStream viewStyles = loadResource("view-styles-demo.json");
        InputStream colors  = loadResource("colors-demo.json");
        InputStream styles  = loadResource("styles-demo.json");
        TestProcessor processor = new TestProcessor(styles, colors, viewStyles);

        processor.fullProcess();

        JSONObject definitions = processor.getViewStyles();


        JSONObject loginActivity = definitions.getJSONObject("login_activity");
        JSONObject btnLogin = loginActivity.getJSONObject("btnLogin");

        String background = btnLogin.getString("background");
        String textColor = btnLogin.getString("text_color");

        Assert.assertEquals("#ff0000", background);
        Assert.assertEquals("#ff00ff", textColor);
    }

    public InputStream loadResource(String filename) throws IOException, JSONException, URISyntaxException {
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(filename);

        return is;
    }
}
