import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Test class for Rook movement rules
 */
public class RookTest extends ChessTestBase {
    
    @Test
    public void testRookMovement() {
        clearBoard();
        
        // Place white rook in the middle of the board
        Rook whiteRook = (Rook) placePiece(Rook.class, 1, 4, 4, "wrook.png");
        
        // Rook should be able to move horizontally and vertically
        // Vertical movement
        assertTrue(isLegalMove(whiteRook, 4, 0));
        assertTrue(isLegalMove(whiteRook, 4, 7));
        
        // Horizontal movement
        assertTrue(isLegalMove(whiteRook, 0, 4));
        assertTrue(isLegalMove(whiteRook, 7, 4));
        
        // Rook should not be able to move diagonally
        assertFalse(isLegalMove(whiteRook, 3, 3));
        assertFalse(isLegalMove(whiteRook, 5, 5));
    }
    
    @Test
    public void testRookBlocked() {
        clearBoard();
        
        // Place white rook
        Rook whiteRook = (Rook) placePiece(Rook.class, 1, 4, 4, "wrook.png");
        
        // Place blocking pieces
        placePiece(Pawn.class, 1, 4, 2, "wpawn.png"); // Same color blocking up
        placePiece(Pawn.class, 0, 6, 4, "bpawn.png"); // Different color blocking right
        
        // Rook should not be able to move past same-color piece
        assertFalse(isLegalMove(whiteRook, 4, 1));
        assertFalse(isLegalMove(whiteRook, 4, 0));
        
        // Rook should be able to capture opponent's piece but not move past it
        assertTrue(isLegalMove(whiteRook, 6, 4));
        assertFalse(isLegalMove(whiteRook, 7, 4));
    }
    
    @Test
    public void testRookCapture() {
        clearBoard();
        
        // Place white rook
        Rook whiteRook = (Rook) placePiece(Rook.class, 1, 4, 4, "wrook.png");
        
        // Place opponent pieces for capture
        placePiece(Pawn.class, 0, 4, 1, "bpawn.png"); // Opponent piece up
        placePiece(Pawn.class, 0, 1, 4, "bpawn.png"); // Opponent piece left
        
        // Rook should be able to capture these pieces
        assertTrue(isLegalMove(whiteRook, 4, 1));
        assertTrue(isLegalMove(whiteRook, 1, 4));
    }
} 