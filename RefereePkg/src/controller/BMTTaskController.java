package controller;

import javax.swing.event.ChangeEvent;

import model.BmtTask;
import model.TaskServer;

public class BMTTaskController implements TaskController {

	@Override
	public void sendSpec(String spec, BaseController controller)
	{
		TaskServer serverTeam1 = controller.getServerTeam1();
		
		if (serverTeam1.sendTaskSpecToClient(spec)) {
			controller.getMainGUI().setStatusLine("Task specification sent to the team.");
		} else {
			controller.getMainGUI().setStatusLine("<html><FONT COLOR=RED>Task specification could not be send to the team!</FONT>  </html>");
		}
	}

	@Override
	public void tabChanged(ChangeEvent event, BaseController controller)
	{
		controller.getServerTeam2().shutdownSocket();
		controller.getTaskSpec().notifyBmtTaskSpecChanged(new BmtTask(), 0, controller.getTaskSpec().getBmtTaskList());		
	}
}
