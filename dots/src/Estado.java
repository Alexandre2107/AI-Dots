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
        if (tabuleiro[i][j].jogador != null && tabuleiro[i][j].jogador == "") {
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
        if (move.j < Main.boardSize - 1) {
          boxesMoves.add(new Move(move.i, move.j + 1, Caixa.Posicao.CIMA));
          
        }
        break;
      case ESQUERDA:
        if (move.i > 0) {
          boxesMoves.add(new Move(move.i - 1, move.j, Caixa.Posicao.DIREITA));
        }
        break;
      case DIREITA:
        if (move.i < Main.boardSize - 1) {
          boxesMoves.add(new Move(move.i + 1, move.j, Caixa.Posicao.ESQUERDA));
        }
        break;

      default:
        break;
    }

    for (Move currentMove: boxesMoves) {
      Caixa caixaAtual = this.getCaixa(currentMove);
      caixaAtual.lados.put(currentMove.posicaoLinha, true);
      this.movPossiveis.remove(currentMove.toString());

      if (!caixaAtual.isBoxOpen()) {
        caixaAtual.jogador = this.vezJogador1?Main.playerName:Main.playerName2;
        this.PontuacaoJogador2 = this.vezJogador1?this.PontuacaoJogador2:this.PontuacaoJogador2 + 1;
        this.PontuacaoJogador1 = this.vezJogador1?this.PontuacaoJogador1 + 1:this.PontuacaoJogador1;
      }

    }
    this.vezJogador1 = !this.vezJogador1;
  }
}
