import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.letranbaosuong.asteroidradarapp.R
import com.letranbaosuong.asteroidradarapp.models.Asteroid

class AsteroidItemAdapter(private val context: Context, private val dataList: List<Asteroid>) :
    RecyclerView.Adapter<AsteroidItemAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtCodename: TextView = itemView.findViewById(R.id.codename)
        val txtCloseApproachDate: TextView = itemView.findViewById(R.id.closeApproachDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.asteroid_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataList[position]
        holder.txtCodename.text = data.codename
        holder.txtCloseApproachDate.text = data.closeApproachDate
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}
