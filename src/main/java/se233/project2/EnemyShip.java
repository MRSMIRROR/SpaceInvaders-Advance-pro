package se233.project2;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class EnemyShip extends Character {
    private Image EnemyShipimage;
    private ImageView imageView;
    private int x;
    private int y;
    public EnemyShip(int x, int y, int w, int h, Image enemyShipimage) {
        super(x, y, w, h);
        this.x = x;
        this.y = y;
        this.setLayoutX(x);
        this.setLayoutY(y);
        this.EnemyShipimage = enemyShipimage;
        this.imageView = new ImageView(EnemyShipimage);
        this.imageView.setFitWidth(w);
        this.imageView.setFitHeight(h);
        this.getChildren().addAll(this.imageView);
    }
}