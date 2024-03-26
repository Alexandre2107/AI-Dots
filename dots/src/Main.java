import java.util.Scanner;

public class Main {

  public static int tamanhoTabuleiro;
  public static String nomeJogador = "Eu";
  public static String nomeIA = "IA";
  private static Scanner scanner = new Scanner(System.in);
  public static int profundidade;
  private static Minimax minimax;
  private static MiniMaxHeuristica miniMaxHeuristica;

  public static void main(String[] args) {

    System.out.println("Qual o tamanho do tabuleiro? ");
    tamanhoTabuleiro = scanner.nextInt();

    System.out.println();

    System.out.println("Qual a profundidade da árvore (1 a 10): ");
    profundidade = scanner.nextInt();
    System.out.println();

    Estado estadoDaRaiz = new Estado();
    estadoDaRaiz.init();
    estadoDaRaiz.vezJogador1 = false;
    estadoDaRaiz.printEstado();

    while (!estadoDaRaiz.jogoAcabou()) {

      String jogadorAtual = estadoDaRaiz.vezJogador1 ? Main.nomeJogador : Main.nomeIA;
      System.out.println("Usuário: " + jogadorAtual);

      if (estadoDaRaiz.vezJogador1) {

        System.out.println("Coordenada da coluna: ");
        int x = scanner.nextInt() - 1;
        System.out.println("Coordenada da linha: ");
        int y = scanner.nextInt() - 1;
        System.out.println("Posição da linha (CIMA, BAIXO, ESQUERDA, DIREITA): ");
        System.out.println("Posições das linha disponíveis: ");
        int index = 0;
        for (Caixa.Posicao moveDisponivel : Caixa.Posicao.values()) {
          index++;
          System.out.print(index + ". " + moveDisponivel + "  ");
        }
        System.out.println();
        Caixa.Posicao posicaoColocada = Caixa.Posicao.values()[scanner.nextInt() - 1];
        Estado.Move moveDoJogador = new Estado.Move(x, y, posicaoColocada);
        estadoDaRaiz.colocaLinha(moveDoJogador);
      }

      else {
        try {
          minimax = new Minimax(estadoDaRaiz, profundidade);
          // miniMaxHeuristica = new MiniMaxHeuristica(estadoDaRaiz, profundidade);
        } catch (CloneNotSupportedException e) {
          e.printStackTrace();
          System.out.println("Erro");
        }
        System.out.println("Movimento " + (minimax.melhorJogada.x + 1) + " " +
            (minimax.melhorJogada.y + 1) + " "
            + minimax.melhorJogada.posicaoLinha);
        estadoDaRaiz.colocaLinha(minimax.melhorJogada);
        // if (miniMaxHeuristica.melhorJogada == null) {
        // System.out.println("Erro");
        // }
        // System.out
        // .println("Movimento " + (miniMaxHeuristica.melhorJogada.x + 1) + " " +
        // (miniMaxHeuristica.melhorJogada.y + 1) + " "
        // + miniMaxHeuristica.melhorJogada.posicaoLinha);
        // estadoDaRaiz.colocaLinha(miniMaxHeuristica.melhorJogada);
      }

      estadoDaRaiz.printEstado();
    }

    System.out.println();
    System.out.print(
        "Pontuação Humano: " + estadoDaRaiz.placarJogador1 + " ///// Pontuação IA: " + estadoDaRaiz.placarJogador2);
    System.out.println();
    if (estadoDaRaiz.placarJogador1 > estadoDaRaiz.placarJogador2) {
      System.out.println("Você ganhou!");
    } else if (estadoDaRaiz.placarJogador1 < estadoDaRaiz.placarJogador2) {
      System.out.println("Você perdeu!");
    } else {
      System.out.println("Empate!");

    }

  }

}