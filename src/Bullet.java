import javafx.scene.shape.Circle;

public class Bullet {
    double b_x;
    double b_y;
    Circle sprite = new Circle();

    Bullet(double newX, double newY) {
        b_x = newX;
        b_y = newY;
        sprite.setCenterX(b_x);
        sprite.setCenterY(b_y);
        sprite.setRadius(3);
    }
}