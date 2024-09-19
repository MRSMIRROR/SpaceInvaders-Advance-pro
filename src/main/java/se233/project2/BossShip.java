package se233.project2;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class BossShip extends Character {
    private Image BossShipimage;
    private ImageView imageView;
    private int hitTaken;
    private int x;
    private int y;
    public BossShip(int x, int y, int w, int h, Image bossShipimage) {
        super(x, y, w, h);
        this.hitTaken = 0;
        this.x = x;
        this.y = y;
        this.setLayoutX(x);
        this.setLayoutY(y);
        this.BossShipimage = bossShipimage;
        this.imageView = new ImageView(BossShipimage);
        this.imageView.setFitWidth(w);
        this.imageView.setFitHeight(h);
        this.getChildren().addAll(this.imageView);
    }
    public void increaseHitTaken() {
        hitTaken++;
    }
    public boolean isDefeated() {
        return hitTaken >= 10;

    }
}
