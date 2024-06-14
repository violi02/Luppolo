package luppolo.Manipolazione;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import luppolo.Nodo.*;

/**
 * OVERVIEW: La classe che implementa l'interfaccia visitor definisce un'implementazione della
 * manipolazione di espansione per ogni tipologia di nodo definita.
 */
public class EspansioneVisitor implements Visitor {
  @Override
  public Nodo visit(FogliaRazionale numeroRazionale) {
    Objects.requireNonNull(numeroRazionale, "Il nodo foglia da espandere non può essere null.");
    return numeroRazionale;
  }

  @Override
  public Nodo visit(FogliaSimbolo fogliaSimbolo) {
    Objects.requireNonNull(fogliaSimbolo, "Il nodo foglia da espandere non può essere null.");
    return fogliaSimbolo;
  }

  @Override
  public Nodo visit(NodoPotenza nodoPotenza) {
    Objects.requireNonNull(nodoPotenza, "Il nodo potenza da espandere non può essere null.");
    Nodo baseEspansa = nodoPotenza.getBase().accept(this);
    NumeroRazionale esponente = nodoPotenza.getEsponenteValore();
    long p = esponente.getNumeratore();
    long q = (int) esponente.getDenominatore();
    long absP = Math.abs((int) p);

    if (p == 0) {
      return new FogliaRazionale(NumeroRazionale.UNO);
    } else if (esponente.equals(NumeroRazionale.UNO)) {
      return baseEspansa;
    } else if (p == -1) {
      return nodoPotenza;
    }
    List<Nodo> prodotti = new ArrayList<>();
    for (int i = 0; i < absP; i++) {
      prodotti.add(baseEspansa);
    }
    Nodo prodottoBase = new NodoMoltiplicazione(prodotti);
    if (baseEspansa.getTipoNodo() == EnumNodo.ADDIZIONE) {
      prodottoBase = prodottoBase.accept(this);
    }
    if (prodotti.size() == 1) prodottoBase = prodotti.get(0);

    long exp = (p / absP) / q;
    if (exp == 1) {
      return prodottoBase;
    }
    return new NodoPotenza(prodottoBase, new FogliaRazionale(new NumeroRazionale(p / absP, q)));
  }

  @Override
  public Nodo visit(NodoAddizione nodoAddizione) {
    Objects.requireNonNull(nodoAddizione, "Il nodo addizione da espandere non può essere null.");
    List<Nodo> addendiSemplificati = new ArrayList<>();
    for (Nodo addendo : nodoAddizione.getFigliNodo()) {
      addendiSemplificati.add(addendo.accept(this));
    }
    return new NodoAddizione(addendiSemplificati);
  }

  @Override
  public Nodo visit(NodoMoltiplicazione nodoMoltiplicazione) {
    Objects.requireNonNull(
        nodoMoltiplicazione, "Il nodo moltiplicazione da espandere non può essere null.");
    int sizeFigli = nodoMoltiplicazione.getFigliNodo().size();
    int countNodiAdd = 0;

    if (sizeFigli < 2) return nodoMoltiplicazione.getFigliNodo().get(0);

    Nodo secondaExp = nodoMoltiplicazione.getFigliNodo().get(sizeFigli - 1).accept(this);
    List<Nodo> primaExp = new ArrayList<>();

    for (int i = 0; i < sizeFigli - 1; i++) {
      primaExp.add(nodoMoltiplicazione.getFigliNodo().get(i).accept(this));
      if (nodoMoltiplicazione.getFigliNodo().get(i).getTipoNodo().equals(EnumNodo.ADDIZIONE))
        countNodiAdd++;
    }

    if (secondaExp.getTipoNodo().equals(EnumNodo.ADDIZIONE)) countNodiAdd++;

    if (countNodiAdd == 0) {
      primaExp.add(secondaExp);
      return new NodoMoltiplicazione(primaExp);
    }

    if (countNodiAdd > 2) {
      Nodo espansionePrimaExp = new NodoMoltiplicazione(primaExp).accept(this);
      primaExp =
          (espansionePrimaExp.getTipoNodo().equals(EnumNodo.MOLTIPLICAZIONE))
              ? espansionePrimaExp.getFigliNodo()
              : List.of(espansionePrimaExp);
    }

    List<Nodo> espansi = new ArrayList<>();
    for (Nodo nodo : primaExp) {
      if (nodo.getTipoNodo().equals(EnumNodo.ADDIZIONE)
          || secondaExp.getTipoNodo().equals(EnumNodo.ADDIZIONE)) {

        Nodo nodoAddizione = nodo.getTipoNodo().equals(EnumNodo.ADDIZIONE) ? nodo : secondaExp;
        Nodo altroNodo = nodo.getTipoNodo().equals(EnumNodo.ADDIZIONE) ? secondaExp : nodo;

        for (Nodo figlioAddizione : nodoAddizione.getFigliNodo()) {
          if (altroNodo.getTipoNodo().equals(EnumNodo.ADDIZIONE)) {
            for (Nodo altroFiglio : altroNodo.getFigliNodo()) {
              espansi.add(new NodoMoltiplicazione(Arrays.asList(figlioAddizione, altroFiglio)));
            }
          } else {
            espansi.add(new NodoMoltiplicazione(Arrays.asList(figlioAddizione, altroNodo)));
          }
        }
      }
    }
    return new NodoAddizione(espansi);
  }
}
