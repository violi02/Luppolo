package luppolo.Nodo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import luppolo.Manipolazione.Visitor;

/**
 * OVERVIEW: La classe NodoPotenza è immutabile ed estende la classe astratta Nodo, rappresentando
 * oggetti immutabili che costituiscono nodi interni potenza, ciascuno contenenti due Nodi, uno che
 * definisce la base e uno che definisce l'esponente di tipologia fissata FogliaRazionale.
 */
public class NodoPotenza extends Nodo {

  /**
   * AF: La classe rappresenta un nodo interno che ha come valore l'operando "^" Un esempio di nodo
   * potenza è la tupla {Nodo, FogliaRazionale} IR: base != null esponente != null;
   */

  /** Figlio che rappresenta la base del nodo potenza */
  private final Nodo base;

  /** Figlio che rappresenta il nodo esponente del nodo potenza */
  private final FogliaRazionale esponente;

  /**
   * Metodo che costruisce un nodo a partire da un nodo base e da un nodo esponente
   *
   * @param base base del nodo potenza
   * @param esponente l'esponente del nodo potenza
   * @throws NullPointerException se il nodo base è null
   * @throws NullPointerException se il nodo esponente è null
   */
  public NodoPotenza(Nodo base, FogliaRazionale esponente) {
    super(EnumNodo.POTENZA);
    Objects.requireNonNull(base, "La base del nodo non può essere null");
    Objects.requireNonNull(esponente, "Il nodo esponente non può essere null");
    this.base = base;
    this.esponente = esponente;
  }

  /**
   * Metodo che restituisce il valore come NUmero Razionale dell'esponente
   *
   * @return il valore dell'esponente
   */
  public NumeroRazionale getEsponenteValore() {
    return new NumeroRazionale(esponente.getNumeratore(), esponente.getDenominatore());
  }

  /**
   * Metodo che restituisce l'esponente
   *
   * @return l'epsonente
   */
  public Nodo getEsponente() {
    return esponente;
  }

  /**
   * Metodo che restituisce la base
   *
   * @return la base
   */
  public Nodo getBase() {
    return base;
  }

  @Override
  public String toString() {
    return "^(" + base.toString() + ", " + esponente.toString() + ")";
  }

  @Override
  protected int compareToStessoTipoNodo(Nodo o) {
    if (!o.getTipoNodo().equals(getTipoNodo()))
      throw new ClassCastException("Non è possibile confrontare nodi di tipi diversi.");
    int confronto = this.getFigliNodo().get(0).compareTo(o.getFigliNodo().get(0));
    if (confronto != 0) {
      return confronto;
    }
    return this.getFigliNodo().get(1).compareTo(o.getFigliNodo().get(1));
  }

  @Override
  public String getValoreNodo() {
    return "^";
  }

  @Override
  public List<Nodo> getFigliNodo() {
    return new ArrayList<>(List.of(base, esponente));
  }

  @Override
  public Nodo accept(Visitor visitor) {
    return visitor.visit(this);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    NodoPotenza that = (NodoPotenza) o;
    return Objects.equals(base, that.base) && Objects.equals(esponente, that.esponente);
  }

  @Override
  public int hashCode() {
    return Objects.hash(base);
  }

  @Override
  public NumeroRazionale evaluate() {
    NumeroRazionale baseValue = base.evaluate();
    NumeroRazionale esponenteValue = esponente.evaluate();
    Nodo nodo = baseValue.potenzaReturnNodo(esponenteValue);
    return nodo.evaluate();
  }
}
