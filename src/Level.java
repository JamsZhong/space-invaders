import javafx.scene.Group;
import java.util.ArrayList;

public class Level {
    //Screen size
    public static final double SCREEN_WIDTH = SpaceInvaders.SCREEN_WIDTH;
    public static final double SCREEN_HEIGHT = SpaceInvaders.SCREEN_HEIGHT;

    public static final double ENEMY1_BULLET_SPEED = SpaceInvaders.ENEMY1_BULLET_SPEED;

    Player p;
    ArrayList<Alien> aliens;
    ArrayList<Bullet> playerBullets;
    ArrayList<Bullet> alienBullets;
    Group g;

    Level() {
        p = new Player(SCREEN_WIDTH / 2, SCREEN_HEIGHT - 30);
        aliens = new ArrayList<>();
        playerBullets = new ArrayList<>();
        alienBullets = new ArrayList<>();
        g = new Group(p.sprite);
        initializeAliens(aliens, g);
    }

    void initializeAliens(ArrayList<Alien> aliens, Group g) {
        int x_gap = 75;
        int y_gap = 45;
        for(int row = 0; row < 5; row++) {
            for(int col = 0; col < 10; col++) {
                Alien a = new Alien(col * x_gap, row * y_gap + 50, ENEMY1_BULLET_SPEED);
                aliens.add(a);
                g.getChildren().add(a.sprite);
            }
        }
    }

    void restart() {
        clear();
        p = new Player(SCREEN_WIDTH / 2, SCREEN_HEIGHT - 30);
        initializeAliens(aliens, g);
        g.getChildren().add(p.sprite);
    }

    void clear() {
        p = null;
        aliens.clear();
        alienBullets.clear();
        playerBullets.clear();
        g.getChildren().clear();
    }

    Boolean checkAlienLanding() {
        for(Alien a : aliens) {
            if(a.sprite.intersects(p.sprite.getLayoutBounds())) {
                return true;
            }
        }

        return false;
    }
}
