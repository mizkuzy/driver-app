package ru.tsystems.logiweb.driver.beans;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;

@ManagedBean
@SessionScoped
public class Login implements Serializable {

    private static final long serialVersionUID = 1094801825228386363L;

    private String pwd;
    private String msg;
    private String user;

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    /**
     * Validates username and password.
     *
     * @return driver_app.xhtml
     */
    public String validateUsernamePassword() {
        boolean valid = getValidation(user, pwd);
        if (valid) {
            HttpSession session = SessionBean.getSession();
            session.setAttribute("username", user);
            return "driver_app";
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN,
                            "Incorrect pair Username and Password.",
                            "Please enter correct username and password."));
            return "login";
        }
    }

    /**
     * Gets connection to required url and prints result and response.
     *
     * @param username
     * @param password
     * @return true or false
     */
    private boolean getValidation(String username, String password) {
        try {
            String urlStr = "http://localhost:9080/rest/hello/validate/" + username + "/" + password + "/";
            System.out.println(urlStr);
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            System.out.println("sending request...");

            conn.setRequestMethod("GET");
            System.out.println(conn.getRequestProperties());

            int responseCode = conn.getResponseCode();
            System.out.println("responseCode" + responseCode);

            String response = "NO";

            if (responseCode == 200) {
                conn.connect();
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                response = br.readLine();
                while (response != null) {
                    System.out.println(response);
                    if (response.equals("OK")) {
                        conn.disconnect();
                        return true;
                    }
                    response = br.readLine();
                }
            }
            conn.disconnect();

            if (response.equals("OK")) {
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

            return false;
    }

    /**
     * Logouts event, invalidate session.
     *
     * @return login.xhtml
     */
    public String logout() {
        HttpSession session = SessionBean.getSession();
        session.invalidate();
        return "login";
    }
}
