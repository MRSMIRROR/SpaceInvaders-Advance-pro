package se233.project2;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;

public class PlayerShip extends Character{
    private Image PlayerShipimage;
    private ImageView imageView;
    private int x;
    private int y;
    private KeyCode leftKey;
    private KeyCode rightKey;
    private KeyCode shootKey;
    public PlayerShip(int x, int y, int w, int h, Image playerShipimage, KeyCode leftKey, KeyCode rightKey, KeyCode shootKey) {
        super(x, y, w, h);
        this.x = x;
        this.y = y;
        this.setLayoutX(x);
        this.setLayoutY(y);
        this.PlayerShipimage = playerShipimage;
        this.imageView = new ImageView(PlayerShipimage);
        this.imageView.setFitWidth(w);
        this.imageView.setFitHeight(h);
        this.leftKey = leftKey;
        this.rightKey = rightKey;
        this.shootKey = shootKey;
        this.getChildren().addAll(this.imageView);
    }
}