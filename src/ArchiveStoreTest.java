import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
//import java.time.LocalDate;
//import java.time.ZoneId;

public class ArchiveStoreTest {
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
		scanner.close();
	}
}


abstract class Archive {
	private  int id;
	protected Date dateArchived;
  //protected LocalDate dateArchived;
	
	public Archive(int id) {
		this.id = id;
		dateArchived = null;
	}
	
	public void setDate(Date date) {
		dateArchived = date;
	  //dateArchived = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}
	
	public int getId() {
		return id;
	}
	
	abstract public String tryToOpen(Date date);
	
	public boolean equals(Archive other) {
		if (other == null)
			return false;
		if (this == other)
			return true;
		return this.id == other.id;
	}
}

class LockedArchive extends Archive {
	private Date dateToOpen;
  //private LocalDate dateToOpen;
	
	public LockedArchive(int id, Date dateToOpen) {
		super(id);
		this.dateToOpen = dateToOpen;
      //this.dateToOpen = dateToOpen.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}

	@Override
	public String tryToOpen(Date date) {
        // LocalDate tryDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		if(date.after(dateToOpen)) // if (tryDate.isAfter(dateToOpen))
			return String.format("Item %s opened at %s", 
                                 getId(), date.toString().replace("GMT", "UTC"));
		else 
            return String.format("Item %s cannot be opened before %s", 
                                 getId(), dateToOpen.toString().replace("GMT", "UTC"));
	}
	
}

class SpecialArchive extends Archive {
	public final int maxOpen;
	private int timesOpened;
	
	public SpecialArchive(int id, int maxOpen) {
		super(id);
		this.maxOpen = maxOpen;
		timesOpened = 0;
	}

	@Override
	public String tryToOpen(Date date) {
		if (timesOpened < maxOpen) {
			timesOpened++;
			return String.format("Item %s opened at %s", 
                                 getId(), date.toString().replace("GMT", "UTC"));
		} else 
            return String.format("Item %s cannot be opened more than %s times", 
                                 getId(), maxOpen);
	}

}

class ArchiveStore {
	private List<Archive> archives;
	private List<String> log;
	
	public ArchiveStore() {
		archives = new ArrayList<>();
		log = new ArrayList<>();
	}
	
	public void archiveItem(Archive item, Date date) {
		item.setDate(date);
		archives.add(item);
		log.add(String.format("Item %s archived at %s",
                             item.getId(), date.toString().replace("GMT", "UTC")));
	}
	
	public void openItem(int id, Date date) {
		if (!archives.stream().anyMatch(a-> a.getId() == id)) 
			throw new NonExistingItemException("Item with id " + id + " doesn't exist");
		
		for (Archive archive : archives) {
			if (archive.getId() == id) {
				log.add(archive.tryToOpen(date));
				return;
			}
		}	
	}
	
	public String getLog() {
		StringBuilder sb = new StringBuilder();
		for (String logEvent : log) {
			sb.append(logEvent);
			sb.append("\n");
		}
		return sb.toString();
	}
	
}

class NonExistingItemException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	String msg;
	
	public NonExistingItemException() {
		super();
	}
	
	public NonExistingItemException(String msg) {
		this.msg = msg;
	}
	
	public String getMessage() {
		return msg;
	}
}