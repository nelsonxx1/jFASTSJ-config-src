package dataBase;

import com.jswitch.base.controlador.licencia.Equipo;
import java.util.Calendar;
import java.util.Date;
import com.jswitch.base.modelo.entidades.Empresa;
import com.jswitch.base.modelo.entidades.Licencia;
import com.jswitch.base.modelo.entidades.Usuario;
import com.jswitch.base.modelo.entidades.auditoria.AuditoriaBasica;

/**
 *
 * @author bc
 */
public class Crear2 extends Crear {

    public Crear2() {
    }

    @Override
    public void create() {
        empresa();
        licencia();
        byte[] sha256 = {31, -35, -28, -37, -15, -21, 90, 22, 46, 71, -99, 89, 72, -57, 101, -112, 86, -114, 28, -47, -89, -23, 93, 19, 73, 32, -17, -76, 88, 89, -47, -68};
        byte[] md5 = {-112, 30, 11, 103, -81, 109, -46, 47, -8, 94, 79, -3, -17, -78, 54, 26};
        Usuario u = new Usuario("Orlando Becerra", "bcrra", md5, new AuditoriaBasica(), false, true, true);
        s.save(u);
        super.create();
        personas2();
    }

    private void empresa() {
        Empresa empresa = new Empresa("V18256939-1", "NombreEmpresa", "nombreEmpresajSipolee@gmail.com", "jsipoleeSoporte@gmail.com", "/jSipolEE/DocDigital", "./Reportes");
        s.save(empresa);
    }

    private void licencia() {
        Calendar c = Calendar.getInstance();
        c.set(2010, 11, 31);
        //Licencia licencia = new Licencia("bc-4427d1d1eeff", "MI CASA AP", Equipo.encodeText("6898-8EF5"), "vfw:Microsoft WDM Image Capture (Win32):0", true, new Date(), c.getTime(), false);
        Licencia licencia2 = new Licencia("orlandobcrra-pc", "portatil", Equipo.encodeText("2400-AB67"), "vfw:Microsoft WDM Image Capture (Win32):0", true, new Date(), c.getTime(), false, 15);
        //s.save(licencia);
        s.save(licencia2);
    }

    public void personas2() {
       
    }

    public static void main(String[] args) {
        try {
            Crear bd = new Crear2();
            bd.create();
            bd.data();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
