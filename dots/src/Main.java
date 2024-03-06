
import java.util.Scanner;

public class Main {

  public static int boardSize;
  public static String playerName = "Me";
  public static String aiName = "Ai";
  private static Scanner scanner = new Scanner(System.in);
  private static Strategies.TYPE strategyType;
  private static int depth;

  public static void main(String[] args) {

    System.out.println("Please enter the size of the board : ");
    boardSize = scanner.nextInt();
    System.out.print("Please select the strategy : ");

    int index = 0;
    for (Strategies.TYPE strategy : Strategies.TYPE.values()) {
      index++;
      System.out.print(index + ". " + strategy + "  ");
    }

    System.out.println();

    strategyType = Strategies.TYPE.values()[scanner.nextInt() - 1];

    System.out.println("Please set the tree depth : ");
    depth = scanner.nextInt();
    System.out.println();

    State rootState = new State();
    rootState.init();
    rootState.isHumanTurn = true;
    rootState.printState();

    while (!rootState.isGameOver()) {

      String currentPlayer = rootState.isHumanTurn ? Main.playerName : Main.aiName;
      System.out.println("User : " + currentPlayer);

      if (rootState.isHumanTurn) {

        System.out.println("Please enter column coordinate : ");
        int x = scanner.nextInt() - 1;
        System.out.println("Please enter row coordinate : ");
        int y = scanner.nextInt() - 1;
        System.out.println("Please enter line position : ");
        System.out.println("Available line positions : ");
        index = 0;
        for (Box.Position availableMove : Box.Position.values()) {
          index++;
          System.out.print(index + ". " + availableMove + "  ");
        }
        System.out.println();
        Box.Position enteredPosition = Box.Position.values()[scanner.nextInt() - 1];
        State.Move playerMove = new State.Move(x, y, enteredPosition);
        rootState.placeLine(playerMove);

      } else {

        try {

          Strategies strategy = new Strategies(strategyType, rootState, depth);
          System.out.println(String.format("Move %d %d %s", (strategy.bestMove.x + 1), (strategy.bestMove.y + 1),
              strategy.bestMove.linePosition));
          rootState.placeLine(strategy.bestMove);
        } catch (CloneNotSupportedException e) {
          e.printStackTrace();
        }

      }

      rootState.printState();
    }

    System.out.println();
    System.out.print(String.format("Player score : %d   Ai score : %d", rootState.playerScore, rootState.aiScore));

  }

}