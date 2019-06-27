package DataBase
import java.sql.Connection
import java.sql.DriverManager
import java.util.Properties

class ConnectionDB {

    internal var connection: Connection? = null

    fun connect(): Connection? {
        try {
            val properties = Properties()
            properties.setProperty("PRAGMA foreign_keys", "ON")
            connection = DriverManager.getConnection("jdbc:sqlite:Calendar.db")
        } catch (e: Exception) {
            connection = null
        }

        return connection
    }

}
