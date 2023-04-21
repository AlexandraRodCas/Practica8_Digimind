package alexandra.rodriguez.mydigimind.ui.home

import alexandra.rodriguez.mydigimind.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import alexandra.rodriguez.mydigimind.databinding.FragmentHomeBinding
import alexandra.rodriguez.mydigimind.Task
import android.content.Context
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HomeFragment : Fragment() {

    private lateinit var storage: FirebaseFirestore
    private lateinit var usuario: FirebaseAuth

    private var adaptador: AdapterTasks? = null
    private var _binding: FragmentHomeBinding? = null
    companion object{
        var tasks = ArrayList<Task>()
        var first = true
    }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        storage = FirebaseFirestore.getInstance()
        usuario = FirebaseAuth.getInstance()

        if(first){
            fillTask()
            first = false
        }
        //Toast.makeText(context, tasks.size, Toast.LENGTH_SHORT).show()
        adaptador = AdapterTasks(root.context, tasks)

        val gridView: GridView = root.findViewById(R.id.reminders)
        gridView.adapter= adaptador

        var txtEmail: TextView = root.findViewById(R.id.txtCorreo)
        var et_direccion: EditText = root.findViewById(R.id.et_direccion)
        var et_telefono: EditText = root.findViewById(R.id.et_telefono)
        txtEmail.text = usuario.currentUser?.email.toString()

        var save: Button = root.findViewById(R.id.btn_guardar)
        save.setOnClickListener {
            storage.collection("usuarios").document(txtEmail.text.toString()).set(
                hashMapOf("email" to txtEmail.text.toString(),
                "direccion" to et_direccion.text.toString(),
                "telefono" to et_telefono.text.toString())
            ).addOnSuccessListener { 
                Toast.makeText(root.context, "Guardado con exito", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener{
                Toast.makeText(root.context, "Fallo al guardar"+it.toString(), Toast.LENGTH_SHORT).show()
            }
        }

        var delete: Button = root.findViewById(R.id.btn_eliminar)
        delete.setOnClickListener {
            storage.collection("usuarios").document(txtEmail.text.toString()).delete()
            Toast.makeText(root.context, "Eliminado con exito", Toast.LENGTH_SHORT).show()
        }

        val docRef = storage.collection("usuarios").document(txtEmail.text.toString())
        docRef.get().addOnCompleteListener{task ->
            if(task.isSuccessful){
                val document = task.result
                if(document!= null){
                    et_direccion.setText(document.getString("direccion"))
                    et_telefono.setText(document.getString("telefono"))
                }else{
                    Toast.makeText(root.context, "No se encontró", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(root.context, "No hay tareas registradas", Toast.LENGTH_SHORT).show()
            }
        }

        return root
    }

    fun fillTask(){
        //tasks.clear()
        storage.collection("actividades")
            .whereEqualTo("email", usuario.currentUser?.email)
            .get()
            .addOnSuccessListener {
                it.forEach{
                    var dias = ArrayList<String>()
                    if(it.getBoolean("lu") == true){
                        dias.add("Monday")
                    }
                    if(it.getBoolean("ma") == true){
                        dias.add("Tuesday")
                    }
                    if(it.getBoolean("mi") == true){
                        dias.add("Wednesday")
                    }
                    if(it.getBoolean("ju") == true){
                        dias.add("Thursday")
                    }
                    if(it.getBoolean("vi") == true){
                        dias.add("Friday")
                    }
                    if(it.getBoolean("sa") == true){
                        dias.add("Saturday")
                    }
                    if(it.getBoolean("do") == true){
                        dias.add("Sunday")
                    }

                    var titulo = it.getString("actividad")
                    var tiempo = it.getString("tiempo")

                    var act = Task(titulo!!, dias, tiempo!!)

                    tasks.add(act)

                }

            }

            .addOnFailureListener{
                Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
            }
    }
    private class AdapterTasks: BaseAdapter {
        var tareas = ArrayList<Task>()
        var contexto: Context? = null

        constructor(contexto: Context, tasks:ArrayList<Task>){
            this.tareas = tasks
            this.contexto = contexto
        }

        override fun getCount(): Int {
            return tareas.size
        }

        override fun getItem(p0: Int): Any {
            return tareas[p0]
        }

        override fun getItemId(p0: Int): Long {
            return p0.toLong()
        }

        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
            var tasks = tareas[p0]
            var inflador = LayoutInflater.from(contexto)
            var vista = inflador.inflate(R.layout.task_view, null)

            var tv_title:TextView = vista.findViewById(R.id.tv_title)
            var tv_time:TextView = vista.findViewById(R.id.tv_time)
            var tv_days:TextView = vista.findViewById(R.id.tv_days)

            tv_title.setText(tasks.title)
            tv_time.setText(tasks.time)
            tv_days.setText(tasks.days.toString())

            return vista
        }
    }
}