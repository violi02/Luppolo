package luppolo.Manipolazione;

import java.util.*;
import luppolo.Nodo.*;

/**
 * OVERVIEW: La classe che implementa l'interfaccia visitor definisce un'implementazione della
 * manipolazione di semplificazione per ogni tipologia di nodo definita.
 */
public class SemplificaVisitor implements Visitor {
  @Override
  public Nodo visit(FogliaRazionale nodo) {
    Objects.requireNonNull(nodo, "Il nodo foglia razionale da semplificare non può essere null.");
    NumeroRazionale numeroRazionale =
        new NumeroRazionale(nodo.getNumeratore(), nodo.getDenominatore());
    return new FogliaRazionale(numeroRazionale);
  }

  @Override
  public Nodo visit(FogliaSimbolo fogliaSimbolo) {
    Objects.requireNonNull(
        fogliaSimbolo, "Il nodo foglia simbolo da semplificare non può essere null.");
    return new FogliaSimbolo(fogliaSimbolo.getValoreNodo());
  }

  @Override
  public Nodo visit(NodoPotenza nodoPotenza) {
    Objects.requireNonNull(nodoPotenza, "Il nodo potenza da semplificare non può essere null.");
    Nodo baseSemplificata = nodoPotenza.getBase().accept(this);
    NumeroRazionale esponente = nodoPotenza.getEsponenteValore();

    if (esponente.equals(NumeroRazionale.UNO)) {
      return baseSemplificata;
    }

    if (baseSemplificata.getTipoNodo() == EnumNodo.RAZIONALE) {
      NumeroRazionale num = baseSemplificata.evaluate();
      if (num.equals(NumeroRazionale.ZERO) && esponente.equals(NumeroRazionale.ZERO))
        throw new IllegalArgumentException("Forma indeterminata 0^0");
      return num.potenzaReturnNodo(esponente);
    }

    if (baseSemplificata.getTipoNodo() == EnumNodo.POTENZA) {
      NodoPotenza basePotenza = (NodoPotenza) baseSemplificata;
      Nodo baseInterna = basePotenza.getBase();
      NumeroRazionale esponenteInterno = basePotenza.getEsponenteValore();
      NumeroRazionale nuovoEsponente = esponenteInterno.moltiplicazione(esponente);
      if (nuovoEsponente.equals(NumeroRazionale.ZERO))
        return new FogliaRazionale(NumeroRazionale.UNO);
      if (baseInterna.equals(new FogliaRazionale(NumeroRazionale.ZERO)))
        return new FogliaRazionale(NumeroRazionale.ZERO);
      if (baseInterna.getTipoNodo() != EnumNodo.SIMBOLO) {
        for (Nodo figlio : baseInterna.getFigliNodo()) {
          if (figlio.getTipoNodo() == EnumNodo.SIMBOLO) {
            return new NodoPotenza(baseInterna, new FogliaRazionale(nuovoEsponente));
          }
        }
        return baseInterna.evaluate().potenzaReturnNodo(nuovoEsponente);
      }
      return new NodoPotenza(baseInterna, new FogliaRazionale(nuovoEsponente));
    }
    return new NodoPotenza(baseSemplificata, new FogliaRazionale(esponente));
  }

  @Override
  public Nodo visit(NodoAddizione nodoAddizione) {
    Objects.requireNonNull(nodoAddizione, "Il nodo addizione da semplificare non può essere null.");
    List<Nodo> addendiSemplificati = new ArrayList<>();
    for (Nodo addendo : nodoAddizione.getFigliNodo()) {
      Nodo addendoSemplificato = addendo.accept(this);
      if (addendoSemplificato.getTipoNodo() == EnumNodo.ADDIZIONE) {
        addendiSemplificati.addAll((addendoSemplificato).getFigliNodo());
      } else {
        addendiSemplificati.add(addendoSemplificato);
      }
    }

    List<Nodo> nodiNonRazionali = new ArrayList<>();
    NumeroRazionale sommaNumeri = NumeroRazionale.ZERO;

    for (Nodo figlio : addendiSemplificati) {
      if (figlio.getTipoNodo() == EnumNodo.RAZIONALE) {
        NumeroRazionale valore = figlio.evaluate();
        if (!valore.equals(NumeroRazionale.ZERO)) {
          sommaNumeri = sommaNumeri.addizione(valore);
        }
      } else {
        nodiNonRazionali.add(figlio);
      }
    }
    if (nodiNonRazionali.isEmpty()) {
      return new FogliaRazionale(sommaNumeri);
    }
    if (!sommaNumeri.equals(NumeroRazionale.ZERO)) {
      nodiNonRazionali.add(new FogliaRazionale(sommaNumeri));
    }

    Map<Nodo, NumeroRazionale> baseFattoreMap = new HashMap<>();
    for (Nodo nodo : nodiNonRazionali) {
      NumeroRazionale fattore = NumeroRazionale.UNO;
      if (nodo.getTipoNodo() == EnumNodo.MOLTIPLICAZIONE) {
        List<Nodo> nonRazionali = new ArrayList<>();
        for (Nodo figlio : nodo.getFigliNodo()) {
          if (figlio.getTipoNodo() == EnumNodo.RAZIONALE) {
            fattore = fattore.moltiplicazione(figlio.evaluate());
          } else {
            nonRazionali.add(figlio);
          }
        }
        nodo =
            nonRazionali.size() == 1 ? nonRazionali.get(0) : new NodoMoltiplicazione(nonRazionali);
      }
      baseFattoreMap.merge(nodo, fattore, NumeroRazionale::addizione);
    }
    List<Nodo> fattorizzati = new ArrayList<>();
    for (Map.Entry<Nodo, NumeroRazionale> entry : baseFattoreMap.entrySet()) {
      Nodo base = entry.getKey();
      NumeroRazionale fattore = entry.getValue();
      if (!fattore.equals(NumeroRazionale.UNO)) {
        if (base.getTipoNodo() == EnumNodo.MOLTIPLICAZIONE && base.getFigliNodo().size() > 1) {
          List<Nodo> figliMoltiplicazione = new ArrayList<>();
          figliMoltiplicazione.add(new FogliaRazionale(fattore));
          figliMoltiplicazione.addAll(base.getFigliNodo());
          fattorizzati.add(new NodoMoltiplicazione(figliMoltiplicazione));
        } else {
          fattorizzati.add(
              new NodoMoltiplicazione(Arrays.asList(new FogliaRazionale(fattore), base)));
        }
      } else {
        fattorizzati.add(base);
      }
    }

    if (fattorizzati.size() == 1) {
      return fattorizzati.get(0);
    }
    return new NodoAddizione(fattorizzati);
  }

  @Override
  public Nodo visit(NodoMoltiplicazione nodoMoltiplicazione) {
    Objects.requireNonNull(
        nodoMoltiplicazione, "Il nodo moltiplicazione da semplificare non può essere null.");
    List<Nodo> figliSemplificati = new ArrayList<>();

    for (Nodo figlio : nodoMoltiplicazione.getFigliNodo()) {
      Nodo figlioSemplificato = figlio.accept(this);
      if (figlioSemplificato.getTipoNodo() == EnumNodo.MOLTIPLICAZIONE) {
        figliSemplificati.addAll(figlioSemplificato.getFigliNodo());
      } else {
        figliSemplificati.add(figlioSemplificato);
      }
    }

    List<Nodo> nodiNonRazionali = new ArrayList<>();
    NumeroRazionale prodottoTotale = NumeroRazionale.UNO;
    for (Nodo figlio : figliSemplificati) {
      if (figlio.getTipoNodo() == EnumNodo.RAZIONALE) {
        NumeroRazionale valore = figlio.evaluate();
        if (valore.equals(NumeroRazionale.ZERO)) {
          return new FogliaRazionale(NumeroRazionale.ZERO);
        }
        prodottoTotale = prodottoTotale.moltiplicazione(valore);
      } else {
        nodiNonRazionali.add(figlio);
      }
    }
    if (!prodottoTotale.equals(NumeroRazionale.UNO)) {
      nodiNonRazionali.add(new FogliaRazionale(prodottoTotale));
    }
    if (nodiNonRazionali.isEmpty()) {
      return new FogliaRazionale(NumeroRazionale.UNO);
    }

    Map<Nodo, NumeroRazionale> baseEsponenteMap = new HashMap<>();
    for (Nodo nodo : nodiNonRazionali) {
      if (nodo.getTipoNodo() == EnumNodo.POTENZA) {
        NodoPotenza potenza = (NodoPotenza) nodo;
        Nodo base = potenza.getBase();
        NumeroRazionale esponente = potenza.getEsponenteValore();
        base = base.accept(this);
        if (base.getTipoNodo() == EnumNodo.POTENZA) {
          NodoPotenza basePotenza = (NodoPotenza) base;
          esponente = esponente.moltiplicazione(basePotenza.getEsponenteValore());
          base = basePotenza.getBase();
        }
        if (!esponente.equals(NumeroRazionale.ZERO)) {
          baseEsponenteMap.merge(base, esponente, NumeroRazionale::addizione);
        }
      } else {
        baseEsponenteMap.merge(nodo, NumeroRazionale.UNO, NumeroRazionale::addizione);
      }
    }
    List<Nodo> fattorizzati = new ArrayList<>();
    for (Map.Entry<Nodo, NumeroRazionale> entry : baseEsponenteMap.entrySet()) {
      Nodo base = entry.getKey();
      NumeroRazionale esponente = entry.getValue();
      if (!esponente.equals(NumeroRazionale.UNO)) {
        fattorizzati.add(new NodoPotenza(base, new FogliaRazionale(esponente)));
      } else {
        fattorizzati.add(base);
      }
    }
    if (fattorizzati.size() == 1) {
      return fattorizzati.get(0);
    }
    return new NodoMoltiplicazione(fattorizzati);
  }
}
