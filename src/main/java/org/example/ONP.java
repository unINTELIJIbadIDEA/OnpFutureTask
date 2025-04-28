package org.example;

public class ONP {
    private TabStack stack = new TabStack();
    /**
     * Metoda sprawdza czy równanie kończy się znakiem '='
     *
     * @param rownanie równanie do sprawdzenia
     * @return true jeśli równanie jest poprawne, false jeśli niepoprawne
     */
    boolean czyPoprawneRownanie(String rownanie) {
        if (rownanie.endsWith("="))
            return true;
        else
            return false;
    }

    double silnia(double a){
        if(a == 0)
            return 1;
        else
            return a * silnia(a-1);
    }
    /**
     * Metoda oblicza wartość wyrażenia zapisanego w postaci ONP
     *
     * @param rownanie równanie zapisane w postaci ONP
     * @return wartość obliczonego wyrażenia
     */
    public String obliczOnp(String rownanie) {
        if (czyPoprawneRownanie(rownanie)) {
            stack.setSize(0);
            String wynik = "";
            Double a = 0.0;
            Double b = 0.0;
            for (int i = 0; i < rownanie.length(); i++) {
                if (rownanie.charAt(i) >= '0' && rownanie.charAt(i) <= '9') {
                    wynik += rownanie.charAt(i);
                    if (i + 1 < rownanie.length() && !(rownanie.charAt(i + 1) >= '0' && rownanie.charAt(i + 1) <= '9')) {
                        stack.push(wynik);
                        wynik = "";
                    }
                } else if (rownanie.charAt(i) == '=') {
                    if (stack.getSize() == 0) {
                        throw new IllegalArgumentException("Za mało operandów dla operatora");
                    }
                    return stack.pop();
                } else if (rownanie.charAt(i) == '!') {
                    if (stack.getSize() < 1) {
                        throw new IllegalArgumentException("Za mało operandów dla operatora");
                    }
                    a = Double.parseDouble(stack.pop());
                    if (a < 0 || a != Math.floor(a)) {
                        throw new ArithmeticException("Silnia tylko dla liczb naturalnych");
                    }
                    stack.push(silnia((int) a.doubleValue()) + "");
                } else if (rownanie.charAt(i) != ' ') {
                    if (stack.getSize() < 2) {
                        throw new IllegalArgumentException(" Za mało operandów dla operatora");
                    }
                    b = Double.parseDouble(stack.pop());
                    a = Double.parseDouble(stack.pop());
                    switch (rownanie.charAt(i)) {
                        case ('+'): {
                            stack.push((a + b) + "");
                            break;
                        }
                        case ('-'): {
                            stack.push((a - b) + "");
                            break;
                        }
                        case ('x'):
                            ;
                        case ('*'): {
                            stack.push((a * b) + "");
                            break;
                        }
                        case ('/'): {
                            if (b == 0) {
                                throw new ArithmeticException("Dzielenie przez zero.");
                            }
                            stack.push((a / b) + "");
                            break;
                        }
                        case ('%'):{
                            if (b == 0) {
                                throw new ArithmeticException( "Modulo przez zero");
                            }
                            stack.push((a % b) + "");
                        }
                        case ('^'): {
                            if (a == 0 && b == 0) {
                                throw new ArithmeticException("Nieokreślony wynik (0^0)");
                            }
                            if (a == 0 && b < 0) {
                                throw new ArithmeticException("Dzielenie przez zero (potęgowanie zera z ujemnym wykładnikiem)");
                            }
                            if (a < 0 && b != Math.floor(b) && b != 0) {
                                double mianownik = 1.0 / (b - Math.floor(b));
                                if (mianownik % 2 == 0 || (Math.abs(mianownik - Math.round(mianownik)) < 1e-10 && Math.round(mianownik) % 2 == 0)) {
                                    throw new ArithmeticException ("Pierwiastek parzystego stopnia z liczby ujemnej");
                                }
                            }
                            stack.push(Math.pow(a, b) + "");
                            break;
                        }
                        default: {
                            throw new IllegalArgumentException("Błąd: Nieprawidłowy operator");
                        }
                    }
                }
            }
            return "0.0";
        } else
            throw new IllegalArgumentException("Nieprawidłowa postać równania, brak '='.");
    }

    /**
     * Metoda zamienia równanie na postać ONP
     *
     * @param rownanie równanie do zamiany na postać ONP
     * @return równanie w postaci ONP
     */
    public String przeksztalcNaOnp(String rownanie) {
        if (czyPoprawneRownanie(rownanie)) {
            stack.setSize(0);
            String wynik = "";
            int i = 0;

            while (i < rownanie.length()) {
                if (rownanie.charAt(i) >= '0' && rownanie.charAt(i) <= '9') {
                    wynik += rownanie.charAt(i);
                    if (i + 1 < rownanie.length() && !(rownanie.charAt(i + 1) >= '0' && rownanie.charAt(i + 1) <= '9'))
                        wynik += " ";
                } else {
                    switch (rownanie.charAt(i)) {
                        case ('+'):
                            ;
                        case ('-'): {
                            while (stack.getSize() > 0 && !stack.showValue(stack.getSize() - 1).equals("(")) {
                                wynik = wynik + stack.pop() + " ";
                            }
                            String str = "" + rownanie.charAt(i);
                            stack.push(str);
                            break;
                        }
                        case ('x'):
                            ;
                        case ('*'):
                            ;
                        case ('/'):
                            ;
                        case ('%'): {
                            while (stack.getSize() > 0 && !stack.showValue(stack.getSize() - 1).equals("(")
                                    && !stack.showValue(stack.getSize() - 1).equals("+")
                                    && !stack.showValue(stack.getSize() - 1).equals("-")) {
                                wynik = wynik + stack.pop() + " ";
                            }
                            String str = "" + rownanie.charAt(i);
                            stack.push(str);
                            break;
                        }
                        case ('^'): {
                            while (stack.getSize() > 0 && stack.showValue(stack.getSize() - 1).equals("^")) {
                                wynik = wynik + stack.pop() + " ";
                            }
                            String str = "" + rownanie.charAt(i);
                            stack.push(str);
                            break;
                        }
                        case ('!'): {
                            wynik = wynik + "! ";
                            break;
                        }
                        case ('('): {
                            String str = "" + rownanie.charAt(i);
                            stack.push(str);
                            break;
                        }
                        case (')'): {
                            while (stack.getSize() > 0 && !stack.showValue(stack.getSize() - 1).equals("(")) {
                                wynik = wynik + stack.pop() + " ";
                            }
                            stack.pop();
                            break;
                        }
                        case ('='): {
                            while (stack.getSize() > 0) {
                                wynik = wynik + stack.pop() + " ";
                            }
                            wynik += "=";
                            break;
                        }
                        default: {
                            throw new IllegalArgumentException("Błąd: Nieprawidłowy operator");
                        }
                    }
                }
                i++;
            }
            return wynik;
        } else
            throw new IllegalArgumentException("Nieprawidłowa postać równania, brak '='.");
    }


}