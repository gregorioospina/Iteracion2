package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import vos.*;

public class DAOReserva {

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
	
	private ArrayList<Object> recursos2;

	/**
	 * Atributo que genera la conexion a la base de datos
	 */
	private Connection conn;

	///////////////////////////////////
	//// METODOS DE INICIALIZACION/////
	///////////////////////////////////

	/**
	 * Metodo constructor de la clase DAOReserva
	 */
	public DAOReserva() {
		recursos = new ArrayList<Object>();
		recursos2 = new ArrayList<Object>();
	}

	////////////////////////////////////////
	///// METODOS DE CONEXION CON LA BD/////
	////////////////////////////////////////

	/**
	 * Metodo que obtiene la informacion de todos los reservas en la Base de Datos
	 * <br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>
	 * 
	 * @return lista con la informacion de todos los reservas que se encuentran en
	 *         la Base de Datos
	 * @throws SQLException
	 *             Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception
	 *             Si se genera un error dentro del metodo.
	 */
	public ArrayList<Reserva> getReserva() throws SQLException, Exception {
		System.out.println("getReservadao");
		ArrayList<Reserva> reservas = new ArrayList<>();
		String sq1 = String.format("SELECT * FROM %s.RESERVAS", USUARIO);
		System.out.println(sq1);
		PreparedStatement prepStmt = conn.prepareStatement(sq1);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			reservas.add(convertResultToReserva(rs));
		}
		return reservas;

	}

	/**
	 * Metodo que obtiene la informacion del reserva en la Base de Datos que tiene
	 * el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>
	 * 
	 * @param id
	 *            el identificador del reserva
	 * @return la informacion del reserva que cumple con los criterios de la
	 *         sentecia SQL Null si no existe el reserva conlos criterios
	 *         establecidos
	 * @throws SQLException
	 *             SQLException Genera excepcion si hay error en la conexion o en la
	 *             consulta SQL
	 * @throws Exception
	 *             Si se genera un error dentro del metodo.
	 */
	public Reserva findReservaById(Long id) throws SQLException, Exception {
		Reserva reserva = null;
		String sq1 = String.format("SELECT * FROM %1$s.RESERVAS WHERE ID_RESERVA = %2$d", USUARIO, id);

		PreparedStatement prepstmt = conn.prepareStatement(sq1);
		recursos.add(prepstmt);
		ResultSet rs = prepstmt.executeQuery();

		while (rs.next()) {
			reserva = convertResultToReserva(rs);
		}

		return reserva;
	}

	/**
	 * Metodo que encuentra las reservas por un usuario y un operador.
	 * 
	 * @throws SQLException
	 */
	public ArrayList<Reserva> findReservaByUsuarioOperador(Long codigo, Long opID) throws SQLException, Exception {
		ArrayList<Reserva> respu = new ArrayList<>();

		String sq1 = String.format("SELECT * FROM %1$s.RESERVAS WHERE CODIGOUNIANDINO = %2$d AND ID_OPERADOR = %3$d",
				USUARIO, codigo, opID);

		PreparedStatement prepstmt = conn.prepareStatement(sq1);
		recursos.add(prepstmt);
		ResultSet rs = prepstmt.executeQuery();

		while (rs.next()) {
			respu.add(convertResultToReserva(rs));
		}

		return respu;
	}

	/**
	 * Metodo que devuelve los operadores con sus nombres y la ganancia anual.
	 * 
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	public ArrayList<RFC1> RFC1() throws SQLException, Exception {
		ArrayList<RFC1> respu = new ArrayList<>();

		StringBuilder sq1 = new StringBuilder();
		sq1.append("SELECT op.NOMBRE, re.ID_OPERADOR ID_OPERADOR, SUM(PRECIO) as GANANCIA_ANUAL");
		sq1.append(String.format(" FROM %1$s.RESERVAS re , %1$s.OPERADORES op", USUARIO));
		sq1.append(" WHERE re.FECHA_INICIAL > CURRENT_DATE - 1500");
		sq1.append(" AND re.ID_OPERADOR = op.ID_OPERADOR");
		sq1.append(" GROUP BY re.ID_OPERADOR, op.NOMBRE");
		sq1.append(" ORDER BY GANANCIA_ANUAL DESC");

		System.out.println(sq1);

		PreparedStatement prepstmt = conn.prepareStatement(sq1.toString());
		recursos.add(prepstmt);
		ResultSet rs = prepstmt.executeQuery();

		while (rs.next()) {
			respu.add(convertResultToRFC1(rs));
		}
		System.out.println(respu);
		return respu;
	}
	
	

	/**
	 * MEtodo que encuentra las 20 ofertas mas populares basado en el historial de
	 * reservas pasadas.
	 * 
	 * @return Una lista de Strings que describen el operador y la habitacion mas
	 *         solicitada.
	 * @throws SQLException
	 * @throws Exception
	 */
	public ArrayList<RFC2> RFC2() throws SQLException, Exception {
		ArrayList<RFC2> respu = new ArrayList<>();

		StringBuilder sq1 = new StringBuilder();
		sq1.append("SELECT DISTINCT ID_OPERADOR, ID_HABITACION");
		sq1.append(String.format(" FROM %s.RESERVAS", USUARIO));
		sq1.append(" GROUP BY ID_OPERADOR, ID_HABITACION");
		sq1.append(" ORDER BY COUNT(ID_OPERADOR) DESC");

		System.out.println(sq1);

		PreparedStatement prepstmt = conn.prepareStatement(sq1.toString());
		recursos.add(prepstmt);
		ResultSet rs = prepstmt.executeQuery();

		int i = 0;
		while (rs.next() && i <= 20) {
			respu.add(convertResultToRFC2(rs));
			i++;
		}

		return respu;
	}

	public ArrayList<RFC5> RFC5() throws SQLException
	{
		ArrayList<RFC5> respu = new ArrayList<>();
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT us.tipo AS tipo, SUM(re.precio) AS ganancia_total, SUM(fecha_final - fecha_inicial) AS dias_totales");
		sql.append(String.format(" FROM %1$s.reservas re RIGHT OUTER JOIN %1$s.usuarios us", USUARIO));
		sql.append(" ON re.codigouniandino = us.codigo");
		sql.append(" GROUP BY us.tipo");
		
		System.out.println(sql);

		PreparedStatement prepstmt = conn.prepareStatement(sql.toString());
		recursos.add(prepstmt);
		ResultSet rs = prepstmt.executeQuery();

		while(rs.next())
		{
			respu.add(ConvertResultToRFC5(rs));
		}
		
		return respu;
		
	}
	
	
	
	
	public ArrayList<RFC6> RFC6 (Long codigo) throws SQLException, Exception{
		ArrayList<RFC6> respu = new ArrayList<>();
	
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT re.CODIGOUNIANDINO as codigo, (re.FECHA_FINAL - re.FECHA_INICIAL) Dias_Alquilados, op.TIPO as Tipo_Operador, hab.TIPO as Tipo_Habitacion, re.PRECIO as Pagado   ");
		sql.append(" FROM RESERVAS re RIGHT JOIN OPERADORES op ");
		sql.append(" ON op.ID_OPERADOR = re.ID_OPERADOR RIGHT JOIN HABITACION hab ");		
		sql.append(" ON re.ID_OPERADOR = hab.ID_OPERADOR ");		
		sql.append(String.format(" WHERE re.CODIGOUNIANDINO = %d", codigo));
		
		System.out.println(sql);
		
		PreparedStatement prepstmt = conn.prepareStatement(sql.toString());
		recursos.add(prepstmt);
		ResultSet rs = prepstmt.executeQuery();
		
		while(rs.next())
		{
			respu.add(convertResultToRFC6(rs));
		}
		
		return respu;
		
	}
	

	
	
	
	
	public String RFC7(Long codigo) throws SQLException, Exception
	{
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT re.id_reserva as reserva, re.fecha_final as fecha, re.precio as precio");
		sql.append(String.format(" FROM %s.reservas re",USUARIO));
		sql.append(String.format(" WHERE id_operador = %d", codigo));
		sql.append(" ORDER BY re.FECHA_FINAL");
	
		
		System.out.println(sql);
		
		PreparedStatement prepstmt = conn.prepareStatement(sql.toString());
		recursos.add(prepstmt);
		ResultSet rs = prepstmt.executeQuery();	
		
		ArrayList<RFC7> respu = new ArrayList<>();
		while(rs.next())
		{
			respu.add(convertResultToRFC7(rs));
		}
		
		return megaRFC7(respu);
		
	}

	public ArrayList<RFC8> RFC8(Long operador) throws SQLException
	{
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT us.codigo, us.correo, us.nombre");
		sql.append(String.format(" FROM %1$s.usuarios us INNER JOIN %1$s.reservas re",USUARIO));
		sql.append(" ON us.codigo = re.codigouniandino");
		sql.append(" HAVING COUNT(us.codigo) > 3 OR SUM(fecha_final-fecha_inicial) > 15");
		sql.append(" GROUP BY us.codigo, us.correo, us.nombre");
		
		System.out.println(sql);
		
		PreparedStatement prepstmt = conn.prepareStatement(sql.toString());
		recursos.add(prepstmt);
		ResultSet rs = prepstmt.executeQuery();	
		
		ArrayList<RFC8> respu = new ArrayList<>();
		while(rs.next())
		{
			respu.add(convertResultToRFC8(rs));
		}
		
		return respu;
		
	}
	
	public List<RFC12_1> RFC12_1MAX() throws SQLException
	{
		StringBuilder sql = new StringBuilder();
		sql.append(" WITH consulta1 as (SELECT MAX(cuenta) maxim,  semana");
		sql.append(" from( SELECT count(ID_OPERADOR) cuenta, ID_OPERADOR, to_char(FECHA_INICIAL - 7/24, 'IW') semana");
		sql.append(String.format(" from %1$d.reservas", USUARIO));
		sql.append(" group by  to_char(FECHA_INICIAL - 7/24, 'IW'), ID_OPERADOR)");
		sql.append(" GROUP BY semana),");
		sql.append(" consulta2 as (");
		sql.append(" SELECT count(ID_OPERADOR) cuenta, ID_OPERADOR, to_char(FECHA_INICIAL - 7/24, 'IW') semana");
		sql.append(" from reservas");
		sql.append(" group by  to_char(FECHA_INICIAL - 7/24, 'IW'), ID_OPERADOR)");
		sql.append(" SELECT consulta1.semana, consulta2.id_operador , consulta1.maxim solicitado");
		sql.append(" FROM consulta1, consulta2");
		sql.append(" WHERE consulta1.maxim = consulta2.cuenta and");
		sql.append(" consulta1.semana=consulta2.semana");
		sql.append(" ORDER BY SEMANA desc;");
		
		System.out.println(sql);
		
		PreparedStatement prepstmt = conn.prepareStatement(sql.toString());
		recursos.add(prepstmt);
		ResultSet rs = prepstmt.executeQuery();	
		
		ArrayList<RFC12_1> respu = new ArrayList<>();
		while(rs.next())
		{
			respu.add(convertResultToRFC12_1(rs));
		}
		
		return respu;

	}
	
	public List<RFC12_1> RFC12_1MIN() throws SQLException
	{
		StringBuilder sql = new StringBuilder();
		sql.append(" WITH consulta1 as (SELECT MIN(cuenta) maxim,  semana");
		sql.append(" from( SELECT count(ID_OPERADOR) cuenta, ID_OPERADOR, to_char(FECHA_INICIAL - 7/24, 'IW') semana");
		sql.append(String.format(" from %1$d.reservas", USUARIO));
		sql.append(" group by  to_char(FECHA_INICIAL - 7/24, 'IW'), ID_OPERADOR)");
		sql.append(" GROUP BY semana),");
		sql.append(" consulta2 as (");
		sql.append(" SELECT count(ID_OPERADOR) cuenta, ID_OPERADOR, to_char(FECHA_INICIAL - 7/24, 'IW') semana");
		sql.append(" from reservas");
		sql.append(" group by  to_char(FECHA_INICIAL - 7/24, 'IW'), ID_OPERADOR)");
		sql.append(" SELECT consulta1.semana, consulta2.id_operador , consulta1.maxim solicitado");
		sql.append(" FROM consulta1, consulta2");
		sql.append(" WHERE consulta1.maxim = consulta2.cuenta and");
		sql.append(" consulta1.semana=consulta2.semana");
		sql.append(" ORDER BY SEMANA desc;");
		
		System.out.println(sql);
		
		PreparedStatement prepstmt = conn.prepareStatement(sql.toString());
		recursos.add(prepstmt);
		ResultSet rs = prepstmt.executeQuery();	
		
		ArrayList<RFC12_1> respu = new ArrayList<>();
		while(rs.next())
		{
			respu.add(convertResultToRFC12_1(rs));
		}
		
		return respu;

	}
	
	public List<RFC12_2> RFC12_2MAX() throws SQLException
	{
		StringBuilder sql = new StringBuilder();
		sql.append(" WITH consulta5 AS (SELECT semana, MAX(cuenta) maxo");
		sql.append(" FROM(SELECT to_char(fecha_inicial - 7/24, 'IW') semana, reservas.id_operador, (operadores.ocupacion - COUNT(reservas.id_operador)) cuenta");
		sql.append(String.format(" FROM %1$d.reservas, %1$d.operadores", USUARIO));
		sql.append(" WHERE reservas.id_operador = operadores.id_operador");
		sql.append(" GROUP BY reservas.id_operador, operadores.ocupacion, to_char(reservas.fecha_inicial - 7/24, 'IW')");
		sql.append(" ORDER BY semana DESC)");
		sql.append(" GROUP BY semana),");
		sql.append(" consulta6 AS (");
		sql.append(" SELECT to_char(fecha_inicial - 7/24, 'IW') semana, reservas.id_operador op, (operadores.ocupacion - COUNT(reservas.id_operador)) cuenta ");
		sql.append(String.format(" FROM %1$d.reservas, %1$d.operadores", USUARIO));
		sql.append(" WHERE reservas.id_operador = operadores.id_operador");
		sql.append(" GROUP BY reservas.id_operador, operadores.ocupacion, to_char(reservas.fecha_inicial - 7/24, 'IW')");
		sql.append(" ORDER BY semana DESC)");
		sql.append(" SELECT consulta5.semana, consulta5.maxo ocupacion, consulta6.op id_operador");
		sql.append(" FROM consulta5, consulta6");
		sql.append(" WHERE consulta5.maxo = consulta6.cuenta");
		sql.append(" AND consulta5.semana = consulta6.semana");
		sql.append(" ORDER BY semana DESC;");

		System.out.println(sql);
		
		PreparedStatement prepstmt = conn.prepareStatement(sql.toString());
		recursos.add(prepstmt);
		ResultSet rs = prepstmt.executeQuery();	
		
		ArrayList<RFC12_2> respu = new ArrayList<>();
		while(rs.next())
		{
			respu.add(convertResultToRFC12_2(rs));
		}
		
		return respu;

	}
	
	public String RFC12() throws SQLException
	{
		List<RFC12_1> maxSolicitados = RFC12_1MAX();
		List<RFC12_1> minSolicitados = RFC12_1MIN();
		List<RFC12_2> maxOcupacion 	 = RFC12_2MAX();
		List<RFC12_2> minOcupacion	 = RFC12_2MIN();
		
		StringBuilder respu = new StringBuilder();
		
		for (int i = 0 ; i<maxOcupacion.size(); i++)
		{
			RFC12_1 max = maxSolicitados.get(i);
			RFC12_1 min = minSolicitados.get(i);
			RFC12_2 minx = minOcupacion.get(i);
			RFC12_2 maxx = maxOcupacion.get(i);
			
			respu.append("Para la semana " + max.getSemana() + ":" );
			respu.append("El operador con mayor ocupacion fue: " + maxx.getId_operador());
			respu.append("El operador con menor ocupacion fue: " + minx.getId_operador());
			respu.append("El operador con mayor solicitudes fue: " + max.getId_operador());
			respu.append("El operador con menor solicitudes fue: " + min.getId_operador());
			respu.append("----------------------------------------------");


		}
		
		return respu.toString();
	}
	
	public List<RFC12_2> RFC12_2MIN() throws SQLException
	{
		StringBuilder sql = new StringBuilder();
		sql.append(" WITH consulta5 AS (SELECT semana, MIN(cuenta) maxo");
		sql.append(" FROM(SELECT to_char(fecha_inicial - 7/24, 'IW') semana, reservas.id_operador, (operadores.ocupacion - COUNT(reservas.id_operador)) cuenta");
		sql.append(String.format(" FROM %1$d.reservas, %1$d.operadores", USUARIO));
		sql.append(" WHERE reservas.id_operador = operadores.id_operador");
		sql.append(" GROUP BY reservas.id_operador, operadores.ocupacion, to_char(reservas.fecha_inicial - 7/24, 'IW')");
		sql.append(" ORDER BY semana DESC)");
		sql.append(" GROUP BY semana),");
		sql.append(" consulta6 AS (");
		sql.append(" SELECT to_char(fecha_inicial - 7/24, 'IW') semana, reservas.id_operador op, (operadores.ocupacion - COUNT(reservas.id_operador)) cuenta ");
		sql.append(String.format(" FROM %1$d.reservas, %1$d.operadores", USUARIO));
		sql.append(" WHERE reservas.id_operador = operadores.id_operador");
		sql.append(" GROUP BY reservas.id_operador, operadores.ocupacion, to_char(reservas.fecha_inicial - 7/24, 'IW')");
		sql.append(" ORDER BY semana DESC)");
		sql.append(" SELECT consulta5.semana, consulta5.maxo ocupacion, consulta6.op id_operador");
		sql.append(" FROM consulta5, consulta6");
		sql.append(" WHERE consulta5.maxo = consulta6.cuenta");
		sql.append(" AND consulta5.semana = consulta6.semana");
		sql.append(" ORDER BY semana DESC;");

		System.out.println(sql);
		
		PreparedStatement prepstmt = conn.prepareStatement(sql.toString());
		recursos.add(prepstmt);
		ResultSet rs = prepstmt.executeQuery();	
		
		ArrayList<RFC12_2> respu = new ArrayList<>();
		while(rs.next())
		{
			respu.add(convertResultToRFC12_2(rs));
		}
		
		return respu;

	}
	
	public String RFC13() throws SQLException
	{
		StringBuilder sql = new StringBuilder();
		sql.append(" WITH cs AS");
		sql.append(String.format(" SELECT reservas.codigouniandino as codigo, habitacion.tipo as hab", USUARIO));
		sql.append(String.format(" FROM %1$d.reservas FULL OUTER JOIN habitacion", USUARIO));
		sql.append(" ON habitacion.id_habitacion = reservas.id_habitacion");
		sql.append(" WHERE habitacion.tipo = 'suite' and reservas.codigouniandino is not null");
		sql.append(" MINUS");
		sql.append(" SELECT reservas.codigouniandino as codigo, habitacion.tipo");
		sql.append(String.format(" FROM %1$d.reservas FULL OUTER JOIN habitacion", USUARIO));
		sql.append(" ON habitacion.id_habitacion = reservas.id_habitacion");
		sql.append(" WHERE habitacion.tipo NOT LIKE 'suite' and reservas.codigouniandino is not null),");
		sql.append(" caros AS");
		sql.append(" (SELECT reservas.codigouniandino AS codigo, AVG(habitacion.precio) AS precio_promedio");
		sql.append(String.format(" FROM reservas FULL OUTER JOIN habitacion", USUARIO));
		sql.append(" ON habitacion.id_habitacion = reservas.id_habitacion");
		sql.append(" WHERE habitacion.precio > 450000 AND reservas.codigouniandino IS NOT NULL");
		sql.append(" GROUP BY reservas.codigouniandino");
		sql.append(" MINUS");
		sql.append(" SELECT reservas.codigouniandino AS codigo, AVG(habitacion.precio) AS precio_promedio");
		sql.append(String.format(" FROM %1$d.reservas FULL OUTER JOIN habitacion", USUARIO));
		sql.append(" ON habitacion.id_habitacion = reservas.id_habitacion");
		sql.append(" WHERE habitacion.precio < 450000 AND reservas.codigouniandino IS NOT NULL");
		sql.append(" GROUP BY reservas.codigouniandino),");
		sql.append(" mensual AS(SELECT reservas.codigouniandino AS codigo");
		sql.append(String.format(" FROM %1$d.reservas", USUARIO));
		sql.append(" GROUP BY substr(reservas.fecha_inicial, 4,3), reservas.codigouniandino");
		sql.append(" HAVING (COUNT(DISTINCT substr(reservas.fecha_inicial, 4,3))> COUNT(reservas.codigouniandino)))");
		sql.append(" SELECT DISTINCT reservas.codigouniandino codigouniandino, usuarios.nombre, cs.hab, caros.precio_promedio");
		sql.append(" FROM reservas INNER JOIN usuarios ON reservas.codigouniandino = usuarios.codigo LEFT JOIN caros ON reservas.codigouniandino = caros.codigo LEFT JOIN mensual ON reservas.codigouniandino = mensual.codigo LEFT JOIN cs ON reservas.codigouniandino = cs.codigo");
		System.out.println(sql);
		
		PreparedStatement prepstmt = conn.prepareStatement(sql.toString());
		recursos.add(prepstmt);
		ResultSet rs = prepstmt.executeQuery();	
		
		ArrayList<RFC13> respu = new ArrayList<>();
		while(rs.next())
		{
			respu.add(convertResultToRFC13(rs));
		}
		
		return megaRFC13(respu);
	}
	

	/**
	 * Metodo que agregar la informacion de una nueva reserva en la Base de Datos a
	 * partir del parametro ingresado<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>
	 * 
	 * @param Reserva
	 *            reserva que desea agregar a la Base de Datos
	 * @throws SQLException
	 *             SQLException Genera excepcion si hay error en la conexion o en la
	 *             consulta SQL
	 * @throws Exception
	 *             Si se genera un error dentro del metodo.
	 */
	public void addReserva(Reserva reserva) throws SQLException, Exception {
		char s1 = boolToInt(reserva.getCancelado());
		String sq1 = String.format(
				"INSERT INTO %1$s.RESERVAS (ID_RESERVA, CODIGOUNIANDINO, ID_OPERADOR, CANCELADO, PRECIO, ID_HABITACION, FECHA_INICIAL, FECHA_FINAL, HORA_CREACION) VALUES (%2$d, %3$d, %4$d, %5$c, %6$f, %7$d, TO_DATE('%8$tF', 'YYYY-MM-DD'), TO_DATE('%9$tF', 'YYYY-MM-DD'), CURRENT_TIMESTAMP)",
				USUARIO, reserva.getIdReserva(), reserva.getIdUsuario(), reserva.getIdOperador(), s1,
				reserva.getPrecio(), reserva.getIdHabitacion(), reserva.getFechaInicio(), reserva.getFechaFinal());

		System.out.println(sq1);

		PreparedStatement prpstmt = conn.prepareStatement(sq1);
		recursos.add(prpstmt);
		prpstmt.executeQuery();
	}

	/**
	 * Metodo que actualiza la informacion del usuario en la Base de Datos que tiene
	 * el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>
	 * 
	 * @param usuario
	 *            usuario que desea actualizar a la Base de Datos
	 * @throws SQLException
	 *             SQLException Genera excepcion si hay error en la conexion o en la
	 *             consulta SQL
	 * @throws Exception
	 *             Si se genera un error dentro del metodo.
	 */
	public void updateReservas(Reserva reserva) throws SQLException, Exception {

		char s1 = boolToInt(reserva.getCancelado());
		StringBuilder sq1 = new StringBuilder();
		sq1.append(String.format("UPDATE %s.RESERVAS SET ", USUARIO));
		sq1.append(String.format("ID_RESERVA = %1$d, CODIGOUNIANDINO = %2$d, ID_OPERADOR = %3$d, CANCELADO = '%4$c', PRECIO = %5$f, ID_HABITACION = %6$d, FECHA_INICIAL = TO_DATE('%6$tF', 'YYYY-MM-DD'), FECHA_FINAL = TO_DATE('%7$tF', 'YYYY-MM-DD')",
				reserva.getIdReserva(), reserva.getIdUsuario(), reserva.getIdOperador(), s1, reserva.getPrecio(),
				reserva.getIdHabitacion(), reserva.getFechaInicio(), reserva.getFechaFinal()));
		sq1.append(String.format("WHERE ID_RESERVA = %d ", reserva.getIdReserva()));

		System.out.println(sq1);
		
		
		
		PreparedStatement prepStmt = conn.prepareStatement(sq1.toString());
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}
	
	public void cancelarReserva(Long id) throws SQLException {

		String sql = String.format("UPDATE reservas SET precio = precio*.1,cancelado = '1' where id_reserva = %d", id);
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}
	
	public void rf8(Long id) throws SQLException {
		String sql = String.format("SELECT ID_RESERVA FROM RESERVAS_COLECTIVAS WHERE ID_COLECTIVA = %d", id);
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		while (rs.next()) {
			cancelarReserva(convertResultToRF8(rs));
		}
		sql = String.format("DELETE FROM RESERVAS_COLECTIVAS WHERE ID_COLECTIVA = %d", id);
		prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}
	
	public void rf9(Long id) throws Exception{
		String sql = String.format("UPDATE OPERADORES SET HABILITADO = '0' WHERE %d", id);
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		sql = String.format("SELECT *\r\n" + 
				"FROM Reservas \r\n" + 
				"where ID_OPERADOR = %d AND FECHA_INICIAL>CURRENT_DATE AND CANCELADO = '0'" , id);
		prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		while (rs.next()) {
			cancelarReserva(convertResultToRF8(rs));
			Reserva reservaTemp = convertResultToReserva(rs);
			reservaTemp.setIdReserva(reservaTemp.getIdReserva()+10000);
			addReserva(reservaTemp);
		}
		
		
	}

	
	/**
	 * Metodo que actualiza la informacion del reserva en la Base de Datos que tiene
	 * el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>
	 * 
	 * @param reserva
	 *            reserva que desea actualizar a la Base de Datos
	 * @throws SQLException
	 *             SQLException Genera excepcion si hay error en la conexion o en la
	 *             consulta SQL
	 * @throws Exception
	 *             Si se genera un error dentro del metodo.
	 */
	public void deleteReserva(Reserva reserva) throws SQLException, Exception {
		String sq1 = String.format("DELETE FROM %1$s.RESERVAS WHERE ID_RESERVA = %2$d", USUARIO,
				reserva.getIdReserva());
		System.out.println(sq1);

		PreparedStatement prepStmt = conn.prepareStatement(sq1);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}
	

	private Long aDia (Date date)
	{
		Long t = date.getTime();
		Long respu = t/(1000*60*60*24);
		
		return respu;
	}


	
	////////////////////////////////
	////// METODOS AUXILIARES////////
	////////////////////////////////

	private String megaRFC7(ArrayList<RFC7> rs) throws SQLException
	{
		Date fechaMenor = new Date();
		Date fechaMayor = new Date();
		Date fechaPrecio = new Date();
	
		System.out.println("bien");
		Date fechaBase = rs.get(0).getFecha();
		
		int mayor = 0;
		int menor = Integer.MAX_VALUE;
		Double precio = 0.0;
		
		System.out.println("bien2");
	
		for(int i = 0; i < rs.size(); i++)
		{
			int tiempos = 0;
			Double precioT = 0.0;
			
			while (aDia(fechaBase)-aDia(rs.get(i).getFecha()) < 31 && i<rs.size()-1)
			{
				tiempos++;
				precioT += rs.get(i).getPrecio();
				i++;
			}
			
			if(tiempos < menor)
			{
				menor = tiempos;
				fechaMenor = fechaBase;
			}
			if(tiempos>mayor)
			{
				mayor = tiempos;
				fechaMayor = fechaBase;
			}
			if(precioT > precio)
			{
				precio = precioT;
				fechaPrecio = fechaBase;
			}
			
			fechaBase = rs.get(i).getFecha();	
			
		}
		StringBuilder respu = new StringBuilder();
		respu.append("El mes que mas ganancias monetarias trajo empezo en " + fechaPrecio.toString() + "\n \n");
		respu.append("El mes en el que mas reservas hubo empezo en " + fechaMayor.toString() + "\n \n");
		respu.append("El mes en el que menos reservas hubo empezo en " + fechaMenor.toString() + "\n \n");
		
		return respu.toString();
				
	}

	public Reserva convertResultToReserva(ResultSet resultSet) throws SQLException {
		System.out.println("convertResultSetToReserva");
		Long idReserva = resultSet.getLong("ID_RESERVA");
		Long codigoUsuario = resultSet.getLong("CODIGOUNIANDINO");
		Long idHabitacion = resultSet.getLong("ID_HABITACION");
		Long idOperador = resultSet.getLong("ID_OPERADOR");
		System.out.println("ckpoint1");
		Double precio = resultSet.getDouble("PRECIO");
		Boolean cancelado = resultSet.getBoolean("CANCELADO");
		System.out.println("ckpoint2");
		Date fechaInicial = resultSet.getDate("FECHA_INICIAL");
		System.out.println("ckpointDate");
		Date fechaFinal = resultSet.getDate("FECHA_FINAL");
		Date horaCreacion = resultSet.getDate("HORA_CREACION");
		System.out.println("ckpointDate2");
		Reserva reserva = new Reserva(idReserva, codigoUsuario, idOperador, cancelado, precio, idHabitacion,
				fechaInicial, fechaFinal, horaCreacion);

		return reserva;
	}
	
	public RFC1 convertResultToRFC1(ResultSet resultSet) throws SQLException{
		String nombre = resultSet.getString("NOMBRE");
		Integer ganancia = resultSet.getInt("GANANCIA_ANUAL");
		Integer operador = resultSet.getInt("ID_OPERADOR");
		return new RFC1(nombre,operador,ganancia);
	}

	public RFC2 convertResultToRFC2(ResultSet resultSet) throws SQLException{
		Integer habitacion = resultSet.getInt("ID_HABITACION");
		Integer operador = resultSet.getInt("ID_OPERADOR");
		return new RFC2(operador, habitacion);
	}
	
	public RFC5 ConvertResultToRFC5(ResultSet rs) throws SQLException
	{
		String tipo = rs.getString("TIPO");
		Double ganancia = rs.getDouble("GANANCIA_TOTAL");
		Integer dias = rs.getInt("DIAS_TOTALES");
		
		return new RFC5(tipo, dias, ganancia);
	}

	public RFC6 convertResultToRFC6(ResultSet resultSet) throws SQLException
	{
		Long codigouniandes = resultSet.getLong("CODIGO");
		Integer diasalquilados = resultSet.getInt("DIAS_ALQUILADOS");
		String tipooperador = resultSet.getString("TIPO_OPERADOR");
		String tipohabitacion = resultSet.getString("TIPO_HABITACION");
		Double pagado = resultSet.getDouble("PAGADO");
	
		return new RFC6(codigouniandes, diasalquilados, tipooperador, tipohabitacion, pagado);
	
	}

	public RFC7 convertResultToRFC7(ResultSet rs) throws SQLException
	{
		Double precio = rs.getDouble("PRECIO");
		Date fecha = rs.getDate("FECHA");
		Long reserva = rs.getLong("RESERVA");
		
		return new RFC7(precio, fecha, reserva);
	}
	
	public RFC8 convertResultToRFC8(ResultSet rs) throws SQLException
	{
		Long codigo = rs.getLong("CODIGO");
		String correo = rs.getString("CORREO");
		String nombre = rs.getString("NOMBRE");
		
		return new RFC8(codigo,correo,nombre);
	}
	
	
	public Long convertResultToRF8(ResultSet resultSet)throws SQLException
	{
		return resultSet.getLong("ID_RESERVA");
	}
	
	public RFC12_1 convertResultToRFC12_1(ResultSet rs) throws SQLException
	{
		Integer semana = rs.getInt("SEMANA");
		Long id_operador = rs.getLong("ID_OPERADOR");
		Integer solicitado = rs.getInt("SOLICITADO");
		
		return new RFC12_1(semana, id_operador, solicitado);
	}
	
	public RFC12_2 convertResultToRFC12_2(ResultSet rs) throws SQLException
	{
		Integer semana = rs.getInt("SEMANA");
		Long id_operador = rs.getLong("ID_OPERADOR");
		Integer ocupacion = rs.getInt("OCUPACION");
		
		return new RFC12_2(semana, id_operador, ocupacion);
	}


	public vos.RFC13 convertResultToRFC13(ResultSet rs) throws SQLException
	{
		Long codigouniandino = rs.getLong("CODIGOUNIANDINO");
		String nombre = rs.getString("NOMBRE");
		String hab = rs.getString("HAB");
		Double precio_promedio = rs.getDouble("PRECIO_PROMEDIO");
		
		return new RFC13(codigouniandino, nombre, hab, precio_promedio);
	}
	
	public String megaRFC13(List<RFC13> lista)
	{
		int cont = 0;
		StringBuilder respu = new StringBuilder();
		for (int i=0; i<lista.size(); i++)
		{
			RFC13 x = lista.get(i);
			if(x.getHab() == null && x.getPrecio_promedio() == null && cont < 40 )
			{
				respu.append("El cliente con el nombre " + x.getNombre() + " y codigo " +  x.getCodigouniandino() + " es cliente de beneficio tipo multiples reservas ");
				cont++;
			}
			else if( x.getHab() == null && x.getPrecio_promedio() != null)
			{
				respu.append("El cliente con el nombre " + x.getNombre() + " y codigo " +  x.getCodigouniandino() + " es cliente de beneficio tipo precio exclusivo ");
			}
			else if(x.getHab() != null && x.getPrecio_promedio() == null)
			{
				respu.append("El cliente con el nombre " + x.getNombre() + " y codigo " +  x.getCodigouniandino() + " es cliente de beneficio tipo suite ");
			}
		}
		
		return respu.toString();
	}
	
	private char boolToInt(Boolean bol) {
		return bol ? '1' : '0';
	}

	/**
	 * Metodo encargado de inicializar la conexion del DAO a la Base de Datos a
	 * partir del parametro <br/>
	 * <b>Postcondicion: </b> el atributo conn es inicializado <br/>
	 * 
	 * @param connection
	 *            la conexion generada en el TransactionManager para la comunicacion
	 *            con la Base de Datos
	 */
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
		for (Object ob : recursos2) {
			if (ob instanceof PreparedStatement)
				try {
					((Statement) ob).close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
		}
	}

}
