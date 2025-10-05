import bagel.Font;
import bagel.Window;
import bagel.util.Point;
import bagel.Image;

/**
 * Helper methods to display information for the player
 */
public class UserInterface {
    public static void drawStats(double health, double coins, double keys, double weapon) {
        int fontSize = Integer.parseInt(ShadowDungeon.getGameProps().getProperty("playerStats.fontSize"));
        drawData(String.format("%s %.1f", ShadowDungeon.getMessageProps().getProperty("healthDisplay"), health), fontSize,
                IOUtils.parseCoords(ShadowDungeon.getGameProps().getProperty("healthStat")));
        drawData(String.format("%s %.0f", ShadowDungeon.getMessageProps().getProperty("coinDisplay"), coins), fontSize,
                IOUtils.parseCoords(ShadowDungeon.getGameProps().getProperty("coinStat")));
        drawData(String.format("%s %.0f", ShadowDungeon.getMessageProps().getProperty("keyDisplay"), keys), fontSize,
                IOUtils.parseCoords(ShadowDungeon.getGameProps().getProperty("keyStat")));
//        drawData(String.format("%s %.0f", ShadowDungeon.getMessageProps().getProperty("keyDisplay"), coins), fontSize,
//                IOUtils.parseCoords(ShadowDungeon.getGameProps().getProperty("keyStat")));
        drawData(String.format("%s %.0f", ShadowDungeon.getMessageProps().getProperty("weaponDisplay"), weapon), fontSize,
                IOUtils.parseCoords(ShadowDungeon.getGameProps().getProperty("weaponStat")));

    }

    public static void drawStartMessages() {
        int fontSize = Integer.parseInt(ShadowDungeon.getGameProps().getProperty("playerStats.fontSize"));
        drawTextCentered("title", Integer.parseInt(ShadowDungeon.getGameProps().getProperty("title.fontSize")), Double.parseDouble(ShadowDungeon.getGameProps().getProperty("title.y")));
        drawTextCentered("moveMessage", Integer.parseInt(ShadowDungeon.getGameProps().getProperty("prompt.fontSize")), Double.parseDouble(ShadowDungeon.getGameProps().getProperty("moveMessage.y")));
        drawTextCentered("selectMessage", Integer.parseInt(ShadowDungeon.getGameProps().getProperty("prompt.fontSize")), Double.parseDouble(ShadowDungeon.getGameProps().getProperty("selectMessage.y")));
        drawData(String.format("%s", ShadowDungeon.getMessageProps().getProperty("robotDescription")), fontSize,
                IOUtils.parseCoords(ShadowDungeon.getGameProps().getProperty("robotMessage")));
        drawData(String.format("%s", ShadowDungeon.getMessageProps().getProperty("marineDescription")), fontSize,
                IOUtils.parseCoords(ShadowDungeon.getGameProps().getProperty("marineMessage")));
        new Image("res/robot_sprite.png").draw(IOUtils.parseCoords(ShadowDungeon.getGameProps().getProperty("Robot")).x, IOUtils.parseCoords(ShadowDungeon.getGameProps().getProperty("Robot")).y);
        new Image("res/marine_sprite.png").draw(IOUtils.parseCoords(ShadowDungeon.getGameProps().getProperty("Marine")).x, IOUtils.parseCoords(ShadowDungeon.getGameProps().getProperty("Marine")).y);






    }

    public static void drawEndMessage(boolean win) {
        drawTextCentered(win ? "gameEnd.won" : "gameEnd.lost", Integer.parseInt(ShadowDungeon.getGameProps().getProperty("title.fontSize")), Double.parseDouble(ShadowDungeon.getGameProps().getProperty("title.y")));
    }

    public static void drawTextCentered(String textPath, int fontSize, double posY) {
        Font font = new Font("res/wheaton.otf", fontSize);
        String text = ShadowDungeon.getMessageProps().getProperty(textPath);
        double posX = (Window.getWidth() - font.getWidth(text)) / 2;
        font.drawString(text, posX, posY);
    }

    public static void drawData(String data, int fontSize, Point location) {
        Font font = new Font("res/wheaton.otf", fontSize);
        font.drawString(data, location.x, location.y);
    }
}
