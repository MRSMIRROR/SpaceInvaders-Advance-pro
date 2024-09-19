package se233.project2;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class Character extends Pane {
    private Image image;
    private ImageView imageView;
    private int x;
    private int y;
    private int w;
    private int h;
    public Character(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.setLayoutX(x);
        this.setLayoutY(y);
        this.w = w;
        this.h = h;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public int getW() {
        return w;
    }
    public int getH() {
        return h;
    }
}
