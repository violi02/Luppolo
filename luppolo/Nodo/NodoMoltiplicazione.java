package luppolo.Nodo;

import java.util.*;
import luppolo.Manipolazione.Visitor;

/**
 * OVERVIEW: La classe NodoMoltiplicazione è immutabile ed estende la classe astratta Nodo,
 * rappresentando oggetti immutabili che costituiscono nodi interni moltiplicazione, ciascuno
 * contenente una lista di nodi figli detti fattori. Inoltre la classe implementa l'interfaccia
 * Iterable, essendo cosi possibile iterare sui nodi figli.
 */
public class NodoMoltiplicazione extends Nodo implements Iterable<Nodo> {

  /**
   * AF: La classe rappresenta un nodo interno che ha come valore l'operando "*", insieme agli n
   * fattori Un esempio di nodo moltiplicazione è {nodo1,..., nodo_n} dove n è la dimensione della
   * lista di fattori IR: tipoNodo != null, fattori != null, la lista di fattori non può essere
   * vuota, per ogni nodo x appartenente alla lista di fattori x != null
   */

  /* I figli (fattori) del nodo */
  private final List<Nodo> fattori;

  /**
   * Metodo che costruisce un nodo a partire da una lista di nodi
   *
   * @param fattori lista di nodi figli del nodo this
   * @throws NullPointerException se la lista di nodi fattori è null
   * @throws NullPointerException se un nodo nella lista di fattori è null
   * @throws IllegalArgumentException se la lista di figli è vuota
   */
  public NodoMoltiplicazione(List<Nodo> fattori) {
    super(EnumNodo.MOLTIPLICAZIONE);
    Objects.requireNonNull(fattori, "L'insieme contenenti i figli del nodo non può essere null");
    if (fattori.isEmpty())
      throw new IllegalArgumentException(
          "La lista di nodi figli non può essere vuota per i nodi interni");
    for (Nodo nodo : fattori) {
      Objects.requireNonNull(nodo, "Ogni nodo figlio non può essere null");
    }
    List<Nodo> tmp = new ArrayList<>(fattori);
    Collections.sort(tmp);
    this.fattori = new ArrayList<>(tmp);
  }

  @Override
  protected int compareToStessoTipoNodo(Nodo other) {
    NodoMoltiplicazione o = (NodoMoltiplicazione) other;
    int len = Math.min(this.fattori.size(), o.fattori.size());
    for (int i = 0; i < len; i++) {
      int confronto = this.fattori.get(i).compareTo(o.fattori.get(i));
      if (confronto != 0) {
        return confronto;
      }
    }
    return Integer.compare(this.fattori.size(), o.fattori.size());
  }

  //      @Override
  //      protected int compareToStessoTipoNodo(Nodo o) {
  //        if (!o.getTipoNodo().equals(getTipoNodo()))
  //          throw new ClassCastException("Non è possibile confrontare nodi di tipi diversi.");
  //        int len = Math.min(this.fattori.size(), o.getFigliNodo().size());
  //        for (int i = 0; i < len; i++) {
  //          int confronto = this.fattori.get(i).compareTo(o.getFigliNodo().get(i));
  //          if (confronto != 0) {
  //            return confronto;
  //          }
  //        }
  //        return Integer.compare(this.fattori.size(), o.getFigliNodo().size());
  //    }

  @Override
  public String getValoreNodo() {
    return "*";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(getValoreNodo());
    sb.append("(");
    for (int i = 0; i <= fattori.size() - 1; i++) {
      sb.append(fattori.get(i).toString());
      if (i < fattori.size() - 1) {
        sb.append(", ");
      }
    }
    sb.append(")");
    return sb.toString();
  }

  @Override
  public Iterator<Nodo> iterator() {
    return Collections.unmodifiableList(fattori).iterator();
  }

  @Override
  public List<Nodo> getFigliNodo() {
    Iterator<Nodo> io = iterator();
    List<Nodo> tmp = new ArrayList<>();
    while (io.hasNext()) {
      tmp.add(io.next());
    }
    Collections.sort(tmp);
    return tmp;
  }

  @Override
  public Nodo accept(Visitor visitor) {
    return visitor.visit(this);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    NodoMoltiplicazione nodes = (NodoMoltiplicazione) o;
    return Objects.equals(fattori, nodes.fattori);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fattori);
  }

  @Override
  public NumeroRazionale evaluate() {
    NumeroRazionale risultato = new NumeroRazionale(1, 1);
    for (Nodo figlio : fattori) {
      risultato = risultato.moltiplicazione(figlio.evaluate());
    }
    return risultato;
  }
}
