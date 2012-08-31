


public class RobotModule {
	private static String serverIP = "127.0.1.1";
	private static String serverPort = "11111";
	private static String teamName = "b-it-bots";
	
	public static void main(String[] args) throws Exception {
		JavaClient jclient = new JavaClient();
		// Obtain task specification from server
		jclient.obtainTaskSpecFromServer(serverIP,serverPort,teamName);
	}
}
