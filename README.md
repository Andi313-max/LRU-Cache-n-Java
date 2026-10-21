# LRU Cache në Java

Implementim i politikës Least Recently Used me `get` dhe `put` në kohë mesatare
të amortizuar O(1), duke përdorur `HashMap` dhe një listë të lidhur dyfishe.
Projekt grupor, Algoritme të Avancuara dhe Struktura të Dhënash.

## Anëtarët
- Sotiraq Xhaxha
- Andi Ruci

## Arkitektura
Diagrami në Figma: [shiko këtu](https://www.figma.com/design/c70qc438xkjs6nViSHN4NV/Untitled?node-id=0-1&t=oAdKXgUqfyAI5aTh-1)

## Struktura
- `src/Cache.java`: ndërfaqja (`get`, `put`, `size`).
- `src/LRUCache.java`: implementimi me `HashMap` dhe listë dyfishe.
- `src/Node.java`: nyja e brendshme e listës.
- `src/Main.java`: demonstrimi me kapacitet 3.
- `test/LRUCacheTest.java`: testet automatike pa varësi të jashtme.

## Kompilimi dhe ekzekutimi

Nevojitet JDK 8 ose më i ri, me `java` dhe `javac` në PATH.
Nga dosja kryesore e projektit, në PowerShell ose terminal:

```sh
javac -encoding UTF-8 -d out src/Cache.java src/Node.java src/LRUCache.java src/Main.java test/LRUCacheTest.java
java -cp out Main
java -cp out LRUCacheTest
```

Dalja e demonstrimit:

```text
get(1) = 10
get(2) = -1
get(4) = 40
size = 3
```

Leximi i çelësit `1` e bën atë më të përdorurin së fundi. Shtimi i çelësit `4`
largon çelësin `2`, i cili ka mbetur pa u përdorur për kohën më të gjatë.

## Kontrata dhe kufizimet

- Ky version përdor çelësa `int` jo-negativë dhe vlera `int`, sipas skenarëve
  të dokumentimit. Ndërfaqja gjenerike `Cache<K,V>` mbetet për zgjerim të mëvonshëm.
- Kapaciteti duhet të jetë pozitiv; kapaciteti i pavlefshëm ose çelësi negativ
  shkakton `IllegalArgumentException`.
- `get` kthen `-1` kur çelësi mungon. Edhe vlera `-1` mund të ruhet, prandaj vetëm
  rezultati i `get` nuk e dallon atë nga një mungesë.
- Leximi i suksesshëm dhe çdo `put` e vendosin hyrjen në krye të listës.
  Përditësimi i një çelësi ekzistues nuk e rrit madhësinë.
- Cache-i përdoret nga një thread, ruhet vetëm në memorie dhe zë O(kapacitet) hapësirë.

Testet mbulojnë cache-in bosh, rendin LRU, përditësimet, leximet pa rezultat,
kapacitetin 1 dhe hyrjet e pavlefshme. Krahasimi me `LinkedHashMap` në rend aksesi
verifikon edhe 50 000 operacione me seed fiks në pesë kapacitete.

Krahasimi i performancës me FIFO dhe `LinkedHashMap`, gjeneratori i workload-eve
dhe matjet eksperimentale mbeten punë e planifikuar.
