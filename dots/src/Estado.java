import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;

public class Estado {

  int placarJogador1;
  int placarJogador2;
  boolean vezJogador1;
  Caixa[][] tabuleiro;
  Estado proxEstado;
  CopyOnWriteArraySet<String> movPossiveis = new CopyOnWriteArraySet<String>();

  @Override
  public Estado clone() throws CloneNotSupportedException {

    Estado novoEstado = new Estado();
    novoEstado.tabuleiro = new Caixa[Main.tamanhoTabuleiro][Main.tamanhoTabuleiro];
    for (int i = 0; i < this.tabuleiro.length; i++) {
      for (int j = 0; j < this.tabuleiro[i].length; j++) {
        novoEstado.tabuleiro[i][j] = this.tabuleiro[i][j].clone();
      }
    }

    novoEstado.placarJogador2 = this.placarJogador2;
    novoEstado.placarJogador1 = this.placarJogador1;
    novoEstado.movPossiveis = new CopyOnWriteArraySet<String>();
    novoEstado.movPossiveis.addAll(this.movPossiveis);
    novoEstado.vezJogador1 = this.vezJogador1;

    return novoEstado;
  }

  public void init() {

    tabuleiro = new Caixa[Main.tamanhoTabuleiro][Main.tamanhoTabuleiro];
    for (int x = 0; x < Main.tamanhoTabuleiro; x++) {

      for (int y = 0; y < Main.tamanhoTabuleiro; y++) {

        HashMap<Caixa.Posicao, Boolean> CaixaMap = new HashMap<Caixa.Posicao, Boolean>();

        for (Caixa.Posicao posicaoCaixa : Caixa.Posicao.values()) {

          CaixaMap.put(posicaoCaixa, false);
          movPossiveis.add(new Move(x, y, posicaoCaixa).toString());

        }

        tabuleiro[x][y] = new Caixa(CaixaMap);

      }

    }

  }

  public boolean jogoAcabou() {

    for (int x = 0; x < Main.tamanhoTabuleiro; x++) {

      for (int y = 0; y < Main.tamanhoTabuleiro; y++) {

        if (tabuleiro[x][y].jogador != null && tabuleiro[x][y].jogador == "") {

          return false;
        }
      }
    }

    return true;
  }

  public Caixa getCaixa(Move move) {

    return tabuleiro[move.y][move.x];

  }

  public void colocaLinha(Move move) {

    if (!move.moveIlegal()) {
      return;
    }

    if (!movPossiveis.contains(move.toString())) {
      System.out.println("Essa jogada já foi realizada. Escolha outra.");
      return;
    }

    List<Move> movCaixas = new ArrayList<Move>();
    movCaixas.add(move);

    switch (move.posicaoLinha) {

      case CIMA:

        if (move.y > 0) {

          movCaixas.add(new Move(move.x, move.y - 1, Caixa.Posicao.BAIXO));
        }

        break;

      case BAIXO:

        if (move.y < Main.tamanhoTabuleiro - 1) {

          movCaixas.add(new Move(move.x, move.y + 1, Caixa.Posicao.CIMA));
        }

        break;

      case ESQUERDA:

        if (move.x > 0) {

          movCaixas.add(new Move(move.x - 1, move.y, Caixa.Posicao.DIREITA));
        }

        break;
      case DIREITA:

        if (move.x < Main.tamanhoTabuleiro - 1) {

          movCaixas.add(new Move(move.x + 1, move.y, Caixa.Posicao.ESQUERDA));

        }

        break;

    }

    for (Move movAtual : movCaixas) {
      Caixa caixaAtual = this.getCaixa(movAtual);
      caixaAtual.linhas.put(movAtual.posicaoLinha, true);
      this.movPossiveis.remove(movAtual.toString());

      if (!caixaAtual.isBoxOpen()) {
        caixaAtual.jogador = this.vezJogador1 ? Main.nomeJogador : Main.nomeIA;
        this.placarJogador2 = this.vezJogador1 ? this.placarJogador2 : this.placarJogador2 + 1;
        this.placarJogador1 = this.vezJogador1 ? this.placarJogador1 + 1 : this.placarJogador1;
      }
    }

    this.vezJogador1 = !this.vezJogador1;
  }

  public void printEstado() {

    int column = 1;
    System.out.println();
    for (int row = 1; row <= this.tabuleiro[0].length; row++) {

      System.out.print("  " + row + " ");

    }

    System.out.println();

    for (Caixa[] caixas : this.tabuleiro) {
      for (Caixa box : caixas) {
        System.out.print("*");
        if (box.linhas.get(Caixa.Posicao.CIMA)) {
          System.out.print("---");
        } else {
          System.out.print("   ");
        }
      }
      System.out.print("*");
      System.out.println();

      for (Caixa caixa : caixas) {
        if (caixa.linhas.get(Caixa.Posicao.ESQUERDA)) {
          System.out.print("| ");
        } else {
          System.out.print("  ");
        }
        if (!caixa.isBoxOpen()) {
          System.out.print(caixa.jogador);
        } else {
          System.out.print("  ");
        }
        if (caixa == caixas[caixas.length - 1]) {
          if (caixa.linhas.get(Caixa.Posicao.DIREITA)) {
            System.out.print("|");
          } else {
            System.out.print(" ");
          }
          System.out.print("  " + column++);
        }
      }

      System.out.println();
    }
    for (Caixa caixa : this.tabuleiro[this.tabuleiro.length - 1]) {
      System.out.print("*");
      if (caixa.linhas.get(Caixa.Posicao.BAIXO)) {
        System.out.print("---");
      } else {
        System.out.print("   ");
      }
    }
    System.out.println("*");
    System.out.println();
  }

  static class Move {

    int x;
    int y;
    Caixa.Posicao posicaoLinha;

    Move(int x, int y, Caixa.Posicao posicaoLinha) {

      this.x = x;
      this.y = y;
      this.posicaoLinha = posicaoLinha;

    }

    public String toString() {

      return this.x + " " + this.y + " " + posicaoLinha.toString();

    }

    public boolean moveIlegal() {

      if (this.x > Main.tamanhoTabuleiro - 1 || this.y > Main.tamanhoTabuleiro - 1 || this.x < 0 || this.y < 0) {

        return false;
      }

      return true;
    }

    Move(String input) {

      String arr[] = input.split(" ");

      this.x = Integer.parseInt(arr[0]);
      this.y = Integer.parseInt(arr[1]);
      this.posicaoLinha = Caixa.Posicao.valueOf(arr[2]);

    }

  }

}