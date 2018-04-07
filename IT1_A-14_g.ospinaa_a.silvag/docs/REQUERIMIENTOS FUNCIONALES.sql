-- REQUERIMIENTOS FUNCIONALES

-- RFC1
-- Con la manera como poblamos las tablas, es necesario cambiar la instancia donde dice "CURRENT_DATE - 365" a por lo menos "CURRENT_DATE - 800" ya que 
-- los datos que le insertamos a las tablas son de años menores a un año desde el current time.

SELECT op.NOMBRE, re.ID_OPERADOR ID_OPERADOR, SUM(PRECIO) as GANANCIA_ANUAL
FROM ISIS2304A311810.RESERVAS re , ISIS2304A311810.OPERADORES op
WHERE re.FECHA_INICIAL > CURRENT_DATE - 365
AND re.ID_OPERADOR = op.ID_OPERADOR
GROUP BY re.ID_OPERADOR, op.NOMBRE
ORDER BY GANANCIA_ANUAL DESC;



-- RFC2
-- Nosotros consideramos que la oferta mas popular es aquella habitacion de un mismo 
--operador que tenga la mayor cantidad de apariciones en las reservas. 
--La condicion de que solo sean 20 es restringida en el DAO donde solo se itera sobre los primeros 20 Resultsets.


SELECT DISTINCT ID_OPERADOR, ID_HABITACION
FROM ISIS2304A311810.RESERVAS
GROUP BY ID_OPERADOR, ID_HABITACION
ORDER BY COUNT(ID_OPERADOR) DESC;