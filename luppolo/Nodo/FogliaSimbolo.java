package luppolo.Nodo;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import luppolo.Manipolazione.Visitor;

/**
 * OVERVIEW: La classe FogliaSimbolo è immutabile ed estende la classe astratta Nodo, rappresentando
 * oggetti immutabili che costituiscono nodi foglia, ciascuno contenente un valore di tipo stringa.
 */
public class FogliaSimbolo extends Nodo {

  /**
   * AF: L'oggetto fogliaSimbolo è un nodo senza figli, che ha come valore una stringa tra {a, ...,
   * z} un tipico oggetto FogliaSimbolo è "x", dove x è una stringa compresa nel supporto {a-z} IR:
   * valore != null valore != empty valore deve essere una lettera tra a e z
   */

  /* Rappresentazione del valore del nodo foglia con una stringa */
  private final String valore;

  /**
   * Costruisce un oggetto di tipo FogliaSimbolo a partire da una stringa
   *
   * @param valore della foglia
   * @throws NullPointerException se il valore della foglia è nullo
   * @throws IllegalArgumentException se il valore della foglia è vuoto
   * @throws IllegalArgumentException se il valore non è un valore appartenete al dominio [a-z]
   */
  public FogliaSimbolo(String valore) {
    super(EnumNodo.SIMBOLO);
    Objects.requireNonNull(valore, "Il valore del nodo foglia non può essere null");
    if (valore.isEmpty())
      throw new IllegalArgumentException(" Il valore del nodo non può essere vuoto");
    if (!valore.matches("[a-z]"))
      throw new IllegalArgumentException(
          valore + " il valore del nodo deve essere compreso tra le lettere a-z");
    this.valore = valore;
  }

  @Override
  public String getValoreNodo() {
    return valore;
  }

  @Override
  public String toString() {
    return valore;
  }

  @Override
  protected int compareToStessoTipoNodo(Nodo other) {
    return this.getValoreNodo().compareTo(other.getValoreNodo());
  }

  @Override
  public NumeroRazionale evaluate() {
    throw new UnsupportedOperationException(
        this + " non è possibile evaluare un nodo che ha come valore un simbolo");
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    FogliaSimbolo that = (FogliaSimbolo) o;
    return Objects.equals(valore, that.valore);
  }

  @Override
  public int hashCode() {
    return Objects.hash(valore);
  }

  @Override
  public List<Nodo> getFigliNodo() {
    return Collections.emptyList();
  }

  @Override
  public Nodo accept(Visitor visitor) {
    return visitor.visit(this);
  }
}
