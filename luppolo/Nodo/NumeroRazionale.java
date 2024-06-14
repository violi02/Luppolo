package luppolo.Nodo;

import java.util.Objects;

/**
 * OVERVIEW: La classe immutabile rappresenta un oggetto che identifica un tipico numero razionale,
 * appartennete all'insieme numerico Q, definito da un numeratore e da un denominatore.
 */
public class NumeroRazionale {

  /**
   * AF: Il numero razionale viene rappresentato da due long, numeratore e denominatore. Un esempio
   * è {numeratore/denominatore} dove numeratore e denominatore sono due long. IR: denominatore != 0
   */

  /** Il numeratore del numero razionale */
  private long numeratore;

  /** Il denominatore del numero razionale */
  private long denominatore;

  /** La fazione 1. */
  public static final NumeroRazionale UNO = new NumeroRazionale(1, 1);

  /** La frazione 0 */
  public static final NumeroRazionale ZERO = new NumeroRazionale(0, 1);

  /**
   * Costruisce un numero razionale a partire da due long che rappresentano rispettivamente il
   * numeratore e il denominatore
   *
   * @param num numeratore
   * @param den denominatore
   * @throws IllegalArgumentException se il denominatore è = 0
   */
  public NumeroRazionale(long num, long den) {
    if (den == 0) throw new IllegalArgumentException("Il denominatore non può essere zero");
    if (den < 0) {
      this.numeratore = -num;
      this.denominatore = -den;
    } else {
      this.numeratore = num;
      this.denominatore = den;
    }
    semplifica();
  }

  /**
   * Metodo mutazionale che una volta calcolato l'mcd tra il this.numeratore e il this.denominatore
   * li divide per l'mcd stesso semplificandoli.
   */
  private void semplifica() {
    int mcd = mcd(Math.abs((int) numeratore), (int) denominatore);
    numeratore /= mcd;
    denominatore /= mcd;
  }

  /**
   * Metodo che restituisce il denominatore di this
   *
   * @return il denominatore del numero razionale
   */
  public long getDenominatore() {
    return denominatore;
  }

  /**
   * Metodo che restituisce il numeratore di this
   *
   * @return il numeratore del numero razionale
   */
  public long getNumeratore() {
    return numeratore;
  }

  /**
   * Restitusice il massimo comun divisore dati due interi
   *
   * @param a primo numero
   * @param b secondo numero
   * @return a se b = 0 altrimenti mcd(a, b)
   */
  public int mcd(int a, int b) {
    return b == 0 ? a : mcd(b, a % b);
  }

  /**
   * Metodo che restituisce un numero razionale che è dtao dalla somma di this e un altro numero
   * razionale
   *
   * @param altro numero razionale da sommare
   * @return la somma dei due numeri
   * @throws NullPointerException se il numero da sommare è null
   */
  public NumeroRazionale addizione(NumeroRazionale altro) {
    Objects.requireNonNull(altro, "Il numero da sommare non può essere null");
    long nuovoNumeratore =
        (this.numeratore * altro.denominatore) + (altro.numeratore * this.denominatore);
    long nuovoDenominatore = this.denominatore * altro.denominatore;
    return new NumeroRazionale(nuovoNumeratore, nuovoDenominatore);
  }

  /**
   * Metodo che restituisce un numero razionale che è dato dal prodotto di this e un altro numero
   * razionale
   *
   * @param altro numero razionale da moltiplicare
   * @return il prodotto dei due numeri
   * @throws NullPointerException se il numero da moltiplicare è null
   */
  public NumeroRazionale moltiplicazione(NumeroRazionale altro) {
    Objects.requireNonNull(altro, "Il numero da moltiplicare non può essere null");
    return new NumeroRazionale(
        this.numeratore * altro.numeratore, this.denominatore * altro.denominatore);
  }

  /**
   * Metodo che restituisce un numero razionale che è daato dalla sottrazione di this e un altro
   * numero razionale
   *
   * @param altro numero razionale da sottrarre a this
   * @return la sottrazione dei due numeri
   * @throws NullPointerException se il numero da sottrarre è null
   */
  public NumeroRazionale sottrazione(NumeroRazionale altro) {
    Objects.requireNonNull(altro, "Il numero da moltiplicare non può essere null");
    long nuovoNumeratore =
        this.numeratore * altro.denominatore - altro.numeratore * this.denominatore;
    long nuovoDenominatore = this.denominatore * altro.denominatore;
    return new NumeroRazionale(nuovoNumeratore, nuovoDenominatore);
  }

  /**
   * Metodo che restituisce il nodo risultante dall'elevamento a potenza di due numeri razionali
   *
   * @param esponente numero razionale
   * @return un nodo
   * @throws NullPointerException se il numero esponente è null
   */
  public Nodo potenzaReturnNodo(NumeroRazionale esponente) {
    Objects.requireNonNull(esponente, "Il numero non può essere null");
    boolean isNegativeBase = false;
    boolean isNegativeExponent = false;

    long numeratore = this.numeratore;
    long denominatore = this.denominatore;
    long expNumeratore = esponente.getNumeratore();
    long expDenominatore = esponente.getDenominatore();

    if (numeratore < 0) {
      isNegativeBase = true;
      numeratore = -numeratore;
    }

    if (isNegativeBase && (expDenominatore % 2 == 0)) {
      if (expNumeratore < 0) {
        return new NodoPotenza(
            new FogliaRazionale(new NumeroRazionale(-denominatore, numeratore)),
            new FogliaRazionale(new NumeroRazionale(-expNumeratore, expDenominatore)));
      } else {
        return new NodoPotenza(
            new FogliaRazionale(new NumeroRazionale(-numeratore, denominatore)),
            new FogliaRazionale(new NumeroRazionale(expNumeratore, expDenominatore)));
      }
    }

    if (expNumeratore < 0) {
      isNegativeExponent = true;
      expNumeratore = -expNumeratore;
    }

    if (isNegativeExponent) {
      long temp = numeratore;
      numeratore = denominatore;
      denominatore = temp;
    }
    double radiceNumeratore = Math.pow(numeratore, 1.0 / expDenominatore);
    double radiceDenominatore = Math.pow(denominatore, 1.0 / expDenominatore);
    double potenzaNumeratore = Math.pow(radiceNumeratore, expNumeratore);
    double potenzaDenominatore = Math.pow(radiceDenominatore, expNumeratore);

    if (isNegativeBase) {
      numeratore = -numeratore;
    }

    if (isNegativeBase && expNumeratore % 2 != 0) {
      potenzaNumeratore = -potenzaNumeratore;
    }

    if (Math.floor(potenzaNumeratore) == potenzaNumeratore
        && Math.floor(potenzaDenominatore) == potenzaDenominatore) {
      return new FogliaRazionale(
          new NumeroRazionale((long) potenzaNumeratore, (long) potenzaDenominatore));
    } else {
      if (isNegativeExponent) {
        return new NodoPotenza(
            new FogliaRazionale(new NumeroRazionale(numeratore, denominatore)),
            new FogliaRazionale(new NumeroRazionale(expNumeratore, expDenominatore)));
      }
      return new NodoPotenza(
          new FogliaRazionale(new NumeroRazionale(numeratore, denominatore)),
          new FogliaRazionale(esponente));
    }
  }

  /**
   * Metodo che confronta due numeri razionali
   *
   * @param n1 numero razionale
   * @return un intero che stabilisce quale dei due numeir è il maggiore
   */
  public int compareTo(NumeroRazionale n1) {
    long sx = this.numeratore * n1.denominatore;
    long dx = n1.numeratore * this.denominatore;
    return Integer.compare((int) sx, (int) dx);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (!(obj instanceof NumeroRazionale other)) return false;
    return denominatore == other.denominatore && numeratore == other.numeratore;
  }

  @Override
  public int hashCode() {
    return Objects.hash(numeratore, denominatore);
  }

  @Override
  public String toString() {
    return denominatore == 1 ? String.valueOf(numeratore) : numeratore + "/" + denominatore;
  }
}
