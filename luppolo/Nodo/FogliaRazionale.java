package luppolo.Nodo;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import luppolo.Manipolazione.Visitor;

/**
 * OVERVIEW: La classe FogliaRazionale è immutabile ed estende la classe astratta Nodo,
 * rappresentando oggetti immutabili che costituiscono nodi foglia di un albero n-ario, ciascuno
 * contenente un valore di tipo NumeroRazionale.
 */
public class FogliaRazionale extends Nodo {

  /**
   * AF: L'oggetto fogliaRazionale è un nodo senza figli, con valore un oggetto del tipo
   * NumeroRazionale Un esempio di foglia razionale è {numeratore/denominatore} dove numeratore e
   * denominatore definiscono attraverso un oggetto di tipo Numero Razionale il valore del nodo. IR:
   * valore != null
   */

  /* Il valore della foglia definita come un Numero Razionale */
  private final NumeroRazionale valore;

  /**
   * Costruisce una foglia razionale a partire da un numero razionale
   *
   * @param valore della foglia
   * @throws NullPointerException se il valore è null.
   */
  public FogliaRazionale(NumeroRazionale valore) {
    super(EnumNodo.RAZIONALE);
    Objects.requireNonNull(valore, "Il valore non può essere null.");
    this.valore = valore;
  }

  /**
   * Metodo che definisce il numeratore del numero razionale associato alla foglia
   *
   * @return il numeratore del numero razionale
   */
  public long getNumeratore() {
    return valore.getNumeratore();
  }

  /**
   * Metodo che definisce il denominatore del numero razionale associato alla foglia
   *
   * @return il denominatore del numero razionale
   */
  public long getDenominatore() {
    return valore.getDenominatore();
  }

  @Override
  public String getValoreNodo() {
    return toString();
  }

  @Override
  public NumeroRazionale evaluate() {
    return valore;
  }

  @Override
  public List<Nodo> getFigliNodo() {
    return Collections.emptyList();
  }

  @Override
  public String toString() {
    return valore.toString();
  }

  @Override
  protected int compareToStessoTipoNodo(Nodo other) {
    return this.evaluate().compareTo(other.evaluate());
  }

  @Override
  public Nodo accept(Visitor visitor) {
    return visitor.visit(this);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    FogliaRazionale that = (FogliaRazionale) o;
    return Objects.equals(valore, that.valore);
  }

  @Override
  public int hashCode() {
    return Objects.hash(valore);
  }
}
