package dataBase;

import java.util.ArrayList;
import java.util.Date;
import com.jswitch.base.modelo.Dominios;
import com.jswitch.base.modelo.HibernateUtil;
import com.jswitch.base.modelo.entidades.CalendarioBancario;
import com.jswitch.persona.modelo.dominio.Estado;
import com.jswitch.base.modelo.entidades.auditoria.AuditoriaBasica;
import com.jswitch.base.modelo.entidades.Usuario;
import com.jswitch.base.modelo.entidades.defaultData.DefaultData;
import com.jswitch.persona.modelo.dominio.TipoActividadEconomica;
import com.jswitch.persona.modelo.dominio.TipoCapacidadEconomica;
import com.jswitch.persona.modelo.dominio.TipoCodigoArea;
import com.jswitch.persona.modelo.dominio.TipoCuentaBancaria;
import com.jswitch.persona.modelo.dominio.TipoDireccion;
import com.jswitch.persona.modelo.dominio.TipoPersona;
import com.jswitch.persona.modelo.dominio.TipoTelefono2;
import com.jswitch.persona.modelo.maestra.Persona;
import com.jswitch.persona.modelo.maestra.PersonaJuridica;
import com.jswitch.persona.modelo.maestra.PersonaNatural;
import com.jswitch.persona.modelo.maestra.Rif;
import com.jswitch.base.modelo.entidades.TipoDocumento;
import com.jswitch.asegurados.modelo.dominio.Departamento;
import com.jswitch.asegurados.modelo.dominio.Parentesco;
import com.jswitch.configuracion.modelo.maestra.Plan;
import com.jswitch.configuracion.modelo.dominio.PlazoEspera;
import com.jswitch.asegurados.modelo.dominio.TipoContrato;
import com.jswitch.base.modelo.entidades.defaultData.ConfiguracionesGenerales;
import com.jswitch.configuracion.modelo.dominio.Ramo;
import com.jswitch.persona.modelo.maestra.LNPersonaNatural;
import com.jswitch.pagos.modelo.dominio.ConceptoSENIAT;
import com.jswitch.siniestros.modelo.dominio.EstatusSiniestro;
import com.jswitch.siniestros.modelo.dominio.EtapaSiniestro;
import com.jswitch.siniestros.modelo.dominio.TipoSiniestro;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;

/**
 * @author Orlando Becerra
 * @author Enrique Becerra
 */
public class Crear {

    protected Session s;
    protected Usuario userDD;
    protected Transaction tx;
    protected DefaultData defaultData;
    protected TipoPersona tpCompania;
    protected TipoPersona tpFinanciadora;
    protected TipoPersona tpBanco;
    protected TipoPersona tpProveedor;
    private TipoPersona tpLaboratiorio;
    protected TipoPersona tpTaxi;
    protected TipoPersona tpTaller;
    protected TipoPersona tpAsegurado;
    protected TipoPersona tpBeneficiario;
    protected TipoPersona tpContratante;
    protected AuditoriaBasica auditoriaActivo;
    protected AuditoriaBasica auditoriaInactivo;

    public Crear() {
    }

    public void openCreateDrop() {
        s = HibernateUtil.getAnnotationConfiguration().
                //setProperty("hibernate.show_sql", "true").
                //setProperty("hibernate.format_sql", "true").
                setProperty("hibernate.hbm2ddl.auto", "create-drop").
                buildSessionFactory().openSession();
        tx = s.beginTransaction();
        String dropSeq = "DROP SEQUENCE IF EXISTS seq_siniestro, seq_ordenpago, seq_remesa, seq_poliza;";
        s.createSQLQuery(dropSeq).executeUpdate();
        String seqSin = "CREATE SEQUENCE seq_siniestro INCREMENT 1 MINVALUE 1 MAXVALUE 999999 START 1 CACHE 1; ALTER TABLE seq_siniestro OWNER TO postgres;";
        s.createSQLQuery(seqSin).executeUpdate();
        String seqOrd = "CREATE SEQUENCE seq_ordenpago  INCREMENT 1 MINVALUE 1 MAXVALUE 999999 START 1 CACHE 1; ALTER TABLE seq_ordenpago OWNER TO postgres;";
        s.createSQLQuery(seqOrd).executeUpdate();
        String seqRem = "CREATE SEQUENCE seq_remesa  INCREMENT 1 MINVALUE 1 MAXVALUE 999999 START 1 CACHE 1; ALTER TABLE seq_remesa OWNER TO postgres;";
        s.createSQLQuery(seqRem).executeUpdate();
        String seqPol = "CREATE SEQUENCE seq_poliza  INCREMENT 1 MINVALUE 1 MAXVALUE 999999 START 1 CACHE 1; ALTER TABLE seq_poliza OWNER TO postgres;";
        s.createSQLQuery(seqPol).executeUpdate();

    }

    public void openNormal() {
        s = HibernateUtil.getSessionFactory().openSession();
        tx = s.beginTransaction();
    }

    public void close() {
        tx.commit();
        s.close();
    }

    public void create() {
        openCreateDrop();
        close();
    }

    public void data() {
        openNormal();
        defaultData = new DefaultData();
        datosPorDefecto();
        s.save(defaultData.persona);
        close();
    }

    private void datosPorDefecto() {
        userDD = new Usuario("defaultdata", "defaultdata", new byte[]{}, null, false, false, false);
        s.save(userDD);
        Date d = new Date();
        auditoriaActivo = new AuditoriaBasica(d, userDD.getUserName(), true);
        auditoriaInactivo = new AuditoriaBasica(d, userDD.getUserName(), false);
        configuracionesGenerales(auditoriaActivo);
        tiposPersona(auditoriaActivo, auditoriaInactivo);
        tiposActividadEconomica(auditoriaActivo);
        tipoCuentaBancaria(auditoriaActivo);
        tiposDocumento(auditoriaActivo);
        tiposDireccion(auditoriaActivo);
        tiposTelefono(auditoriaActivo);
        tiposCodigoArea(auditoriaActivo);
        tiposCapacidadEconomica(auditoriaActivo);
        estados(auditoriaActivo);
        personasDefault(auditoriaActivo);
        calendarioBancario(auditoriaActivo);
        parentescos(auditoriaActivo);
        planes(auditoriaActivo);
        plazosEsperas(auditoriaActivo);
        departamentos(auditoriaActivo);
        tiposContratos(auditoriaActivo);
        conceptoSENIAT(auditoriaActivo);
        tipoSiniestro(auditoriaActivo);
        etapaSiniestro(auditoriaActivo);
        ramos(auditoriaActivo);
    }

    private void ramos(AuditoriaBasica a) {
        s.save(new Ramo("HCM", "HCM", a));
        s.save(new Ramo("FUNERARIO", "FUNE", a));
        s.save(new Ramo("VIDA", "VIDA", a));
    }

    private void conceptoSENIAT(AuditoriaBasica a) {
        ArrayList<ConceptoSENIAT> list = new ArrayList<ConceptoSENIAT>(0);
        list.add(new ConceptoSENIAT("000", "Ninguno de Estos", 0d, a));
        list.add(new ConceptoSENIAT("002", "honorarios profecionales (PJD)", 0.05d, a));
        list.add(new ConceptoSENIAT("004", "honorarios profecionales (PNR)", 0.03d, a));
        list.add(new ConceptoSENIAT("055", "Pago a empresas contratistas(PJD)", 0.02d, a));
        for (ConceptoSENIAT o : list) {
            s.save(o);
        }
    }

    private void etapaSiniestro(AuditoriaBasica a) {
        ArrayList<EtapaSiniestro> list = new ArrayList<EtapaSiniestro>(0);
        EstatusSiniestro es[] = {new EstatusSiniestro("PENDIENTE", a), new EstatusSiniestro("PAGADO", a), new EstatusSiniestro("ANULADO", a)};
        for (int i = 0; i < es.length; i++) {
            s.save(es[i]);
        }
        AuditoriaBasica a2 = new AuditoriaBasica(a.getFechaInsert(), a.getUsuarioInsert(), Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE);
        list.add(new EtapaSiniestro("ANALIZANDO", "ANA", Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, es[0], a2));
        list.add(new EtapaSiniestro("RECAUDOS", "REC", Boolean.TRUE, Boolean.TRUE, Boolean.FALSE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, es[0], a2));
        list.add(new EtapaSiniestro("ARCHIVO", "FILE", Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, es[0], a2));
        list.add(new EtapaSiniestro("LIQUIDADO", "LIQ", Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, es[0], a2));
        list.add(new EtapaSiniestro("CARTA COMPROMISO", "CARTA", Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, es[0], a2));
        list.add(new EtapaSiniestro("ADMINISTRACION", "ORD_PAG", Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, es[0], a2));
        list.add(new EtapaSiniestro("INGRESADO", "ING", Boolean.FALSE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, es[0], a2));
        list.add(new EtapaSiniestro("EGRESADO", "EGR", Boolean.FALSE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, es[0], a2));
        list.add(new EtapaSiniestro("VIDA", "VIDA", Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE, es[0], a2));
        list.add(new EtapaSiniestro("PAGO DE GRACIA", "PAG-G", Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, es[1], a2));
        list.add(new EtapaSiniestro("PAGO REGULAR", "PAG", Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, es[1], a2));
        list.add(new EtapaSiniestro("POR ERROR", "NULL-E", Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, es[2], a2));
        list.add(new EtapaSiniestro("RECHAZADO", "NULL", Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, es[2], a2));

        for (EtapaSiniestro o : list) {
            s.save(o);
        }
    }

    private void tipoSiniestro(AuditoriaBasica a) {
        ArrayList<TipoSiniestro> list = new ArrayList<TipoSiniestro>(0);
        list.add(new TipoSiniestro("Hospitalizacion - Tratamiento Medico",
                Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE,
                Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, a));
        list.add(new TipoSiniestro("Hospitalizacion - Intervencion Quirurgica",
                Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE,
                Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, a));
        list.add(new TipoSiniestro("Hospitalizacion - Maternidad - Parto Normal",
                Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE,
                Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, a));
        list.add(new TipoSiniestro("Hospitalizacion - Maternidad - Cesaria",
                Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE,
                Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, a));
        list.add(new TipoSiniestro("Ambulatoria - Tratamiento Medico",
                Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE,
                Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, a));
        list.add(new TipoSiniestro("Ambulatoria - Intervencion Quirurgica",
                Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE,
                Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, a));
        list.add(new TipoSiniestro("Examenes y Estudios",
                Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, Boolean.TRUE,
                Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, a));
        for (TipoSiniestro o : list) {
            s.save(o);
        }
    }

    private void tipoCuentaBancaria(AuditoriaBasica a) {
        ArrayList<TipoCuentaBancaria> list = new ArrayList<TipoCuentaBancaria>(0);
        list.add(new TipoCuentaBancaria("AHORRO","00", a));
        list.add(new TipoCuentaBancaria("CORRIENTE","11", a));
        list.add(new TipoCuentaBancaria("TARJETA DE CREDITO", a));
        list.add(new TipoCuentaBancaria("TARJETA DE DEBITO", a));
        for (TipoCuentaBancaria o : list) {
            s.save(o);
        }
    }

    private void tiposDocumento(AuditoriaBasica a) {
        ArrayList<TipoDocumento> list = new ArrayList<TipoDocumento>(0);
        list.add(new TipoDocumento("CEDULA DE IDENTIDAD", Dominios.Modulos.PERSONAS, a));
        list.add(new TipoDocumento("CARNET DE EMPRESA", Dominios.Modulos.PERSONAS, a));
        list.add(new TipoDocumento("CERTIFICADO MEDICO", Dominios.Modulos.PERSONAS, a));
        list.add(new TipoDocumento("LICENCIA DE CONDUCIR", Dominios.Modulos.PERSONAS, a));
        list.add(new TipoDocumento("PARTIDA DE NACIMIENTO", Dominios.Modulos.PERSONAS, a));
        list.add(new TipoDocumento("ACTA DE MATRIMONIO", Dominios.Modulos.PERSONAS, a));
        list.add(new TipoDocumento("CARTA DE CONCUBINATO", Dominios.Modulos.PERSONAS, a));
        list.add(new TipoDocumento("FOTO", Dominios.Modulos.PERSONAS, a));
        list.add(new TipoDocumento("FACTURA", Dominios.Modulos.SINIESTROS, a));
        list.add(new TipoDocumento("INFORME MEDICO", Dominios.Modulos.SINIESTROS, a));
        for (TipoDocumento o : list) {
            s.save(o);
        }
    }

    private void tiposCapacidadEconomica(AuditoriaBasica a) {
        ArrayList<TipoCapacidadEconomica> list = new ArrayList<TipoCapacidadEconomica>(0);
        TipoCapacidadEconomica cap = new TipoCapacidadEconomica("Desconocido", a);
        list.add(cap);
        list.add(new TipoCapacidadEconomica("HASTA 5 Mil Bs", a));
        list.add(new TipoCapacidadEconomica("DESDE  5 Mil HASTA 10 MIL Bs", a));
        list.add(new TipoCapacidadEconomica("DESDE 10 Mil HASTA 20 MIL Bs", a));
        list.add(new TipoCapacidadEconomica("DESDE 20 Mil HASTA 30 MIL Bs", a));
        list.add(new TipoCapacidadEconomica("MAS DE 30 MIL Bs", a));
        for (TipoCapacidadEconomica o : list) {
            s.save(o);
        }
        defaultData.persona.setCapacidadEconomica(cap);
    }

    private void tiposDireccion(AuditoriaBasica a) {
        ArrayList<TipoDireccion> list = new ArrayList<TipoDireccion>(0);
        TipoDireccion dir = new TipoDireccion("DOMICIO FISCAL", a);
        list.add(dir);
        list.add(new TipoDireccion("COBRO", a));
        list.add(new TipoDireccion("PPRINCIPAL", a));
        list.add(new TipoDireccion("HABITACION", a));
        list.add(new TipoDireccion("TRABAJO", a));
        list.add(new TipoDireccion("SUCURSAL", a));
        for (TipoDireccion o : list) {
            s.save(o);
        }
        defaultData.persona.setDireccion(dir);
    }

    private void tiposTelefono(AuditoriaBasica a) {
        ArrayList<TipoTelefono2> list = new ArrayList<TipoTelefono2>(0);
        TipoTelefono2 tel = new TipoTelefono2("COBRO", a);
        list.add(tel);
        list.add(new TipoTelefono2("PRINCIPAL", a));
        list.add(new TipoTelefono2("HABITACION", a));
        list.add(new TipoTelefono2("TRABAJO", a));
        list.add(new TipoTelefono2("SUCURSAL", a));
        list.add(new TipoTelefono2("MOVIL", a));
        for (TipoTelefono2 o : list) {
            s.save(o);
        }
        defaultData.persona.setTelefono(tel);
    }

    private void tiposCodigoArea(AuditoriaBasica a) {
        ArrayList<TipoCodigoArea> list = new ArrayList<TipoCodigoArea>(0);
        list.add(new TipoCodigoArea("MOVISTAR 414", 414, a));
        list.add(new TipoCodigoArea("MOVISTAR 424", 424, a));
        list.add(new TipoCodigoArea("MOVILNET 416", 416, a));
        list.add(new TipoCodigoArea("MOVILNET 426", 426, a));
        list.add(new TipoCodigoArea("DIGITEL 412", 412, a));
        list.add(new TipoCodigoArea("TA S/CTBAL. 276", 276, a));
        for (TipoCodigoArea o : list) {
            s.save(o);
        }
    }

    private void tiposActividadEconomica(AuditoriaBasica a) {
        ArrayList<TipoActividadEconomica> list = new ArrayList<TipoActividadEconomica>(0);
        TipoActividadEconomica act = new TipoActividadEconomica("Desconocido", a);
        list.add(act);
        list.add(new TipoActividadEconomica("CONCEJO COMUNAL", a));
        list.add(new TipoActividadEconomica("AMA DE CASA", a));
        list.add(new TipoActividadEconomica("AGRICULTURA", a));
        list.add(new TipoActividadEconomica("AUTONOMO/PROPIETARIO", a));
        list.add(new TipoActividadEconomica("COMERCIANTE/ARTESANO", a));
        list.add(new TipoActividadEconomica("CONSULTOR/SERV.PROFESIONALES", a));
        list.add(new TipoActividadEconomica("CONTABILIDAD/FINANZAS", a));
        list.add(new TipoActividadEconomica("DIRECCION EJECUTIVA", a));
        list.add(new TipoActividadEconomica("ESTUDIANTE", a));
        list.add(new TipoActividadEconomica("VENTAS/MERCADOTECNIA/PUBLICIDA", a));
        list.add(new TipoActividadEconomica("GOBIERNO/EJERCITO", a));
        list.add(new TipoActividadEconomica("INFORMATICA", a));
        list.add(new TipoActividadEconomica("PESCA", a));
        list.add(new TipoActividadEconomica("SIN EMPLEO/INACTIVIDAD TEMPORAL", a));
        list.add(new TipoActividadEconomica("TRANSPORTE", a));
        list.add(new TipoActividadEconomica("TURISMO", a));
        list.add(new TipoActividadEconomica("MINERIA", a));
        list.add(new TipoActividadEconomica("INDUSTRIA", a));
        list.add(new TipoActividadEconomica("PESCA", a));
        for (TipoActividadEconomica o : list) {
            s.save(o);
        }
        defaultData.persona.setActividadEconomica(act);
    }

    private void tiposPersona(AuditoriaBasica a, AuditoriaBasica a2) {
        ArrayList<TipoPersona> list = new ArrayList<TipoPersona>(0);
        tpAsegurado = new TipoPersona("ASE", "ASEGURADO", true, a);
        list.add(tpAsegurado);
        tpContratante = new TipoPersona("CON", "CONTRATANTE", true, a);
        list.add(tpContratante);
        list.add(new TipoPersona("TIT", "TITULAR", true, a));
        list.add(new TipoPersona("NEG", "ANALISTA NEGOCIADOR", false, a));
        list.add(new TipoPersona("MED", "MEDICO", false, a));
        list.add(new TipoPersona("CLI", "CLINICA", false, a));
        tpLaboratiorio = new TipoPersona("LAB", "LABORATORIO", false, a);
        list.add(new TipoPersona("IVA", "CONTRIBUYENTE", false, a2));
        list.add(new TipoPersona("RAZ", "RAZON SOCIAL", false, a2));
        list.add(new TipoPersona("EJE", "EJECUTIVO", true, a));
        tpCompania = new TipoPersona("SEG", "COMPAÑIA DE SEGUROS", true, a);
        list.add(tpCompania);
        tpFinanciadora = new TipoPersona("FIN", "FINANCIADORA", true, a2);
        list.add(tpFinanciadora);
        tpBanco = new TipoPersona("BAN", "BANCO", true, a);
        list.add(tpBanco);
        list.add(new TipoPersona("BEN", "BENEFICIARIO", true, a));
        tpTaller = new TipoPersona("TAL", "TALLER", false, a);
        list.add(tpTaller);
        list.add(new TipoPersona("TER", "TERCERO", false, a2));
        list.add(new TipoPersona("EMP", "EMPLEADO", false, a2));
        list.add(new TipoPersona("ABG", "ABOGADO", false, a2));
        tpProveedor = new TipoPersona("PRV", "PROVEEDOR", true, a);
        list.add(tpProveedor);
        list.add(tpLaboratiorio);
        list.add(new TipoPersona("FIA", "FIADOR", false, a2));
        list.add(new TipoPersona("PAG", "PAGADOR", true, a));
        list.add(new TipoPersona("FAR", "FARMACIA", false, a));
        tpTaxi = new TipoPersona("TXA", "TAXIS", false, a2);
        list.add(tpTaxi);
        list.add(new TipoPersona("TCL", "TRANSPORTE COLECTIVO", false, a2));
        list.add(new TipoPersona("RES", "RESTAURANTE", false, a2));
        list.add(new TipoPersona("PRO", "EMPRESA", true, a));
        list.add(new TipoPersona("FUN", "FUNERARIA", true, a));
        for (TipoPersona o : list) {
            s.save(o);
        }
    }

    private void estados(AuditoriaBasica a) {
        ArrayList<Estado> list = new ArrayList<Estado>(0);
        list.add(new Estado("TACHIRA", a));
        list.add(new Estado("DISTRITO FEDERAL", a));
        list.add(new Estado("ANZUATEGUI", a));
        list.add(new Estado("APURE", a));
        list.add(new Estado("ARAGUA", a));
        list.add(new Estado("BARINAS", a));
        list.add(new Estado("BOLIVAR", a));
        list.add(new Estado("CARABOBO", a));
        list.add(new Estado("COJEDES", a));
        list.add(new Estado("FALCON", a));
        list.add(new Estado("GUARICO", a));
        list.add(new Estado("LARA", a));
        list.add(new Estado("MERIDA", a));
        list.add(new Estado("MIRANDA", a));
        list.add(new Estado("MONAGAS", a));
        list.add(new Estado("NUEVA ESPARTA", a));
        list.add(new Estado("PORTUGUEZA", a));
        list.add(new Estado("SUCRE", a));
        list.add(new Estado("TRUJILLO", a));
        list.add(new Estado("YARACUY", a));
        list.add(new Estado("ZULIA", a));
        list.add(new Estado("MONAGAS", a));
        list.add(new Estado("VARGAS", a));
        list.add(new Estado("ANTILLAS", a));
        list.add(new Estado("TERRITORIO DEL TAMACURO", a));
        list.add(new Estado("OTRO PAIS", a));
        for (Estado o : list) {
            s.save(o);
        }
    }

    private void dataPersona(Persona p, AuditoriaBasica a) {
        p.setAuditoria(a);
        p.setCapacidadEconomica(defaultData.persona.getCapacidadEconomica());
        p.setActividadEconomica(defaultData.persona.getActividadEconomica());
        p.setTipoContribuyente(Dominios.TipoContribuyente.DESCONOCIDO);
        p.setRanking(Dominios.Ranking.B);
    }

    protected void dataPersonaNatural(PersonaNatural pn, AuditoriaBasica a) {
        pn.setTipoNombre(Dominios.TipoNombre.DESCONOCIDO);
        pn.setSexo(Dominios.Sexo.DESCONOCIDO);
        pn.setEstadoCivil(Dominios.EstadoCivil.DESCONOCIDO);
        LNPersonaNatural.generarNombres(pn);
        dataPersona(pn, a);
    }

    private void personasDefault(AuditoriaBasica a) {
        //Persona Natural

        //Persona Juridica Bancos
        PersonaJuridica bcomercantil1 = new PersonaJuridica();
        bcomercantil1.setRif(new Rif(Dominios.TipoCedula.JURIDICO, 2961, 0));
        bcomercantil1.setNombreLargo("MERCANTIL, C.A. BANCO UNIVERSAL");
        bcomercantil1.setNombreCorto("MERCANTIL");
        bcomercantil1.setWeb("http://http://www.bancomercantil.com");
        bcomercantil1.getTiposPersona().add(tpBanco);
        dataPersona(bcomercantil1, a);
        s.save(bcomercantil1);

        PersonaJuridica bcobicentenario = new PersonaJuridica();
        bcobicentenario.setRif(new Rif(Dominios.TipoCedula.GOBIERNO, 20009148, 7));
        bcobicentenario.setNombreLargo("BANCO BICENTENARIO, C.A. BANCO UNIVERSAL");
        bcobicentenario.setNombreCorto("BICENTENARIO");
        bcobicentenario.setWeb("http://www.bicentenariobu.com/");
        bcobicentenario.getTiposPersona().add(tpBanco);
        dataPersona(bcobicentenario, a);
        s.save(bcobicentenario);

        PersonaJuridica bcovenezuela = new PersonaJuridica();
        bcovenezuela.setRif(new Rif(Dominios.TipoCedula.JURIDICO, 2948, 2));
        bcovenezuela.setNombreLargo("BANCO DE VENEZUELA, S.A BANCO UNIVERSAL");
        bcovenezuela.setNombreCorto("VENEZUELA");
        bcovenezuela.setWeb("http://www.bancodevenezuela.com/");
        bcovenezuela.getTiposPersona().add(tpBanco);
        dataPersona(bcovenezuela, a);
        s.save(bcovenezuela);

        PersonaJuridica bcotesoro = new PersonaJuridica();
        bcotesoro.setRif(new Rif(Dominios.TipoCedula.GOBIERNO, 20005187, 6));
        bcotesoro.setNombreLargo("BANCO DEL TESORO, C.A. BANCO UNIVERSA");
        bcotesoro.setNombreCorto("TESORO");
        bcotesoro.setWeb("http://www.bt.gob.ve/");
        bcotesoro.getTiposPersona().add(tpBanco);
        dataPersona(bcotesoro, a);
        s.save(bcotesoro);

        PersonaJuridica bcobanesco = new PersonaJuridica();
        bcobanesco.setRif(new Rif(Dominios.TipoCedula.JURIDICO, 7013380, 5));
        bcobanesco.setNombreLargo("BANESCO BANCO UNIVERSAL, C.A.");
        bcobanesco.setNombreCorto("BANESCO");
        bcobanesco.setWeb("http://www.banesco.com/");
        bcobanesco.getTiposPersona().add(tpBanco);
        dataPersona(bcobanesco, a);
        s.save(bcobanesco);

        PersonaJuridica bcosofitasa = new PersonaJuridica();
        bcosofitasa.setRif(new Rif(Dominios.TipoCedula.JURIDICO, 9028384, 6));
        bcosofitasa.setNombreLargo("BANCO SOFITASA BANCO UNIVERSAL, C. A.");
        bcosofitasa.setNombreCorto("SOFITASA");
        bcosofitasa.setWeb("http://www.sofitasa.com/index.asp");
        bcosofitasa.getTiposPersona().add(tpBanco);
        dataPersona(bcosofitasa, a);
        s.save(bcosofitasa);

        PersonaJuridica bcobod = new PersonaJuridica();
        bcobod.setRif(new Rif(Dominios.TipoCedula.JURIDICO, 30061946, 0));
        bcobod.setNombreLargo("BANCO OCCIDENTAL DE DESCUENTO, BANCO UNIVERSAL, C.A. ");
        bcobod.setNombreCorto("BOD");
        bcobod.setWeb("http://www.bodinternet.com/Red_de_Oficinas.asp");
        bcobod.getTiposPersona().add(tpBanco);
        dataPersona(bcobod, a);
        s.save(bcobod);

        PersonaJuridica bcoexterior = new PersonaJuridica();
        bcoexterior.setRif(new Rif(Dominios.TipoCedula.JURIDICO, 2950, 4));
        bcoexterior.setNombreLargo("BANCO EXTERIOR,C.A, BANCO UNIVERSAL");
        bcoexterior.setNombreCorto("EXTERIOR");
        bcoexterior.setWeb("http://www.bancoexterior.com/");
        bcoexterior.getTiposPersona().add(tpBanco);
        dataPersona(bcoexterior, a);
        s.save(bcoexterior);

        PersonaJuridica bcocoro = new PersonaJuridica();
        bcocoro.setRif(new Rif(Dominios.TipoCedula.JURIDICO, 7000173, 9));
        bcocoro.setNombreLargo("BANCORO C.A. BANCO UNIVERSAL REGIONAL");
        bcocoro.setNombreCorto("BANCORO");
        bcocoro.setWeb("http://www.bancoro.com/");
        bcocoro.getTiposPersona().add(tpBanco);
        dataPersona(bcocoro, a);
        s.save(bcocoro);

        PersonaJuridica bcobfc = new PersonaJuridica();
        bcobfc.setRif(new Rif(Dominios.TipoCedula.JURIDICO, 30778189, 0));
        bcobfc.setNombreLargo("BFC BANCO FONDO COMUN, C.A. BANCO UNIVERSAL");
        bcobfc.setNombreCorto("BFC");
        bcobfc.setWeb("http://www.bfc.com.ve/");
        bcobfc.getTiposPersona().add(tpBanco);
        dataPersona(bcobfc, a);
        s.save(bcobfc);

        PersonaJuridica bcoprovincial = new PersonaJuridica();
        bcoprovincial.setRif(new Rif(Dominios.TipoCedula.JURIDICO, 2967, 9));
        bcoprovincial.setNombreLargo("BANCO PROVINCIAL, S.A. BANCO UNIVERSAL");
        bcoprovincial.setNombreCorto("PROVINCIAL");
        bcoprovincial.setWeb("https://www.provincial.com/");
        bcoprovincial.getTiposPersona().add(tpBanco);
        dataPersona(bcoprovincial, a);
        s.save(bcoprovincial);

        PersonaJuridica bcoguayana = new PersonaJuridica();
        bcoguayana.setRif(new Rif(Dominios.TipoCedula.JURIDICO, 2941, 5));
        bcoguayana.setNombreLargo("BANCO GUAYANA C.A.");
        bcoguayana.setNombreCorto("GUAYANA");
        bcoguayana.setWeb("http://www.bancoguayana.net/");
        bcoguayana.getTiposPersona().add(tpBanco);
        dataPersona(bcoguayana, a);
        s.save(bcoguayana);

        PersonaJuridica bcoiv = new PersonaJuridica();
        bcoiv.setRif(new Rif(Dominios.TipoCedula.JURIDICO, 2957, 1));
        bcoiv.setNombreLargo("BANCO INDUSTRIAL DE VENEZUELA C A");
        bcoiv.setNombreCorto("INDUSTRIAL");
        bcoiv.setWeb("http://www.biv.com.ve//");
        bcoiv.getTiposPersona().add(tpBanco);
        dataPersona(bcoiv, a);
        s.save(bcoiv);

        //Persona Juridica Prestacion de servicios
        PersonaJuridica servmovilnet = new PersonaJuridica();
        servmovilnet.setRif(new Rif(Dominios.TipoCedula.JURIDICO, 30000493, 7));
        servmovilnet.setNombreLargo("TELECOMUNICACIONES MOVILNET, C.A.");
        servmovilnet.setNombreCorto("MOVILNET");
        servmovilnet.setWeb("http://www.movilnet.com.ve/sitio/");
        servmovilnet.getTiposPersona().add(tpProveedor);
        dataPersona(servmovilnet, a);
        s.save(servmovilnet);

        PersonaJuridica servcantv = new PersonaJuridica();
        servcantv.setRif(new Rif(Dominios.TipoCedula.JURIDICO, 30186298, 8));
        servcantv.setNombreLargo("CANTV.NET, C.A.");
        servcantv.setNombreCorto("CANTV");
        servcantv.setWeb("http://www.cantv.net/");
        servcantv.getTiposPersona().add(tpProveedor);
        dataPersona(servcantv, a);
        s.save(servcantv);

        PersonaJuridica servmovistar = new PersonaJuridica();
        servmovistar.setRif(new Rif(Dominios.TipoCedula.JURIDICO, 343994, 0));
        servmovistar.setNombreLargo("TELCEL, C.A.");
        servmovistar.setNombreCorto("MOVISTAR");
        servmovistar.setWeb("http://www.movistar.com.ve/");
        servmovistar.getTiposPersona().add(tpProveedor);
        dataPersona(servmovistar, a);
        s.save(servmovistar);

        PersonaJuridica servdigitel = new PersonaJuridica();
        servdigitel.setRif(new Rif(Dominios.TipoCedula.JURIDICO, 30468971, 3));
        servdigitel.setNombreLargo("CORPORACION DIGITEL, C.A. ");
        servdigitel.setNombreCorto("DIGITEL");
        servdigitel.setWeb("http://www.digitel.com.ve/");
        servdigitel.getTiposPersona().add(tpProveedor);
        dataPersona(servdigitel, a);
        s.save(servdigitel);

        // pendiente revisar rif seniat
        PersonaJuridica servcadafe = new PersonaJuridica();
        servcadafe.setRif(new Rif(Dominios.TipoCedula.JURIDICO, 4366, 3));
        servcadafe.setNombreLargo("CADAFE");
        servcadafe.setNombreCorto("CADAFE");
        servcadafe.setWeb("http://www.google.com");
        servcadafe.getTiposPersona().add(tpProveedor);
        dataPersona(servcadafe, a);
        s.save(servcadafe);

        // pendiente revisar rif seniat
        PersonaJuridica servhso = new PersonaJuridica();
        servhso.setRif(new Rif(Dominios.TipoCedula.GOBIERNO, 20008085, 0));
        servhso.setNombreLargo("HIDROSUROESTE");
        servhso.setNombreCorto("HIDROSUROESTE");
        servhso.setWeb("http://www.google.com");
        servhso.getTiposPersona().add(tpProveedor);
        dataPersona(servhso, a);
        s.save(servhso);

        PersonaJuridica servseniat = new PersonaJuridica();
        servseniat.setRif(new Rif(Dominios.TipoCedula.GOBIERNO, 20000303, 0));
        servseniat.setNombreLargo("SERVICIO NACIONAL INTEGRADO DE ADMINISTRACION ADUANERA Y TRIBUTARIA");
        servseniat.setNombreCorto("SENIAT");
        servseniat.setWeb("http://www.seniat.gob.ve/");
        servseniat.getTiposPersona().add(tpProveedor);
        dataPersona(servseniat, a);
        s.save(servseniat);

        PersonaJuridica servdirectv = new PersonaJuridica();
        servdirectv.setRif(new Rif(Dominios.TipoCedula.JURIDICO, 30259700, 5));
        servdirectv.setNombreLargo("GALAXY ENTERTAINMENT DE VENEZUELA, C.A.");
        servdirectv.setNombreCorto("DIRECTV");
        servdirectv.setWeb("http://www.directv.com.ve/");
        servdirectv.getTiposPersona().add(tpProveedor);
        dataPersona(servdirectv, a);
        s.save(servdirectv);

        PersonaJuridica servnetuno = new PersonaJuridica();
        servnetuno.setRif(new Rif(Dominios.TipoCedula.JURIDICO, 30108335, 0));
        servnetuno.setNombreLargo("NETUNO, C.A");
        servnetuno.setNombreCorto("NETUNO");
        servnetuno.setWeb("https://www.netuno.net/");
        servnetuno.getTiposPersona().add(tpProveedor);
        dataPersona(servnetuno, a);
        s.save(servnetuno);

        //Persona Juridica Lineas Aereas  ASERCA AIRLINES, C.A.
        PersonaJuridica laserca = new PersonaJuridica();
        laserca.setRif(new Rif(Dominios.TipoCedula.JURIDICO, 7503559, 3));
        laserca.setNombreLargo("ASERCA AIRLINES, C.A.");
        laserca.setNombreCorto("ASERCA");
        laserca.setWeb("http://www.asercaairlines.com/");
        laserca.getTiposPersona().add(tpProveedor);
        dataPersona(laserca, a);
        s.save(laserca);

        PersonaJuridica lconviasa = new PersonaJuridica();
        lconviasa.setRif(new Rif(Dominios.TipoCedula.GOBIERNO, 20007774, 3));
        lconviasa.setNombreLargo("CONSORCIO VENEZOLANO DE INDUSTRIAS AERONAUTICAS Y SERVICIOS AEREOS, S.A.");
        lconviasa.setNombreCorto("CONVIASA");
        lconviasa.setWeb("http://www.conviasa.aero/");
        lconviasa.getTiposPersona().add(tpProveedor);
        dataPersona(lconviasa, a);
        s.save(lconviasa);

//   // pendiente revisar rif seniat
//        PersonaJuridica lrutaca = new PersonaJuridica();
//        lrutaca.setRif(new Rif(Dominios.TipoCedula.JURIDICO, 88));
//        lrutaca.setNombreLargo("RUTACA");
//        lrutaca.setNombreCorto("RUTACA");
//        lrutaca.setWeb("http://www.rutaca.com.ve/");
//        lrutaca.getTiposPersona().add(tpProveedor);
//        dataPersona(lrutaca, a);
//        s.save(lrutaca);

        //Persona Juridica LINEA TRANSPORTE (EXP. LOS LLANOS - OCCIDENTE - MERIDA) ETC.

//        //        //Persona Gobierno Seguro Social
//   // pendiente revisar rif seniat
//        PersonaJuridica gobivss = new PersonaJuridica();
//        gobivss.setRif(new Rif(Dominios.TipoCedula.GOBIERNO, 88));
//        gobivss.setNombreLargo("INSTITUTO VENEZOLANO DE LOS SEGUROS SOCIALES");
//        gobivss.setNombreCorto("IVSS");
//        gobivss.setWeb("http://www.ivss.gov.ve/");
//        gobivss.getTiposPersona().add(tpProveedor);
//        dataPersona(gobivss, a);
//        s.save(gobivss);

        //Persona Gobierno BANAVIV

        //Persona Juridica Taxis

        //Persona Juridica Farmacias

        //Persona Juridica Clinicas

        //Persona Juridica Talleres

        //Persona Juridica Encomiendas
        //
        PersonaJuridica provmrw = new PersonaJuridica();
        provmrw.setRif(new Rif(Dominios.TipoCedula.JURIDICO, 274758, 7));
        provmrw.setNombreLargo("MENSAJEROS RADIO WORLDWIDE C.A");
        provmrw.setNombreCorto("MRW");
        provmrw.setWeb("http://www.mrw.com.ve/");
        provmrw.getTiposPersona().add(tpProveedor);
        dataPersona(provmrw, a);
        s.save(provmrw);

        PersonaJuridica provzoom = new PersonaJuridica();
        provzoom.setRif(new Rif(Dominios.TipoCedula.JURIDICO, 102174, 4));
        provzoom.setNombreLargo("ZOOM INTERNATIONAL SERVICES C A");
        provzoom.setNombreCorto("ZOOM");
        provzoom.setWeb("http://www.grupozoom.com/");
        provzoom.getTiposPersona().add(tpProveedor);
        dataPersona(provzoom, a);
        s.save(provzoom);

        PersonaJuridica provipostel = new PersonaJuridica();
        provipostel.setRif(new Rif(Dominios.TipoCedula.JURIDICO, 20000043, 0));
        provipostel.setNombreLargo("INSTITUTO POSTAL TELEGRAFICO DE VENEZUELA ");
        provipostel.setNombreCorto("IPOSTEL");
        provipostel.setWeb("http://www.ipostel.gov.ve/");
        provipostel.getTiposPersona().add(tpProveedor);
        dataPersona(provipostel, a);
        s.save(provipostel);
    }

    private void calendarioBancario(AuditoriaBasica a) {
        ArrayList<CalendarioBancario> list = new ArrayList<CalendarioBancario>(0);
        list.add(new CalendarioBancario("AÑO NUEVO", "", new Date(110, 0, 1), false, true, false, a));
        list.add(new CalendarioBancario("DIA DE REYES", "", new Date(110, 0, 4), true, false, false, a));
//        list.add(new CalendarioBancario("DIA DE REYES", "", new Date(110, 0, 6), false, true, false, a));
//        list.add(new CalendarioBancario("DIA DEL COMERCIANTE", "SOLO ESTADO FALCON", new Date(110, 0, 2), false, false, true, a));
//        list.add(new CalendarioBancario("DIA DE LA DIVINA PASTORA", "", new Date(110, 0, 14), false, false, true, a));
//        list.add(new CalendarioBancario("DIA DE SAN SEBASTIAN", "Solo Estado Tachira", new Date(110, 0, 20), false, false, true, a));
        list.add(new CalendarioBancario("CARNAVAL", "", new Date(110, 1, 15), false, true, false, a));
        list.add(new CalendarioBancario("CARNAVAL", "", new Date(110, 1, 16), false, true, false, a));
//        list.add(new CalendarioBancario("DIA DE LA BATALLA DE LA VICTORIA", "SOLO ESTADO ARAGUA", new Date(110, 1, 12), false, false, true, a));
//        list.add(new CalendarioBancario("Día de Jose Maria Vargas", "SOLO ESTADO VARGAS", new Date(110, 2, 10), false, false, true, a));
        list.add(new CalendarioBancario("DIA DE SAN JOSE", "", new Date(110, 2, 19), true, false, false, a));
        list.add(new CalendarioBancario("SEMANA SANTA", "", new Date(110, 2, 1), false, true, false, a));
        list.add(new CalendarioBancario("SEMANA SANTA", "", new Date(110, 2, 2), false, true, false, a));
        list.add(new CalendarioBancario("MOVIMIENTO PRECURSOR DE LA INDEPENDENCIA", "", new Date(110, 3, 19), false, true, false, a));
        list.add(new CalendarioBancario("DIA DEL TRABAJADOR", "", new Date(110, 4, 1), false, true, false, a));
//        list.add(new CalendarioBancario("DIA DE LA CRUZ DE MAYO", "SOLO ESTADO MIRANDA", new Date(110, 4, 3), false, false, true, a));
//        list.add(new CalendarioBancario("Ascensión del Señor", "", new Date(110, 4, 13), false, true, false, a));
        list.add(new CalendarioBancario("ASCENSION DEL SEÑOR", "", new Date(110, 4, 17), true, false, false, a));
//        list.add(new CalendarioBancario("Corpus Christie", "", new Date(110, 5, 3), false, true, false, a));
        list.add(new CalendarioBancario("CORPUS CHRISTIE", "", new Date(110, 5, 07), true, false, false, a));
//        list.add(new CalendarioBancario("Día de Jacinto Lara", "Solo Estado Lara", new Date(110, 4, 28), false, false, true, a));
//        list.add(new CalendarioBancario("Día de San Fernardo", "Solo Estado Apure", new Date(110, 4, 30), false, false, true, a));
        list.add(new CalendarioBancario("BATALLA DE CARABOBO", "", new Date(110, 5, 24), false, true, false, a));
        list.add(new CalendarioBancario("DIA DE SAN PEDRO Y SAN PABLO", "", new Date(110, 5, 28), true, false, false, a));
//        list.add(new CalendarioBancario("Día de San Pedro y San Pablo", "", new Date(110, 5, 29), false, true, false, a));
//        list.add(new CalendarioBancario("Día de Barinas ", "Solo Estado Barinas", new Date(110, 5, 30), false, false, true, a));
        list.add(new CalendarioBancario("DIA DE LA INDEPENDECIA", "", new Date(110, 6, 5), false, true, false, a));
//        list.add(new CalendarioBancario("Día de la Virgen del Carmen", "Solo Edo. Zulia", new Date(110, 6, 16), false, false, true, a));
//        list.add(new CalendarioBancario("Día de Santa Maria Magdalena", "Solo Edo. Aragua", new Date(110, 6, 16), false, false, true, a));
        list.add(new CalendarioBancario("NATALICIO DEL LIBERTADOR", "", new Date(110, 6, 24), false, true, false, a));
        list.add(new CalendarioBancario("ASSUNCION DE NUESTRA SEÑORA", "", new Date(110, 7, 15), true, false, false, a));
//        list.add(new CalendarioBancario("Día de la Vigen del Valle", "El Tigre (edo. Anzuategui) y Edo. Nueva Esparta", new Date(110, 8, 8), false, false, true, a));
//        list.add(new CalendarioBancario("Dia de Barquisimeto", "Solo Estado Lara", new Date(110, 8, 14), false, false, true, a));
//        list.add(new CalendarioBancario("Día de la Virgen del Rosario", "Villa Rosario (Edo. Zulia) y Guigue (Edo. Carabobo)", new Date(110, 9, 7), false, false, true, a));
        list.add(new CalendarioBancario("DIA DE LA RESISTENCIA INDIGENA", "", new Date(110, 9, 12), false, true, false, a));
//        list.add(new CalendarioBancario("Día de Rafael Urdaneta", "Solo Edo. Zulia", new Date(110, 9, 24), false, false, true, a));
//        list.add(new CalendarioBancario("Día de San Rafael", "Solo Bejuma (Edo. Carabobo)", new Date(110, 9, 24), false, false, true, a));
        list.add(new CalendarioBancario("DIA DE TODOS LOS SANTOS", "", new Date(110, 10, 1), true, false, false, a));
//        list.add(new CalendarioBancario("Toma de Puerto Cabello", "Solo Estado Carabobo", new Date(110, 0, 8), false, false, true, a));
//        list.add(new CalendarioBancario("Día de la Virgen del Socorro", "Solo Municipios de Valencia ", new Date(110, 10, 13), false, false, true, a));
//        list.add(new CalendarioBancario("Natalicio de Jose Antonio Anzuategui", "Solo Estado Anzuategui", new Date(110, 10, 14), false, false, true, a));
//        list.add(new CalendarioBancario("Día de Nuestra Señora de la Chiquinquira", "Solo Estado Zulia", new Date(110, 10, 18), false, false, true, a));
        list.add(new CalendarioBancario("DIA DE LA INMACULADA CONCEPCION", "", new Date(110, 10, 6), true, false, false, a));
//        list.add(new CalendarioBancario("Día de la Inmaculada Concepción", "", new Date(110, 10, 8), false, true, false, a));
//        list.add(new CalendarioBancario("Día de la Vigen de Guadalupe", "Estado Falcon", new Date(110, 12, 12), false, false, true, a));
//        list.add(new CalendarioBancario("Día de Santa Lucia", "Solo Estado Miranda", new Date(110, 12, 13), false, false, true, a));
        list.add(new CalendarioBancario("NATIVIDAD DEL SEÑOR", "", new Date(110, 12, 25), false, true, false, a));

        for (CalendarioBancario o : list) {
            s.save(o);
        }
    }

    private void parentescos(AuditoriaBasica a) {
        ArrayList<Parentesco> list = new ArrayList<Parentesco>(0);
        list.add(new Parentesco("TRABAJADOR", a));
        list.add(new Parentesco("PADRE/MADRE", a));
        list.add(new Parentesco("HIJO/HIJA", a));
        list.add(new Parentesco("HERMANO/HERMANA", a));
        list.add(new Parentesco("ABUELO/ABUELA", a));
        list.add(new Parentesco("NO APLICA", a));
        list.add(new Parentesco("TODOS", a));
        for (Parentesco o : list) {
            s.save(o);
        }
    }

    private void plazosEsperas(AuditoriaBasica a) {
        ArrayList<PlazoEspera> list = new ArrayList<PlazoEspera>(0);
        list.add(new PlazoEspera("Sin Plazo y sin Preexistensia", a));
        list.add(new PlazoEspera("Con Plazo y con Preexistensia", a));
        list.add(new PlazoEspera("Sin Plazo y con Preexistensia", a));
        for (PlazoEspera o : list) {
            s.save(o);
        }
    }

    private void planes(AuditoriaBasica a) {
        ArrayList<Plan> list = new ArrayList<Plan>(0);
        list.add(new Plan("ESPECIAL-A 3000", a));
        for (Plan o : list) {
            s.save(o);
        }
    }

    private void departamentos(AuditoriaBasica a) {
        ArrayList<Departamento> list = new ArrayList<Departamento>(0);
        list.add(new Departamento("PRESIDENCIA", a));
        list.add(new Departamento("TESORERIA", a));
        list.add(new Departamento("SECRETARIA", a));
        list.add(new Departamento("INFORMATICA", a));
        list.add(new Departamento("FINANZAS", a));
        list.add(new Departamento("RECURSOS HUMANOS", a));
        for (Departamento o : list) {
            s.save(o);
        }
    }

    private void tiposContratos(AuditoriaBasica a) {
        ArrayList<TipoContrato> list = new ArrayList<TipoContrato>(0);
        list.add(new TipoContrato("EMPLEADO", a));
        list.add(new TipoContrato("CONTRATADO", a));
        list.add(new TipoContrato("JUBILADO", a));
        list.add(new TipoContrato("INCAPACITADO", a));
        list.add(new TipoContrato("APRENDIZ INCE", a));
        for (TipoContrato o : list) {
            s.save(o);
        }
    }

    public static void main(String[] args) {
        try {
            Crear bd = new Crear();
            bd.create();
            bd.data();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void configuracionesGenerales(AuditoriaBasica auditoriaActivo) {
        ArrayList<ConfiguracionesGenerales> list = new ArrayList<ConfiguracionesGenerales>(0);
        list.add(new ConfiguracionesGenerales("ut", 75d));
        list.add(new ConfiguracionesGenerales("iva", 0.12d));
        list.add(new ConfiguracionesGenerales("reembolso.diasVencimiento", 180));
        list.add(new ConfiguracionesGenerales("cartaAval.diasVencimiento", 30));
        list.add(new ConfiguracionesGenerales("aps.diasVencimiento", 15));
        list.add(new ConfiguracionesGenerales("remesa.maxUt.reembolso", 10000d));
        ConfiguracionesGenerales a = new ConfiguracionesGenerales("remesa.numNeg", "00002100");
        a.setValorInteger(2100);
        list.add(a);
        for (ConfiguracionesGenerales o : list) {
            o.setAuditoria(auditoriaActivo);
            s.save(o);
        }
    }
}
