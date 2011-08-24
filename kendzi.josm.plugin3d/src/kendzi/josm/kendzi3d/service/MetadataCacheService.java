/*
 * This software is provided "AS IS" without a warranty of any kind.
 * You use it on your own risk and responsibility!!!
 *
 * This file is shared under BSD v3 license.
 * See readme.txt and BSD3 file for details.
 *
 */

package kendzi.josm.kendzi3d.service;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import kendzi.josm.kendzi3d.metadata.ModelMetadata;
import kendzi.josm.kendzi3d.metadata.TextureMetadata;

public class MetadataCacheService {

//    private static MetadataCache metadataCache = null;
    private static String metadataDir = null;
    private static Map<String, TextureMetadata> cacheTexture = new HashMap<String, TextureMetadata>();
    private static Map<String, ModelMetadata> cacheModel = new HashMap<String, ModelMetadata>();
    private static Properties metadataProperties;

    /**
     * Initialize Metadata cache. It need plugin dir to load external files.
     *
     * @param pMetadataDir
     *            plugin dir where are external files
     */
    public static void initMetadataCache(String pMetadataDir) {

        metadataDir = pMetadataDir;

        loadMetadataProperties();

    }

    /**
     * Read textures properties.
     */
    private static void loadMetadataProperties() {
        metadataProperties = new Properties();
        String fileName = "/resources/metadata.properties";
        try {
            File f = new File(metadataDir, fileName);
            if (f.exists()) {
                FileInputStream in = new FileInputStream(f);
                metadataProperties.load(in);
            }

        } catch (Exception e) {
            // XXX
            e.printStackTrace();
        }
        try {
            URL fileUrl = MetadataCacheService.class.getResource(fileName);

            metadataProperties.load(fileUrl.openStream());

        } catch (Exception e) {
            // XXX
            e.printStackTrace();
        }
    }

    /**
     * Clean up all textures from cache.
     */
    public static void clear() {
        cacheModel.clear();
        cacheTexture.clear();
        loadMetadataProperties();
    }

    /** Gets metadata for texture from cache or load it from properties file.
     *  Location of properties file is {PLUGIN_DIR_NAME}/resources/metadata.properties
     * @param pId name of texture, "textures." prefix is added.
     * @return metadata of texture
     */
    public static TextureMetadata getTexture(String pId) {
        String key = "textures." + pId;
        TextureMetadata textureMetadata = cacheTexture.get(key);
        if (textureMetadata == null) {
            textureMetadata = loadTextureMetadata(key);
            cacheTexture.put(key, textureMetadata);
        }
        return textureMetadata;
    }

    /** Gets metadata for model from cache or load it from properties file.
     *  Location of properties file is {PLUGIN_DIR_NAME}/resources/metadata.properties
     * @param pId of model
     * @return metadata of model
     */
    public static ModelMetadata getModel(String pId) {
        String key = "models." + pId;
        ModelMetadata modelMetadata = cacheModel.get(key);
        if (modelMetadata == null) {
            modelMetadata = loadModelMetadata(key);
            cacheModel.put(key, modelMetadata);
        }
        return modelMetadata;
    }

    /** Loads metadata of texture from file.
     * @param pName name of texture
     * @return metadata of texture
     */
    private static TextureMetadata loadTextureMetadata(String pName) {
        TextureMetadata textureMetadata = new TextureMetadata();

        textureMetadata.setSizeU(parseDouble(pName + ".sizeU", 1d));
        textureMetadata.setSizeV(parseDouble(pName + ".sizeV", 1d));
        textureMetadata.setFile(
                metadataProperties.getProperty(pName + ".file", null));

        //TODO
        return textureMetadata;
    }

    /** Loads metadata of texture from file.
     * @param pName name of texture
     * @return metadata of texture
     */
    private static ModelMetadata loadModelMetadata(String pName) {
        ModelMetadata modelMetadata = new ModelMetadata();
        modelMetadata.setFile(
                metadataProperties.getProperty(pName + ".file", null));



        //TODO
        return modelMetadata;
    }

    /** Parse Double value for key.
     * @param pKey properties key
     * @param pDefaultValue default value
     * @return double value
     */
    private static Double parseDouble(String pKey, Double pDefaultValue) {
        Double value = null;
        try {
            String property = metadataProperties.getProperty(pKey, null);
            if (property != null) {
                value = Double.valueOf(property);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (value == null) {
            value = pDefaultValue;
        }
        return value;
    }

    public static Double getPropertitesDouble(String pKey, Double pDefaultValue) {
        return parseDouble(pKey, pDefaultValue);
    }

    public static String getPropertites(String pKey, String pDefaultValue) {
        return metadataProperties.getProperty(pKey, pDefaultValue);
    }

    public static String getPropertites(String pPattern, String pDefaultValue, String ... pKeyParts) {
        return getPropertites(pPattern, pDefaultValue, true, pKeyParts);
    }

    public static String getPropertites(String pPattern, String pDefaultValue, boolean pTakeUnknown, String ... pKeyParts) {

        if (pTakeUnknown && pKeyParts != null) {
            for (int i = 0; i < pKeyParts.length; i++) {

                if (pKeyParts[i] == null) {
                    pKeyParts[i] = "unknown";
                }
            }
        }

        String key = MessageFormat.format(
                pPattern, (Object []) pKeyParts);

        return metadataProperties.getProperty(key, pDefaultValue);
    }

    public static Double getPropertitesDouble(String pPattern, Double pDefaultValue, String ... pKeyParts) {
        return getPropertitesDouble(pPattern, pDefaultValue, true, pKeyParts);
    }

    public static Double getPropertitesDouble(String pPattern, Double pDefaultValue, boolean pTakeUnknown, String ... pKeyParts) {

        if (pTakeUnknown && pKeyParts != null) {
            for (int i = 0; i < pKeyParts.length; i++) {

                if (pKeyParts[i] == null) {
                    pKeyParts[i] = "unknown";
                }
            }
        }

        String key = MessageFormat.format(
                pPattern, (Object []) pKeyParts);

        return parseDouble(key, pDefaultValue);
    }

}