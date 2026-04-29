# Proiect Programare Funcțională - Calculator pentru Expresii Matematice

**Echipa:**
* Jugaru Paul
* Mincu Alin
* Noje Vlad
* Radoi Vlad

## 1. Descrierea Problemei
Acest proiect constă în implementarea unui calculator capabil să evalueze expresii matematice simple scrise într-un format prefixat (similar limbajului LISP, ex: `(+ 3 (* 4 5))`). Proiectul a fost dezvoltat în paralel folosind două limbaje de programare diferite (Scala și Racket) pentru a evidenția diferențele de paradigmă. Scopul principal este construirea unui arbore sintactic abstract (AST) pornind de la input, evaluarea recursivă a acestuia și gestionarea corectă a erorilor de execuție fără oprirea bruscă a programului.

## 2. Soluția Implementată
Pentru ambele limbaje, arhitectura soluției a fost împărțită în următoarele module logice:
* **Structura de Stare (AST):** Expresiile sunt reprezentate intern ca un arbore. În Scala s-au folosit Algebraic Data Types (`sealed trait` și `case class`), iar în Racket s-au folosit structuri (`struct`).
* **Parsarea (`parseaza`):** Transformă un șir de caractere (sau o listă, în cazul Racket) în structura internă arborescentă, validând sintaxa.
* **Evaluarea (`eval-expresie`):** O funcție recursivă care parcurge arborele de jos în sus, aplicând operațiile matematice (adunare, scădere, înmulțire, împărțire, radical, negare).
* **Vizualizarea (`afiseaza-expresie`):** Funcție care convertește arborele intern înapoi într-un format lizibil de tip infix (ex: `(3 + 4)`).
* **Interfața REPL:** O buclă interactivă (Read-Eval-Print Loop) care citește comenzi de la utilizator.
* **Tratarea Erorilor:** S-au implementat mecanisme "safe" pentru cazuri limită (împărțire la zero, radical din număr negativ). În Scala s-a utilizat monada `Either`, iar în Racket blocul `with-handlers`.

## 3. Versiunile Limbajelor de Programare Utilizate
* **Scala:** Versiunea 3.x (compatibil și cu 2.13.x)
* **Racket:** Versiunea 8.x

## 4. Instrucțiuni de Rulare
Ambele variante utilizează un REPL care așteaptă comenzi la promptul `> `. Pentru ieșire se va folosi comanda: `exit`.

Deschideți terminalul în directorul cu fișierele sursă și rulați:
* **Pentru Racket:** `racket calculator.rkt`
* **Pentru Scala:** `scala Calculator.scala`

## 5. Testare și Validare
Toate exemplele de rulare, inclusiv verificarea funcțiilor principale și tratarea cazurilor limită (edge-cases și reziliență la erori), se regăsesc detaliate în fișierul alăturat: **teste.txt**.
