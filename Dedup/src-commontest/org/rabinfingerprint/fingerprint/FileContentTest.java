package org.rabinfingerprint.fingerprint;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import junit.framework.TestCase;

import org.rabinfingerprint.fingerprint.RabinFingerprintLongWindowed;
import org.rabinfingerprint.polynomial.Polynomial;
import org.rabinfingerprint.scanner.IOUtils;

public class FileContentTest extends TestCase {

	public void testFileContent() throws IOException {
		long start = System.currentTimeMillis();
//		long bytesperwindow = 4;		// Window size for the rabin fingerprint algorithm
		byte[] buf1 = new byte[1024];
		byte[] buf2 = new byte[1024];
		int size1, size2;
		
		// Prepare the test files
//		String fileName1 = "plain1.txt";
//		String fileName2 = "plain2.txt";
		String fileName1 = "test1.class";
		String fileName2 = "test2.class";
		File file1 = new File(fileName1);
		File file2 = new File(fileName2);
		BufferedInputStream bis1 = new BufferedInputStream(new FileInputStream(file1));
		BufferedInputStream bis2 = new BufferedInputStream(new FileInputStream(file2));

		// calculate target fingerprint
		Polynomial polynomial = Polynomial.createIrreducible(53);
		Fingerprint<Polynomial> rabin1 = new RabinFingerprintLong(polynomial);
		Fingerprint<Polynomial> rabin2 = new RabinFingerprintLong(polynomial);
		// read file and push 1024 bytes each time for calculating the finger print
		// Once finger print is different, stop and announce the difference
		while(((size1 = bis1.read(buf1)) != -1) && ((size2 = bis2.read(buf2)) != -1)){
			rabin1.pushBytes(buf1);
			rabin2.pushBytes(buf2);
			String s1 = new String(buf1);
			String s2 = new String(buf2);
			
			if(rabin1.getFingerprint().compareTo( rabin2.getFingerprint()) != 0){
				System.out.println("file 1 and file 2 are different!");	
				break;
			}
		}
		if(rabin1.getFingerprint().compareTo( rabin2.getFingerprint()) == 0)
			System.out.println("file 1 and file 2 are the same!");
		
		long end = System.currentTimeMillis();
		System.out.println("Time: " + (end-start) + "ms.");
		assertEquals(0, rabin1.getFingerprint().compareTo(rabin2.getFingerprint()));
	}

	public static void main(String[] args) throws IOException {
		FileContentTest test = new FileContentTest();
		test.testFileContent();
	}

}
