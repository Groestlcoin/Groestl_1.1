package aplikacja.implementation;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import aplikacja.gui.NewGui;

public class CompressionFunction {

	private final static int[][] PROUNDCONSTANT = { { 0, 16, 32, 48, 64, 80, 96, 112 }, { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 } };
	private final static int[][] QROUNDCONSTANT = { { 255, 255, 255, 255, 255, 255, 255, 255 }, { 255, 255, 255, 255, 255, 255, 255, 255 },
			{ 255, 255, 255, 255, 255, 255, 255, 255 }, { 255, 255, 255, 255, 255, 255, 255, 255 }, { 255, 255, 255, 255, 255, 255, 255, 255 },
			{ 255, 255, 255, 255, 255, 255, 255, 255 }, { 255, 255, 255, 255, 255, 255, 255, 255 }, { 255, 239, 223, 207, 191, 175, 159, 143 } };
	private int[][] him1;
	private int[][] mi;
	private int[][] xoredHM;
	private static Map<String, Integer> indexMap;
	private int[][] outputp;
	private int[][] outputq;
	private static int blockNumber;

	public int[] calculate(int[] padArray, int[] messageblock, int i2) {

		CompressionFunction.blockNumber = i2;
		this.him1 = prepareArray(padArray);
		this.mi = prepareArray(messageblock);

		indexMap = new HashMap<String, Integer>();
		indexMap.put("0", 0);
		indexMap.put("1", 1);
		indexMap.put("2", 2);
		indexMap.put("3", 3);
		indexMap.put("4", 4);
		indexMap.put("5", 5);
		indexMap.put("6", 6);
		indexMap.put("7", 7);
		indexMap.put("8", 8);
		indexMap.put("9", 9);
		indexMap.put("A", 10);
		indexMap.put("B", 11);
		indexMap.put("C", 12);
		indexMap.put("D", 13);
		indexMap.put("E", 14);
		indexMap.put("F", 15);

		// pRoundConstant = new int[8][8];
		// qRoundConstant = new int[8][8];

		// for (int i = 0; i < pRoundConstant[0].length; i++) {
		// pRoundConstant[0][i] = (int) (0xFF & (i * 16));
		// }

		// for (int i = 0; i < qRoundConstant.length; i++) {
		// Arrays.fill(qRoundConstant[i], (int) (0xFF & 255));
		// }

		for (int i = 0; i < QROUNDCONSTANT[0].length; i++) {
			QROUNDCONSTANT[7][i] = (int) (0xFF & (255 - (i * 16)));
		}

		System.out.println("\nmi:\n");
		for (int k = 0; k < 8; k++) {
			for (int l = 0; l < 8; l++) {
				System.out.print(String.format("%02X ", mi[k][l] & 0xFF) + " ");
			}
			System.out.println();
		}
		NewGui.stateList.add(new State(i2 + " mi", deepCopy(mi)));
		// NewGui.map.put(i2 + " mi", deepCopy(mi));

		System.out.println("\nhim:\n");
		for (int k = 0; k < 8; k++) {
			for (int l = 0; l < 8; l++) {
				System.out.print(String.format("%02X ", him1[k][l] & 0xFF) + " ");
			}
			System.out.println();
		}
		NewGui.stateList.add(new State(i2 + " him", deepCopy(him1)));
		// NewGui.map.put(i2 + " him", deepCopy(him1));

		this.xoredHM = xorHM(this.him1, this.mi);
		System.out.println("\npo XORHM:\n");
		for (int k = 0; k < 8; k++) {
			for (int l = 0; l < 8; l++) {
				System.out.print(String.format("%02X ", xoredHM[k][l] & 0xFF) + " ");
			}
			System.out.println();
		}
		NewGui.stateList.add(new State(i2 + " xorhm", deepCopy(xoredHM)));
		// NewGui.map.put(i2 + " xorhm", deepCopy(xoredHM));
		this.outputp = pPermutations(xoredHM, false);
		this.outputq = qPermutations(mi);

		return getBlockState(him1, outputp, outputq);
	}

	private int[] getBlockState(int[][] him, int[][] outputp2, int[][] outputq2) {
		int[][] xoredAll = new int[8][8];
		for (int i = 0; i < xoredAll.length; i++) {
			for (int j = 0; j < xoredAll[0].length; j++) {
				xoredAll[i][j] = (int) (0xFF & (him[i][j] ^ outputp2[i][j] ^ outputq2[i][j]));
			}
		}
		System.out.println("\npo XORze wszystkich w CompressionFunction:\n");
		for (int k = 0; k < 8; k++) {
			for (int l = 0; l < 8; l++) {
				System.out.print(String.format("%02X ", xoredAll[k][l] & 0xFF) + " ");
			}
			System.out.println();
		}
		NewGui.stateList.add(new State(blockNumber + " xoredAll", deepCopy(xoredAll)));
		// NewGui.map.put(blockNumber + " xoredAll", deepCopy(xoredAll));

		int[] xoredAll2 = new int[64];
		for (int i = 0; i < xoredAll.length; i++) {
			for (int j = 0; j < xoredAll[0].length; j++) {
				xoredAll2[j * 8 + i] = (byte) (0xFF & xoredAll[i][j]);
			}
		}

		return xoredAll2;
	}

	public static int[][] subBytes(int[][] tab) {

		// System.out.println(Integer.toHexString(Utility.sBox[5][3] & 0xFF));

		for (int i = 0; i < tab.length; i++) {
			for (int j = 0; j < tab[0].length; j++) {
				String sTemp = String.format("%02X", (tab[i][j] & 0xFF)).toUpperCase();
				String fPart = sTemp.substring(0, 1);
				String sPart = sTemp.substring(1);

				int indexI = indexMap.get(fPart);
				int indexJ = indexMap.get(sPart);

				tab[i][j] = Utility.sBox[indexI][indexJ];
			}
		}

		return tab;
	}

	public static int[][] mixBytes(int[][] mtab) {

		int[][] tempTab = new int[mtab.length][mtab[0].length];

		for (int c = 0; c < 8; c++) {
			tempTab[0][c] = (GMul(mtab[0][c], 2) ^ GMul(mtab[1][c], 2) ^ GMul(mtab[2][c], 3) ^ GMul(mtab[3][c], 4) ^ GMul(mtab[4][c], 5) ^ GMul(mtab[5][c], 3)
					^ GMul(mtab[6][c], 5) ^ GMul(mtab[7][c], 7));
			tempTab[1][c] = (GMul(mtab[0][c], 7) ^ GMul(mtab[1][c], 2) ^ GMul(mtab[2][c], 2) ^ GMul(mtab[3][c], 3) ^ GMul(mtab[4][c], 4) ^ GMul(mtab[5][c], 5)
					^ GMul(mtab[6][c], 3) ^ GMul(mtab[7][c], 5));
			tempTab[2][c] = (GMul(mtab[0][c], 5) ^ GMul(mtab[1][c], 7) ^ GMul(mtab[2][c], 2) ^ GMul(mtab[3][c], 2) ^ GMul(mtab[4][c], 3) ^ GMul(mtab[5][c], 4)
					^ GMul(mtab[6][c], 5) ^ GMul(mtab[7][c], 3));
			tempTab[3][c] = (GMul(mtab[0][c], 3) ^ GMul(mtab[1][c], 5) ^ GMul(mtab[2][c], 7) ^ GMul(mtab[3][c], 2) ^ GMul(mtab[4][c], 2) ^ GMul(mtab[5][c], 3)
					^ GMul(mtab[6][c], 4) ^ GMul(mtab[7][c], 5));
			tempTab[4][c] = (GMul(mtab[0][c], 5) ^ GMul(mtab[1][c], 3) ^ GMul(mtab[2][c], 5) ^ GMul(mtab[3][c], 7) ^ GMul(mtab[4][c], 2) ^ GMul(mtab[5][c], 2)
					^ GMul(mtab[6][c], 3) ^ GMul(mtab[7][c], 4));
			tempTab[5][c] = (GMul(mtab[0][c], 4) ^ GMul(mtab[1][c], 5) ^ GMul(mtab[2][c], 3) ^ GMul(mtab[3][c], 5) ^ GMul(mtab[4][c], 7) ^ GMul(mtab[5][c], 2)
					^ GMul(mtab[6][c], 2) ^ GMul(mtab[7][c], 3));
			tempTab[6][c] = (GMul(mtab[0][c], 3) ^ GMul(mtab[1][c], 4) ^ GMul(mtab[2][c], 5) ^ GMul(mtab[3][c], 3) ^ GMul(mtab[4][c], 5) ^ GMul(mtab[5][c], 7)
					^ GMul(mtab[6][c], 2) ^ GMul(mtab[7][c], 2));
			tempTab[7][c] = (GMul(mtab[0][c], 2) ^ GMul(mtab[1][c], 3) ^ GMul(mtab[2][c], 4) ^ GMul(mtab[3][c], 5) ^ GMul(mtab[4][c], 3) ^ GMul(mtab[5][c], 5)
					^ GMul(mtab[6][c], 7) ^ GMul(mtab[7][c], 2));
		}

		return tempTab;
	}

	private static int GMul(int a, int b) {
		int p = 0;
		int counter;
		int hi_bit_set;
		for (counter = 0; counter < 8; counter++) {
			if ((b & 1) != 0) {
				p ^= a;
			}
			hi_bit_set = (a & 0x80);
			a <<= 1;
			if (hi_bit_set != 0) {
				a ^= 0x1b; /* x^8 + x^4 + x^3 + x + 1 */
			}
			b >>= 1;
		}
		return p;
	}

	public static int[][] addRoundConstantP(int[][] tab, int r) {

		int[][] tempPConstTab = new int[8][8];

		for (int i = 0; i < tempPConstTab.length; i++)
			for (int j = 0; j < tempPConstTab[i].length; j++)
				tempPConstTab[i][j] = PROUNDCONSTANT[i][j];

		for (int j = 0; j < tempPConstTab[0].length; j++) {
			int con = PROUNDCONSTANT[0][j];
			int xor = con ^ r;
			tempPConstTab[0][j] = xor;
		}

		for (int i = 0; i < tab.length; i++) {
			for (int j = 0; j < tab[0].length; j++) {
				tab[i][j] = (0xFF & (tempPConstTab[i][j] ^ tab[i][j]));
			}
		}

		return tab;
	}

	public int[][] addRoundConstantQ(int[][] tab, int r) {

		int[][] tempQConstTab = new int[8][8];

		for (int i = 0; i < tempQConstTab.length; i++)
			for (int j = 0; j < tempQConstTab[i].length; j++)
				tempQConstTab[i][j] = QROUNDCONSTANT[i][j];

		for (int j = 0; j < tempQConstTab[7].length; j++) {
			int con = QROUNDCONSTANT[7][j];
			int xor = con ^ r;
			tempQConstTab[7][j] = xor;
		}

		for (int i = 0; i < tab.length; i++) {
			for (int j = 0; j < tab[0].length; j++) {
				tab[i][j] = (0xFF & (tempQConstTab[i][j] ^ tab[i][j]));
			}
		}

		return tab;
	}

	public static int[][] shiftBytesP(int[][] tab) {

		int[] c = { 0, 1, 2, 3, 4, 5, 6, 7 };
		int[][] tempTab = new int[8][8];
		for (int i = 0; i < tempTab.length; i++) {
			for (int j = 0; j < tempTab[0].length; j++) {
				int r = j - c[i];
				if (r < 0) {
					tempTab[i][8 + r] = tab[i][j];
				} else {
					tempTab[i][r] = tab[i][j];
				}
			}
		}

		return tempTab;
	}

	public int[][] shiftBytesQ(int[][] tab) {
		int[] c = { 1, 3, 5, 7, 0, 2, 4, 6 };
		int[][] tempTab = new int[8][8];
		for (int i = 0; i < tempTab.length; i++) {
			for (int j = 0; j < tempTab[0].length; j++) {
				int r = j - c[i];
				if (r < 0) {
					tempTab[i][8 + r] = tab[i][j];
				} else {
					tempTab[i][r] = tab[i][j];
				}
			}
		}

		return tempTab;
	}

	private int[][] qPermutations(int[][] mi) {
		int[][] tempTab = new int[8][8];
		tempTab = mi;
		for (int i = 0; i < 10; i++) {
			tempTab = addRoundConstantQ(tempTab, i);
			System.out.println("\nt: " + i + "\tQ po addRoundConstantQ:\n");
			for (int k = 0; k < 8; k++) {
				for (int l = 0; l < 8; l++) {
					System.out.print(String.format("%02X ", tempTab[k][l] & 0xFF) + " ");
				}
				System.out.println();
			}
			NewGui.stateList.add(new State(blockNumber + " Q addRoundConstant " + i, deepCopy(tempTab)));
			// NewGui.map.put(blockNumber + " Q addRoundConstant " + i,
			// deepCopy(tempTab));

			tempTab = subBytes(tempTab);
			System.out.println("\nt: " + i + "\tQ po subBytes:\n");
			for (int k = 0; k < 8; k++) {
				for (int l = 0; l < 8; l++) {
					System.out.print(String.format("%02X ", tempTab[k][l] & 0xFF) + " ");
				}
				System.out.println();
			}
			NewGui.stateList.add(new State(blockNumber + " Q subBytes " + i, deepCopy(tempTab)));
			// NewGui.map.put(blockNumber + " Q subBytes " + i,
			// deepCopy(tempTab));

			tempTab = shiftBytesQ(tempTab);
			System.out.println("\nt: " + i + "\tQ po shiftBytesQ:\n");
			for (int k = 0; k < 8; k++) {
				for (int l = 0; l < 8; l++) {
					System.out.print(String.format("%02X ", tempTab[k][l] & 0xFF) + " ");
				}
				System.out.println();
			}
			NewGui.stateList.add(new State(blockNumber + " Q shiftBytes " + i, deepCopy(tempTab)));
			// NewGui.map.put(blockNumber + " Q shiftBytes " + i,
			// deepCopy(tempTab));

			tempTab = mixBytes(tempTab);
			System.out.println("\nt: " + i + "\tQ po mixBytes:\n");
			for (int k = 0; k < 8; k++) {
				for (int l = 0; l < 8; l++) {
					System.out.print(String.format("%02X ", tempTab[k][l] & 0xFF) + " ");
				}
				System.out.println();
			}
			NewGui.stateList.add(new State(blockNumber + " Q mixBytes " + i, deepCopy(tempTab)));
			// NewGui.map.put(blockNumber + " Q mixBytes " + i,
			// deepCopy(tempTab));
		}
		return deepCopy(tempTab);
	}

	public static int[][] pPermutations(int[][] xoredHM2, boolean outputTransf) {
		int[][] tempTab = new int[8][8];
		tempTab = xoredHM2;

		for (int i = 0; i < 10; i++) {
			tempTab = addRoundConstantP(tempTab, i);
			System.out.println("\nt: " + i + "\tP po addRoundConstantP:\n");
			for (int k = 0; k < 8; k++) {
				for (int l = 0; l < 8; l++) {
					System.out.print(String.format("%02X ", tempTab[k][l] & 0xFF) + " ");
				}
				System.out.println();
			}
			if (outputTransf) {
				NewGui.stateList.add(new State("T: " + blockNumber + " P addRoundConstantP " + i, deepCopy(tempTab)));
			} else {
				NewGui.stateList.add(new State(blockNumber + " P addRoundConstantP " + i, deepCopy(tempTab)));
			}
			// NewGui.map.put(blockNumber + " P addRoundConstantP " + i,
			// deepCopy(tempTab));

			tempTab = subBytes(tempTab);
			System.out.println("\nt: " + i + "\tP po subBytes:\n");
			for (int k = 0; k < 8; k++) {
				for (int l = 0; l < 8; l++) {
					System.out.print(String.format("%02X ", tempTab[k][l] & 0xFF) + " ");
				}
				System.out.println();
			}
			if (outputTransf) {
				NewGui.stateList.add(new State("T: " + blockNumber + " P subBytes " + i, deepCopy(tempTab)));
			} else {
				NewGui.stateList.add(new State(blockNumber + " P subBytes " + i, deepCopy(tempTab)));
			}
			// NewGui.map.put(blockNumber + " P subBytes " + i,
			// deepCopy(tempTab));

			tempTab = shiftBytesP(tempTab);
			System.out.println("\nt: " + i + "\tP po shiftBytesP:\n");
			for (int k = 0; k < 8; k++) {
				for (int l = 0; l < 8; l++) {
					System.out.print(String.format("%02X ", tempTab[k][l] & 0xFF) + " ");
				}
				System.out.println();
			}
			if (outputTransf) {
				NewGui.stateList.add(new State("T: " + blockNumber + " P shiftBytes " + i, deepCopy(tempTab)));
			} else {
				NewGui.stateList.add(new State(blockNumber + " P shiftBytes " + i, deepCopy(tempTab)));
			}
			// NewGui.map.put(blockNumber + " P shiftBytes " + i,
			// deepCopy(tempTab));

			tempTab = mixBytes(tempTab);
			System.out.println("\nt: " + i + "\tpo mixBytes:\n");
			for (int k = 0; k < 8; k++) {
				for (int l = 0; l < 8; l++) {
					System.out.print(String.format("%02X ", tempTab[k][l] & 0xFF) + " ");
				}
				System.out.println();
			}
			if (outputTransf) {
				NewGui.stateList.add(new State("T: " + blockNumber + " P mixBytes " + i, deepCopy(tempTab)));
			} else {
				NewGui.stateList.add(new State(blockNumber + " P mixBytes " + i, deepCopy(tempTab)));
			}
			// NewGui.map.put(blockNumber + " P mixBytes " + i,
			// deepCopy(tempTab));
		}
		return deepCopy(tempTab);
	}

	private int[][] xorHM(int[][] him12, int[][] mi2) {

		int[][] x = new int[8][8];
		for (int i = 0; i < x.length; i++) {
			for (int j = 0; j < x[0].length; j++) {
				x[i][j] = (int) (0xFF & (him1[i][j] ^ mi[i][j]));
			}
		}
		return x;
	}

	public static int[][] prepareArray(int[] tab) {
		int[][] preparedArray = new int[8][8];
		for (int i = 0; i < tab.length; i++) {
			preparedArray[i % 8][i / 8] = tab[i];
		}

		return preparedArray;
	}

	public static int[][] deepCopy(int[][] original) {
		if (original == null) {
			return null;
		}

		final int[][] result = new int[original.length][];
		for (int i = 0; i < original.length; i++) {
			result[i] = Arrays.copyOf(original[i], original[i].length);
		}
		return result;
	}

}
