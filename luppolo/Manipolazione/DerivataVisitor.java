package luppolo.Manipolazione;

import java.util.*;
import luppolo.Nodo.*;

/**
 * OVERVIEW: La classe che implementa l'interfaccia visitor definisce un'implementazione della
 * manipolazione di derivata per ogni tipologia di nodo definita.
 */
public class DerivataVisitor implements Visitor {

  /** La variabile fissata secondo cui derivare. */
  private final String variabile;

  /**
   * Metodo che associa al DerivataVisitor la variabile secondo cui derivare
   *
   * @param variabile fissata
   * @throws NullPointerException se la variabile è null
   * @throws IllegalArgumentException se la variabile è vuota
   * @throws IllegalArgumentException se la variabile non è definita nel dominio {a-z}
   */
  public DerivataVisitor(String variabile) {
    Objects.requireNonNull(variabile, "La variabile non può essere null.");
    if (variabile.isEmpty())
      throw new IllegalArgumentException("La variabile non può essere vuota.");
    if (!variabile.matches("-?[a-z]"))
      throw new IllegalArgumentException("La variabile deve essere compresa nell'intervallo a-z");
    this.variabile = variabile;
  }

  @Override
  public Nodo visit(FogliaRazionale numeroRazionale) {
    Objects.requireNonNull(numeroRazionale, "Il nodo foglia da derivare non può essere null.");
    return new FogliaRazionale(NumeroRazionale.ZERO);
  }

  @Override
  public Nodo visit(FogliaSimbolo fogliaSimbolo) {
    Objects.requireNonNull(fogliaSimbolo, "Il nodo foglia da derivare non può essere null.");
    if (fogliaSimbolo.getValoreNodo().equals(variabile))
      return new FogliaRazionale(NumeroRazionale.UNO);
    return new FogliaRazionale(NumeroRazionale.ZERO);
  }

  @Override
  public Nodo visit(NodoPotenza nodoPotenza) {
    Objects.requireNonNull(nodoPotenza, "Il nodo potenza da derivare non può essere null.");
    Nodo base = nodoPotenza.getBase();
    Nodo derivataBase = base.accept(this);
    NumeroRazionale esponente = nodoPotenza.getEsponenteValore();
    NodoPotenza potenza =
        new NodoPotenza(base, new FogliaRazionale(esponente.sottrazione(NumeroRazionale.UNO)));
    return new NodoMoltiplicazione(List.of(potenza, derivataBase, nodoPotenza.getEsponente()));
  }

  @Override
  public Nodo visit(NodoAddizione nodoAddizione) {
    Objects.requireNonNull(nodoAddizione, "Il nodo addizione da derivare non può essere null.");
    List<Nodo> sommaAddendiDerivati = new ArrayList<>();
    for (Nodo nodo : nodoAddizione.getFigliNodo()) {
      sommaAddendiDerivati.add(nodo.accept(this));
    }
    return new NodoAddizione(sommaAddendiDerivati);
  }

  @Override
  public Nodo visit(NodoMoltiplicazione nodoMoltiplicazione) {
    Objects.requireNonNull(
        nodoMoltiplicazione, "Il nodo moltiplicazione da derivare non può essere null.");
    List<Nodo> fattori = nodoMoltiplicazione.getFigliNodo();
    List<Nodo> derivata = new ArrayList<>();
    for (int i = 0; i < fattori.size(); i++) {
      Nodo e_i = fattori.get(i).accept(this);
      List<Nodo> moltiplicazione = new ArrayList<>();
      for (int j = 0; j < fattori.size(); j++) {
        if (i != j) {
          moltiplicazione.add(fattori.get(j));
        }
      }
      moltiplicazione.add(e_i);
      derivata.add(new NodoMoltiplicazione(moltiplicazione));
    }
    return new NodoAddizione(derivata);
  }
}
