import java.util.*;

class ArchiveStoreTest {
    public static void main(String[] args) {
        ArchiveStore store = new ArchiveStore();
        Date date = new Date(113, 10, 7);
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        int n = scanner.nextInt();
        scanner.nextLine();
        scanner.nextLine();
        int i;
        for (i = 0; i < n; ++i) {
            int id = scanner.nextInt();
            long days = scanner.nextLong();
            Date dateToOpen = new Date(date.getTime() + (days * 24 * 60
                    * 60 * 1000));
            LockedArchive lockedArchive = new LockedArchive(id, dateToOpen);
            store.archiveItem(lockedArchive, date);
        }
        scanner.nextLine();
        scanner.nextLine();
        n = scanner.nextInt();
        scanner.nextLine();
        scanner.nextLine();
        for (i = 0; i < n; ++i) {
            int id = scanner.nextInt();
            int maxOpen = scanner.nextInt();
            SpecialArchive specialArchive = new SpecialArchive(id, maxOpen);
            store.archiveItem(specialArchive, date);
        }
        scanner.nextLine();
        scanner.nextLine();
        while(scanner.hasNext()) {
            int open = scanner.nextInt();
            try {
                store.openItem(open, date);
            } catch(NonExistingItemException e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println(store.getLog());
    }
}

class ArchiveStore {
    private List<Archive> archiveList;
    private List<String> archiveLogsList;

    public ArchiveStore() {
        this.archiveList = new ArrayList<>();
        this.archiveLogsList = new ArrayList<>();
    }

    public void archiveItem(Archive item, Date date) {
        archiveList.add(item);
        archiveLogsList.add(item.archiveItem(date));
    }

    public void openItem(int id, Date date) throws NonExistingItemException {
        for (Archive archive: this.archiveList) {
            if (archive.getId() == id) {
                archiveLogsList.add(archive.openArchive(date));
                return;
            }
        }

        throw new NonExistingItemException(String.format("Item with id %d doesn't exist", id));
    }

    public String getLog() {
        StringBuilder allLogsInString = new StringBuilder();
        for (String s: this.archiveLogsList) {
            allLogsInString.append(s + "\n");
        }

        return allLogsInString.toString();
    }
}

abstract class Archive {
    protected int id;
    protected Date dateArchived;

    public Archive(int id) {
        this.id = id;
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        this.dateArchived = new Date();
    }

    public int getId() {
        return id;
    }

    protected String openArchive(Date date) {
        return String.format("Item %d opened at %s", this.id, date.toString());
    }

    protected String archiveItem(Date date) {
        return String.format("Item %d archived at %s", this.id, date.toString());
    }
}

class LockedArchive extends Archive {
    private Date dateToOpen;

    public LockedArchive(int id, Date dateToOpen) {
        super(id);
        this.dateToOpen = dateToOpen;
    }

    @Override
    public String openArchive(Date date) {
        if (date.before(this.dateToOpen)) {
            return String.format("Item %d cannot be opened before %s", this.id, this.dateToOpen.toString());
        }
        return super.openArchive(date);
    }
}

class SpecialArchive extends Archive {
    private int maxOpen;
    private int canOpenIsNotZero;

    public SpecialArchive(int id, int maxOpen) {
        super(id);
        this.maxOpen = maxOpen;
        this.canOpenIsNotZero = maxOpen;
    }

    public String openArchive(Date date) {
        if (canOpenIsNotZero == 0) {
            return String.format("Item %d cannot be opened more than %d times", this.id, this.maxOpen);
        }
        this.canOpenIsNotZero--;
        return super.openArchive(date);
    }
}

class NonExistingItemException extends Exception {
    public NonExistingItemException(String message) {
        super(message);
    }
}