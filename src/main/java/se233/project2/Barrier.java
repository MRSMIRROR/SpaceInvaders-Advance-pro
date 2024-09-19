package se233.project2;

import javafx.scene.paint.Color;

public class Barrier {
    private int shootTaken;
    private int[][] barrier;
    private Color color;
    private double locationX;
    private double locationY;
    private int barrierWidth;

    public Barrier() {
        this.barrier = new int[][]{
                {0, 0, 0, 1, 0, 0, 1, 0, 0, 0},
                {0, 0, 1, 1, 1, 1, 1, 1, 0, 0},
                {0, 1, 1, 1, 1, 1, 1, 1, 1, 0},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {0, 1, 1, 1, 1, 1, 1, 1, 1, 0},
                {0, 0, 1, 0, 0, 0, 0, 1, 0, 0},
                {0, 1, 0, 1, 0, 0, 1, 0, 1, 0}
        };
        this.color = Color.rgb(105, 255, 194);
    }

    public int[][] getBarrier() {
        return barrier;
    }
    public int getBarrierWidth() {
        return barrierWidth;
    }


    public Color getColor() {
        return color;
    }

    public double getLocationX() {
        return locationX;
    }

    public double getLocationY() {
        return locationY;
    }

    public void deleteBricksAround(int row, int col) {
        barrier[row][col] = 0;
        if (row < barrier.length - 1) {
            barrier[row + 1][col] = 0;
            if (col < barrier[0].length - 1) {
                barrier[row][col + 1] = 0;
            }
            if (col > 0) {
                barrier[row][col - 1] = 0;
            }
        }
    }
    public boolean isDestroyed() {
        for (int[] row : barrier) {
            for (int brick : row) {
                if (brick != 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public void setLocationX(double locationX) {
        this.locationX = locationX;
    }

    public void setLocationY(double locationY) {
        this.locationY = locationY;
    }
    public void increaseShootTaken() {
        this.shootTaken++;
    }
    public int getShootTaken() {
        return shootTaken;
    }
}
