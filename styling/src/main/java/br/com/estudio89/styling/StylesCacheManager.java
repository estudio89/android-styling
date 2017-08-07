package br.com.estudio89.styling;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by luccascorrea on 8/7/17.
 */

public class StylesCacheManager {
    private static final String SERIALIZED_COLORS_FILE = "colors_serialized";
    private static final String SERIALIZED_STYLES_FILE = "styles_serialized";
    private static final String SERIALIZED_VIEWS_STYLES_FILE = "view_styles_serialized";
    private static final String COLORS_HASH_KEY = "colors_hash";
    private static final String STYLES_HASH_KEY = "styles_hash";
    private static final String VIEWS_STYLES_HASH_KEY = "views_styles_hash";
    private final String STYLES_PREFERENCE_FILE = "br.com.estudio89.styling.cache";
    private final Context context;

    private JSONObject colorsObject;
    private JSONObject stylesObject;
    private JSONObject viewStylesObject;
    private StylesProcessor stylesProcessor;

    public StylesCacheManager(Context context) {
        this.context = context;
    }

    public boolean loadFromCache(byte[] stylesBytes, byte[] colorsBytes, byte[] viewStylesBytes) {
        String currentColorsHash = getHash(COLORS_HASH_KEY);
        String currentStylesHash = getHash(STYLES_HASH_KEY);
        String currentViewsStylesHash = getHash(VIEWS_STYLES_HASH_KEY);

        if (currentColorsHash == null || currentStylesHash == null || currentViewsStylesHash == null) {
            return false;
        }

        String newColorsHash = MD5.calculateMD5(colorsBytes);
        String newStylesHash = MD5.calculateMD5(stylesBytes);
        String newViewsStylesHash = MD5.calculateMD5(viewStylesBytes);

        if (!currentColorsHash.equalsIgnoreCase(newColorsHash) || !currentStylesHash.equalsIgnoreCase(newStylesHash) || !currentViewsStylesHash.equalsIgnoreCase(newViewsStylesHash)) {
            return false;
        }

        colorsObject = loadSerialized(context, SERIALIZED_COLORS_FILE);
        stylesObject = loadSerialized(context, SERIALIZED_STYLES_FILE);
        viewStylesObject = loadSerialized(context, SERIALIZED_VIEWS_STYLES_FILE);
        return stylesObject != null && colorsObject != null && viewStylesObject != null;
    }

    public boolean setupProcessor(InputStream stylesFile, InputStream colorsFile, InputStream viewStylesFile) {

        byte[] stylesBytes;
        byte[] colorsBytes;
        byte[] viewStylesBytes;
        try {
            stylesBytes = getBytes(stylesFile);
            colorsBytes = getBytes(colorsFile);
            viewStylesBytes = getBytes(viewStylesFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (loadFromCache(stylesBytes, colorsBytes, viewStylesBytes)) {
//            Log.d("Styling", "Loaded styles from cache");
            stylesProcessor = new StylesProcessor(stylesObject, colorsObject, viewStylesObject);
            return false;
        } else {
//            Log.d("Styling", "Did not load styles from cache");
            stylesProcessor = new StylesProcessor(stylesBytes, colorsBytes, viewStylesBytes);
            boolean hasUndefinedVariables = stylesProcessor.fullProcess();

            if (!hasUndefinedVariables) {
                try {
                    saveSerialized(context, SERIALIZED_COLORS_FILE, stylesProcessor.colorsDefinition);
                    storeHash(COLORS_HASH_KEY, MD5.calculateMD5(colorsBytes));
                    saveSerialized(context, SERIALIZED_STYLES_FILE, stylesProcessor.stylesDefinition);
                    storeHash(STYLES_HASH_KEY, MD5.calculateMD5(stylesBytes));
                    saveSerialized(context, SERIALIZED_VIEWS_STYLES_FILE, stylesProcessor.viewsStyles);
                    storeHash(VIEWS_STYLES_HASH_KEY, MD5.calculateMD5(viewStylesBytes));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return hasUndefinedVariables;
        }
    }

    public StylesProcessor getStylesProcessor() {
        return stylesProcessor;
    }

    private byte[] getBytes(InputStream is) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        int nRead;
        byte[] data = new byte[8192];

        while ((nRead = is.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }

        buffer.flush();

        return buffer.toByteArray();
    }
    private JSONObject loadSerialized(Context context, String filename) {
        String json = null;
        try {

            InputStream is = new FileInputStream(new File(context.getCacheDir(), filename));

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");
            return new JSONObject(json);

        } catch (IOException ex) {
            return null;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveSerialized(Context context, String filename, JSONObject object) throws IOException {
        File file = new File(context.getCacheDir(), filename);
        if (!file.exists()) {
            file.createNewFile();
        }

        FileWriter writer = new FileWriter(file, false);
        try {
            writer.write(object.toString());
        } finally {
            writer.flush();
            writer.close();
        }
    }

    private void storeHash(String key, String hash) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(STYLES_PREFERENCE_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, hash);
        editor.apply();
    }

    private String getHash(String filename) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(STYLES_PREFERENCE_FILE, Context.MODE_PRIVATE);
        return sharedPreferences.getString(filename, null);
    }

    static class MD5 {
        private static final String TAG = "MD5";

        public static boolean checkMD5(String md5, byte[] buffer) {
            if (TextUtils.isEmpty(md5) || buffer == null) {
                Log.e(TAG, "MD5 string empty or updateFile null");
                return false;
            }

            String calculatedDigest = calculateMD5(buffer);
            if (calculatedDigest == null) {
                Log.e(TAG, "calculatedDigest null");
                return false;
            }

            Log.v(TAG, "Calculated digest: " + calculatedDigest);
            Log.v(TAG, "Provided digest: " + md5);

            return calculatedDigest.equalsIgnoreCase(md5);
        }

        public static String calculateMD5(byte[] buffer) {
            MessageDigest digest;
            try {
                digest = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                Log.e(TAG, "Exception while getting digest", e);
                return null;
            }


            int read;
            digest.update(buffer, 0, buffer.length);
            byte[] md5sum = digest.digest();
            BigInteger bigInt = new BigInteger(1, md5sum);
            String output = bigInt.toString(16);
            // Fill to 32 chars
            output = String.format("%32s", output).replace(' ', '0');
            return output;
        }
    }
}
