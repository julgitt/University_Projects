#lang racket
;Julia Noczyńska 331013

(provide (struct-out column-info)
         (struct-out table)
         (struct-out and-f)
         (struct-out or-f)
         (struct-out not-f)
         (struct-out eq-f)
         (struct-out eq2-f)
         (struct-out lt-f)
         table-insert
         table-project
         table-sort
         table-select
         table-rename
         table-cross-join
         table-natural-join)

(define-struct column-info (name type) #:transparent)

(define-struct table (schema rows) #:transparent)

(define cities
  (table
   (list (column-info 'city    'string)
         (column-info 'country 'string)
         (column-info 'area    'number)
         (column-info 'capital 'boolean))
   (list (list "Wrocław" "Poland"  293 #f)
         (list "Warsaw"  "Poland"  517 #t)
         (list "Poznań"  "Poland"  262 #f)
         (list "Berlin"  "Germany" 892 #t)
         (list "Munich"  "Germany" 310 #f)
         (list "Paris"   "France"  105 #t)
         (list "Rennes"  "France"   50 #f))))

(define countries
  (table
   (list (column-info 'country 'string)
         (column-info 'population 'number))
   (list (list "Poland" 38)
         (list "Germany" 83)
         (list "France" 67)
         (list "Spain" 47))))

(define (empty-table columns) (table columns '()))

;===========================================
;                  INSERT
;===========================================


(define (table-insert row tab)
  ;check if types in the row and the table schema match
  (define (compare-types row schema) 
    (cond
      [(and (null? row) (null? schema)) #t] ;all types match, return true
      [(and (number? (car row)) (equal? 'number (column-info-type (car schema))))
       (compare-types (cdr row)(cdr schema))]
      [(and (string? (car row)) (equal? 'string (column-info-type (car schema))))
       (compare-types (cdr row)(cdr schema))]
      [(and (boolean? (car row)) (equal? 'boolean (column-info-type (car schema))))
       (compare-types (cdr row)(cdr schema))]
      [(and (symbol? (car row)) (equal? 'symbol (column-info-type (car schema))))
       (compare-types (cdr row)(cdr schema))]
      [else #f])) ;type is not correct, return false
  (if (equal? (length row) (length (table-schema tab))) 
      (if (compare-types row (table-schema tab))
          (table (table-schema tab) (cons row (table-rows tab)))
          (error "The row types don't match the schema types of the table"))
      (error "The length of the row doesn't match the length of the lines in the table")))

;===========================================
;                  PROJECT
;===========================================

(define (table-project cols tab)
  (find-columns cols tab null (set-rows-acc (table-rows tab))))

;sets up an accumulator that stores rows for the list of empty lists
(define (set-rows-acc tab-rows)
  (map (lambda (x) (list)) tab-rows))

(define (find-columns cols tab schema-acc rows-acc)
  (if (null? (table-schema tab)) ;we found all the columns we wanted to find, so we return them
      (table (reverse schema-acc) (map reverse rows-acc))
      (let ([rows-rest  (map cdr (table-rows tab))]
            [schema-rest  (cdr (table-schema tab))]
            [rows-first (map car (table-rows tab))] ;rows of the first column
            [schema-first (column-info-name (car(table-schema tab)))]);name of the first column
        (cond
          ;we found one of the columns we were looking for
          [(member schema-first cols) 
           (find-columns cols            
                         ;we checked x columns, now we need to search the rest of the table
                         (table schema-rest rows-rest)
                         ;we add to accumulators table-schema and rows of the column
                         (cons (car(table-schema tab)) schema-acc)
                         (map cons rows-first rows-acc))]
          ;this column is not on the list, so we skip it and we search the rest of the table
          [else (find-columns cols (table schema-rest rows-rest) schema-acc rows-acc)]))))
    

;===========================================
;                  RENAME
;===========================================

(define (table-rename col new-col tab)
  (table 
   (map (lambda(current-col)(if (equal? (column-info-name current-col) col)
                                ;rename if it's column that we want to rename
                                (column-info new-col (column-info-type current-col))
                                ;otherwise don't change column
                                current-col))(table-schema tab))(table-rows tab)))

;===========================================
;                  SORT
;===========================================

(define (table-sort cols tab) ;cols - list of columns to sort by
  (table (table-schema tab) ; we don't change table-schema
         (insertion-sort cols  (table-rows tab) tab)))

(define (insertion-sort cols rows tab)
  (define (it cols rows tab acc) ;acc contains list of sorted rows
    (if (null? rows)
        acc
        ; insert another line to acc
        (it cols (cdr rows) tab (insert cols (car rows) tab acc))))
  (it cols rows tab null))


(define (insert cols row tab acc)
  (cond
    ;there is no elements in acc so we don't check the place where we need to insert
    [(null? acc) (list row)]
    ;we don't change the order if there are no more columns to sort by
    [(null? cols) (append acc (list row))]  
    [else (let ([type (take-type (car cols) (table-schema tab))]
                [value-insert (take-value (car cols) row (table-schema tab))]
                [value-acc (take-value (car cols) (car acc) (table-schema tab))])
            (if (equal? value-insert value-acc);we have to sort by the next column rows with the same value
                (append (insert (cdr cols) row tab
                                ; filter acc to a list of rows with a value equal to the value of value-insert
                                (filter (lambda(row)(equal?(take-value (car cols) row (table-schema tab))
                                                           value-insert))acc))
                        ;append with the rest of acc
                        (filter (lambda(row)(not (equal?
                                                  (take-value (car cols) row (table-schema tab))
                                                  value-insert)))acc))
                ;if values are not equal compare them
                (if(cond [(equal? 'string type)
                          (string<? value-acc value-insert)]
                         [(equal? 'number type)
                          (< value-acc value-insert)]
                         [(equal? 'symbol type)
                          (symbol<? value-acc value-insert)]
                         [(equal? 'boolean type)
                          (not(and (not value-insert) value-acc))])
                   ;if value-acc < value-insert
                   (cons (car acc) (insert cols row tab (cdr acc)))
                   (cons row acc))))]))
 

(define (take-value col-name row schema)
  (cond [(null? schema) (error "there is no column with that name")]
        [(equal? col-name (column-info-name (car schema)))
         (car row)]
        [else(take-value col-name (cdr row) (cdr schema))]))

(define (take-type col-name schema)
  (cond [(null? schema) (error "there is no column with that name")]
        [(equal? col-name (column-info-name (car schema)))
         (column-info-type (car schema))]
        [else (take-type col-name (cdr schema))]))    

;===========================================
;                  SELECT
;===========================================

(define-struct and-f (l r))
(define-struct or-f (l r))
(define-struct not-f (e))
(define-struct eq-f (name val))
(define-struct eq2-f (name name2))
(define-struct lt-f (name val))
 
(define (table-select form tab)
  (define (eval form schema row)
    (cond [(and-f? form)
           (and (eval (and-f-l form) schema row)
                (eval  (and-f-r form) schema row))]
          [(or-f? form)
           (or (eval (or-f-r form) schema row)
               (eval (or-f-l form) schema row))]
          [(not-f? form)
           (not (eval (not-f-e form) schema row))]
          [(eq-f? form)
           (equal? (eq-f-val form) (take-value (eq-f-name form) row schema))]
          [(eq2-f? form)
           (equal?
            (take-value (eq2-f-name2 form) row schema)
            (take-value (eq2-f-name form) row schema))]
          [(lt-f? form)
           (cond
             [(equal? 'string (take-type (lt-f-name form) schema))
              (string<?
               (take-value (lt-f-name form) row schema)
               (lt-f-val form))]
             [(equal? 'number (take-type (lt-f-name form) schema))
              (< (take-value (lt-f-name form) row schema)
                 (lt-f-val form))]
             [(equal? 'symbol (take-type (lt-f-name form) schema))
              (symbol<?
               (take-value (lt-f-name form) row schema)
               (lt-f-val form))]
             [(equal? 'boolean (take-type (lt-f-name form) schema))
              (if (and (not (lt-f-val form)) (take-value (lt-f-name form) row schema))
                  #t #f)])]))
  (table (table-schema tab) (filter (lambda (row)(eval form (table-schema tab) row)) (table-rows tab))))

;===========================================
;                  CROSS-JOIN
;===========================================

(define (table-cross-join tab1 tab2)
  (let ([schema (append (table-schema tab1) (table-schema tab2))])
    (table schema
           (rejoin (flatten;
                    ;append all rows of the tab1 with all rows of the tab2
                    (map (lambda (y) (map (lambda (x) (append y x))(table-rows tab2)))
                         (table-rows tab1))) (length schema)))))

;to give the parentheses correctly
;"take" makes a list from first x elements of the list given in the argument (where x is a second argument of "take")
;"drop" makes a list from elements that come after first x elements 
(define (rejoin all-rows length)
  (cond [(null? all-rows) null]
        [else (cons (take all-rows length)
                    (rejoin (drop all-rows length) length))]))      

;===========================================
;                  NATURAL-JOIN
;===========================================

;1)Zmień nazwę tych kolumn tabeli tab2, które występują również w tab1.
;2) Wykonaj złączenie kartezjańskie.
;3) Wybierz tylko te wiersze, dla których wartości w kolumnach powtarzających
;się w obu tabelach są równe.
;4) Wykonaj projekcję, usuwając z wyniku kolumny, które zostały przenazwane
;w pierwszym punkcie.


(define symbol-append ;(string symbol -> symbol)
  (lambda(x y)(string->symbol (string-append(symbol->string x) y))))

(define (table-natural-join tab1 tab2)
  ;4)
  (table-project (columns (table-schema (select-rows;3)
                                         (table-cross-join ;2)
                                          tab1 (changing-names tab1 tab2));1)
                                         (changing-names tab1 tab2) tab2))
                          (table-schema tab2) (table-schema (changing-names tab1 tab2)))
                 (select-rows (table-cross-join tab1 (changing-names tab1 tab2))
                              (changing-names tab1 tab2) tab2)))

;1)Rename tab2 columns that also appear in tab1
(define (changing-names tab1 tab2)
  (define (it tab1 tab2 current-col)
    (cond [(null? current-col) tab2]
          [(member (car current-col) (table-schema tab1))
           ;if column appear in tab1 rename by adding "!" to the name, then search for other repetitions
           (it tab1 (table-rename (column-info-name (car current-col));current-col-name
                                  (symbol-append (column-info-name (car current-col)) "!")
                                  tab2) (cdr current-col))]
          [else(it tab1 tab2 (cdr current-col))]))
  (it tab1 tab2 (table-schema tab2)))

;3)Select only those rows with values ​​in repeating columns
;in both tables are equal.

(define (select-rows result-table tab-changed-name tab)
  (let ([names (find-names (table-schema tab) (table-schema tab-changed-name) null)]);List of changed names
    (define (it name result)
      (if(null? name) result
         (it (cdr name) (table-select;select only those rows where values are equal
                          (eq2-f (car name)(symbol-append (car name) "!")) 
                          result-table))))
    (it names result-table)))

;to find list of changed names
(define (find-names tab1 tab2 xs)
  (cond [(or (null? tab2)(null? tab1))  xs]
        [(equal? (symbol-append (column-info-name (car tab1)) "!") ; name was changed
                 (column-info-name (car tab2)))
         (find-names (cdr tab1) (cdr tab2)
                     (cons (column-info-name (car tab1)) xs))]
        [else (find-names (cdr tab1) (cdr tab2) xs)]))

;4)list of names of columns (without those changed)                               
(define (columns result-tab tab tab-changed-name)
  (let ([names (find-names tab  tab-changed-name null)])
    ;delete changed names
    (filter (lambda (x)(not (member x (map (lambda(name)(symbol-append name "!")) names))))
            ;take  list of all column names
            (map (lambda (y)(column-info-name y)) result-tab))))


;===========================================
;                  TESTS
;===========================================

;INSERT

;(table-rows (table-insert(list "Rzeszow" "Poland" 129 #f)cities))

;bad length
;(table-insert(list "Rzeszow" "Poland" 129 #f)countries))

;bad type
;(table-insert(list #f "Poland" 129 #f)cities))

;bad type
;(table-insert (list(list)(list)(list)(list)) cities)


;(table-insert (list "Rzeszow" "Poland" 129 #f) (empty-table (table-schema cities)))
;bad type
;(table-insert (list #f "Poland" 129 #f)(empty-table (list)))

;(table-insert (list) (empty-table (list)))
;_________________________________________________
;PROJECT

;(table-project '(city country) cities)
;(table-project '(city country capital area) cities)

;with a column name that does not exist
;(table-project '( city country capital area cities) cities ) 
;(table-project '( ) cities )
;(table-project '(city) (empty-table (table-schema cities)))
;(table-project '(city) (empty-table null))

;_________________________________________________
;RENAME

;(table-rename 'city 'name cities)
;(table-rename 'city 'city cities)
;(table-rename 'cities 'city cities)

;_________________________________________________
;SORT

;(table-sort '(country city) cities)
;(table-sort '(capital area) cities)
;(table-sort '() cities)
;(table-sort '(capital) cities)

;_________________________________________________
;SELECT

;(define test-form2
 ;(and-f (or-f (eq-f 'capital #t) (or (eq2-f 'country 'city) (lt-f 'country "Poland")))
   ;    (not-f (lt-f 'area 600))))

;(define test-form
 ;(and-f (or-f (eq-f 'capital #t)(lt-f 'country "Poland"))
  ;             (not-f (lt-f 'area 300))))

;(table-select (and-f(eq-f 'capital #t)
 ;                  (not-f (lt-f 'area 300)))cities)

;(table-select test-form cities)
;(table-select test-form2 cities)

;_________________________________________________
;TABLE-CROSS-JOIN

;(table-cross-join cities countries)

;(table-cross-join countries cities)

;(table-cross-join (empty-table null) cities)

;(table-cross-join (empty-table (table-schema cities)) cities)

;(table-cross-join cities (empty-table null))

;_________________________________________________
;TABLE-NATURAL-JOIN

;1)Zmień nazwę tych kolumn tabeli tab2, które występują również w tab1.
;2) Wykonaj złączenie kartezjańskie.
;3) Wybierz tylko te wiersze, dla których wartości w kolumnach powtarzających
;się w obu tabelach są równe.
;4) Wykonaj projekcję, usuwając z wyniku kolumny, które zostały przenazwane
;w pierwszym punkcie.


;(table-natural-join cities countries)
;(table-natural-join countries cities)
;(table-natural-join (empty-table (table-schema cities)) cities)

;(changing-names cities countries)

;(find-names (table-schema countries) (table-schema (changing-names cities countries)) null)

;(select-rows
 ;(table-cross-join 
  ;cities (changing-names cities countries))
 ;(changing-names cities countries) countries)

;(columns (table-schema (select-rows
;                        (table-cross-join 
;                         cities (changing-names cities countries))
;                        (changing-names cities countries) countries))
;         (table-schema countries) (table-schema (changing-names cities countries)))