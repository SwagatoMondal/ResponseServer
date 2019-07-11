public class Server {

    private static Home home;

    public static void main(String[] args) {
        try {
            home = new Home();
            home.showDialog();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
