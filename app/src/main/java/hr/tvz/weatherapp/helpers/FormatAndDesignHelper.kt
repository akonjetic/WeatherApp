package hr.tvz.weatherapp.helpers

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.location.Location
import android.location.LocationManager
import hr.tvz.weatherapp.MainActivity
import hr.tvz.weatherapp.R
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class FormatAndDesignHelper {

    fun getLattLongFormatted(lattLong: String): String {

        val latt = lattLong.substringBefore(",").dropLast(3)
        val long = lattLong.substringAfter(",").dropLast(3)

        return "$latt°, $long°"
    }

    fun getDistance(context: Context, lattLong: String): String {
        val location = Location(LocationManager.GPS_PROVIDER)
        location.latitude = MainActivity.latt
        location.longitude = MainActivity.long

        val distance = location.distanceTo(
            Location(LocationManager.GPS_PROVIDER).apply {
                latitude = lattLong.substringBefore(",").dropLast(3).toDouble()
                longitude = lattLong.substringAfter(",").dropLast(3).toDouble()
            }
        ).toInt()

        return if (MainActivity.metrics) {
            context.getString(R.string.distance) + " " + (distance / 1000).toString() + " km"
        } else {
            context.getString(R.string.distance) + " " + ((distance / 1000) * 0.621371).toInt().toString() + " miles"
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun formatDateToDayInWeek(date: String): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        val dateInUse = formatter.parse(date)
        return SimpleDateFormat("EE").format(dateInUse).uppercase()
    }

    fun setDialogDesign(context: Context, dialog: AlertDialog) {
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(context.getColor(R.color.color_secondary))
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(context.getColor(R.color.on_color))
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(context.getColor(R.color.color_secondary))
    }

    fun getDateInString(): String {
        var dateInS = ""

        val date = getCurrentDateTime()
        dateInS = date.toString("yyyy/MM/dd")

        return dateInS
    }

    fun getDateAndDayFormatted(timeString: String): String {
        val dateWithDay = LocalDate.parse(
            timeString.subSequence(0, 10),
            DateTimeFormatter.ofPattern("yyyy-MM-dd")
        )

        return dateWithDay.format(DateTimeFormatter.ofPattern("E, MMM dd"))
    }

    fun getTimeFromString(timeString: String, timezone: String): String {
        val currentTimeHour = timeString.subSequence(11, 13).toString().toInt()
        val currentTimeMin = timeString.subSequence(14, 16).toString()

        return currentTimeHour.toString() + ":" + currentTimeMin + " (" + timezone.substringBefore("/") + ")"
    }

    fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }

    fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }
}
