package count_min_sketch_app;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.stream.Stream;

public class Count_Min_Sketch_App {

    public static String[] parseFile(String strpath) { // читаем файл в массив слов
        Path path = Paths.get(strpath);
        try (Stream<String> lines = Files.lines(path)) {
            ArrayList<String> result = new ArrayList<String>();
            lines.forEach(s -> {
                StringTokenizer token = new StringTokenizer(s, " .,<>@-=():_';\"");
                while (token.hasMoreTokens()) {
                    result.add(token.nextToken());
                }
            });
            return (String[]) result.toArray(new String[result.size()]);
        } catch (IOException ex) {
        }
        return null;
    }

    public static int countWord(String[] wordArr, String sample) { // точный подсчет
        int count = 0;
        for (int i = 0; i < wordArr.length; i++) {
            if (wordArr[i].equals(sample)) {
                count++;
            }
        }
        return count;
    }

    public static void main(String[] args) {

        String findString = "Stephens"; // слово для поиска

        String[] words = parseFile("src\\count_min_sketch_app\\wiki.train.tokens"); // wiki.test.train.tokens - 890 ; wiki.train.tokens - 1 762 978 слов
        System.out.println("total words " + words.length);

        System.out.println("exact result " + countWord(words, findString));
        System.out.println("--------------------");

        CMSketch cms1 = new CMSketch(4, 16000); // 1 параметр - d; 2 параметр - w
        for (int i = 0; i < words.length; i++) {
            cms1.add(words[i]);
        }

        System.out.println("approximate result " + cms1.get(findString));

        System.out.println("--------------------"); // другой вариант размера таблицы        

        cms1 = new CMSketch(3, 21333);
        for (int i = 0; i < words.length; i++) {
            cms1.add(words[i]);
        }
        System.out.println("approximate result " + cms1.get(findString));

        System.out.println("--------------------"); // другой вариант размера таблицы

        cms1 = new CMSketch(5, 12800);
        for (int i = 0; i < words.length; i++) {
            cms1.add(words[i]);
        }
        System.out.println("approximate result " + cms1.get(findString));

//                cms1.CMTableOut(); // вывод для малых таблиц

        




        System.out.println("");
        int MaxSize = 10000; //////////////////////////////////////////////////////////////////////// <
        System.out.println("- тестирование false positives и false find для неповторяющихся строк -");
        String [] testN = new String[MaxSize];
        
        for (int i = 0; i < MaxSize; i++) { // массив неповторяющихся цифр 1 .. 1 млн
            testN[i] = String.valueOf(i);
        }
        
        System.out.println("total words " + testN.length);
        
        CMSketch cms2 = new CMSketch(4, 16000); ////////////////////////////////////////////////////// <
        for (int i = 0; i < testN.length; i++) {
            cms2.add(testN[i]);
        }
        System.out.println("тестирование false find");
        
        int ff = 0;
        for (int i = MaxSize+1; i < MaxSize*2+1; i++) { // проверка на положительный результат для незанесенных чисел в Count Min Sketch
            if (cms2.get(String.valueOf(i)) != 0) {
                ff ++;
            }
        }
        System.out.println("false find - " + ff + "  percent- " + ((float)ff / MaxSize) * 100);        
        
        System.out.println("тестирование false positives");
        int fp = 0;
        for (int i = 0; i < MaxSize; i++) { // проверка на результат != 1 для занесенных чисел в Count Min Sketch
            if (cms2.get(String.valueOf(i)) != 1) {
                fp ++;
            }
        }
        System.out.println("false positives - " + fp + "  percent- " + ((float)fp / MaxSize) * 100);  
        
    }
}
