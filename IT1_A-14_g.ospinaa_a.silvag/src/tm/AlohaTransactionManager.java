package tm;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Properties;

import EstructurasAuxiliares.Pair;
import dao.*;
import vos.*;

public class AlohaTransactionManager {

	//////////////////////////////
	////////// CONSTANTES//////////
	//////////////////////////////

	/**
	 * Constante que contiene el path relativo del archivo que tiene los datos
	 * de la conexion
	 */
	private static final String CONNECTION_DATA_FILE_NAME_REMOTE = "/conexion.properties";
	public final static String USUARIO = "ISIS2304A311810";

	/**
	 * Atributo estatico que contiene el path absoluto del archivo que tiene los
	 * datos de la conexion
	 */
	private static String CONNECTION_DATA_PATH;

	private final static Integer CANTIDAD_MAXIMA_HOTELES = 10;

	private final static Integer CANTIDAD_MAXIMA_HOSTELES = 12;

	///////////////////////////////
	////////// ATRIBUTOS////////////
	///////////////////////////////

	/**
	 * Atributo que guarda el usuario que se va a usar para conectarse a la base
	 * de datos.
	 */
	private String user;

	/**
	 * Atributo que guarda la clave que se va a usar para conectarse a la base
	 * de datos.
	 */
	private String password;

	/**
	 * Atributo que guarda el URL que se va a usar para conectarse a la base de
	 * datos.
	 */
	private String url;

	/**
	 * Atributo que guarda el driver que se va a usar para conectarse a la base
	 * de datos.
	 */
	private String driver;

	/**
	 * Atributo que representa la conexion a la base de datos
	 */
	private Connection conn;

	////////////////////////////////////////
	// METODOS DE CONEXION E INICIALIZACION//
	////////////////////////////////////////

	public AlohaTransactionManager(String contextPathP) {

		try {
			CONNECTION_DATA_PATH = contextPathP + CONNECTION_DATA_FILE_NAME_REMOTE;
			initializeConnectionData();
		} 
		catch (ClassNotFoundException e) {			
			e.printStackTrace();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Metodo encargado de inicializar los atributos utilizados para la conexion con la Base de Datos.<br/>
	 * <b>post: </b> Se inicializan los atributos para la conexion<br/>
	 * @throws IOException Se genera una excepcion al no encontrar el archivo o al tener dificultades durante su lectura<br/>
	 * @throws ClassNotFoundException 
	 */
	private void initializeConnectionData() throws IOException, ClassNotFoundException {

		FileInputStream fileInputStream = new FileInputStream(new File(AlohaTransactionManager.CONNECTION_DATA_PATH));
		Properties properties = new Properties();
		
		properties.load(fileInputStream);
		fileInputStream.close();
		
		this.url = properties.getProperty("url");
		this.user = properties.getProperty("usuario");
		this.password = properties.getProperty("clave");
		this.driver = properties.getProperty("driver");
		
		//Class.forName(driver);
	}
	
	
	/**
	 * Metodo encargado de generar una conexion con la Base de Datos.<br/>
	 * <b>Precondicion: </b>Los atributos para la conexion con la Base de Datos han sido inicializados<br/>
	 * @return Objeto Connection, el cual hace referencia a la conexion a la base de datos
	 * @throws SQLException Cualquier error que se pueda llegar a generar durante la conexion a la base de datos
	 */
	private Connection darConexion() throws SQLException {
		System.out.println("[ALOHA APP] Attempting Connection to: " + url + " - By User: " + user);
		return DriverManager.getConnection(url, user, password);
	}
	
	///////////////////////////////
	////METODOS TRANSACCIONALES////
	///////////////////////////////
	
	//---------------------------------------------------------------------//
	//-----------------------METODOS USUARIOS------------------------------//
	//---------------------------------------------------------------------//
	
	/**
	 * Metodo que modela la transaccion que retorna todos los usuarioes de la base de datos. <br/>
	 * @return List<Usuario> - Lista de usuarioes que contiene el resultado de la consulta.
	 * @throws Exception -  Cualquier error que se genere durante la transaccion
	 */
	public List<Usuario> getAllUsuarios() throws Exception {
		DAOUsuario daoUsuario = new DAOUsuario();
		List<Usuario> usuarios;
		try 
		{
			this.conn = darConexion();
			daoUsuario.setConn(conn);
			
			//Por simplicidad, solamente se obtienen los primeros 50 resultados de la consulta
			usuarios = daoUsuario.getUsuario();
		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoUsuario.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return usuarios;
	}
	
	/**
	 * Metodo que modela la transaccion que busca el usuario en la base de datos que tiene el ID dado por parametro. <br/>
	 * @param name -id del usuario a buscar. id != null
	 * @return Usuario - Usuario que se obtiene como resultado de la consulta.
	 * @throws Exception -  cualquier error que se genere durante la transaccion
	 */
	public Usuario getUsuarioByCodigo(Long id) throws Exception {
		DAOUsuario daoUsuario = new DAOUsuario();
		Usuario usuario = null;
		try 
		{
			this.conn = darConexion();
			daoUsuario.setConn(conn);
			usuario = daoUsuario.findUsuarioByCodigo(id);
			if(usuario == null)
			{
				throw new Exception("El usuario con el codigo = " + id + " no se encuentra persistido en la base de datos.");				
			}
		} 
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoUsuario.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return usuario;
	}
	
	/**
	 * Metodo que modela la transaccion que agrega un usuario a la base de datos. <br/>
	 * <b> post: </b> se ha agregado el usuario que entra como parametro <br/>
	 * @param usuario - el usuario a agregar. usuario != null
	 * @throws Exception - Cualquier error que se genere agregando el usuario
	 */
	public void addUsuario(Usuario usuario) throws Exception 
	{
		
		DAOUsuario daoUsuario = new DAOUsuario( );
		try
		{
			this.conn = darConexion();
			daoUsuario.setConn(conn);
			daoUsuario.addUsuario(usuario);

		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoUsuario.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}
	
	/**
	 * Metodo que modela la transaccion que actualiza en la base de datos al usuario que entra por parametro.<br/>
	 * Solamente se actualiza si existe el usuario en la Base de Datos <br/>
	 * <b> post: </b> se ha actualizado el usuario que entra como parametro <br/>
	 * @param usuario - Usuario a actualizar. usuario != null
	 * @throws Exception - Cualquier error que se genere actualizando al usuario.
	 */
	public void updateUsuario(Usuario usuario) throws Exception 
	{
		DAOUsuario daoUsuario = new DAOUsuario( );
		try
		{
			this.conn = darConexion();
			daoUsuario.setConn( conn );
			if(daoUsuario.findUsuarioByCodigo(usuario.getCodigoUniandes()) == null)
			{
				throw new Exception("El usuario al que esta intentando modificar no existe");
			}
			daoUsuario.updateUsuario(usuario);		

		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoUsuario.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}	
	}
	
	/**
	 * Metodo que modela la transaccion que elimina de la base de datos al usuario que entra por parametro. <br/>
	 * Solamente se actualiza si existe el usuario en la Base de Datos <br/>
	 * <b> post: </b> se ha eliminado el usuario que entra por parametro <br/>
	 * @param Usuario - usuario a eliminar. usuario != null
	 * @throws Exception - Cualquier error que se genere eliminando al usuario.
	 */
	public void deleteUsuario(Usuario usuario) throws Exception 
	{
		DAOUsuario daoUsuario = new DAOUsuario( );
		try
		{
			this.conn = darConexion();
			daoUsuario.setConn( conn );
			if(daoUsuario.findUsuarioByCodigo(usuario.getCodigoUniandes()) == null)
			{
				throw new Exception("El usuario que esta intentando eliminar no existe");
			}
			daoUsuario.deleteUsuario(usuario);

		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoUsuario.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}	
	}
	
	
		//---------------------------------------------------------------------//
		//-----------------------METODOS RESERVA-------------------------------//
		//---------------------------------------------------------------------//
		
		/**
		 * Metodo que modela la transaccion que retorna todos los reserva de la base de datos. <br/>
		 * @return List<Reserva> - Lista de reservaes que contiene el resultado de la consulta.
		 * @throws Exception -  Cualquier error que se genere durante la transaccion
		 */
		public List<Reserva> getAllReservas() throws Exception {
			DAOReserva daoReserva = new DAOReserva();
			List<Reserva> reservas;
			try 
			{
				this.conn = darConexion();
				daoReserva.setConn(conn);
				
				//Por simplicidad, solamente se obtienen los primeros 50 resultados de la consulta
				reservas = daoReserva.getReserva();
			}
			catch (SQLException sqlException) {
				System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
				sqlException.printStackTrace();
				throw sqlException;
			} 
			catch (Exception exception) {
				System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			} 
			finally {
				try {
					daoReserva.cerrarRecursos();
					if(this.conn!=null){
						this.conn.close();					
					}
				}
				catch (SQLException exception) {
					System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
					exception.printStackTrace();
					throw exception;
				}
			}
			return reservas;
		}
		
		/**
		 * Metodo que modela la transaccion que busca el reserva en la base de datos que tiene el ID dado por parametro. <br/>
		 * @param name -id del reserva a buscar. id != null
		 * @return Reserva - Reserva que se obtiene como resultado de la consulta.
		 * @throws Exception -  cualquier error que se genere durante la transaccion
		 */
		public Reserva getReservaById(Long id) throws Exception {
			DAOReserva daoReserva = new DAOReserva();
			Reserva reserva = null;
			try 
			{
				this.conn = darConexion();
				daoReserva.setConn(conn);
				reserva = daoReserva.findReservaById(id);
				if(reserva == null)
				{
					throw new Exception("El reserva con el codigo = " + id + " no se encuentra persistido en la base de datos.");				
				}
			} 
			catch (SQLException sqlException) {
				System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
				sqlException.printStackTrace();
				throw sqlException;
			} 
			catch (Exception exception) {
				System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			} 
			finally {
				try {
					daoReserva.cerrarRecursos();
					if(this.conn!=null){
						this.conn.close();					
					}
				}
				catch (SQLException exception) {
					System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
					exception.printStackTrace();
					throw exception;
				}
			}
			return reserva;
		}
		
		/**
		 * Metodo que modela la transaccion que busca el reserva en la base de datos que tiene el ID dado por parametro. <br/>
		 * @param name -id del reserva a buscar. id != null
		 * @return Reserva - Reserva que se obtiene como resultado de la consulta.
		 * @throws Exception -  cualquier error que se genere durante la transaccion
		 */
		public ArrayList<Reserva> getReservaByUsuarioOperador(Long codigo, Long opID) throws Exception {
			ArrayList<Reserva> respu = new ArrayList<>();
			DAOReserva daoReserva = new DAOReserva();
			try 
			{
				this.conn = darConexion();
				daoReserva.setConn(conn);
				respu = daoReserva.findReservaByUsuarioOperador(codigo, opID);

			} 
			catch (SQLException sqlException) {
				System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
				sqlException.printStackTrace();
				throw sqlException;
			} 
			catch (Exception exception) {
				System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			} 
			finally {
				try {
					daoReserva.cerrarRecursos();
					if(this.conn!=null){
						this.conn.close();					
					}
				}
				catch (SQLException exception) {
					System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
					exception.printStackTrace();
					throw exception;
				}
			}
			return respu;
		}
		
		public void RF7(LinkedHashMap<String, Object> mapa)throws SQLException{
			
			DAOReserva daoReserva = new DAOReserva();
			DAOOperador daoOperador =new DAOOperador();
			Statement s = conn.createStatement();
			
			try
			{
				this.conn = darConexion();
				daoReserva.setConn(conn);
				/*
				 * {
				 * cantidad: int
				 * tipo: "string"
				 * inicio: date
				 * final: date
				 * identificador: int
				 * usuario : int
				 * }
				 */
				Integer cantReservas = (Integer)mapa.get("cantidad");
				String tipo = (String)mapa.get("tipo");
				Date inicio = (Date)mapa.get("inicio");
				Date finale = (Date)mapa.get("final");
				Integer identificador = (Integer)mapa.get("identificador");
				Integer usuario = (Integer)mapa.get("usuario");
				String sql ="Alter session set isolation_level=serializable";
				
				s.addBatch(sql);
				
				ArrayList<RFC4> lista = daoOperador.RFC4(tipo, inicio, finale);
				
				for (int i = 0; i < cantReservas; i++) {
					sql = String.format("INSERT INTO RESERVAS_COLECTIVAS values (%1$d,%2$d)", identificador*i+10000,identificador);
					s.addBatch(sql);
					sql = String.format(
							"INSERT INTO %1$s.RESERVAS (ID_RESERVA, CODIGOUNIANDINO, ID_OPERADOR, CANCELADO, PRECIO, FECHA_INICIAL, FECHA_FINAL, HORA_CREACION) VALUES (%2$d, %3$d, %4$d, '%5$c', %6$f, TO_DATE('%7$tF', 'YYYY-MM-DD'), TO_DATE('%8$tF', 'YYYY-MM-DD'), CURRENT_TIMESTAMP)",
							USUARIO, identificador*i+10000, usuario, lista.get(i), '0',
							0,  inicio, finale);
					s.addBatch(sql);
				}
				s.addBatch("commit");
				s.executeBatch();
			}
			
			catch (SQLException sqlException) {
				System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
				sqlException.printStackTrace();
				throw sqlException;
			} 
			catch (Exception exception) {
				System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			} 
			finally {
				try {
					daoReserva.cerrarRecursos();
					daoOperador.cerrarRecursos();
					s.close();
					if(this.conn!=null){
						this.conn.close();					
					}
					
				}
				catch (SQLException exception) {
					System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
					exception.printStackTrace();
					throw exception;
				}
			}
		}
		
		public List<RFC4> RFC4(LinkedHashMap<String, Object> mapa) throws SQLException{
			ArrayList<RFC4> respu = new ArrayList<>();
			DAOOperador daoOperador = new DAOOperador();
			
			try
			{
				this.conn = darConexion();
				daoOperador.setConn(conn);
				respu = daoOperador.RFC4(mapa);
			}
			
			catch (SQLException sqlException) {
				System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
				sqlException.printStackTrace();
				throw sqlException;
			} 
			catch (Exception exception) {
				System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			} 
			finally {
				try {
					daoOperador.cerrarRecursos();
					if(this.conn!=null){
						this.conn.close();					
					}
				}
				catch (SQLException exception) {
					System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
					exception.printStackTrace();
					throw exception;
				}
			}
			return respu;
		}
		
		public List<RFC6> RFC6(Long usuario) throws Exception {
			ArrayList<RFC6> respu = new ArrayList<>();
			DAOReserva daoReserva = new DAOReserva();
			try
			{
				this.conn = darConexion();
				daoReserva.setConn(conn);
				respu = daoReserva.RFC6(usuario);
			}
			
			catch (SQLException sqlException) {
				System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
				sqlException.printStackTrace();
				throw sqlException;
			} 
			catch (Exception exception) {
				System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			} 
			finally {
				try {
					daoReserva.cerrarRecursos();
					if(this.conn!=null){
						this.conn.close();					
					}
				}
				catch (SQLException exception) {
					System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
					exception.printStackTrace();
					throw exception;
				}
			}
			return respu;
			
			
		}
		
		/**
		 * RFC2
		 * @return lista con 20 ofertas mas solicitadas.
		 * @throws Exception
		 */
		public List<RFC2> RFC2() throws Exception
		{
			ArrayList<RFC2> respu = new ArrayList<>();
			DAOReserva daoReserva = new DAOReserva();
			
			try
			{
				this.conn = darConexion();
				daoReserva.setConn(conn);
				respu = daoReserva.RFC2();
			}
			
			catch (SQLException sqlException) {
				System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
				sqlException.printStackTrace();
				throw sqlException;
			} 
			catch (Exception exception) {
				System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			} 
			finally {
				try {
					daoReserva.cerrarRecursos();
					if(this.conn!=null){
						this.conn.close();					
					}
				}
				catch (SQLException exception) {
					System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
					exception.printStackTrace();
					throw exception;
				}
			}
			return respu;
		}
		
		/**
		 * RFC2
		 * @return lista con 20 ofertas mas solicitadas.
		 * @throws Exception
		 */
		public List<RFC1> RFC1() throws Exception
		{
			ArrayList<RFC1> respu = new ArrayList<>();
			DAOReserva daoReserva = new DAOReserva();
			
			try
			{
				this.conn = darConexion();
				daoReserva.setConn(conn);
				respu = daoReserva.RFC1();
			}
			
			catch (SQLException sqlException) {
				System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
				sqlException.printStackTrace();
				throw sqlException;
			} 
			catch (Exception exception) {
				System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			} 
			finally {
				try {
					daoReserva.cerrarRecursos();
					if(this.conn!=null){
						this.conn.close();					
					}
				}
				catch (SQLException exception) {
					System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
					exception.printStackTrace();
					throw exception;
				}
			}
			return respu;
		}
		
		/**
		 * Metodo que modela la transaccion que agrega un reserva a la base de datos. <br/>
		 * <b> post: </b> se ha agregado el reserva que entra como parametro <br/>
		 * @param reserva - el reserva a agregar. reserva != null
		 * @throws Exception - Cualquier error que se genere agregando el reserva
		 */
		public void addReserva(Reserva reserva) throws Exception 
		{
			
			DAOReserva daoReserva = new DAOReserva( );
			try
			{
				this.conn = darConexion();
				daoReserva.setConn(conn);
				daoReserva.addReserva(reserva);

			}
			catch (SQLException sqlException) {
				System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
				sqlException.printStackTrace();
				throw sqlException;
			} 
			catch (Exception exception) {
				System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			} 
			finally {
				try {
					daoReserva.cerrarRecursos();
					if(this.conn!=null){
						this.conn.close();					
					}
				}
				catch (SQLException exception) {
					System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
					exception.printStackTrace();
					throw exception;
				}
			}
		}
		
		/**
		 * Metodo que modela la transaccion que actualiza en la base de datos al reserva que entra por parametro.<br/>
		 * Solamente se actualiza si existe el reserva en la Base de Datos <br/>
		 * <b> post: </b> se ha actualizado el reserva que entra como parametro <br/>
		 * @param reserva - Reserva a actualizar. reserva != null
		 * @throws Exception - Cualquier error que se genere actualizando al reserva.
		 */
		public void updateReserva(Reserva reserva) throws Exception 
		{
			DAOReserva daoReserva = new DAOReserva( );
			try
			{
				this.conn = darConexion();
				daoReserva.setConn( conn );
				if(daoReserva.findReservaById(reserva.getIdReserva()) == null)
				{
					throw new Exception("El reserva al que esta intentando modificar no existe");
				}
				daoReserva.updateReservas(reserva);		

			}
			catch (SQLException sqlException) {
				System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
				sqlException.printStackTrace();
				throw sqlException;
			} 
			catch (Exception exception) {
				System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			} 
			finally {
				try {
					daoReserva.cerrarRecursos();
					if(this.conn!=null){
						this.conn.close();					
					}
				}
				catch (SQLException exception) {
					System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
					exception.printStackTrace();
					throw exception;
				}
			}	
		}
		
		
		public void RF8(Long id) throws SQLException, Exception {
			DAOReserva daoReserva = new DAOReserva( );
			try
			{
				this.conn = darConexion();
				daoReserva.setConn( conn );
				
				daoReserva.rf8(id);		

			}
			catch (SQLException sqlException) {
				System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
				sqlException.printStackTrace();
				throw sqlException;
			} 
			catch (Exception exception) {
				System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			} 
			finally {
				try {
					daoReserva.cerrarRecursos();
					if(this.conn!=null){
						this.conn.close();					
					}
				}
				catch (SQLException exception) {
					System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
					exception.printStackTrace();
					throw exception;
				}
			}	
		}
		
		public void RF9(Long id) throws SQLException, Exception {
			DAOReserva daoReserva = new DAOReserva( );
			try
			{
				this.conn = darConexion();
				daoReserva.setConn( conn );
				
				daoReserva.rf9(id);		

			}
			catch (SQLException sqlException) {
				System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
				sqlException.printStackTrace();
				throw sqlException;
			} 
			catch (Exception exception) {
				System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			} 
			finally {
				try {
					daoReserva.cerrarRecursos();
					if(this.conn!=null){
						this.conn.close();					
					}
				}
				catch (SQLException exception) {
					System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
					exception.printStackTrace();
					throw exception;
				}
			}	
		}
		
		public void RF10(Long id)throws SQLException, Exception {
			DAOOperador daoOperador = new DAOOperador( );
			try
			{
				this.conn = darConexion();
				daoOperador.setConn( conn );
				
				daoOperador.RF10(id);		

			}
			catch (SQLException sqlException) {
				System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
				sqlException.printStackTrace();
				throw sqlException;
			} 
			catch (Exception exception) {
				System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			} 
			finally {
				try {
					daoOperador.cerrarRecursos();
					if(this.conn!=null){
						this.conn.close();					
					}
				}
				catch (SQLException exception) {
					System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
					exception.printStackTrace();
					throw exception;
				}
			}	
		}
		
		public void cancelarReserva(Long id) throws SQLException, Exception {
			DAOReserva daoReserva = new DAOReserva( );
			try
			{
				this.conn = darConexion();
				daoReserva.setConn( conn );
				
				daoReserva.cancelarReserva(id);		

			}
			catch (SQLException sqlException) {
				System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
				sqlException.printStackTrace();
				throw sqlException;
			} 
			catch (Exception exception) {
				System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			} 
			finally {
				try {
					daoReserva.cerrarRecursos();
					if(this.conn!=null){
						this.conn.close();					
					}
				}
				catch (SQLException exception) {
					System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
					exception.printStackTrace();
					throw exception;
				}
			}	
		}
		
		/**
		 * Metodo que modela la transaccion que elimina de la base de datos al reserva que entra por parametro. <br/>
		 * Solamente se actualiza si existe el reserva en la Base de Datos <br/>
		 * <b> post: </b> se ha eliminado el reserva que entra por parametro <br/>
		 * @param Reserva - reserva a eliminar. reserva != null
		 * @throws Exception - Cualquier error que se genere eliminando al reserva.
		 */
		public void deleteReserva(Reserva reserva) throws Exception 
		{
			DAOReserva daoReserva = new DAOReserva( );
			try
			{
				this.conn = darConexion();
				daoReserva.setConn( conn );
				if(daoReserva.findReservaById(reserva.getIdReserva()) == null)
				{
					throw new Exception("El reserva que esta intentando eliminar no existe");
				}
				daoReserva.deleteReserva(reserva);

			}
			catch (SQLException sqlException) {
				System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
				sqlException.printStackTrace();
				throw sqlException;
			} 
			catch (Exception exception) {
				System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			} 
			finally {
				try {
					daoReserva.cerrarRecursos();
					if(this.conn!=null){
						this.conn.close();					
					}
				}
				catch (SQLException exception) {
					System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
					exception.printStackTrace();
					throw exception;
				}
			}	
		}
		
		
		
		
		//---------------------------------------------------------------------//
		//-----------------------METODOS HABITACION----------------------------//
		//---------------------------------------------------------------------//
		
		/**
		 * Metodo que modela la transaccion que retorna todos los habitacion de la base de datos. <br/>
		 * @return List<Habitacion> - Lista de habitaciones que contiene el resultado de la consulta.
		 * @throws Exception -  Cualquier error que se genere durante la transaccion
		 */
		public List<Habitacion> getAllHabitaciones() throws Exception {
			DAOHabitacion daoHabitacion = new DAOHabitacion();
			List<Habitacion> habitacions;
			try 
			{
				this.conn = darConexion();
				daoHabitacion.setConn(conn);				
				habitacions = daoHabitacion.getHabitaciones();
			}
			catch (SQLException sqlException) {
				System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
				sqlException.printStackTrace();
				throw sqlException;
			} 
			catch (Exception exception) {
				System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			} 
			finally {
				try {
					daoHabitacion.cerrarRecursos();
					if(this.conn!=null){
						this.conn.close();					
					}
				}
				catch (SQLException exception) {
					System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
					exception.printStackTrace();
					throw exception;
				}
			}
			return habitacions;
		}
		
		/**
		 * Metodo que modela la transaccion que retorna la habitacion por ID de la base de datos. <br/>
		 * @return List<Habitacion> - Lista de habitaciones que contiene el resultado de la consulta.
		 * @throws Exception -  Cualquier error que se genere durante la transaccion
		 */
		public Habitacion getAllHabitacionesById(Long id) throws Exception {
			DAOHabitacion daoHabitacion = new DAOHabitacion();
			Habitacion habitacion = null;
			try 
			{
				this.conn = darConexion();
				daoHabitacion.setConn(conn);				
				habitacion = daoHabitacion.findHabitacionById(id);
			}
			catch (SQLException sqlException) {
				System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
				sqlException.printStackTrace();
				throw sqlException;
			} 
			catch (Exception exception) {
				System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			} 
			finally {
				try {
					daoHabitacion.cerrarRecursos();
					if(this.conn!=null){
						this.conn.close();					
					}
				}
				catch (SQLException exception) {
					System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
					exception.printStackTrace();
					throw exception;
				}
			}
			return habitacion;
		}
		
		/**
		 * Metodo que modela la transaccion que retorna todos los habitacion disponibles de la base de datos. <br/>
		 * @return List<Habitacion> - Lista de habitaciones que contiene el resultado de la consulta.
		 * @throws Exception -  Cualquier error que se genere durante la transaccion
		 */
		public List<Habitacion> getAllHabitacionesDisponibles() throws Exception {
			DAOHabitacion daoHabitacion = new DAOHabitacion();
			List<Habitacion> habitacions;
			try 
			{
				this.conn = darConexion();
				daoHabitacion.setConn(conn);
				habitacions = daoHabitacion.getHabitacionesDisponibles();
			}
			catch (SQLException sqlException) {
				System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
				sqlException.printStackTrace();
				throw sqlException;
			} 
			catch (Exception exception) {
				System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			} 
			finally {
				try {
					daoHabitacion.cerrarRecursos();
					if(this.conn!=null){
						this.conn.close();					
					}
				}
				catch (SQLException exception) {
					System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
					exception.printStackTrace();
					throw exception;
				}
			}
			return habitacions;
		}
		
		/**
		 * Metodo que modela la transaccion que retorna todos los habitacion disponibles del operador
		 * especificado en parametro de la base de datos. <br/>
		 * @return List<Habitacion> - Lista de habitaciones que contiene el resultado de la consulta.
		 * @throws Exception -  Cualquier error que se genere durante la transaccion
		 */
		public List<Habitacion> getAllHabitacionesDisponiblesOperador(Long opID) throws Exception {
			DAOHabitacion daoHabitacion = new DAOHabitacion();
			List<Habitacion> habitacions;
			try 
			{
				this.conn = darConexion();
				daoHabitacion.setConn(conn);
				habitacions = daoHabitacion.getHabitacionesDisponiblesOperador(opID);
			}
			catch (SQLException sqlException) {
				System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
				sqlException.printStackTrace();
				throw sqlException;
			} 
			catch (Exception exception) {
				System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			} 
			finally {
				try {
					daoHabitacion.cerrarRecursos();
					if(this.conn!=null){
						this.conn.close();					
					}
				}
				catch (SQLException exception) {
					System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
					exception.printStackTrace();
					throw exception;
				}
			}
			return habitacions;
		}
		
		/**
		 * Metodo que modela la transaccion que agrega un habitacion a la base de datos. <br/>
		 * <b> post: </b> se ha agregado el habitacion que entra como parametro <br/>
		 * @param habitacion - el habitacion a agregar. habitacion != null
		 * @throws Exception - Cualquier error que se genere agregando el habitacion
		 */
		public void addHabitacion(Habitacion habitacion) throws Exception 
		{
			
			DAOHabitacion daoHabitacion = new DAOHabitacion( );
			try
			{
				this.conn = darConexion();
				daoHabitacion.setConn(conn);
				daoHabitacion.addHabitacion(habitacion);

			}
			catch (SQLException sqlException) {
				System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
				sqlException.printStackTrace();
				throw sqlException;
			} 
			catch (Exception exception) {
				System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			} 
			finally {
				try {
					daoHabitacion.cerrarRecursos();
					if(this.conn!=null){
						this.conn.close();					
					}
				}
				catch (SQLException exception) {
					System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
					exception.printStackTrace();
					throw exception;
				}
			}
		}
		
		/**
		 * Metodo que modela la transaccion que actualiza en la base de datos al habitacion que entra por parametro.<br/>
		 * Solamente se actualiza si existe el habitacion en la Base de Datos <br/>
		 * <b> post: </b> se ha actualizado el habitacion que entra como parametro <br/>
		 * @param habitacion - Habitacion a actualizar. habitacion != null
		 * @throws Exception - Cualquier error que se genere actualizando al habitacion.
		 */
		public void updateHabitacion(Habitacion habitacion) throws Exception 
		{
			DAOHabitacion daoHabitacion = new DAOHabitacion( );
			try
			{
				this.conn = darConexion();
				daoHabitacion.setConn( conn );
				if(daoHabitacion.findHabitacionById(habitacion.getIdHabitacion()) == null)
				{
					throw new Exception("El habitacion al que esta intentando modificar no existe");
				}
				daoHabitacion.updateHabitacion(habitacion);		

			}
			catch (SQLException sqlException) {
				System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
				sqlException.printStackTrace();
				throw sqlException;
			} 
			catch (Exception exception) {
				System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			} 
			finally {
				try {
					daoHabitacion.cerrarRecursos();
					if(this.conn!=null){
						this.conn.close();					
					}
				}
				catch (SQLException exception) {
					System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
					exception.printStackTrace();
					throw exception;
				}
			}	
		}
		
		/**
		 * Metodo que modela la transaccion que elimina de la base de datos al habitacion que entra por parametro. <br/>
		 * Solamente se actualiza si existe el habitacion en la Base de Datos <br/>
		 * <b> post: </b> se ha eliminado el habitacion que entra por parametro <br/>
		 * @param Habitacion - habitacion a eliminar. habitacion != null
		 * @throws Exception - Cualquier error que se genere eliminando al habitacion.
		 */
		public void deleteHabitacion(Habitacion habitacion) throws Exception 
		{
			DAOHabitacion daoHabitacion = new DAOHabitacion( );
			try
			{
				this.conn = darConexion();
				daoHabitacion.setConn( conn );
				if(daoHabitacion.findHabitacionById(habitacion.getIdHabitacion()) == null)
				{
					throw new Exception("El habitacion que esta intentando eliminar no existe");
				}
				daoHabitacion.deleteHabitacion(habitacion);

			}
			catch (SQLException sqlException) {
				System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
				sqlException.printStackTrace();
				throw sqlException;
			} 
			catch (Exception exception) {
				System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			} 
			finally {
				try {
					daoHabitacion.cerrarRecursos();
					if(this.conn!=null){
						this.conn.close();					
					}
				}
				catch (SQLException exception) {
					System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
					exception.printStackTrace();
					throw exception;
				}
			}	
		}
		

		//---------------------------------------------------------------------//
		//-----------------------METODOS OPERADOR------------------------------//
		//---------------------------------------------------------------------//
		
		/**
		 * Metodo que modela la transaccion que retorna todos los operadores de la base de datos. <br/>
		 * @return List<Operador> - Lista de operadores que contiene el resultado de la consulta.
		 * @throws Exception -  Cualquier error que se genere durante la transaccion
		 */
		public List<Operador> getAllOperadores() throws Exception {
			DAOOperador daoOperador = new DAOOperador();
			List<Operador> operadors;
			try 
			{
				this.conn = darConexion();
				daoOperador.setConn(conn);				
				operadors = daoOperador.getAll();
			}
			catch (SQLException sqlException) {
				System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
				sqlException.printStackTrace();
				throw sqlException;
			} 
			catch (Exception exception) {
				System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			} 
			finally {
				try {
					daoOperador.cerrarRecursos();
					if(this.conn!=null){
						this.conn.close();					
					}
				}
				catch (SQLException exception) {
					System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
					exception.printStackTrace();
					throw exception;
				}
			}
			return operadors;
		}
		
		/**
		 * Metodo que modela la transaccion que retorna todos los operador disponibles del operador
		 * especificado en parametro de la base de datos. <br/>
		 * @return List<Operador> - Lista de operadores que contiene el resultado de la consulta.
		 * @throws Exception -  Cualquier error que se genere durante la transaccion
		 */
		public List<Object> getOperadorByTipo(String tipo) throws Exception {
			DAOOperador daoOperador = new DAOOperador();
			List<Object> operadors;
			try 
			{
				this.conn = darConexion();
				daoOperador.setConn(conn);
				operadors = daoOperador.getOperadoresByTypo(tipo);
			}
			catch (SQLException sqlException) {
				System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
				sqlException.printStackTrace();
				throw sqlException;
			} 
			catch (Exception exception) {
				System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			} 
			finally {
				try {
					daoOperador.cerrarRecursos();
					if(this.conn!=null){
						this.conn.close();					
					}
				}
				catch (SQLException exception) {
					System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
					exception.printStackTrace();
					throw exception;
				}
			}
			return operadors;
		}
		
		
		
		/**
		 * Metodo que modela la transaccion que retorna todos los operador disponibles del operador
		 * especificado en parametro de la base de datos. <br/>
		 * @return List<Operador> - Lista de operadores que contiene el resultado de la consulta.
		 * @throws Exception -  Cualquier error que se genere durante la transaccion
		 */
		public Pair findOperadorById(Long id) throws Exception {
			System.out.println("llego");
			DAOOperador daoOperador = new DAOOperador();
			Pair operador;
			try 
			{
				this.conn = darConexion();
				daoOperador.setConn(conn);
				operador = daoOperador.findOperadorById(id);
			}
			catch (SQLException sqlException) {
				System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
				sqlException.printStackTrace();
				throw sqlException;
			} 
			catch (Exception exception) {
				System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			} 
			finally {
				try {
					daoOperador.cerrarRecursos();
					if(this.conn!=null){
						this.conn.close();					
					}
				}
				catch (SQLException exception) {
					System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
					exception.printStackTrace();
					throw exception;
				}
			}
			return operador;
		}
		
		
		/**
		 * Metodo que modela la transaccion que agrega un operador a la base de datos. <br/>
		 * <b> post: </b> se ha agregado el operador que entra como parametro <br/>
		 * @param operador - el operador a agregar. operador != null
		 * @throws Exception - Cualquier error que se genere agregando el operador
		 */
		public void addOperador(Operador operador) throws Exception 
		{
			
			DAOOperador daoOperador = new DAOOperador( );
			try
			{
				this.conn = darConexion();
				daoOperador.setConn(conn);
				daoOperador.addOperador(operador);

			}
			catch (SQLException sqlException) {
				System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
				sqlException.printStackTrace();
				throw sqlException;
			} 
			catch (Exception exception) {
				System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
				exception.printStackTrace();
				
				throw exception;
			} 
			finally {
				try {
					daoOperador.cerrarRecursos();
					if(this.conn!=null){
						this.conn.close();					
					}
				}
				catch (SQLException exception) {
					System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
					exception.printStackTrace();
					throw exception;
				}
			}
		}
		
		/**
		 * Metodo que modela la transaccion que agrega un operador a la base de datos. <br/>
		 * <b> post: </b> se ha agregado el operador que entra como parametro <br/>
		 * @param operador - el operador a agregar. operador != null
		 * @throws Exception - Cualquier error que se genere agregando el operador
		 */
		public void addOperador(Object operador, String tipo) throws Exception 
		{
			
			DAOOperador daoOperador = new DAOOperador( );
			try
			{
				this.conn = darConexion();
				daoOperador.setConn(conn);
				daoOperador.addOperador(operador, tipo);

			}
			catch (SQLException sqlException) {
				System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
				sqlException.printStackTrace();
				throw sqlException;
			} 
			catch (Exception exception) {
				System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
				exception.printStackTrace();
				
				throw exception;
			} 
			finally {
				try {
					daoOperador.cerrarRecursos();
					if(this.conn!=null){
						this.conn.close();					
					}
				}
				catch (SQLException exception) {
					System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
					exception.printStackTrace();
					throw exception;
				}
			}
		}
		
		/**
		 * Metodo que modela la transaccion que actualiza en la base de datos al operador que entra por parametro.<br/>
		 * Solamente se actualiza si existe el operador en la Base de Datos <br/>
		 * <b> post: </b> se ha actualizado el operador que entra como parametro <br/>
		 * @param operador - Operador a actualizar. operador != null
		 * @throws Exception - Cualquier error que se genere actualizando al operador.
		 */
		public void updateOperador(Operador operador, String tipo) throws Exception 
		{
			DAOOperador daoOperador = new DAOOperador( );
			try
			{
				this.conn = darConexion();
				daoOperador.setConn( conn );
				if(daoOperador.findOperadorById(operador.getIdOperador()) == null)
				{
					throw new Exception("El operador al que esta intentando modificar no existe");
				}
				daoOperador.updateOperador(operador, tipo);		

			}
			catch (SQLException sqlException) {
				System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
				sqlException.printStackTrace();
				throw sqlException;
			} 
			catch (Exception exception) {
				System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			} 
			finally {
				try {
					daoOperador.cerrarRecursos();
					if(this.conn!=null){
						this.conn.close();					
					}
				}
				catch (SQLException exception) {
					System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
					exception.printStackTrace();
					throw exception;
				}
			}	
		}
		
		/**
		 * Metodo que modela la transaccion que actualiza en la base de datos al operador que entra por parametro.<br/>
		 * Solamente se actualiza si existe el operador en la Base de Datos <br/>
		 * <b> post: </b> se ha actualizado el operador que entra como parametro <br/>
		 * @param operador - Operador a actualizar. operador != null
		 * @throws Exception - Cualquier error que se genere actualizando al operador.
		 */
		public void updateOperador(Object operador, String tipo) throws Exception 
		{
			DAOOperador daoOperador = new DAOOperador( );
			try
			{
				this.conn = darConexion();
				daoOperador.setConn( conn );
				daoOperador.updateOperador(operador, tipo);		

			}
			catch (SQLException sqlException) {
				System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
				sqlException.printStackTrace();
				throw sqlException;
			} 
			catch (Exception exception) {
				System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			} 
			finally {
				try {
					daoOperador.cerrarRecursos();
					if(this.conn!=null){
						this.conn.close();					
					}
				}
				catch (SQLException exception) {
					System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
					exception.printStackTrace();
					throw exception;
				}
			}	
		}
		
		/**
		 * Metodo que modela la transaccion que elimina de la base de datos al operador que entra por parametro. <br/>
		 * Solamente se actualiza si existe el operador en la Base de Datos <br/>
		 * <b> post: </b> se ha eliminado el operador que entra por parametro <br/>
		 * @param Operador - operador a eliminar. operador != null
		 * @throws Exception - Cualquier error que se genere eliminando al operador.
		 */
		public void deleteOperador(Pair operador) throws Exception 
		{
			DAOOperador daoOperador = new DAOOperador( );
			try
			{
				this.conn = darConexion();
				daoOperador.setConn( conn );
				
				daoOperador.deleteOperador(operador);

			}
			catch (SQLException sqlException) {
				System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
				sqlException.printStackTrace();
				throw sqlException;
			} 
			catch (Exception exception) {
				System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			} 
			finally {
				try {
					daoOperador.cerrarRecursos();
					if(this.conn!=null){
						this.conn.close();					
					}
				}
				catch (SQLException exception) {
					System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
					exception.printStackTrace();
					throw exception;
				}
			}	
		}
		
		
		
		

}
