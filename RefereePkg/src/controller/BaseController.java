package controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import model.CompetitionIdentifier;
import model.ConfigFile;
import model.Task;
import model.TaskServer;
import model.TaskSpec;
import view.MainGUI;

public class BaseController {

	public MouseListener taskTableListener = new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
	
			}
	
			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
			}
	
			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
			}
	
			@Override
			public void mousePressed(MouseEvent arg0) {
	                        int selectedRow = mG.getSequenceTable(compIdent.ordinal()).getSelectedRow();
				if (selectedRow >= 0) {
					Task tT = tS.getTaskAtIndex(selectedRow, compIdent);
					mG.setTaskBoxSelected(tT, compIdent);
					mG.setStatusLine("Selected task " + tT.getString() + ".");
				}
	
			}
	
			@Override
			public void mouseReleased(MouseEvent arg0) {
	                        int selectedRow = mG.getSequenceTable(compIdent.ordinal()).getSelectedRow();
	                    	int selectedColumn = mG.getSequenceTable(compIdent.ordinal()).getSelectedColumn();
				if (selectedColumn > 0) {
					tS.setTaskState(selectedRow, selectedColumn, compIdent);
					mG.setStatusLine("Updated task state of " + tS.getTaskAtIndex(selectedRow, compIdent).getString());
					mG.setTableCellCorrected(selectedRow, selectedColumn, compIdent);
					mG.getSequenceTable(compIdent.ordinal()).clearSelection();
				}
	//			int selectedRow = mG.getSequenceTable(compIdent.ordinal()).getSelectedRow();
	//			int selectedColumn = mG.getSequenceTable(compIdent.ordinal()).getSelectedColumn();
	//			if (selectedColumn > 0) {
	//				tS.setTaskState(selectedRow, selectedColumn, compIdent);
	//				mG.setStatusLine("Updated task state of " + tS.getTaskAtIndex(selectedRow, compIdent).getString());
	//				mG.setTableCellCorrected(selectedRow, selectedColumn, compIdent);
	//				mG.getSequenceTable(compIdent.ordinal()).clearSelection();
	//			}
	
			}
		};
	public ChangeListener tabbChangeListener = new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent evt) {
				JTabbedPane pane = (JTabbedPane) evt.getSource();
				if (competitionMode || pane.getSelectedIndex() == -1) {
					mG.getTabbedPane().setSelectedIndex(compIdent.ordinal());
				} else {
					compIdent = CompetitionIdentifier.values()[pane.getSelectedIndex()];
					
					getTaskController().tabChanged(evt, getThis());
				}
			}
		};
	protected CompetitionIdentifier compIdent = CompetitionIdentifier.BNT;
	MainGUI mG;
	TaskSpec tS;
	protected boolean competitionMode = false;
	protected TaskServer serverTeam1;
	protected TaskServer serverTeam2;
	protected ConfigFile cfgFile;
	TaskController bmtController = new BMTTaskController();
	TaskController bntController = new BNTTaskController();
	TaskController bttController = new BTTTaskController();
	TaskController cttController = new CTTTaskController();

	public TaskServer getServerTeam1() {
		return serverTeam1;
	}
	
	public TaskServer getServerTeam2() {
		return serverTeam2;
	}
	
	public BaseController getThis() {
		return this;
	}

	public TaskController getTaskController() {
		return getTaskController(compIdent);
	}

	public TaskController getTaskController(CompetitionIdentifier id) {
		switch(id) {
			case BMT: {
				return bmtController;
			}
			
			case BNT: {
				return bntController;
			}
			
			case BTT: {
				return bttController;
			}
			
			case CTT: {
				return cttController;
			}
		}
		
		return null;
	}

	public MainGUI getMainGUI() {
		return mG;
	}
	
	public TaskSpec getTaskSpec() {
		return tS;
	}
	
	public BaseController() {
		super();
	}

	protected void createServers() {
		serverTeam1.createServerSocket(cfgFile.getServerIP(), cfgFile.getPortTeam1());
		serverTeam1.listenForConnection();
		if (compIdent == CompetitionIdentifier.CTT) {
			serverTeam2.createServerSocket(cfgFile.getServerIP(), cfgFile.getPortTeam2());
			serverTeam2.listenForConnection();
		}
	}

}