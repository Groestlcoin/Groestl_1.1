package aplikacja.implementation;

public class MessageBlock {

	public int[] calculateMessageBlock(int[] padArray, int[] messageblock, int i) {
		
		CompressionFunction cf = new CompressionFunction();
		int[] output = cf.calculate(padArray, messageblock, i);
		
		return output;
	}
	

}
