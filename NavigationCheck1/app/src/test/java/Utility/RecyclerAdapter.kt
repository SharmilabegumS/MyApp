package Utility

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import de.hdodenhof.circleimageview.CircleImageView

class RecyclerAdapter(private val context: Context, private val contacts: List<Contacts>) :
    RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>() {
    private val itemSelectedListener: RecyclerItemSelectedListener

    val itemCount: Int
        get() = contacts.size

    init {
        itemSelectedListener = context as MainActivity
    }

    @NonNull
    fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): MyViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return MyViewHolder(view)
    }

    fun onBindViewHolder(@NonNull holder: MyViewHolder, position: Int) {

        holder.profileName.setText(contacts[position].getName())
        holder.profilePic.setImageDrawable(ContextCompat.getDrawable(context, contacts[position].getPicId()))
    }

    internal inner class MyViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        var profilePic: CircleImageView
        var profileName: TextView
        var rootView: LinearLayout

        init {
            profileName = itemView.findViewById(R.id.profile_name)
            profilePic = itemView.findViewById<View>(R.id.profile_pic)
            rootView = itemView.findViewById(R.id.rootView)
            rootView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            itemSelectedListener.onItemSelected(contacts[getAdapterPosition()])

        }
    }
}
