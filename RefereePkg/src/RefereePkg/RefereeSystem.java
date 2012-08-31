package RefereePkg;

import controller.MainController;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import model.Logging;

public class RefereeSystem {

	static MainController mC;
	private static Logging logg;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		Date date = new Date();
		String fileName = "Tasklog" + "_" + dateformat.format(date.getTime()) + ".log";
		Logging.setFileName(fileName);
		logg = Logging.getInstance();
		logg.globalLogging("Application", "started");
		mC = new MainController(args);
		mC.showView();

	}
}
