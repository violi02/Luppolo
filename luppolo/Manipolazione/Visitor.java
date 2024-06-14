package luppolo.Manipolazione;

import luppolo.Nodo.*;

/** OVERVIEW: l'interfaccia definisce un metodo visit per ogni tipologia di nodo */
public interface Visitor {
  /**
   * Metodo che prende in ingresso un nodo di tipo Foglia Razionale e restituisce un Nodo generico.
   *
   * @param numeroRazionale nodo da visitare
   * @return un Nodo
   * @throws NullPointerException se il Nodo passato è null.
   */
  Nodo visit(FogliaRazionale numeroRazionale) throws NullPointerException;

  /**
   * Metodo che prende in ingresso un nodo di tipo Foglia Simbolo e restituisce un Nodo generico.
   *
   * @param fogliaSimbolo nodo da visitare
   * @return un Nodo
   * @throws NullPointerException se il Nodo passato è null.
   */
  Nodo visit(FogliaSimbolo fogliaSimbolo) throws NullPointerException;

  /**
   * Metodo che prende in ingresso un nodo di tipo NodoPotenza e restituisce un Nodo generico.
   *
   * @param nodoPotenza nodo da visitare
   * @return un Nodo
   * @throws NullPointerException se il Nodo passato è null.
   * @throws IllegalArgumentException se dopo le opportune semplificazioni si ha come risultato un
   *     nodo potenza del tipo^(0,0)
   */
  Nodo visit(NodoPotenza nodoPotenza) throws NullPointerException, IllegalArgumentException;

  /**
   * Metodo che prende in ingresso un nodo di tipo NodoAddizione e restituisce un Nodo generico.
   *
   * @param nodoAddizione nodo da visitare
   * @return un Nodo
   * @throws NullPointerException se il Nodo passato è null.
   */
  Nodo visit(NodoAddizione nodoAddizione) throws NullPointerException;

  /**
   * Metodo che prende in ingresso un nodo di tipo NodoMoltiplicazione e restituisce un Nodo
   * generico.
   *
   * @param nodoMoltiplicazione nodo da visitare
   * @return un Nodo
   * @throws NullPointerException se il Nodo passato è null.
   */
  Nodo visit(NodoMoltiplicazione nodoMoltiplicazione) throws NullPointerException;
}
