package ru.tsystems.logiweb.driver.beans;


import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@ManagedBean
@SessionScoped
public class Driver {

    private static final long serialVersionUID = 1L;

    /**
     * Send to main app that this driver's begun turn.
     */
    public void beginTurn() {
        String username = SessionBean.getUserName();
        String urlStr = "http://localhost:9080/rest/hello/begin/" + username;
        getConnection(urlStr);
    }

    /**
     * Send to main app that this driver's finished turn.
     */
    public void endTurn() {

        String username = SessionBean.getUserName();
        String urlStr = "http://localhost:9080/rest/hello/end/" + username;

        getConnection(urlStr);

    }

    /**
     * Gets connection to required url and prints result and response.
     *
     * @param urlStr
     */
    private void getConnection(String urlStr) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            System.out.println("sending request...");

            conn.setRequestMethod("GET");
            System.out.println(conn.getRequestProperties());

            int responseCode = conn.getResponseCode();
            System.out.println("responseCode" + responseCode);

            if (responseCode == 200) {
                conn.connect();
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                String nextLine = br.readLine();
                while (nextLine != null) {
                    System.out.println(nextLine);
                    nextLine = br.readLine();
                }
            }
            conn.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends to main app that this driver finished the order.
     */
    public void finishOrder() {
        String username = SessionBean.getUserName();
        String urlStr = "http://localhost:9080/rest/hello/finish_order/" + username;

        getConnection(urlStr);
    }

}