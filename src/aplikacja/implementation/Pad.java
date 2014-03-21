package aplikacja.implementation;

import java.nio.ByteBuffer;

public class Pad {

	private byte[] hexByteArray;

	private int w;

	private byte[] hexByteArrayAppended;

	private int p;

	public int getP() {
		return p;
	}

	private byte[] paddedArrayOfBytes;

	public Pad(byte[] hexByteArray) {
		this.hexByteArray = hexByteArray;
	}

	public void printPlainByteArray() {
//		for (int i = 0; i < hexByteArray.length; i++) {
//			System.out.print(hexByteArray[i] + " ");
//		}
//		System.out.println();
//		System.out.println(this.getClass().getCanonicalName() + "\thexByteArray.length: " + hexByteArray.length);
	}

	public void append1Bit() {
		byte[] tempByteArray = new byte[this.hexByteArray.length + 1];
		for (int i = 0; i < this.hexByteArray.length; i++) {
			tempByteArray[i] = this.hexByteArray[i];
		}
		tempByteArray[tempByteArray.length - 1] = ToArray.toByteArray("80")[0];
		this.hexByteArrayAppended = tempByteArray;
	}

	public void printPlainByteArray1() {
//		for (int i = 0; i < hexByteArrayAppended.length; i++) {
//			System.out.print(hexByteArrayAppended[i] + " ");
//		}
//		System.out.println();
//		System.out.println(this.getClass().getCanonicalName() + "\thexByteArrayAppended.length: " + hexByteArrayAppended.length);
	}

	public void calculateW() {
		this.w = (0 - this.hexByteArrayAppended.length * 8) + (447);
		while (this.w < 0) {
			this.w += 512;
		}
//		System.out.println("w: " + w);
	}

	public void calculateP() {
		this.p = ((this.hexByteArrayAppended.length * 8) + this.w + 65) / 512;
//		System.out.println("p: " + p);
	}

	public void add64bitRepresentationOfP() {
		byte[] pRepresentation = ByteBuffer.allocate(8).putLong(this.p).array();
		byte[] tempArrayOfBytes = new byte[(int) (this.p * 64)];
		for (int i = 0; i < this.hexByteArrayAppended.length; i++) {
			tempArrayOfBytes[i] = this.hexByteArrayAppended[i];
		}
		for (int i = pRepresentation.length - 1, j = tempArrayOfBytes.length - 1; i >= 0; i--, j--) {
			tempArrayOfBytes[j] = pRepresentation[i];
		}
//		for (int i = 0; i < tempArrayOfBytes.length; i++) {
//			System.out.print(tempArrayOfBytes[i] + " ");
//		}
//		System.out.println();
//		System.out.println(this.getClass().getCanonicalName() + "\ttempArrayOfBytes.length: " + tempArrayOfBytes.length);
		this.paddedArrayOfBytes = tempArrayOfBytes;
	}

	public byte[] getPaddedArrayOfBytes() {
		return paddedArrayOfBytes;
	}
}
