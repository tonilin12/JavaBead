
# 2023/24 őszi félév – Haladó Java beadandó

---

## Feltételek

- A beadandó általános feltételei az előadás Canvasében, a Tematika oldalon olvashatók.
- A feladatban megadott neveket betűre pontosan úgy kell használni, ahogy meg vannak adva.
- A megoldás legyen a lehető legjobb minőségű.
- A Java nyelv szokásos konvencióit követni kell.
- A kód szerkezete, a változók nevei legyenek megfelelők.

## Beadás

- Az elkészített megoldást **zip** formátumba csomagolva kell feltölteni a Canvasbe.
- A zip csak a megoldás forrásfájljait tartalmazza a megfelelő könyvtárstruktúrában.
- Más fájlokat és könyvtárakat (pl. `.class`, IDE projekt) **ne** tartalmazzon.
- A megoldás a határidőn belül többször is beadható.
- Az utoljára beadott megoldás kerül értékelésre.
- **A határidő éles, aki lemarad, az kimarad!**

---

## Alapfeladat (7 pont)

Ebben a feladatban a `task.compulsory.MultiProducer.multiProducerFactory` nyilvános láthatóságú, osztályszintű adattagba olyan („külső") lambdát kell elkészíteni, ami egy másik („belső", a továbbiakban `multiProducer`, de ennek a kódban nem feltétlenül jelenik meg a neve) lambdát ad ki.

A belső lambda két paramétert vár, ezek szintén lambdák:
- Az első paraméter (`amountLambda`) `int` értékeket szolgáltat (nem `Integer`eket).
- A második (`contentLambda`) szövegeket szolgáltat.

A `multiProducer` célja, hogy a `contentLambda`tól kapott szövegeket bocsássa ki – egy megkapott szövegből egymás után annyit, amilyen számot az `amountLambda` ad.

Az `amountLambda` adhat nullát vagy negatív számot is. Ekkor el kell vetni a másik lambdából éppen megkapott szöveget, és újat kell kérni abból is, és számból is. Az újonnan kapott szám is lehet nemnegatív...

**Példa:** ha az `amountLambda` sorban a `3, 6, -1, -2, 0, 2, ...` számokat szolgáltatja, a `contentLambda` pedig sorban az `a, b, c, ...` szövegeket, akkor a `multiProducer` ismételt meghívásai sorban a következő kimeneteket adják:

```
a, a, a, b, b, b, b, b, b, f, f, ...
```

A külső lambda nem kap paramétert; feladata csupán azt biztosítani, hogy egyszerre több, egymástól független `multiProducer` jellegű lambdánk lehessen.

### Tesztelés

A `task.test.MultiProducerTest` tesztelje JUnit 5 segítségével a fentieket a következőképpen.

Legyen négy nyilvános, példányszintű lambda adattagja:
- `amountLambda123A` és `amountLambda123B` – a `0, 1, 2, 3, ...` értékeket adják ki egymástól függetlenül.
- `contentLambdaA` és `contentLambdaB` – az `a, aa, aaa, aaaa, aaaaa, ...` szövegeket adják ki.

Az állapotaikat a `value123X` és a `txtX` nevű, nyilvános, példányszintű adattagok tárolják egyelemű tömbökben, ahol `X` az `A` vagy `B` valamelyike.

A `task.test.MultiProducerTest.test` tesztelő metódus használja fel az említett lambdákat az egyetlen logikus kombinációban, és a két adódó lambdát hajtsa meg **hét lépés** erejéig, **váltakozó sorrendben** (előbb `A`, aztán `B`, aztán `A`, stb.).

Ellenőrzendő, hogy a kapott tartalmak egy-egy lambdára a következők, és a lambdák egymástól függetlenek:

```
a, aa, aa, aaa, aaa, aaa, aaaa
```

> Az alapfeladat megoldása során szabad folyamokat használni, de nem kötelező.

A feladat akkor tekinthető megoldottnak, ha a kód jó minőségű és problémák (error/warning) nélkül lefordul és fut, a leírtaknak megfelelő szerkezetű, és a teszt helyes eredményt ad.

---

## Bővített feladat (8 pont)

A `task.extension.MultiProducer2.cachedMultiProducer` nyilvános láthatóságú, osztályszintű adattag lambdája az előző feladat egy változata. Most a kimeneti szövegek egymásból alakulnak ki.

### A legbelső lambda (`multiProducer`) működése

A `multiProducer` paraméter nélküli hívásokra sorban szövegeket ad ki: `txt1, txt2, txt3, txt4, ...`

- Az első szöveg az **üres szöveg**.
- Minden következő szöveg egy `decisionLambda` döntése alapján képződik:
  - `KEEP` → az új szöveg megegyezik az előzővel.
  - `EXTEND` → az `appendTxt` szöveg hozzáfűzésével bővül az előző szöveg.

**Példa:** ha `appendTxt = "a"`, és a `decisionLambda` a `KEEP, KEEP, EXTEND, EXTEND, EXTEND, EXTEND, KEEP, KEEP, EXTEND, ...` döntéseket hozza:

```
<üres>, <üres>, <üres>, a, aa, aaa, aaaa, aaaa, aaaa, aaaaa, ...
```

> Azért három üres szöveg van kezdetben, mert a kezdeti szöveget is kiadjuk egyszer, mielőtt akár egyetlen döntést is hozna a `decisionLambda`.

### Gyorsítótár (cache)

A `decisionLambda` érzékeny, ezért gyors egymásutánban legfeljebb `decisionCount` (pozitív egész) döntést szabad meghozatni vele. Ezért felveszünk egy `cache` listát, ami kezdetben az üres szöveget tartalmazza, és **etapokban** töltjük fel.

**Példa** (`decisionCount = 3`):

1. A cache-ben kezdetben az üres szöveg az egyedüli elem.
2. Első híváskor érzékeljük, hogy majdnem kifogyott → feltöltjük:
   - `1. <üres>`, `2. <üres>`, `3. <üres>` (2 db `decisionLambda`-hívással)
   - Kiadjuk és kivesszük az első elemet.
3. Második híváskor kiadjuk a `2.` elemet.
4. Harmadik híváskor ismét kevés az elem → ismét feltöltjük.

> **Fontos:** a legutoljára elkészített elemből kiindulva kell a folytatást elkészíteni, de azt az elemet még egyszer **nem** kell betenni a cache-be!

### A `cachedMultiProducer` technikai részletei

- A **külső lambda** megkapja a `decisionCount` (pozitív egész) és a `decisionLambda` (`MultiProducer2State` értékeket kiadó, paraméter nélküli lambda) paramétereket.
- A külső lambda kódja tartalmazza a `cache` listát (kezdetben egyetlen üres szöveg).
- A külső lambda visszatérése olyan lambda, ami az `appendTxt` szöveget veszi át, és egy **paraméter nélküli lambdát** ad vissza.
- A legbelső lambda:
  1. Megvizsgálja, hogy a `cache` mérete egy-e.
  2. Ha igen, kiveszi ezt az elemet, és összesen `decisionCount` lépésen keresztül elkészíti az új cache elemeket. (**Ezt a részt kötelező egyetlen `Stream` használatával megoldani.**)
     - `KEEP` → az új elem az előző másolata.
     - `EXTEND` → az előző + `appendTxt`.
  3. Kiveszi a cache első elemét és visszatér vele.

A `MultiProducer2State` elemeit `static import` stílusban kell használni (mind az osztályban, mind a tesztelőben).

### `oneFromEach` metódus

Az osztályban legyen egy `oneFromEach` metódus, amelynek egy `T` sablonparamétere van, nincs (érték)paramétere. Visszatérése egy olyan lambda, ami paraméterként két olyan lambdát vár, amelyek `T` típusú elemeket adnak ki, és eredménye egy olyan lambda, amely **felváltott sorrendben** adja ki ezeket az elemeket.

**Példa:** egyik bemeneti lambda: `a, b, c, ...`, másik: `1, 2, 3, ...` → kimenet: `a, 1, b, 2, c, 3, ...`

### Tesztelés

#### `MultiProducerTest2.testConstant`

Paraméterezett tesztelővel ellenőrzi, hogy ha a `cachedMultiProducer` folyton `KEEP` értéket visszaadó `decisionLambda`t kap, az így kapott lambda mindig üres szöveggel tér vissza.

Paraméterek:
- átadandó szöveg (tetszőleges, akár üres)
- `decisionCount` (legyen kis és nagy érték is)
- hányadik hívás után vizsgáljuk

Ilyen paraméterezésből **három-négy** teszt fusson le.

#### `setup` metódus

Minden teszteset előtt állítson be két adattagot:

- `flipDecisionLambda`: sorban `EXTEND, KEEP, EXTEND, KEEP, ...` (végtelenül)
- `bunchDecisionLambda`: sorban `EXTEND, KEEP, EXTEND, KEEP, KEEP, EXTEND, KEEP, KEEP, KEEP, EXTEND, ...` (mindig eggyel több `KEEP` az `EXTEND`ek között)

#### `flip20` és `bunch20`

Tesztelje, hogy az első 20 kijövő érték megfelelő-e a fenti lambdáknál.

#### `oneOfAB`

Próbálja ki a `oneFromEach` metódust csupa `"a"` és csupa `"b"` értékeket adó lambdákkal. Az első hat adódó értéket egy `Stream` segítségével gyűjtse listába. Elvárt tartalom:

```
"a", "b", "a", "b", "a", "b"
```

#### `cachedMultiProducerTest`

Paraméterezett teszt (`decisionCount` értékei pl. `1, 3, 10`):
- `flipDecisionLambda` + `"a"` → egy lambda
- `bunchDecisionLambda` + `"b"` → egy lambda
- A `oneFromEach` segítségével hajtja ezeket felváltva.
- `Stream` segítségével veszi az első 20-20 elemet (összesen 40-et), listába gyűjtve.

Elvárt lista (az első kettő üres, majd):

| # | Érték |
|---|-------|
| 1–2 | `""`, `""` |
| 3–4 | `a`, `b` |
| 5–6 | `a`, `b` |
| 7–8 | `aa`, `bb` |
| 9–10 | `aa`, `bb` |
| 11–12 | `aaa`, `bb` |
| 13–14 | `aaa`, `bbb` |
| 15–16 | `aaaa`, `bbb` |
| 17–18 | `aaaa`, `bbb` |
| 19–20 | `aaaaa`, `bbb` |
| 21–22 | `aaaaa`, `bbbb` |
| 23–24 | `aaaaaa`, `bbbb` |
| 25–26 | `aaaaaa`, `bbbb` |
| 27–28 | `aaaaaaa`, `bbbb` |
| 29–30 | `aaaaaaa`, `bbbb` |
| 31–32 | `aaaaaaaa`, `bbbbb` |
| 33–34 | `aaaaaaaa`, `bbbbb` |
| 35–36 | `aaaaaaaaa`, `bbbbb` |
| 37–38 | `aaaaaaaaa`, `bbbbb` |
| 39–40 | `aaaaaaaaaa`, `bbbbb` |
