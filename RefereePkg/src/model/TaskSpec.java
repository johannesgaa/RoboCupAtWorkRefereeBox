package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.regex.*;
import java.util.StringTokenizer;

import javax.swing.event.EventListenerList;

//import model.TaskTriplet.State;

import view.Utils;
import controller.TaskListener;

public class TaskSpec {
	private ArrayList<BntTask> bntTaskList;
	private ArrayList<BmtTask> bmtTaskList;
	private ArrayList<BttTask> bttTaskList;
	private ArrayList<CttTask> cttTaskList;
	private ArrayList<CbtTask> cbtTaskList;
	private ArrayList<PptTask> pptTaskList;

	
	private EventListenerList listOfTaskListeners = new EventListenerList();
	private Logging logg;
	private String taskListName = "TaskList";

	private String removeSpaces(String str) {
		StringTokenizer tokens = new StringTokenizer(str, " ", false);
		String newStr = "";
		while (tokens.hasMoreElements()) {
			newStr += tokens.nextElement();
		}
		return newStr;
	}

	public TaskSpec(int num) {
		bntTaskList = new ArrayList<BntTask>();
		bmtTaskList = new ArrayList<BmtTask>();
		bttTaskList = new ArrayList<BttTask>();
		cttTaskList = new ArrayList<CttTask>();
		cbtTaskList = new ArrayList<CbtTask>();
		pptTaskList = new ArrayList<PptTask>();
		logg = Logging.getInstance();
	}

	public String getTaskSpecString(CompetitionIdentifier compIdent,
			boolean xmlFormat) {
		String s = new String("");

		s = s.concat("<?xml version=\"1.0\"?>");


		switch (compIdent) {
		case BNT:
			if (bntTaskList.size() > 0) {

				s = s.concat("<competition type=\"BNT\">");

				Iterator<BntTask> itBnt = bntTaskList.iterator();
				int i = 0;
				while (itBnt.hasNext()) {
					BntTask bnt = itBnt.next();

					// add Orientation Attribute
//					if(i==0)
//						s= s.concat("<area type=\"Initial\" orientation=\"" + (bnt).getOrientation() + "\" ");
//					else if(i==bntTaskList.size()-1)
//						s= s.concat("<area type=\"Final\" orientation=\"" + (bnt).getOrientation() + "\" ");
//					else
						s= s.concat("<area type=\"Source\" orientation=\"" + (bnt).getOrientation() + "\" ");
					// add Duration Attribute
					s= s.concat("pause=\"" + (bnt).getPause() + "\">");

					//area value
					s= s.concat(bnt.getPlace());
					s= s.concat("</area>");
					i++;
				}
				s = s.concat("</competition>");
			}
			break;
		case BMT:
			if (bmtTaskList.size() > 0) {	
				s = s.concat("<competition type=\"BMT\">");

				//Inital Position youbot
				s= s.concat("<area type=\"Initial\">"+(bmtTaskList.get(0)).getPlaceInitial());
				s=s.concat("</area>");

				//Add all Areas
				Iterator<BmtTask> itBmt = bmtTaskList.iterator();
				BmtTask bmt = new BmtTask();

				ArrayList<String> areas = new ArrayList<String>();
				ArrayList<String> configs = new ArrayList<String>();
				List<List<String>> objects = new ArrayList<List<String>>();


				int i = 0;
				while (itBmt.hasNext()) {
					// add Orientation Attribute
					bmt = (BmtTask)itBmt.next();
					boolean isNew = true;
					int sCount = 0;
					for( String q : areas)
					{
						if(bmt.getPlaceSource().equals(q))
						{
							isNew = false;

							break;
						}
						sCount++;
					}


					if(isNew)
					{
						areas.add(bmt.getPlaceSource());
						ArrayList<String> gut = new ArrayList<String>();
						gut.add(bmt.getObject());
						objects.add(gut);
						configs.add(bmt.getConfiguration());
					}
					else
					{
						objects.get(sCount).add(bmt.getObject());						
					}
				}
				itBmt = bmtTaskList.iterator();


				ArrayList<String> areasDes = new ArrayList<String>();
				ArrayList<String> configDes = new ArrayList<String>();
				List<List<String>> objectsDes = new ArrayList<List<String>>();

				i = 0;
				while (itBmt.hasNext()) {
					// add Orientation Attribute
					bmt = (BmtTask)itBmt.next();
					boolean isNew = true;
					int sCount = 0;
					for( String q : areasDes)
					{
						if(bmt.getPlaceDestination().equals(q))
						{
							isNew = false;

							break;
						}
						sCount++;
					}


					if(isNew)
					{
						areasDes.add(bmt.getPlaceDestination());
						ArrayList<String> gut = new ArrayList<String>();
						gut.add(bmt.getObject());
						objectsDes.add(gut);
						configDes.add(bmt.getConfiguration());
					}
					else
					{
						objectsDes.get(sCount).add(bmt.getObject());						
					}
				}

				//implement all Source areas
				int y=0;
				for(String sq : areas )
				{
					s= s.concat("<area type=\"Source\" ");

					// add Duration Attribute
					s= s.concat("configuration=\"" + configs.get(y) + "\">");

					//area value
					s = s.concat(sq);
					
					//add Objects
					for(String oq : objects.get(y))
					{
						s=s.concat("<object>"+oq+"</object>");
					}

					s= s.concat("</area>");
					y++;
				}

				//implement all Destination areas
				y=0;
				for(String sq : areasDes )
				{
					s= s.concat("<area type=\"Destination\" ");

					// add Duration Attribute
					s= s.concat("configuration=\"" + configDes.get(y) + "\">");

					//area value
					s = s.concat(sq);
					
					//add Objects
					for(String oq : objectsDes.get(y))
					{
						s=s.concat("<object>"+oq+"</object>");
					}

					s= s.concat("</area>");
					y++;
				}
				//Final Position youbot
				s= s.concat("<area type=\"Final\">"+(bmtTaskList.get(bmtTaskList.size()-1)).getPlaceFinal()+"</area>");
			}

			s = s.concat("</competition>");
			break;
		case BTT:
			if (bttTaskList.size() > 0) {	
				s = s.concat("<competition type=\"BTT\">");

				//Add Areas
				Iterator<BttTask> itBtt = bttTaskList.iterator();
				BttTask btt = new BttTask();

				ArrayList<String> areas = new ArrayList<String>();
				ArrayList<String> config = new ArrayList<String>();
				List<List<String>> objects = new ArrayList<List<String>>();

				ArrayList<String> areasDes = new ArrayList<String>();
				ArrayList<String> configDes = new ArrayList<String>();
				List<List<String>> objectsDes = new ArrayList<List<String>>();

				String startSit = bttTaskList.get(0).getSituation();
				String endSit = bttTaskList.get(bttTaskList.size()-1).getSituation(); 


				while (itBtt.hasNext()) {
					
					// add Orientation Attribute
					btt = (BttTask)itBtt.next();

					if(btt.getSituation().equals(startSit))
					{
						boolean isNew = true;
						int sCount = 0;
						for( String q : areas)
						{
							if(btt.getPlace().equals(q))
							{
								isNew = false;
								break;
							}
							sCount++;
						}


						if(isNew)
						{
							areas.add(btt.getPlace());
							ArrayList<String> gut = new ArrayList<String>();
							gut.add(btt.getObject());
							objects.add(gut);
							config.add(btt.getConfiguration());
						}
						else
						{
							objects.get(sCount).add(btt.getObject());						
						}

					}
					else if(btt.getSituation().equals(endSit))
					{
						boolean isNew = true;
						int sCount = 0;
						for( String q : areasDes)
						{
							if(btt.getPlace().equals(q))
							{
								isNew = false;
								break;
							}
							sCount++;
						}


						if(isNew)
						{
							areasDes.add(btt.getPlace());
							ArrayList<String> gut = new ArrayList<String>();
							gut.add(btt.getObject());
							objectsDes.add(gut);
							configDes.add(btt.getConfiguration());
						}
						else
						{
							objectsDes.get(sCount).add(btt.getObject());						
						}

					}
				}

				//implement all Source areas
				int y=0;
				for(String sq : areas )
				{
					s= s.concat("<area type=\"Source\" ");

					// add Duration Attribute
					s= s.concat("configuration=\"" + config.get(y) + "\">");

					//area value
					s = s.concat(sq);
					
					//add Objects
					for(String oq : objects.get(y))
					{
						s=s.concat("<object>"+oq+"</object>");
					}

					s= s.concat("</area>");
					y++;
				}

				//implement all Destination areas
				y=0;
				for(String sq : areasDes )
				{
					s= s.concat("<area type=\"Destination\" ");

					// add Duration Attribute
					s= s.concat("configuration=\"" + configDes.get(y) + "\">");

					//area value
					s = s.concat(sq);
					
					//add Objects
					for(String oq : objectsDes.get(y))
					{
						s=s.concat("<object>"+oq+"</object>");
					}

					s= s.concat("</area>");
					y++;
				}	
			}
			s = s.concat("</competition>");
			break;

		case CTT:
			if (cttTaskList.size() > 0) {
				Iterator<CttTask> itCtt = cttTaskList.iterator();
				CttTask ctt = itCtt.next();
				CttTask previous = new CttTask();
				String sTeam1 = "";
				String sTeam2 = "";
				do {
					if (ctt.getSituation().equals("initial")) {
						sTeam1 = sTeam1.concat(ctt.getSituation()
								+ "situation(");
						sTeam2 = sTeam2.concat(ctt.getSituation()
								+ "situation(");
						do {
							sTeam1 = sTeam1.concat("<" + ctt.getPlace() + ",(");
							sTeam2 = sTeam2.concat("<" + ctt.getPlace() + ",(");
							do {
								sTeam1 = sTeam1.concat(ctt.getObject() + ",");
								sTeam2 = sTeam2.concat(ctt.getObject() + ",");
								previous = ctt;
								if (itCtt.hasNext())
									ctt = itCtt.next();
								else
									ctt = new CttTask();
							} while (ctt.getPlace().equals(previous.getPlace())
									&& (ctt.getConfiguration().equals(previous
											.getConfiguration())));
							sTeam1 = sTeam1.substring(0, sTeam1.length() - 1);
							sTeam2 = sTeam2.substring(0, sTeam2.length() - 1);
							sTeam1 = sTeam1.concat(")>");
							sTeam2 = sTeam2.concat(")>");
						} while (ctt.getSituation().equals(
								previous.getSituation()));
						sTeam1 = sTeam1.concat(")");
						sTeam2 = sTeam2.concat(")");
						sTeam1 = sTeam1.concat(";");
						sTeam2 = sTeam2.concat(";");
					} else if (ctt.getSituation().equals("team1Goal")) {
						sTeam1 = sTeam1.concat("goalsituation(");
						do {
							sTeam1 = sTeam1.concat("<" + ctt.getPlace() + ",");
							sTeam1 = sTeam1
									.concat(ctt.getConfiguration() + "(");
							do {
								sTeam1 = sTeam1.concat(ctt.getObject() + ",");
								previous = ctt;
								if (itCtt.hasNext())
									ctt = itCtt.next();
								else
									ctt = new CttTask();
							} while (ctt.getPlace().equals(previous.getPlace())
									&& (ctt.getConfiguration().equals(previous
											.getConfiguration())));
							sTeam1 = sTeam1.substring(0, sTeam1.length() - 1);
							sTeam1 = sTeam1.concat(")>");
						} while (ctt.getSituation().equals(
								previous.getSituation()));
						sTeam1 = sTeam1.concat(")");
					} else if (ctt.getSituation().equals("team2Goal")) {
						sTeam2 = sTeam2.concat("goalsituation(");
						do {
							sTeam2 = sTeam2.concat("<" + ctt.getPlace() + ",");
							sTeam2 = sTeam2
									.concat(ctt.getConfiguration() + "(");
							do {
								sTeam2 = sTeam2.concat(ctt.getObject() + ",");
								previous = ctt;
								if (itCtt.hasNext())
									ctt = itCtt.next();
								else {
									ctt = new CttTask();
								}
							} while (ctt.getPlace().equals(previous.getPlace())
									&& (ctt.getConfiguration().equals(previous
											.getConfiguration())));
							sTeam2 = sTeam2.substring(0, sTeam2.length() - 1);
							sTeam2 = sTeam2.concat(")>");
						} while (ctt.getSituation().equals(
								previous.getSituation()));
						sTeam2 = sTeam2.concat(")");
					}
				} while (ctt.getSituation().length() != 0);

				s = s.concat(sTeam1);
				s = s.concat(">#CTT<");
				s = s.concat(sTeam2);
			}
			break;
		case CBT:
			if (cbtTaskList.size() > 0) {
				
				s = s.concat("<competition type=\"CBT\">");
				
				Iterator<CbtTask> itCbt = cbtTaskList.iterator();
				while (itCbt.hasNext()) {
					
					s = s.concat("<area type=\"Source\">");
					s = s.concat(((CbtTask) itCbt.next()).getPlace());
					s= s.concat("</area>");
					
				}
				s = s.concat("</competition>");
			}
			
			break;
		case PPT:
			if (pptTaskList.size() > 0) {
				s = s.concat("<competition type=\"PPT\">");

				Iterator<PptTask> itPpt = pptTaskList.iterator();
				PptTask ppt = new PptTask();

				//Add Source Areas
				ArrayList<String> areas = new ArrayList<String>();
				List<List<String>> objects = new ArrayList<List<String>>();

				while (itPpt.hasNext()) {
					// add object to area
					ppt = (PptTask)itPpt.next();
					boolean isNew = true;
					int areaCount = 0;
					for( String q : areas)
					{
						if(ppt.getSource().equals(q))
						{
							isNew = false;

							break;
						}
						areaCount++;
					}


					if(isNew)
					{
						// new area
						areas.add(ppt.getSource());
						ArrayList<String> gut = new ArrayList<String>();
						gut.add(ppt.getObject());
						objects.add(gut);
					}
					else
					{
						objects.get(areaCount).add(ppt.getObject());						
					}
				}
				itPpt = pptTaskList.iterator();

				//Add Destination Areas
				ArrayList<String> areasDes = new ArrayList<String>();
				List<List<String>> objectsDes = new ArrayList<List<String>>();

				while (itPpt.hasNext()) {
					// add object to area
					ppt = (PptTask)itPpt.next();
					boolean isNew = true;
					int areaCount = 0;
					for( String q : areasDes)
					{
						if(ppt.getDestination().equals(q))
						{
							isNew = false;

							break;
						}
						areaCount++;
					}


					if(isNew)
					{
						//new area
						areasDes.add(ppt.getDestination());
						ArrayList<String> gut = new ArrayList<String>();
						gut.add(ppt.getObject());
						objectsDes.add(gut);
					}
					else
					{
						objectsDes.get(areaCount).add(ppt.getObject());						
					}
				}

				//implement all Source areas
				int y=0;
				for(String sq : areas )
				{
					s = s.concat("<area type=\"Source\">");

					//area value
					s = s.concat(sq);
					
					//add Objects
					for(String oq : objects.get(y))
					{
						s = s.concat("<object>"+oq+"</object>");
					}
					
					s = s.concat("</area>");
					y++;
				}

				//implement all Destination areas
				y=0;
				for(String sq : areasDes )
				{
					s = s.concat("<area type=\"Destination\">");

					//area value
					s = s.concat(sq);
					
					//add Objects
					for(String oq : objectsDes.get(y))
					{
						s = s.concat("<object>"+oq+"</object>");
					}
					
					s = s.concat("</area>");
					y++;
				}
				s = s.concat("</competition>");	
			}
			break;
		default:
			break;
		}

		return s;
	}
	
	public String getTaskSpecString(CompetitionIdentifier compIdent) {
		String s = new String();
		s = s.concat(compIdent.name());
		s = s.concat("<");
		switch (compIdent) {
		case BNT:
			if (bntTaskList.size() > 0) {
				Iterator<BntTask> itBnt = bntTaskList.iterator();
				while (itBnt.hasNext()) {
					s = s.concat(((Task) itBnt.next()).getString());
				}
			}
			break;
		case BMT:
			if (bmtTaskList.size() > 0) {
				Iterator<BmtTask> itBmt = bmtTaskList.iterator();
				BmtTask first = new BmtTask();
				if (itBmt.hasNext()) {
					first = itBmt.next();
					s = s.concat(BmtTask.getPlaceInitial());
					s = s.concat(",");
					s = s.concat(BmtTask.getPlaceSource());
					s = s.concat(",");
					s = s.concat(BmtTask.getPlaceDestination());
					s = s.concat(",");
					s = s.concat(first.getConfiguration());
					s = s.concat("(");
					s = s.concat(first.getObject());
				}
				BmtTask last = first;
				while (itBmt.hasNext()) {
					last = itBmt.next();
					s = s.concat(",");
					s = s.concat(last.getObject());
				}
				s = s.concat(")");
				s = s.concat(",");
				s = s.concat(BmtTask.getPlaceFinal());
			}
			break;
		case BTT:
			if (bttTaskList.size() > 0) {
				Iterator<BttTask> itBtt = bttTaskList.iterator();
				BttTask btt = itBtt.next();
				BttTask previous = new BttTask();
				do {
					s = s.concat(btt.getSituation() + "situation(");
					do {
						s = s.concat("<"+btt.getPlace() + ",");
						s = s.concat(btt.getConfiguration() + "(");
						do {
							s = s.concat(btt.getObject() + ",");
							previous = btt;
							if (itBtt.hasNext())
								btt = itBtt.next();
							else
								btt = new BttTask();
						} while (btt.getPlace().equals(previous.getPlace()) && (btt.getConfiguration().equals(previous.getConfiguration())));
						s = s.substring(0, s.length() - 1); // comma is no
						// longer needed
						s = s.concat(")>");
					} while (btt.getSituation().equals(previous.getSituation()));
					s = s.concat(")");
					if (btt.getSituation().length() != 0) {
						s = s.concat(";");
					}
				} while (btt.getSituation().length() != 0);
			}
			break;
		case CTT:
			if (cttTaskList.size() > 0) {
				Iterator<CttTask> itCtt = cttTaskList.iterator();
				CttTask ctt = itCtt.next();
				CttTask previous = new CttTask();
				String sTeam1 = "";
				String sTeam2 = "";
				do {
					if (ctt.getSituation().equals("initial")) {
						sTeam1 = sTeam1.concat(ctt.getSituation() + "situation(");
						sTeam2 = sTeam2.concat(ctt.getSituation() + "situation(");
						do {
							sTeam1 = sTeam1.concat("<" + ctt.getPlace() + ",(");
							sTeam2 = sTeam2.concat("<" + ctt.getPlace() + ",(");
							do {
								sTeam1 = sTeam1.concat(ctt.getObject() + ",");
								sTeam2 = sTeam2.concat(ctt.getObject() + ",");
								previous = ctt;
								if (itCtt.hasNext())
									ctt = itCtt.next();
								else
									ctt = new CttTask();
							} while (ctt.getPlace().equals(previous.getPlace()) && (ctt.getConfiguration().equals(previous.getConfiguration())));
							sTeam1 = sTeam1.substring(0, sTeam1.length() - 1);
							sTeam2 = sTeam2.substring(0, sTeam2.length() - 1);
							sTeam1 = sTeam1.concat(")>");
							sTeam2 = sTeam2.concat(")>");
						} while (ctt.getSituation().equals(previous.getSituation()));
						sTeam1 = sTeam1.concat(")");
						sTeam2 = sTeam2.concat(")");
						sTeam1 = sTeam1.concat(";");
						sTeam2 = sTeam2.concat(";");
					} else if (ctt.getSituation().equals("team1Goal")) {
						sTeam1 = sTeam1.concat("goalsituation(");
						do {
							sTeam1 = sTeam1.concat("<" + ctt.getPlace() + ",");
							sTeam1 = sTeam1.concat(ctt.getConfiguration() + "(");
							do {
								sTeam1 = sTeam1.concat(ctt.getObject() + ",");
								previous = ctt;
								if (itCtt.hasNext())
									ctt = itCtt.next();
								else
									ctt = new CttTask();
							} while (ctt.getPlace().equals(previous.getPlace()) && (ctt.getConfiguration().equals(previous.getConfiguration())));
							sTeam1 = sTeam1.substring(0, sTeam1.length() - 1);
							sTeam1 = sTeam1.concat(")>");
						} while (ctt.getSituation().equals(previous.getSituation()));
						sTeam1 = sTeam1.concat(")");
					} else if (ctt.getSituation().equals("team2Goal")) {
						sTeam2 = sTeam2.concat("goalsituation(");
						do {
							sTeam2 = sTeam2.concat("<" + ctt.getPlace() + ",");
							sTeam2 = sTeam2.concat(ctt.getConfiguration() + "(");
							do {
								sTeam2 = sTeam2.concat(ctt.getObject() + ",");
								previous = ctt;
								if (itCtt.hasNext())
									ctt = itCtt.next();
								else {
									ctt = new CttTask();
								}
							} while (ctt.getPlace().equals(previous.getPlace()) && (ctt.getConfiguration().equals(previous.getConfiguration())));
							sTeam2 = sTeam2.substring(0, sTeam2.length() - 1);
							sTeam2 = sTeam2.concat(")>");
						} while (ctt.getSituation().equals(previous.getSituation()));
						sTeam2 = sTeam2.concat(")");
					}
				} while (ctt.getSituation().length() != 0);

				s = s.concat(sTeam1);
				s = s.concat(">#CTT<");
				s = s.concat(sTeam2);
			}
			break;
		case CBT:
			if (cbtTaskList.size() > 0) {
				Iterator<CbtTask> itCbt = cbtTaskList.iterator();
				while (itCbt.hasNext()) {
					s = s.concat(((CbtTask) itCbt.next()).getPlace() + ",");
				}
				s = s.substring(0, s.length() - 1); // comma is no
			}
			
			break;
		case PPT:
			if (pptTaskList.size() > 0) {
				Iterator<PptTask> itPpt = pptTaskList.iterator();
				PptTask ppt = itPpt.next();
				PptTask previous = new PptTask();
				
				s = s.concat(ppt.getSource() + ",(");
				String destination = ppt.getDestination();
				do {
					s = s.concat(ppt.getObject() + ",");
					previous = ppt;
					if (itPpt.hasNext())
						ppt = itPpt.next();
					else
						ppt = new PptTask();
				} while (ppt.getSource().equals(previous.getSource()));				
				s = s.substring(0, s.length() - 1); // remove last comma
				s = s.concat("),");
				s = s.concat(destination);	
			}
			break;
		default:
		}
		s = s.concat(">");
		return s;
	}

	/**
	 * @param compIdent
	 * @param task
	 */
	public void addTask(CompetitionIdentifier compIdent, Object task) {
		switch (compIdent) {
		case BNT:
			BntTask bntTask = (BntTask) task;
			bntTaskList.add(bntTask);
			logg.globalLogging(taskListName, CompetitionIdentifier.BNT + bntTask.getString() + " no. " + bntTaskList.indexOf(bntTask) + " added");
			notifyBntTaskSpecChanged(bntTask, bntTaskList.indexOf(bntTask), bntTaskList);
			break;
		case BMT:
			BmtTask bmtTask = (BmtTask) task;
			bmtTaskList.add(bmtTask);
			logg.globalLogging(taskListName, CompetitionIdentifier.BMT + bmtTask.getString() + " no. " + bmtTaskList.indexOf(bmtTask) + " added");
			notifyBmtTaskSpecChanged(bmtTask, bmtTaskList.indexOf(bmtTask), bmtTaskList);
			break;
		case BTT:
			BttTask bttTask = (BttTask) task;
			bttTaskList.add(bttTask);
			Collections.sort(bttTaskList);
			logg.globalLogging(taskListName, CompetitionIdentifier.BTT + bttTask.getString() + " no. " + bttTaskList.indexOf(bttTask) + " added");
			notifyBttTaskSpecChanged(bttTask, bttTaskList.indexOf(bttTask), bttTaskList);
			break;
		case CTT:
			CttTask cttTask = (CttTask) task;
			cttTaskList.add(cttTask);
			Collections.sort(cttTaskList);
			logg.globalLogging(taskListName, CompetitionIdentifier.BTT + cttTask.getString() + " no. " + bttTaskList.indexOf(cttTask) + " added");
			notifyCttTaskSpecChanged(cttTask, cttTaskList.indexOf(cttTask), cttTaskList);
			break;
		case CBT:
			CbtTask cbtTask = (CbtTask) task;
			cbtTaskList.add(cbtTask);
			Collections.sort(cbtTaskList);
			logg.globalLogging(taskListName, CompetitionIdentifier.CBT + cbtTask.getString() + " no. " + cbtTaskList.indexOf(cbtTask) + " added");
			notifyCbtTaskSpecChanged(cbtTask, cbtTaskList.indexOf(cbtTask), cbtTaskList);
			break;
		case PPT:
			PptTask pptTask = (PptTask) task;
			pptTaskList.add(pptTask);
			Collections.sort(pptTaskList);
			logg.globalLogging(taskListName, CompetitionIdentifier.PPT + pptTask.getString() + " no. " + pptTaskList.indexOf(pptTask) + " added");
			notifyPptTaskSpecChanged(pptTask, pptTaskList.indexOf(pptTask), pptTaskList);
			break;
		default:
			return;
		}
	}

	public Task deleteTask(int pos, CompetitionIdentifier compIdent) {
		switch (compIdent) {
		case BNT:
			BntTask bntTask = bntTaskList.remove(pos);
			logg.globalLogging(taskListName, CompetitionIdentifier.BNT + bntTask.getString() + " no. " + pos + " deleted");
			notifyBntTaskSpecChanged(bntTask, pos, bntTaskList);
			return bntTask;
		case BMT:
			BmtTask bmtTask = bmtTaskList.remove(pos);
			logg.globalLogging(taskListName, CompetitionIdentifier.BMT + bmtTask.getString() + " no. " + pos + " deleted");
			notifyBmtTaskSpecChanged(bmtTask, pos, bmtTaskList);
			return bmtTask;
		case BTT:
			BttTask bttTask = bttTaskList.remove(pos);
			Collections.sort(bttTaskList);
			logg.globalLogging(taskListName, CompetitionIdentifier.CTT + bttTask.getString() + " no. " + pos + " deleted");
			notifyBttTaskSpecChanged(bttTask, pos, bttTaskList);
			return bttTask;
		case CTT:
			CttTask cttTask = cttTaskList.remove(pos);
			logg.globalLogging(taskListName, CompetitionIdentifier.CTT + "not implemented yet");
			Collections.sort(cttTaskList);
			logg.globalLogging(taskListName, CompetitionIdentifier.CTT + cttTask.getString() + " no. " + pos + " deleted");
			notifyCttTaskSpecChanged(cttTask, pos, cttTaskList);
			return cttTask;
		case CBT:
			CbtTask cbtTask = cbtTaskList.remove(pos);
			Collections.sort(cbtTaskList);
			logg.globalLogging(taskListName, CompetitionIdentifier.CBT + cbtTask.getString() + " no. " + pos + " deleted");
			notifyCbtTaskSpecChanged(cbtTask, pos, cbtTaskList);
			return cbtTask;
		case PPT:
			PptTask pptTask = pptTaskList.remove(pos);
			Collections.sort(pptTaskList);
			logg.globalLogging(taskListName, CompetitionIdentifier.PPT + pptTask.getString() + " no. " + pos + " deleted");
			notifyPptTaskSpecChanged(pptTask, pos, pptTaskList);
			return pptTask;
		
		default:
			return null;
		}
	}

	public Task moveUp(int pos, CompetitionIdentifier compIdent) {

		if (pos == 0) {
			return null;
		} else {
			switch (compIdent) {
			case BNT:
				BntTask bntTask = bntTaskList.remove(pos);
				bntTaskList.add(pos - 1, bntTask);
				logg.globalLogging(taskListName, CompetitionIdentifier.BNT + bntTask.getString() + " no. " + bntTaskList.indexOf(bntTask) + " moved up");
				notifyBntTaskSpecChanged(bntTask, pos, bntTaskList);
				return bntTask;
			case BMT:
				BmtTask bmtTask = bmtTaskList.remove(pos);
				bmtTaskList.add(pos - 1, bmtTask);
				logg.globalLogging(taskListName, CompetitionIdentifier.BMT + bmtTask.getString() + " no. " + bmtTaskList.indexOf(bmtTask) + " moved up");
				notifyBmtTaskSpecChanged(bmtTask, pos, bmtTaskList);
				return bmtTask;
			case BTT:
				BttTask bttTask = bttTaskList.remove(pos);
				bttTaskList.add(pos - 1, bttTask);
				Collections.sort(bttTaskList);
				logg.globalLogging(taskListName, CompetitionIdentifier.BTT + bttTask.getString() + " no. " + bttTaskList.indexOf(bttTask) + " moved up");
				notifyBttTaskSpecChanged(bttTask, pos, bttTaskList);
				return bttTask;
			case CTT:
				CttTask cttTask = cttTaskList.remove(pos);
				cttTaskList.add(pos - 1, cttTask);
				Collections.sort(cttTaskList);
				logg.globalLogging(taskListName, CompetitionIdentifier.CTT + cttTask.getString() + " no. " + cttTaskList.indexOf(cttTask) + " moved up");
				notifyCttTaskSpecChanged(cttTask, pos, cttTaskList);
				return cttTask;
			case CBT:
				CbtTask cbtTask = cbtTaskList.remove(pos);
				cbtTaskList.add(pos - 1, cbtTask);
				Collections.sort(cbtTaskList);
				logg.globalLogging(taskListName, CompetitionIdentifier.CBT + cbtTask.getString() + " no. " + cbtTaskList.indexOf(cbtTask) + " moved up");
				notifyCbtTaskSpecChanged(cbtTask, pos, cbtTaskList);
				return cbtTask;
			case PPT:
				PptTask pptTask = pptTaskList.remove(pos);
				pptTaskList.add(pos - 1, pptTask);
				Collections.sort(pptTaskList);
				logg.globalLogging(taskListName, CompetitionIdentifier.PPT + pptTask.getString() + " no. " + pptTaskList.indexOf(pptTask) + " moved up");
				notifyPptTaskSpecChanged(pptTask, pos, pptTaskList);
				return pptTask;
			default:
				return null;
			}
		}
	}

	public Task moveDown(int pos, CompetitionIdentifier compIdent) {
		switch (compIdent) {
		case BNT:
			if (bntTaskList.size() == pos + 1)
				return null;
			BntTask bntTask = bntTaskList.remove(pos);
			bntTaskList.add(pos + 1, bntTask);
			logg.globalLogging(taskListName, CompetitionIdentifier.BNT + bntTask.getString() + " no. " + bntTaskList.indexOf(bntTask) + " moved down");
			notifyBntTaskSpecChanged(bntTask, pos, bntTaskList);
			return bntTask;
		case BMT:
			if (bmtTaskList.size() == pos + 1)
				return null;
			BmtTask bmtTask = bmtTaskList.remove(pos);
			bmtTaskList.add(pos + 1, bmtTask);
			logg.globalLogging(taskListName, CompetitionIdentifier.BMT + bmtTask.getString() + " no. " + bmtTaskList.indexOf(bmtTask) + " moved down");
			notifyBmtTaskSpecChanged(bmtTask, pos, bmtTaskList);
			return bmtTask;
		case BTT:
			if (bttTaskList.size() == pos + 1)
				return null;
			BttTask bttTask = bttTaskList.remove(pos);
			bttTaskList.add(pos + 1, bttTask);
			Collections.sort(bttTaskList);
			logg.globalLogging(taskListName, CompetitionIdentifier.BTT + bttTask.getString() + " no. " + bttTaskList.indexOf(bttTask) + " moved down");
			notifyBttTaskSpecChanged(bttTask, pos, bttTaskList);
			return bttTask;
		case CTT:
			if (bttTaskList.size() == pos + 1)
				return null;
			CttTask cttTask = cttTaskList.remove(pos);
			cttTaskList.add(pos + 1, cttTask);
			Collections.sort(cttTaskList);
			logg.globalLogging(taskListName, CompetitionIdentifier.BTT + cttTask.getString() + " no. " + bttTaskList.indexOf(cttTask) + " moved down");
			notifyCttTaskSpecChanged(cttTask, pos, cttTaskList);
			return cttTask;
		case CBT:
			if (cbtTaskList.size() == pos + 1)
				return null;
			CbtTask cbtTask = cbtTaskList.remove(pos);
			cbtTaskList.add(pos + 1, cbtTask);
			Collections.sort(cbtTaskList);
			logg.globalLogging(taskListName, CompetitionIdentifier.CBT + cbtTask.getString() + " no. " + cbtTaskList.indexOf(cbtTask) + " moved down");
			notifyCbtTaskSpecChanged(cbtTask, pos, cbtTaskList);
			return cbtTask;
		case PPT:
			if (pptTaskList.size() == pos + 1)
				return null;
			PptTask pptTask = pptTaskList.remove(pos);
			pptTaskList.add(pos + 1, pptTask);
			Collections.sort(pptTaskList);
			logg.globalLogging(taskListName, CompetitionIdentifier.PPT + pptTask.getString() + " no. " + pptTaskList.indexOf(pptTask) + " moved down");
			notifyPptTaskSpecChanged(pptTask, pos, pptTaskList);
			return pptTask;
		default:
			return null;
		}
	}

	public Task updateTask(int pos, Task task, CompetitionIdentifier compIdent) {
		switch (compIdent) {
		case BNT:
			BntTask bntTask = bntTaskList.set(pos, (BntTask) task);
			logg.globalLogging(taskListName, CompetitionIdentifier.BNT + bntTask.getString() + " no. " + bntTaskList.indexOf(task) + " updated to " + task.getString());
			notifyBntTaskSpecChanged(bntTask, bntTaskList.indexOf(bntTask), bntTaskList);
			return bntTask;
		case BMT:
			BmtTask bmtTask = bmtTaskList.set(pos, (BmtTask) task);
			logg.globalLogging(taskListName, CompetitionIdentifier.BMT + bmtTask.getString() + " no. " + bmtTaskList.indexOf(task) + " updated to " + task.getString());
			notifyBmtTaskSpecChanged(bmtTask, bmtTaskList.indexOf(bmtTask), bmtTaskList);
			return bmtTask;
		case BTT:
			BttTask bttTask = bttTaskList.set(pos, (BttTask) task);
			Collections.sort(bttTaskList);
			logg.globalLogging(taskListName, CompetitionIdentifier.BTT + bttTask.getString() + " no. " + bttTaskList.indexOf(task) + " updated to " + task.getString());
			notifyBttTaskSpecChanged(bttTask, bttTaskList.indexOf(bttTask), bttTaskList);
			return bttTask;
		case CTT:
			CttTask cttTask = cttTaskList.set(pos, (CttTask) task);
			Collections.sort(bttTaskList);
			logg.globalLogging(taskListName, CompetitionIdentifier.CTT + cttTask.getString() + " no. " + bttTaskList.indexOf(task) + " updated to " + task.getString());
			notifyCttTaskSpecChanged(cttTask, bttTaskList.indexOf(cttTask), cttTaskList);
			return cttTask;
		case CBT:
			CbtTask cbtTask = cbtTaskList.set(pos, (CbtTask) task);
			Collections.sort(cbtTaskList);
			logg.globalLogging(taskListName, CompetitionIdentifier.CBT + cbtTask.getString() + " no. " + cbtTaskList.indexOf(task) + " updated to " + task.getString());
			notifyCbtTaskSpecChanged(cbtTask, cbtTaskList.indexOf(cbtTask), cbtTaskList);
			return cbtTask;
		case PPT:
			PptTask pptTask = pptTaskList.set(pos, (PptTask) task);
			Collections.sort(pptTaskList);
			logg.globalLogging(taskListName, CompetitionIdentifier.PPT + pptTask.getString() + " no. " + pptTaskList.indexOf(task) + " updated to " + task.getString());
			notifyPptTaskSpecChanged(pptTask, pptTaskList.indexOf(pptTask), pptTaskList);
			return pptTask;
		default:
			return null;
		}
	}

	/*
	 * public List<TaskTriplet> getTaskTripletList() { return null; //
	 * taskTripletList; }
	 */

	public Task getTaskAtIndex(int index, CompetitionIdentifier compIdent) {
		switch (compIdent) {
		case BNT:
			return bntTaskList.get(index);
		case BMT:
			return bmtTaskList.get(index);
		case BTT:
			return bttTaskList.get(index);
		case CTT:
			return cttTaskList.get(index);
		case CBT:
			return cbtTaskList.get(index);
		case PPT:
			return pptTaskList.get(index);
		default:
			return null;
		}
	}

	public void addTaskListener(TaskListener tL) {
		listOfTaskListeners.add(TaskListener.class, tL);
	}

	public void removeTripletListener(TaskListener tL) {
		listOfTaskListeners.remove(TaskListener.class, tL);
	}

	public void notifyBntTaskSpecChanged(BntTask bntTask, int pos, ArrayList<BntTask> bntTaskList2) {
		Object[] listeners = listOfTaskListeners.getListenerList();
		// Each listener occupies two elements - the first is the listener class
		// and the second is the listener instance
		for (int i = 0; i < listeners.length; i += 2) {
			if (listeners[i] == TaskListener.class) {
				((TaskListener) listeners[i + 1]).bntTaskSpecChanged(bntTask, pos, bntTaskList);
			}
		}
	}

	public void notifyBmtTaskSpecChanged(BmtTask bmtTask, int pos, ArrayList<BmtTask> bntTaskList2) {
		Object[] listeners = listOfTaskListeners.getListenerList();
		// Each listener occupies two elements - the first is the listener class
		// and the second is the listener instance
		for (int i = 0; i < listeners.length; i += 2) {
			if (listeners[i] == TaskListener.class) {
				((TaskListener) listeners[i + 1]).bmtTaskSpecChanged(bmtTask, pos, bmtTaskList);
			}
		}
	}

	public void notifyBttTaskSpecChanged(BttTask bttTask, int pos, ArrayList<BttTask> bttTaskList) {
		Object[] listeners = listOfTaskListeners.getListenerList();
		// Each listener occupies two elements - the first is the listener class
		// and the second is the listener instance
		for (int i = 0; i < listeners.length; i += 2) {
			if (listeners[i] == TaskListener.class) {
				((TaskListener) listeners[i + 1]).bttTaskSpecChanged(bttTask, pos, bttTaskList);
			}
		}
	}

	public void notifyCttTaskSpecChanged(CttTask cttTask, int pos, ArrayList<CttTask> cttTaskList) {
		Object[] listeners = listOfTaskListeners.getListenerList();
		// Each listener occupies two elements - the first is the listener class
		// and the second is the listener instance
		for (int i = 0; i < listeners.length; i += 2) {
			if (listeners[i] == TaskListener.class) {
				((TaskListener) listeners[i + 1]).cttTaskSpecChanged(cttTask, pos, cttTaskList);
			}
		}
	}
	
	public void notifyCbtTaskSpecChanged(CbtTask cbtTask, int pos, ArrayList<CbtTask> cbtTaskList) {
		Object[] listeners = listOfTaskListeners.getListenerList();
		// Each listener occupies two elements - the first is the listener class
		// and the second is the listener instance
		for (int i = 0; i < listeners.length; i += 2) {
			if (listeners[i] == TaskListener.class) {
				((TaskListener) listeners[i + 1]).cbtTaskSpecChanged(cbtTask, pos, cbtTaskList);
			}
		}
	}
	
	public void notifyPptTaskSpecChanged(PptTask pptTask, int pos, ArrayList<PptTask> pptTaskList) {
		Object[] listeners = listOfTaskListeners.getListenerList();
		// Each listener occupies two elements - the first is the listener class
		// and the second is the listener instance
		for (int i = 0; i < listeners.length; i += 2) {
			if (listeners[i] == TaskListener.class) {
				((TaskListener) listeners[i + 1]).pptTaskSpecChanged(pptTask, pos, pptTaskList);
			}
		}
	}

	public boolean saveTaskSpec(File file) {

		file = Utils.correctFile(file);
		try {
			FileWriter fstream = new FileWriter(file);
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(getTaskSpecString(CompetitionIdentifier.BNT));
			out.write("\n");
			out.write(getTaskSpecString(CompetitionIdentifier.BMT));
			out.write("\n");
			out.write(getTaskSpecString(CompetitionIdentifier.BTT));
			out.write("\n");
			out.write(getTaskSpecString(CompetitionIdentifier.CTT));
			out.write("\n");
			out.write(getTaskSpecString(CompetitionIdentifier.CBT));
			out.write("\n");
			out.write(getTaskSpecString(CompetitionIdentifier.PPT));
			out.write("\n");
			out.close();
			logg.globalLogging("TODO", "saved actual task specification in >" + file.getName() + "<");
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
			logg.globalLogging("TODO", "saving failed! >");
			return false;
		}

		return true;
	}

	public boolean openTaskSpec(File file) {

		try {
			FileInputStream fstream = new FileInputStream(file);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			while ((strLine = br.readLine()) != null) {
				if (!parseTaskSpecString(strLine))
					return false;
			}
			in.close();
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
			return false;
		}

		return true;
	}

	public boolean parseTaskSpecString(String tSpecStr) {
		tSpecStr = removeSpaces(tSpecStr);

		String competition = tSpecStr.substring(0, 3);
		try {
			if (competition.equals(CompetitionIdentifier.BNT.toString())) {
				bntTaskList = new ArrayList<BntTask>();
				Pattern pat = Pattern.compile(ValidTaskElements.getInstance().getValidBNTPattern());
				Matcher m = pat.matcher(tSpecStr);
				do {
					BntTask nextTask = new BntTask();
					if (m.find()) {
						nextTask.setPlace(m.group(1));
						nextTask.setOrientation(m.group(2));
						nextTask.setPause(m.group(3));
						bntTaskList.add(nextTask);
						logg.globalLogging(taskListName, nextTask.getString() + " no. " + bntTaskList.indexOf(nextTask) + " added");
						notifyBntTaskSpecChanged(nextTask, bntTaskList.indexOf(nextTask), bntTaskList);
					}
				} while (!m.hitEnd());
			}
			if (competition.equals(CompetitionIdentifier.BMT.toString())) {

				bmtTaskList = new ArrayList<BmtTask>();
				String delims = "[<>,()]";
				String[] tokens = tSpecStr.split(delims);

				for (int i = 5; i < tokens.length - 2; i++) {
					BmtTask nextTask = new BmtTask();
					BmtTask.setPlaceInitial(tokens[1]);
					BmtTask.setPlaceSource(tokens[2]);
					BmtTask.setPlaceDestination(tokens[3]);
					nextTask.setConfiguration(tokens[4]);
					BmtTask.setPlaceFinal(tokens[tokens.length - 1]);
					nextTask.setObject(tokens[i]);
					bmtTaskList.add(nextTask);
					logg.globalLogging(taskListName, nextTask.getString() + " no. " + bmtTaskList.indexOf(nextTask) + " added");
					notifyBmtTaskSpecChanged(nextTask, bmtTaskList.indexOf(nextTask), bmtTaskList);
				}
			}
			if (competition.equals(CompetitionIdentifier.BTT.toString())) {

				bttTaskList = new ArrayList<BttTask>();
				String delimssemicolon = "[;]";
				String delimsbracket = "[()]";
				String delimskomma = "[,]";
				String[] taskspec = tSpecStr.split("[<>]");
				if (taskspec.length > 1) {
					String[] initgoalsituation = taskspec[1].split(delimssemicolon);

					for (int d = 0; d < initgoalsituation.length; d++) {

						String[] tokens = initgoalsituation[d].split(delimsbracket);

						String pose;
						String config;
						String situation = "";
						if (tokens[0].contains("initial"))
							situation = "initial";
						if (tokens[0].contains("goal"))
							situation = "goal";

						for (int i = 1; i < tokens.length; i++) {
							String test[] = tokens[i].split(delimskomma);
							pose = test[0];
							if (test.length > 1)
								config = tokens[i].split(delimskomma)[1];
							else
								config = "";
							i++;
							String[] objects = tokens[i].split(delimskomma);
							for (int u = 0; u < objects.length; u++) {

								BttTask nextTask = new BttTask();

								// System.out.println("pose: "+ pose);
								// System.out.println("config: "+ config);
								// System.out.println("objects: "+ objects[u]);
								nextTask.setSituation(situation);
								nextTask.setPlace(pose);
								nextTask.setConfiguration(config);
								nextTask.setObject(objects[u]);
								bttTaskList.add(nextTask);
								logg.globalLogging(taskListName, nextTask.getString() + " no. " + bttTaskList.indexOf(nextTask) + " added");
								notifyBttTaskSpecChanged(nextTask, bttTaskList.indexOf(nextTask), bttTaskList);
							}
						}
					}
				}
			}
			if (competition.equals(CompetitionIdentifier.CTT.toString())) {

				cttTaskList = new ArrayList<CttTask>();
				String delimssemicolon = "[;]";
				String delimsbracket = "[()]";
				String delimskomma = "[,]";
				String[] teams = tSpecStr.split("[#]");
				if (teams.length > 1) {
					teams[0] = teams[0].split("[<>]")[1];
					teams[1] = teams[1].split("[<>]")[1];

					// for (int e = 0; e < teams.length; e++) {
					// System.out.println("team: "+ teams[e]);
					// }
					for (int e = 0; e < teams.length; e++) {
						String[] initgoalsituation = teams[e].split(delimssemicolon);
						// for (int f = 0; f < initgoalsituation.length; f++) {
						// System.out.println("initgoalsituation: "+
						// initgoalsituation[f]);
						// }

						for (int d = 0; d < initgoalsituation.length; d++) {

							String[] tokens = initgoalsituation[d].split(delimsbracket);

							// for (int f = 0; f < tokens.length; f++) {
							// System.out.println("tokens"+f+": "+ tokens[f] +
							// "  e= "+e);
							// }

							String pose;
							String config;
							String situation = "";

							if (tokens[0].contains("initial")) {
								if (e == 0) {
									situation = "initial";
								} else {
									continue;
								}
							}
							if (tokens[0].contains("goal")) {
								if (e == 0) {
									situation = "team1Goal";
								} else {
									situation = "team2Goal";
								}
							}

							for (int i = 1; i < tokens.length; i++) {
								String test[] = tokens[i].split(delimskomma);
								pose = test[0];
								if (test.length > 1)
									config = tokens[i].split(delimskomma)[1];
								else
									config = "";
								i++;
								String[] objects = tokens[i].split(delimskomma);
								for (int u = 0; u < objects.length; u++) {

									CttTask nextTask = new CttTask();

									// System.out.println("pose: "+ pose);
									// System.out.println("config: "+ config);
									// System.out.println("objects: "+
									// objects[u]);
									nextTask.setSituation(situation);
									nextTask.setPlace(pose);
									nextTask.setConfiguration(config);
									nextTask.setObject(objects[u]);
									cttTaskList.add(nextTask);
									logg.globalLogging(taskListName, nextTask.getString() + " no. " + cttTaskList.indexOf(nextTask) + " added");
									notifyCttTaskSpecChanged(nextTask, cttTaskList.indexOf(nextTask), cttTaskList);
								}
							}
						}
					}
				}
			}
			if (competition.equals(CompetitionIdentifier.CBT.toString())) {

				cbtTaskList = new ArrayList<CbtTask>();
				String delimssemicolon = "[;]";
				String delimskomma = "[,]";
				String[] taskspec = tSpecStr.split("[<>]");
								
				if (taskspec.length > 1) {
					String[] positions = taskspec[1].split(delimssemicolon);
					
					for (int d = 0; d < positions.length; d++) {
						
						String[] tokens = positions[d].split(delimskomma);

						for (int i = 0; i < tokens.length; i++) {
							
							CbtTask nextTask = new CbtTask();

							nextTask.setPlace(tokens[i]);
							cbtTaskList.add(nextTask);
							logg.globalLogging(taskListName, nextTask.getString() + " no. " + cbtTaskList.indexOf(nextTask) + " added");
							notifyCbtTaskSpecChanged(nextTask, cbtTaskList.indexOf(nextTask), cbtTaskList);
						
						}
					}
				}
			}
			if (competition.equals(CompetitionIdentifier.PPT.toString())) {

				pptTaskList = new ArrayList<PptTask>();
				String delimsbracket = "[()]";
				String delimskomma = "[,]";
				String[] taskspec = tSpecStr.split("[<>]");
								
				if (taskspec.length > 1) {
									
					String[] spec_splitted = taskspec[1].split(delimsbracket);
					
					if(spec_splitted.length == 3) {
						
						String src = spec_splitted[0].substring(0, spec_splitted[0].length() - 1); // remove last comma for source location
						String dest = spec_splitted[2].substring(1, spec_splitted[2].length()); // remove first comma for destination locations
			
						String[] objects = spec_splitted[1].split(delimskomma);
						for (int u = 0; u < objects.length; u++) {

							PptTask nextTask = new PptTask();

							nextTask.setSource(src);
							nextTask.setObject(objects[u]);
							nextTask.setDestination(dest);
							pptTaskList.add(nextTask);
							logg.globalLogging(taskListName, nextTask.getString() + " no. " + pptTaskList.indexOf(nextTask) + " added");
							notifyPptTaskSpecChanged(nextTask, pptTaskList.indexOf(nextTask), pptTaskList);
						
						}
			
					}
				}
			}
		} catch (Exception e) {
			System.out.println("Caught exception in parseTaskSpec. Error: " + e.getMessage());
			return false;
		}
		return true;
	}

	public void setTaskState(int tripletIndex, int column, CompetitionIdentifier compIdent) {
		Task tT = getTaskAtIndex(tripletIndex, compIdent);
		StateOfTask newState;
		if (column == 1)
			newState = StateOfTask.PASSED;
		else
			newState = StateOfTask.FAILED;
		if (tT.getState() == newState)
			tT.setState(StateOfTask.INIT);
		else
			tT.setState(newState);
		switch (compIdent) {
		case BNT:
			logg.globalLogging(taskListName, CompetitionIdentifier.BNT + tT.getString() + " no. " + tripletIndex + " new state: " + tT.getState());
			logg.competitionLogging(taskListName, CompetitionIdentifier.BNT + tT.getString() + " no. " + tripletIndex + " new state: " + tT.getState());
			notifyBntTaskSpecChanged(((BntTask) tT), column, bntTaskList);
			break;
		case BMT:
			logg.globalLogging(taskListName, CompetitionIdentifier.BMT + tT.getString() + " no. " + tripletIndex + " new state: " + tT.getState());
			logg.competitionLogging(taskListName, CompetitionIdentifier.BMT + tT.getString() + " no. " + tripletIndex + " new state: " + tT.getState());
			notifyBmtTaskSpecChanged(((BmtTask) tT), column, bmtTaskList);
			break;
		case BTT:
			logg.globalLogging(taskListName, CompetitionIdentifier.BTT + tT.getString() + " no. " + tripletIndex + " new state: " + tT.getState());
			logg.competitionLogging(taskListName, CompetitionIdentifier.BTT + tT.getString() + " no. " + tripletIndex + " new state: " + tT.getState());
			notifyBttTaskSpecChanged(((BttTask) tT), column, bttTaskList);
			break;
		case CTT:
			logg.globalLogging(taskListName, CompetitionIdentifier.CTT + tT.getString() + " no. " + tripletIndex + " new state: " + tT.getState());
			logg.competitionLogging(taskListName, CompetitionIdentifier.CTT + tT.getString() + " no. " + tripletIndex + " new state: " + tT.getState());
			notifyCttTaskSpecChanged(((CttTask) tT), column, cttTaskList);
			break;
		case CBT:
			logg.globalLogging(taskListName, CompetitionIdentifier.CBT + tT.getString() + " no. " + tripletIndex + " new state: " + tT.getState());
			logg.competitionLogging(taskListName, CompetitionIdentifier.CBT + tT.getString() + " no. " + tripletIndex + " new state: " + tT.getState());
			notifyCbtTaskSpecChanged(((CbtTask) tT), column, cbtTaskList);
			break;
		case PPT:
			logg.globalLogging(taskListName, CompetitionIdentifier.PPT + tT.getString() + " no. " + tripletIndex + " new state: " + tT.getState());
			logg.competitionLogging(taskListName, CompetitionIdentifier.PPT + tT.getString() + " no. " + tripletIndex + " new state: " + tT.getState());
			notifyPptTaskSpecChanged(((PptTask) tT), column, pptTaskList);
			break;
		default:
			;
		}
	}

	public void resetStates(CompetitionIdentifier compIdent) {
		switch (compIdent) {
		case BNT:
			for (BntTask tT : bntTaskList) {
				tT.setState(StateOfTask.INIT);
			}
		case BMT:
			for (BmtTask tT : bmtTaskList) {
				tT.setState(StateOfTask.INIT);
			}
		case BTT:
			for (BttTask tT : bttTaskList) {
				tT.setState(StateOfTask.INIT);
			}
		case CTT:
			for (CttTask tT : cttTaskList) {
				tT.setState(StateOfTask.INIT);
			}
		case CBT:
			for (CbtTask tT : cbtTaskList) {
				tT.setState(StateOfTask.INIT);
			}
		case PPT:
			for (PptTask tT : pptTaskList) {
				tT.setState(StateOfTask.INIT);
			}
		}
	}

	public boolean checkAllSubgoals(CompetitionIdentifier compIdent) {
		switch (compIdent) {
		case BNT:
			for (BntTask tT : bntTaskList) {
				if (tT.getState() == StateOfTask.INIT) {
					return false;
				}
			}
			break;
		case BMT:
			for (BmtTask tT : bmtTaskList) {
				if (tT.getState() == StateOfTask.INIT) {
					return false;
				}
			}
			break;
		case BTT:
			for (BttTask tT : bttTaskList) {
				if (tT.getState() == StateOfTask.INIT) {
					return false;
				}
			}
			break;
		case CTT:
			for (CttTask tT : cttTaskList) {
				if (tT.getState() == StateOfTask.INIT) {
					return false;
				}
			}
			break;
		case CBT:
			for (CbtTask tT : cbtTaskList) {
				if (tT.getState() == StateOfTask.INIT) {
					return false;
				}
			}
			break;
		case PPT:
			for (PptTask tT : pptTaskList) {
				if (tT.getState() == StateOfTask.INIT) {
					return false;
				}
			}
			break;
		default:
			return false;
		}
		return true;
	}

	public ArrayList<BntTask> getBntTaskList() {
		return bntTaskList;
	}

	public ArrayList<BmtTask> getBmtTaskList() {
		return bmtTaskList;
	}

	public ArrayList<BttTask> getBttTaskList() {
		return bttTaskList;
	}

	public ArrayList<CttTask> getCttTaskList() {
		return cttTaskList;
	}
	
	public ArrayList<CbtTask> getCbtTaskList() {
		return cbtTaskList;
	}
	
	public ArrayList<PptTask> getPptTaskList() {
		return pptTaskList;
	}
}
