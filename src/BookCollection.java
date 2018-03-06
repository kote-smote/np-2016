import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by martin on 12/27/16.
 */
public class BookCollection {
    Map<String, TreeSet<Book>> booksByCat;

    public BookCollection() {
        this.booksByCat = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    }

    public void addBook(Book book) {
        String category = book.getCategory();
        this.booksByCat.computeIfPresent(category, (key, books) -> {
            books.add(book);
            return books;
        });
        TreeSet<Book> ls = this.booksByCat.computeIfAbsent(category,
                key -> new TreeSet<Book>());
        if (ls != null)
            ls.add(book);
    }

    public void printByCategory(String category) {
        this.booksByCat.get(category).forEach(System.out::println);
    }

    public List<Book> getCheapestN(int n) {
        List<Book> ls = new ArrayList<>();

        for (TreeSet<Book> s : this.booksByCat.values())
            ls.addAll(s.stream().collect(Collectors.toList()));

        ls.sort(Comparator.comparing(Book::getPrice).thenComparing(Book::getTitle));

        if (ls.size() < n)
            return ls;
        return ls.subList(0, n);
    }
}

class Book implements Comparable<Book> {
    private String title;
    private String category;
    private float price;

    public Book(String title, String category, float price) {
        this.title = title;
        this.category = category;
        this.price = price;
    }

    @Override
    public int compareTo(Book other) {
        if (this.title.compareTo(other.title) == 0)
            return Float.compare(this.price, other.price);
        return this.title.compareTo(other.title);
    }

    @Override
    public String toString() {
        return String.format("%s (%s) %.2f", title, category, price);
    }

    public float getPrice() {
        return this.price;
    }

    public String getTitle() {
        return this.title;
    }

    public String getCategory() {
        return this.category;
    }

}
