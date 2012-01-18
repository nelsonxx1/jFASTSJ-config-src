package dataBase;

import com.jswitch.asegurados.modelo.maestra.Asegurado;
import com.jswitch.base.modelo.HibernateUtil;
import com.jswitch.configuracion.modelo.dominio.patologias.Diagnostico;
import com.jswitch.configuracion.modelo.transaccional.SumaAsegurada;
import com.jswitch.siniestros.modelo.maestra.DiagnosticoSiniestro;
import org.hibernate.classic.Session;

/**
 *
 * @author bc
 */
public class ActualizarParametros_1 {

    public ActualizarParametros_1() {
        System.out.println("Act Parametros");
        Session s = HibernateUtil.getSessionFactory().openSession();
        System.out.println("sesion abierta");

        //Transaction tx = s.beginTransaction();
        System.out.println("transaccion begin");

        System.out.println("---------*----------------");
//        Diagnostico d = (Diagnostico) s.createQuery("FROM " + Diagnostico.class.getName()).setMaxResults(1).uniqueResult();
//        Asegurado a = (Asegurado) s.createQuery("FROM " + Asegurado.class.getName()).setMaxResults(1).uniqueResult();
//
//        System.out.println(d.getEspecialidad().getRamo().getNombre());
//        System.out.println(d.getEspecialidad().getNombre());
//        System.out.println(d.getNombre());

//        Double totalCoberturaDiagnosticoPlan = (Double) s.createQuery("SELECT sumaAmparada.monto FROM "
//                + SumaAsegurada.class.getName() + " WHERE diagnostico.id=:diag AND "
//                + "plan.id=:plan").setLong("diag", d.getId()).setLong("plan", a.getPlan().getId()).uniqueResult();
//
//        System.out.println(totalCoberturaDiagnosticoPlan);

        s.createQuery("SELECT D.detalleSiniestro.siniestro.ayo, D.detalleSiniestro.siniestro.asegurado.id, "
                + "D.diagnostico.id, SUM(D.montoPagado) "
                + " FROM "
                + DiagnosticoSiniestro.class.getName() + " as D WHERE "
                //+ "detalleSiniestro.siniestro.asegurado.id=:aseg AND "
                //+ "diagnostico.id=:diag AND "
                + " UPPER(D.detalleSiniestro.etapaSiniestro.estatusSiniestro.nombre)=:estatus "
                + " GROUP BY D.detalleSiniestro.siniestro.asegurado.id,"
                + " D.detalleSiniestro.siniestro.ayo, D.diagnostico.id") //.setLong("aseg", a.getId())
                //.setLong("diag", d.getId())
                .setString("estatus", "PAGADO").list();

        //System.out.println(pagado);
//        Double pendiente = (Double) s.createQuery("SELECT SUM(montoPagado+montoPendiente) FROM "
//                + DiagnosticoSiniestro.class.getName() + " WHERE "
//                + "detalleSiniestro.siniestro.asegurado.id=:aseg AND "
//                + "diagnostico.id=:diag AND "
//                + "UPPER(detalleSiniestro.etapaSiniestro.estatusSiniestro.nombre)=:estatus")
//                .setLong("aseg", a.getId())
//                .setLong("diag", d.getId())
//                .setString("estatus", "PENDIENTE")
//                .uniqueResult();
//
//        System.out.println(pendiente);

        System.out.println("-----------***---------------------");
        System.out.println("comit");
        s.close();
        System.out.println("close");
        System.out.println("Listo");
    }

    public static void main(String[] args) {
        new ActualizarParametros_1();
    }
}
