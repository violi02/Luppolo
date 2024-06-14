package luppolo.Rappresentazione;

import java.util.List;
import java.util.Objects;
import luppolo.Espressione;
import luppolo.Nodo.*;

/**
 * OVERVIW: La classe immutabile rappresenta un oggetto ALberoTestuale che è la rappresentazione
 * testuale dell'espressione.
 */
public class AlberoTestuale {

  /** L'espressione */
  private final Espressione exp;

  /**
   * Metodo che costruisce un albero etstuale
   *
   * @param exp l'espressione
   * @throws NullPointerException se l'espressione è null.
   */
  public AlberoTestuale(Espressione exp) {
    Objects.requireNonNull(exp, "L'espressione non può essere null.");
    this.exp = exp;
  }

  /**
   * Metodo che restituisce l'espressione da rappresentare
   *
   * @return l'espressione
   */
  private Espressione getEspressione() {
    return exp;
  }

  /**
   * Metodo che restituisce la rappresentazione testuale dell'espressione in formato stringa
   *
   * @return una stringa rappresentente l'espressione
   */
  public String printTree() {
    StringBuilder sb = new StringBuilder();
    recursiveTree("", getEspressione().getRadice(), false, sb, true);
    return sb.toString();
  }

  /**
   * Metodo statico che costruisce ricorsivamente la stringa rappresentativa dell'espressione a
   * partire dal nodo radice
   *
   * @param prefix il prefisso
   * @param nodo il nodo da rappresentera
   * @param isUltimoFiglioGenitore booleano che indica se è l'utlimo figlio del genitore
   * @param sb stringbuilder che tiene conto della stringa gia costruita
   * @param isRadice s booleano che indica se stiamo trattando un nodo radice
   * @throws NullPointerException se la stringa che identifica il prefisso è null
   * @throws NullPointerException se il nodo è null
   */
  private static void recursiveTree(
      String prefix,
      Nodo nodo,
      boolean isUltimoFiglioGenitore,
      StringBuilder sb,
      boolean isRadice) {
    Objects.requireNonNull(prefix, "Il prefisso non può essere nullo");
    Objects.requireNonNull(nodo, "Il nodo non può essre null");
    if (isRadice) {
      sb.append(nodo.getValoreNodo()).append("\n");
    } else {
      sb.append(prefix)
          .append(isUltimoFiglioGenitore ? "╰── " : "├── ")
          .append(nodo.getValoreNodo())
          .append("\n");
    }
    List<Nodo> figli = nodo.getFigliNodo();
    if (figli.isEmpty()) {
      return;
    }
    for (int i = 0; i < figli.size(); i++) {
      Nodo figlio = figli.get(i);
      boolean isUltimoFiglio = (i == figli.size() - 1);
      String nuovoPrefix =
          isRadice ? prefix : (prefix + (isUltimoFiglioGenitore ? "    " : "│   "));
      recursiveTree(nuovoPrefix, figlio, isUltimoFiglio, sb, false);
    }
  }
}
