import java.util.HashMap;

public class Caixa {

  public enum Posicao {
    CIMA, BAIXO, ESQUERDA, DIREITA
  }

  HashMap<Posicao, Boolean> lados = new HashMap<>();
  public String jogador = "";

  Caixa(HashMap<Posicao, Boolean> lados) {
    this.lados = lados;
  }

  @Override
  public Caixa clone() {
    Caixa caixa = new Caixa((HashMap<Posicao, Boolean>) this.lados.clone());

    return caixa;
  }
}
