package dataBase;

import com.jswitch.base.modelo.HibernateUtil;
import com.jswitch.pagos.modelo.maestra.OrdenDePago;
import java.util.List;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;

/**
 *
 * @author bc
 */
public class ConsultaPruebaOrlando {

    public ConsultaPruebaOrlando() {
        System.out.println("Act Parametros_2");
        Session s = HibernateUtil.getSessionFactory().openSession();
        System.out.println("sesion abierta");

        Transaction tx = s.beginTransaction();
        System.out.println("transaccion begin");

        //s.createQuery("SELECT tipoPersona.nombre as numeroSiniestros FROM "+DetalleSiniestro.class.getName()).list();

        List l = s.createQuery(" SELECT DIAG.id "
                + " FROM " + OrdenDePago.class.getName() + " O "
                + " JOIN O.detalleSiniestros DES "
                + " JOIN DES.diagnosticoSiniestros DIAG").list();

        System.out.println(l);

        tx.commit();
        System.out.println("comit");
        s.close();
        System.out.println("close");
        System.out.println("Listo");
    }

    public static void main(String[] args) {
        new ConsultaPruebaOrlando();
    }
}
