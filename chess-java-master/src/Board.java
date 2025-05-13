import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.*;

@SuppressWarnings("serial")
public class Board extends JPanel implements MouseListener, MouseMotionListener {
	// Resource location constants for piece images
    private static final String RESOURCES_WBISHOP_PNG = "wbishop.png";
	private static final String RESOURCES_BBISHOP_PNG = "bbishop.png";
	private static final String RESOURCES_WKNIGHT_PNG = "wknight.png";
	private static final String RESOURCES_BKNIGHT_PNG = "bknight.png";
	private static final String RESOURCES_WROOK_PNG = "wrook.png";
	private static final String RESOURCES_BROOK_PNG = "brook.png";
	private static final String RESOURCES_WKING_PNG = "wking.png";
	private static final String RESOURCES_BKING_PNG = "bking.png";
	private static final String RESOURCES_BQUEEN_PNG = "bqueen.png";
	private static final String RESOURCES_WQUEEN_PNG = "wqueen.png";
	private static final String RESOURCES_WPAWN_PNG = "wpawn.png";
	private static final String RESOURCES_BPAWN_PNG = "bpawn.png";
    
    // Piece color constants
    private static final int BLACK = 0;
    private static final int WHITE = 1;
	
	// Logical and graphical representations of board
	private final Square[][] board;
    private final GameWindow g;
    
    // List of pieces and whether they are movable
    public final LinkedList<Piece> Bpieces;
    public final LinkedList<Piece> Wpieces;
    public List<Square> movable;
    
    private boolean whiteTurn;

    private Piece currPiece;
    private int currX;
    private int currY;
    
    private CheckmateDetector cmd;
    
    public Board(GameWindow g) {
        this.g = g;
        board = new Square[8][8];
        Bpieces = new LinkedList<Piece>();
        Wpieces = new LinkedList<Piece>();
        setLayout(new GridLayout(8, 8, 0, 0));

        this.addMouseListener(this);
        this.addMouseMotionListener(this);

        initializeSquares();
        initializePieces();

        this.setPreferredSize(new Dimension(400, 400));
        this.setMaximumSize(new Dimension(400, 400));
        this.setMinimumSize(this.getPreferredSize());
        this.setSize(new Dimension(400, 400));

        whiteTurn = true;
    }
    
    private void initializeSquares() {
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                int squareColor = ((x + y) % 2 == 0) ? 1 : 0;
                board[x][y] = new Square(this, squareColor, y, x);
                this.add(board[x][y]);
            }
        }
    }

    private void initializePieces() {
        // Initialize pawns
        initializePawns();
        
        // Initialize royal pieces
        initializeRoyalPieces();
        
        // Add all pieces to respective lists
        addPiecesToLists();
        
        // Initialize checkmate detector
        King wk = (King) board[7][4].getOccupyingPiece();
        King bk = (King) board[0][4].getOccupyingPiece();
        cmd = new CheckmateDetector(this, Wpieces, Bpieces, wk, bk);
    }
    
    private void initializePawns() {
        for (int x = 0; x < 8; x++) {
            board[1][x].put(new Pawn(BLACK, board[1][x], RESOURCES_BPAWN_PNG));
            board[6][x].put(new Pawn(WHITE, board[6][x], RESOURCES_WPAWN_PNG));
        }
    }
    
    private void initializeRoyalPieces() {
        // Queens
        board[7][3].put(new Queen(WHITE, board[7][3], RESOURCES_WQUEEN_PNG));
        board[0][3].put(new Queen(BLACK, board[0][3], RESOURCES_BQUEEN_PNG));
        
        // Kings
        board[0][4].put(new King(BLACK, board[0][4], RESOURCES_BKING_PNG));
        board[7][4].put(new King(WHITE, board[7][4], RESOURCES_WKING_PNG));

        // Rooks
        placePiecePair(Rook.class, 0, 7, 0, RESOURCES_BROOK_PNG, RESOURCES_WROOK_PNG);
        
        // Knights
        placePiecePair(Knight.class, 1, 6, 0, RESOURCES_BKNIGHT_PNG, RESOURCES_WKNIGHT_PNG);
        
        // Bishops
        placePiecePair(Bishop.class, 2, 5, 0, RESOURCES_BBISHOP_PNG, RESOURCES_WBISHOP_PNG);
    }
    
    private void placePiecePair(Class<? extends Piece> pieceClass, int col1, int col2, int row, 
                               String blackResource, String whiteResource) {
        try {
            // Black pieces at row 0
            board[row][col1].put(pieceClass.getDeclaredConstructor(int.class, Square.class, String.class)
                .newInstance(BLACK, board[row][col1], blackResource));
            board[row][col2].put(pieceClass.getDeclaredConstructor(int.class, Square.class, String.class)
                .newInstance(BLACK, board[row][col2], blackResource));
            
            // White pieces at row 7
            board[7][col1].put(pieceClass.getDeclaredConstructor(int.class, Square.class, String.class)
                .newInstance(WHITE, board[7][col1], whiteResource));
            board[7][col2].put(pieceClass.getDeclaredConstructor(int.class, Square.class, String.class)
                .newInstance(WHITE, board[7][col2], whiteResource));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void addPiecesToLists() {
        for(int y = 0; y < 2; y++) {
            for (int x = 0; x < 8; x++) {
                Bpieces.add(board[y][x].getOccupyingPiece());
                Wpieces.add(board[7-y][x].getOccupyingPiece());
            }
        }
    }

    public Square[][] getSquareArray() {
        return this.board;
    }

    public boolean getTurn() {
        return whiteTurn;
    }

    public void setCurrPiece(Piece p) {
        this.currPiece = p;
    }

    public Piece getCurrPiece() {
        return this.currPiece;
    }
    
    /**
     * Checks if the piece can be moved based on the current turn
     */
    private boolean isCorrectTurn(Piece piece) {
        if (piece == null) return false;
        return (piece.getColor() == WHITE && whiteTurn) || 
               (piece.getColor() == BLACK && !whiteTurn);
    }
    
    /**
     * Handles checkmate condition and ends the game
     */
    private void handleCheckmate() {
        if (cmd.blackCheckMated() || cmd.whiteCheckMated()) {
            currPiece = null;
            repaint();
            this.removeMouseListener(this);
            this.removeMouseMotionListener(this);
            g.checkmateOccurred(cmd.blackCheckMated() ? BLACK : WHITE);
        } else {
            currPiece = null;
            whiteTurn = !whiteTurn;
            movable = cmd.getAllowableSquares(whiteTurn);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        // Paint all squares
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                board[y][x].paintComponent(g);
            }
        }

        // Paint the piece being dragged
        if (currPiece != null && isCorrectTurn(currPiece)) {
            final Image i = currPiece.getImage();
            g.drawImage(i, currX, currY, null);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        currX = e.getX();
        currY = e.getY();

        Square sq = (Square) this.getComponentAt(new Point(e.getX(), e.getY()));

        if (sq.isOccupied()) {
            currPiece = sq.getOccupyingPiece();
            if (!isCorrectTurn(currPiece)) return;
            sq.setDisplay(false);
        }
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        Square sq = (Square) this.getComponentAt(new Point(e.getX(), e.getY()));

        if (currPiece != null) {
            if (!isCorrectTurn(currPiece)) return;

            List<Square> legalMoves = currPiece.getLegalMoves(this);
            movable = cmd.getAllowableSquares(whiteTurn);

            if (legalMoves.contains(sq) && movable.contains(sq) && cmd.testMove(currPiece, sq)) {
                sq.setDisplay(true);
                currPiece.move(sq);
                cmd.update();
                handleCheckmate();
            } else {
                currPiece.getPosition().setDisplay(true);
                currPiece = null;
            }
        }

        repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        currX = e.getX() - 24;
        currY = e.getY() - 24;
        repaint();
    }

    // Irrelevant methods, do nothing for these mouse behaviors
    @Override
    public void mouseMoved(MouseEvent e) {}

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
}