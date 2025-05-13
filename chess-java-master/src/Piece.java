import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;

public abstract class Piece {
    private final int color;
    private Square currentSquare;
    private BufferedImage img;
    
    // Direction constants for traversal
    private static final int UP = 0;
    private static final int DOWN = 1;
    private static final int LEFT = 2;
    private static final int RIGHT = 3;
    
    public Piece(int color, Square initSq, String img_file) {
        this.color = color;
        this.currentSquare = initSq;
        
        try {
            if (this.img == null) {
              this.img = ImageIO.read(getClass().getResource(img_file));
            }
          } catch (IOException e) {
            System.out.println("File not found: " + e.getMessage());
          }
    }
    
    public boolean move(Square fin) {
        Piece occup = fin.getOccupyingPiece();
        
        if (occup != null) {
            if (occup.getColor() == this.color) return false;
            else fin.capture(this);
        }
        
        currentSquare.removePiece();
        this.currentSquare = fin;
        currentSquare.put(this);
        return true;
    }
    
    public Square getPosition() {
        return currentSquare;
    }
    
    public void setPosition(Square sq) {
        this.currentSquare = sq;
    }
    
    public int getColor() {
        return color;
    }
    
    public Image getImage() {
        return img;
    }
    
    public void draw(Graphics g) {
        int x = currentSquare.getX();
        int y = currentSquare.getY();
        
        g.drawImage(this.img, x, y, null);
    }
    
    /**
     * Helper method to check for occupations in a given direction
     * 
     * @param board The game board
     * @param x Starting x coordinate
     * @param y Starting y coordinate
     * @param direction Direction to check (UP, DOWN, LEFT, RIGHT)
     * @return The furthest reachable position in that direction
     */
    private int findOccupationLimit(Square[][] board, int x, int y, int direction) {
        int limit;
        int start, end, step;
        boolean isVertical = (direction == UP || direction == DOWN);
        
        // Set up traversal parameters based on direction
        switch (direction) {
            case UP:
                start = 0;
                end = y;
                step = 1;
                limit = 0;
                break;
            case DOWN:
                start = 7;
                end = y;
                step = -1;
                limit = 7;
                break;
            case LEFT:
                start = 0;
                end = x;
                step = 1;
                limit = 0;
                break;
            case RIGHT:
                start = 7;
                end = x;
                step = -1;
                limit = 7;
                break;
            default:
                return -1; // Invalid direction
        }
        
        // Traverse board in the specified direction
        for (int i = start; (step > 0) ? i < end : i > end; i += step) {
            Square square = isVertical ? board[i][x] : board[y][i];
            
            if (square.isOccupied()) {
                if (square.getOccupyingPiece().getColor() != this.color) {
                    limit = i;
                } else {
                    limit = (step > 0) ? i + 1 : i - 1;
                }
            }
        }
        
        return limit;
    }
    
    /**
     * Finds limits of movement in linear directions (horizontal and vertical)
     */
    public int[] getLinearOccupations(Square[][] board, int x, int y) {
        int lastYabove = findOccupationLimit(board, x, y, UP);
        int lastYbelow = findOccupationLimit(board, x, y, DOWN);
        int lastXleft = findOccupationLimit(board, x, y, LEFT);
        int lastXright = findOccupationLimit(board, x, y, RIGHT);
        
        int[] occups = {lastYabove, lastYbelow, lastXleft, lastXright};
        
        return occups;
    }
    
    /**
     * Finds all squares that can be reached diagonally
     */
    public List<Square> getDiagonalOccupations(Square[][] board, int x, int y) {
        LinkedList<Square> diagOccup = new LinkedList<Square>();
        
        // Collect all reachable squares in each diagonal direction
        collectDiagonalSquares(board, x, y, -1, -1, diagOccup); // Northwest
        collectDiagonalSquares(board, x, y, -1, 1, diagOccup);  // Southwest
        collectDiagonalSquares(board, x, y, 1, 1, diagOccup);   // Southeast
        collectDiagonalSquares(board, x, y, 1, -1, diagOccup);  // Northeast
        
        return diagOccup;
    }
    
    /**
     * Helper method to collect squares in a diagonal direction
     */
    private void collectDiagonalSquares(Square[][] board, int startX, int startY, 
                                        int deltaX, int deltaY, List<Square> squares) {
        int currentX = startX + deltaX;
        int currentY = startY + deltaY;
        
        while (currentX >= 0 && currentX < 8 && currentY >= 0 && currentY < 8) {
            Square square = board[currentY][currentX];
            
            if (square.isOccupied()) {
                if (square.getOccupyingPiece().getColor() == this.color) {
                    break;
                } else {
                    squares.add(square);
                    break;
                }
            } else {
                squares.add(square);
                currentY += deltaY;
                currentX += deltaX;
            }
        }
    }
    
    // No implementation, to be implemented by each subclass
    public abstract List<Square> getLegalMoves(Board b);
}