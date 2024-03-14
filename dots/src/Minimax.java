public class Minimax {

  public int pontuou;
  public Estado.Move melhorJogada;

  Minimax(Estado estado, int profundidade) throws CloneNotSupportedException {
    pontuou = miniMax(estado, profundidade);
  }

  private int miniMax(Estado state, int depth) throws CloneNotSupportedException {

    int maxScore = Integer.MIN_VALUE;
    int minScore = Integer.MAX_VALUE;

    if (depth == 0 || state.jogoAcabou()) {

      return state.placarJogador2 - state.placarJogador1;
    }

    if (!state.vezJogador1) {

      return getMiniMaxScore(state, depth, true, maxScore);

    } else {

      return getMiniMaxScore(state, depth, false, minScore);

    }

  }

  private int getMiniMaxScore(Estado estado, int profundidade, boolean isMax, int pontuou)
      throws CloneNotSupportedException {

    for (String move : estado.movPossiveis) {
      estado.proxEstado = estado.clone();
      Estado.Move movPossivel = new Estado.Move(move);
      estado.proxEstado.colocaLinha(movPossivel);
      int currentScore = miniMax(estado.proxEstado, profundidade - 1);

      if (isMax && pontuou <= currentScore) {
        pontuou = currentScore;
        this.melhorJogada = movPossivel;
      }

      if (!isMax && pontuou >= currentScore) {

        pontuou = currentScore;
        this.melhorJogada = movPossivel;
      }

    }

    return pontuou;
  }

}