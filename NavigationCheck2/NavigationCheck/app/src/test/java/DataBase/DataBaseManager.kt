package DataBase



import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.ArrayList
import java.util.Arrays
import java.util.Calendar
import java.util.TimeZone

import Entity.Event
import android.text.Editable

class DataBaseManager {

private val queryExec = QueryExecution()
private val cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"))
private var status:Boolean? = null
private var prepareStatement:PreparedStatement? = null
private var queries:String? = null

 fun addEvent(eventId:String, startDate:Long, endDate:Long, location:String, eventGuests:String,
description:String, userId:String, eventRepeat:Int, eventType:Int):Boolean {
queries = ("insert into " + TABLE_EVENTS + " (" + EVENT_ID + " , " + EVENT_FROM_DATE + " ," + EVENT_TO_DATE
+ " ,	" + EVENT_LOCATION + "," + EVENT_GUESTS + ", " + EVENT_DESCRIPTION + ", " + USER_ID + ","
+ EVENT_REPEAT + ", " + EVENT_TYPE + ")values(?,?,?,?,?,?,?,?,?)")
try
{
prepareStatement = connection!!.prepareStatement(queries)
prepareStatement!!.setString(1, eventId)
prepareStatement!!.setLong(2, startDate)
prepareStatement!!.setLong(3, endDate)
prepareStatement!!.setString(4, location)
prepareStatement!!.setString(5, eventGuests)
prepareStatement!!.setString(6, description)
prepareStatement!!.setString(7, userId)
prepareStatement!!.setInt(8, eventRepeat)
prepareStatement!!.setInt(9, eventType)
status = queryExec.updateQuery(prepareStatement as PreparedStatement)
return status!!
}
catch (e:SQLException) {
e.printStackTrace()
return status!!
}

}

 fun getEvents(begin:Long?, userId:Int):ArrayList<Event>? {
val eventList = ArrayList<Event>()
var guestList:ArrayList<String>? = ArrayList()
try
{
queries = ("select * from  " + TABLE_EVENTS + " where " + EVENT_FROM_DATE + " between ? and ? and " + USER_ID
+ " = ?")
prepareStatement = connection!!.prepareStatement(queries)
prepareStatement!!.setLong(1, begin!!)
prepareStatement!!.setLong(2, begin!! + 86400000L)
prepareStatement!!.setInt(3, userId)
val rs = queryExec.executeQuery(prepareStatement as PreparedStatement)
while (rs!!.next())
{
val names = rs.getString(5)
val elements = names.split(",".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
val nameList = Arrays.asList<String>(*elements)
guestList = ArrayList(nameList)

eventList.add(Event(rs.getString(1), rs.getLong(2), rs.getLong(3), rs.getString(4), guestList,
rs.getString(6)))
}
guestList = null
}
catch (e:SQLException) {

return null
}

return eventList
}

 fun getSingleEvents(begin:Long?, eventId:String, userId:Int):Event? {
var guestList = ArrayList<String>()
var ev:Event? = null
try
{
queries = ("select * from  " + TABLE_EVENTS + " where " + EVENT_FROM_DATE + " between ? and ? and "
+ EVENT_ID + " =? and " + USER_ID + " = ?")
prepareStatement = connection!!.prepareStatement(queries)
prepareStatement!!.setLong(1, begin!!)
prepareStatement!!.setLong(2, begin!! + 86400000L)
prepareStatement!!.setString(3, eventId)
prepareStatement!!.setInt(4, userId)
val rs = queryExec.executeQuery(prepareStatement as PreparedStatement)
while (rs!!.next())
{
val names = rs.getString(5)
val elements = names.split(",".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
val nameList = Arrays.asList<String>(*elements)
guestList = ArrayList(nameList)

ev = Event(rs.getString(1), rs.getLong(2), rs.getLong(3), rs.getString(4), guestList,
rs.getString(6))
}
}
catch (e:SQLException) {
e.printStackTrace()
return null
}

return ev
}

 fun doLogin(userId:Int, password:String):Boolean? {
try
{
queries = "select * from  $TABLE_USERS where $USER_ID = ? "
prepareStatement = connection!!.prepareStatement(queries)

prepareStatement!!.setInt(1, userId)

val rs = queryExec.executeQuery(prepareStatement as PreparedStatement)
while (rs!!.next())
{

if (rs.getString(2) == password && rs.getInt(1) == userId)
{
return true
}
}

}
catch (e:SQLException) {
return false
}

return false

}

 fun getUserId(eventOrganizerId:String):ArrayList<String>? {
try
{
val userId = ArrayList<String>()
queries = "select $USER_ID  from  $TABLE_USERS where  $USER_ID != ? "
prepareStatement = connection!!.prepareStatement(queries)
prepareStatement!!.setString(1, eventOrganizerId)
val rs = queryExec.executeQuery(prepareStatement as PreparedStatement)
while (rs!!.next())
{

userId.add(rs.getString(1))
}
return userId

}
catch (e:SQLException) {
return null
}

}

 fun getEventRepeatStatus(eventId:String, startDate:Long?):Boolean {
var repeatStatus = false
try
{

queries = ("select " + EVENT_REPEAT + "  from  " + TABLE_EVENTS + " where  " + EVENT_ID + " = ? and "
+ EVENT_FROM_DATE + " between ? and ? ")
prepareStatement = connection!!.prepareStatement(queries)
prepareStatement!!.setString(1, eventId)
prepareStatement!!.setLong(2, startDate!!)
prepareStatement!!.setLong(3, startDate!! + 86400000L)
val rs = queryExec.executeQuery(prepareStatement as PreparedStatement)
while (rs!!.next())
{
if (rs!!.getInt(1) == 1)
{
repeatStatus = true
}
else if (rs!!.getInt(1) == 0)
{
repeatStatus = false
}

}
return repeatStatus

}
catch (e:SQLException) {
return repeatStatus
}

}

 fun getEventTypeAndRepeatStatus(eventId:String, startDate:Long?):IntArray? {

var ans:IntArray? = null
try
{

queries = ("select " + EVENT_REPEAT + " , " + EVENT_TYPE + " from  " + TABLE_EVENTS + " where  " + EVENT_ID
+ " = ? and " + EVENT_FROM_DATE + " between ? and ? ")
prepareStatement = connection!!.prepareStatement(queries)
prepareStatement!!.setString(1, eventId)
prepareStatement!!.setLong(2, startDate!!)
prepareStatement!!.setLong(3, startDate!! + 86400000L)
val rs = queryExec.executeQuery(prepareStatement as PreparedStatement)
while (rs!!.next())
{
ans = IntArray(2)
ans[0] = rs.getInt(1)
ans[1] = rs.getInt(2)

}

}
catch (e:SQLException) {

}

return ans

}

 fun deleteEvent(eventId:String, date:Long?):Boolean {
queries = ("delete from " + TABLE_EVENTS + " where " + EVENT_ID + " = ? and  " + EVENT_FROM_DATE
+ " between ? and ? ")
try
{
prepareStatement = connection!!.prepareStatement(queries)
prepareStatement!!.setString(1, eventId)
prepareStatement!!.setLong(2, date!!)
prepareStatement!!.setLong(3, date!! + 86400000L)
status = queryExec.updateQuery(prepareStatement as PreparedStatement)
}
catch (e:SQLException) {

return status!!
}

return status!!
}

 fun deleteAllEvent(eventId:String):Boolean {
queries = "delete from $TABLE_EVENTS where $EVENT_ID = ? "
try
{
prepareStatement = connection!!.prepareStatement(queries)
prepareStatement!!.setString(1, eventId)
status = queryExec.updateQuery(prepareStatement as PreparedStatement)
}
catch (e:SQLException) {

return status!!
}

return status!!

}

 fun deleteFutureEvent(eventId:String, date:Long?):Boolean {
queries = "delete from $TABLE_EVENTS where $EVENT_ID = ? and  $EVENT_FROM_DATE >= ? "
try
{
prepareStatement = connection!!.prepareStatement(queries)
prepareStatement!!.setString(1, eventId)
prepareStatement!!.setLong(2, date!!)
status = queryExec.updateQuery(prepareStatement as PreparedStatement)
}
catch (e:SQLException) {

return status!!
}

return status!!
}

 fun editEventOnThatDay(lengthOfChoiceList:Int, editChoice:ArrayList<Int>,
editContents:ArrayList<Any>, eventId:String, date:Long?, calendar:Calendar):Boolean {

try
{
queries = getEditEventQuery(lengthOfChoiceList, editChoice)
queries = queries + (" where $EVENT_ID = ?  and  $EVENT_FROM_DATE between ? and ? ")
prepareStatement = connection!!.prepareStatement(queries)
val itr = editContents.iterator()
var index = 1
while (itr.hasNext())
{
prepareStatement!!.setObject(index, itr.next())
index++
}
prepareStatement!!.setString(index, eventId)
prepareStatement!!.setLong(index + 1, date!!)
prepareStatement!!.setLong(index + 2, date!! + 86400000L)

status = queryExec.updateQuery(prepareStatement as PreparedStatement)
checkForAllDayEventTimeThatDate(eventId, date, calendar)
}
catch (e:SQLException) {

return status!!
}

return status!!

}

 fun editEventOnFutureDay(lengthOfChoiceList:Int, editChoice:ArrayList<Int>,
editContents:ArrayList<Any>, eventId:String, date:Long?, calendar:Calendar):Boolean {

try
{
queries = getEditEventQuery(lengthOfChoiceList, editChoice)
queries = queries + (" where $EVENT_ID = ?  and  $EVENT_FROM_DATE >= ? ")
prepareStatement = connection!!.prepareStatement(queries)
val itr = editContents.iterator()
var index = 1
while (itr.hasNext())
{
prepareStatement!!.setObject(index, itr.next())
index++
}
prepareStatement!!.setString(index, eventId)
prepareStatement!!.setLong(index + 1, date!!)

status = queryExec.updateQuery(prepareStatement as PreparedStatement)
checkForAllDayEventTimeFutureDate(eventId, date, calendar)
}
catch (e:SQLException) {

return status!!
}

return status!!

}

 fun editEvent(lengthOfChoiceList:Int, editChoice:ArrayList<Int>, editContents:ArrayList<Any>,
eventId:String, calendar:Calendar):Boolean {

try
{
queries = getEditEventQuery(lengthOfChoiceList, editChoice)
queries = queries + (" where $EVENT_ID = ? ")
prepareStatement = connection!!.prepareStatement(queries)
val itr = editContents.iterator()
var index = 1
while (itr.hasNext())
{
prepareStatement!!.setObject(index, itr.next())
index++
}
prepareStatement!!.setString(index, eventId)
status = queryExec.updateQuery(prepareStatement as PreparedStatement)
checkForAllDayEventTimeAllDate(eventId, calendar)
}
catch (e:SQLException) {

return status!!
}

return status!!
}

private fun checkForAllDayEventTimeThatDate(eventId:String, date:Long?, calendar:Calendar) {
try
{
queries = ("select " + EVENT_TYPE + "," + EVENT_FROM_DATE + "," + EVENT_TO_DATE + " from " + TABLE_EVENTS
+ " where " + EVENT_ID + " = ? and  " + EVENT_FROM_DATE + "  between ? and ?  ")
prepareStatement = connection!!.prepareStatement(queries)
prepareStatement!!.setString(1, eventId)
prepareStatement!!.setLong(2, date!!)
prepareStatement!!.setLong(3, date!! + 86400000L)
val rs = queryExec.executeQuery(prepareStatement as PreparedStatement)
while (rs!!.next())
{

if (rs.getInt(1) == 1)
{

makeChangeInTime(eventId, rs.getLong(2), rs.getLong(3), calendar)

}
}
}
catch (e:SQLException) {
e.printStackTrace()
}

}

private fun checkForAllDayEventTimeAllDate(eventId:String, calendar:Calendar) {
try
{
queries = ("select " + EVENT_TYPE + "," + EVENT_FROM_DATE + "," + EVENT_TO_DATE + " from " + TABLE_EVENTS
+ " where " + EVENT_ID + " = ? ")
prepareStatement = connection!!.prepareStatement(queries)
prepareStatement!!.setString(1, eventId)
val rs = queryExec.executeQuery(prepareStatement as PreparedStatement)
while (rs!!.next())
{

if (rs.getInt(1) == 1)
{

makeChangeInTime(eventId, rs.getLong(2), rs.getLong(3), calendar)

}
}
}
catch (e:SQLException) {
e.printStackTrace()
}

}

private fun checkForAllDayEventTimeFutureDate(eventId:String, date:Long?, calendar:Calendar) {
try
{
queries = ("select " + EVENT_TYPE + "," + EVENT_FROM_DATE + "," + EVENT_TO_DATE + " from " + TABLE_EVENTS
+ " where " + EVENT_ID + " = ? and  " + EVENT_FROM_DATE + " >= ? ")
prepareStatement = connection!!.prepareStatement(queries)
prepareStatement!!.setString(1, eventId)
prepareStatement!!.setLong(2, date!!)
val rs = queryExec.executeQuery(prepareStatement as PreparedStatement)
while (rs!!.next())
{

if (rs.getInt(1) == 1)
{

makeChangeInTime(eventId, rs.getLong(2), rs.getLong(3), calendar)

}
}
}
catch (e:SQLException) {
e.printStackTrace()
}

}

private fun makeChangeInTime(eventId:String, l:Long, m:Long, calendar:Calendar) {
try
{
val begin = makeAllDayTimeBegin(l, calendar)
val end = makeAllDayTimeEnd(m, calendar)
queries = ("update " + TABLE_EVENTS + " set " + EVENT_FROM_DATE + " = ? , " + EVENT_TO_DATE + " = ?  where "
+ EVENT_ID + " = ?  and  " + EVENT_FROM_DATE + " = ? ")
prepareStatement = connection!!.prepareStatement(queries)

prepareStatement!!.setLong(1, begin)
prepareStatement!!.setLong(2, end)
prepareStatement!!.setString(3, eventId)
prepareStatement!!.setLong(4, l)
}
catch (e:SQLException) {
e.printStackTrace()
}

}

private fun makeAllDayTimeBegin(l:Long, calendar:Calendar):Long {
var finalTime:Long = 0
val value1 = OffsetDateTime.now().offset
val sign = value1.toString().substring(0, 1)
val x = Integer.parseInt(value1.toString().substring(1, 3))
val y = Integer.parseInt(value1.toString().substring(4, 6))
val timeVal = (x * 60 * 60 + y * 60) * 1000

if (sign == "+")
{
finalTime = l - timeVal
}
else if (sign == "-")
{
finalTime = l + timeVal
}
    calendar.timeInMillis = finalTime
calendar.set(Calendar.MINUTE, 1)
calendar.set(Calendar.HOUR, 0)
    cal.time = calendar.time

return cal.timeInMillis
}

private fun makeAllDayTimeEnd(l:Long, calendar:Calendar):Long {
var finalTime:Long = 0
val value1 = OffsetDateTime.now().offset
val sign = value1.toString().substring(0, 1)
val x = Integer.parseInt(value1.toString().substring(1, 3))
val y = Integer.parseInt(value1.toString().substring(4, 6))
val timeVal = (x * 60 * 60 + y * 60) * 1000

if (sign == "+")
{
finalTime = l - timeVal
}
else if (sign == "-")
{
finalTime = l + timeVal
}
    calendar.timeInMillis = finalTime
calendar.set(Calendar.MINUTE, 59)
calendar.set(Calendar.HOUR, 23)
    cal.time = calendar.time

return cal.timeInMillis
}

 fun getEventType(eventId:String, date:Long):Int {
try
{
queries = ("select " + EVENT_TYPE + " from " + TABLE_EVENTS + " where " + EVENT_ID + " = ? and "
+ EVENT_FROM_DATE + " between ? and ?  ")
prepareStatement = connection!!.prepareStatement(queries)
prepareStatement!!.setString(1, eventId)
prepareStatement!!.setLong(2, date)
prepareStatement!!.setLong(3, date + 86400000L)
val rs = queryExec.executeQuery(prepareStatement as PreparedStatement)
while (rs!!.next())
{

return rs.getInt(1)

}
}
catch (e:SQLException) {
e.printStackTrace()
}

return 0

}

companion object {

private var connection:Connection? = null

private val TABLE_USERS = "Users"
private val USER_ID = "User_Id"
private val PASSWORD = "Password"

private val TABLE_EVENTS = "Events"
private val EVENT_ID = "Event_Id"
private val EVENT_FROM_DATE = "From_Date"
private val EVENT_TO_DATE = "To_Date"
private val EVENT_LOCATION = "Event_Location"
private val EVENT_DESCRIPTION = "Event_Description"
private val EVENT_GUESTS = "Event_Guests"
private val EVENT_REPEAT = "Event_Repeat"
private val EVENT_TYPE = "Event_Type"

 fun initialise() {

if (connection == null)
{
val con = ConnectionDB()
connection = con.connect()

}
}

 fun createTable():Boolean {
try
{
val stmt = connection!!.createStatement()
stmt.executeUpdate(
    "create table if not exists " + TABLE_USERS + "( " + USER_ID
    + " int(11) PRIMARY KEY UNIQUE, " + PASSWORD + "  varchar(45) NOT NULL) "
)
stmt.executeUpdate(("CREATE TABLE if not exists " + TABLE_EVENTS + " (" + EVENT_ID + " varchar(45), "
+ EVENT_FROM_DATE + " int," + EVENT_TO_DATE + " int, " + EVENT_LOCATION + " varchar(45) NOT NULL,"
+ EVENT_GUESTS + " varchar(45) NOT NULL, " + EVENT_DESCRIPTION + " varchar(45) NOT NULL," + USER_ID
+ " int(11), " + EVENT_REPEAT + " int(11)," + EVENT_TYPE + " int(11), PRIMARY KEY(" + EVENT_ID
+ ", " + USER_ID + " , " + EVENT_FROM_DATE + " )  ON CONFLICT REPLACE ,FOREIGN KEY (" + USER_ID
+ ") REFERENCES  " + TABLE_USERS + " ( " + USER_ID + " )ON DELETE CASCADE)"))
stmt.close()
}
catch (e:SQLException) {
e.printStackTrace()
return false
}

return true
}

private fun getEditEventQuery(lengthOfChoiceList:Int, editChoice:ArrayList<Int>):String {
var lengthOfChoiceList = lengthOfChoiceList
val query = "update $TABLE_EVENTS set "
val queryBuilder = StringBuilder(query)
val itr = editChoice.iterator()
while (itr.hasNext())
{
val choice = itr.next()
when (choice) {
1 -> {
queryBuilder.append(" $EVENT_FROM_DATE = ? ")

}
2 -> {
queryBuilder.append(" $EVENT_TO_DATE = ? ")
}
3 -> {
queryBuilder.append(" $EVENT_LOCATION  = ?  ")
}
4 -> {
queryBuilder.append(" $EVENT_GUESTS  = ?  ")
}
5 -> {
queryBuilder.append(" $EVENT_DESCRIPTION  = ?  ")

}
}
lengthOfChoiceList = lengthOfChoiceList - 1
if ((lengthOfChoiceList) > 0)
{
queryBuilder.append(",")
}

}

return queryBuilder.toString()
}
}

}
