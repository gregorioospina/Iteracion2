package dao;

import java.sql.Connection;
import vos.*;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import org.codehaus.jackson.annotate.JsonProperty;
import EstructurasAuxiliares.Pair;

public class DAOReservaColectiva 
{

	/////////////////////////
	//////// CONSTANTES///////
	/////////////////////////

	/**
	 * Constante para indicar el usuario Oracle
	 */
	public final static String USUARIO = "ISIS2304A311810";

	/////////////////////////
	//////// ATRIBUTOS////////
	/////////////////////////

	/**
	 * Arraylits de recursos que se usan para la ejecucion de sentencias SQL
	 */
	private ArrayList<Object> recursos;

	/**
	 * Atributo que genera la conexion a la base de datos
	 */
	private Connection conn;

	///////////////////////////////////
	//// METODOS DE INICIALIZACION/////
	///////////////////////////////////

	/**
	 * Metodo constructor de la clase DAOReservaColectiva
	 */
	public DAOReservaColectiva(){
		recursos = new ArrayList<>();
	}
	////////////////////////////////////////
	///// METODOS DE CONEXION CON LA BD/////
	////////////////////////////////////////

	
	public ArrayList<ReservaColectiva> getAllReservaC() throws SQLException
	{
		ArrayList<ReservaColectiva> reservas = new ArrayList<>();
		String sq1 = String.format("SELECT * FROM %s.RESERVAS_COLECTIVAS", USUARIO);

		PreparedStatement prepStmt = conn.prepareStatement(sq1);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			reservas.add(convertResultToReservaColectiva(rs));
		}
		return reservas;
	}
	
	public ArrayList<ReservaColectiva> getByIdColectiva(Long id) throws SQLException
	{
		ArrayList<ReservaColectiva> reserva = new ArrayList<>();
		String sq1 = String.format("SELECT * FROM %1$s.RESERVAS_COLECTIVAS WHERE ID_COLECTIVA = %2$d", USUARIO, id);

		PreparedStatement prepstmt = conn.prepareStatement(sq1);
		recursos.add(prepstmt);
		ResultSet rs = prepstmt.executeQuery();

		while (rs.next()) {
			reserva.add(convertResultToReservaColectiva(rs));
		}

		return reserva;
	}
	
	public ReservaColectiva getByIdReserva(Long id) throws SQLException
	{
		ReservaColectiva reserva = null;
		String sq1 = String.format("SELECT * FROM %1$s.RESERVAS_COLECTIVAS WHERE ID_RESERVA = %2$d", USUARIO, id);
		
		PreparedStatement prepstmt = conn.prepareStatement(sq1);
		recursos.add(prepstmt);
		ResultSet rs = prepstmt.executeQuery();
		
		if(rs != null)
		{
			reserva = convertResultToReservaColectiva(rs);
		}
		
		return reserva;
	}
	
	public void addReservaColectiva(ReservaColectiva res) throws SQLException
	{
		String sql = String.format("insert into %1$s.RESERVAS_COLECTIVAS values (%2$d,%3$d);", USUARIO, res.getIdreserva(), res.getIdcolectiva() );
		
		System.out.println(sql);
		
		PreparedStatement prpstmt = conn.prepareStatement(sql);
		recursos.add(prpstmt);
		prpstmt.executeQuery();		
	}
	
	public void updateReservaColeciva(ReservaColectiva res) throws SQLException
	{
		String sql = String.format("update %1$s.RESERVAS_COLECTIVAS SET ID_RESERVA = %2$d, ID_COLECTIVA = %3$d where ID_RESERVA = %2$d; ", USUARIO, res.getIdcolectiva());
		
		System.out.println(sql);
		
		PreparedStatement prepStmt = conn.prepareStatement(sql.toString());
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}
	
	public void deleteReservaFromColectiva(Reserva reserva) throws SQLException, Exception {
		String sq1 = String.format("DELETE FROM %1$s.RESERVAS_COLECTIVAS WHERE ID_RESERVA = %2$d", USUARIO,
				reserva.getIdReserva());
		System.out.println(sq1);

		PreparedStatement prepStmt = conn.prepareStatement(sq1);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}
	
	
	////////////////////////////////
	////// METODOS AUXILIARES////////
	////////////////////////////////

	
	public ReservaColectiva convertResultToReservaColectiva(ResultSet rs) throws SQLException
	{
		Long idre = rs.getLong("ID_RESERVA");
		Long idc = rs.getLong("ID_COLECTIVA");
		
		return new ReservaColectiva(idre, idc);
	}
	
	public void setConn(Connection connection) {
		this.conn = connection;
	}

	/**
	 * Metodo que cierra todos los recursos que se encuentran en el arreglo de
	 * recursos<br/>
	 * <b>Postcondicion: </b> Todos los recurso del arreglo de recursos han sido
	 * cerrados.
	 */
	public void cerrarRecursos() {
		for (Object ob : recursos) {
			if (ob instanceof PreparedStatement)
				try {
					((PreparedStatement) ob).close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
