package dataBase;

import java.util.ArrayList;
import java.util.Date;
import com.jswitch.base.modelo.HibernateUtil;
import com.jswitch.base.modelo.entidades.auditoria.AuditoriaBasica;
import com.jswitch.base.modelo.entidades.defaultData.ConfiguracionesGenerales;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;

/**
 *
 * @author bc
 */
public class ActualizarParametros {

    public ActualizarParametros() {
        System.out.println("Act Parametros");
        Session s = HibernateUtil.getSessionFactory().openSession();
        System.out.println("sesion abierta");

        Transaction tx = s.beginTransaction();
        System.out.println("transaccion begin");

        System.out.println(s.createQuery("DELETE FROM " + ConfiguracionesGenerales.class.getName()).executeUpdate());

        System.out.println("viejos borrados ");

        AuditoriaBasica ab = new AuditoriaBasica(new Date(), "defaultdata", true);

        System.out.println("metodo Parametros");

        ArrayList<ConfiguracionesGenerales> list = new ArrayList<ConfiguracionesGenerales>(0);

        list.add(new ConfiguracionesGenerales("ut", 75.0));
        list.add(new ConfiguracionesGenerales("iva",0.12));       
        list.add(new ConfiguracionesGenerales("reembolso.diasVencimiento", 180));
        list.add(new ConfiguracionesGenerales("cartaAval.diasVencimiento", 30));
        list.add(new ConfiguracionesGenerales("aps.diasVencimiento", 15));
        list.add(new ConfiguracionesGenerales("remesa.maxUt.reembolso", 10000.0));
        ConfiguracionesGenerales a = new ConfiguracionesGenerales("remesa.numNeg", "00002100");
        a.setValorInteger(2100);
        list.add(a);

        for (ConfiguracionesGenerales o : list) {
            o.setAuditoria(ab);
            s.saveOrUpdate(o);
        }
        System.out.println("antes del comit");

        tx.commit();
        System.out.println("comit");
        s.close();
        System.out.println("close");
        System.out.println("Listo");
    }

    public static void main(String[] args) {
        new ActualizarParametros();
    }
}
