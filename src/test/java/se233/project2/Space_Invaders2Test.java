package se233.project2;

import javafx.embed.swing.JFXPanel;
import javafx.scene.image.ImageView;
import org.junit.Before;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.*;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;

import java.util.List;


public class Space_Invaders2Test {
    private Space_Invaders2 spaceInvaders2;

    @Before
    public void setUp() {
        spaceInvaders2 = new Space_Invaders2();
        new JFXPanel();
        spaceInvaders2.addEnemy();
    }

    @Test
    public void testSpacialShoot() {
        Space_Invaders2 spaceInvaders2 = new Space_Invaders2();
        double x = 100;
        double y = 200;
        ImageView spacialShoot = spaceInvaders2.SpacialShoot(x, y);

        assertNotNull(spacialShoot);
        assertEquals(x, spacialShoot.getLayoutX());
        assertEquals(y, spacialShoot.getLayoutY());
        assertEquals(18, spacialShoot.getFitWidth());
        assertEquals(58, spacialShoot.getFitHeight());
    }

    @Test
    public void testShoot() {
        double x = 100;
        double y = 200;
        Circle expectedShoot = new Circle();
        expectedShoot.setRadius(5);
        expectedShoot.setFill(Color.GREENYELLOW);
        expectedShoot.setLayoutX(x);
        expectedShoot.setLayoutY(y);

        Space_Invaders2 spaceInvaders2 = new Space_Invaders2();
        Circle actualShoot = spaceInvaders2.shoot(x, y);

        assertEquals(expectedShoot.getRadius(), actualShoot.getRadius(), 0);
        assertEquals(expectedShoot.getFill(), actualShoot.getFill());
        assertEquals(expectedShoot.getLayoutX(), actualShoot.getLayoutX(), 0);
        assertEquals(expectedShoot.getLayoutY(), actualShoot.getLayoutY(), 0);
    }

    @Test
    public void testEnemiesMoveToLeft() {
        Space_Invaders2 spaceInvaders = new Space_Invaders2();
        spaceInvaders.toRight = false;
        double initialX = spaceInvaders.dotR.getLayoutX();
        spaceInvaders.enemiesMove();
        assertTrue(initialX > spaceInvaders.dotR.getLayoutX());
    }

    @Test
    public void testEnemiesMoveToRight() {
        Space_Invaders2 spaceInvaders = new Space_Invaders2();
        spaceInvaders.toRight = true;
        double initialX = spaceInvaders.dotR.getLayoutX();
        spaceInvaders.enemiesMove();
        assertTrue(initialX < spaceInvaders.dotR.getLayoutX());
    }

    @Test
    public void testRandomEnemyShootWithinRange() {
        Space_Invaders2 space_invaders2 = new Space_Invaders2();
        int min = 0;
        int max = 10;
        int result = space_invaders2.randomEnemyShoot(min, max);
        assertTrue(result >= min && result <= max, "The result should be within the specified range.");
    }

    @Test
    public void testRandomEnemyShootWithNegativeRange() {
        Space_Invaders2 space_invaders2 = new Space_Invaders2();
        int min = -10;
        int max = -5;
        int result = space_invaders2.randomEnemyShoot(min, max);
        assertTrue(result >= min && result <= max, "The result should be within the specified range.");
    }

    @Test
    public void testRandomEnemyShootWithLargeRange() {
        Space_Invaders2 space_invaders2 = new Space_Invaders2();
        int min = 0;
        int max = 100000;
        int result = space_invaders2.randomEnemyShoot(min, max);
        assertTrue(result >= min && result <= max, "The result should be within the specified range.");
    }

    @Test
    public void testEnemyShoot() {
        int initialSize = spaceInvaders2.eShoots.size();
        spaceInvaders2.enemyShoot();
        int finalSize = spaceInvaders2.eShoots.size();
        assertEquals(initialSize + 1, finalSize, "New enemy shoot should be added.");
    }

    @Test
    public void testEnemyShootCoordinates() {
        spaceInvaders2.enemyShoot();
        List<Circle> eShoots = spaceInvaders2.getEShoots();
        Circle lastShoot = eShoots.get(eShoots.size() - 1);
        assertTrue(lastShoot.getLayoutX() > 0, "Shoot layout X should be greater than 0.");
        assertTrue(lastShoot.getLayoutY() > 0, "Shoot layout Y should be greater than 0.");
    }

    @Test
    public void testIsMonsterDestroyed() {
        // Setting up initial conditions
        spaceInvaders2.currentLevel = 1;
        spaceInvaders2.numPoints = 0;
        Circle pShoot = new Circle();
        pShoot.setLayoutX(40);
        pShoot.setLayoutY(50);
        spaceInvaders2.pShoots.add(pShoot);
        EnemyShip enemy = new EnemyShip(40, 50, 50, 50, null);
        spaceInvaders2.enemies.add(enemy);

        spaceInvaders2.isMonsterDestroyed();

        // Verifying the expected changes
        // assertTrue(spaceInvaders2.enemies.isEmpty(), "Enemy should have been removed.");
        // assertTrue(spaceInvaders2.pShoots.isEmpty(), "Player shoot should have been removed.");
        assertEquals(100, spaceInvaders2.numPoints, "Points should have increased by 100.");
        assertEquals("Points: 100", spaceInvaders2.points.getText(), "Points label should be updated.");
        assertEquals("Level: 2", spaceInvaders2.level.getText(), "Level label should be updated.");
    }
}

