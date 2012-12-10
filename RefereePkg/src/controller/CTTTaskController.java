package controller;

import javax.swing.event.ChangeEvent;

import model.CttTask;
import model.TaskServer;

public class CTTTaskController implements TaskController {

	@Override
	public void sendSpec(String spec, BaseController controller)
	{
		String taskSpec = spec;
		String[] newTaskSpec = taskSpec.split("[#]");
		String message = "";
		
		TaskServer serverTeam1 = controller.getServerTeam1();
		TaskServer serverTeam2 = controller.getServerTeam2();
		
		if (newTaskSpec.length > 0 && serverTeam1.sendTaskSpecToClient(newTaskSpec[0])) {
			message = message.concat("Task specification sent to the " + serverTeam1.getTeamName() + " ");
		} else {
			message = message.concat("<html><FONT COLOR=RED>Task specification could not be send to the team1!</FONT>  </html>");
		}
		if (newTaskSpec.length > 1 && serverTeam2.sendTaskSpecToClient(newTaskSpec[1])) {
			message = message.concat("Task specification sent to the " + serverTeam2.getTeamName() + " ");
		} else {
			message = message.concat("<html><FONT COLOR=RED>Task specification could not be send to the team2!</FONT>  </html>");
		}
		
		controller.getMainGUI().setStatusLine(message);
	}

	@Override
	public void tabChanged(ChangeEvent event, BaseController controller)
	{
		controller.createServers();
		controller.getTaskSpec().notifyCttTaskSpecChanged(new CttTask(), 0, controller.getTaskSpec().getCttTaskList());		
	}
}
