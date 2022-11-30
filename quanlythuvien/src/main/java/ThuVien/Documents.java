package ThuVien;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;

import Polyfill.PFArray;
import Polyfill.StringHelper;
import Polyfill.ThoiGian;

public class Documents extends Management<Document> {
    private static final Logger LOGGER = Logger.getLogger(Documents.class.getName());

    public Documents() {
        super();
    }

    public Documents(Document[] r) {
        super(r);
    }

    public Documents(PFArray<String[]> blob) {
        blob.stream().forEach(e -> instance.push_back(switch (Integer.parseInt(e[0])) {
            case 1 -> Newspaper.fromBlob(e);
            case 2 -> NativeBook.fromBlob(e);
            case 3 -> ForeignNontranslatedBook.fromBlob(e);
            case 4 -> ForeignTranslatedBook.fromBlob(e);
            default -> {
                LOGGER.severe(String.format("File read Document Blob type out of range"));
                yield null;
            }
        }));
        updateCounter();
    }

    public int menuEdit() {
        return StringHelper.acceptInput("Ten  sach", "Nam xuat ban", "So luong ban sao");
    }

    @Override
    public Document add() {
        Document document = null;
        int n = StringHelper.acceptInput("Bao", "Sach trong nuoc",
                "Sach dich", "Sach chua dich");
        switch (n) {
            case 1 -> document = new Newspaper(genNextId());
            case 2 -> document = new NativeBook(genNextId());
            case 3 -> document = new ForeignTranslatedBook(genNextId());
            case 4 -> document = new ForeignNontranslatedBook(genNextId());
        }

        document.setName(StringHelper.acceptLine("Nhap ten sach: "));

        int soLuongTacGia = Integer.parseInt(StringHelper.acceptLine("Nhap so luong tac gia: "));
        Author[] authors = new Author[soLuongTacGia];
        IntStream.range(0, authors.length).forEach(i -> {
            int index = authors_instance.promptSearch();
            if (index == -1) {
                System.out.println("Khong tim thay tac gia: ");
                int p = StringHelper.acceptInput("Them tac gia moi"
                        , "Nhap tac gia khac");
                switch (p) {
                    case 1 -> authors_instance.add();
                }
            } else {
                authors[i] = authors_instance.instance.at(index);
            }
        });
        document.setAuthors(authors);

        document.setPublication(ThoiGian.parseTG(StringHelper.acceptLine("Nhap thoi gian xuat ban")));

        document.setCopies(Integer.parseInt(StringHelper.acceptLine("Nhap so luong ban sao: ")));

        return document;
    }

    public int promptSearch() {
        int n;
        try {
            n = search(Integer.parseInt(StringHelper.acceptLine("Nhap ma tai lieu: ")));
            if (n == -1) {
                System.out.println("Tim kiem khong co ket qua: ");
            } else {
                System.out.println("Tim thay tai lieu: ");
                instance.at(n).toString();
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Input error", e);
            throw e;
        }
        return n;
    }

    @Override
    public Document remove() {
        int n;
        Document document = null;
        try {
            n = promptSearch();
            if (n == -1) {
                System.out.println("Tim kiem that bai, remove tai lieu khong thanh cong");
            } else {
                System.out.println("Xac nhan xoa tai lieu: ");
                instance.at(n).toString();
                int m = StringHelper.acceptInput("Co", "Suy nghi lai");
                if (m == 1) {
                    document = instance.erase(n);
                    System.out.println("Da xoa thu ngan");
                    document.toString();
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Co loi xay ra, remove tai lieu that bai", e);
        }
        return document;
    }

    @Override
    public Document edit() {
        int n;
        Document document = null;
        try {
            n = promptSearch();
            if (n == -1) {
                System.out.println("Tim kiem that bai, edit tai lieu that bai");
            } else {
                int m;
                do {
                    document = instance.at(n);
                    System.out.println("Dang thao tac edit tai lieu: ");
                    document.toString();
                    System.out.println("Chon thao tac: ");
                    switch (m = StringHelper.acceptInput("Ten", "Ngay xuat ban", "So luong ban sao")) {
                        case 1 -> document.setName(StringHelper.acceptLine("Nhap ten gia: "));
                        case 2 ->
                                document.setPublication(ThoiGian.parseTG(StringHelper.acceptLine("Nhap ngay xuat ban: ")));
                        case 3 ->
                                document.setCopies(Integer.parseInt(StringHelper.acceptLine("Nhap so luong ban sao: ")));
                        default -> {
                            m = -1;
                            System.out.println("Ket thuc edit tai lieu");
                        }
                    }
                } while (m >= 0);
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Co loi xay ra, edit tai lieu khong thanh cong", e);
            throw e;
        }
        return document;
    }

    @Override
    public int[] search() {
        throw new UnsupportedOperationException("Chuc nang nay chua code xong do khong du thoi gian");
    }

//    @Override
//    public int[] search() {
//        // TODO Auto-generated method stub
//        return null;
//    }

    private PFArray<Document> instance;

    private Authors authors_instance;

    public static final class Type {
        public static final int MAGAZINE = 1;
        public static final int NATIVE_BOOK = 2;
        public static final int FOREIGN_TRANSLATED_BOOK = 3;
        public static final int FOREIGN_NONTRANSLATED_BOOK = 4;
    }

    public static class Instance {
        public static final Newspaper newspaper = new Newspaper(0);
        public static final NativeBook nativeBook = new NativeBook(0);
        public static final ForeignNontranslatedBook foreignNontranslatedBook = new ForeignNontranslatedBook(0);
        public static final ForeignTranslatedBook foreigntranslatedBook = new ForeignTranslatedBook(0);
    }
}
