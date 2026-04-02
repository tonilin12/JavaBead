Haladó Java – MultiProducer beadandó (2023/24 ősz)
📌 Feltételek
A beadandó részletes feltételei a Canvas Tematika oldalon találhatók
A megadott neveket pontosan kell használni
Követni kell a Java konvenciókat
A kód legyen:
jól strukturált
olvasható
warning és error mentes
📦 Beadás
ZIP fájlban
Csak forráskódok
Helyes könyvtárstruktúrával
NEM tartalmazhat:
.class
IDE fájlok
Többszöri beadás lehetséges
Csak az utolsó számít
A határidő után nincs beadás
🧩 Alapfeladat (7 pont)
🎯 Feladat

A task.compulsory.MultiProducer.multiProducerFactory egy külső lambda, amely:

nem kap paramétert
visszaad egy belső lambdát (multiProducer)
🔧 Belső lambda működése

Két paramétert kap:

amountLambda → int értékek
contentLambda → String értékek
Működés:
Lekér egy számot az amountLambda-tól
Ha pozitív:
lekér egy szöveget a contentLambda-tól
azt annyiszor adja vissza, mint a szám
Ha nem pozitív:
eldobja mindkettőt
újra próbálkozik
🧠 Példa
amount:   3, 6, -1, -2, 0, 2, ...
content:  a, b, c, ...
output:   a, a, a, b, b, b, b, b, b, f, f, ...
⚠️ Fontos
Több multiProducer példány független
Negatív és nulla értékek kezelése kötelező
🧪 Tesztelés (MultiProducerTest)
Lambdák
amountLambda123A, amountLambda123B → 0,1,2,3,...
contentLambdaA, contentLambdaB → "a", "aa", "aaa", ...

Állapot tárolás:

value123X
txtX
egyelemű tömbökben
Teszt menete
2 producer létrehozása

váltakozó hívás:

A, B, A, B, ...
összesen 7 lépés
✔️ Elvárt eredmény
a, aa, aa, aaa, aaa, aaa, aaaa
🚀 Bővített feladat (8 pont)
🎯 cachedMultiProducer
Alapötlet

A kimeneti stringek egymásból épülnek:

kezdő érték: ""
decisionLambda:
KEEP → marad
EXTEND → hozzáfűz appendTxt
🧠 Példa
appendTxt = "a"

decision:
KEEP, KEEP, EXTEND, EXTEND, EXTEND...

output:
"", "", "", "a", "aa", "aaa", ...
📦 Cache mechanizmus
Lista (cache)
kezdetben: [""]
Működés:
Ha cache.size() == 1:
feltöltjük decisionCount elemmel
majd elemeket kivesszük sorban

⚠️ Fontos:

az utolsó elem alapján folytatjuk
azt nem tesszük vissza újra
🔄 Stream követelmény

A cache feltöltés:

kötelezően 1 darab Stream-mel
🔁 oneFromEach metódus
🎯 Funkció

Két lambda értékeit felváltva adja vissza

Példa
A: a, b, c
B: 1, 2, 3

output:
a, 1, b, 2, c, 3
🧪 Tesztelés (MultiProducerTest2)
🔹 testConstant
decisionLambda mindig KEEP
eredmény mindig: ""
🔹 setup

Két lambda:

flipDecisionLambda

EXTEND, KEEP, EXTEND, KEEP, ...

bunchDecisionLambda

EXTEND, KEEP, EXTEND, KEEP, KEEP,
EXTEND, KEEP, KEEP, KEEP, ...
🔹 flip20 / bunch20
első 20 elem ellenőrzése
🔹 oneOfAB
két lambda: csak "a" és "b"
első 6 elem:
a, b, a, b, a, b
🔹 cachedMultiProducerTest
Beállítás:
két producer:
"a" + flipDecisionLambda
"b" + bunchDecisionLambda
decisionCount: pl. 1, 3, 10
Feldolgozás:
oneFromEach használata
első 40 elem kigyűjtése (20-20)
✔️ Elvárt minta (részlet)
"", "",
a, b,
a, b,
aa, bb,
aa, bb,
aaa, bb,
aaa, bbb,
...
📁 Projekt struktúra
task/
 ├── compulsory/
 │    └── MultiProducer.java
 ├── extension/
 │    ├── MultiProducer2.java
 │    └── MultiProducer2State.java
 └── test/
      ├── MultiProducerTest.java
      └── MultiProducerTest2.java
