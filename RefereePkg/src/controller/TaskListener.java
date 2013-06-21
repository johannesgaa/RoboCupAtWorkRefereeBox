package controller;

import java.util.ArrayList;
import java.util.EventListener;
import model.BmtTask;
import model.BntTask;
import model.BttTask;
import model.CttTask;
import model.CbtTask;
import model.PptTask;


public interface TaskListener extends EventListener{
	public void bntTaskSpecChanged(BntTask bntTask, int pos, ArrayList<BntTask> bntTaskList);
	public void bmtTaskSpecChanged(BmtTask bmtTask, int pos, ArrayList<BmtTask> bmtTaskList);
	public void bttTaskSpecChanged(BttTask bttTask, int pos, ArrayList<BttTask> bttTaskList);
	public void cttTaskSpecChanged(CttTask cttTask, int pos, ArrayList<CttTask> cttTaskList);
	public void cbtTaskSpecChanged(CbtTask cbtTask, int pos, ArrayList<CbtTask> cbtTaskList);
	public void pptTaskSpecChanged(PptTask pptTask, int pos, ArrayList<PptTask> pptTaskList);
}
