package luppolo.Nodo;

import java.util.*;
import luppolo.Manipolazione.Visitor;

/**
 * OVERVIEW: La classe astratta definisce oggetti immutabili che rappresentano un Nodo di un albero
 * n-ario. La classe implementa l'interfaccai Comparable su oggetti di tipo Nodo per il confronto
 * tra nodi di diverse tipologie.
 *
 * <p>Questa classe astratta definisce le proprietà e i comportamenti comuni che tutti i nodi
 * dovrebbero avere, il compare tra nodi di tipo diverso, tra nodi dello stesso tipo, un metodo che
 * restituisce la tipologia di nodo definita per quel tipo specifico, il valore del nodo in formato
 * stringa, restituisce i figli del nodo, il metodo che a partire da un nodo restituisce il
 * corrispettivo in numero razionale, il metodo che accetta l'oggetto Visitor. Ogni nodo possiede
 * una tipologia specifica definita dall'enum EnumNodo.
 */
public abstract class Nodo implements Comparable<Nodo> {

  /**
   * IR: tipoNodo != null AF: il nodo avrà la tipologia specifica corrispondente al valore dell'Enum
   */

  /** Enum che definisce il tipo di Nodo */
  private final EnumNodo tipoNodo;

  /**
   * Inizializza la tipologia del nodo
   *
   * @param tipoNodo il tipo di nodo
   * @throws NullPointerException se la tipologia di nodo è null
   */
  protected Nodo(EnumNodo tipoNodo) {
    Objects.requireNonNull(tipoNodo, "Il tipo di nodo non può essere null.");
    this.tipoNodo = tipoNodo;
  }

  /**
   * Metodo che definisce la tipologia di nodo
   *
   * @return la tipologia del nodo su cui viene chiamato
   */
  public EnumNodo getTipoNodo() {
    return tipoNodo;
  }

  @Override
  public int compareTo(Nodo other) {
    EnumNodo thisType = this.getTipoNodo();
    EnumNodo otherType = other.getTipoNodo();

    Integer o1 = thisType.ordinal();
    Integer o2 = otherType.ordinal();

    int comparazioneTipo = o1.compareTo(o2);
    if (comparazioneTipo != 0) {
      return comparazioneTipo;
    }
    return compareToStessoTipoNodo(other);
  }

  /**
   * Metodo astratto che confronta due nodi dello stesso tipo
   *
   * @param other il nodo da confrontare
   * @return un intero
   * @throws ClassCastException se i nodi da confrontare sono di tipo differente.
   */
  abstract int compareToStessoTipoNodo(Nodo other) throws ClassCastException;

  /**
   * Metodo astratto che Restituisce il valore del nodo.
   *
   * @return il valore del nodo come stringa
   */
  public abstract String getValoreNodo();

  /**
   * Valuta il nodo e restituisce un NumeroRazionale.
   *
   * @return un NumeroRazionale
   * @throws UnsupportedOperationException se il nodo non può essere valutato
   */
  public abstract NumeroRazionale evaluate() throws UnsupportedOperationException;

  /**
   * Metodo che restituisce la lista di figli di un nodo. I nodi foglia restituiscono una lista
   * vuota.
   *
   * @return una lista di nodi figli
   */
  public abstract List<Nodo> getFigliNodo();

  /**
   * Metodo astratto che accetta un oggetto di tipo Visitor e restituisce un nodo
   *
   * @param visitor il visitor
   * @return un Nodo
   */
  public abstract Nodo accept(Visitor visitor);
}
