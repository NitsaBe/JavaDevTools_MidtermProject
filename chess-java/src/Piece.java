

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
    
//    public int[] getLinearOccupations(Square[][] board, int x, int y) {
//        int lastYabove = 0;
//        int lastXright = 7;
//        int lastYbelow = 7;
//        int lastXleft = 0;
//
//        for (int i = 0; i < y; i++) {
//            if (board[i][x].isOccupied()) {
//                if (board[i][x].getOccupyingPiece().getColor() != this.color) {
//                    lastYabove = i;
//                } else lastYabove = i + 1;
//            }
//        }
//
//        for (int i = 7; i > y; i--) {
//            if (board[i][x].isOccupied()) {
//                if (board[i][x].getOccupyingPiece().getColor() != this.color) {
//                    lastYbelow = i;
//                } else lastYbelow = i - 1;
//            }
//        }
//
//        for (int i = 0; i < x; i++) {
//            if (board[y][i].isOccupied()) {
//                if (board[y][i].getOccupyingPiece().getColor() != this.color) {
//                    lastXleft = i;
//                } else lastXleft = i + 1;
//            }
//        }
//
//        for (int i = 7; i > x; i--) {
//            if (board[y][i].isOccupied()) {
//                if (board[y][i].getOccupyingPiece().getColor() != this.color) {
//                    lastXright = i;
//                } else lastXright = i - 1;
//            }
//        }
//
//        int[] occups = {lastYabove, lastYbelow, lastXleft, lastXright};
//
//        return occups;
//    }

    public int[] getLinearOccupations(Square[][] board, int x, int y) {
        return new int[] {
                findBoundary(board, x, y, Direction.UP),
                findBoundary(board, x, y, Direction.DOWN),
                findBoundary(board, x, y, Direction.LEFT),
                findBoundary(board, x, y, Direction.RIGHT)
        };
    }

    private enum Direction { UP, DOWN, LEFT, RIGHT }

    private int findBoundary(Square[][] board, int x, int y, Direction dir) {
        int start=0, end=0, step=0;
        boolean vertical;
        vertical=false;

        switch (dir) {
            case UP: {
                start = y - 1;
                end = -1;
                step = -1;
                vertical = true;
                break;
            }
            case DOWN: {
                start = y + 1;
                end = 8;
                step = 1;
                vertical = true;
                break;
            }
            case LEFT: {
                start = x - 1;
                end = -1;
                step = -1;
                break;

            }
            case RIGHT: {
                start = x + 1;
                end = 8;
                step = 1;
                break;
            }
        }



        for (int i = start; (step > 0) ? i < end : i > end; i += step) {
            Square square = vertical ? board[i][x] : board[y][i];
            if (square.isOccupied()) {
                return square.getOccupyingPiece().getColor() != this.color ? i :
                        i + (step > 0 ? 1 : -1);
            }
        }

        return (step > 0) ? end - 1 : end + 1;
    }

//    public List<Square> getDiagonalOccupations(Square[][] board, int x, int y) {
//        LinkedList<Square> diagOccup = new LinkedList<Square>();
//
//        int xNW = x - 1;
//        int xSW = x - 1;
//        int xNE = x + 1;
//        int xSE = x + 1;
//        int yNW = y - 1;
//        int ySW = y + 1;
//        int yNE = y - 1;
//        int ySE = y + 1;
//
//        while (xNW >= 0 && yNW >= 0) {
//            if (board[yNW][xNW].isOccupied()) {
//                if (board[yNW][xNW].getOccupyingPiece().getColor() == this.color) {
//                    break;
//                } else {
//                    diagOccup.add(board[yNW][xNW]);
//                    break;
//                }
//            } else {
//                diagOccup.add(board[yNW][xNW]);
//                yNW--;
//                xNW--;
//            }
//        }
//
//        while (xSW >= 0 && ySW < 8) {
//            if (board[ySW][xSW].isOccupied()) {
//                if (board[ySW][xSW].getOccupyingPiece().getColor() == this.color) {
//                    break;
//                } else {
//                    diagOccup.add(board[ySW][xSW]);
//                    break;
//                }
//            } else {
//                diagOccup.add(board[ySW][xSW]);
//                ySW++;
//                xSW--;
//            }
//        }
//
//        while (xSE < 8 && ySE < 8) {
//            if (board[ySE][xSE].isOccupied()) {
//                if (board[ySE][xSE].getOccupyingPiece().getColor() == this.color) {
//                    break;
//                } else {
//                    diagOccup.add(board[ySE][xSE]);
//                    break;
//                }
//            } else {
//                diagOccup.add(board[ySE][xSE]);
//                ySE++;
//                xSE++;
//            }
//        }
//
//        while (xNE < 8 && yNE >= 0) {
//            if (board[yNE][xNE].isOccupied()) {
//                if (board[yNE][xNE].getOccupyingPiece().getColor() == this.color) {
//                    break;
//                } else {
//                    diagOccup.add(board[yNE][xNE]);
//                    break;
//                }
//            } else {
//                diagOccup.add(board[yNE][xNE]);
//                yNE--;
//                xNE++;
//            }
//        }
//
//        return diagOccup;
//    }

    public List<Square> getDiagonalOccupations(Square[][] board, int x, int y) {
        List<Square> diagOccup = new LinkedList<>();

        // Check all four diagonal directions
        exploreDirection(board, x, y, -1, -1, diagOccup); // Northwest
        exploreDirection(board, x, y, -1, 1, diagOccup);  // Southwest
        exploreDirection(board, x, y, 1, 1, diagOccup);   // Southeast
        exploreDirection(board, x, y, 1, -1, diagOccup);  // Northeast

        return diagOccup;
    }

    private void exploreDirection(Square[][] board, int startX, int startY,
                                  int xStep, int yStep, List<Square> result) {
        int currentX = startX + xStep;
        int currentY = startY + yStep;

        while (currentX >= 0 && currentX < 8 && currentY >= 0 && currentY < 8) {
            Square square = board[currentY][currentX];

            if (square.isOccupied()) {
                if (square.getOccupyingPiece().getColor() != this.color) {
                    result.add(square); // Include opponent piece
                }
                break;
            }

            result.add(square);
            currentX += xStep;
            currentY += yStep;
        }
    }
    
    // No implementation, to be implemented by each subclass
    public abstract List<Square> getLegalMoves(Board b);
}