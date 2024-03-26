public class MiniMaxHeuristica {

  public int pontuou;
  public Estado.Move melhorJogada;

  MiniMaxHeuristica(Estado estado, int profundidade) throws CloneNotSupportedException {
    pontuou = miniMax(estado, profundidade, Integer.MIN_VALUE, Integer.MAX_VALUE);
  }

  private int miniMax(Estado state, int depth, int alpha, int beta) throws CloneNotSupportedException {
    if (depth == 0 || state.jogoAcabou()) {
      return state.placarJogador2 - state.placarJogador1;
    }

    if (!state.vezJogador1) {
      return getMaxScore(state, depth, alpha, beta);
    } else {
      return getMinScore(state, depth, alpha, beta);
    }
  }

  private int getMaxScore(Estado estado, int profundidade, int alpha, int beta)
      throws CloneNotSupportedException {
    int maxScore = Integer.MIN_VALUE;
    for (String move : estado.movPossiveis) {
      estado.proxEstado = estado.clone();
      Estado.Move movPossivel = new Estado.Move(move);
      estado.proxEstado.colocaLinha(movPossivel);
      int currentScore = miniMax(estado.proxEstado, profundidade - 1, alpha, beta);
      this.melhorJogada = movPossivel;
      if (currentScore > maxScore) {
        maxScore = currentScore;
        this.melhorJogada = movPossivel;
      }
      if (maxScore >= beta) {
        return maxScore;
      }
      alpha = Math.max(alpha, maxScore);
    }
    return maxScore;
  }

  private int getMinScore(Estado estado, int profundidade, int alpha, int beta)
      throws CloneNotSupportedException {
    int minScore = Integer.MAX_VALUE;
    for (String move : estado.movPossiveis) {
      estado.proxEstado = estado.clone();
      Estado.Move movPossivel = new Estado.Move(move);
      estado.proxEstado.colocaLinha(movPossivel);
      int currentScore = miniMax(estado.proxEstado, profundidade - 1, alpha, beta);
      this.melhorJogada = movPossivel;
      if (currentScore < minScore) {
        minScore = currentScore;
        this.melhorJogada = movPossivel;
      }
      if (minScore <= alpha) {
        return minScore;
      }
      beta = Math.min(beta, minScore);
    }
    return minScore;
  }
}
