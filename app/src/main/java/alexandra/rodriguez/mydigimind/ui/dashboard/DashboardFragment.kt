package alexandra.rodriguez.mydigimind.ui.dashboard

import alexandra.rodriguez.mydigimind.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import alexandra.rodriguez.mydigimind.databinding.FragmentDashboardBinding
import alexandra.rodriguez.mydigimind.Task
import alexandra.rodriguez.mydigimind.ui.home.HomeFragment
import android.app.TimePickerDialog
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private lateinit var storage: FirebaseFirestore
    private lateinit var usuario: FirebaseAuth

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        storage = FirebaseFirestore.getInstance()
        usuario = FirebaseAuth.getInstance()
        val btn_time: Button = root.findViewById(R.id.btn_time)

        btn_time.setOnClickListener{
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener{ timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)

                btn_time.text = SimpleDateFormat("HH:mm").format(cal.time)
            }
            TimePickerDialog(root.context, timeSetListener, cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE), true).show()

        }

        val btn_save = root.findViewById(R.id.done) as Button
        val et_titulo = root.findViewById(R.id.name) as EditText
        val checkMonday = root.findViewById(R.id.monday) as CheckBox
        val checkTuesday = root.findViewById(R.id.tuesday) as CheckBox
        val checkWednesday = root.findViewById(R.id.wednesday) as CheckBox
        val checkThursday = root.findViewById(R.id.thursday) as CheckBox
        val checkFriday = root.findViewById(R.id.friday) as CheckBox
        val checkSaturday = root.findViewById(R.id.saturday) as CheckBox
        val checkSunday = root.findViewById(R.id.sunday) as CheckBox

        btn_save.setOnClickListener{

            var titulo = et_titulo.text.toString()
            var tiempo = btn_time.text.toString()
            var days = ArrayList<String>()

            if(checkMonday.isChecked)
                days.add("Monday")
            if(checkTuesday.isChecked)
                days.add("Tuesday")
            if(checkWednesday.isChecked)
                days.add("Wednesday")
            if(checkThursday.isChecked)
                days.add("Thursday")
            if(checkFriday.isChecked)
                days.add("Friday")
            if(checkSaturday.isChecked)
                days.add("Saturday")
            if(checkSunday.isChecked)
                days.add("Sunday")


            val actividad = hashMapOf(
                "actividad" to et_titulo.text.toString(),
                "email" to usuario.currentUser?.email.toString(),
                "lu" to checkMonday.isChecked,
                "ma" to checkTuesday.isChecked,
                "mi" to checkWednesday.isChecked,
                "ju" to checkThursday.isChecked,
                "vi" to checkFriday.isChecked,
                "sa" to checkSaturday.isChecked,
                "do" to checkSunday.isChecked,
                "tiempo" to btn_time.text.toString()
            )

            storage.collection("actividades")
                .add(actividad)
                .addOnSuccessListener {
                    Toast.makeText(root.context, "Task agregada", Toast.LENGTH_SHORT).show()
                    var task = Task(titulo, days, tiempo)
                    HomeFragment.tasks.add(task)
                }
                .addOnFailureListener{
                    Toast.makeText(root.context, it.toString(), Toast.LENGTH_SHORT).show()
                }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}