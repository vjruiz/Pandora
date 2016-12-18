package com.spring.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.springframework.stereotype.Component;

import com.utilidades.ficheros.ConvertirXLSHTML;


@SuppressWarnings("serial")
@Component("serverdefinido")
public class ServerDefinido implements Serializable {

	/**
	 * 
	 */
	@SuppressWarnings("unused")
	private final long serialVersionUID = 3683687261914085091L;
    private Session session;    
    private Properties properties;
    private InputStream is = null;
    private Transport transportServ;    
	private String id_server;
	private String ob_server;
	private String id_usuario;
	private String id_contrasena;	
	private String ds_dominio;     
	private String ds_host_smtp;
	private String id_puerto_host;
	private String ds_cadena_conexion;
    	 
	
   	public ServerDefinido() {
	}


	public ServerDefinido(String id_server, String ob_server, String id_usuario, String id_contrasena, String ds_dominio,
			String ds_host_smtp, String id_puerto_host, String ds_cadena_conexion) {
		super();
		this.id_server = id_server;
		this.ob_server = ob_server;
		this.id_usuario = id_usuario;
		this.id_contrasena = id_contrasena;
		this.ds_dominio = ds_dominio;
		this.ds_host_smtp = ds_host_smtp;
		this.id_puerto_host = id_puerto_host;
		this.ds_cadena_conexion = ds_cadena_conexion;
	}


	public String getId_server() {
		return id_server;
	}


	public void setId_server(String id_server) {
		this.id_server = id_server;
	}

	public String getOb_server() {
		return ob_server;
	}


	public void setOb_server(String ob_server) {
		this.ob_server = ob_server;
	}


	public String getId_usuario() {
		return id_usuario;
	}


	public void setId_usuario(String id_usuario) {
		this.id_usuario = id_usuario;
	}


	public String getId_contrasena() {
		return id_contrasena;
	}


	public void setId_contrasena(String id_contrasena) {
		this.id_contrasena = id_contrasena;
	}


	public String getDs_dominio() {
		return ds_dominio;
	}


	public void setDs_dominio(String ds_dominio) {
		this.ds_dominio = ds_dominio;
	}


	public String getDs_host_smtp() {
		return ds_host_smtp;
	}


	public void setDs_host_smtp(String ds_host_smtp) {
		this.ds_host_smtp = ds_host_smtp;
	}


	public String getId_puerto_host() {
		return id_puerto_host;
	}


	public void setId_puerto_host(String id_puerto_host) {
		this.id_puerto_host = id_puerto_host;
	}


	public String getDs_cadena_conexion() {
		return ds_cadena_conexion;
	}


	public void setDs_cadena_conexion(String ds_cadena_conexion) {
		this.ds_cadena_conexion = ds_cadena_conexion;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ds_cadena_conexion == null) ? 0 : ds_cadena_conexion.hashCode());
		result = prime * result + ((ds_dominio == null) ? 0 : ds_dominio.hashCode());
		result = prime * result + ((ds_host_smtp == null) ? 0 : ds_host_smtp.hashCode());
		result = prime * result + ((id_contrasena == null) ? 0 : id_contrasena.hashCode());
		result = prime * result + ((id_puerto_host == null) ? 0 : id_puerto_host.hashCode());
		result = prime * result + ((id_server == null) ? 0 : id_server.hashCode());
		result = prime * result + ((id_usuario == null) ? 0 : id_usuario.hashCode());
		result = prime * result + ((ob_server == null) ? 0 : ob_server.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ServerDefinido other = (ServerDefinido) obj;
		if (ds_cadena_conexion == null) {
			if (other.ds_cadena_conexion != null)
				return false;
		} else if (!ds_cadena_conexion.equals(other.ds_cadena_conexion))
			return false;
		if (ds_dominio == null) {
			if (other.ds_dominio != null)
				return false;
		} else if (!ds_dominio.equals(other.ds_dominio))
			return false;
		if (ds_host_smtp == null) {
			if (other.ds_host_smtp != null)
				return false;
		} else if (!ds_host_smtp.equals(other.ds_host_smtp))
			return false;
		if (id_contrasena == null) {
			if (other.id_contrasena != null)
				return false;
		} else if (!id_contrasena.equals(other.id_contrasena))
			return false;
		if (id_puerto_host == null) {
			if (other.id_puerto_host != null)
				return false;
		} else if (!id_puerto_host.equals(other.id_puerto_host))
			return false;
		if (id_server == null) {
			if (other.id_server != null)
				return false;
		} else if (!id_server.equals(other.id_server))
			return false;
		if (id_usuario == null) {
			if (other.id_usuario != null)
				return false;
		} else if (!id_usuario.equals(other.id_usuario))
			return false;
		if (ob_server == null) {
			if (other.ob_server != null)
				return false;
		} else if (!ob_server.equals(other.ob_server))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "ServeDefinido [id_server=" + id_server + ", ob_server=" + ob_server + ", id_usuario=" + id_usuario
				+ ", id_contrasena=" + id_contrasena + ", ds_dominio=" + ds_dominio + ", ds_host_smtp=" + ds_host_smtp
				+ ", id_puerto_host=" + id_puerto_host + ", ds_cadena_conexion=" + ds_cadena_conexion + "]";
	} 	

	
	public boolean enviarCorreo(String pathFicheroEnvio,
								String nombreFicheroEnvio,
								String asuntoMensaje,
								String destinatariosPara,
								String destinatariosCC)
	{
        boolean resultadoEnvio = true;
        if ( configurarSession() & abrirConexion())
        {            
            //enviamos el Email del fichero con los datos recuperados
            // de la tabla.
            resultadoEnvio =
                enviarMensaje(
                		montarMensajeConFicheroCuerpoHtml(
                					new File( pathFicheroEnvio
                							+ nombreFicheroEnvio),
                							asuntoMensaje,
                							destinatariosPara,
                							destinatariosCC));
            // se cierra la conexion con el servidor
            cerrarConexion();
        } else
        {
        	resultadoEnvio = false;
        }
        
		return resultadoEnvio;
	}

	public boolean enviarCorreo(String pathFicheroEnvio, 
								String nombreFicheroEnvio, 
								String asuntoMensaje,
								String cuerpoMensaje,
								String destinatariosPara, 
								String destinatariosCC) {
		boolean resultadoEnvio = true;
		if (configurarSession() & abrirConexion()) {
			// enviamos el Email del fichero con los datos recuperados
			// de la tabla.
			resultadoEnvio = enviarMensaje(
					montarMensajeConFicheroSinCuerpoHtml(
							new File(pathFicheroEnvio + nombreFicheroEnvio), 
							asuntoMensaje,
							cuerpoMensaje,
							destinatariosPara, 
							destinatariosCC));
			// se cierra la conexion con el servidor
			cerrarConexion();
		} else {
			resultadoEnvio = false;
		}

		return resultadoEnvio;
	}
	
	public boolean enviarCorreo(String asuntoMensaje,
								String cuerpoMensaje,
								String destinatariosPara, 
								String destinatariosCC)			 
	{
		boolean resultadoEnvio = true;
		if (configurarSession() & abrirConexion()) {
			// enviamos el Email del fichero con los datos recuperados
			// de la tabla.
            resultadoEnvio =
                    enviarMensaje(
                    		montarMensajeSinFichero(                    				
                    							asuntoMensaje,
                    							cuerpoMensaje,
                    							destinatariosPara,
                    							destinatariosCC));
			// se cierra la conexion con el servidor
			cerrarConexion();
		} else {
			resultadoEnvio = false;
		}

		return resultadoEnvio;
	}
    
    private boolean configurarSession() 
    {      
        boolean controlCargaSession;

        try {
            is = new FileInputStream("C:\\encelado\\config\\configuracionServidor.properties");
            properties = new Properties();
            properties.load(is);

            System.out.println("prueba envio " + this.getDs_host_smtp());
            
            properties.setProperty("mail.smtp.host", this.getDs_host_smtp());
            properties.setProperty("mail.smtp.port", this.getId_puerto_host());            
            properties.setProperty("mail.smtp.user", this.getId_usuario());
            properties.setProperty("mail.smtp.pass", this.getId_contrasena());
            properties.setProperty("mail.smtp.mail.sender", this.getDs_dominio());
            
            System.out.println("host.-" + properties.getProperty("mail.smtp.host"));
            System.out.println("port.-" + properties.getProperty("mail.smtp.port"));
            System.out.println("user.-" + properties.getProperty("mail.smtp.user"));
            System.out.println("pass.-" + properties.getProperty("mail.smtp.pass"));
            System.out.println("mail.sender.-" + properties.getProperty("mail.smtp.mail.sender"));
            session = Session.getDefaultInstance(properties);
                session.setDebug(true);
             
            controlCargaSession = true;

        } catch (FileNotFoundException ex) {
            System.out.println("Fichero configuracion no existe");
            controlCargaSession = false;            
        } catch (IOException ex) {
            System.out.println("No se puede cargar fichero.");
            controlCargaSession = false;                        
        }
        
        return controlCargaSession;
    }
    
    private boolean abrirConexion() 
    {   
        System.out.println("abrir conexion");
        boolean controlApertura;
        
        try {            
            // creamos el objeto transport que nos permite realizar el envio.
            transportServ = session.getTransport("smtp");
            transportServ.connect((String)properties.get("mail.smtp.user"), 
                                  (String)properties.get("mail.smtp.pass"));
            controlApertura = true;
            
        } catch (NoSuchProviderException ex) {
            System.out.println("Problemas al abrir conexion" + ex);
            controlApertura = false;
        } catch (MessagingException ex) {
            System.out.println("Problemas al abrir conexion" + ex);
            controlApertura = false;            
        }
        
        return controlApertura;
    }
    
    private void cerrarConexion() 
    {                 
        try {   
            // cerramos la conexion con el servidor de correo
            transportServ.close();  
            
        } catch (NoSuchProviderException ex) {
            System.out.println("No se ha cerrado la conexion con el servidor." + ex);
        } catch (MessagingException ex) {
            System.out.println("No se ha cerrado la conexion con el servidor." + ex);
        }               
    }    

    private boolean enviarMensaje(MimeMessage message) 
    {                 
        boolean controlEnvio;     
        
        try {
            transportServ.sendMessage(message, message.getAllRecipients());
            controlEnvio = true;
            
        } catch (NoSuchProviderException ex) {
            System.out.println("No se ha realizado el envio." + ex);
            controlEnvio = false;
        } catch (MessagingException ex) {
            System.out.println("No se ha realizado el envio." + ex);
            controlEnvio = false;            
        }
        
        return controlEnvio;
    }
    
    public MimeMessage montarMensajeConFicheroCuerpoHtml(File archivoAdjuntoEnvio,
                                String entradaAsunto,
                                String destinatarios,
                                String destinatariosCC) 
    {   
        MimeMessage message = null;
        MimeMultipart multiParte  = null;
        BodyPart adjunto = null;
        BodyPart texto = null;
        
        try{      
            // Recuperamos el cuerpo del mensaje desde un HTML.
            texto = new MimeBodyPart();                      
                texto.setContent(crearCuerpoHTML(archivoAdjuntoEnvio),"text/alternative");

            // creamos la parte del correo con el fichero adjunto.
            adjunto = new MimeBodyPart();
               adjunto.setDataHandler(new DataHandler(new FileDataSource(archivoAdjuntoEnvio)));
                    adjunto.setFileName(archivoAdjuntoEnvio.getName());

            //a�adimos todas las partes.
            multiParte = new MimeMultipart();
                    multiParte.addBodyPart( texto );
                    multiParte.addBodyPart(adjunto);                                               

            // Creamos un objeto mensaje tipo MimeMessage por defecto.
            message = new MimeMessage(session);

                // Asignamos el 
                message.setFrom(new InternetAddress((String)
                        properties.get("mail.smtp.mail.sender")));

                // Asignamos el 
                message.addRecipients(Message.RecipientType.TO, 
                                     componerDestinatarios(destinatarios));
                // Asignamos el
                if (!destinatariosCC.equals(""))
                {
                	message.addRecipients(Message.RecipientType.CC, 
    						componerDestinatarios(destinatariosCC));
                }             

                 // Asignamos el asunto                             
                message.setSubject(entradaAsunto);                            

                // Juntamos todas las partes del correo antes del envio
                message.setContent(multiParte);                
                              
        }catch (MessagingException me){
            System.out.println("Error en envio de mensaje.-" + me);                            			
        } catch (FileNotFoundException ex) {       
            System.out.println("No existe fichero envio.-" + ex);                            	
        } catch (IOException ex) {
            System.out.println("Problemas con el fichero adjunto." + ex); 
        }
        
        return message;
    }

	public MimeMessage montarMensajeConFicheroSinCuerpoHtml(File archivoAdjuntoEnvio, String entradaAsunto,
						String cuerpoMensajeo,String destinatarios, String destinatariosCC) {
		MimeMessage message = null;
		MimeMultipart multiParte = null;
		BodyPart adjunto = null;
		BodyPart texto = null;

		try {
			// Recuperamos el cuerpo del mensaje desde un HTML.
			texto = new MimeBodyPart();
			texto.setContent(cuerpoMensajeo, "text/alternative");

			// creamos la parte del correo con el fichero adjunto.
			adjunto = new MimeBodyPart();
			adjunto.setDataHandler(new DataHandler(new FileDataSource(archivoAdjuntoEnvio)));
			adjunto.setFileName(archivoAdjuntoEnvio.getName());

			// a�adimos todas las partes.
			multiParte = new MimeMultipart();
			multiParte.addBodyPart(texto);
			multiParte.addBodyPart(adjunto);

			// Creamos un objeto mensaje tipo MimeMessage por defecto.
			message = new MimeMessage(session);

			// Asignamos el
			message.setFrom(new InternetAddress((String) properties.get("mail.smtp.mail.sender")));

			// Asignamos el
			message.addRecipients(Message.RecipientType.TO, componerDestinatarios(destinatarios));
            // Asignamos el
            if (!destinatariosCC.equals(""))
            {
            	message.addRecipients(Message.RecipientType.CC, 
						componerDestinatarios(destinatariosCC));
            }

			// Asignamos el asunto
			message.setSubject(entradaAsunto);

			// Juntamos todas las partes del correo antes del envio
			message.setContent(multiParte);

		} catch (MessagingException me) {
			System.out.println("Error en envio de mensaje.-" + me);
		} 

		return message;
	}
    
	public MimeMessage montarMensajeSinFichero(String asuntoMensaje,String cuerpoMensaje,
											String destinatarios,String destinatariosCC) 			
	{
		MimeMessage message = null;
		MimeMultipart multiParte = null;
		BodyPart texto = null;

		try {

            // Recuperamos el cuerpo del mensaje desde un HTML.
            texto = new MimeBodyPart();                      
                texto.setContent(cuerpoMensaje,"text/alternative");
			
			// a�adimos todas las partes.
			multiParte = new MimeMultipart();
			multiParte.addBodyPart(texto);

			// Creamos un objeto mensaje tipo MimeMessage por defecto.
			message = new MimeMessage(session);

			// Asignamos el
			message.setFrom(new InternetAddress((String) properties.get("mail.smtp.mail.sender")));

			// Asignamos el
			message.addRecipients(Message.RecipientType.TO, componerDestinatarios(destinatarios));
            // Asignamos el
            if (!destinatariosCC.equals(""))
            {
            	message.addRecipients(Message.RecipientType.CC, 
						componerDestinatarios(destinatariosCC));
            }

			// Asignamos el asunto
			message.setSubject(asuntoMensaje);

			// Juntamos todas las partes del correo antes del envio
			message.setContent(multiParte);

		} catch (MessagingException me) {
			System.out.println("Error en envio de mensaje.-" + me);
		} 

		return message;
	}
    
    private static Address[] componerDestinatarios(String destinatariosIO) 
    {           
        String[] recipientList = destinatariosIO.split(";");        
        InternetAddress[] recipientAddress = new InternetAddress[recipientList.length];
        int contador = 0;
        
        for (String recipient : recipientList) {
            try {
                recipientAddress[contador] = new InternetAddress(recipient.trim());
            } catch (AddressException ex) {
                Logger.getLogger(ServerDefinido.class.getName()).log(Level.SEVERE, null, ex);
            }
            contador++;
        }
        
        return recipientAddress;
    }

    /**
    *
    * @param archivoEnvio
    * @return
    * @throws java.io.FileNotFoundException
    */
	public static String crearCuerpoHTML(File archivoEnvio) throws FileNotFoundException, IOException {

		StringBuilder sb = new StringBuilder();
		ConvertirXLSHTML conversor;

		conversor = new ConvertirXLSHTML(new FileInputStream(archivoEnvio), 0);

		sb.append("<HTML>\n");
		sb.append("<HEAD>\n");
		sb.append("<TITLE>\n");
		sb.append("prueba" + "\n");
		sb.append("</TITLE>\n");
		sb.append("</HEAD>\n");
		sb.append("<BODY>\n");
		sb.append(conversor.getHTML());
		sb.append("</BODY>\n");
		sb.append("</HTML>\n");

		return sb.toString();
	}
}
