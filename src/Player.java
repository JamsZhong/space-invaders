import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class Player {
    double x;
    double y;

    Image image = new Image("images/baseshipb.png");
    ImageView sprite = new ImageView(image);

    Player(double newX, double newY) {
        x = newX;
        y = newY;

        sprite.setPreserveRatio(true);
        sprite.setX(x);
        sprite.setY(y);
        sprite.setFitWidth(image.getWidth());
        sprite.setFitHeight(image.getHeight());
    }
}
