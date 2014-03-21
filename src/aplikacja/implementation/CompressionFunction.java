package aplikacja.implementation;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import aplikacja.gui.NewGui;

public class CompressionFunction {

	private static int[][] pRoundConstant;
	private int[][] qRoundConstant;
	private int[][] him1;
	private int[][] mi;
	private int[][] xoredHM;
	private static Map<String, Integer> indexMap;
	private int[][] outputp;
	private int[][] outputq;
	private static int blockNumber;

	public int[] calculate(int[] padArray, int[] messageblock, int i2) {

		this.blockNumber = i2;
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

		pRoundConstant = new int[8][8];
		qRoundConstant = new int[8][8];

		for (int i = 0; i < pRoundConstant[0].length; i++) {
			pRoundConstant[0][i] = (int) (0xFF & (i * 16));
		}

		for (int i = 0; i < qRoundConstant.length; i++) {
			Arrays.fill(qRoundConstant[i], (int) (0xFF & 255));
		}

		for (int i = 0; i < qRoundConstant[0].length; i++) {
			qRoundConstant[7][i] = (int) (0xFF & (255 - (i * 16)));
		}

		System.out.println("\nmi:\n");
		for (int k = 0; k < 8; k++) {
			for (int l = 0; l < 8; l++) {
				System.out.print(String.format("%02X ", mi[k][l] & 0xFF) + " ");
			}
			System.out.println();
		}
		NewGui.map.put(i2 + " mi", mi.clone());

		System.out.println("\nhim:\n");
		for (int k = 0; k < 8; k++) {
			for (int l = 0; l < 8; l++) {
				System.out.print(String.format("%02X ", him1[k][l] & 0xFF) + " ");
			}
			System.out.println();
		}
		NewGui.map.put(i2 + " him", him1.clone());

		this.xoredHM = xorHM(this.him1, this.mi);
		System.out.println("\npo XORHM:\n");
		for (int k = 0; k < 8; k++) {
			for (int l = 0; l < 8; l++) {
				System.out.print(String.format("%02X ", xoredHM[k][l] & 0xFF) + " ");
			}
			System.out.println();
		}
		NewGui.map.put(i2 + " xorhm", xoredHM);
		this.outputp = pPermutations(xoredHM);
		this.outputq = qPermutations(mi);

		return getBlockState(xoredHM, outputp, outputq);
	}

	private int[] getBlockState(int[][] xoredHM2, int[][] outputp2, int[][] outputq2) {
		int[][] xoredAll = new int[8][8];
		for (int i = 0; i < xoredAll.length; i++) {
			for (int j = 0; j < xoredAll[0].length; j++) {
				xoredAll[i][j] = (int) (0xFF & (xoredHM2[i][j] ^ outputp2[i][j] ^ outputq2[i][j]));
			}
		}
		NewGui.map.put(blockNumber + " xoredAll", xoredAll);

		int[] xoredAll2 = new int[64];
		for (int i = 0; i < xoredAll.length; i++) {
			for (int j = 0; j < xoredAll[0].length; j++) {
				xoredAll2[j + i] = (byte) (0xFF & xoredAll[i][j]);
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

	public static int[][] mixBytes(int[][] tempTab) {

		return tempTab;
	}

	public static int[][] addRoundConstantP(int[][] tab, int r) {

		int[][] tempPConstTab = pRoundConstant.clone();

		for (int j = 0; j < tempPConstTab[0].length; j++) {
			int con = pRoundConstant[0][j];
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

		int[][] tempPConstTab = qRoundConstant.clone();

		for (int j = 0; j < tempPConstTab[0].length; j++) {
			int con = this.qRoundConstant[0][j];
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
		for (int i = 0; i < 10; i++) {
			tempTab = addRoundConstantQ(mi, i);
			System.out.println("\nQ po addRoundConstantQ:\n");
			for (int k = 0; k < 8; k++) {
				for (int l = 0; l < 8; l++) {
					System.out.print(String.format("%02X ", tempTab[k][l] & 0xFF) + " ");
				}
				System.out.println();
			}
			NewGui.map.put(blockNumber + " Q addRoundConstant " + i, tempTab.clone());

			tempTab = subBytes(tempTab);
			System.out.println("\nQ po subBytes:\n");
			for (int k = 0; k < 8; k++) {
				for (int l = 0; l < 8; l++) {
					System.out.print(String.format("%02X ", tempTab[k][l] & 0xFF) + " ");
				}
				System.out.println();
			}
			NewGui.map.put(blockNumber + " Q subBytes " + i, tempTab.clone());

			tempTab = shiftBytesQ(tempTab);
			System.out.println("\nQ po shiftBytesQ:\n");
			for (int k = 0; k < 8; k++) {
				for (int l = 0; l < 8; l++) {
					System.out.print(String.format("%02X ", tempTab[k][l] & 0xFF) + " ");
				}
				System.out.println();
			}
			NewGui.map.put(blockNumber + " Q shiftBytes " + i, tempTab.clone());

			tempTab = mixBytes(tempTab);
			System.out.println("\nQ po mixBytes:\n");
			for (int k = 0; k < 8; k++) {
				for (int l = 0; l < 8; l++) {
					System.out.print(String.format("%02X ", tempTab[k][l] & 0xFF) + " ");
				}
				System.out.println();
			}
			NewGui.map.put(blockNumber + " Q mixBytes " + i, tempTab.clone());
		}
		return tempTab.clone();
	}

	public static int[][] pPermutations(int[][] xoredHM2) {

		int[][] tempTab = new int[8][8];
		for (int i = 0; i < 10; i++) {
			tempTab = addRoundConstantP(xoredHM2, 0);
			System.out.println("\nP po addRoundConstantP:\n");
			for (int k = 0; k < 8; k++) {
				for (int l = 0; l < 8; l++) {
					System.out.print(String.format("%02X ", tempTab[k][l] & 0xFF) + " ");
				}
				System.out.println();
			}
			NewGui.map.put(blockNumber + " P addRoundConstantP " + i, tempTab.clone());

			tempTab = subBytes(tempTab);
			System.out.println("\nP po subBytes:\n");
			for (int k = 0; k < 8; k++) {
				for (int l = 0; l < 8; l++) {
					System.out.print(String.format("%02X ", tempTab[k][l] & 0xFF) + " ");
				}
				System.out.println();
			}
			NewGui.map.put(blockNumber + " P subBytes " + i, tempTab.clone());

			tempTab = shiftBytesP(tempTab);
			System.out.println("\nP po shiftBytesP:\n");
			for (int k = 0; k < 8; k++) {
				for (int l = 0; l < 8; l++) {
					System.out.print(String.format("%02X ", tempTab[k][l] & 0xFF) + " ");
				}
				System.out.println();
			}
			NewGui.map.put(blockNumber + " P shiftBytes " + i, tempTab.clone());

			tempTab = mixBytes(tempTab);
			System.out.println("\npo mixBytes:\n");
			for (int k = 0; k < 8; k++) {
				for (int l = 0; l < 8; l++) {
					System.out.print(String.format("%02X ", tempTab[k][l] & 0xFF) + " ");
				}
				System.out.println();
			}
			NewGui.map.put(blockNumber + " P mixBytes " + i, tempTab.clone());
		}
		return tempTab.clone();
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

}
