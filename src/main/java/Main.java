
import java.sql.*;
import java.util.Scanner;

public class Main {


    private Connection connection = null;
    PreparedStatement stmt = null;
    String query;
    ResultSet rs;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Main dataBase = new Main();
        dataBase.getConnection();
        System.out.println("1. Query Jugadores Caveliers (Procedencia-Posición)" +
                "\n2. Query Numero Jugadores Españoles" +
                "\n3. Añadir Jugador Luka Donĉić" +
                "\n4. Query Jugadores Media > 10");
        int option = sc.nextInt();
        dataBase.getQuerySelect(option);
    }

    private Connection getConnection(){
        try {
            connection = DriverManager.getConnection("jdbc:mysql://192.168.22.157:3306/NBA", "angel", "Mugiw@ra27");
            System.out.println("BBDD CORRECTO");
        } catch (SQLException e) {
            System.out.println(e);
        }
        return connection;
    }

    private void getQuerySelect(int option) {
        try {
            switch (option) {
                case 1:
                    query = "SELECT * FROM jugadores WHERE Nombre_equipo = ?";
                    stmt = connection.prepareStatement(query);
                    stmt.setString(1, "Cavaliers");
                    rs = stmt.executeQuery();
                    while (rs.next()) {
                        String procedencia = rs.getString("Procedencia");
                        String posicion = rs.getString("Posicion");
                        System.out.println("Procedencia: " + procedencia + " | Posición: " + posicion);
                    }
                    break;
                case 2:
                    query = "SELECT count(*) as count FROM jugadores WHERE Procedencia = ?";
                    stmt = connection.prepareStatement(query);
                    stmt.setString(1, "Spain");
                    rs = stmt.executeQuery();
                    while (rs.next()) {
                        String contador = rs.getString("count");
                        System.out.println("Jugadores Españoles: " + contador);
                    }
                    break;
                case 3:
                    query = "INSERT INTO jugadores VALUES (?, ?, ?, ?, ?, ?, ?)";
                    stmt = connection.prepareStatement(query);
                    stmt.setInt(1, 667);
                    stmt.setString(2, "Angel Mateu Paris");
                    stmt.setString(3, "Catalunya");
                    stmt.setString(4, "6-7");
                    stmt.setInt(5, 230);
                    stmt.setString(6, "G-F");
                    stmt.setString(7, "Raptors");
                    stmt.execute();
                    break;
                case 4:
                    query = "SELECT AVG(e.Puntos_Por_Partido) as Media, j.Nombre as Nombre_Jugador FROM estadisticas e JOIN jugadores j WHERE j.codigo = e.jugador AND e.temporada = '04/05' AND e.Puntos_por_partido > (SELECT AVG(e.Puntos_por_partido) FROM estadisticas e) GROUP BY j.codigo, e.Puntos_por_partido";
                    stmt = connection.prepareStatement(query);
                    rs = stmt.executeQuery();
                    while (rs.next()) {
                        int media = rs.getInt("Media");
                        String nombreJugador = rs.getString("Nombre_Jugador");
                        System.out.println("Media: " + media + " | Nombre Jugador: " + nombreJugador);
                    }
                    break;
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
