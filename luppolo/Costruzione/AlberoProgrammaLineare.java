package luppolo.Costruzione;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import luppolo.Espressione;
import luppolo.Nodo.*;

/**
 * OVERVEIW: La Classe costruisce un espressione a partire dalla sua rappresentazione come programma
 * lineare
 */
public class AlberoProgrammaLineare {

  /** Il programma lineare */
  private final List<String> righe;

  /**
   * Costruisce l'espressione a partire dal programma lineare in ingresso
   *
   * @param sequenzaProgrammi il programma lineare
   * @throws NullPointerException se la sequenza di programmi è null
   */
  public AlberoProgrammaLineare(List<String> sequenzaProgrammi) {
    Objects.requireNonNull(sequenzaProgrammi, "Il programma lineare non può essere null");
    this.righe = List.copyOf(sequenzaProgrammi);
  }

  /**
   * Metodo che restituisce una copia delle espressioni del programma lineare
   *
   * @return la lista di comandi
   */
  private List<String> getEspressioni() {
    return new ArrayList<>(righe);
  }

  /**
   * Metodo che costruisce un Espressione a partire da un programma lineare
   *
   * @return un espressione
   * @throws IllegalArgumentException se l'operando passato non è definito.
   */
  public Espressione alberoProgrammaLineare() {
    List<Nodo> nodiExp = new ArrayList<>();
    for (String riga : getEspressioni()) {
      String[] espressioni = riga.split("\s+");
      String operando = espressioni[0];
      switch (operando) {
        case ".":
          nodiExp.add(creaNodoFoglia(espressioni[1]));
          break;
        case "+", "-", "*", "/", "^":
          nodiExp.add(creaNodo(nodiExp, espressioni));
          break;
        default:
          throw new IllegalArgumentException(operando + " non è definito");
      }
    }
    return new Espressione(nodiExp.get(nodiExp.size() - 1));
  }

  /**
   * Crea un nodo a partire da una lista di nodi recuperando i nodi necessari a partire dalla lista
   * di stringhe corrispondenti agli indici presi in ingresso
   *
   * @param nodi presenti nella lista
   * @param riga che indica gli indici dei nodi da prendere dalla lista di nodi
   * @return un nodo
   * @throws NullPointerException se la lista di nodi è null
   * @throws NullPointerException se la lista di indici è null
   * @throws IndexOutOfBoundsException se l'indice eccede la dimensione del vettore nodi
   */
  private Nodo creaNodo(List<Nodo> nodi, String[] riga) {
    Objects.requireNonNull(nodi, "La lista di nodi non può essere null");
    Objects.requireNonNull(riga, "La lista di indici non può essere null");
    List<Nodo> nodiDiInteresse = new ArrayList<>();
    for (int i = 1; i < riga.length; i++) {
      if (Integer.parseInt(riga[i]) < 0 || Integer.parseInt(riga[i]) >= nodi.size())
        throw new IndexOutOfBoundsException("L'indice eccede la dimensione del vettore");
      nodiDiInteresse.add(nodi.get(Integer.parseInt(riga[i])));
    }
    return creaPerOperando(nodiDiInteresse, riga[0]);
  }

  /**
   * Metodo che costruisce un nodo a partire dall'operando e da i nodi che servono in base agli
   * indici ricevuti in ingresso
   *
   * @param nodiDiInteresse nodi che corrispondono agli indici presenti nella riga di ingresso
   * @param operando l'operando
   * @return un Nodo
   * @throws NullPointerException se l'operando è null
   * @throws NullPointerException se la lista di nodi è null
   * @throws IllegalArgumentException se la lista di nodi è vuota
   * @throws IllegalArgumentException se l'operando è vuoto
   * @throws IllegalArgumentException se l'operando non è valido
   */
  private Nodo creaPerOperando(List<Nodo> nodiDiInteresse, String operando) {
    Objects.requireNonNull(nodiDiInteresse, "La lista non può essere null.");
    if (nodiDiInteresse.isEmpty())
      throw new IllegalArgumentException("La lista di nodi non pyò essere vuota");
    Objects.requireNonNull(operando, "L'operando non può essere null.");
    if (operando.isEmpty())
      throw new IllegalArgumentException(
          "La stringa che rappresenta l'operando non può essere vuoto.");
    if (nodiDiInteresse.size() == 1) {
      return nodiDiInteresse.get(0);
    }
    switch (operando) {
      case "+":
        return new NodoAddizione(nodiDiInteresse);
      case "-":
        List<Nodo> children = new ArrayList<>();
        children.add(nodiDiInteresse.get(0));
        for (int i = 1; i < nodiDiInteresse.size(); i++) {
          Nodo convertiNodo =
              new NodoMoltiplicazione(
                  List.of(nodiDiInteresse.get(i), new FogliaRazionale(new NumeroRazionale(-1, 1))));
          children.add(convertiNodo);
        }
        return new NodoAddizione(children);
      case "*":
        return new NodoMoltiplicazione(nodiDiInteresse);
      case "^":
        Nodo base = nodiDiInteresse.get(0);
        NumeroRazionale esponenteValore =
            nodiDiInteresse.get(nodiDiInteresse.size() - 1).evaluate();
        for (int i = nodiDiInteresse.size() - 2; i >= 1; i--) {
          esponenteValore =
              nodiDiInteresse.get(i).evaluate().potenzaReturnNodo(esponenteValore).evaluate();
        }
        return new NodoPotenza(base, new FogliaRazionale(esponenteValore));
      case "/":
        List<Nodo> nodiMoltiplicazione = new ArrayList<>();
        nodiMoltiplicazione.add(nodiDiInteresse.get(0));
        for (int i = 1; i < nodiDiInteresse.size(); i++) {
          Nodo inversoNodo =
              new NodoPotenza(
                  nodiDiInteresse.get(i), new FogliaRazionale(new NumeroRazionale(-1, 1)));
          nodiMoltiplicazione.add(inversoNodo);
        }
        return new NodoMoltiplicazione(nodiMoltiplicazione);
      default:
        throw new IllegalArgumentException("Operatore non valido: " + operando);
    }
  }

  /**
   * Metodo che restituisce un nodo foglia da una stringa
   *
   * @param elemento da cui creare il nodo foglia
   * @return un nodo foglia specifico
   * @throws NullPointerException se l'elemento è null.
   * @throws IllegalArgumentException se l'elemento è vuoto
   */
  private Nodo creaNodoFoglia(String elemento) {
    Objects.requireNonNull(elemento, "La stringa che definisce l'elemnto non può essere null");
    if (elemento.isEmpty()) throw new IllegalArgumentException("L'elemento non può essere vuoto");
    if (elemento.matches("-?[a-z]")) {
      return new FogliaSimbolo(elemento);
    } else if (elemento.matches("^-\\d+$")) {
      return new FogliaRazionale(new NumeroRazionale(Integer.parseInt(elemento), 1));
    } else {
      return new FogliaRazionale(new NumeroRazionale(Integer.parseInt(elemento), 1));
    }
  }
}
