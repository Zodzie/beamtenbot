package de.hof.junit.helpers;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import de.hof.mainbot.helpers.BotProperties;

public class TestBotProperties {
	String fileName = "junit_test.properties";
	BotProperties prop;
	@Before
	public void setUp() throws Exception {
		prop = new BotProperties(fileName);
	}

	@Test
	public void testString() {
		String name = prop.getString("name");
		assertEquals("Peter", name);
	}

	@Test
	public void testStringEmpty(){
		String nul = prop.getString("nameNull");
		assertEquals("", nul);
	}
	
	@Test
	public void testStringDefault(){
		String def = prop.getString("keyNichtVorhanden", "default");
		assertEquals("default", def);
	}
	
	@Test
	public void testBoolean(){
		Boolean b = prop.getBoolean("debug");
		assertEquals(new Boolean(true), b);
		b = prop.getBoolean("false");
		assertEquals(new Boolean(false), b);
	}
	
	@Test
	public void testBooleanEmpty(){
		Boolean b = prop.getBoolean("verbose");
		assertEquals(new Boolean(false), b);
	}
	
	@Test
	public void testBooleanDefault(){
		Boolean b = prop.getBoolean("keyNichtVorhanden", false);
		assertEquals(false, b);
		b = prop.getBoolean("keyNichtVorhanden", true);
		assertEquals(true, b);
	}
	
	@Test
	public void testInteger(){
		Integer i = prop.getInteger("trueint");
		assertEquals(new Integer(4711), i);
	}

	@Test
	public void testIntegerEmpty(){
		Integer i = prop.getInteger("emptyint");
		assertEquals(null, i);
	}
	
	@Test
	public void testIntegerDefault(){
		Integer i = prop.getInteger("nottrueint", 4711);
		assertEquals(new Integer("4711"), i);
	}
	
	@Test
	public void testStringList(){
		String[] list = prop.getStringList("channels");
		assertEquals(3, list.length);
		assertEquals("#test1 hwu", list[0]);
		assertEquals("#test3", list[1]);
		assertEquals("#test4", list[2]);
	}
}
