package samples;

public class KillerMan implements Comparable<KillerMan> {
	public final String name;
	public final int age;
	public final int height;
	private Integer kills;
	
	public KillerMan() {
		name = "unknown";
		age = 0;
		height = 0;
		kills = 0;
	}
	
	public KillerMan(String name, int age, int height, Integer kills) {
		this.name = name;
		this.age = age;
		this.height = height;
		this.kills = kills;
	}
	
	public String fullInfo() {
		return String.format("name:%8s, age:%3d, height:%4d, kills:%3d", 
				name, age, height, kills);
	}

	public Integer getKills() {
		return kills;
	}

	@Override
	public int compareTo(KillerMan other) {
		return this.age < other.age ? -1 : this.age > other.age ? 1 : 0;
	}	
	
	@Override
	public String toString() {
		return String.format("%s", name);
	}	
}
