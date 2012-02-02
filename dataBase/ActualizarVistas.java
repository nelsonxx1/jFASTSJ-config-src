package dataBase;

import com.jswitch.base.modelo.HibernateUtil;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;

/**
 * Crea las vistas en la base de datos
 * 
 * @author bc
 */
public class ActualizarVistas {

    /**
     * Crea las vistas en la base de datos
     */
    public ActualizarVistas() {
        Session s = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        System.out.println(s.createSQLQuery("DROP TABLE IF EXISTS vista1,view_sumadetalle, view_agotamiento").executeUpdate());
        String v = "";
        v = "CREATE OR REPLACE VIEW view_agotamiento AS "
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
        v = "CREATE OR REPLACE VIEW view_sumadetalle AS "
                + "SELECT sini_factura.detallesiniestro_id AS id,"
                + " count(*) AS cantidadfacturas,"
                + " sum(sini_factura.baseislr) AS baseislr,"
                + " sum(sini_factura.baseiva) AS baseiva, sum(sini_factura.gastosclinicos) AS gastosclinicos, "
                + " sum(sini_factura.honorariosmedicos) AS honorariosmedicos, sum(sini_factura.montoamparado) AS montoamparado,"
                + " sum(sini_factura.montodeducible) AS montodeducible, "
                + " sum(sini_factura.montodescuentoprontopago) AS montodescuentoprontopago, sum(sini_factura.montoiva) AS montoiva,"
                + " sum(sini_factura.montonoamparado) AS montonoamparado, sum(sini_factura.montoretencionisrl) AS montoretencionisrl, sum(sini_factura.montoretencioniva) AS montoretencioniva, sum(sini_factura.montotm) AS montotm, sum(sini_factura.sustraendo) AS sustraendo, sum(sini_factura.totalacancelar) AS totalacancelar, sum(sini_factura.totalfacturado) AS totalfacturado, sum(sini_factura.totalliquidado) AS totalliquidado, sum(sini_factura.totalretenido) AS totalretenido "
                + "FROM sini_factura"
                + "GROUP BY sini_factura.detallesiniestro_id;"
                + "ALTER TABLE view_sumadetalle OWNER TO postgres;";
        System.out.println(s.createSQLQuery(v).executeUpdate());
        v = "CREATE OR REPLACE VIEW view_sumaorden AS "
                + "SELECT detallesin1_.ordendepago_id AS id, count(sumadetall0_.id) AS cantidaddetalles, sum("
                + "        CASE"
                + "            WHEN titular5_.persona_id = asegurado8_.persona_id THEN 1"
                + "            ELSE 0"
                + "        END) AS numerosiniestrostitular, sum("
                + "        CASE"
                + "            WHEN titular5_.persona_id <> asegurado8_.persona_id THEN 1"
                + "            ELSE 0"
                + "        END) AS numerosiniestrosfamiliar, sum("
                + "        CASE"
                + "            WHEN titular5_.persona_id = asegurado8_.persona_id THEN sumadetall0_.montoamparado"
                + "            ELSE 0::double precision"
                + "        END) AS montotitulares, sum("
                + "        CASE"
                + "            WHEN titular5_.persona_id <> asegurado8_.persona_id THEN sumadetall0_.montoamparado"
                + "            ELSE 0::double precision"
                + "        END) AS montofamiliares, sum(sumadetall0_.baseislr) AS baseislr, sum(sumadetall0_.baseiva) AS baseiva, sum(sumadetall0_.cantidadfacturas) AS cantidadfactura, sum(sumadetall0_.gastosclinicos) AS gastosclinicos, sum(sumadetall0_.honorariosmedicos) AS honorariosmedicos, sum(sumadetall0_.montoamparado) AS montoamparado, sum(sumadetall0_.montodeducible) AS montodeducible, sum(sumadetall0_.montodescuentoprontopago) AS montodescuentoprontopago, sum(sumadetall0_.montoiva) AS montoiva, sum(sumadetall0_.montonoamparado) AS montonoamparado, sum(sumadetall0_.montoretencionisrl) AS montoretencionisrl, sum(sumadetall0_.montoretencioniva) AS montoretencioniva, sum(sumadetall0_.montotm) AS montotm, sum(sumadetall0_.sustraendo) AS sustraendo, sum(sumadetall0_.totalacancelar) AS totalacancelar, sum(sumadetall0_.totalfacturado) AS totalfacturado, sum(sumadetall0_.totalliquidado) AS totalliquidado, sum(sumadetall0_.totalretenido) AS totalretenido"
                + "   FROM view_sumadetalle sumadetall0_"
                + "  CROSS JOIN sini_detallesiniestro detallesin1_"
                + "  CROSS JOIN sini_siniestro siniestro3_"
                + "  CROSS JOIN aseg_certificado certificad4_     "
                + "     CROSS JOIN aseg_titular titular5_"
                + "  CROSS JOIN aseg_asegurado asegurado8_"
                + "  WHERE sumadetall0_.id = detallesin1_.id AND detallesin1_.siniestro_id = siniestro3_.id AND siniestro3_.certificado_id = certificad4_.id AND certificad4_.titular_id = titular5_.id AND siniestro3_.asegurado_id = asegurado8_.id AND detallesin1_.ordendepago_id IS NOT NULL"
                + "  GROUP BY detallesin1_.ordendepago_id;"
                + "ALTER TABLE view_sumaorden OWNER TO postgres;";
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
