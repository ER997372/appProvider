package space.softsys.testfly.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import space.softsys.testfly.databinding.ItemViewBinding
import space.softsys.testfly.model.App

class AppsAdapter(private val apps : ArrayList<App>, private val listener : (App) -> Unit) : RecyclerView.Adapter<AppsAdapter.DataViewHolder>() {
    class DataViewHolder(
        private val binding: ItemViewBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(app: App, listener: (App) -> Unit) {
            binding.card.setOnClickListener{listener(app)}
            binding.itemName.text = app.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val binding =
            ItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DataViewHolder(binding)
    }

    override fun getItemCount(): Int = apps.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) = holder.bind(apps[position], listener)

    fun fillData(list: List<App>) = apps.addAll(list)

    fun appendData(data: App) = apps.add(data)

    fun removeData(index: Int) = apps.removeAt(index)
    fun removeData(element: App) = apps.remove(element)

    fun clearData() = apps.clear()
}