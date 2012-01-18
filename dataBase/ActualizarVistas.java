package dataBase;

import com.jswitch.base.modelo.HibernateUtil;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;

/**
 *
 * @author bc
 */
public class ActualizarVistas {

    public ActualizarVistas() {
        System.out.println("Act Vistas");
        Session s = HibernateUtil.getSessionFactory().openSession();
        System.out.println("sesion abierta");

        Transaction tx = s.beginTransaction();
        System.out.println("transaccion begin");

        //System.out.println(s.createSQLQuery("DROP VIEW IF EXISTS vista1").executeUpdate());
        //System.out.println(s.createSQLQuery("DROP TABLE IF EXISTS vista1").executeUpdate());

        System.out.println("tablas borradas");

        String v="CREATE OR REPLACE VIEW vista1 AS SELECT nextval('xxx') AS id, sini_diagnosticosiniestro.usuarioinsert AS nombre, sini_diagnosticosiniestro.montopagado + sini_diagnosticosiniestro.montopendiente AS suma   FROM sini_diagnosticosiniestro;";
        v+="ALTER TABLE vista1 OWNER TO postgres;";
        
        System.out.println(s.createSQLQuery(v).executeUpdate());
        
        tx.commit();
        System.out.println("comit");
        s.close();
        System.out.println("close");
        System.out.println("Listo");
    }

    public static void main(String[] args) {
        new ActualizarVistas();
    }
}
