import java.util.List;
import java.util.ArrayList;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.time.format.DateTimeFormatter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;

public class SubtitleTest {
	public static void main(String[] args) {
		Subtitle subtitle = new Subtitle();
		int n = subtitle.loadSubtitle(System.in);
		System.out.println("+++++ ORIGINIAL SUBTITLES +++++");
		subtitle.print();
		int shift = n * 37;
        shift = (shift % 2 == 1) ? -shift : shift;
		System.out.println(String.format("SHIFT FOR %d ms", shift));
		subtitle.shift(shift);
		System.out.println("+++++ SHIFTED SUBTITLES +++++");
		subtitle.print();
	}
}

class Subtitle {
	List<SubtitleItem> items;
	
	public Subtitle(){
		items = new ArrayList<>();
	}

	public int loadSubtitle(InputStream inputStream){
		int readedElements = 0;
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))){
            String line = "";
			while(line != null) {
                // reading the number
                line = reader.readLine();
				int number = Integer.parseInt(line.trim());
                // reading the times
				line = reader.readLine();
				String[] parts = line.split(" --> ");
				String startTime = parts[0];
				String endTime = parts[1];
                // reading the text
				StringBuilder text = new StringBuilder();
                line = reader.readLine();
				while(line != null && !line.isEmpty()) {
					text.append(line);
                    text.append("\n");
                    line = reader.readLine();
				}
                
				SubtitleItem item = new SubtitleItem(number, startTime, endTime, text.toString());
				items.add(item);
				readedElements++;
			}
			
		}catch(IOException e){
			System.out.println(e.getMessage());
		}
		return readedElements;
	}

	void print() {
		for (SubtitleItem item : items) 
			System.out.println(item);
	}

	void shift(long millis){
		for (SubtitleItem item : items) {          
           	item.startTime = item.startTime.plus(millis, ChronoUnit.MILLIS);
            item.endTime = item.endTime.plus(millis, ChronoUnit.MILLIS);
        }
	}

	class SubtitleItem {
		long number;
		String text;
		LocalTime startTime,
				 	endTime;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss,SSS");

		public SubtitleItem(long number, String startTime, String endTime, String text) {
			this.number = number;
			this.startTime = LocalTime.parse(startTime, this.formatter);
			this.endTime = LocalTime.parse(endTime, this.formatter);
			this.text = text;
		}

		public String toString() {
			return String.format("%d\n%s --> %s\n%s", 
				this.number,
				this.startTime.format(this.formatter), 
				this.endTime.format(this.formatter), 
				this.text);
		}
	}
}