package controller;

import javax.swing.event.ChangeEvent;

public interface TaskController {
	public void sendSpec(String spec, BaseController controller);
	public void tabChanged(ChangeEvent event, BaseController controller);
}
