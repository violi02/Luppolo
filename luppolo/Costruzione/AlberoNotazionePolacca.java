package luppolo.Costruzione;

import java.util.*;
import luppolo.Espressione;
import luppolo.Nodo.*;

/**
 * OVERVIEW: La classe immutabile crea un'espressione a partire dalla sua rappresentazione in
 * notazione polacca
 */
public class AlberoNotazionePolacca {

  /** L'espressione in notazione polacca */
  private final String espressione;

  /**
   * Metodo che costruisce un AlberoNotazionePOlacca da una stringa
   *
   * @param espressione associata all'albero definito con notazione polacca
   * @throws NullPointerException se l'espressione è null
   * @throws IllegalArgumentException se la stringa che rappresenta l'espressione è vuota
   */
  public AlberoNotazionePolacca(String espressione) {
    Objects.requireNonNull(espressione);
    if (espressione.isEmpty())
      throw new IllegalArgumentException("L'espressione non può essere vuota");
    this.espressione = espressione;
  }

  /**
   * Metodo che restituisce l'espressione in notazione polacca
   *
   * @return l'espressione
   */
  private String getEspressione() {
    return espressione;
  }

  /**
   * Metodo che restituisce un' Espressione a partire dall'espressione in notazione polacca
   *
   * @return l'espressione
   * @throws NoSuchElementException se non ci sono abbastanza elementi per costruire il nodo.
   */
  public Espressione alberoNotazionePolacca() {
    String[] exp = getEspressione().split("\s+");

    Stack<Nodo> pila = new Stack<>();

    for (int index = exp.length - 1; index >= 0; index--) {
      String elemento = exp[index];
      if (elemento.matches("[+*-/^]")) {
        List<Nodo> estratti = new ArrayList<>();
        if (pila.size() < 2)
          throw new NoSuchElementException(
              "Non ci sono abbastanza elementi nella pila per poterne creare un altro");
        estratti.add(pila.pop());
        estratti.add(pila.pop());

        Nodo daInserire = creoNodo(elemento, estratti);

        pila.push(daInserire);
      } else {
        pila.push(creaNodoFoglia(elemento));
      }
    }
    return new Espressione(pila.pop());
  }

  /**
   * Metodo che dato un operatore e una lista di nodi restituisce il nodo composto specifico
   *
   * @param operatore l'operatore del nodo
   * @param estratti lista contenente i nodi estratti dalla pila
   * @return un Nodo in base all'operatore
   * @throws NullPointerException se l'operatore è null
   * @throws IllegalArgumentException se la stringa che rappresenta l'operatore è vuota
   * @throws IllegalArgumentException se l'operatore passato non è definito.
   */
  private Nodo creoNodo(String operatore, List<Nodo> estratti) {
    Objects.requireNonNull(operatore);
    if (operatore.isEmpty())
      throw new IllegalArgumentException("L'espressione non può essere vuota");
    Nodo nodoA = estratti.get(1);
    Nodo nodoB = estratti.get(0);
    switch (operatore) {
      case "+":
        return new NodoAddizione(estratti);
      case "-":
        return new NodoAddizione(List.of(nodoB, convertiSegnoMeno(nodoA)));
      case "*":
        return new NodoMoltiplicazione(estratti);
      case "/":
        return new NodoMoltiplicazione(List.of(nodoB, convertiSegnoDivisione(nodoA)));
      case "^":
        return new NodoPotenza(nodoB, new FogliaRazionale(nodoA.evaluate()));
      default:
        throw new IllegalArgumentException(operatore + " non è un operatore valido");
    }
  }

  /**
   * Metodo che restituisce, dato un nodo divisione, un nodo potenza del tipo ^(nodo,-1)
   *
   * @param nodo da convertire
   * @return un nodo potenza
   * @throws NullPointerException il nodo da convertire non può essere null
   */
  private NodoPotenza convertiSegnoDivisione(Nodo nodo) {
    Objects.requireNonNull(nodo);
    return new NodoPotenza(nodo, new FogliaRazionale(new NumeroRazionale(-1, 1)));
  }

  /**
   * Metodo che restituisce, dato un nodo sottrazione, un nodo moltiplicazione del tipo *(-1,nodo)
   *
   * @param nodo da convertire
   * @return nodo moltiplicazione
   * @throws NullPointerException il nodo da convertire non può essere null
   */
  private NodoMoltiplicazione convertiSegnoMeno(Nodo nodo) {
    Objects.requireNonNull(nodo);
    return new NodoMoltiplicazione(List.of(new FogliaRazionale(new NumeroRazionale(-1, 1)), nodo));
  }

  /**
   * Metodo che data una stringa restituisce il nodo foglia corrispondente
   *
   * @param elemento preso in ingresso dalla lettura dell'espressione
   * @return un Nodo
   * @throws NullPointerException se l'elemento da cui costruire il nodo è null
   * @throws IllegalArgumentException se l'elemento è vuoto
   */
  private Nodo creaNodoFoglia(String elemento) {
    Objects.requireNonNull(elemento, "L'elemento non può essere null");
    if (elemento.isEmpty()) throw new IllegalArgumentException("L'elemento non èuò essere vuoto");
    if (elemento.matches("-?[a-z]")) {
      return new FogliaSimbolo(elemento);
    } else if (elemento.matches("^-\\d+$")) {
      return new FogliaRazionale(new NumeroRazionale(Long.parseLong(elemento), 1));
    } else {
      return new FogliaRazionale(new NumeroRazionale(Long.parseLong(elemento), 1));
    }
  }
}
