import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Test class for Knight movement rules
 */
public class KnightTest extends ChessTestBase {
    
    @Test
    public void testKnightMovement() {
        clearBoard();
        
        // Place white knight in the middle of the board
        Knight whiteKnight = (Knight) placePiece(Knight.class, 1, 4, 4, "wknight.png");
        
        // Knight should move in L shapes
        assertTrue(isLegalMove(whiteKnight, 2, 3)); // 2 left, 1 up
        assertTrue(isLegalMove(whiteKnight, 2, 5)); // 2 left, 1 down
        assertTrue(isLegalMove(whiteKnight, 6, 3)); // 2 right, 1 up
        assertTrue(isLegalMove(whiteKnight, 6, 5)); // 2 right, 1 down
        assertTrue(isLegalMove(whiteKnight, 3, 2)); // 1 left, 2 up
        assertTrue(isLegalMove(whiteKnight, 5, 2)); // 1 right, 2 up
        assertTrue(isLegalMove(whiteKnight, 3, 6)); // 1 left, 2 down
        assertTrue(isLegalMove(whiteKnight, 5, 6)); // 1 right, 2 down
        
        // Knight should not be able to move to other squares
        assertFalse(isLegalMove(whiteKnight, 4, 6)); // Straight down 2
        assertFalse(isLegalMove(whiteKnight, 6, 6)); // Diagonal down-right 2
    }
    
    @Test
    public void testKnightJumpsOverPieces() {
        clearBoard();
        
        // Place white knight
        Knight whiteKnight = (Knight) placePiece(Knight.class, 1, 4, 4, "wknight.png");
        
        // Place pieces in immediate surrounding (non-L positions)
        placePiece(Pawn.class, 1, 3, 4, "wpawn.png"); // Left
        placePiece(Pawn.class, 0, 4, 3, "bpawn.png"); // Up
        placePiece(Pawn.class, 1, 5, 4, "wpawn.png"); // Right
        placePiece(Pawn.class, 0, 4, 5, "bpawn.png"); // Down
        
        // Knight should still be able to move in L shapes (jumping over pieces)
        assertTrue(isLegalMove(whiteKnight, 2, 3));
        assertTrue(isLegalMove(whiteKnight, 6, 3));
        assertTrue(isLegalMove(whiteKnight, 2, 5));
        assertTrue(isLegalMove(whiteKnight, 6, 5));
    }
    
    @Test
    public void testKnightCapture() {
        clearBoard();
        
        // Place white knight
        Knight whiteKnight = (Knight) placePiece(Knight.class, 1, 4, 4, "wknight.png");
        
        // Place opponent pieces at L positions
        placePiece(Pawn.class, 0, 2, 3, "bpawn.png");
        placePiece(Pawn.class, 0, 6, 5, "bpawn.png");
        
        // Knight should be able to capture these pieces
        assertTrue(isLegalMove(whiteKnight, 2, 3));
        assertTrue(isLegalMove(whiteKnight, 6, 5));
        
        // Place same color pieces at other L positions
        placePiece(Pawn.class, 1, 3, 2, "wpawn.png");
        placePiece(Pawn.class, 1, 5, 6, "wpawn.png");
        
        // Knight should not be able to move to squares occupied by same color
        assertFalse(isLegalMove(whiteKnight, 3, 2));
        assertFalse(isLegalMove(whiteKnight, 5, 6));
    }
} 