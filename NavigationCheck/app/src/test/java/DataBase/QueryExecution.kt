package DataBase


import java.sql.PreparedStatement
import java.sql.ResultSet

class QueryExecution {

    lateinit var rs: ResultSet

    fun executeQuery(prepareStmt: PreparedStatement): ResultSet? {
        try {

            rs = prepareStmt.executeQuery()
        } catch (e: Exception) {
            return null
        }

        return rs

    }

    fun updateQuery(prepareStmt: PreparedStatement): Boolean? {
        var flag: Boolean? = false
        try {
            prepareStmt.executeUpdate()
            flag = true
            prepareStmt.close()
        } catch (e: Exception) {
            println(e)
            return false
        }

        return flag
    }
}
