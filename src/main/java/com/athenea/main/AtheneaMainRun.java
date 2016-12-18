package com.athenea.main;

import java.io.IOException;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class AtheneaMainRun {

	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException, IOException {
        Object[] opciones={"Lanzar informeitor, ala Madrid", "Nada, entonces pa que me lanzas.",
        "Bien,tal vez quiera saber quien gana la champions."};
                   
    int n=JOptionPane.showOptionDialog(null,"�Que quieres hacer?",
            "Contesta la pregunta",
            JOptionPane.YES_NO_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            opciones,
            opciones[2]);
    
    if(n==0){
                
        // Sirve para carga la tabla maestra de protocolos.        
        //new cargarMaestraProtocolos().cargarItsms();        
        
        // Se actualizan los datos de Hades con el total de ITSMs con protocolos Referencia/Minorista
        // y el total sin protocolo de todos los buzones del dominio.
/*        new CargarTotalITSMBD().realizarCarga("hadesindra","athenea");
        new CargarITSMProtocoloBD().realizarCarga("hadesindra","athenea");*/
        // se activa el diario correspondiente segun el dia y la hora.
        
        // Se envia mensaje de fin al operario.
        JOptionPane.showMessageDialog(null,"Termine Mu�on's�����","Aviso",JOptionPane.PLAIN_MESSAGE);
    }
    else if(n==1){
        JOptionPane.showMessageDialog(null,"Pues ala me piro","Aviso",JOptionPane.PLAIN_MESSAGE);
    }
    else
        JOptionPane.showMessageDialog(null,"Pues el Madrid , no la van a ganar los indios���","Aviso",JOptionPane.PLAIN_MESSAGE);

    
    
    //ZONA TEST
    //ejecucion unitaria de un flujo de reportes.
    //new ControlPeticionReportes().ejecutarPeticiones("caso2345", 2255);
    
                   
    // test mostrar jasper en pantalla        
    //JasperViewer viewer = new JasperViewer(EjecutarReportes.getInstance().ejecutarInforme(
    //        ConexionBD.getInstance().devolverConexion(), 
    //        new FileInputStream(new File(
    //            "C:\\Users\\vjruiz\\Documents\\"
    //                    + "NetBeansProjects\\Athenea\\src\\es\\"
    //                   + "indra\\athenea\\report\\templates\\report2.jasper")),null,null));        
    //viewer.setTitle("Prueba");
    //viewer.setVisible(true);


	}

}
