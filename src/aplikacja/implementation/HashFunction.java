package aplikacja.implementation;

public class HashFunction {

	private int numberOfBlocks;
	private int[][] tabOfMessageBlocks;
	private int[] inputArray;

	public String calculateHash(byte[] inputHEX) {
		byte[] hexbyteArray = inputHEX;
		inputArray = new int[hexbyteArray.length];
		for (int i = 0; i < hexbyteArray.length; i++) {
			inputArray[i] = hexbyteArray[i];
		}

		byte[] padA = pad(hexbyteArray);				//wywolanie funkcji pad, ktora uzupelnia dane do wielokrotnosci 512bitow
		int[] padArray = new int[padA.length];
		for (int i = 0; i < padArray.length; i++) {
			padArray[i] = padA[i];
		}

		//dzielenie na 512bitowe bloki
		this.tabOfMessageBlocks = new int[numberOfBlocks][64];
		for (int i = 0, j = 0; i < padArray.length; i++, j++) {
			if (j == 64) {
				j = 0;
			}
			int blockNumber = i / 64;
			this.tabOfMessageBlocks[blockNumber][j] = padArray[i];
		}

		//calculateMessageBlock - dzia³ania na bloku
		MessageBlock mb = new MessageBlock();
		int[] previousState = null;
		System.out.println("=======================================tabOfMessageBlocks.length: " + tabOfMessageBlocks.length);
		for (int i = 0; i < tabOfMessageBlocks.length; i++) {
			if (i == 0) {
				previousState = mb.calculateMessageBlock(Utility.representation256, tabOfMessageBlocks[i], i);
			} else {
				previousState = mb.calculateMessageBlock(previousState, tabOfMessageBlocks[i], i);
			}
		}

		return toByteString(previousState);

	}

	
	private String toByteString(int[] blockState) {

		CompressionFunction.blockNumber++;

		int[][] endState = CompressionFunction.prepareArray(blockState);

		//output transformation
		endState = CompressionFunction.pPermutations(endState, true);

		int[][] inputState = CompressionFunction.prepareArray(blockState);
		for (int i = 0; i < endState.length; i++) {
			for (int j = 0; j < endState[0].length; j++) {
				endState[i][j] = endState[i][j] ^ inputState[i][j];
			}
		}

		StringBuilder sb = new StringBuilder();
		for (int i = 4; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				sb.append(String.format("%02X ", (endState[j][i] & 0xFF)));
			}
		}

		return sb.toString();
	}

	private byte[] pad(byte[] hexbyteArray) {

		Pad pad = new Pad(hexbyteArray);
		pad.append1Bit();							//dodanie 1 bitu "1"
		pad.calculateW();							
		pad.calculateP();
		pad.add64bitRepresentationOfP();			//dodanie 64bitowej reprezentacji z liczby p - która jest liczb¹ bloków

		this.numberOfBlocks = pad.getP();
		return pad.getPaddedArrayOfBytes();			// zwraca uzupe³nion¹ tablicê zawieraj¹c¹ wielokrotnoœæ 512 bitów
	}

}
