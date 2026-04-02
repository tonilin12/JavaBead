# 2023/24 őszi félév – Haladó Java beadandó

**Feladat:** `task1-hu-23-24-1-multiproducer`  
**Dátum:** 2023. 12. 10. 22:29

---

## Feltételek

A beadandó általános feltételei az előadás Canvasében, a Tematika oldalon olvashatók.

- A feladatban megadott neveket betűre pontosan úgy kell használni, ahogy meg vannak adva.
- A megoldás legyen a lehető legjobb minőségű.
- A Java nyelv szokásos konvencióit követni kell.
- A kód szerkezete, a változók nevei legyenek megfelelők.

### Beadás

Az elkészített megoldást zip formátumba csomagolva kell feltölteni a Canvasbe.

- A zip csak a megoldás forrásfájljait tartalmazza a megfelelő könyvtárstruktúrában.
- Más fájlokat és könyvtárakat (pl. `.class`, IDE projekt) ne tartalmazzon.
- A megoldás a határidőn belül többször is beadható.
- Az utoljára beadott megoldás kerül értékelésre.
- A határidő éles, aki lemarad, az kimarad.

---

## Alapfeladat (7 pont)

Ebben a feladatban a `task.compulsory.MultiProducer.multiProducerFactory` nyilvános láthatóságú, osztályszintű adattagba olyan **külső lambdát** kell elkészíteni, ami egy másik **belső lambdát** ad vissza.

A belső lambda két paramétert vár, ezek szintén lambdák:

- az első paraméter (`amountLambda`) `int` értékeket szolgáltat,
- a második (`contentLambda`) pedig szövegeket.

A `multiProducer` célja, hogy a `contentLambda`-tól kapott szövegeket bocsássa ki úgy, hogy egy adott szöveget annyiszor adjon vissza egymás után, amennyi számot az `amountLambda` ad.

### Fontos szabály

Az `amountLambda` adhat `0`-t vagy negatív számot is.

Ilyenkor:

- a `contentLambda`-tól éppen kapott szöveget el kell vetni,
- új számot kell kérni az `amountLambda`-tól,
- új szöveget kell kérni a `contentLambda`-tól.

Ez addig ismétlődik, amíg pozitív számot nem kapunk.

### Példa

Ha az `amountLambda` sorban ezeket adja:

`3, 6, -1, -2, 0, 2, ...`

és a `contentLambda` sorban ezt adja:

`a, b, c, ...`

akkor a `multiProducer` ismételt meghívásai ezt adják:

`a, a, a, b, b, b, b, b, b, f, f, ...`

### A külső lambda szerepe

A külső lambda nem kap paramétert. Feladata csak az, hogy egyszerre több, egymástól független `multiProducer` jellegű lambdát lehessen létrehozni.

### Tesztelés

A `task.test.MultiProducerTest` osztály tesztelje JUnit 5 segítségével a fentieket.

Legyen négy nyilvános, példányszintű lambda adattagja:

- `amountLambda123A`
- `amountLambda123B`
- `contentLambdaA`
- `contentLambdaB`

Ezek közül:

- `amountLambda123A` és `amountLambda123B` a `0, 1, 2, 3, ...` értékeket adják egymástól függetlenül,
- `contentLambdaA` és `contentLambdaB` az `a, aa, aaa, aaaa, aaaaa, ...` szövegeket adják.

Az állapotukat az alábbi nyilvános, példányszintű adattagok tárolják egyelemű tömbökben:

- `value123A`, `value123B`
- `txtA`, `txtB`

A `task.test.MultiProducerTest.test` metódus használja fel ezeket a lambdákat az egyetlen logikus kombinációban, és a két adódó lambdát hajtsa meg **7 lépésen keresztül, váltakozó sorrendben**:

- egy lépés az `A` lambdával,
- egy lépés a `B` lambdával,
- majd ismét `A`, stb.

### Elvárt eredmény

Mindkét lambda esetén az eredmények legyenek:

`a, aa, aa, aaa, aaa, aaa, aaaa`

A lambdáknak egymástól függetlenül kell működniük.

### Megjegyzés

Az alapfeladat megoldása során szabad folyamokat használni, de nem kötelező.

A feladat akkor tekinthető megoldottnak, ha:

- a kód jó minőségű,
- problémák (`error` / `warning`) nélkül lefordul és fut,
- a leírt szerkezetnek megfelel,
- a teszt helyes eredményt ad.

---

## Bővített feladat (8 pont)

A `task.extension.MultiProducer2.cachedMultiProducer` nyilvános láthatóságú, osztályszintű adattag lambdája az előző feladat egy változata lesz.

Ebben a változatban a kimeneti szövegek egymásból alakulnak ki.

### A legbelső lambda működése

A legbelső lambda (`multiProducer`) paraméter nélküli hívásokra sorban szövegeket ad ki:

`txt1, txt2, txt3, txt4, ...`

- Az első ezek közül az **üres szöveg**.
- A következő érték attól függ, hogy egy `decisionLambda` mit dönt.

A `decisionLambda` kétféle értéket adhat vissza a `task.extension.MultiProducer2State` felsorolási típusból:

- `KEEP`
- `EXTEND`

Jelentésük:

- `KEEP`: a következő kiadandó szöveg megegyezik az előzővel,
- `EXTEND`: a következő kiadandó szöveg az előző szöveg + `appendTxt`.

### Példa

Ha `appendTxt = "a"`, és a `decisionLambda` sorban ezt adja:

`KEEP, KEEP, EXTEND, EXTEND, EXTEND, EXTEND, KEEP, KEEP, EXTEND, ...`

akkor a kimenet:

`<üres>, <üres>, <üres>, a, aa, aaa, aaaa, aaaa, aaaa, aaaaa, ...`

Azért van kezdetben három üres szöveg, mert a kezdeti szöveget egyszer kiadjuk még azelőtt, hogy a `decisionLambda` egyáltalán döntene.

---

## Gyorsítótár használata

A `decisionLambda` érzékeny, ezért gyors egymásutánban legfeljebb `decisionCount` döntést szabad vele meghozatni.

Ezért egy gyorsítótárat (`cache`) kell használni, ami egy lista, és kezdetben az **üres szöveget** tartalmazza.

Amikor a gyorsítótár mérete `1`, akkor elő kell állítani a következő `decisionCount` elemet.

Vagyis etapokban dolgozunk:

1. feltöltjük a gyorsítótárat `decisionCount` elemmel,
2. majd lefogyasztjuk.

### Példa `decisionCount = 3` esetén

1. A gyorsítótár kezdetben: `["]`.
2. Amikor a `multiProducer`-t először meghívják, észleljük, hogy majdnem kifogyott.
   - Feltöltjük a cache-t ezekkel:
     1. `<üres>`
     2. `<üres>`
     3. `<üres>`
   - Ehhez kétszer kellett meghívni a `decisionLambda`-t.
   - Ezután kiadjuk az első elemet, és eltávolítjuk a cache-ből.
3. Második hívásnál kiadjuk a 2. elemet.
4. Újabb hívásnál ismét kevés az elem, újratöltjük a cache-t.

### Fontos szabály

A folytatást mindig a **legutoljára elkészített elem** alapján kell előállítani, de ezt az elemet még egyszer **nem kell** betenni a gyorsítótárba.

---

## A `cachedMultiProducer` szerkezete

A külső lambda paraméterei:

- `decisionCount` – pozitív egész,
- `decisionLambda` – paraméter nélküli lambda, amely `MultiProducer2State` értékeket ad.

A külső lambda:

- tartalmazza a szövegeket tároló `cache` listát,
- ez kezdetben egyetlen elemmel, az üres szöveggel van feltöltve,
- visszaad egy olyan lambdát, amely `appendTxt` szöveget kap,
- ez pedig visszaad egy paraméter nélküli lambdát.

A legbelső lambda működése:

- megnézi, hogy a `cache` mérete `1`-e,
- ha igen, akkor ebből az egy elemből kiindulva összesen `decisionCount` lépésen keresztül elkészíti a `cache` új elemeit,
- az új elem előállítása:
  - ha a `decisionLambda` eredménye `KEEP`, akkor az új elem ugyanaz, mint az előző,
  - ha `EXTEND`, akkor az új elem az előző + `appendTxt`.

### Kötelező megkötés

Ezt a részt **kötelező egyetlen Stream használatával megoldani**.

Más megoldás itt akkor sem ér pontot, ha egyébként helyes.

Végül a legbelső lambda:

- kiveszi a `cache` első elemét,
- és visszatér vele.

Itt is és a tesztelőben is **static import** stílusban kell használni a `MultiProducer2State` elemeit.

---

## `oneFromEach` metódus

Az osztályban legyen továbbá egy `oneFromEach` metódus, amelynek:

- egy `T` sablonparamétere van,
- nincs értékparamétere.

A visszatérési értéke egy olyan lambda, amely paraméterként két olyan lambdát vár, amelyek `T` típusú elemeket adnak ki, és eredménye egy olyan lambda, amely **felváltva** adja ki ezeknek az elemeit.

### Példa

Ha az egyik bemeneti lambda ezt adja:

`a, b, c, ...`

és a másik ezt:

`1, 2, 3, ...`

akkor az eredmény lambda:

`a, 1, b, 2, c, 3, ...`

---

## Tesztelés a bővített részhez

### `task.test.MultiProducerTest2.testConstant`

Paraméterezett teszttel vizsgálja, hogy ha a `cachedMultiProducer` olyan `decisionLambda`-t kap, amely mindig `KEEP` értéket ad, akkor az így kapott lambda mindig az üres szöveggel tér vissza.

A paraméterek:

- a kapott lambdának átadandó szöveg,
- `decisionCount` (legyen kis és nagy érték is),
- hanyadik hívás után vizsgáljuk meg, hogy üres szöveg-e az eredmény.

Három-négy ilyen paraméterezett teszt fusson le.

### `setup` metódus

Minden teszteset előtt állítson be két adattagot:

- `flipDecisionLambda`
- `bunchDecisionLambda`

#### `flipDecisionLambda`

Sorban ezt adja:

`EXTEND, KEEP, EXTEND, KEEP, EXTEND, KEEP, ...`

#### `bunchDecisionLambda`

Sorban ezt adja:

`EXTEND, KEEP, EXTEND, KEEP, KEEP, EXTEND, KEEP, KEEP, KEEP, EXTEND, ...`

Tehát mindig eggyel több `KEEP` kerül az `EXTEND` értékek közé.

### `flip20` és `bunch20`

Tesztelje, hogy az első 20 kijövő érték megfelelő-e.

### `task.test.MultiProducerTest2.oneOfAB`

Próbálja ki a `oneFromEach` metódust olyan lambdákkal, amelyek:

- csupa `"a"` értéket,
- illetve csupa `"b"` értéket adnak.

Az első 6 eredményt egy **Stream** segítségével gyűjtse listába.

Elvárt eredmény:

`["a", "b", "a", "b", "a", "b"]`

### `task.test.MultiProducerTest2.cachedMultiProducerTest`

Tesztelje a `cachedMultiProducer`-t:

- készítsen a `flipDecisionLambda` felhasználásával egy lambdát,
- készítsen a `bunchDecisionLambda` felhasználásával egy másikat,
- az elsőnek az `"a"`, a másodiknak a `"b"` szöveget adjuk át,
- a `decisionCount` a teszt paraméteréből jöjjön,
- adjunk meg több értéket is, például: `1`, `3`, `10`.

A teszt:

- a `oneFromEach` segítségével hajtsa meg őket felváltva,
- egy **Stream** segítségével vegye az első 40 elemet,
- és gyűjtse őket listába.

Az elvárt lista eleje:

```text
"", "",
"a", "b",
"a", "b",
"aa", "bb",
"aa", "bb",
"aaa", "bb",
"aaa", "bbb",
"aaaa", "bbb",
"aaaa", "bbb",
"aaaaa", "bbb",
"aaaaa", "bbbb",
"aaaaaa", "bbbb",
"aaaaaa", "bbbb",
"aaaaaaa", "bbbb",
"aaaaaaa", "bbbb",
"aaaaaaaa", "bbbbb",
"aaaaaaaa", "bbbbb",
"aaaaaaaaa", "bbbbb",
"aaaaaaaaa", "bbbbb",
"aaaaaaaaaa", "bbbbb"
```

---

## Rövid összefoglalás

A beadandó lényege:

1. lambdákból lambdát előállítani,
2. állapotot lambdákon belül kezelni,
3. több független példányt létrehozni,
4. JUnit 5 tesztekkel ellenőrizni a működést,
5. a bővített részben cache-es működést és kötelező `Stream`-használatot megvalósítani.
