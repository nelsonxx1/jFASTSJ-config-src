package dataBase;

import com.jswitch.asegurados.modelo.maestra.Asegurado;
import java.util.ArrayList;
import java.util.Date;
import com.jswitch.base.modelo.Dominios;
import com.jswitch.base.modelo.HibernateUtil;
import com.jswitch.reporte.modelo.Reporte;
import com.jswitch.base.modelo.entidades.auditoria.AuditoriaBasica;
import com.jswitch.pagos.modelo.maestra.Factura;
import com.jswitch.pagos.modelo.maestra.OrdenDePago;
import com.jswitch.pagos.modelo.maestra.Remesa;
import com.jswitch.persona.modelo.maestra.Persona;
import com.jswitch.persona.modelo.maestra.PersonaNatural;
import com.jswitch.siniestros.modelo.maestra.DetalleSiniestro;
import com.jswitch.siniestros.modelo.maestra.DiagnosticoSiniestro;
import com.jswitch.siniestros.modelo.maestra.detalle.Emergencia;
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

        list.add(new Reporte(Dominios.CategoriaReporte.PERSONAS, 0, "PER-D001", "Personas x Nombre", "Todas las Personas", "FROM " + Persona.class.getName() + " as P ORDER BY nombreLargo", "Carta 8½ x 11 Vertical", false, true, true,false));
        list.add(new Reporte(Dominios.CategoriaReporte.PERSONAS, 0, "PER-D002", "Personas, Telefono, Direccion", "Todas las Personas con sus Telefonos y Direcciones", "FROM " + Persona.class.getName() + " as P ORDER BY nombreLargo", "Carta 8½ x 11 Vertical", false, true, true, false));
        list.add(new Reporte(Dominios.CategoriaReporte.PERSONAS, 0, "PER-D003", "Personas Naturales, Fecha Nacimiento, Sexo, Telefono y Direccion.", "Personas segun su Tipo, con Telefonos y Direccions", "FROM " + PersonaNatural.class.getName() + " as P ORDER BY nombreLargo", "Carta 8½ x 11 Vertical", false, true, true, false));
        list.add(new Reporte(Dominios.CategoriaReporte.PERSONAS, 0, "PER-D004", "Personas x Tipo", "Todas las Personas", "SELECT DISTINCT P.nombreLargo AS nombreLargo, T.nombre AS nombre FROM com.jswitch.persona.modelo.maestra.Persona AS P LEFT JOIN P.tiposPersona AS T ORDER BY T.nombre, P.nombreLargo", "Carta 8½ x 11 Vertical", false, true, true,true));
        list.add(new Reporte(Dominios.CategoriaReporte.PERSONAS, 0, "PER-D005", "Personas x Banco", "Todas las Personas", "SELECT DISTINCT P.nombreLargo AS nombreLargo, B.banco.nombreLargo AS nombre FROM com.jswitch.persona.modelo.maestra.Persona AS P LEFT JOIN P.cuentasBancarias AS B ORDER BY B.banco.nombreLargo, P.nombreLargo", "Carta 8½ x 11 Vertical", false, true, true,true));

        list.add(new Reporte(Dominios.CategoriaReporte.ASEGURADOS, 0, "PAGO_D_FACTURAS_001", 
                "ASEGURADOS ACTUALES EN EL FONDO ADMINISTRADO DE SALUD", 
                "Todos los Asegurados que NO han egresado", 
                "FROM " + Asegurado.class.getName() + " as P "
                + "WHERE P.detalleSiniestro.personaPago.id=18017 AND P.detalleSiniestro.etapaSiniestro.idPropio IN ('LIQ','ORD_PAG') "
                + "ORDER BY P.detalleSiniestro.personaPago.id, P.detalleSiniestro.siniestro.certificado.titular.tipoContrato.id ", "Carta 8½ x 11 Vertical", 
                false, true, true, false));                        
        
        list.add(new Reporte(Dominios.CategoriaReporte.PAGOS, 0, "PAGO_D_FACTURAS_001", 
                "PAGOS PENDIENTES", 
                "DESCRIP", 
                "FROM " + Factura.class.getName() + " as P "
                + "WHERE P.detalleSiniestro.personaPago.id=18017 AND P.detalleSiniestro.etapaSiniestro.idPropio IN ('LIQ','ORD_PAG') "
                + "ORDER BY P.detalleSiniestro.personaPago.id, P.detalleSiniestro.siniestro.certificado.titular.tipoContrato.id ", "Carta 8½ x 11 Vertical", 
                false, true, true, false));                
        

        list.add(new Reporte(Dominios.CategoriaReporte.PAGOS, 0, "PAGO_D_FACTURAS_001", 
                "PAGOS PAGADOS", 
                "DESCRIP", 
                "FROM " + Factura.class.getName() + " as P "
                + "WHERE P.detalleSiniestro.etapaSiniestro.estatusSiniestro.nombre IN ('PAGADO') "
                + "ORDER BY P.detalleSiniestro.siniestro.certificado.titular.tipoContrato.nombre, P.detalleSiniestro.personaPago.id", "Carta 8½ x 11 Vertical", 
                false, true, true, false));
    
        list.add(new Reporte(Dominios.CategoriaReporte.SINIESTROS, 0, "SINI_APS_D001", 
                "ORDEN DE APS", 
                "Orden de Atencion Médica Primaria", 
                "FROM " + DetalleSiniestro.class.getName() + " as P "
                + "WHERE P.id=31812 ",
                //+ "ORDER BY P.detalleSiniestro.siniestro.certificado.titular.tipoContrato.nombre, P.detalleSiniestro.personaPago.id", 
                "Carta 8½ x 11 Vertical", 
                false, true, true, false));              

        list.add(new Reporte(Dominios.CategoriaReporte.EMERGENCIAS, 0, "SINI_EMERGENCIA_D001", 
                "ORDEN DE EMERGECIA", 
                "Orden de Emergencia", 
                "FROM " + Emergencia.class.getName() + " as P "
                + "WHERE P.id=33239 ",
                //+ "ORDER BY P.detalleSiniestro.siniestro.certificado.titular.tipoContrato.nombre, P.detalleSiniestro.personaPago.id", 
                "Carta 8½ x 11 Vertical", 
                false, true, true, false));            
        
        list.add(new Reporte(Dominios.CategoriaReporte.REMESA, 0, "REM-R001", "Listado de Ordenes de Pago por Remesa", "Agrupados por Remesa, Nombre de Persona a Pagar", "FROM " + OrdenDePago.class.getName() + " as P ORDER BY P.remesa.id, P.numeroOrden", "Carta 8½ x 11 Vertical", false, true, true, false));                
        list.add(new Reporte(Dominios.CategoriaReporte.REMESA, 0, "REM-R003", "Listado de Remesas", "Remesas cargadas", "FROM " + Remesa.class.getName() + " as P ORDER BY P.fechaPago", "Carta 8½ x 11 Horizontal", false, true, true, false));                
        
        list.add(new Reporte(Dominios.CategoriaReporte.SINIESTROS, 0, "SIN-R001", "Listado de Diagnosticos Sinoiestros de Pago por Remesa", "Agrupados por Remesa, Nombre de Persona a Pagar", "SELECT P.diagnostico , P.montoPagado  FROM com.jswitch.siniestros.modelo.maestra.DiagnosticoSiniestro as P WHERE P.id=30921 ORDER BY P.diagnostico.nombre", "Carta 8½ x 11 Vertical", false, true, false, false));
//        list.add(new Reporte(Dominios.CategoriaReporte.SINIESTROS, 0, "SIN-R001", "Listado de Diagnosticos Sinoiestros de Pago por Remesa", "Agrupados por Remesa, Nombre de Persona a Pagar", "FROM " + DiagnosticoSiniestro.class.getName() + " as P WHERE P.id=30921 ORDER BY P.diagnostico.nombre", "Carta 8½ x 11 Vertical", false, true, false));        

        list.add(new Reporte(Dominios.CategoriaReporte.x1, 0, "xxx3", "xx", "xx", "select id, montopagado, tratamientoescrito from sini_diagnosticosiniestro", "Carta 8½ x 11 Vertical", true, false, false, false));
        list.add(new Reporte(Dominios.CategoriaReporte.x1, 0, "x2", "x2", "x2", "", "Carta 8½ x 11 Vertical", false, true, false, false));

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
