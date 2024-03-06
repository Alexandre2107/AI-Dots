
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;

public class State {

  int playerScore;
  int aiScore;
  boolean isHumanTurn;
  Box[][] board;
  State nextState;
  CopyOnWriteArraySet<String> possibleMoves = new CopyOnWriteArraySet<String>();

  @Override
  public State clone() throws CloneNotSupportedException {

    State newState = new State();
    newState.board = new Box[Main.boardSize][Main.boardSize];
    for (int i = 0; i < this.board.length; i++) {
      for (int j = 0; j < this.board[i].length; j++) {
        newState.board[i][j] = this.board[i][j].clone();
      }
    }

    newState.aiScore = this.aiScore;
    newState.playerScore = this.playerScore;
    newState.possibleMoves = new CopyOnWriteArraySet<String>();
    newState.possibleMoves.addAll(this.possibleMoves);
    newState.isHumanTurn = this.isHumanTurn;

    return newState;
  }

  /**
   * Initialize empty board
   */
  public void init() {

    board = new Box[Main.boardSize][Main.boardSize];
    for (int x = 0; x < Main.boardSize; x++) {

      for (int y = 0; y < Main.boardSize; y++) {

        HashMap<Box.Position, Boolean> boxMap = new HashMap<Box.Position, Boolean>();

        for (Box.Position boxPosition : Box.Position.values()) {

          boxMap.put(boxPosition, false);
          possibleMoves.add(new Move(x, y, boxPosition).toString());

        }

        board[x][y] = new Box(boxMap);

      }

    }

  }

  /**
   * Check if game is over, all the Boxes belong to some player
   * 
   * @return boolean gameisover
   */
  public boolean isGameOver() {

    for (int x = 0; x < Main.boardSize; x++) {

      for (int y = 0; y < Main.boardSize; y++) {

        if (board[x][y].player != null && board[x][y].player == "") {

          return false;
        }
      }
    }

    return true;
  }

  /**
   * Get current box from move parameter
   * 
   * @param move
   * @return current Box
   */
  public Box getBox(Move move) {

    return board[move.y][move.x];

  }

  /**
   * Place a line into specific position
   * If current Box have neighbors, place a line there too
   * ex. : top of the current box will be bottom of upper neighbor box
   * 
   * @param move
   */
  public void placeLine(Move move) {

    if (!move.isLegalMove()) {

      return;
    }

    if (!possibleMoves.contains(move.toString())) {
      System.out.println("Essa jogada já foi realizada. Escolha outra.");
      return;
  }

    List<Move> boxesMoves = new ArrayList<Move>();
    boxesMoves.add(move);

    switch (move.linePosition) {

      case TOP:

        if (move.y > 0) {

          boxesMoves.add(new Move(move.x, move.y - 1, Box.Position.BOTTOM));
        }

        break;

      case BOTTOM:

        if (move.y < Main.boardSize - 1) {

          boxesMoves.add(new Move(move.x, move.y + 1, Box.Position.TOP));
        }

        break;

      case LEFT:

        if (move.x > 0) {

          boxesMoves.add(new Move(move.x - 1, move.y, Box.Position.RIGHT));
        }

        break;
      case RIGHT:

        if (move.x < Main.boardSize - 1) {

          boxesMoves.add(new Move(move.x + 1, move.y, Box.Position.LEFT));

        }

        break;

    }

    for (Move currentMove : boxesMoves) {

      Box currentBox = this.getBox(currentMove);
      currentBox.lines.put(currentMove.linePosition, true);
      this.possibleMoves.remove(currentMove.toString());

      if (!currentBox.isBoxOpen()) {

        currentBox.player = this.isHumanTurn ? Main.playerName : Main.aiName;
        this.aiScore = this.isHumanTurn ? this.aiScore : this.aiScore + 1;
        this.playerScore = this.isHumanTurn ? this.playerScore + 1 : this.playerScore;
      }

    }

    this.isHumanTurn = !this.isHumanTurn;

  }

  /**
   * Print current board state
   */
  public void printState() {

    int column = 1;
    System.out.println();

    /*
     * Print row cooddinates
     */
    for (int row = 1; row <= this.board[0].length; row++) {

      System.out.print("  " + row + " ");

    }

    System.out.println();

    for (Box[] boxes : this.board) {
      for (Box box : boxes) {
        System.out.print("*");
        if (box.lines.get(Box.Position.TOP)) {
          System.out.print("---");
        } else {
          System.out.print("   ");
        }
      }
      System.out.print("*");
      System.out.println();

      for (Box box : boxes) {
        if (box.lines.get(Box.Position.LEFT)) {
          System.out.print("| ");
        } else {
          System.out.print("  ");
        }
        if (!box.isBoxOpen()) {
          System.out.print(box.player);
        } else {
          System.out.print("  ");
        }
        if (box == boxes[boxes.length - 1]) {
          if (box.lines.get(Box.Position.RIGHT)) {
            System.out.print("|");
          } else {
            System.out.print(" ");
          }

          /*
           * Print column coordinates
           */
          System.out.print("  " + column++);
        }
      }

      System.out.println();
    }
    for (Box box : this.board[this.board.length - 1]) {
      System.out.print("*");
      if (box.lines.get(Box.Position.BOTTOM)) {
        System.out.print("---");
      } else {
        System.out.print("   ");
      }
    }
    System.out.println("*");
    System.out.println();
  }

  /**
   * Move class holder constructor
   * x : horizontal coordinate
   * y : vertical position
   * linePosition : position of the line {TOP,BOTTOM,LEFT,RIGHT}
   */
  static class Move {

    int x;
    int y;
    Box.Position linePosition;

    Move(int x, int y, Box.Position linePosition) {

      this.x = x;
      this.y = y;
      this.linePosition = linePosition;

    }

    /**
     * String representation of the Move
     * 
     * @return String
     */
    public String toString() {

      return this.x + " " + this.y + " " + linePosition.toString();

    }

    /**
     * Check for move constraints
     * 
     * @return true if legal
     */
    public boolean isLegalMove() {

      if (this.x > Main.boardSize - 1 || this.y > Main.boardSize - 1 || this.x < 0 || this.y < 0) {

        return false;
      }

      return true;
    }

    /**
     * Alternative input for object as String
     * 
     * @param input
     */
    Move(String input) {

      String arr[] = input.split(" ");

      this.x = Integer.parseInt(arr[0]);
      this.y = Integer.parseInt(arr[1]);
      this.linePosition = Box.Position.valueOf(arr[2]);

    }

  }

}