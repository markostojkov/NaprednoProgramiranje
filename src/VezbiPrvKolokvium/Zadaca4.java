import java.util.*;
import java.util.concurrent.TimeUnit;

class FrontPageTest {
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
    }
}

class FrontPage {
    private List<NewsItem> newsItems;
    private List<Category> categories;

    public FrontPage(Category[] categories) {
        this.newsItems = new ArrayList<>();
        this.categories = new ArrayList<>();

        this.categories.addAll(Arrays.asList(categories));
    }

    public void addNewsItem(NewsItem newsItem) {
        this.newsItems.add(newsItem);
    }

    public List<NewsItem> listByCategory(Category category) {
        List<NewsItem> itemsInCategory = new ArrayList<>();
        for (NewsItem item: this.newsItems) {
            if (item.getCategory().equals(category)) itemsInCategory.add(item);
        }

        return itemsInCategory;
    }

    public List<NewsItem> listByCategoryName(String category) throws CategoryNotFoundException {
        List<NewsItem> items = this.listByCategory(new Category(category));

        if (items.isEmpty()) throw new CategoryNotFoundException(String.format("Category %s was not found", category));
        return items;
    }

    @Override
    public String toString() {
        StringBuilder teasers = new StringBuilder();

        for (NewsItem item: this.newsItems) {
            teasers.append(item.getTeaser() + "\n");
        }

        return teasers.toString();
    }
}

class Category {
    private String name;

    public Category(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Category category = (Category) o;
        return this.name.equalsIgnoreCase(category.getName());
    }
}

abstract class NewsItem {
    protected String headline;
    protected Date dateOfPublish;
    protected Category category;

    public NewsItem(String headline, Date dateOfPublish, Category category) {
        this.headline = headline;
        this.dateOfPublish = dateOfPublish;
        this.category = category;
    }

    public Category getCategory() {
        return category;
    }

    public String getTeaser() {
        StringBuilder teaser = new StringBuilder();
        teaser.append(this.headline + "\n");
        teaser.append(TimeUnit.MILLISECONDS.toMinutes(new Date().getTime() - this.dateOfPublish.getTime()) + "\n");

        return teaser.toString();
    }
}

class TextNewsItem extends NewsItem {
    private String text;

    public TextNewsItem(String headline, Date dateOfPublish, Category category, String text) {
        super(headline, dateOfPublish, category);
        this.text = text;
    }

    @Override
    public String getTeaser() {
        StringBuilder teaser = new StringBuilder(super.getTeaser());

        if (this.text.length() <= 80) teaser.append(this.text);
        else teaser.append(this.text.substring(0, 80));

        return teaser.toString();
    }
}

class MediaNewsItem extends NewsItem {
    private int views;
    private String url;

    public MediaNewsItem(String headline, Date dateOfPublish, Category category, String url, int views) {
        super(headline, dateOfPublish, category);
        this.url = url;
        this.views = views;
    }

    @Override
    public String getTeaser() {
        StringBuilder teaser = new StringBuilder(super.getTeaser());
        teaser.append(this.url + "\n");
        teaser.append(this.views);
        return teaser.toString();
    }
}

class CategoryNotFoundException extends Exception {
    public CategoryNotFoundException(String message) {
        super(message);
    }
}