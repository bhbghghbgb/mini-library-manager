package ThuVien;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

import Polyfill.StringHelper;

public class Global {
    public static Authors authors;
    public static Cards cards;
    public static Readers readers;
    public static Documents documents;
    public static Cashiers cashiers;
    public static Managers managers;
    public static HoaDons hoadons;
    public static Owner trandan;
    public static final int ratePerDay = 5000;
    public static final Scanner scanner = new Scanner(System.in);
    public static final Random random = new Random();

    public static void identityLookup() {//Dùng cho tìm kiếm
        System.out.println("Select an identity to lookup:");
        switch (StringHelper.acceptInput("Authors", "Documents", "Readers", "Cashiers", "Managers", "HoaDons")) {
            case 1 -> Arrays.stream(authors.search()).forEach(e -> System.out.println(e));
            case 2 -> Arrays.stream(documents.search()).forEach(e -> System.out.println(e));
            case 3 -> readers.search();
            case 4 -> cashiers.search();
            case 5 -> managers.search();
        }
    }
}
