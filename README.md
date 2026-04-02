 nullát,
- negatív számot is.

Ilyenkor:

- el kell vetni a másik lambdából éppen megkapott szöveget,
- új számot kell kérni,
- és új szöveget is kell kérni.

Az újonnan kapott szám szintén lehet nempozitív, tehát ezt addig kell ismételni, amíg pozitív érték nem érkezik.

### Példa

Ha az `amountLambda` sorban a következő értékeket adja:

```text
3, 6, -1, -2, 0, 2, ...

és a contentLambda sorban ezeket:

a, b, c, d, e, f, ...

akkor a multiProducer ismételt meghívásai sorban a következő kimeneteket adják:

a, a, a, b, b, b, b, b, b, f, f, ...
A külső lambda szerepe

A külső lambda nem kap paramétert.

A feladata csupán azt biztosítani, hogy egyszerre több, egymástól független multiProducer jellegű lambdánk lehessen.

Tesztelés az alapfeladathoz

A task.test.MultiProducerTest tesztelje JUnit 5 segítségével a fentieket a következőképpen.

Nyilvános, példányszintű lambda adattagok

Legyen négy nyilvános, példányszintű lambda adattag:

amountLambda123A
amountLambda123B
contentLambdaA
contentLambdaB

Ezek közül:

amountLambda123A és amountLambda123B a következő értékeket adják ki egymástól függetlenül:
0, 1, 2, 3, ...
contentLambdaA és contentLambdaB pedig a következő szövegeket adják ki:
a, aa, aaa, aaaa, aaaaa, ...
Állapot tárolása

Az állapotukat a következő nyilvános, példányszintű adattagok tárolják egyelemű tömbökben:

value123A
value123B
txtA
txtB

ahol X az egyik A és B közül.

A tesztelő metódus

A task.test.MultiProducerTest.test metódus:

használja fel az említett lambdákat az egyetlen logikus kombinációban,
a két adódó lambdát hajtsa meg hét lépés erejéig,
váltakozó sorrendben.

Tehát:

egy lépést tesz meg a kapott A lambda,
aztán egyet a B,
aztán megint egyet az A,
és így tovább.
Ellenőrzendő kimenet

A kapott tartalmak egy-egy lambdára a következők legyenek:

a, aa, aa, aaa, aaa, aaa, aaaa

Azt is ellenőrizni kell, hogy a lambdák függetlenek, egymást nem zavarják.

Megjegyzés

Az alapfeladat megoldása során szabad folyamokat használni, de nem kötelező.

A feladat akkor tekinthető megoldottnak, ha:

a kód jó minőségű,
problémák (error / warning) nélkül lefordul és fut,
a leírtaknak megfelelő szerkezetű,
és a teszt helyes eredményt ad.
Bővített feladat (8 pont)

A task.extension.MultiProducer2.cachedMultiProducer nyilvános láthatóságú, osztályszintű adattag lambdája az előző feladat egy változata lesz.

Az előző feladatban a contentLambda egymástól független szövegeket adott át nekünk, és azokból adtunk ki néhány példányt. Most viszont a kimeneti szövegek egymásból alakulnak ki.

A könnyebb érthetőség kedvéért fordított sorrendben, a végeredmény szerkezetét ismertetve lássuk, mi történik.

A legbelső lambda működése

A legbelső lambdánk (multiProducer) az őt érő paraméter nélküli hívásokra sorban szövegeket ad ki:

txt1, txt2, txt3, txt4, txt5, txt6, txt7, ...

Az első ezek közül az üres szöveg.

A következő úgy adódik, hogy egy decisionLambda lambda eldönti, hogy a következő kiadandó szöveg:

meg kell-e egyezzen az előzővel, vagy
bővülnie kell.

A task.extension.MultiProducer2State felsorolási típus értékei:

KEEP → a következő elem megegyezik az előzővel,
EXTEND → a következő elem bővül.

Bővüléskor az appendTxt szöveget fűzzük a korábbihoz.

Példa

Ha:

appendTxt egyetlen a betűt tartalmaz,
a decisionLambda pedig a következő döntéseket hozza:
KEEP, KEEP, EXTEND, EXTEND, EXTEND, EXTEND, KEEP, KEEP, EXTEND, ...

akkor a kijövő elemek sorban:

"", "", "", "a", "aa", "aaa", "aaaa", "aaaa", "aaaa", "aaaaa", ...

Azért három szöveg üres kezdetben, mert a kezdeti szöveget is kiadjuk egyszer, még mielőtt akár egyetlen döntést is hozna a decisionLambda.

Gyorsítótár (cache)

Bonyodalom, hogy a decisionLambda eszközről ismert, hogy érzékeny, és gyors egymásutánban legfeljebb decisionCount döntést szabad meghozatni vele.

Ezért felveszünk egy gyorsítótárat (cache), ami egy lista, és kezdetben az üres szöveget tartalmazza.

Amikor a gyorsítótár egy elemet tartalmaz, akkor elkészítjük bele a következő decisionCount elemet.

Tehát etapokban dolgozunk:

feltöltjük a gyorsítótárat decisionCount elemmel,
majd lefogyasztjuk.
Példa decisionCount = 3 esetén

A korábbi példa így működik.

i.

A gyorsítótárban az üres szöveg az egyedüli elem.

ii.

Amikor a multiProducer lambdát először hívják meg, érzékeljük, hogy majdnem kifogyott.

a.

Feltöltjük ezekkel:

1. "", 2. "", 3. ""

A számok csak az olvasást könnyítik, nem jelennek meg a kódban.

A feltöltéshez kétszer meg kellett hívni a decisionLambda-t.

b.

Kiadjuk az első elemet. Ezt természetesen ki is vesszük a gyorsítótárból.

iii.

Amikor a multiProducer lambdát másodszor hívják meg, kiadjuk a 2. elemet.

iv.

Újabb hívásnál érzékeljük, hogy ismét kevés az elem, és ismét feltöltjük a gyorsítótárat.

Fontos

A legutoljára elkészített elemből kiindulva kell a folytatást elkészíteni, de azt az elemet még egyszer nem kell betenni a gyorsítótárba.

A cachedMultiProducer technikai részletei

A külső lambda megkapja:

a decisionCount paramétert, amely egész és feltételezhetően pozitív,
a decisionLambda paramétert.

Utóbbi MultiProducer2State értékeket kiadó, paraméter nélküli lambda.

A külső lambda kódja tartalmazza a szövegeket tároló cache listát. Ez kezdetben egyetlen elemmel, egy üres szöveggel van feltöltve.

A külső lambda visszatérése olyan lambda, ami:

az appendTxt szöveget veszi át,
és egy paraméter nélküli lambdát ad vissza.

Ez a legbelső lambda megvizsgálja, hogy a cache mérete egy-e.

Ha igen, akkor:

veszi ezt az elemet,
és összesen decisionCount lépésen keresztül elkészíti a cache új elemeit belőle.

Az új elem így jön ki a megelőzőből:

meghívjuk a decisionLambda-t,
ha az eredmény KEEP, akkor az új elem tartalma egyszerűen a megelőzőé,
ha az eredmény EXTEND, akkor a megelőző végéhez hozzáfűzzük az appendTxt szöveget.
Kötelező megkötés

Ezt a részét a feladatnak kötelező egyetlen Stream használatával megoldani.

Más eszközökkel elkészített megoldás akkor sem ér itt pontot, ha egyébként teljesen helyes.

Végül a legbelső lambda:

kiveszi a cache első elemét,
és visszatér vele.

Itt is és a tesztelőben is static import stílusban használjuk a MultiProducer2State elemeit.

oneFromEach metódus

Az osztályban legyen továbbá egy oneFromEach metódus, amelynek egy T sablonparamétere van, és nincs értékparamétere.

A visszatérése egy olyan lambda, ami paraméterként két olyan lambdát vár, amelyek T típusú elemeket adnak ki, és eredménye egy olyan lambda, amely felváltott sorrendben adja ki ezeket az elemeket.

Példa

Ha az egyik bemeneti lambda:

a, b, c, ...

a másik pedig:

1, 2, 3, ...

akkor az eredmény lambda sorban:

a, 1, b, 2, c, 3, ...
Tesztelés a bővített feladathoz
task.test.MultiProducerTest2.testConstant

Ez a metódus paraméterezett tesztelővel próbálja ki, hogy ha a cachedMultiProducer olyan decisionLambda-t kap, ami folyton a KEEP értéket állítja elő, akkor az így kapott lambda mindig az üres szöveggel tér vissza.

Paraméterek
a kapott lambdának átadandó szöveg, amely tetszőleges lehet, akár üres is,
decisionCount, ahol legyen kis és nagy érték is,
hanyadik hívás után vizsgáljuk meg, hogy üres szöveg-e az eredmény.

Ilyen jellegű paraméterezésből három-négy teszt fusson le.

setup

A tesztelő osztály setup metódusa minden teszteset lefutása előtt állítson be két adattagot.

flipDecisionLambda

Sorban a következő értékeket állítja elő a végtelenségig:

EXTEND, KEEP, EXTEND, KEEP, ...
bunchDecisionLambda

Sorban a következő értékeket állítja elő:

EXTEND, KEEP, EXTEND, KEEP, KEEP, EXTEND, KEEP, KEEP, KEEP, EXTEND, ...

Tehát mindig eggyel több KEEP kerül az EXTEND értékek közé.

flip20 és bunch20

A tesztelő osztály flip20 és bunch20 metódusa tesztelje a lambdákról, hogy az első 20 kijövő érték megfelelő-e.

task.test.MultiProducerTest2.oneOfAB

Ez a metódus próbálja ki a oneFromEach metódust olyan lambdákkal, amelyek:

csupa "a" értéket adnak,
illetve csupa "b" értéket adnak.

Az első hat adódó értéket a tesztelő metódus gyűjtse össze egy Stream segítségével egy listába.

Ennek elvárt tartalma:

"a", "b", "a", "b", "a", "b"
task.test.MultiProducerTest2.cachedMultiProducerTest

Ez a metódus próbálja ki a cachedMultiProducer-t:

készítsen a flipDecisionLambda felhasználásával egy lambdát,
készítsen a bunchDecisionLambda felhasználásával is egy lambdát.

Az elsőnek az "a" szöveget adjuk át, a másodiknak a "b" szöveget.

A decisionCount minden esetben a tesztelő metódus paraméteréből jöjjön, ahol adjunk meg néhány értéket, például:

1, 3, 10

A tesztelő a oneFromEach segítségével hajtsa ezeket meg felváltva, és egy Stream segítségével vegyük az első 20-20 elemet, összesen negyvenet, amelyeket gyűjtsünk össze egy listába.

Az elvárt elemek közül az első kettő üres, majd a továbbiak:

a,          b,
a,          b,
aa,         bb,
aa,         bb,
aaa,        bb,
aaa,        bbb,
aaaa,       bbb,
aaaa,       bbb,
aaaaa,      bbb,
aaaaa,      bbbb,
aaaaaa,     bbbb,
aaaaaa,     bbbb,
aaaaaaa,    bbbb,
aaaaaaa,    bbbb,
aaaaaaaa,   bbbbb,
aaaaaaaa,   bbbbb,
aaaaaaaaa,  bbbbb,
aaaaaaaaa,  bbbbb,
aaaaaaaaaa, bbbbb
Összefoglalás

A beadandó két részből áll:

Alapfeladat
multiProducerFactory
független állapotú lambdák
amountLambda és contentLambda együttműködése
Bővített feladat
cachedMultiProducer
KEEP és EXTEND alapú szövegépítés
gyorsítótár használata
kötelező egyetlen Stream használata
generikus oneFromEach metódus

A megoldás akkor megfelelő, ha:

a szerkezet pontosan követi a kiírást,
a megadott nevek helyesen szerepelnek,
a kód jó minőségű,
hibamentesen fordul,
és a tesztek helyes eredményt adna
