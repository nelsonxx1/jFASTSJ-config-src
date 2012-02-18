
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import vista_controlador.ConfigFrame;
import vista_controlador.Logon;

/**
 *
 * @author bc
 */
public class Config {

    public static void main(String args[]) throws IOException {
        BufferedReader b = null;
        b = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec("hostname").getInputStream()));
        String hostname = b.readLine();
        System.getProperties().put("COMPUTERNAME", hostname);
        System.getProperties().put("valido", "si");
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                try {
                    ConfigFrame cf = new ConfigFrame();
                    new Logon(cf, true).setVisible(true);
                    cf.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
