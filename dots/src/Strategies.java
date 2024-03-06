public class Strategies {

  public enum TYPE {
    MINIMAX, ALPHABETA
  }

  public int score;
  public State.Move bestMove;

  Strategies(TYPE type, State state, int depth) throws CloneNotSupportedException {

    switch (type) {

      case MINIMAX:

        score = miniMax(state, depth);

        break;
      default:
        break;

    }

  }

  /**
   * Minimax representation
   * Going over states recursively to find maximum or minimum score depends on the
   * player
   * 
   * @param state current board state
   * @param depth current recursion depth
   * @return
   */
  private int miniMax(State state, int depth) throws CloneNotSupportedException {

    int maxScore = Integer.MIN_VALUE;
    int minScore = Integer.MAX_VALUE;

    if (depth == 0 || state.isGameOver()) {

      return state.aiScore - state.playerScore;
    }

    if (!state.isHumanTurn) {

      return getMiniMaxScore(state, depth, true, maxScore);

    } else {

      return getMiniMaxScore(state, depth, false, minScore);

    }

  }

  /**
   * Main implementation for MinMax recursive call
   * 
   * @param state current state
   * @param depth current play
   * @param isMax Max or Min player
   * @param score max or min score
   * @return current score
   * @throws CloneNotSupportedException
   */
  private int getMiniMaxScore(State state, int depth, boolean isMax, int score) throws CloneNotSupportedException {

    for (String move : state.possibleMoves) {
      state.nextState = state.clone();
      State.Move possibleMove = new State.Move(move);
      state.nextState.placeLine(possibleMove);
      int currentScore = miniMax(state.nextState, depth - 1);

      if (isMax && score <= currentScore) {
        score = currentScore;
        this.bestMove = possibleMove;
      }

      if (!isMax && score >= currentScore) {

        score = currentScore;
        this.bestMove = possibleMove;
      }

    }

    return score;
  }

}