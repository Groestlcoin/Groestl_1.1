package aplikacja.implementation;

public class State {

	private String key;
	private int[][] array;
	
	public State(String key, int[][] array) {
		this.key = key;
		this.array = array;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public int[][] getArray() {
		return array;
	}
	public void setArray(int[][] array) {
		this.array = array;
	}
	
}
