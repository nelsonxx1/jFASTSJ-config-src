package dataBase;

import com.jswitch.base.controlador.General;
import com.jswitch.base.modelo.HibernateUtil;
import com.jswitch.base.modelo.entidades.auditoria.AuditoriaBasica;
import com.jswitch.rol.modelo.Item;
import com.jswitch.rol.modelo.MenuByRol;
import com.jswitch.rol.modelo.Rol;
import java.util.Date;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author adrian
 */
public class LlenarMenu {
    
    public static void llenarItems() {
        Session s = null;
        
        try {
            s = HibernateUtil.getSessionFactory().openSession();
            Transaction tx = s.beginTransaction();
            AuditoriaBasica a = new AuditoriaBasica(new Date(), "defaultdata", Boolean.TRUE);
            Item root = new Item("root", null, a);

            // <editor-fold defaultstate="collapsed" desc="Personas">
            Item personas = new Item("Personas", null, a);
            {
                Item presonaNueva = new Item("Nuevo", "PER", "insert2.png", "getPersonaNueva", a);
                Item presonasGrid = new Item("Todas", "PER", "user3.png", "getPersonas", a);
                Item buscar = new Item("Buscar", "PER", "find.png", "getBuscarPersona", a);
                personas.getItems().add(presonaNueva);
                personas.getItems().add(presonasGrid);
                personas.getItems().add(buscar);
                
            }
            root.getItems().add(personas);
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="Polizas">
            Item poliza = new Item("Polizas", null, a);
            
            {
                
                Item polizaNueva = new Item("Nueva", "POL", "insert2.png", "getPolizaNueva", a);
                Item polizas = new Item("Todas", "POL", "poliza.png", "getPolizas", a);
//                Item buscar = new Item("Buscar", "POL", "find.png", "AUN_NO_FUN", a);
                poliza.getItems().add(polizaNueva);
                poliza.getItems().add(polizas);
//                poliza.getItems().add(buscar);
            }
            root.getItems().add(poliza);
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="Certificado">
            Item certificado = new Item("Certificado", null, a);
            {
                Item cert = new Item("Nuevo", "CERT", "insert2.png", "getCertificadoNuevo", a);
                Item certs = new Item("Todos", "CERT", "poliza.png", "getCertificados", a);
//                Item buscar = new Item("Buscar", "CERT", "find.png", "AUN_NO_FUN", a);
                certificado.getItems().add(cert);
                certificado.getItems().add(certs);
//                certificado.getItems().add(buscar);
            }
            root.getItems().add(certificado);
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="Asegurado">
            Item aseguradosFolder = new Item("Asegurados", null, a);
            {
                Item buscar = new Item("Buscar Asegurados", "ASE", "find.png", "getBuscarAsegurado", a);
                Item asegurados = new Item("Lista Asegurados", "ASE", "asegurado.png", "getAsegurados", a);
                Item beneficiarios = new Item("Lista Beneficiarios", "ASE", "asegurado.png", "getBeneficiarios", a);
                Item titulares = new Item("Lista Titulares", "ASE", "titulares.png", "getTitulares", a);
                aseguradosFolder.getItems().add(buscar);
                aseguradosFolder.getItems().add(asegurados);
                aseguradosFolder.getItems().add(beneficiarios);
                aseguradosFolder.getItems().add(titulares);                
            }
            root.getItems().add(aseguradosFolder);
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="Siniestros">
            Item siniestro = new Item("Siniestros", null, a);
            {                
                Item siniestroNuevo = new Item("Nuevo", "SIN", "insert2.png", "getSiniestroNuevo", a);
                Item siniestros = new Item("Todos", "SIN", "poliza.png", "getSiniestros", a);
                
                Item aps = new Item("Atencion Primaria de Servicio (APS)", null, a);
                Item searchAps = new Item("Todos APS", "SIN", "poliza.png", "getApsGrid", a);
                
                Item ayudaSocial = new Item("Ayudas Sociales", null, a);
                Item searchAS = new Item("Lista de  Ayudas Sociales", "SIN", "poliza.png", "getAyudaSocialGrid", a);
                
                Item cartaAval = new Item("Carta Aval", null, a);
                Item searchAval = new Item("Lista de  Cartas Aval", "SIN", "poliza.png", "getCartaAvalGrid", a);
                
                Item emergencia = new Item("Emergencias", null, a);
                Item emergenciaBuscar = new Item("Lista de Emergencias", "SIN", "poliza.png", "getEmergenciaGrid", a);
                
                Item funerario = new Item("Funerarios", null, a);
                Item searchFune = new Item("Lista de Gastos Funerarios", "SIN", "poliza.png", "getFunerarioGrid", a);
                
                Item reembolsos = new Item("Reembolsos", null, a);
                Item searchReem = new Item("Lista de Reembolsos", "SIN", "poliza.png", "getReembolsoGrid", a);
                
                Item vida = new Item("Vida", null, a);
                Item searchVida = new Item("Lista de Imdemnizaccion por Vida", "SIN", "poliza.png", "getVidaGrid", a);
                
                Item buscarSin = new Item("Buscar", "SIN", "find.png", "getBuscarSin", a);
                                               
                siniestro.getItems().add(siniestroNuevo);
                siniestro.getItems().add(siniestros);
                
                siniestro.getItems().add(aps);
                aps.getItems().add(searchAps);
                
                siniestro.getItems().add(ayudaSocial);
                ayudaSocial.getItems().add(searchAS);
                
                siniestro.getItems().add(cartaAval);
                cartaAval.getItems().add(searchAval);
                
                siniestro.getItems().add(emergencia);
                emergencia.getItems().add(emergenciaBuscar);
                
                siniestro.getItems().add(funerario);
                funerario.getItems().add(searchFune);
                
                siniestro.getItems().add(reembolsos);
                reembolsos.getItems().add(searchReem);
                
                siniestro.getItems().add(vida);
                vida.getItems().add(searchVida);
                
                siniestro.getItems().add(buscarSin);
            }
            root.getItems().add(siniestro);
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="Pagos">
            Item pays = new Item("Pagos", null, a);
            {
                Item pago = new Item("Nuevo Pago", "PAG", "money.png", "getNewPago", a);
                Item persPag = new Item("Lista de Personas a Pagar", "PAG", "85.png", "getPersonasAPagarGridFrame", a);
                Item todos = new Item("Lista de Pagos", "PAG", "85.png", "getPagosGrid", a);
                Item rem = new Item("Nueva Remesa",
                        "PAG", "money.png", "getNewRemesa", a);
                Item remList = new Item("Lista de Remesas",
                        "PAG", "85.png", "getGridRemesa", a);
                
                pays.getItems().add(pago);
                pays.getItems().add(persPag);
                pays.getItems().add(todos);
                pays.getItems().add(rem);
                pays.getItems().add(remList);
                
            }
            root.getItems().add(pays);
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="Mantenimiento">
            //Mantenimiento
            Item mantenimiento = new Item("Mantenimiento", null, a);
            {//Mantenimiento Roles
                Item asegurados = new Item("Asegurados", null, a);
                {
                    Item departamento = new Item("Departamentos", "ASE", null, "getDepartamento", a);
                    Item contrato = new Item("Tipo Contrato Nomina", "ASE", null, "getTipoContrato", a);
                    Item parentescos = new Item("Parentescos", "ASE", null, "getParentesco", a);
                    Item plazosEspera = new Item("Plazos de Escpera", "ASE", null, "getPlazoEspera", a);
                    Item recalculo = new Item("Recalcular Prima y Edad", "ASE", null, null, a);
                    
                    asegurados.getItems().add(departamento);
                    asegurados.getItems().add(contrato);
                    asegurados.getItems().add(parentescos);
                    asegurados.getItems().add(plazosEspera);
                    asegurados.getItems().add(recalculo);
                }
                mantenimiento.getItems().add(asegurados);
                
                Item pagos = new Item("Pagos", null, a);
                {
                    Item cob = new Item("Coberturas",
                            "PAG", "rols.png", "getCoberturas", a);
                    Item confCob = new Item("Configurar Coberturas",
                            "PAG", "ramos_manager.png", "getConfiguracionCoberturas", a);
                    pagos.getItems().add(cob);
                    pagos.getItems().add(confCob);
                    
                }
                mantenimiento.getItems().add(pagos);
                Item convenio = new Item("Convenios", null, a);
                {
                    Item pp = new Item("Descuento Pronto Pago",
                            "CON", "poliza.png", "getProntoPago", a);
                    Item tm = new Item("Timbre Municipal",
                            "CON", "poliza.png", "getTimbreMunicipal", a);
                    convenio.getItems().add(pp);
                    convenio.getItems().add(tm);
                }
                mantenimiento.getItems().add(convenio);



                //Mantenimiento Personas
                Item manPer = new Item("Personas", null, a);
                
                {
                    mantenimiento.getItems().add(manPer);
                    Item actEco = new Item("Tipo Actividad Economica",
                            "MAN", "glod.png", "getTipoActividadEconomica", a);
                    Item capEco = new Item("Tipo Capacidad Economica",
                            "MAN", "glod.png", "getTipoCapacidadEconomica", a);
                    Item tipPer = new Item("Tipo Persona",
                            "MAN", "user3.png", "getTipoPersona", a);
                    Item tipTel = new Item("Tipo Telefono",
                            "MAN", "phone2.png", "getTipoTelefono", a);
                    Item tipDir = new Item("Tipo Direccion",
                            "MAN", "home.png", "getTipoDireccion", a);
                    Item tipCtB = new Item("Tipo Cuenta Bancaria",
                            "MAN", "money.png", "getTipoCuentaBancaria", a);
                    Item codigoArea = new Item("Codigos de Area", "MAN", "phonecode.png", "getCodigoArea", a);
                    manPer.getItems().add(actEco);
                    manPer.getItems().add(capEco);
                    manPer.getItems().add(tipPer);
                    manPer.getItems().add(tipTel);
                    manPer.getItems().add(tipDir);
                    manPer.getItems().add(tipCtB);
                    manPer.getItems().add(codigoArea);
                }
                
                Item polizas = new Item("Polizas", null, a);
                
                {
                    Item planes = new Item("Planes", "MAN", "planes.png", "getPlan", a);
                    
                    Item RamoCobertura = new Item("Ramo-Cobertura",
                            "MAN", "ramo-cobertura.png", "getRamoCobertura", a);
                    
                    Item configPrima = new Item("Configuracion Prima",
                            "MAN", "settings.png", "getConfiguracionPrima", a);
                    polizas.getItems().add(planes);
                    polizas.getItems().add(RamoCobertura);
                    polizas.getItems().add(configPrima);
                }
                mantenimiento.getItems().add(polizas);
                
                Item roles = new Item("Roles", "seguridad.png", a);
                {
                    Item rol = new Item("Lista Roles",
                            "ROL", "rols.png", "getNuevoRol", a);
                    Item rolOp = new Item("Configurar Roles",
                            "ROL", "ramos_manager.png", "getRoles", a);
                    roles.getItems().add(rol);
                    roles.getItems().add(rolOp);
                    
                }
                mantenimiento.getItems().add(roles);
                
                Item siniestros = new Item("Siniestros", null, a);
                
                {
                    Item tipoSiniestro = new Item("Tipo Siniestro", "SIN", "asegurado.png", "getTipoSiniestro", a);
                    Item conceptoSeniat = new Item("Conceto SENIAT", "SIN", "asegurado.png", "getConceptoSENIAT", a);
                    Item patologias = new Item("Patologias Medicas", "SIN", "virussafe.png", "getPatologias", a);
                    Item etapaSiniestro = new Item("Etapa Siniestro", "SIN", "asegurado.png", "getEtapaSiniestro", a);
                    siniestros.getItems().add(tipoSiniestro);
                    siniestros.getItems().add(patologias);
                    siniestros.getItems().add(conceptoSeniat);
                    siniestros.getItems().add(etapaSiniestro);
                }
                mantenimiento.getItems().add(siniestros);


                // mantenimiento Sistema
                Item mantSistema = new Item("Sistema", null, a);
                
                {
                    mantenimiento.getItems().add(mantSistema);
                    Item usuario = new Item("Usuarios",
                            "MAN", "user3.png", "getUsuarios", a);
                    mantSistema.getItems().add(usuario);
                    Item passw = new Item("Cambiar Contraseña",
                            "MAN", "password.png", "getCambiarPass", a);
                    mantSistema.getItems().add(passw);
                    
                    Item empresa = new Item("Empresa",
                            "MAN", "home.png", "getEmpresa", a);
                    mantSistema.getItems().add(empresa);
                    Item configuracion = new Item("Configuracion",
                            "MAN", "miscellaneous2.png", "getConfiguracion", a);
                    mantSistema.getItems().add(configuracion);
                    if (General.usuario.getAdministrador()) {
                        Item licencias = new Item("Licencias",
                                "MAN", "miscellaneous2.png", "getLicencias", a);
                        mantSistema.getItems().add(licencias);
                    }
                    
                    Item configLnF = new Item("Configuracion Look and Feel",
                            "MAN", "settings.png", "getConfigLnF", a);
                    mantSistema.getItems().add(configLnF);
                    Item configGen = new Item("Configuraciones Generales",
                            "MAN", "sharemanager.png", "getConfigGen", a);
                    mantSistema.getItems().add(configGen);
                }
                
                
                
                
                Item tipDocAnex = new Item("Tipo Documentos Anexos",
                        "MAN", "85.png", "getTipoDocAnex", a);
                mantenimiento.getItems().add(tipDocAnex);
                
                Item encabezado = new Item("Encabezados de Reporte",
                        "MAN", null, "getEncabezado", a);
                if (General.usuario.getAdministrador()) {
                    mantenimiento.getItems().add(encabezado);
                }
            }
            
            root.getItems().add(mantenimiento);
            // </editor-fold>                                     

            // <editor-fold defaultstate="collapsed" desc="Log">
            //Reportes

            Item log = new Item("Auditoria",
                    "MAN", "audit.png", "getLog", a);
            
            root.getItems().add(log);
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="Reportes">
            //Reportes

            Item reportes = new Item("Reportes",
                    "REPORT", "printer2.png", "getReporteH", a);
            
            root.getItems().add(reportes);
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="Soporte Tecnico">
            Item helpCenter = new Item("Soporte Técnico",
                    "HelpCenter", "helpcenter.png", "getHelpCenter", a);
            
            root.getItems().add(helpCenter);
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="Salir">
            Item exit = new Item("Salir",
                    "HELP", "exit.png", "getExit", a);
            
            root.getItems().add(exit);
            // </editor-fold>

            s.saveOrUpdate(root);
            tx.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            s.close();
        }
        
    }
    
    public static void printMenuIds(Item i, int level) {
        System.out.print(i.getId());
        for (int k = 0; k < level; k++) {
            System.out.print(" ");
        }
        System.out.println(i.getNombre());
        for (Item item : i.getItems()) {
            printMenuIds(item, level + 1);
        }
    }
    
    public static void llenarRoles() {
        Session s = null;
        Date d = new Date();
        AuditoriaBasica audit = (new AuditoriaBasica(d, "defaultData", true));
        AuditoriaBasica audit1 = (new AuditoriaBasica(d, "defaultData", true));
        try {
            s = HibernateUtil.getSessionFactory().openSession();
            Transaction tx = null;

            // <editor-fold defaultstate="collapsed" desc="Roles">
            tx = s.beginTransaction();
            Rol root = new Rol("Administrador del Sistema");
            {
                root.setAuditoria(audit);
                root.getAuditoria().setActivo(Boolean.FALSE);
                root.getAuditoria().setVisible2(Boolean.FALSE);
                s.save(root);
            }
            Rol secretaria = new Rol("USUARIOS");
            {
                secretaria.setAuditoria(audit1);
                secretaria.getAuditoria().setActivo(Boolean.TRUE);
                secretaria.getAuditoria().setVisible2(Boolean.TRUE);
                s.save(secretaria);
            }
            tx.commit();
            // </editor-fold>

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            s.close();
        }
        
    }
    
    public static void llenarMenuByRoles() {
        Session s = null;
        try {
            s = HibernateUtil.getSessionFactory().openSession();
            Transaction tx = null;

            // <editor-fold defaultstate="collapsed" desc="Roles">
            tx = s.beginTransaction();
            java.util.List roles = optenerData(Rol.class.getName());
            Item items = (Item) optenerData(Item.class.getName()
                    + " where nombre='root'").get(0);
            for (Object object : roles) {
                Rol rol = (Rol) object;
                for (Item item : items.getItems()) {
                    fillMenu(rol, item, s);
                }
            }
            
            tx.commit();
            // </editor-fold>

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            s.close();
        }
        
    }
    
    static void fillMenu(Rol rol, Item items, Session s) {
        MenuByRol nuevo = new MenuByRol(items.getId(), rol.getId(), Boolean.TRUE);
        s.save(nuevo);
        for (Item item : items.getItems()) {
            fillMenu(rol, item, s);
        }
    }
    
    static java.util.List optenerData(String valueObjectClassName) {
        
        Session s = null;
        java.util.List mensajes = null;
        try {
            s = HibernateUtil.getSessionFactory().openSession();
            Transaction t = s.beginTransaction();
            {
                String hql = "FROM " + valueObjectClassName;
                Query query = s.createQuery(hql);
                mensajes = query.list();
            }
            t.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            s.close();
        }
        return mensajes;
    }
    
    public static void saveData(Object o) {
        Session s = null;
        try {
            s = HibernateUtil.getSessionFactory().openSession();
            Transaction tx = null;
            
            tx = s.beginTransaction();
            s.save(o);
            tx.commit();
            
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            s.close();
        }
    }
    
    public static void updateData(Object o) {
        Session s = null;
        try {
            s = HibernateUtil.getSessionFactory().openSession();
            Transaction tx = null;
            tx = s.beginTransaction();
            s.update(o);
            tx.commit();
            
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            s.close();
        }
    }
    
    public static void addMenu(Item menu) {
        Item root = new LlenarMenu().getRootItem();
        root.getItems().add(menu);
        updateData(root);
        Session s = null;
        try {
            s = HibernateUtil.getSessionFactory().openSession();
            Transaction tx = null;
            
            tx = s.beginTransaction();
            java.util.List roles = optenerData(Rol.class.getName());
            for (Object object : roles) {
                Rol rol = (Rol) object;
                fillMenu(rol, menu, s);
                for (Item item : menu.getItems()) {
                    fillMenu(rol, item, s);
                }
            }
            tx.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            s.close();
        }
    }
    
    private static Item getRootItem() {
        Session s = null;
        try {
            s = HibernateUtil.getSessionFactory().openSession();
            Item items = (Item) optenerData(Item.class.getName()
                    + " where nombre='root'").get(0);
            return items;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            s.close();
        }
        return null;
    }
    
    public static void updateMenu() {
        printMenuIds(getRootItem(), 0);
        
        Session s = null;
        try {
            s = HibernateUtil.getSessionFactory().openSession();
            Transaction tx = s.beginTransaction();
            Item i = (Item) optenerData(Item.class.getName()
                    + " where id=122").get(0);
            Item nuevoItem = new Item("Nueva Carpeta", null, null);
            int pos = 1;
            i.getItems().add(pos, nuevoItem);
            s.saveOrUpdate(i);
            
            java.util.List roles = optenerData(Rol.class.getName());
            for (Object object : roles) {
                Rol rol = (Rol) object;
                fillMenu(rol, nuevoItem, s);
                
            }
            tx.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            s.close();
        }
        //i.getItems().add(1, new Item("nueva carpeta",null,a););
    }
}
