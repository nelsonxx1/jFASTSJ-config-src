package dataBase;

import com.jswitch.base.modelo.HibernateUtil;
import com.jswitch.vistasbd.vista1;
import java.util.List;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;

/**
 *
 * @author bc
 */
public class ActualizarParametros_2 {

    public ActualizarParametros_2() {
        System.out.println("Act Parametros");
        Session s = HibernateUtil.getSessionFactory().openSession();
        System.out.println("sesion abierta");

        Transaction tx = s.beginTransaction();
        System.out.println("transaccion begin");

        //s.createQuery("SELECT tipoPersona.nombre as numeroSiniestros FROM "+DetalleSiniestro.class.getName()).list();

        List l=s.createQuery("FROM "+vista1.class.getName()).list();
        
        System.out.println(l);
        
        tx.commit();
        System.out.println("comit");
        s.close();
        System.out.println("close");
        System.out.println("Listo");
    }

    public static void main(String[] args) {
        new ActualizarParametros_2();
    }
}
