package dataBase;

import com.jswitch.base.modelo.HibernateUtil;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;

/**
 * Crea las vistas en la base de datos
 * @author Luis Adrian Gonzalez Benavides 
 * @author bc
 */
public class ActualizarVistas {

    /**
     * Crea las vistas en la base de datos
     */
    public ActualizarVistas() {

        Session s = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        System.out.println("DROP TABLES");
        try {
            System.out.println("view_sumafactura:");
            s.createSQLQuery("DROP TABLE IF EXISTS view_sumafactura").executeUpdate();
        } catch (Exception ex) {
            tx.rollback();
            tx = s.beginTransaction();
        }
        try {
            System.out.println("view_sumadetalle:");
            s.createSQLQuery("DROP TABLE IF EXISTS view_sumadetalle").executeUpdate();
            tx.commit();
        } catch (Exception ex) {
            tx.rollback();
        }
        tx = s.beginTransaction();
        try {
            System.out.println("view_sumaorden:");
            s.createSQLQuery("DROP TABLE IF EXISTS view_sumaorden").executeUpdate();
            tx.commit();
        } catch (Exception ex) {
            tx.rollback();
        }
        tx = s.beginTransaction();
        try {
            System.out.println("view_sumaremesa:");
            s.createSQLQuery("DROP TABLE IF EXISTS view_sumaremesa").executeUpdate();
            tx.commit();
        } catch (Exception ex) {
            tx.rollback();
        }
        tx = s.beginTransaction();
        try {
            System.out.println("view_sumapartida:");
            s.createSQLQuery("DROP TABLE IF EXISTS view_sumapartida").executeUpdate();
            tx.commit();
        } catch (Exception ex) {
            tx.rollback();
        }
        tx = s.beginTransaction();
        try {
            System.out.println("view_listadiagnostico:");
            s.createSQLQuery("DROP TABLE IF EXISTS view_listadiagnostico").executeUpdate();
            tx.commit();
        } catch (Exception ex) {
            tx.rollback();
        }
        tx = s.beginTransaction();
        try {
            System.out.println("view_agotamiento:");
            s.createSQLQuery("DROP TABLE IF EXISTS view_agotamiento").executeUpdate();
            tx.commit();
        } catch (Exception ex) {
            tx.rollback();
        }
        tx = s.beginTransaction();

        s.createSQLQuery(" DROP VIEW IF EXISTS view_agotamiento CASCADE;"
                + " CREATE OR REPLACE VIEW view_agotamiento AS "
                + " SELECT (siniestro2_.asegurado_id||'-'||diagnostic0_.diagnostico_id||'-'|| siniestro2_.ayo) AS id,"
                + "siniestro2_.asegurado_id, diagnostic0_.diagnostico_id, sum("
                + " CASE"
                + " WHEN upper(estatussin7_.nombre) = 'PAGADO' THEN diagnostic0_.montopagado"
                + " ELSE 0"
                + " END) AS montopagado, sum("
                + " CASE"
                + " WHEN upper(estatussin7_.nombre) = 'PENDIENTE' THEN diagnostic0_.montopendiente + diagnostic0_.montopagado"
                + " ELSE 0"
                + " END) AS montopendiente, siniestro2_.ayo"
                + " FROM sini_diagnosticosiniestro diagnostic0_, sini_detallesiniestro detallesin1_, sini_siniestro siniestro2_, sini_etapasiniestro etapasinie6_, sini_estatussiniestro estatussin7_"
                + " WHERE diagnostic0_.detallesiniestro_id = detallesin1_.id AND detallesin1_.siniestro_id = siniestro2_.id AND detallesin1_.etapasiniestro_id = etapasinie6_.id AND etapasinie6_.estatussiniestro_id = estatussin7_.id GROUP BY siniestro2_.asegurado_id, siniestro2_.ayo, diagnostic0_.diagnostico_id;"
                + " ALTER TABLE view_agotamiento OWNER TO postgres;"
                + " DROP VIEW IF EXISTS view_sumafactura CASCADE;"
                + " CREATE OR REPLACE VIEW view_sumafactura AS "
                + " SELECT sini_factura.id,"
                + "  sum(sini_factura.totalliquidado * sini_factura.porcentajeretencionprontopago) AS montoretencionprontopago,"
                + "  sum(sini_factura.totalliquidado * sini_factura.porcentajeretenciontm) AS montoretenciontm,"
                + "  sum("
                + "  (sini_factura.montoretenciondeducible + sini_factura.montoretencioniva + sini_factura.montoretencionislr) "
                + " + (sini_factura.totalliquidado * sini_factura.porcentajeretenciontm)"
                + " + (sini_factura.totalliquidado * sini_factura.porcentajeretencionprontopago)"
                + " ) AS totalretenido, "
                + "  sum"
                + "  (sini_factura.totalliquidado - "
                + " 	 ("
                + "  (sini_factura.montoretenciondeducible + sini_factura.montoretencioniva + sini_factura.montoretencionislr) "
                + " + (sini_factura.totalliquidado * sini_factura.porcentajeretenciontm)"
                + " + (sini_factura.totalliquidado * sini_factura.porcentajeretencionprontopago)"
                + " )"
                + "  )"
                + "  AS totalacancelar"
                + "    FROM sini_factura"
                + "   WHERE sini_factura.activo = true"
                + "   GROUP BY sini_factura.id;"
                + " ALTER TABLE view_sumafactura OWNER TO postgres;"
                + " CREATE OR REPLACE VIEW view_sumadetalle AS "
                + " SELECT sini_factura.detallesiniestro_id AS id,"
                + " count(sini_factura.id) AS cantidadfacturas,"
                + " sum(sini_factura.baseislr) AS baseislr,"
                + " sum(sini_factura.baseiva) AS baseiva,"
                + " sum(sini_factura.gastosclinicos) AS gastosclinicos,"
                + " sum(sini_factura.honorariosmedicos) AS honorariosmedicos,"
                + " sum(sini_factura.montoamparado) AS montoamparado,"
                + " sum(sini_factura.montoretenciondeducible) AS montoretenciondeducible,"
                + " sum(view_sumafactura.montoretencionprontopago) AS montoretencionprontopago,"
                + " sum(sini_factura.montoiva) AS montoiva,"
                + " sum(sini_factura.montonoamparado) AS montonoamparado,"
                + " sum(sini_factura.montoretencionislr) AS montoretencionislr,"
                + " sum(sini_factura.montoretencioniva) AS montoretencioniva,"
                + " sum(view_sumafactura.montoretenciontm) AS montoretenciontm,"
                + " sum(view_sumafactura.totalacancelar) AS totalacancelar,"
                + " sum(sini_factura.totalfacturado) AS totalfacturado,"
                + " sum(sini_factura.totalliquidado) AS totalliquidado,"
                + " sum(view_sumafactura.totalretenido) AS totalretenido"
                + " FROM sini_factura"
                + " CROSS JOIN view_sumafactura"
                + " WHERE sini_factura.id = view_sumafactura.id"
                + " GROUP BY sini_factura.detallesiniestro_id;"
                + " ALTER TABLE view_sumadetalle OWNER TO postgres;"
                + " CREATE OR REPLACE VIEW view_sumaorden AS "
                + " SELECT detallesin1_.ordendepago_id AS id, count(sumadetall0_.id) AS cantidaddetalles, sum("
                + " CASE"
                + " WHEN titular5_.persona_id = asegurado8_.persona_id THEN 1"
                + " ELSE 0"
                + " END) AS numerosiniestrostitular, sum("
                + " CASE"
                + " WHEN titular5_.persona_id <> asegurado8_.persona_id THEN 1"
                + " ELSE 0"
                + " END) AS numerosiniestrosfamiliar, sum("
                + " CASE"
                + " WHEN titular5_.persona_id = asegurado8_.persona_id THEN sumadetall0_.totalliquidado"
                + " ELSE 0"
                + " END) AS montotitulares, sum("
                + " CASE"
                + " WHEN titular5_.persona_id <> asegurado8_.persona_id THEN sumadetall0_.totalliquidado"
                + " ELSE 0"
                + " END) AS montofamiliares, sum(sumadetall0_.baseislr) AS baseislr, sum(sumadetall0_.baseiva) AS baseiva, sum(sumadetall0_.cantidadfacturas) AS cantidadfacturas, sum(sumadetall0_.gastosclinicos) AS gastosclinicos, sum(sumadetall0_.honorariosmedicos) AS honorariosmedicos, sum(sumadetall0_.montoamparado) AS montoamparado, sum(sumadetall0_.montoretenciondeducible) AS montoretenciondeducible, sum(sumadetall0_.montoretencionprontopago) AS montoretencionprontopago, sum(sumadetall0_.montoiva) AS montoiva, sum(sumadetall0_.montonoamparado) AS montonoamparado, sum(sumadetall0_.montoretencionislr) AS montoretencionislr, sum(sumadetall0_.montoretencioniva) AS montoretencioniva, sum(sumadetall0_.montoretenciontm) AS montoretenciontm, sum(sumadetall0_.totalacancelar) AS totalacancelar, sum(sumadetall0_.totalfacturado) AS totalfacturado, sum(sumadetall0_.totalliquidado) AS totalliquidado, sum(sumadetall0_.totalretenido) AS totalretenido"
                + " FROM view_sumadetalle sumadetall0_"
                + " CROSS JOIN sini_detallesiniestro detallesin1_"
                + " CROSS JOIN sini_siniestro siniestro3_"
                + " CROSS JOIN aseg_certificado certificad4_"
                + " CROSS JOIN aseg_titular titular5_"
                + " CROSS JOIN aseg_asegurado asegurado8_"
                + " WHERE sumadetall0_.id = detallesin1_.id AND detallesin1_.siniestro_id = siniestro3_.id AND siniestro3_.certificado_id = certificad4_.id AND certificad4_.titular_id = titular5_.id AND siniestro3_.asegurado_id = asegurado8_.id AND detallesin1_.ordendepago_id IS NOT NULL"
                + " GROUP BY detallesin1_.ordendepago_id;"
                + " ALTER TABLE view_sumaorden OWNER TO postgres;"
                + " CREATE OR REPLACE VIEW view_sumaremesa AS "
                + " SELECT pago_ordendepago.remesa_id AS id,"
                + " count(view_sumaorden.id) AS cantidadordenes,"
                + " sum(view_sumaorden.cantidaddetalles) AS cantidaddetalles,"
                + " sum(view_sumaorden.numerosiniestrostitular) AS numerosiniestrostitular,"
                + " sum(view_sumaorden.numerosiniestrosfamiliar) AS numerosiniestrosfamiliar,"
                + " sum(view_sumaorden.montotitulares) AS montotitulares,"
                + " sum(view_sumaorden.montofamiliares) AS montofamiliares,"
                + " sum(view_sumaorden.baseislr) AS baseislr,"
                + " sum(view_sumaorden.baseiva) AS baseiva,"
                + " sum(view_sumaorden.cantidadfacturas) AS cantidadfacturas,"
                + " sum(view_sumaorden.gastosclinicos) AS gastosclinicos,"
                + " sum(view_sumaorden.honorariosmedicos) AS honorariosmedicos,"
                + " sum(view_sumaorden.montoamparado) AS montoamparado,"
                + " sum(view_sumaorden.montoretenciondeducible) AS montoretenciondeducible,"
                + " sum(view_sumaorden.montoretencionprontopago) AS montoretencionprontopago,"
                + " sum(view_sumaorden.montoiva) AS montoiva,"
                + " sum(view_sumaorden.montonoamparado) AS montonoamparado,"
                + " sum(view_sumaorden.montoretencionislr) AS montoretencionislr,"
                + " sum(view_sumaorden.montoretencioniva) AS montoretencioniva,"
                + " sum(view_sumaorden.montoretenciontm) AS montoretenciontm,"
                + " sum(view_sumaorden.totalacancelar) AS totalacancelar,"
                + " sum(view_sumaorden.totalfacturado) AS totalfacturado,"
                + " sum(view_sumaorden.totalliquidado) AS totalliquidado,"
                + " sum(view_sumaorden.totalretenido) AS totalretenido"
                + " FROM view_sumaorden"
                + " CROSS JOIN pago_ordendepago"
                + " WHERE view_sumaorden.id = pago_ordendepago.id AND  pago_ordendepago.remesa_id IS NOT NULL"
                + " GROUP BY pago_ordendepago.remesa_id;"
                + " ALTER TABLE view_sumaremesa OWNER TO postgres;"
                + "CREATE OR REPLACE VIEW view_sumapartida AS "
                + " SELECT (detallesin0_.ordendepago_id||'-'||tipocontra4_.partidapresupuestaria_id) AS id,"
                + " tipocontra4_.partidapresupuestaria_id partidapresupuestaria_id,"
                + " detallesin0_.ordendepago_id ordendepago_id,"
                + " count(suma.id) AS cantidaddetalles, sum(suma.cantidadfacturas) AS cantidadfacturas, sum(suma.baseislr) AS baseislr, sum(suma.baseiva) AS baseiva, sum(suma.gastosclinicos) AS gastosclinicos, sum(suma.honorariosmedicos) AS honorariosmedicos, sum(suma.montoamparado) AS montoamparado, sum(suma.montoretenciondeducible) AS montoretenciondeducible, sum(suma.montoretencionprontopago) AS montoretencionprontopago, sum(suma.montoiva) AS montoiva, sum(suma.montonoamparado) AS montonoamparado, sum(suma.montoretencionislr) AS montoretencionislr, sum(suma.montoretencioniva) AS montoretencioniva, sum(suma.montoretenciontm) AS montoretenciontm, sum(suma.totalacancelar) AS totalacancelar, sum(suma.totalfacturado) AS totalfacturado, sum(suma.totalliquidado) AS totalliquidado, sum(suma.totalretenido) AS totalretenido"
                + " FROM sini_detallesiniestro detallesin0_"
                + " CROSS JOIN view_sumadetalle suma"
                + " CROSS JOIN sini_siniestro siniestro1_"
                + " CROSS JOIN aseg_certificado certificad2_"
                + " CROSS JOIN aseg_titular titular3_"
                + " CROSS JOIN aseg_tipocontrato tipocontra4_"
                + " WHERE detallesin0_.siniestro_id = siniestro1_.id AND suma.id = detallesin0_.id AND siniestro1_.certificado_id = certificad2_.id AND certificad2_.titular_id = titular3_.id AND titular3_.tipocontrato_id = tipocontra4_.id"
                + " GROUP BY tipocontra4_.partidapresupuestaria_id, detallesin0_.ordendepago_id;"
                + " ALTER TABLE view_sumapartida OWNER TO postgres;"
                + " DROP VIEW IF EXISTS view_listadiagnostico CASCADE;"
                + " CREATE OR REPLACE VIEW view_listadiagnostico AS "
                + " SELECT (siniestro.id||'-'||detalleSiniestro.id||'-'||diagnosticoSiniestro.id) id,"
                + " siniestro.id siniestro_id,"
                + " detalleSiniestro.id detalleSiniestro_id,"
                + " diagnosticoSiniestro.id diagnosticoSiniestro_id"
                + " FROM "
                + " sini_siniestro siniestro,"
                + " sini_detalleSiniestro detalleSiniestro,"
                + " sini_diagnosticoSiniestro diagnosticoSiniestro"
                + " WHERE siniestro.id=detalleSiniestro.siniestro_id"
                + " AND diagnosticoSiniestro.detalleSiniestro_id=detalleSiniestro.id;"
                + " ALTER TABLE view_listadiagnostico OWNER TO postgres;").executeUpdate();

        tx.commit();
        System.out.println("Vistas Creadas");
        s.close();
        System.out.println("Listo");
    }

    public static void main(String[] args) {
        new ActualizarVistas();
    }
}
