package se233.project2;

import javafx.embed.swing.JFXPanel;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CharacterTest {
    private Character floatingCharacter;
    @Before
    public void setup() {
        JFXPanel jfxPanel = new JFXPanel();
        floatingCharacter = new Character(275, 720, 50, 50);
    }
    @Test
    public void characterInitialValuesShouldMatchConstructorArguments() {
        assertEquals("Initial x", 275, floatingCharacter.getX(), 0);
        assertEquals("Initial y", 720, floatingCharacter.getY(), 0);
        assertEquals("initial w", 50, floatingCharacter.getW(), 0);
        assertEquals("initial h", 50, floatingCharacter.getH(), 0);
    }
}