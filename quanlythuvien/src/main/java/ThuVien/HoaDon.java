package ThuVien;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import Polyfill.PFArray;
import Polyfill.StringHelper;
import Polyfill.ThoiGian;

public class HoaDon extends VirtualHoaDon implements IDataProcess<HoaDon> {
    private static final Logger LOGGER = Logger.getLogger(HoaDon.class.getName());

    public HoaDon(int id, PFArray<Document> borrows, Reader creator) {
        super(id, creator);
        setBorrows(borrows);
    }

    public HoaDon(int id, VirtualHoaDon vhd) {
        this(id, vhd.getBorrows(), vhd.getCreator());
    }

    protected HoaDon setDeadline(ThoiGian deadline) {
        this.deadline = deadline;
        return this;
    }

    public ThoiGian getDeadline() {
        return deadline;
    }

    public PFArray<Document> getHoldings() {
        return holdings;
    }

    protected HoaDon setHoldings(PFArray<Document> returns) {
        this.holdings = returns;
        return this;
    }

    public HoaDon edit() {
        if (holdings.size() <= 0) {
            System.out.println("Hoa don nay da ket thuc");
        }
        try {
            switch (StringHelper.acceptInput("Tra tai lieu", "Mat tai lieu")) {
                case 1 -> {
                    System.out.println("Chon tai lieu tra (nhap all de tra het");
                    IntStream.range(0, holdings.size())
                            .forEach(i -> System.out.println(String.format("%d. %s", i, holdings.at(i).toString())));
                    String input = Global.scanner.nextLine().trim().toLowerCase();
                    if (input.equals("all")) {
                        holdings.stream().forEach(e -> e.returns());
                        holdings.clear();
                        System.out.println("Da tra het tai lieu");
                    } else {
                        Document d = holdings.erase(Integer.parseInt(StringHelper.acceptLine("Nhap stt tai lieu tra")));
                        d.returns();
                        System.out.println("Tai lieu vua tra");
                        System.out.println(d.toString());
                    }
                }
                case 2 -> {
                    System.out.println("Chon tai lieu mat (nhap all la mat het");
                    IntStream.range(0, holdings.size())
                            .forEach(i -> System.out.println(String.format("%d. %s", i, holdings.at(i).toString())));
                    String input = Global.scanner.nextLine().trim().toLowerCase();
                    if (input.equals("all")) {
                        holdings.stream().forEach(e -> e.lost());
                        holdings.clear();
                        System.out.println("Da xu ly mat het tai lieu");
                    } else {
                        Document d = holdings.erase(Integer.parseInt(StringHelper.acceptLine("Nhap stt tai lieu mat")));
                        d.lost();
                        System.out.println("Tai lieu vua mat");
                        System.out.println(d.toString());
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Input error", e);
            throw e;
        }
        return this;
    }

    public String[] toBlob() {
        return new String[] { String.valueOf(getId()), String.valueOf(getCreator().getId()),
                StringHelper.lv1Join(getBorrows().stream().map(Document::toBlob).toArray()),
                StringHelper.lv1Join(getHoldings().stream().map(Document::toBlob).toArray()),
                getDeadline().toString() };
    }

    public static HoaDon fromBlob(String[] inp) {
        int id = Integer.parseInt(inp[0]);
        Reader creator = Global.readers.getById(Integer.parseInt(inp[1]));
        PFArray<Document> borrows = new PFArray<>();
        Stream.of(StringHelper.lv1Split(inp[2])).forEach(e -> Global.readers.getById(Integer.parseInt(e)));
        PFArray<Document> returns = new PFArray<>();
        Stream.of(StringHelper.lv1Split(inp[3])).forEach(e -> Global.readers.getById(Integer.parseInt(e)));
        ThoiGian deadline = ThoiGian.parseTG(inp[4]);
        HoaDon __ = new HoaDon(id, borrows, creator);
        __.setHoldings(returns).setDeadline(deadline);
        return __;
    }

    private ThoiGian deadline;
    private PFArray<Document> holdings = new PFArray<>();
}