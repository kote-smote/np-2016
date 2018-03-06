import java.util.TreeMap;

/**
 * Napreno Programiranje - Lab 01 - Zadaca 1
 * 
 * Da se napise metod koj ke prima eden cel broj i ke ja pecati negovata 
 * reprezentacija kako rimski broj.
 * Vlez: 1998 
 * Izlez: MCMXCVIIII
 */
public class RomanConverterTest {
    
	public static void main(String[] args) {                                   
        int[] input = { 
    		746, 
    		6763, 
    		594, 
    		890, 
    		9666, 
    		6741, 
    		8062, 
    		3368, 
    		9601, 
    		5351, 
    		2737, 
    		1232, 
    		993, 
    		3686, 
    		9741, 
    		2886, 
    		7219, 
    		4321, 
    		2955, 
    		6054, 
    		5956, 
    		6680, 
    		7384, 
    		4130, 
    		6770 
        };

		System.out.println("RomanConverterBasic Test:");
        for (int decimal : input) 
        	System.out.println(RomanConverterBasic.toRoman(decimal)); 
        
        System.out.println();
        
        System.out.println("RomanConverter Test:");
        for (int decimal : input) 
        	System.out.println(RomanConverter.toRoman(decimal)); 
	}
}

// Using TreeMap
class RomanConverter {
	private static final TreeMap<Integer, String> map 
		= new TreeMap<Integer, String>();
	
	static {
		map.put(1000, "M");
        map.put(900, "CM");
        map.put(500,  "D");
        map.put(400, "CD");
        map.put(100,  "C");
        map.put(90,  "XC");
        map.put(50,   "L");
        map.put(40,  "XL");
        map.put(10,   "X");
        map.put(9,   "IX");
        map.put(5,    "V");
        map.put(4,   "IV");
        map.put(1,    "I");
        map.put(0,     "");
	}
		
	/**
     * Roman to decimal converter
     *
     * @param number in decimal format
     * @return string representation of the number in Roman numeral
     */
	public static String toRoman(int number) {
		int l = map.floorKey(number);
		if ( number == 0 ) return map.get(number);
		return map.get(l) + toRoman(number - l);
	}
}

class RomanConverterBasic {
    private static final String[] ROMANS    
    	= { "M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
    private static final Integer[] DECIMALS 
    	= {1000,  900, 500,  400, 100,   90,  50,   40,  10,    9,   5,    4,  1 };
    private static int SIZE = ROMANS.length;
   
        
    /**
     * Finds the index of the closest number smaller than n,
     * in the static String array containing basic the Roman Numerals
     * 
     * @param n number 
     * @return int index of closest number smaller than n
     */
    private static int floorNumIndex(int n) {
        int result = 0;
        for (int i = 0; i < SIZE; i++) {
            result = i;
    		if (n >= DECIMALS[i])  
    			break;
        }
        return result;
    }
    
    /**
     * Roman to decimal converter
     *
     * @param n number in decimal format
     * @return string representation of the number in Roman numeral
     */
    public static String toRoman(int n) {
    	int index = floorNumIndex(n); // index of the closest number smaller than n
    	int floorNumber = DECIMALS[index]; // Find the closest number smaller than n
    	if (n == 0) return ""; // not if(n==1) because that case can be skipped 
    	return ROMANS[index] + toRoman(n-floorNumber); 
    }
}
