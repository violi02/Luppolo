package luppolo.Nodo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import luppolo.Manipolazione.Visitor;

/**
 * OVERVIEW: La classe NodoAddizione è immutabile ed estende la classe astratta Nodo, rappresentando
 * oggetti immutabili che costituiscono nodi interni addizione, ciascuno contenente una lista di
 * nodi figli detti addendi. Inoltre la classe implementa l'interfaccia Iterable su oggetti di tipo
 * Nodo cosi da poter iterare sui figli.
 */
public class NodoAddizione extends Nodo implements Iterable<Nodo> {

  /**
   * AF: La classe rappresenta un nodo interno, che ha come valore l'operando "+" e come figli gli n
   * addendi Un esempio di nodo addizione è {nodo1,..., nodo_n} dove n è la dimensione della lista
   * di addendi IR: tipoNodo != null, addendi != null addendi, la lista di addendi non può essere
   * vuota, per ogni nodo x appartenente alla lista di addendi x != null
   */

  /** I figli (addendi) del nodo */
  private final List<Nodo> addendi;

  /**
   * Metodo che costruisce un nodo a partire da una lista di nodi
   *
   * @param addendi lista di nodi figli del nodo this
   * @throws NullPointerException se la lista di addendi è null
   * @throws NullPointerException se un nodo nella lista di addendi è null
   * @throws IllegalArgumentException se la lista di figli è vuota
   */
  public NodoAddizione(List<Nodo> addendi) {
    super(EnumNodo.ADDIZIONE);
    Objects.requireNonNull(addendi, "L'insieme contenenti i figli del nodo non può essere null");
    if (addendi.isEmpty())
      throw new IllegalArgumentException(
          "La lista di nodi figli non può essere vuota per i nodi interni");
    for (Nodo nodo : addendi) {
      Objects.requireNonNull(nodo, "Ogni nodo figlio non può essere null");
    }
    List<Nodo> tmp = new ArrayList<>(addendi);
    Collections.sort(tmp);
    this.addendi = new ArrayList<>(tmp);
  }

  @Override
  public String getValoreNodo() {
    return "+";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(getValoreNodo());
    sb.append("(");
    for (int i = 0; i <= addendi.size() - 1; i++) {
      sb.append(addendi.get(i).toString());
      if (i < addendi.size() - 1) {
        sb.append(", ");
      }
    }
    sb.append(")");
    return sb.toString();
  }

  @Override
  protected int compareToStessoTipoNodo(Nodo o) {
    if (!o.getTipoNodo().equals(getTipoNodo()))
      throw new ClassCastException("Non è possibile confrontare nodi di tipi diversi.");
    int len = Math.min(this.addendi.size(), o.getFigliNodo().size());
    for (int i = 0; i < len; i++) {
      int confronto = this.addendi.get(i).compareTo(o.getFigliNodo().get(i));
      if (confronto != 0) {
        return confronto;
      }
    }

    return Integer.compare(this.addendi.size(), o.getFigliNodo().size());
  }

  @Override
  public Iterator<Nodo> iterator() {
    return Collections.unmodifiableList(addendi).iterator();
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
    NodoAddizione nodes = (NodoAddizione) o;
    return this.getTipoNodo() == nodes.getTipoNodo() && Objects.equals(addendi, nodes.addendi);
  }

  @Override
  public int hashCode() {
    return Objects.hash(addendi, getTipoNodo());
  }

  @Override
  public NumeroRazionale evaluate() {
    NumeroRazionale risultato = new NumeroRazionale(0, 1);
    for (Nodo figlio : addendi) {
      risultato = risultato.addizione(figlio.evaluate());
    }
    return risultato;
  }
}
