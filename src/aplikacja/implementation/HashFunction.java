package aplikacja.implementation;


public class HashFunction {

	private int numberOfBlocks;
	private int[][] tabOfMessageBlocks;
	private int[] inputArray;

	public String calculateHash(String inputHEX) {
		byte[] hexbyteArray = null;
		try {
			hexbyteArray = ToArray.toByteArray(inputHEX);
		} catch (Exception e) {
			System.out.println("Podaj parzyst¹ liczbê znaków");
		}
		inputArray = new int[hexbyteArray.length];
		for (int i = 0; i < hexbyteArray.length; i++) {
			inputArray[i] = hexbyteArray[i];
		}

		byte[] padA = pad(hexbyteArray);
		int[] padArray = new int[padA.length];
		for (int i = 0; i < padArray.length; i++) {
			padArray[i] = padA[i];
		}

		this.tabOfMessageBlocks = new int[numberOfBlocks][64];
		for (int i = 0, j = 0; i < padArray.length; i++, j++) {
			if (j == 64) {
				j = 0;
			}
			int blockNumber = i / 64;
			this.tabOfMessageBlocks[blockNumber][j] = padArray[i];
		}

		MessageBlock mb = new MessageBlock();
		int[] previousState = null;
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

		int[][] endState = CompressionFunction.prepareArray(blockState);
		System.out.println("\nHashFunction.toByteString()\n");
		for (int k = 0; k < 8; k++) {
			for (int l = 0; l < 8; l++) {
				System.out.print(String.format("%02X ", endState[k][l] & 0xFF) + " ");
			}
			System.out.println();
		}
		endState = CompressionFunction.pPermutations(endState);
		System.out.println("\ntoByteString, po pPermutations:\n");
		for (int k = 0; k < 8; k++) {
			for (int l = 0; l < 8; l++) {
				System.out.print(String.format("%02X ", endState[k][l] & 0xFF) + " ");
			}
			System.out.println();
		}
		int[][] inputState = CompressionFunction.prepareArray(blockState);
		for (int i = 0; i < endState.length; i++) {
			for (int j = 0; j < endState[0].length; j++) {
				endState[i][j] = endState[i][j] ^ inputState[i][j];
			}
		}
		System.out.println("\ntoByteString, po xorze:\n");
		for (int k = 0; k < 8; k++) {
			for (int l = 0; l < 8; l++) {
				System.out.print(String.format("%02X ", endState[k][l] & 0xFF) + " ");
			}
			System.out.println();
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
		pad.append1Bit();
		pad.calculateW();
		pad.calculateP();
		pad.add64bitRepresentationOfP();

		this.numberOfBlocks = pad.getP();
		return pad.getPaddedArrayOfBytes();
	}

}
