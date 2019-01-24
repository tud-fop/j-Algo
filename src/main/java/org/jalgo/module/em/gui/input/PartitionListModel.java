package org.jalgo.module.em.gui.input;

import java.util.Set;

import javax.swing.DefaultListModel;

import org.jalgo.module.em.data.Partition;

/**
 * A specified ListModel which offers a method for setting the data to
 * specific elements.
 * 
 * @author Tobias Nett
 *
 */
public class PartitionListModel extends DefaultListModel {

	private static final long serialVersionUID = -5061338496640971667L;

	/**
	 * Sets the model's data to the specified set of partitions.
	 * 
	 * @param observations set of partitions of the event set
	 */
	public void setElements(Set<Partition> observations){
		this.clear();
		for (Partition partition : observations) {
			this.addElement(partition);
		}
	}
}
