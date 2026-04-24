#lang racket

;; --- 1. Definirea tipurilor de date (AST) ---
(struct num (v) #:transparent)
(struct binop (op e1 e2) #:transparent)
(struct unop (op e) #:transparent)

;; --- 2. Parsare ---
;; Primește o listă (S-expression) și construiește AST-ul
(define (parseaza expr)
  (match expr
    [(? number? n) (num n)]
    [(list '+ e1 e2) (binop '+ (parseaza e1) (parseaza e2))]
    [(list '- e1 e2) (binop '- (parseaza e1) (parseaza e2))]
    [(list '* e1 e2) (binop '* (parseaza e1) (parseaza e2))]
    [(list '/ e1 e2) (binop '/ (parseaza e1) (parseaza e2))]
    [(list 'sqrt e)  (unop 'sqrt (parseaza e))]
    [(list 'neg e)   (unop 'neg (parseaza e))]
    [_ (error "Eroare: Expresie sau operator invalid!")]))

;; --- 3. Evaluare ---
(define (eval-expresie ast)
  (match ast
    [(num n) n]
    [(binop '+ e1 e2) (+ (eval-expresie e1) (eval-expresie e2))]
    [(binop '- e1 e2) (- (eval-expresie e1) (eval-expresie e2))]
    [(binop '* e1 e2) (* (eval-expresie e1) (eval-expresie e2))]
    [(binop '/ e1 e2) 
     (let ([v2 (eval-expresie e2)])
       (if (= v2 0)
           (error "Eroare: Împărțire la zero!")
           (/ (eval-expresie e1) v2)))]
    [(unop 'sqrt e) 
     (let ([v (eval-expresie e)])
       (if (< v 0)
           (error "Eroare: Radical din număr negativ!")
           (sqrt v)))]
    [(unop 'neg e) (- (eval-expresie e))]))

;; --- 4. Vizualizare (Conversie la Infix) ---
(define (afiseaza-expresie ast)
  (match ast
    [(num n) (number->string n)]
    [(binop op e1 e2) 
     (string-append "(" (afiseaza-expresie e1) " " (symbol->string op) " " (afiseaza-expresie e2) ")")]
    [(unop 'sqrt e) (string-append "sqrt(" (afiseaza-expresie e) ")")]
    [(unop 'neg e) (string-append "(-" (afiseaza-expresie e) ")")]))

;; --- 5. REPL Simplu ---
(define (repl)
  (display "> ")
  (let ([input (read)])
    (unless (eq? input 'exit)
      (with-handlers ([exn:fail? (lambda (exn) (displayln (exn-message exn)))])
        (let* ([ast (parseaza input)]
               [rezultat (eval-expresie ast)]
               [infix (afiseaza-expresie ast)])
          (printf "Infix: ~a\nRezultat: ~a\n" infix rezultat)))
      (repl))))
