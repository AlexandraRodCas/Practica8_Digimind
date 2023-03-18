package alexandra.rodriguez.mydigimind.ui.home

import alexandra.rodriguez.mydigimind.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import alexandra.rodriguez.mydigimind.databinding.FragmentHomeBinding
import alexandra.rodriguez.mydigimind.Task
import android.content.Context
import android.widget.BaseAdapter
import android.widget.GridView
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

        if(first){
            fillTask()
            first = false
        }

        adaptador = AdapterTasks(root.context, tasks)

        val gridView: GridView = root.findViewById(R.id.reminders)
        gridView.adapter= adaptador

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun fillTask(){
        tasks.add(Task("Practice 1", arrayListOf("Monday", "sunday"), "17:30"))
        tasks.add(Task("Practice 2", arrayListOf("Tuesday"), "17:30"))
        tasks.add(Task("Practice 3", arrayListOf("Monday", "sunday"), "17:30"))
        tasks.add(Task("Practice 4", arrayListOf("Monday", "sunday"), "17:30"))
        tasks.add(Task("Practice 5", arrayListOf("Monday", "sunday"), "17:30"))
        tasks.add(Task("Practice 6", arrayListOf("Monday", "sunday"), "17:30"))
        tasks.add(Task("Practice 7", arrayListOf("Monday", "sunday"), "17:30"))
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