package asu.gunma.ui.util.AssetManagement;

import asu.gunma.ui.util.AssetManagement.monthlyAssets.Month;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.Locale;
import java.util.ResourceBundle;

/* These are the global game assets.
*
* To add a new global asset add the following line:
* public static <asset_type> <asset_name> = <asset_value>;
*
* To add a seasonal asset, see [asu.gunma.ui.util.AssetManagement.seasonalAssets.Season].
* To add a monthly asset, see [asu.gunma.ui.util.AssetManagement.monthlyAssets.Month].
*
* Note: adding an asset to this file will override any seasonal or monthly assets.
* */
public class GameAssets extends Month {
    // Global Assets Across Screens
    public static Color backgroundColor = Color.WHITE;
    public static String fontPath = "irohamaru-mikami-Regular.ttf";
    public static String introMusicPath = "IntroMusic.mp3";
    public static String titleGunmaPath = "title_gunma.png";

    // Main Menu Screen Assets

    // Option Menu Screen Assets

    // Settings Screen Assets

    // Title Screen Assets

    // Flashcard Screen Assets
    public static String greenCirclePath = "greenCircle.png";
    public static String redXPath = "redX.png";

    // Game Screen Assets
    public static String gunmaSpritePath = "sprite_gunma.png";
    public static String gunmaFaintedSpritePath = "gunma_fainted.png";
    public static String backgroundImagePath = "BG_temp.png";
    public static String onionWalkAnimationPath = "onion_sheet.png";
    public static String gunmaWalkAnimationPath = "gunma_sheet.png";
    public static String correctSpritePath = "background/correct.png";
    public static String incorrectSpritePath = "background/incorrect.png";

    // Background Drawer Assets
    public static String cloud1Path = "background/cloud1.png";
    public static String cloud2Path = "background/cloud2.png";
    public static String skyImagePath = "background/skyx2.png";

    // High scores
    public Score score1 = new Score(0, "");
    public Score score2 = new Score(0, "");
    public Score score3 = new Score(0, "");
    public Score score4 = new Score(0, "");
    public Score score5 = new Score(0, "");
    public Score score6 = new Score(0, "");
    public Score score7 = new Score(0, "");
    public Score score8 = new Score(0, "");
    public Score score9 = new Score(0, "");
    public Score score10 = new Score(0, "");

    public void saveUserScore(int score, String nickname) {
        // TODO: save user score to cloud
        if(score > score1.value) {
            score1 = new Score(score, nickname);
        } else if(score > score2.value) {
            score2 = new Score(score, nickname);
        } else if(score > score3.value) {
            score3 = new Score(score, nickname);
        } else if(score > score4.value) {
            score4 = new Score(score, nickname);
        } else if(score > score5.value) {
            score5 = new Score(score, nickname);
        } else if(score > score6.value) {
            score6 = new Score(score, nickname);
        } else if(score > score7.value) {
            score7 = new Score(score, nickname);
        } else if(score > score8.value) {
            score8 = new Score(score, nickname);
        } else if(score > score9.value) {
            score9 = new Score(score, nickname);
        } else if(score > score10.value) {
            score10 = new Score(score, nickname);
        }
    }

    public static Skin getColorSkin(Color color, String name) {
        Skin skin = new Skin();
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();
        skin.add(name, new Texture(pixmap));
        return skin;
    }

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
        Texture textureCN = new Texture(Gdx.files.internal("custom_font_hiero_2.png"), true);
        textureCN.setFilter(Texture.TextureFilter.MipMapLinearNearest, Texture.TextureFilter.Linear);

        return new BitmapFont(Gdx.files.internal("custom_font_hiero_2.fnt"), new TextureRegion(textureCN), false);
    }
}
