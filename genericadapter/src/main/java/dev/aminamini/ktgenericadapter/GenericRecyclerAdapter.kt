package dev.aminamini.ktgenericadapter

import android.content.Context
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import androidx.core.util.set
import androidx.recyclerview.widget.RecyclerView
import java.lang.reflect.Constructor
import java.util.concurrent.atomic.AtomicInteger

interface GenericAdapterView<Model> {
    fun onBind(model: Model, position: Int, extraObject: Any?)
}

data class Section<Model>(
    var type: Int,
    var data: Model,
    var extraObject: Any?
)

open class VH(view: View) : RecyclerView.ViewHolder(view)

open class GenericRecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val sections = ArrayList<Section<*>>()
    var providers = SparseArray< (ViewGroup, Int)->View >()

    private var types = HashMap<Class<*>, Pair<Int, Constructor<*>?>>()
    private var typeIds = AtomicInteger(0)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return VH(providers[viewType](parent, viewType))
    }


    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val v = holder.itemView as? GenericAdapterView<Any>
        val sec = sections[position]
        sec.data?.run {
            v?.onBind(this, position, sec.extraObject)
        }

    }

    override fun getItemCount() = sections.size
    override fun getItemViewType(position: Int) = sections[position].type


    inline fun<reified V:View> viewType(addDefaultProvider: Boolean = true) = viewType(V::class.java, addDefaultProvider)
    fun viewType(cls: Class<*>, addDefaultProvider: Boolean = true): Int {

        val prevAns = types[cls]
        if(prevAns == null){
            var cons : Constructor<*>? = null
            try{
                cons = cls.getDeclaredConstructor(Context::class.java)
            }catch (ignored: Throwable){}
            types[cls] = Pair(typeIds.getAndIncrement(), cons)

            val type = types[cls]!!

            if(addDefaultProvider && providers[type.first] == null){
                providers[type.first] = { vg, _ ->
                    type.second!!.newInstance(vg.context) as View
                }

            }
            return type.first
        } else {
            return prevAns.first
        }
    }


    inline fun<reified V> setProvider(noinline provider: (ViewGroup, Int)->V) where V:View, V:GenericAdapterView<*>{
        providers[viewType<V>(false)] = provider
    }
    inline fun<reified V> provider(noinline provider: (Context)->V) where V:View, V:GenericAdapterView<*>{
        providers[viewType<V>(false)] = {vg, _ -> provider(vg.context)}
    }




    inline fun<reified V, reified Model : Any> setSections(data: Collection<Model>, extra: Any? = null) where V:View , V:GenericAdapterView<Model>{
        sections.clear()
        addSections<V,Model>(data, extra)
    }
    inline fun<reified V, reified Model : Any> setSectionsAndNotify(data: Collection<Model>, extra: Any? = null) where V:View , V:GenericAdapterView<Model>{
        setSections<V,Model>(data, extra)
        notifyDataSetChanged()
    }
    inline fun<reified V, reified Model : Any> setSectionsAndNotifyRange(data: Collection<Model>, extra: Any? = null) where V:View , V:GenericAdapterView<Model>{
        setSections<V,Model>(data, extra)
        notifyItemRangeChanged(0, itemCount)
    }


    inline fun<reified V, reified Model : Any> addSections(data: Collection<Model>, extra: Any? = null) where V:View , V:GenericAdapterView<Model>{
        val type = viewType<V>()
        sections.addAll(data.map { Section(type, it, extra) })
    }
    inline fun<reified V, reified Model : Any> addSectionsAndNotify(data: Collection<Model>, extra: Any? = null) where V:View , V:GenericAdapterView<Model>{
        addSections<V,Model>(data, extra)
        notifyDataSetChanged()
    }
    inline fun<reified V, reified Model : Any> addSectionsAndNotifyRange(data: Collection<Model>, extra: Any? = null) where V:View , V:GenericAdapterView<Model>{
        addSections<V,Model>(data, extra)
        notifyItemRangeChanged(0, itemCount)
    }





    inline fun<reified V, reified Model : Any> setSection(position: Int, data: Model, extra: Any? = null) where V:View , V:GenericAdapterView<Model>{
        sections[position] = Section( viewType<V>(), data, extra )
    }
    inline fun<reified V, reified Model : Any> setSectionAndNotify(position: Int, data: Model, extra: Any? = null) where V:View , V:GenericAdapterView<Model>{
        setSection<V,Model>(position, data, extra)
        notifyItemChanged(position)
    }



    inline fun<reified V, reified Model : Any> addSection(data: Model, extra: Any? = null) where V:View , V:GenericAdapterView<Model>{
        sections.add( Section( viewType<V>(), data, extra ) )
    }
    inline fun<reified V, reified Model : Any> addSectionAndNotify(data: Model, extra: Any? = null) where V:View , V:GenericAdapterView<Model>{
        addSection<V,Model>(data, extra)
        notifyItemInserted(sections.size - 1)
    }

    inline fun<reified V, reified Model : Any> addSection(position:Int, data: Model, extra: Any? = null) where V:View , V:GenericAdapterView<Model>{
        sections.add( position, Section( viewType<V>(), data, extra ) )
    }
    inline fun<reified V, reified Model : Any> addSectionAndNotify(position:Int, data: Model, extra: Any? = null) where V:View , V:GenericAdapterView<Model>{
        addSection<V,Model>(position, data, extra)
        notifyItemInserted(position)
    }


    fun clearData() {
        sections.clear()
    }

    fun clearDataAndNotify() {
        sections.clear()
        notifyDataSetChanged()
    }

    inline fun<reified V:View> clearViews(){
        val type = viewType<V>()
        sections.removeAll { it.type == type }
    }
    inline fun<reified V:View> clearSectionsWithTypeAndNotify(){
        clearViews<V>()
        notifyDataSetChanged()
    }
}