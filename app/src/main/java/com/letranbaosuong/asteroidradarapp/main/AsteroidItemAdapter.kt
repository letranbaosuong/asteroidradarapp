import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.letranbaosuong.asteroidradarapp.R
import com.letranbaosuong.asteroidradarapp.databinding.AsteroidItemBinding
import com.letranbaosuong.asteroidradarapp.models.Asteroid

class AsteroidItemAdapter(private val context: Context, private val dataList: List<Asteroid>) :
    RecyclerView.Adapter<AsteroidItemAdapter.AsteroidItemViewHolder>() {

//    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
////        val txtCodename: TextView = itemView.findViewById(R.id.codename)
////        val txtCloseApproachDate: TextView = itemView.findViewById(R.id.closeApproachDate)
////        val image: ImageView = itemView.findViewById(R.id.imageView)
//
//    }

    inner class AsteroidItemViewHolder(private val binding: AsteroidItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(asteroid: Asteroid) {
            binding.asteroid = asteroid
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidItemViewHolder {
//        val view = LayoutInflater.from(context).inflate(R.layout.asteroid_item, parent, false)
//        return ViewHolder(view)
        val inflater = LayoutInflater.from(parent.context)
        val binding = AsteroidItemBinding.inflate(inflater, parent, false)
        return AsteroidItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AsteroidItemViewHolder, position: Int) {
        val data = dataList[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}
