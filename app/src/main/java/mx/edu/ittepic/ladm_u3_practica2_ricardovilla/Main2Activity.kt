package mx.edu.ittepic.ladm_u3_practica2_ricardovilla

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main2.*

class Main2Activity : AppCompatActivity() {
    var baseRestuarante = FirebaseFirestore.getInstance()
    var dataLista = ArrayList<String>()
    var listaID = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        //Mostrar todos
        btn_todos.setOnClickListener {

            baseRestuarante.collection("restaurante")
                .addSnapshotListener { querySnapshot, firebaseFirestoreException ->

                    if (firebaseFirestoreException!=null){

                        Toast.makeText(this,"ERROR NO SE PUEDE ACCEDER A CONSULTA", Toast.LENGTH_LONG).show()
                        return@addSnapshotListener
                    }

                    dataLista.clear()
                    listaID.clear()
                    for (document in querySnapshot!!){

                        var cadena = "Nombre: "+document.getString("nombre")+"\n"+"Domicilio: "+document.getString("domicilio")+"\nCelular: "+document.getString("celular")+
                                "\nPEDIDO"+"\nDescripcion: "+document.get("pedido.descripcion")+ "/ Precio: "+
                                document.get("pedido.precio")+" / Cantidad: "+document.get("pedido.cantidad")+
                                " / Entregado: "+document.get("pedido.entregado")
                        dataLista.add(cadena)
                        listaID.add(document.id)
                    }

                    if(dataLista.size==0){
                        dataLista.add("NO HAY DATA")
                    }

                    var adaptador = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,dataLista)
                    lista.adapter =  adaptador


                }

        }


        //Consulta
        btn_consultar.setOnClickListener {

            if(edit_buscar.text.isEmpty()){
                Toast.makeText(this,"DEBE PONER LO QUE DESEA BUSCAR", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            when(clave.selectedItemPosition){

                0->(consultaNombre(edit_buscar.text.toString()))
                1->(consultaDesc(edit_buscar.text.toString()))
                2->(consultaPrecio(edit_buscar.text.toString().toDouble()))
            }
        }


        //Mostrar
        baseRestuarante.collection("restaurante")
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->

                if (firebaseFirestoreException!=null){

                    Toast.makeText(this,"ERROR NO SE PUEDE ACCEDER A CONSULTA", Toast.LENGTH_LONG).show()
                    return@addSnapshotListener
                }

                dataLista.clear()
                listaID.clear()
                for (document in querySnapshot!!){

                    var cadena = "Nombre: "+document.getString("nombre")+"\n"+"Domicilio: "+document.getString("domicilio")+"\nCelular: "+document.getString("celular")+
                            "\nPEDIDO"+"\nDescripcion: "+document.get("pedido.descripcion")+ "/ Precio: "+
                            document.get("pedido.precio")+" / Cantidad: "+document.get("pedido.cantidad")+
                            " / Entregado: "+document.get("pedido.entregado")
                    dataLista.add(cadena)
                    listaID.add(document.id)
                }

                if(dataLista.size==0){
                    dataLista.add("NO HAY DATA")
                }

                var adaptador = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,dataLista)
                lista.adapter =  adaptador


            }

        lista.setOnItemClickListener { parent, view, position, id ->
        
            if(listaID.size == 0){
                return@setOnItemClickListener
            }
            eliminarActualizar(position)
            
        }
    }


    private fun eliminarActualizar(position: Int) {
        AlertDialog.Builder(this).setTitle("ATENCION")
            .setMessage("¿Qué deseas hacer con \n${dataLista[position]}?")
            .setPositiveButton("Eliminar"){d,w->
                eliminar(listaID[position])
            }
            .setNegativeButton("Actualizar"){d,w->

                ventanaActualizar(listaID[position])

            }
            .setNeutralButton("Cancelar"){dialog,which->}
            .show()
    }
    private fun ventanaActualizar(idActualizar: String) {

        baseRestuarante.collection("restaurante")
            .document(idActualizar)
            .get()
            .addOnSuccessListener {
                var v = Intent(this,Main3Activity::class.java)
                v.putExtra("id",idActualizar)
                v.putExtra("nombre",it.getString("nombre"))
                v.putExtra("domicilio",it.getString("domicilio"))
                v.putExtra("celular",it.getString("celular"))
                //pedido
                v.putExtra("descripcion",it.get("pedido.descripcion").toString())
                v.putExtra("precio",it.get("pedido.precio").toString())
                v.putExtra("cantidad",it.get("pedido.cantidad").toString())
                v.putExtra("entregado",it.get("pedido.entregado").toString())

                startActivity(v)
            }
            .addOnFailureListener {
                Toast.makeText(this,"ERROR NO HAY CONEXION DE RED",Toast.LENGTH_LONG).show()
            }
    }
    private fun eliminar(idEliminar: String) {
        baseRestuarante.collection("restaurante")
            .document(idEliminar)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(this,"SE ELIMINO CON EXITO",Toast.LENGTH_LONG).show()


            }
            .addOnFailureListener {
                Toast.makeText(this,"NO SE ELIMINO",Toast.LENGTH_LONG).show()

            }

    }


    private fun consultaPrecio(valor: Double) {
        baseRestuarante.collection("restaurante")
            .whereEqualTo("pedido.precio",valor)
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if(firebaseFirestoreException!=null){
                    Toast.makeText(this,"ERROR NO SE PUEDE ACCEDER A CONSULTA", Toast.LENGTH_LONG).show()
                    return@addSnapshotListener
                }
                dataLista.clear()
                listaID.clear()
                var cadena =""
                for(document in querySnapshot!!){

                    cadena = "Nombre: "+document.getString("nombre")+"\n"+"Domicilio: "+document.getString("domicilio")+"\nCelular: "+document.getString("celular")+
                            "\nPEDIDO"+"\nDescripción: "+document.get("pedido.descripcion")+ "/ Precio: "+
                            document.get("pedido.precio")+" / Cantidad: "+document.get("pedido.cantidad")+
                            " / Entregado: "+document.get("pedido.entregado")
                    dataLista.add(cadena)
                    listaID.add(document.id)

                }

                if(dataLista.size==0){
                    dataLista.add("NO HAY DATA")
                }

                var adaptador = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,dataLista)
                lista.adapter =  adaptador
            }

    }

    private fun consultaDesc(valor: String) {
        baseRestuarante.collection("restaurante")
            .whereEqualTo("pedido.descripcion",valor)
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if(firebaseFirestoreException!=null){
                    Toast.makeText(this,"ERROR NO SE PUEDE ACCEDER A CONSULTA", Toast.LENGTH_LONG).show()
                    return@addSnapshotListener
                }
                dataLista.clear()
                listaID.clear()
                var cadena =""
                for(document in querySnapshot!!){

                    cadena = "Nombre: "+document.getString("nombre")+"\n"+"Domicilio: "+document.getString("domicilio")+"\nCelular: "+document.getString("celular")+
                            "\nPEDIDO"+"\nDescripción: "+document.get("pedido.descripcion")+ "/ Precio: "+
                            document.get("pedido.precio")+" / Cantidad: "+document.get("pedido.cantidad")+
                            " / Entregado: "+document.get("pedido.entregado")
                    dataLista.add(cadena)
                    listaID.add(document.id)

                }

                if(dataLista.size==0){
                    dataLista.add("NO HAY DATA")
                }

                var adaptador = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,dataLista)
                lista.adapter =  adaptador
            }

    }

    private fun consultaNombre(valor: String) {
        baseRestuarante.collection("restaurante")
            .whereEqualTo("nombre",valor)
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if(firebaseFirestoreException!=null){
                    Toast.makeText(this,"ERROR NO SE PUEDE ACCEDER A CONSULTA", Toast.LENGTH_LONG).show()
                    return@addSnapshotListener
                                    }
                dataLista.clear()
                listaID.clear()
                var cadena =""
                for(document in querySnapshot!!){

                    cadena = "Nombre: "+document.getString("nombre")+"\n"+"Domicilio: "+document.getString("domicilio")+"\nCelular: "+document.getString("celular")+
                            "\nPEDIDO"+"\nDescripción: "+document.get("pedido.descripcion")+ "/ Precio: "+
                            document.get("pedido.precio")+" / Cantidad: "+document.get("pedido.cantidad")+
                            " / Entregado: "+document.get("pedido.entregado")
                    dataLista.add(cadena)
                    listaID.add(document.id)

                }

                if(dataLista.size==0){
                    dataLista.add("NO HAY DATA")
                }

                var adaptador = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,dataLista)
                lista.adapter =  adaptador
            }

    }
}
