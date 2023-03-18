package alexandra.rodriguez.mydigimind

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

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

        var tv_title: TextView = vista.findViewById(R.id.tv_title)
        var tv_time: TextView = vista.findViewById(R.id.tv_time)
        var tv_days: TextView = vista.findViewById(R.id.tv_days)

        tv_title.setText(tasks.title)
        tv_time.setText(tasks.time)
        tv_days.setText(tasks.days.toString())

        return vista
    }
}