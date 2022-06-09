#lang racket
;Julia Noczyńska
(require data/heap)
(provide sim? wire?
         (contract-out
          [make-sim        (-> sim?)]
          [sim-wait!       (-> sim? positive? void?)]
          [sim-time        (-> sim? real?)]
          [sim-add-action! (-> sim? positive? (-> any/c) void?)]

          [make-wire       (-> sim? wire?)]
          [wire-on-change! (-> wire? (-> any/c) void?)]
          [wire-value      (-> wire? boolean?)]
          [wire-set!       (-> wire? boolean? void?)]

          [bus-value (-> (listof wire?) natural?)]
          [bus-set!  (-> (listof wire?) natural? void?)]

          [gate-not  (-> wire? wire? void?)]
          [gate-and  (-> wire? wire? wire? void?)]
          [gate-nand (-> wire? wire? wire? void?)]
          [gate-or   (-> wire? wire? wire? void?)]
          [gate-nor  (-> wire? wire? wire? void?)]
          [gate-xor  (-> wire? wire? wire? void?)]

          [wire-not  (-> wire? wire?)]
          [wire-and  (-> wire? wire? wire?)]
          [wire-nand (-> wire? wire? wire?)]
          [wire-or   (-> wire? wire? wire?)]
          [wire-nor  (-> wire? wire? wire?)]
          [wire-xor  (-> wire? wire? wire?)]

          [flip-flop (-> wire? wire? wire? void?)]))

;____________________________SIMULATION____________________________

(struct sim ([current-time #:mutable] [event-queue #:mutable]))

(struct action ([time #:mutable] [function #:mutable]))

(define (action<=? a1 a2)
  (<= (action-time a1)(action-time a2)))

;(action<=? (action 1 (lambda (x)x))(action 2 (lambda (x)x)))

(define (make-sim)
  (sim 0.0 (make-heap action<=?)))

;(sim-current-time (make-sim))

(define (sim-time sim) ;getter
  (sim-current-time sim))

(define (sim-wait! sim time)
  (define (it sim time delta)
    (if (<= time 0.0)
        (void)
        (begin
          (set-sim-current-time! sim (+ (sim-time sim) delta))
          (call-event-queue (sim-event-queue sim) sim)
          (it sim (- time  delta) delta))))
  (if (< time 1.0)
      (it sim time (/ time 10.0))
      (it sim time 1.0)))

(define (call-event-queue events-heap sim)
  (cond
    [(equal? (heap-count events-heap) 0) ;jeżeli kolejka zdarzeń jest pusta
      (void)]
    [(<= (action-time (heap-min events-heap)) (sim-time sim)) ;jeżeli czas jakiejś akcji upłynął
     (begin
       ((action-function (heap-min events-heap))) ;wywołujemy funcję
       (heap-remove-min! events-heap) ;usuwamy ją z kolejki
       (call-event-queue events-heap sim))] ;sprawdzamy dla reszty akcji
    [else (void)]))
     
(define (sim-add-action! sim time f)
 (heap-add! (sim-event-queue sim) (action (+ (sim-time sim) time) f)))

;TEST Z KTÓREGO WYNIKA ZE SIM-WAIT JEST ZLE
;(define sim-test (make-sim))
;(sim-add-action! sim-test 3 (lambda ()(display " test sim-wait! ")))
;(sim-add-action! sim-test 8.5 (lambda ()(display " second test sim-wait! ")))
;(display "1 ")
;(sim-wait! sim-test 2)
;(sim-current-time sim-test)
;(display "2 ")
;(sim-wait! sim-test 1)
;(sim-current-time sim-test)
;(display "3 ")
;(sim-wait! sim-test 5.5)
;(sim-current-time sim-test)


;____________________________WIRE__________________________________

(struct wire ([value #:mutable] [sim  #:mutable] [actions #:mutable]))

(define (make-wire sim)
  (wire #f sim (list)))

(define (wire-on-change! wire f)
  (begin
    (f) ;wywołujemy akcję
    (set-wire-actions! wire (cons f (wire-actions wire))))) ; dodajemy akcję do listy akcji przewodu

(define (call-actions xs)
  (if (null? xs)
      (void)
      (begin
        ((car xs))
        (call-actions (cdr xs)))))

(define (wire-set! wire value) ;setter przewodu
  (if (equal? value (wire-value wire))
      (void)
      (begin
        (set-wire-value! wire value)
        (call-actions (wire-actions wire)))))

;TEST
;(define test-wire (make-wire (make-sim)))
;(display "1 ")
;(wire-on-change! test-wire (lambda()(display " test wire ")))
;(display "2 ")
;(wire-set! test-wire #t)
;(display "3 ")

  ;_________________________________BUS________________________________

(define (bus-set! wires value) ;setter magistrali
  (match wires
    ['() (void)]
    [(cons w wires)
     (begin
       (wire-set! w (= (modulo value 2) 1))
       (bus-set! wires (quotient value 2)))]))

(define (bus-value ws) ;getter?
  (foldr (lambda (w value) (+ (if (wire-value w) 1 0) (* 2 value)))
         0
         ws))

(define (flip-flop out clk data)
  (define sim (wire-sim data))
  (define w1  (make-wire sim))
  (define w2  (make-wire sim))
  (define w3  (wire-nand (wire-and w1 clk) w2))
  (gate-nand w1 clk (wire-nand w2 w1))
  (gate-nand w2 w3 data)
  (gate-nand out w1 (wire-nand out w3)))

  ;_________________________________GATES________________________________

(define (gate-not out in)
  (define (not-action)
    (sim-add-action! (wire-sim out) 1.0 (lambda () (wire-set! out (not (wire-value in))))))
  (wire-on-change! in not-action))

(define (gate-and out in1 in2)
  (define (and-action)
    (sim-add-action! (wire-sim out) 1.0 (lambda () (wire-set! out (and (wire-value in1)(wire-value in2))))))
  (begin 
    (wire-on-change! in1 and-action)
    (wire-on-change! in2 and-action)))

(define (gate-nand out in1 in2)
  (define (nand-action)
    (sim-add-action! (wire-sim out) 1.0 (lambda () (wire-set! out (nand (wire-value in1) (wire-value in2))))))
  (begin
    (wire-on-change! in1 nand-action)
    (wire-on-change! in2 nand-action)))

(define (gate-or out in1 in2)
  (define (or-action)
    (sim-add-action! (wire-sim out) 1.0 (lambda () (wire-set! out (or (wire-value in1)(wire-value in2))))))
  (begin
    (wire-on-change! in1 or-action)
    (wire-on-change! in2 or-action)))

(define (gate-nor out in1 in2)
   (define (nor-action)
    (sim-add-action! (wire-sim out) 1.0 (lambda () (wire-set! out (nor (wire-value in1)(wire-value in2))))))
  (begin
    (wire-on-change! in1 nor-action)
    (wire-on-change! in2 nor-action)))
  

(define (gate-xor out in1 in2)
  (define (xor-action)
    (sim-add-action! (wire-sim out) 2.0 (lambda () (wire-set! out (xor (wire-value in1)(wire-value in2))))))
  (begin
    (wire-on-change! in1 xor-action)
    (wire-on-change! in2 xor-action)))

  ;_________________________________WIRE-GATES________________________________
;(struct wire ([value #:mutable] [sim  #:mutable] [actions #:mutable]))

(define (wire-not in)
  (define out
    (make-wire (wire-sim in)))
  (gate-not out in)
  out)

;TEST
;(define test-gate (make-sim))
;(define not-wire (wire-not (make-wire test-gate)))
;(wire-value not-wire)
;(sim-wait! test-gate 1)
;(wire-value not-wire)

(define (wire-and in1 in2)
  (define out
    (make-wire (wire-sim in1)))
  (gate-and out in1 in2)
  out)


(define (wire-nand in1 in2)
    (define out
      (make-wire (wire-sim in1)))
  (gate-nand out in1 in2)
  out)


(define (wire-or in1 in2)
   (define out
      (make-wire (wire-sim in1)))
  (gate-or out in1 in2)
  out)

(define (wire-nor in1 in2)
 (define out
      (make-wire (wire-sim in1)))
  (gate-nor out in1 in2)
  out)

(define (wire-xor in1 in2)
  (define out
      (make-wire (wire-sim in1)))
  (gate-xor out in1 in2)
  out)

;___________________________________________________________________________________


;(define simulation (make-sim))
;(define x (wire-xor (make-wire simulation)(make-wire simulation)))
;(define simulation (make-sim))
;
;(define (make-counter n clk en)
;  (if (= n 0)
;      '()
;      (let [(out (make-wire simulation))]
;        (flip-flop out clk (wire-xor en out))
;        (cons out (make-counter (- n 1) clk (wire-and en out))))))
;
;(define clk (make-wire simulation))
;(define en  (make-wire simulation))
;(define counter (make-counter 4 clk en))
;(wire-set! en #t)
;
;
;(define (probe name w)
;  (wire-on-change! w (lambda ()
;                       (display name)
;                       (display " = ")
;                       (display (wire-value w))
;                       (display "\n")
;                       (display (wire-actions w))
;                       (display "\n"))))
;(define (tick)
;  (wire-set! clk #t)
;  (sim-wait! simulation 20)
;  (wire-set! clk #f)
;  (sim-wait! simulation 20)
;  (bus-value counter))
;
;(define add_sim (make-sim))
;
;(define a (make-wire add_sim))
;(define b (make-wire add_sim))
;(define s (make-wire add_sim)) 
;(define c (make-wire add_sim)) 
;
;(probe 'a a)
;(probe 'b b)
;(probe 'c c)
;(probe 's s)
;
;(define (half-adder a b s c)
;  (let ((d (make-wire add_sim)) (e (make-wire add_sim)))
;    (gate-or d a b)
;    (gate-and c a b)
;    (gate-not e c )
;    (gate-and s d e)
;    (begin
;      (probe 'd d)
;      (probe 'e e))))



