package dataBase;

import java.util.ArrayList;
import java.util.Date;
import com.jswitch.base.modelo.Dominios;
import com.jswitch.base.modelo.HibernateUtil;
import com.jswitch.reporte.modelo.Reporte;
import com.jswitch.base.modelo.entidades.auditoria.AuditoriaBasica;
import com.jswitch.pagos.modelo.maestra.OrdenDePago;
import com.jswitch.persona.modelo.maestra.Persona;
import com.jswitch.persona.modelo.maestra.PersonaNatural;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;

/**
 *
 * @author bc
 */
public class ActualizarReportes {

    public ActualizarReportes() {

        System.out.println("Act Reportes");
        Session s = HibernateUtil.getSessionFactory().openSession();
        System.out.println("sesion abierta");

        Transaction tx = s.beginTransaction();
        System.out.println("transaccion begin");

        System.out.println(s.createQuery("DELETE FROM " + Reporte.class.getName()).executeUpdate());

        System.out.println("viejos borrados ");

        AuditoriaBasica ab = new AuditoriaBasica(new Date(), "defaultdata", true);

        System.out.println("metodo reportes");

        ArrayList<Reporte> list = new ArrayList<Reporte>(0);

        list.add(new Reporte(Dominios.CategoriaReporte.PERSONAS, 0, "PER-D001", "Personas x Nombre", "Todas las Personas", "FROM " + Persona.class.getName() + " as P ORDER BY nombreLargo", "Carta 8½ x 11 Vertical", false, true, true));
        list.add(new Reporte(Dominios.CategoriaReporte.PERSONAS, 0, "PER-D002", "Personas, Telefono, Direccion", "Todas las Personas con sus Telefonos y Direcciones", "FROM " + Persona.class.getName() + " as P ORDER BY nombreLargo", "Carta 8½ x 11 Vertical", false, true, true));
        list.add(new Reporte(Dominios.CategoriaReporte.PERSONAS, 0, "PER-D003", "Personas Naturales, Fecha Nacimiento, Sexo, Telefono y Direccion.", "Personas segun su Tipo, con Telefonos y Direccions", "FROM " + PersonaNatural.class.getName() + " as P ORDER BY nombreLargo", "Carta 8½ x 11 Vertical", false, true, true));
        
        list.add(new Reporte(Dominios.CategoriaReporte.REMESA, 0, "REM-R001", "Listado de Ordenes de Pago por Remesa", "Agrupados por Remesa, Nombre de Persona a Pagar", "FROM " + OrdenDePago.class.getName() + " as P ORDER BY P.remesa.id, P.numeroOrden", "Carta 8½ x 11 Vertical", false, true, true));        
        
        list.add(new Reporte(Dominios.CategoriaReporte.SINIESTROS, 0, "REM-R001", "Listado de Ordenes de Pago por Remesa", "Agrupados por Remesa, Nombre de Persona a Pagar", "FROM " + OrdenDePago.class.getName() + " as P ORDER BY P.remesa.id, P.numeroOrden", "Carta 8½ x 11 Vertical", false, true, true));        

        list.add(new Reporte(Dominios.CategoriaReporte.x1, 0, "xxx3", "xx", "xx", "select id, montopagado, tratamientoescrito from sini_diagnosticosiniestro", "Carta 8½ x 11 Vertical", true, false, false));

        for (Reporte o : list) {
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
        new ActualizarReportes();
    }
}
