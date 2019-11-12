package asu.gunma.ui.util.AssetManagement;

import asu.gunma.ui.util.AssetManagement.monthlyAssets.Month;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

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

    public static Skin getColorSkin(Color color, String name) {
        Skin skin = new Skin();
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();
        skin.add(name, new Texture(pixmap));
        return skin;
    }
}
