package org.jalgo.module.app.coreTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

import java.util.Set;

import org.jalgo.module.app.core.dataType.AvailableDataTypes;
import org.jalgo.module.app.core.dataType.DataType;
import org.junit.Test;

public class AvailableDataTypesTest {
	/** check if there are any DataTypes available
	 */
	@Test
	public void getDataTypes() {
		assertNotNull(AvailableDataTypes.getDataTypes());
		assertNotSame(0,AvailableDataTypes.getDataTypes().size());
	}
	
	/** check if there's only one instance
	 */
	@Test
	public void sameInstanceTest() {
		Set<Class<? extends DataType>> dataTypes1 = AvailableDataTypes.getDataTypes();
		Set<Class<? extends DataType>> dataTypes2 = AvailableDataTypes.getDataTypes();
		
		assertEquals(dataTypes1,dataTypes2);
	}
}
