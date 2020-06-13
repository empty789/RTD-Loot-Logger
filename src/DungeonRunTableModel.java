import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

public class DungeonRunTableModel extends AbstractTableModel {

	private String[] columnNames = { "#", "Time", "Gold", "MoR", "Glory", "Quartz", "Refined", "Exp" };
	private ArrayList<DungeonRun> myList;
	private int index = -2;

	public DungeonRunTableModel(ArrayList<DungeonRun> runList) {
		myList = runList;
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		int size;
		if (myList == null) {
			size = 0;
		} else {
			size = myList.size();
		}
		return size;
	}

	@Override
	public Object getValueAt(int row, int col) {
		Object temp = null;
		if (col == 0) {
			temp = myList.get(row).getIndex();
			
		}else if (col == 1) {
			temp = myList.get(row).getPrettyTime();
		} else if (col == 2) {
			temp = myList.get(row).getGold();
		} else if (col == 3) {
			temp = myList.get(row).getMor();
		} else if (col == 4) {
			temp = myList.get(row).getGlory();
		} else if (col == 5) {
			temp = myList.get(row).getQuartz();
		}else if (col == 6) {
			temp = myList.get(row).getRefined();
		}else if (col == 7) {
			temp = myList.get(row).getExp();
		}
		
		return temp;
	}

	// needed to show column names in JTable
	public String getColumnName(int col) {
		return columnNames[col];
	}

	public Class getColumnClass(int col) {
		if (col == 1 || col == 7) {
			return Long.class;
		} else if (col == 0) {
			return String.class;
		} else {
			return Integer.class;
		}
	}
	
	public boolean isCellEditable(int row, int column){  
        return false;  
    }

}
