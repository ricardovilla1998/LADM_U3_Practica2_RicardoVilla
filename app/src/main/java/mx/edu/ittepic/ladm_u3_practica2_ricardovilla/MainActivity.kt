package mx.edu.ittepic.ladm_u3_practica2_ricardovilla

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var baseRestuarante = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_insertar.setOnClickListener {
            insertarPedido()
        }
        btn_ver.setOnClickListener {
            var ventanaConsulta = Intent(this,Main2Activity::class.java)
            startActivity(ventanaConsulta)
        }
    }

    private fun insertarPedido() {

        var data = hashMapOf(
            "nombre" to edit_nombre.text.toString(),
            "domicilio" to edit_domicilio.text.toString(),
            "celular" to edit_celular.text.toString()

        )
        baseRestuarante.collection("restaurante")
            .add(data)
            .addOnSuccessListener {
                var pedido = hashMapOf(
                    "descripcion" to edit_desc.text.toString(),
                    "precio" to edit_precio.text.toString().toDouble(),
                    "cantidad" to edit_cantidad.text.toString().toInt(),
                    "entregado" to check_pedido.isChecked
                )
                baseRestuarante.collection("restaurante")
                    .document(it.id)
                    .update("pedido",pedido as Map<String,Any>)

                Toast.makeText(this,"SE CAPTURO",Toast.LENGTH_LONG).show()
                limpiarCampos()
            }
            .addOnFailureListener {
                Toast.makeText(this,"ERROR! NO SE CAPTURO",Toast.LENGTH_LONG).show()
            }


    }
    private fun limpiarCampos() {
        edit_desc.setText("")
        edit_nombre.setText("")
        edit_cantidad.setText("")
        edit_domicilio.setText("")
        edit_precio.setText("")
        edit_celular.setText("")
        check_pedido.isChecked = false
    }
}
