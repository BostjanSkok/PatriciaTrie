package com.company;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * Created by Bostjan on 7.4.2014.
 */
public class PatriciaTest extends TestCase {
    Patricia<Integer> patricia;

    protected void setUp() throws Exception {
        patricia = new Patricia<Integer>();
    }


    public void testInsertRetrive() throws Exception {
        patricia.Vstavi("Test", 34);
        assertEquals(34, (int) patricia.Najdi("Test"));
    }
    public void testInsertRemoveRetrive() throws Exception {
        patricia.Vstavi("Test", 34);
        assertEquals(34,(int)patricia.Brisi("Test"));
        assertNull(patricia.Najdi("Test"));
    }

    public void testInsertRetrive5x() throws Exception {
        patricia.Vstavi("Test", 34);
        patricia.Vstavi("neki", 34);
        patricia.Vstavi("different", 88);
        assertEquals(34, (int) patricia.Najdi("Test"));
        assertEquals(88, (int) patricia.Najdi("different"));
        assertEquals(34, (int) patricia.Najdi("neki"));
    }


       public void testInsertRetriveRemove5x() throws Exception {
        patricia.Vstavi("Test", 34);
        patricia.Vstavi("neki", 34);
        patricia.Vstavi("different", 88);
        assertEquals(34, (int) patricia.Najdi("Test"));
        assertEquals(88,(int)patricia.Brisi("different"));
        assertNull(patricia.Najdi("different"));
        assertEquals(34, (int) patricia.Najdi("neki"));
    }

    public void testAddRemove5x() throws Exception {
        patricia.Vstavi("Test", 34);
        patricia.Vstavi("neki", 34);
        patricia.Vstavi("different", 88);
        assertEquals(34, (int) patricia.Brisi("Test"));
        assertEquals(88,(int)patricia.Brisi("different"));
        assertNull(patricia.Najdi("different"));
        assertEquals(34, (int) patricia.Brisi("neki"));
        assertNull(patricia.Najdi("neki"));
        assertNull(patricia.Najdi("Test"));

    }



    public void testDuplicateKey() throws Exception {
        patricia.Vstavi("Test", 34);
        patricia.Vstavi("neki", 34);
        patricia.Vstavi("different", 88);
        assertEquals(34, (int) patricia.Najdi("Test"));
        assertEquals(88, (int) patricia.Najdi("different"));
        assertEquals(34, (int) patricia.Najdi("neki"));
        try {
            patricia.Vstavi("Test", 39);
        }catch (Exception exp){
            assertEquals("Dvojni kljuc",exp.getMessage());
        }
    }
}
