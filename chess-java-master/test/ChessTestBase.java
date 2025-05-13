import static org.junit.Assert.*;
import org.junit.Before;
import java.util.List;

/**
 * Base class for chess piece tests providing common functionality
 */
public abstract class ChessTestBase {
    
    protected Board testBoard;
    
    @Before
    public void setUp() {
        // Create a minimal game window for testing
        GameWindow gameWindow = new GameWindow("TestBlack", "TestWhite", 0, 0, 0);
        testBoard = new Board(gameWindow);
    }
    
    /**
     * Helper method to clear the board of all pieces
     */
    protected void clearBoard() {
        Square[][] squares = testBoard.getSquareArray();
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if (squares[y][x].isOccupied()) {
                    squares[y][x].removePiece();
                }
            }
        }
        testBoard.Wpieces.clear();
        testBoard.Bpieces.clear();
    }
    
    /**
     * Helper method to place a piece on the board at a specific position
     */
    protected Piece placePiece(Class<? extends Piece> pieceClass, int color, int x, int y, String resource) {
        Square[][] squares = testBoard.getSquareArray();
        try {
            Piece piece = pieceClass.getDeclaredConstructor(int.class, Square.class, String.class)
                .newInstance(color, squares[y][x], resource);
            squares[y][x].put(piece);
            
            // Add the piece to the appropriate list
            if (color == 0) { // BLACK
                testBoard.Bpieces.add(piece);
            } else { // WHITE
                testBoard.Wpieces.add(piece);
            }
            return piece;
        } catch (Exception e) {
            fail("Failed to place piece: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Helper method to verify if a square is in a piece's legal moves
     */
    protected boolean isLegalMove(Piece piece, int targetX, int targetY) {
        Square[][] squares = testBoard.getSquareArray();
        List<Square> legalMoves = piece.getLegalMoves(testBoard);
        return legalMoves.contains(squares[targetY][targetX]);
    }
} 