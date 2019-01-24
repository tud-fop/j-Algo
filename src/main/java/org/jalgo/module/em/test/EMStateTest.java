package org.jalgo.module.em.test;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.jalgo.module.em.data.EMData;
import org.jalgo.module.em.data.EMState;

/**
 * @author kilian
 */
public class EMStateTest extends TestCase {
    private List<EMData> emDataList;
    private EMState eMDataTestInstance;
    
    @Override
    protected void setUp() throws Exception {
        emDataList = new ArrayList<EMData>();
        for (int i = 0; i<5; i++){
        	emDataList.add(new EMData(null, null, null, null, null, null));
        }
        eMDataTestInstance = new EMState(emDataList);
    	super.setUp();
    }
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of getEMData method, of class EMState.
     */
    public void testGetEMData() {
//        System.out.println("getEMData");
        EMState instance = eMDataTestInstance;
        List<EMData> expResult = emDataList;
        List<EMData> result = instance.getEMData();
        assertEquals(expResult, result);
    }
}
