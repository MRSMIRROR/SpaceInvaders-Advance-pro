package se233.project2;

import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Group;
import javafx.scene.shape.Rectangle;
import javafx.scene.image.ImageView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Level;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class Space_Invaders2 extends Application {
    AnimationTimer timer;
    Pane root = new Pane();
    Circle dotR = new Circle();
    List<EnemyShip> enemies = new ArrayList<EnemyShip>();
    List<BossShip> boss = new ArrayList<BossShip>();
    List<Circle> pShoots = new ArrayList<Circle>();
    List<Circle> eShoots = new ArrayList<Circle>();
    List<ImageView> bBossShoot = new ArrayList<ImageView>();
    boolean toRight = true;
    Text lives;
    Text points;
    Text level;
    int currentLevel = 1;
    int numPoints = 0;
    int numLives = 3;
    private PlayerShip playerShip;
    public BossShip bossShip;
    private EnemyShip enemyShip;
    private Barrier barrier;
    private Group barrierGroup = new Group();
    private Group secondBarrier = new Group();
    private Group thirdBarrier = new Group();
    private Group fourthBarrier = new Group();
    private int SCENE_WIDTH = 600;
    private int APP_HEIGHT = 800;
    private int rectangleSize = 8;
    boolean bossToRight = true;
    private static final Logger logger = LogManager.getLogger(Space_Invaders2.class);
    private static final Logger characterLogger = LogManager.getLogger("CharacterActionsAndScoring");
    private ArrayList<Barrier> barriers = new ArrayList<>();
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            lives = new Text("Lives: 3");
            lives.setLayoutX(20);
            lives.setLayoutY(30);
            lives.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
            lives.setFill(Color.WHITE);
            points = new Text("Points: 0");
            points.setLayoutX(450);
            points.setLayoutY(30);
            points.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
            points.setFill(Color.WHITE);
            level = new Text("Level: 1");
            level.setLayoutX(260);
            level.setLayoutY(30);
            level.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
            level.setFill(Color.WHITE);
            playerShip = new PlayerShip(275, 720, 50, 50,new Image(getClass().getResourceAsStream("assets/MicrosoftTeams-image (13).png")), KeyCode.A, KeyCode.D, KeyCode.SPACE);
            root.getChildren().addAll(lives, points, level, playerShip, barrierGroup, secondBarrier, thirdBarrier, fourthBarrier);
            dotR.setLayoutX(0);
            timer = new AnimationTimer() {
                @Override
                public void handle(long arg0) {
                    gameUpdate();
                }
            };
            timer.start();

            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(1000), e -> {
                if (enemies.isEmpty()){
                    NextLevel();
                }
                if (!enemies.isEmpty()) {
                    enemyShoot();
                }
                if (!boss.isEmpty()) {
                    BossShoot();
                }
            }));
            timeline.setCycleCount(Animation.INDEFINITE);
            timeline.play();

            setBarriers();
            logger.info("Starting the Space Invaders game.");

            Scene scene = new Scene(root, 600, 800);
            scene.setFill(Color.BLACK);
            scene.setOnKeyPressed(e -> {
                if (e.getCode() == KeyCode.A) {
                    playerShip.setLayoutX(playerShip.getLayoutX() - 5);
                    logger.info("Player ship moved left.");
                }
                if (e.getCode() == KeyCode.D) {
                    playerShip.setLayoutX(playerShip.getLayoutX() + 5);
                    logger.info("Player ship moved right.");
                }
                if (e.getCode() == KeyCode.SPACE) {
                    playerShoot(playerShip.getLayoutX());
                    logger.info("Player ship fired a shot.");
                }
            });
            primaryStage.setScene(scene);
            primaryStage.setTitle("Space Invaders");
            primaryStage.setResizable(false);
            primaryStage.show();
        }catch (Exception e){
            logger.error("An exception occurred during game initialization.", e);
            handleException(e);
        }
    }
    private void handleException(Exception e) {
        e.printStackTrace();
    }
    public void gameUpdate() {
        playerShootUpdate();
        enemiesShootUpdate();
        bossShootingUpdate();
        enemiesMove();
        if (bossShip != null) {bossMove();}
        isBossDestroyed();
        isMonsterDestroyed();
        isPlayerDestroyed();
        isLost();
    }
    public ImageView SpacialShoot(double x, double y) {
        ImageView spacialShoot = new ImageView(new Image(getClass().getResourceAsStream("assets/MicrosoftTeams-image (5).png")));
        spacialShoot.setLayoutX(x);
        spacialShoot.setLayoutY(y);
        spacialShoot.setFitWidth(18);
        spacialShoot.setFitHeight(58);
        return spacialShoot;
    }
    public Circle shoot(double x, double y) {
        Circle shoot = new Circle();
        shoot.setRadius(5);
        shoot.setFill(Color.GREENYELLOW);
        shoot.setLayoutX(x);
        shoot.setLayoutY(y);
        return shoot;
    }
    public void playerShoot(double x) {
        pShoots.add(shoot((x + 25), 700));
        root.getChildren().add(pShoots.get(pShoots.size() - 1));
    }
    private void playerShootUpdate() {
        if (!pShoots.isEmpty()) {
            for (int i = 0; i < pShoots.size(); i++) {
                pShoots.get(i).setLayoutY(pShoots.get(i).getLayoutY() - 5);
                if (pShoots.get(i).getLayoutY() < 0) {
                    root.getChildren().remove(pShoots.get(i));
                    pShoots.remove(i);
                }
            }
        }
    }
    public void addEnemy() {
        for (int i = 0, w = 40; i < 8; i++, w += 70) {
            enemies.add(new EnemyShip(w,50, 44, 32, new Image(getClass().getResourceAsStream("assets/Invaders.png"))));
            root.getChildren().add(enemies.get(i));
        }
        for (int i = 0, w = 40; i < 8; i++, w += 70) {
            enemies.add(new EnemyShip(w,120,44, 32, new Image(getClass().getResourceAsStream("assets/MicrosoftTeams-image (2).png"))));
            root.getChildren().add(enemies.get(i + 8));
        }
        for (int i = 0, w = 40; i < 8; i++, w += 70) {
            enemies.add(new EnemyShip(w,190, 44, 32,new Image(getClass().getResourceAsStream("assets/MicrosoftTeams-image (3).png"))));
            root.getChildren().add(enemies.get(i + 16));
        }
    }
    public void addEnemy2() {
        for (int i = 0, w = 40; i < 8; i++, w += 70) {
            enemies.add(new EnemyShip(w,260, 44, 32, new Image(getClass().getResourceAsStream("assets/Invaders.png"))));
            root.getChildren().add(enemies.get(i));
        }
        for (int i = 0, w = 40; i < 8; i++, w += 70) {
            enemies.add(new EnemyShip(w,330,44, 32, new Image(getClass().getResourceAsStream("assets/MicrosoftTeams-image (2).png"))));
            root.getChildren().add(enemies.get(i + 8));
        }
        for (int i = 0, w = 40; i < 8; i++, w += 70) {
            enemies.add(new EnemyShip(w,400, 44, 32,new Image(getClass().getResourceAsStream("assets/MicrosoftTeams-image (3).png"))));
            root.getChildren().add(enemies.get(i + 16));
        }
        for (int i = 0, w = 170; i < 1; i++, w += 70) {
            boss.add(new BossShip(w, 50, 264, 190, new Image(getClass().getResourceAsStream("assets/Ocram_(Phase_1).gif"))));
            root.getChildren().add(boss.get(boss.size() - 1));
            bossShip = boss.get(boss.size() - 1); // Set bossShip here
}

    }
    public int randomEnemyShoot(int min, int max) {
        return (int)(Math.random() * (max - min + 1) + min);
    }
    public void enemyShoot() {
        int getShootingEnemy = randomEnemyShoot(0,enemies.size() - 1);
        eShoots.add(shoot(enemies.get(getShootingEnemy).getLayoutX() + 25, enemies.get(getShootingEnemy).getLayoutY() + 50));
        root.getChildren().add(eShoots.get(eShoots.size() - 1));
    }
    public void BossShoot(){
        int getShootingBoss = randomEnemyShoot(0,boss.size() - 1);
        bBossShoot.add(SpacialShoot(boss.get(getShootingBoss).getLayoutX() + 132, boss.get(getShootingBoss).getLayoutY() + 50));
        root.getChildren().add(bBossShoot.get(bBossShoot.size() - 1));
    }
    public void bossShootingUpdate() {
        if (!bBossShoot.isEmpty()) {
            for (int i = 0; i < bBossShoot.size(); i++) {
                bBossShoot.get(i).setLayoutY(bBossShoot.get(i).getLayoutY() + 3);
                if (bBossShoot.get(i).getLayoutY() <= 0) {
                    root.getChildren().remove(bBossShoot.get(i));
                    bBossShoot.remove(i);
                } else if (checkBarrierCollisionImage(bBossShoot.get(i))) {
                    root.getChildren().remove(bBossShoot.get(i));
                    bBossShoot.remove(i);
                }
            }
        }
    }
    public void enemiesShootUpdate() {
        if (!eShoots.isEmpty()) {
            for (int i = 0; i < eShoots.size(); i++) {
                eShoots.get(i).setLayoutY(eShoots.get(i).getLayoutY() + 3);
                if (eShoots.get(i).getLayoutY() <= 0) {
                    root.getChildren().remove(eShoots.get(i));
                    eShoots.remove(i);
                } else if (checkBarrierCollision(eShoots.get(i))) {
                    root.getChildren().remove(eShoots.get(i));
                    eShoots.remove(i);
                }
            }
        }
    }

    private boolean checkBarrierCollision(Circle shoot) {
        for (Barrier barrier : barriers) {
            int[][] matrix = barrier.getBarrier();
            Group group = getBarrierGroup((int) barrier.getLocationX());
            for (int row = 0; row < matrix.length; row++) {
                for (int j = 0; j < matrix[0].length; j++) {
                    if (matrix[row][j] != 0) {
                        if (shoot.getLayoutX() >= barrier.getLocationX() - 5 &&
                                shoot.getLayoutX() <= barrier.getLocationX() + j * rectangleSize + 5 &&
                                shoot.getLayoutY() >= barrier.getLocationY() - 5 &&
                                shoot.getLayoutY() <= barrier.getLocationY() + row * rectangleSize + 5) {
                            removeBrickFromBarrier(barrier, row, j);
                            barrier.deleteBricksAround(row, j);
                            renderBarrier((int) (barrier.getLocationX()), barrier, matrix, group);

                            // Check if the barrier has been completely destroyed
                            if (barrier.isDestroyed()) {
                                barriers.remove(barrier);
                            }

                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private void removeBrickFromBarrier(Barrier barrier, int row, int column) {
        Group group = getBarrierGroup((int) barrier.getLocationX());
        Rectangle brick = (Rectangle) group.getChildren().get(row * barrier.getBarrierWidth() + column);
        group.getChildren().remove(brick);
        barrier.getBarrier()[row][column] = 0; // Assuming 0 represents the absence of a brick
    }


    private boolean checkBarrierCollisionImage(ImageView shoot) {
        for (Barrier barrier : barriers) {
            int[][] matrix = barrier.getBarrier();
            for (int row = 0; row < matrix.length; row++) {
                for (int j = 0; j < matrix[0].length; j++) {
                    if (matrix[row][j] != 0) {
                        if (shoot.getLayoutX() >= barrier.getLocationX() - 5 &&
                                shoot.getLayoutX() <= barrier.getLocationX() + j * rectangleSize + 5 &&
                                shoot.getLayoutY() >= barrier.getLocationY() - 5 &&
                                shoot.getLayoutY() <= barrier.getLocationY() + row * rectangleSize + 5) {
                            barrier.deleteBricksAround(row, j);
                            Group group = getBarrierGroup((int) barrier.getLocationX());
                            renderBarrier((int) (barrier.getLocationX()), barrier, matrix, group);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public void enemiesMove() {
        double speed;

        if (toRight)
            speed = 0.3;
        else
            speed = - 0.3;
        if (dotR.getLayoutX() >= 40) {
            toRight = false;
            for (int i = 0; i < enemies.size(); i++) {
                enemies.get(i).setLayoutY(enemies.get(i).getLayoutY() + 8);
            }
        }
        if (dotR.getLayoutX() <= - 20) {
            toRight = true;
            for (int i = 0; i < enemies.size(); i++) {
                enemies.get(i).setLayoutY(enemies.get(i).getLayoutY() + 8);
            }
        }
        for (int i = 0; i < enemies.size(); i++) {
            enemies.get(i).setLayoutX(enemies.get(i).getLayoutX() + speed);
        }
        dotR.setLayoutX(dotR.getLayoutX() + speed);
    }
    public void bossMove() {
    double speed;

    if (bossToRight)
        speed = 1;
    else
        speed = - 1;

    if (bossShip.getLayoutX() >= 320) {
        bossToRight = false;
    }

    if (bossShip.getLayoutX() <= - 0) {
        bossToRight = true;
    }

    bossShip.setLayoutX(bossShip.getLayoutX() + speed);
    }
    public void isMonsterDestroyed() {
        for (int i = 0; i < pShoots.size(); i++) {
            for (int j = 0; j < enemies.size(); j++) {
                if ((pShoots.get(i).getLayoutX() > enemies.get(j).getLayoutX()) &&
                        (pShoots.get(i).getLayoutX() < enemies.get(j).getLayoutX() + 50) &&
                        (pShoots.get(i).getLayoutY() > enemies.get(j).getLayoutY()) &&
                        (pShoots.get(i).getLayoutY() < enemies.get(j).getLayoutY() + 50)) {
                    createExplosion(enemies.get(j).getLayoutX(), enemies.get(j).getLayoutY());
                    root.getChildren().remove(enemies.get(j));
                    enemies.remove(j);
                    root.getChildren().remove(pShoots.get(i));
                    pShoots.remove(i);
                    if (currentLevel == 1) {
                        numPoints += 100;
                        characterLogger.log(Level.INFO, "Player scored 100 points.");
                    }
                    if (currentLevel == 2){
                        numPoints += 200;
                        characterLogger.log(Level.INFO, "Player scored 200 points.");
                    }
                    points.setText("Points: " + String.valueOf(numPoints));
                    if (enemies.isEmpty()) {
                        currentLevel++;
                        level.setText("Level: " + String.valueOf(currentLevel));
                    }
                }
            }
        }
    }
    private void isBossDestroyed() {
    if (bossShip != null) {
        for (int i = 0; i < pShoots.size(); i++) {
            if (pShoots.get(i).getLayoutX() > bossShip.getLayoutX()
                    && pShoots.get(i).getLayoutX() < bossShip.getLayoutX() + bossShip.getW()
                    && pShoots.get(i).getLayoutY() > bossShip.getLayoutY()
                    && pShoots.get(i).getLayoutY() < bossShip.getLayoutY() + bossShip.getH()) {
                bossShip.increaseHitTaken();
                root.getChildren().remove(pShoots.get(i)); // Remove the shot
                pShoots.remove(i);
                if (bossShip.isDefeated()) {
                    root.getChildren().remove(bossShip);
                    boss.remove(bossShip);
                    bossShip = null; // Reset bossShip
                }
                break; // Exit the loop since the boss has been hit
            }
        }
    }
}

    private void isPlayerDestroyed() {
        for (int i = 0; i < eShoots.size(); i++) {
            if (eShoots.get(i).getLayoutX() > playerShip.getLayoutX()
                    && eShoots.get(i).getLayoutX() < playerShip.getLayoutX() + 50
                    && eShoots.get(i).getLayoutY() > playerShip.getLayoutY()
                    && eShoots.get(i).getLayoutY() < playerShip.getLayoutY() + 50) {
                root.getChildren().remove(eShoots.get(i));
                eShoots.remove(i);
                root.getChildren().remove(playerShip);
                numLives--;
                lives.setText("Lives: " + String.valueOf(numLives));
                if (numLives > 0)
                    root.getChildren().add(playerShip);

            }
        }
    }
        public void NextLevel(){
            switch (currentLevel) {
                case 1:
                    addEnemy();
                    break;
                case 2:
                    addEnemy2();
                    break;
                default:
                    isWin();
                    break;
                }
            }

        public void isWin(){
	    if(enemies.isEmpty()) {
		    Text text = new Text();
	        text.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 50));
	        text.setX(240);
	        text.setY(300);
	        text.setFill(Color.YELLOW);
	        text.setStrokeWidth(3);
	        text.setStroke(Color.GOLD);
	        text.setText("WIN");
	        root.getChildren().add(text);
	        timer.stop();
            logger.info("Player won the game.");
	    }
    }
        public void isLost(){
	    if(numLives <= 0) {
            logger.info("Player lost the game.");
		    Text text = new Text();
	        text.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 50));
	        text.setX(240);
	        text.setY(300);
	        text.setFill(Color.RED);
	        text.setStrokeWidth(3);
	        text.setStroke(Color.CRIMSON);
	        text.setText("LOST");
	        root.getChildren().add(text);
	        timer.stop();
	    }
    }
    private int SPACE = 50;
    private void setBarriers() {
        int startingX = 20;
        Barrier barrier1 = new Barrier();
        int[][] barrierMatrix1 = barrier1.getBarrier();
        Group group1 = getBarrierGroup(startingX);
        renderBarrier(startingX, barrier1, barrierMatrix1, group1);

        startingX += 3 * SPACE;
        Barrier barrier2 = new Barrier();
        int[][] barrierMatrix2 = barrier2.getBarrier();
        Group group2 = getBarrierGroup(startingX);
        renderBarrier(startingX, barrier2, barrierMatrix2, group2);

        startingX += 3 * SPACE;
        Barrier barrier3 = new Barrier();
        int[][] barrierMatrix3 = barrier3.getBarrier();
        Group group3 = getBarrierGroup(startingX);
        renderBarrier(startingX, barrier3, barrierMatrix3, group3);

        startingX += 3 * SPACE;
        Barrier barrier4 = new Barrier();
        int[][] barrierMatrix4 = barrier4.getBarrier();
        Group group4 = getBarrierGroup(startingX);
        renderBarrier(startingX, barrier4, barrierMatrix4, group4);
    }

    private Group getBarrierGroup(int x) {
        if (x <= 180) {
            return barrierGroup;
        } else if (x <= 300) {
            return secondBarrier;
        } else if (x <= 420) {
            return thirdBarrier;
        } else {
            return fourthBarrier;
        }
    }
    private void renderBarrier(int startingX, Barrier barrier, int[][] barrierMatrix, Group group) {
        barrier.setLocationX(startingX);
        barrier.setLocationY(APP_HEIGHT - 150);
        barriers.add(barrier);

        Color customColor = Color.web("#ddffa4");

        // Create and render the individual bricks based on the barrierMatrix
        for (int i = 0; i < barrierMatrix.length; i++) {
            for (int j = 0; j < barrierMatrix[0].length; j++) {
                if (barrierMatrix[i][j] == 1) { // Assuming 1 represents the presence of a brick
                    Rectangle brick = new Rectangle(startingX + j * rectangleSize, barrier.getLocationY() + i * rectangleSize, rectangleSize, rectangleSize);
                    brick.setFill(customColor); // Customize the color of the brick
                    group.getChildren().add(brick);
                }
            }
        }
    }

    private void createExplosion(double x, double y) {
        ImageView explosion = new ImageView(new Image(getClass().getResourceAsStream("assets/explosion.gif")));
        explosion.setLayoutX(x);
        explosion.setLayoutY(y);
        root.getChildren().add(explosion);
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), ev -> root.getChildren().remove(explosion)));
        timeline.play();
    }

    public void setPShoots(List<Circle> pShoots) {
        this.pShoots = pShoots;
    }

    public List<Circle> getPShoots() {
        return pShoots;
    }

    public void setEShoots(List<Circle> eShoots) {
        this.eShoots =eShoots;
    }
    public List<Circle> getEShoots() {
        return eShoots;
    }

    public void setBBossShoot(List<ImageView> bBossShoot) {
        this.bBossShoot = bBossShoot;
    }
    public List<ImageView> getBBossShoot() {
        return bBossShoot;
    }
}

