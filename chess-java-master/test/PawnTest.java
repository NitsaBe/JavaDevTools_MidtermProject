import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Test class for Pawn movement rules
 */
public class PawnTest extends ChessTestBase {
    
    @Test
    public void testPawnInitialMove() {
        clearBoard();
        
        // Place white pawn at starting position
        Pawn whitePawn = (Pawn) placePiece(Pawn.class, 1, 4, 6, "wpawn.png");
        
        // White pawn should be able to move 1 or 2 spaces forward from initial position
        assertTrue(isLegalMove(whitePawn, 4, 5)); // Move 1 space
        assertTrue(isLegalMove(whitePawn, 4, 4)); // Move 2 spaces
        
        // Place black pawn at starting position
        Pawn blackPawn = (Pawn) placePiece(Pawn.class, 0, 4, 1, "bpawn.png");
        
        // Black pawn should be able to move 1 or 2 spaces forward from initial position
        assertTrue(isLegalMove(blackPawn, 4, 2)); // Move 1 space
        assertTrue(isLegalMove(blackPawn, 4, 3)); // Move 2 spaces
    }
    
    @Test
    public void testPawnCapture() {
        clearBoard();
        
        // Place white pawn
        Pawn whitePawn = (Pawn) placePiece(Pawn.class, 1, 4, 4, "wpawn.png");
        
        // Place black pieces for capture
        placePiece(Pawn.class, 0, 3, 3, "bpawn.png");
        placePiece(Pawn.class, 0, 5, 3, "bpawn.png");
        
        // White pawn should be able to capture diagonally
        assertTrue(isLegalMove(whitePawn, 3, 3)); // Capture left
        assertTrue(isLegalMove(whitePawn, 5, 3)); // Capture right
        
        // Test black pawn captures
        clearBoard();
        Pawn blackPawn = (Pawn) placePiece(Pawn.class, 0, 4, 4, "bpawn.png");
        
        // Place white pieces for capture
        placePiece(Pawn.class, 1, 3, 5, "wpawn.png");
        placePiece(Pawn.class, 1, 5, 5, "wpawn.png");
        
        // Black pawn should be able to capture diagonally
        assertTrue(isLegalMove(blackPawn, 3, 5)); // Capture left
        assertTrue(isLegalMove(blackPawn, 5, 5)); // Capture right
    }
    
    @Test
    public void testPawnBlocked() {
        clearBoard();
        
        // Place white pawn
        Pawn whitePawn = (Pawn) placePiece(Pawn.class, 1, 4, 6, "wpawn.png");
        
        // Place blocking piece directly in front
        placePiece(Pawn.class, 0, 4, 5, "bpawn.png");
        
        // Pawn should not be able to move forward when blocked
        assertFalse(isLegalMove(whitePawn, 4, 5));
        assertFalse(isLegalMove(whitePawn, 4, 4));
    }
    
    @Test
    public void testPawnNonCaptureDiagonal() {
        clearBoard();
        
        // Place white pawn in middle of board
        Pawn whitePawn = (Pawn) placePiece(Pawn.class, 1, 4, 4, "wpawn.png");
        
        // Pawn should not be able to move diagonally without capturing
        assertFalse(isLegalMove(whitePawn, 3, 3));
        assertFalse(isLegalMove(whitePawn, 5, 3));
    }
} 