package count_min_sketch_app;

import static java.lang.Math.abs;

public class CMSketch {

    private int[][] CMTable; // таблица CMSketch
    private int W; // ширина
    private int[] HTable; // таблица простых чисел для хэш функций

    CMSketch(int h, int w) { // число хэш функций и ширина
        CMTable = new int[h][w];
        HTable = new int[h];
        initHTable();
        W = w;
    }

    private void initHTable() {
        HTable[0] = 31;
        for (int i = 1; i < HTable.length; i++) {
            HTable[i] = newSize(HTable[i - 1]);
        }
        for (int i = 0; i < HTable.length; i++) {
//            System.out.print(HTable [i] + " ");
        }
    }

    private int newSize(int s) {
        for (int i = s * 2 + 1; i < s * 3; i++) { // ищем простое число для следующей хэш функции
            if (isPrime(i)) {
                return i;
            }
        }
        return -1;
    }

    private boolean isPrime(int n) { // проверка простого числа
        if (n < 2) {
            return false;
        }
        for (int i = 2; i * i <= n; i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }

    private int hash(String s, int i) { // строка для хэширования и номер хэш функции
        long hash = 0;
        for (int j = 0; j < s.length(); j++) {
            hash = HTable[i] * hash + s.charAt(j);
        }
        return (int) (abs(hash) % W);
    }

    public void add(String s) { // добавление слова
        for (int i = 0; i < HTable.length; i++) { // проходим по всем хэш функциям для вычисления столбца (строки - номера хэш функций), увеличивем счетчики в таблице CMSketch
            CMTable[i][hash(s, i)]++;
        }
    }

    public int get(String s) { // считывание счетчика для слова s, выбор минимального из возможных
        int minCount = CMTable[0][hash(s, 0)];
        for (int i = 1; i < HTable.length; i++) { // проходим по всем строкам CMTable начиная со 2-ой
            if (minCount > CMTable[i][hash(s, i)]) {
                minCount = CMTable[i][hash(s, i)];
            }
        }
        return minCount;
    }

    public void clear() {
        for (int i = 0; i < CMTable.length; i++) {
            for (int j = 0; j < CMTable[i].length; j++) {
                CMTable[i][j] = 0;
            }
        }
    }

    public void CMTableOut() { // вывод CMTable
        for (int i = 0; i < CMTable.length; i++) {
            System.out.print("I " + i + " > ");
            for (int j = 0; j < CMTable[i].length; j++) {
                System.out.print(CMTable[i][j] + " ");
            }
            System.out.println("");
        }
    }

}
