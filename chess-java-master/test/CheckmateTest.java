import static org.junit.Assert.*;
import org.junit.Test;
import java.util.List;

/**
 * Test class for check and checkmate scenarios
 */
public class CheckmateTest extends ChessTestBase {
    
    @Test
    public void testKingInCheck() {
        clearBoard();
        
        // Place kings
        King whiteKing = (King) placePiece(King.class, 1, 4, 7, "wking.png");
        King blackKing = (King) placePiece(King.class, 0, 4, 0, "bking.png");
        
        // Place a black rook to put white king in check
        Rook blackRook = (Rook) placePiece(Rook.class, 0, 4, 5, "brook.png");
        
        // Initialize checkmate detector
        CheckmateDetector cmd = new CheckmateDetector(testBoard, testBoard.Wpieces, testBoard.Bpieces, whiteKing, blackKing);
        
        // White king should be in check
        assertTrue(cmd.whiteInCheck());
        
        // Black king should not be in check
        assertFalse(cmd.blackInCheck());
    }
    
    @Test
    public void testBlockingCheck() {
        clearBoard();
        
        // Place kings
        King whiteKing = (King) placePiece(King.class, 1, 4, 7, "wking.png");
        King blackKing = (King) placePiece(King.class, 0, 4, 0, "bking.png");
        
        // Place a black rook to put white king in check
        Rook blackRook = (Rook) placePiece(Rook.class, 0, 4, 5, "brook.png");
        
        // Place a white piece that can block the check
        Queen whiteQueen = (Queen) placePiece(Queen.class, 1, 2, 7, "wqueen.png");
        
        // Initialize checkmate detector
        CheckmateDetector cmd = new CheckmateDetector(testBoard, testBoard.Wpieces, testBoard.Bpieces, whiteKing, blackKing);
        
        // White king should be in check
        assertTrue(cmd.whiteInCheck());
        
        // Should not be checkmate since the queen can block
        assertFalse(cmd.whiteCheckMated());
        
        // Get legal moves for white queen
        List<Square> queenMoves = whiteQueen.getLegalMoves(testBoard);
        
        // Verify queen can move to block the check
        Square blockingSquare = testBoard.getSquareArray()[6][4]; // Square between king and rook
        assertTrue(queenMoves.contains(blockingSquare));
        
        // Verify this move would be valid according to the checkmate detector
        assertTrue(cmd.testMove(whiteQueen, blockingSquare));
    }
    
    @Test
    public void testCapturingAttacker() {
        clearBoard();
        
        // Place kings
        King whiteKing = (King) placePiece(King.class, 1, 4, 7, "wking.png");
        King blackKing = (King) placePiece(King.class, 0, 4, 0, "bking.png");
        
        // Place a black bishop to put white king in check diagonally
        Bishop blackBishop = (Bishop) placePiece(Bishop.class, 0, 7, 4, "bbishop.png");
        
        // Place a white knight that can capture the bishop
        Knight whiteKnight = (Knight) placePiece(Knight.class, 1, 5, 5, "wknight.png");
        
        // Initialize checkmate detector
        CheckmateDetector cmd = new CheckmateDetector(testBoard, testBoard.Wpieces, testBoard.Bpieces, whiteKing, blackKing);
        
        // White king should be in check
        assertTrue(cmd.whiteInCheck());
        
        // Should not be checkmate since the knight can capture the bishop
        assertFalse(cmd.whiteCheckMated());
        
        // Get legal moves for white knight
        List<Square> knightMoves = whiteKnight.getLegalMoves(testBoard);
        
        // Verify knight can move to capture the bishop
        Square captureSquare = testBoard.getSquareArray()[4][7]; // Position of black bishop
        assertTrue(knightMoves.contains(captureSquare));
        
        // Verify this move would be valid according to the checkmate detector
        assertTrue(cmd.testMove(whiteKnight, captureSquare));
    }
    
    @Test
    public void testCheckmate() {
        clearBoard();
        
        // Place kings
        King whiteKing = (King) placePiece(King.class, 1, 7, 7, "wking.png");
        King blackKing = (King) placePiece(King.class, 0, 0, 0, "bking.png");
        
        // Set up checkmate scenario with two rooks
        Rook blackRook1 = (Rook) placePiece(Rook.class, 0, 6, 7, "brook.png");
        Rook blackRook2 = (Rook) placePiece(Rook.class, 0, 7, 6, "brook.png");
        
        // Initialize checkmate detector
        CheckmateDetector cmd = new CheckmateDetector(testBoard, testBoard.Wpieces, testBoard.Bpieces, whiteKing, blackKing);
        
        // White king should be in check
        assertTrue(cmd.whiteInCheck());
        
        // White king should be in checkmate
        assertTrue(cmd.whiteCheckMated());
    }
    
    @Test
    public void testStalemate() {
        clearBoard();
        
        // Place white king in the corner
        King whiteKing = (King) placePiece(King.class, 1, 0, 0, "wking.png");
        King blackKing = (King) placePiece(King.class, 0, 4, 4, "bking.png");
        
        // Place black queen to create stalemate position
        Queen blackQueen = (Queen) placePiece(Queen.class, 0, 1, 2, "bqueen.png");
        
        // Initialize checkmate detector
        CheckmateDetector cmd = new CheckmateDetector(testBoard, testBoard.Wpieces, testBoard.Bpieces, whiteKing, blackKing);
        
        // White king should not be in check
        assertFalse(cmd.whiteInCheck());
        
        // Get legal moves for white king
        List<Square> kingMoves = whiteKing.getLegalMoves(testBoard);
        
        // Verify the king has no legal moves (stalemate)
        for (Square square : kingMoves) {
            assertFalse("King should not have any legal moves in stalemate", 
                cmd.testMove(whiteKing, square));
        }
    }
} 