

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;


/**
 * Component of the Chess game that detects check mates in the game.
 *
 * @author Jussi Lundstedt
 *
 */
public class CheckmateDetector {
    private Board b;
    private LinkedList<Piece> wPieces;
    private LinkedList<Piece> bPieces;
    private LinkedList<Square> movableSquares;
    private final LinkedList<Square> squares;
    private King bk;
    private King wk;
    private HashMap<Square,List<Piece>> wMoves;
    private HashMap<Square,List<Piece>> bMoves;

    /**
     * Constructs a new instance of CheckmateDetector on a given board. By
     * convention should be called when the board is in its initial state.
     *
     * @param b The board which the detector monitors
     * @param wPieces White pieces on the board.
     * @param bPieces Black pieces on the board.
     * @param wk Piece object representing the white king
     * @param bk Piece object representing the black king
     */
    public CheckmateDetector(Board b, LinkedList<Piece> wPieces,
                             LinkedList<Piece> bPieces, King wk, King bk) {
        this.b = b;
        this.wPieces = wPieces;
        this.bPieces = bPieces;
        this.bk = bk;
        this.wk = wk;

        // Initialize other fields
        squares = new LinkedList<Square>();
        movableSquares = new LinkedList<Square>();
        wMoves = new HashMap<Square,List<Piece>>();
        bMoves = new HashMap<Square,List<Piece>>();

        Square[][] brd = b.getSquareArray();

        // add all squares to squares list and as hashmap keys
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                squares.add(brd[y][x]);
                wMoves.put(brd[y][x], new LinkedList<Piece>());
                bMoves.put(brd[y][x], new LinkedList<Piece>());
            }
        }

        // update situation
        update();
    }

    /**
     * Updates the object with the current situation of the game.
     */
    public void update() {
        // Iterators through pieces
        Iterator<Piece> wIter = wPieces.iterator();
        Iterator<Piece> bIter = bPieces.iterator();

        // empty moves and movable squares at each update
        for (List<Piece> pieces : wMoves.values()) {
            pieces.removeAll(pieces);
        }

        for (List<Piece> pieces : bMoves.values()) {
            pieces.removeAll(pieces);
        }

        movableSquares.removeAll(movableSquares);

        // Add each move white and black can make to map

        addEachMoveOnMap(wIter,"w");
        addEachMoveOnMap(bIter,"b");
    }

    public void addEachMoveOnMap(Iterator<Piece> colorIter , String color){
        while (colorIter.hasNext()) {
            Piece p = colorIter.next();

            if (!p.getClass().equals(King.class)) {
                if (p.getPosition() == null) {
                    colorIter.remove();
                    continue;
                }

                List<Square> mvs = p.getLegalMoves(b);
                Iterator<Square> iter = mvs.iterator();
                if (color.equals("w")){
                    while (iter.hasNext()) {
                        List<Piece> pieces = wMoves.get(iter.next());
                        pieces.add(p);
                    }
                }
                else if (color.equals("b")){
                    while (iter.hasNext()) {
                        List<Piece> pieces = bMoves.get(iter.next());
                        pieces.add(p);
                    }
                }

            }
        }
    }

    /**
     * Checks if the color king is threatened
     * @return boolean representing whether the said king is in check.
     */

    public boolean isInCheck(String color) {
        update();
        Square sq =null;
        if (color.equals("w")){
            sq=wk.getPosition();
            if (bMoves.get(sq).isEmpty()) {
                movableSquares.addAll(squares);
                return false;
            }

        }
        else if (color.equals("b")){
            sq = bk.getPosition();
            if (wMoves.get(sq).isEmpty()) {
                movableSquares.addAll(squares);
                return false;
            }
        }

        return true;
    }

    /**
     * Checks whether black is in checkmate.
     * @return boolean representing if black player is checkmated.
     */

    public Boolean colorCheckMated(String color){

        boolean checkmate = true;
        King k =null;
        HashMap<Square,List<Piece>>  opositeMoves=null;
        HashMap<Square,List<Piece>>  colorMoves=null;

        if (color.equals("w")){
            if (!this.isInCheck("w")) return false;
            opositeMoves=bMoves;
            colorMoves=wMoves;
            k =wk;
        }
        else if (color.equals("b")){
            if (!this.isInCheck("b")) return false;
            opositeMoves=wMoves;
            colorMoves=bMoves;
            k = bk;
        }

        // If yes, check if king can evade
        if (canEvade(opositeMoves, k)) checkmate = false;

        // If no, check if threat can be captured
        List<Piece> threats = opositeMoves.get(k.getPosition());
        if (canCapture(colorMoves, threats, k)) checkmate = false;

        // If no, check if threat can be blocked
        if (canBlock(threats, colorMoves, k)) checkmate = false;

        // If no possible ways of removing check, checkmate occurred
        return checkmate;
    }





    /*
     * Helper method to determine if the king can evade the check.
     * Gives a false positive if the king can capture the checking piece.
     */
    private boolean canEvade(Map<Square,List<Piece>> tMoves, King tKing) {
        boolean evade = false;
        List<Square> kingsMoves = tKing.getLegalMoves(b);

        // If king is not threatened at some square, it can evade
        for (Square sq : kingsMoves) {
            if (!testMove(tKing, sq)) continue;
            if (tMoves.get(sq).isEmpty()) {
                movableSquares.add(sq);
                evade = true;
            }
        }

        return evade;
    }

    /*
     * Helper method to determine if the threatening piece can be captured.
     */
    private boolean canCapture(Map<Square,List<Piece>> poss,
                               List<Piece> threats, King k) {

        boolean capture = false;
        if (threats.size() == 1) {
            Square sq = threats.get(0).getPosition();

            if (k.getLegalMoves(b).contains(sq)) {
                movableSquares.add(sq);
                if (testMove(k, sq)) {
                    capture = true;
                }
            }

            List<Piece> caps = poss.get(sq);
            ConcurrentLinkedDeque<Piece> capturers = new ConcurrentLinkedDeque<Piece>();
            capturers.addAll(caps);

            if (!capturers.isEmpty()) {
                movableSquares.add(sq);
                for (Piece p : capturers) {
                    if (testMove(p, sq)) {
                        capture = true;
                    }
                }
            }
        }

        return capture;
    }

    /*
     * Helper method to determine if check can be blocked by a piece.
     */
    private boolean canBlock(List<Piece> threats,
                             Map <Square,List<Piece>> blockMoves, King k) {
        boolean blockable = false;

        if (threats.size() == 1) {
            Square ts = threats.get(0).getPosition();
            Square ks = k.getPosition();
            Square[][] brdArray = b.getSquareArray();

            if (ks.getXNum() == ts.getXNum()) {
                int max = Math.max(ks.getYNum(), ts.getYNum());
                int min = Math.min(ks.getYNum(), ts.getYNum());

                for (int i = min + 1; i < max; i++) {
                    List<Piece> blks =
                            blockMoves.get(brdArray[i][ks.getXNum()]);
                    ConcurrentLinkedDeque<Piece> blockers =
                            new ConcurrentLinkedDeque<Piece>();
                    blockers.addAll(blks);

                    if (!blockers.isEmpty()) {
                        movableSquares.add(brdArray[i][ks.getXNum()]);

                        for (Piece p : blockers) {
                            if (testMove(p,brdArray[i][ks.getXNum()])) {
                                blockable = true;
                            }
                        }

                    }
                }
            }

            if (ks.getYNum() == ts.getYNum()) {
                int max = Math.max(ks.getXNum(), ts.getXNum());
                int min = Math.min(ks.getXNum(), ts.getXNum());

                for (int i = min + 1; i < max; i++) {
                    List<Piece> blks =
                            blockMoves.get(brdArray[ks.getYNum()][i]);
                    ConcurrentLinkedDeque<Piece> blockers =
                            new ConcurrentLinkedDeque<Piece>();
                    blockers.addAll(blks);

                    if (!blockers.isEmpty()) {

                        movableSquares.add(brdArray[ks.getYNum()][i]);

                        for (Piece p : blockers) {
                            if (testMove(p, brdArray[ks.getYNum()][i])) {
                                blockable = true;
                            }
                        }

                    }
                }
            }

            Class<? extends Piece> tC = threats.get(0).getClass();

            if (tC.equals(Queen.class) || tC.equals(Bishop.class)) {
                int kX = ks.getXNum();
                int kY = ks.getYNum();
                int tX = ts.getXNum();
                int tY = ts.getYNum();

                int startX, endX ;
                int step;

                if (kX < tX) {
                    startX = tX - 1;
                    endX = kX;
                    step = -1;
                }
                else {
                    startX = tX + 1;
                    endX = kX;
                    step = 1;
                }

                for (int i = startX; (step == -1) ? (i > endX) : (i < endX); i += step) {

                    if (kY > tY) {
                        tY++;
                    } else if (kY < tY) {
                        tY--;
                    }

                    List<Piece> blks = blockMoves.get(brdArray[tY][i]);
                    ConcurrentLinkedDeque<Piece> blockers = new ConcurrentLinkedDeque<Piece>();
                    blockers.addAll(blks);

                    if (!blockers.isEmpty()) {
                        movableSquares.add(brdArray[tY][i]);

                        for (Piece p : blockers) {
                            if (testMove(p, brdArray[tY][i])) {
                                blockable = true;
                            }
                        }
                    }
                }
            }
        }

        return blockable;
    }

    /**
     * Method to get a list of allowable squares that the player can move.
     * Defaults to all squares, but limits available squares if player is in
     * check.
     * @param b boolean representing whether it's white player's turn (if yes,
     * true)
     * @return List of squares that the player can move into.
     */
    public List<Square> getAllowableSquares(boolean b) {
        movableSquares.removeAll(movableSquares);
        if (isInCheck("w")) {
            colorCheckMated("w");
        } else if (isInCheck("b")) {
            colorCheckMated("b");
        }
        return movableSquares;
    }

    /**
     * Tests a move a player is about to make to prevent making an illegal move
     * that puts the player in check.
     * @param p Piece moved
     * @param sq Square to which p is about to move
     * @return false if move would cause a check
     */
    public boolean testMove(Piece p, Square sq) {
        Piece c = sq.getOccupyingPiece();

        boolean movetest = true;
        Square init = p.getPosition();

        p.move(sq);
        update();

        if (p.getColor() == 0 && isInCheck("b")) movetest = false;
        else if (p.getColor() == 1 && isInCheck("w")) movetest = false;

        p.move(init);
        if (c != null) sq.put(c);

        update();

        movableSquares.addAll(squares);
        return movetest;
    }

}