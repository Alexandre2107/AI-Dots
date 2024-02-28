import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;

public class Estado {
  int PontuacaoJogador1;
  int PontuacaoJogador2;
  boolean vezJogador1;
  Caixa[][] tabuleiro;
  CopyOnWriteArraySet<String> movPossiveis = new CopyOnWriteArraySet<String>();

  @Override
  public Estado clone() throws CloneNotSupportedException {

    Estado novoEstado = new Estado();
    novoEstado.tabuleiro = new Caixa[Main.boardSize][Main.boardSize];
    for (int i = 0; i < this.tabuleiro.length; i++) {
      for (int j = 0; j < this.tabuleiro[i].length; j++) {
        novoEstado.tabuleiro[i][j] = this.tabuleiro[i][j].clone();
      }
    }
    novoEstado.PontuacaoJogador2 = this.PontuacaoJogador2;
    novoEstado.PontuacaoJogador1 = this.PontuacaoJogador1;
    novoEstado.movPossiveis = new CopyOnWriteArraySet<String>();
    novoEstado.movPossiveis.addAll(this.movPossiveis);
    novoEstado.vezJogador1 = this.vezJogador1;

    return novoEstado;
  }

  public void init() {
    tabuleiro = new Caixa[Main.boardSize][Main.boardSize];
    for (int i = 0; i < Main.boardSize; i++) {
      for (int j = 0; j < Main.boardSize; j++) {
        HashMap<Caixa.Posicao, Boolean> caixaMap = new HashMap<Caixa.Posicao, Boolean>();
        for (Caixa.Posicao posicaoCaixa : Caixa.Posicao.values()) {
          caixaMap.put(posicaoCaixa, false);
          movPossiveis.add(new Move(i, j, posicaoCaixa).toString());
        }
      }
    }
  }

  public boolean fimDeJogo() {
    for (int i = 0; i < Main.boardSize; i++) {
      for (int j = 0; j < Main.boardSize; j++) {
        if (tabuleiro[i][j].player != null && tabuleiro[i][j].player == "") {
          return false;
        }
      }
    }
    return true;
  }

  public Caixa getCaixa(Move move) {
    return tabuleiro[move.j][move.i];
  }

  public void colocarLinha(Move move) {
    if (!move.isLegalMove()) {
      return;
    }
    List<Move> boxesMoves = new ArrayList<Move>();
    boxesMoves.add(move);

    switch (move.posicaoLinha) {
      case CIMA:
        if (move.j > 0) {
          boxesMoves.add(new Move(move.i, move.j - 1, Caixa.Posicao.BAIXO));
        }
        break;
      case BAIXO:

      default:
        break;
    }
  }
}
