package asu.gunma.ui.util.AssetManagement;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.Locale;
import java.util.ResourceBundle;

public class GameAssets {
    // Localization assets
    public String localeString = "en";
    private Locale locale;
    private ResourceBundle bundle;
    public ResourceBundle getResourceBundle() {
        locale = new Locale(localeString);
        bundle = MyResources.getBundle("asu.gunma.ui.util.AssetManagement.MyResources", locale);
        return bundle;
    }
    public void setLocale(String lang) {
        localeString = lang;
    }

    public BitmapFont getFont() {
        Texture textureCN = new Texture(Gdx.files.internal("custom_font_hiero.png"), true);
        textureCN.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.Linear);

        return new BitmapFont(Gdx.files.internal("custom_font_hiero.fnt"), new TextureRegion(textureCN), false);
    }
}
