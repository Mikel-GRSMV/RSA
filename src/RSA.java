// Java-реализация алгоритма RSA

import java.io.*;

public class RSA {
    // Определение и инициализация значений p, q, n, e, d и т.д.
    private int p = 0;
    private int q = 0;
    private long mod = 0;
    private long φ = 0;
    private long public_key = 0;
    private long private_key = 0;

    private long text = 0; // простой текст
    private long secretWord = 0; // зашифрованный текст
    private long word = 0; // обычный текст после расшифровки

    // проверка p и q = простыми числами
    public boolean primeNumber(long t) {
        long k = 0;
        k = (long) Math.sqrt((double) t);
        boolean flag = true;
        outer:
        for (int i = 2; i <= k; i++) {
            if ((t % i) == 0) {
                flag = false;
                break outer;
            }
        }
        return flag;
    }

    // Вводим простые числа p, q с клавиатуры
    // Определяем, является ли ввод простым числом
    public void inputPQ() throws Exception {
        do {
            System.out.print("Пожалуйста, введите простое число p: ");
            BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
            String br = stdin.readLine();
            this.p = Integer.parseInt(br);

        }
        // Если введено не простое число, на экране будет продолжаться ввод «введите простое число q», пока не будет введено простое число
        while (!primeNumber(this.p));
        do {
            System.out.print("Пожалуйста, введите простое число q: ");
            BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
            String br = stdin.readLine();
            this.q = Integer.parseInt(br);
        }
        // Если оба входа удовлетворяют условиям (простые числа), вычисляем их произведение
        while (!primeNumber(this.q));
        this.mod = (long) this.p * this.q;
        this.φ = (long) (p - 1) * (q - 1);
        System.out.println("Произведение двух простых чисел mod = "+ p +"×"+ q +": " + this.mod);
        System.out.println("Число Эйлера φ(mod) = ("+ p +"-1)×("+ q +"-1): " + this.φ);
    }

    // Чтобы найти НОД, вызовем функцию java gcd, эта функция возвращает наибольший общий множитель двух или более положительных чисел
    public long gcd(long a, long b) {
        long gcd;
        if (b == 0)
            gcd = a;
        else
            gcd = gcd(b, a % b);
        System.out.println("НОД(Наибольший общий делитель)= " + gcd);
        return gcd;
    }

    // Предоставляем экспоненту.Это значение должно быть меньше φ и должно быть взаимно простым простым с m.
    public void getPublic_key() throws Exception {
        do {
            System.out.println("|==========================================================");
            System.out.println("| 1. e простое число;");
            System.out.println("| 2. 1 < e < φ;");
            System.out.println("| 3. e взаимно простое с φ(те не имеет общих делителей с φ)");
            System.out.println("| ==========================================================");
            System.out.print("Пожалуйста, введите экспоненту(e): ");
            BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
            String br = stdin.readLine();
            // Используйте parseLong, чтобы проанализировать строку и вернуть ее в данные Long
            this.public_key = Long.parseLong(br);
        } while ((this.public_key >= this.φ) || (this.gcd(this.φ, this.public_key) != 1));
        System.out.println("Открытый ключ: " + "{" + this.public_key + ", " + mod + "}");
    }

    // Рассчитываем ключ методом расширения Евклида
    public void getPrivate_key() {
        long value = 1;
        outer:
        for (long i = 1; ; i++) {
            value = i * this.φ + 1;
            System.out.println("value:  " + value);
            if ((value % this.public_key == 0) && (value / this.public_key < this.φ)) {
                this.private_key = value / this.public_key;
                break outer;
            }
        }
        System.out.println("Закрытый ключ: " + "{" + this.private_key  + ", " + mod + "}");
    }


    // Предоставляем извне требуемый зашифрованный открытый текст
    public void getText() throws Exception {
        System.out.print("Пожалуйста, введите обычный текст: ");
        // Вызов BufferReader для чтения текста из входного потока символов для вывода на дисплей
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
        String br = stdin.readLine();
        this.text = Long.parseLong(br);
    }

    // Шифрование, расчет дешифрования
    // Появится предупреждение о принудительном преобразовании типа, а также оператор SuppressWarnings для защиты предупреждающего сообщения
    @SuppressWarnings("empty-statement")
    public long colum(long y, long n, long key) {
        long mul;
        if (key == 1)
            mul = y % n;
        else
            mul = y * this.colum(y, n, key - 1) % n;
        return mul;
    }

    // Расшифровать после шифрования
    public void pascolum() throws Exception {
        this.getText();
        System.out.println("Введенный простой текст: " + this.text);
        // Процесс шифрования
        this.secretWord = this.colum(this.text, this.mod, this.public_key);
        System.out.println("Зашифрованный текст: " + this.secretWord);
        // Процесс расшифровки
        this.word = this.colum(this.secretWord, this.mod, this.private_key);
        System.out.println("Открытый текст, полученный после дешифрования: " + this.word);

    }

    // В языке java, если в методе возникает исключение, исключение необходимо передать вызывающему методу для обработки
    // Оператор, объявляющий возникновение исключения, выдает Exception
    public static void main(String[] args) throws Exception {
        RSA t = new RSA();
        t.inputPQ();
        t.getPublic_key();
        t.getPrivate_key();
        t.pascolum();
    }
}
