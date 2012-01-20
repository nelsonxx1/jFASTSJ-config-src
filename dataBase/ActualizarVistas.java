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
        String v="";
//        String v="CREATE OR REPLACE VIEW vista1 AS SELECT nextval('xxx') AS id, sini_diagnosticosiniestro.usuarioinsert AS nombre, sini_diagnosticosiniestro.montopagado + sini_diagnosticosiniestro.montopendiente AS suma   FROM sini_diagnosticosiniestro;";
//        v+="ALTER TABLE vista1 OWNER TO postgres;";
//        
//        System.out.println(s.createSQLQuery(v).executeUpdate());
        
        v="CREATE OR REPLACE VIEW view_agotamiento AS "
                + "SELECT siniestro2_.asegurado_id, diagnostic0_.diagnostico_id, sum("
                + "        CASE"
                + "            WHEN upper(estatussin7_.nombre) = 'PAGADO' THEN diagnostic0_.montopagado"
                + "            ELSE 0"
                + "        END) AS montopagado, sum("
                + "        CASE"
                + "            WHEN upper(estatussin7_.nombre) = 'PENDIENTE' THEN diagnostic0_.montopendiente + diagnostic0_.montopagado"
                + "            ELSE 0"
                + "        END) AS montopendiente, siniestro2_.ayo"
                + "   FROM sini_diagnosticosiniestro diagnostic0_, sini_detallesiniestro detallesin1_, sini_siniestro siniestro2_, sini_etapasiniestro etapasinie6_, sini_estatussiniestro estatussin7_"
                + "  WHERE diagnostic0_.detallesiniestro_id = detallesin1_.id AND detallesin1_.siniestro_id = siniestro2_.id AND detallesin1_.etapasiniestro_id = etapasinie6_.id AND etapasinie6_.estatussiniestro_id = estatussin7_.id  GROUP BY siniestro2_.asegurado_id, siniestro2_.ayo, diagnostic0_.diagnostico_id;"
                + "ALTER TABLE view_agotamiento OWNER TO postgres;";
        
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
