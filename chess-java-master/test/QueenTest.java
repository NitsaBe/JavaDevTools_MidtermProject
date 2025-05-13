import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Test class for Queen movement rules
 */
public class QueenTest extends ChessTestBase {
    
    @Test
    public void testQueenMovement() {
        clearBoard();
        
        // Place white queen in the middle of the board
        Queen whiteQueen = (Queen) placePiece(Queen.class, 1, 4, 4, "wqueen.png");
        
        // Queen should be able to move diagonally
        assertTrue(isLegalMove(whiteQueen, 2, 2)); // Top-left diagonal
        assertTrue(isLegalMove(whiteQueen, 6, 2)); // Top-right diagonal
        assertTrue(isLegalMove(whiteQueen, 2, 6)); // Bottom-left diagonal
        assertTrue(isLegalMove(whiteQueen, 6, 6)); // Bottom-right diagonal
        
        // Queen should be able to move horizontally and vertically
        assertTrue(isLegalMove(whiteQueen, 4, 0)); // Up
        assertTrue(isLegalMove(whiteQueen, 0, 4)); // Left
        assertTrue(isLegalMove(whiteQueen, 7, 4)); // Right
        assertTrue(isLegalMove(whiteQueen, 4, 7)); // Down
    }
    
    @Test
    public void testQueenBlocked() {
        clearBoard();
        
        // Place white queen
        Queen whiteQueen = (Queen) placePiece(Queen.class, 1, 4, 4, "wqueen.png");
        
        // Place blocking pieces
        placePiece(Pawn.class, 1, 2, 2, "wpawn.png"); // Same color blocking top-left diagonal
        placePiece(Pawn.class, 0, 4, 2, "bpawn.png"); // Different color blocking upward
        
        // Queen should not be able to move past same-color piece
        assertFalse(isLegalMove(whiteQueen, 1, 1));
        assertFalse(isLegalMove(whiteQueen, 0, 0));
        
        // Queen should be able to capture opponent's piece but not move past it
        assertTrue(isLegalMove(whiteQueen, 4, 2));
        assertFalse(isLegalMove(whiteQueen, 4, 1));
        assertFalse(isLegalMove(whiteQueen, 4, 0));
    }
    
    @Test
    public void testQueenCapture() {
        clearBoard();
        
        // Place white queen
        Queen whiteQueen = (Queen) placePiece(Queen.class, 1, 4, 4, "wqueen.png");
        
        // Place opponent pieces for capture
        placePiece(Pawn.class, 0, 2, 2, "bpawn.png"); // Diagonal
        placePiece(Pawn.class, 0, 4, 1, "bpawn.png"); // Vertical
        placePiece(Pawn.class, 0, 7, 4, "bpawn.png"); // Horizontal
        
        // Queen should be able to capture these pieces
        assertTrue(isLegalMove(whiteQueen, 2, 2));
        assertTrue(isLegalMove(whiteQueen, 4, 1));
        assertTrue(isLegalMove(whiteQueen, 7, 4));
    }
} 