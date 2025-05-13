import static org.junit.Assert.*;
import org.junit.Test;

import java.util.List;

/**
 * Test class for King movement rules
 */
public class KingTest extends ChessTestBase {
    
    @Test
    public void testKingMovement() {
        clearBoard();
        
        // Place white king in the middle of the board
        King whiteKing = (King) placePiece(King.class, 1, 4, 4, "wking.png");
        
        // King should be able to move one square in any direction
        assertTrue(isLegalMove(whiteKing, 3, 3)); // Diagonal up-left
        assertTrue(isLegalMove(whiteKing, 4, 3)); // Up
        assertTrue(isLegalMove(whiteKing, 5, 3)); // Diagonal up-right
        assertTrue(isLegalMove(whiteKing, 3, 4)); // Left
        assertTrue(isLegalMove(whiteKing, 5, 4)); // Right
        assertTrue(isLegalMove(whiteKing, 3, 5)); // Diagonal down-left
        assertTrue(isLegalMove(whiteKing, 4, 5)); // Down
        assertTrue(isLegalMove(whiteKing, 5, 5)); // Diagonal down-right
        
        // King should not be able to move more than one square
        assertFalse(isLegalMove(whiteKing, 2, 2)); // Two squares diagonally
        assertFalse(isLegalMove(whiteKing, 4, 2)); // Two squares vertically
        assertFalse(isLegalMove(whiteKing, 6, 4)); // Two squares horizontally
    }
    
    @Test
    public void testKingCapture() {
        clearBoard();
        
        // Place white king
        King whiteKing = (King) placePiece(King.class, 1, 4, 4, "wking.png");
        
        // Place opponent pieces for capture
        placePiece(Pawn.class, 0, 3, 3, "bpawn.png"); // Diagonal up-left
        placePiece(Pawn.class, 0, 5, 5, "bpawn.png"); // Diagonal down-right
        
        // King should be able to capture these pieces
        assertTrue(isLegalMove(whiteKing, 3, 3));
        assertTrue(isLegalMove(whiteKing, 5, 5));
        
        // Place same color pieces
        placePiece(Pawn.class, 1, 4, 3, "wpawn.png"); // Up
        placePiece(Pawn.class, 1, 5, 4, "wpawn.png"); // Right
        
        // King should not be able to move to squares occupied by same color
        assertFalse(isLegalMove(whiteKing, 4, 3));
        assertFalse(isLegalMove(whiteKing, 5, 4));
    }
    
    @Test
    public void testKingCannotMoveIntoCheck() {
        clearBoard();
        
        // Place kings
        King whiteKing = (King) placePiece(King.class, 1, 4, 7, "wking.png");
        King blackKing = (King) placePiece(King.class, 0, 4, 0, "bking.png");
        
        // Place black rook to create a check scenario
        Rook blackRook = (Rook) placePiece(Rook.class, 0, 3, 5, "brook.png");
        
        // Initialize the checkmate detector
        CheckmateDetector cmd = new CheckmateDetector(testBoard, testBoard.Wpieces, testBoard.Bpieces, whiteKing, blackKing);
        
        // Get the legal moves for the white king
        List<Square> kingMoves = whiteKing.getLegalMoves(testBoard);
        
        // King should not be able to move to any square in the same file as the rook
        Square[][] squares = testBoard.getSquareArray();
        assertFalse(kingMoves.contains(squares[6][3]));
    }
} 