import java.util.HashMap;

public class Caixa {

  public enum Posicao {
    CIMA, BAIXO, ESQUERDA, DIREITA
  }

  HashMap<Posicao, Boolean> linhas = new HashMap<Posicao, Boolean>();
  public String jogador = "";

  Caixa(HashMap<Posicao, Boolean> linhas) {

    this.linhas = linhas;

  }

  @Override
  public Caixa clone() {

    @SuppressWarnings("unchecked")
    Caixa caixa = new Caixa((HashMap<Posicao, Boolean>) this.linhas.clone());

    return caixa;
  }

  public boolean isBoxOpen() {

    return this.linhas.containsValue(Boolean.FALSE);

  }

}