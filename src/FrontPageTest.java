import java.util.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;


public class FrontPageTest {
	public static void main(String[] args) {
        // Reading
		Scanner scanner = new Scanner(System.in);
		String line = scanner.nextLine();
		String[] parts = line.split(" ");
		Category[] categories = new Category[parts.length];
		for (int i = 0; i < categories.length; ++i) {
			categories[i] = new Category(parts[i]);
		}
		int n = scanner.nextInt();
		scanner.nextLine();
		FrontPage frontPage = new FrontPage(categories);
		Calendar cal = Calendar.getInstance();
		for (int i = 0; i < n; ++i) {
			String title = scanner.nextLine();
			cal = Calendar.getInstance();
            int min = scanner.nextInt();
			cal.add(Calendar.MINUTE, -min);
			Date date = cal.getTime();
			scanner.nextLine();
			String text = scanner.nextLine();
            int categoryIndex = scanner.nextInt();
            scanner.nextLine();
			TextNewsItem tni = new TextNewsItem(title, date, categories[categoryIndex], text);
			frontPage.addNewsItem(tni);
		}
        
		n = scanner.nextInt();
		scanner.nextLine();
		for (int i = 0; i < n; ++i) {
			String title = scanner.nextLine();
            int min = scanner.nextInt();
            cal = Calendar.getInstance();
			cal.add(Calendar.MINUTE, -min);
			scanner.nextLine();
			Date date = cal.getTime();
			String url = scanner.nextLine();
			int views = scanner.nextInt();
			scanner.nextLine();
            int categoryIndex = scanner.nextInt();
            scanner.nextLine();
			MediaNewsItem mni = new MediaNewsItem(title, date, categories[categoryIndex], url, views);
			frontPage.addNewsItem(mni);
		}
        // Execution
        String category = scanner.nextLine();
		System.out.println(frontPage);
        for(Category c : categories) {
            System.out.println(frontPage.listByCategory(c).size());
        }
        try {
        	System.out.println(frontPage.listByCategoryName(category).size());
        } catch(CategoryNotFoundException e) {
            System.out.println(e.getMessage());
        }
        scanner.close();
	}
}

class FrontPage {
	private List<NewsItem> newsItems;
	private List<Category> categories;
	
	public FrontPage(Category[] categories) {
		this.categories = new ArrayList<>(Arrays.asList(categories));
		newsItems = new ArrayList<>();
	}
	
	public void addNewsItem(NewsItem newsItem) {
		newsItems.add(newsItem);
	}
	
	public List<NewsItem> listByCategory(Category category) {
		return newsItems.stream()
			.filter(item -> item.category.equals(category))
			.collect(Collectors.toList());
	}
	
	public List<NewsItem> listByCategoryName(String category) {
		if (!categories.stream().anyMatch(cat -> cat.getName().equals(category)))
			throw new CategoryNotFoundException(category);
		return newsItems.stream()
				.filter(item -> item.category.getName().equals(category))
				.collect(Collectors.toList());
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (NewsItem newsItem : newsItems) {
			sb.append(newsItem.getTeaser());
		}
		return sb.toString();
	}
	
	
}


class TextNewsItem extends NewsItem {
	private String text;

	public TextNewsItem(String header, Date datePublished, Category category, String text) {
		super(header, datePublished, category);
		this.text = text;
	}

	@Override
	public String getTeaser() {
		StringBuilder sb = new StringBuilder();
		sb.append(header);
		sb.append("\n");
		sb.append(howOld());
		sb.append("\n");
		if (text.length() <= 80)
			sb.append(text);
		else 
			sb.append(text.substring(0, 80));
		sb.append("\n");
		return sb.toString();
	}
	
	
	
	
}

class MediaNewsItem extends NewsItem {
	private String url;
	private int views;
	
	public MediaNewsItem(String header, Date datePublished, Category category, String url, int views) {
		super(header, datePublished, category);
		this.url = url;
		this.views = views;
	}
	
	@Override
	public String getTeaser() {
		StringBuilder sb = new StringBuilder();
		sb.append(header);
		sb.append("\n");
		sb.append(howOld());
		sb.append("\n");
		sb.append(url);
		sb.append("\n");
		sb.append(views);
		sb.append("\n");
		return sb.toString();
	}
	
	
}

abstract class NewsItem {
	protected String header;
	protected LocalDateTime datePublished;
	protected Category category;
	
	public NewsItem(String header, Date datePublished, Category category) {
		this.header = header;
		this.datePublished = datePublished.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		this.category = category;
	}
	
	public int howOld() {
		long res = ChronoUnit.MINUTES.between(datePublished, LocalDateTime.now());
		return (int) res;
	}
	
	abstract public String getTeaser();
}

class Category {
	private String name;
	
	public Category(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean equals(Category other) {
		if (other == null)
			return false;
		if (this == other)
			return true;
		return name.equals(other.getName());
	}
}

class CategoryNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private String categoryName;
	
	public CategoryNotFoundException() {
		super();
		categoryName = "";
	}
	
	public CategoryNotFoundException(String categoryName) {
		this.categoryName = categoryName;
	}
	
	public String getMessage() {
		return String.format("Category %s was not found", categoryName);
	}
	
}
