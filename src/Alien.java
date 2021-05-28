import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class Alien {
    double x;
    double y;
    Boolean movingRight = true;

    Image image = new Image("images/saucer1b.png");
    ImageView sprite = new ImageView(image);

    Alien(double newX, double newY, double bS) {
        x = newX;
        y = newY;

        sprite.setPreserveRatio(true);
        sprite.setX(x);
        sprite.setY(y);
        sprite.setFitWidth(image.getWidth());
        sprite.setFitHeight(image.getHeight());
    }
}
