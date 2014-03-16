package org.rabinfingerprint.fingerprint;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;

import junit.framework.TestCase;

import org.rabinfingerprint.polynomial.Polynomial;
import org.rabinfingerprint.scanner.IOUtils;

public class RabinFingerprintTest extends TestCase {
	
	public static void testPolynomialsAndLongs() {
		// generate random data
		byte[] data = new byte[1024];
		Random random = new Random(System.currentTimeMillis());
		random.nextBytes(data);

		// generate random irreducible polynomial
		Polynomial p = Polynomial.createIrreducible(53);
		final Fingerprint<Polynomial> rabin0 = new RabinFingerprintPolynomial(p);
		final Fingerprint<Polynomial> rabin1 = new RabinFingerprintLong(p);

		rabin0.pushBytes(data);
		rabin1.pushBytes(data);

//		String s1 = "l>></htm";
//		String s2 = "l<></htm";
//		rabin0.pushBytes(s1.getBytes());
//		rabin1.pushBytes(s2.getBytes());
		
		System.out.println("test a");
		assertEquals(0, rabin0.getFingerprint().compareTo(rabin1.getFingerprint()));
	}
	
	public static void testWindowing() throws FileNotFoundException, IOException {
//		doTestWindowing(true, 5);
		doTestWindowing(false, 1);
	}
		
	public static void doTestWindowing(boolean usePolynomials, int times) throws FileNotFoundException, IOException {
		Random random = new Random(System.currentTimeMillis());
		int windowSize = 8;
		
		for (int i = 0; i < times; i++) {
			// Generate Random Irreducible Polynomial
			Polynomial p = Polynomial.createIrreducible(53);

			final Fingerprint<Polynomial> rabin0, rabin1;
			if (usePolynomials) {
				rabin0 = new RabinFingerprintPolynomial(p, windowSize);
				rabin1 = new RabinFingerprintPolynomial(p);
			} else {
				rabin0 = new RabinFingerprintLongWindowed(p, windowSize);
//				rabin1 = new RabinFingerprintLong(p);
				rabin1 = new RabinFingerprintLongWindowed(p, windowSize);
			}

			// Generate Random Data
//			byte[] data = new byte[windowSize * 5];
//			byte[] data2 = new byte[windowSize * 5];
//			random.nextBytes(data);
//			data2 = data.clone();
//			data2[30] = 65;
//			System.out.println(data.length + " ** "+ new String(data));
//			System.out.println(data2.length + " ** " + new String(data2));
//			
//			// Read 3 windows of data to populate one fingerprint
//			for (int j = 0; j < windowSize * 3; j++) {
//				rabin0.pushByte(data[j]);
//			}
//
//			// Starting from same offset, continue fingerprinting for 1 more window
//			for (int j = windowSize * 3; j < windowSize * 4; j++) {
//				rabin0.pushByte(data[j]);
//				rabin1.pushByte(data2[j]);
//			}

			String file1 = "plain10.txt";
			String file2 = "plain20.txt";
//			String file1 = "test1.class";
//			String file2 = "test2.class";
			byte data1[] = IOUtils.readBytes(new File(file1));
			byte data2[] = IOUtils.readBytes(new File(file2));

			// Read 3 windows of data to populate one fingerprint
			for (int j = 0; j < windowSize * 3; j++) {
				rabin0.pushByte(data1[j]);
			}

			// Starting from same offset, continue fingerprinting for 1 more window
			for (int j = windowSize * 3; j < windowSize * 4; j++) {
				rabin0.pushByte(data1[j]);
				rabin1.pushByte(data2[j]);
			}

			System.out.println("test b");
			assertEquals(0, rabin0.getFingerprint().compareTo(rabin1.getFingerprint()));
		}
	}
}
