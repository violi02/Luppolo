package luppolo;

import luppolo.Nodo.Nodo;

/**
 * OVERVIEW: La classe immutabile rappresenta un oggetto Espressione sotto forma di albero n-ario
 */
public class Espressione {

  /** La radice dell'albero */
  private final Nodo radice;

  /**
   * Costruisce un espressione a partire da un nodo
   *
   * @param radice radice dell'espressione
   */
  public Espressione(Nodo radice) {
    this.radice = radice;
  }

  /**
   * Metodo che restituisce il nodo radice dell'espressione
   *
   * @return la radice
   */
  public Nodo getRadice() {
    return radice;
  }

  @Override
  public String toString() {
    return radice.toString();
  }
}
