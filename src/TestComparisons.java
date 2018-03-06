import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import samples.KillerMan;

public class TestComparisons {
	
	public static void main(String[] args) {
		
		List<KillerMan> men = new ArrayList<>(Arrays.asList(
							new KillerMan("David", 17, 194, 7),
							new KillerMan("Marko", 24, 203, 3),
							new KillerMan("Hari", 67, 178, 2),
							new KillerMan("Nikola", 12, 154, 5)
						));
		men.stream().forEach(x -> System.out.println(x.fullInfo()));
		System.out.println();
		
		List<KillerMan> ls1 = new ArrayList<>(men); 
		Collections.sort(ls1); // uses the compareTo method from KillerMan
		System.out.println("sorted by age:");
		System.out.println(ls1);
		
		ls1 = new ArrayList<>(men);
		Collections.sort(ls1, new HeightComparator());
		System.out.println("sorted by height:");
		System.out.println(ls1);
		
		// using lambda
		ls1 = new ArrayList<>(men);
		Collections.sort(ls1, (m1, m2) -> m1.name.compareTo(m2.name));
		System.out.println("sorted by name:");
		System.out.println(ls1);
		
		ls1 = new ArrayList<>(men);
		ls1 = ls1.stream()
				.sorted((m1, m2) -> m2.getKills().compareTo(m1.getKills()))
				.collect(Collectors.toList());
		System.out.println("sorted by getKills():");
		System.out.println(ls1);
		
		// find deadliest using Stream.max(Comparator)
		ls1 = new ArrayList<>(men);
		KillerMan deadliest = (KillerMan)ls1.stream()
						.max(Comparator.comparing(KillerMan::getKills))
						.orElse(new KillerMan());
		System.out.println("deadliest: " + deadliest);		
	}
}

class HeightComparator implements Comparator<KillerMan> {

	@Override
	public int compare(KillerMan m1, KillerMan m2) {
		return m1.height < m2.height ? -1 : m1.height > m2.height ? 1 : 0;
	}
	
}
