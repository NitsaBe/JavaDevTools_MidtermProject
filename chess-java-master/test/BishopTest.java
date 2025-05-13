import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Test class for Bishop movement rules
 */
public class BishopTest extends ChessTestBase {
    
    @Test
    public void testBishopMovement() {
        clearBoard();
        
        // Place white bishop in the middle of the board
        Bishop whiteBishop = (Bishop) placePiece(Bishop.class, 1, 4, 4, "wbishop.png");
        
        // Bishop should be able to move diagonally
        assertTrue(isLegalMove(whiteBishop, 2, 2)); // Top-left diagonal
        assertTrue(isLegalMove(whiteBishop, 6, 2)); // Top-right diagonal
        assertTrue(isLegalMove(whiteBishop, 2, 6)); // Bottom-left diagonal
        assertTrue(isLegalMove(whiteBishop, 6, 6)); // Bottom-right diagonal
        
        // Bishop should not be able to move horizontally or vertically
        assertFalse(isLegalMove(whiteBishop, 4, 0));
        assertFalse(isLegalMove(whiteBishop, 0, 4));
    }
    
    @Test
    public void testBishopBlocked() {
        clearBoard();
        
        // Place white bishop
        Bishop whiteBishop = (Bishop) placePiece(Bishop.class, 1, 4, 4, "wbishop.png");
        
        // Place blocking pieces
        placePiece(Pawn.class, 1, 2, 2, "wpawn.png"); // Same color blocking top-left
        placePiece(Pawn.class, 0, 6, 2, "bpawn.png"); // Different color blocking top-right
        
        // Bishop should not be able to move past same-color piece
        assertFalse(isLegalMove(whiteBishop, 1, 1));
        assertFalse(isLegalMove(whiteBishop, 0, 0));
        
        // Bishop should be able to capture opponent's piece but not move past it
        assertTrue(isLegalMove(whiteBishop, 6, 2));
        assertFalse(isLegalMove(whiteBishop, 7, 1));
    }
    
    @Test
    public void testBishopCapture() {
        clearBoard();
        
        // Place white bishop
        Bishop whiteBishop = (Bishop) placePiece(Bishop.class, 1, 4, 4, "wbishop.png");
        
        // Place opponent pieces for capture
        placePiece(Pawn.class, 0, 2, 2, "bpawn.png"); // Opponent piece top-left
        placePiece(Pawn.class, 0, 6, 6, "bpawn.png"); // Opponent piece bottom-right
        
        // Bishop should be able to capture these pieces
        assertTrue(isLegalMove(whiteBishop, 2, 2));
        assertTrue(isLegalMove(whiteBishop, 6, 6));
    }
} 