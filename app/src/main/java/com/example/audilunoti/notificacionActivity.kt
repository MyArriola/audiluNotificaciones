package com.example.audilunoti

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.audilunoti.databinding.ActivityNotificacionBinding

class NotificacionActivity : AppCompatActivity() {
    // crear una variable privada la cual solo se va a poder usar en esta clase
    private lateinit var binding: ActivityNotificacionBinding
    //definir un canal
    private val canalNombre = "canalNotificaciones"
    //definir el canalid
    private val canalId ="canalId"
    private val notificacionId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notificacion)
        //insertar esto para que funcione
        binding = ActivityNotificacionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val btnNoti = findViewById<AppCompatButton>(R.id.btnNotificacion)
        //binding.idBoton.setOnClickListener // cuando aprete el boton va a venir y realizar esto
        crearCanalNotificacion()
        //despues de evaluar q el android sea mayor al android 8:
        //llamo a la funcion:
        crearNotificacion()
        btnNoti.setOnClickListener {
            //va a llamar a la funcion.:
            crearNotificacion()

        }
    }

//FUNCIONES PARA LAS NOTIFICACIONES
    //verifica la version de android en el que se este utilizando sea MAYOR A ANDROID 8
    private fun crearCanalNotificacion(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){ //si la version del dispositivo es mayor a Android 8:
            //creo un canal de importancia
            val canalImportancia = NotificationManager.IMPORTANCE_HIGH
            //creo una variable para el canal
            val canal = NotificationChannel(canalId,canalNombre, canalImportancia)
            //creo una variable para el administrador:
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(canal)
        }
    }

    //creo una funcion para las notificacion
    private fun crearNotificacion(){
        // permite ingresar a la aplicacion desde la notificacion
        val intent = Intent(this, NotificacionActivity::class.java).apply {
            //esto permite que no se creen diversas pestañas cuando seleccionemos la notificacion
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        // si la version del dispositivo es mayor o igual al API 21, hace:
        val flag = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
        val pendingIntent:PendingIntent = PendingIntent.getActivity(this, 1, intent, flag)

        val notificacion = NotificationCompat.Builder(  this, canalId).also {
            it.setContentTitle("Audilu")
            it.setContentText("Su bebé esta inquieto")
            it.setSmallIcon(R.drawable.logoaudilu2png)
            it.priority = NotificationCompat.PRIORITY_HIGH
            it.setContentIntent(pendingIntent)
            it.setAutoCancel(true)
        }.build()

        // prioridad para administrar la configuracion
        val notificationManager = NotificationManagerCompat.from(this)
        // si el permiso de la notificacion no fue aceptado hacer...
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 777 )
        }
        notificationManager.notify(notificacionId,notificacion)
    }
}