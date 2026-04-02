# Haladó Java – MultiProducer beadandó (2023/24 ősz)

Ez a repository a **Haladó Java** tárgyhoz tartozó **MultiProducer beadandó** megoldását tartalmazza a **2023/24 őszi félévből**.

A feladat két részből áll:

- **Alapfeladat** – `MultiProducer`
- **Bővített feladat** – `MultiProducer2`

---

## 📌 Feltételek

A beadandó részletes feltételei a **Canvas Tematika** oldalon találhatók.

A megoldás elkészítése során az alábbiakat kell betartani:

- a megadott neveket **pontosan** kell használni,
- követni kell a **Java konvenciókat**,
- a kód legyen:
  - jól strukturált,
  - olvasható,
  - `warning`- és `error`-mentes.

---

## 📦 Beadás

A beadandót **ZIP fájlban** kell feltölteni.

A ZIP fájl:

- csak a **forráskódokat** tartalmazhatja,
- a **helyes könyvtárstruktúrával** együtt,
- **nem** tartalmazhat:
  - `.class` fájlokat,
  - IDE fájlokat,
  - egyéb generált állományokat.

További szabályok:

- többszöri beadás lehetséges,
- mindig az **utolsó beadás** számít,
- a határidő után **nincs beadás**.

---

# 🧩 Alapfeladat (7 pont)

## 🎯 Feladat

A `task.compulsory.MultiProducer.multiProducerFactory` egy **külső lambda**, amely:

- nem kap paramétert,
- visszaad egy **belső lambdát** (`multiProducer`).

---

## 🔧 A belső lambda működése

A belső lambda két paramétert kap:

- `amountLambda` → `int` értékek
- `contentLambda` → `String` értékek

### Működés

1. Lekér egy számot az `amountLambda`-tól.
2. Ha a szám **pozitív**:
   - lekér egy szöveget a `contentLambda`-tól,
   - azt a szöveget annyiszor adja vissza egymás után, mint a kapott szám.
3. Ha a szám **nem pozitív**:
   - a hozzá tartozó szöveget eldobja,
   - új számmal és új szöveggel próbálkozik.

---

## 🧠 Példa

```text
amount:   3, 6, -1, -2, 0, 2, ...
content:  a, b, c, d, e, f, ...
output:   a, a, a, b, b, b, b, b, b, f, f, ...
