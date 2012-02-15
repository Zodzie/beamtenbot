package de.eidotter.fileio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.hof.mainbot.helpers.FileIO;

public class TestFileIO {
	private static final String FILENAME = "testFileIO.txt";
	private File inside;
	
	@Before
	public void setUp() {
		// Create File inside
		FileWriter fw;
		try{
			fw = new FileWriter(FILENAME);
			fw.write("testFileIO");
			fw.close();
		} catch (Exception e){
			fw = null;
		} finally {
			System.gc();
		}
		inside = new File(FILENAME);
	}


	@Test
	public void testGetFile() {
		File f = FileIO.getFile(FILENAME);
		assertNotNull(f);
		assertTrue(f.exists());
		assertEquals(FILENAME, f.getName());
	}
	
	@Test
	public void testGetFileInputStream(){
		FileInputStream fis = FileIO.getFileInputStream(FILENAME);
		assertNotNull(fis);
	}
	
	@Test
	public void testGetFileWriter(){
		FileWriter fw = FileIO.getFileWriter(FILENAME);
		assertNotNull(fw);
		try {
			fw.append("Test123");
			fw.flush();
			fw.close();
		} catch (IOException e) {
			fail("IOException " + e.getMessage());
		} finally {
			System.gc();
		}
	}

	@After
	public void tearDown() throws Exception {
		// Delete File inside
		if(inside != null){
			inside.delete();
			if(inside.exists()){
				System.out.println("File still exists!");
			}
		}
		
	}
}
