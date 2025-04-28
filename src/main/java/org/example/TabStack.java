package org.example;

public class TabStack {
    private String[] stack = new String[20];
    private int size = 0;

    /**
     * Metoda zdejmująca wartość ze stosu
     *
     * @return wartość z góry stosu
     */
    public String pop() {
        if (size == 0) { // Sprawdzamy, czy stos jest pusty
            throw new IllegalStateException("Stos jest pusty");
        }
        size--;
        return stack[size];
    }

    /**
     * metoda dokładająca na stos
     *
     * @param a - wartość dokładana do stosu
     */
    public void push(String a) {
        if (size >= stack.length) {
            throw new StackOverflowError("Przepełniony stos");
        }
        stack[size] = a;
        size++;
    }

    /**
     * Metoda zwraca tekstową reprezentację stosu
     */
    public String toString() {
        String tmp = "";
        for (int i = 0; i < size; i++)
            tmp += stack[i] + " ";
        return tmp;
    }

    /**
     * Metoda zwraca rozmiar stosu
     *
     * @return size rozmiar stosu
     */
    public int getSize() {
        return size;
    }

    /**
     * Ustawia wartość stosu
     *
     * @param i
     */
    public void setSize(int i) {
        size = i;
    }

    /**
     * Metoda zwraca wartość z określonej pozycji stosu
     *
     * @param i pozycja parametru do zobaczenia
     * @return wartość stosu
     */
    public String showValue(int i) {
        if (i < size)
            return stack[i];
        else return null;
    }
}