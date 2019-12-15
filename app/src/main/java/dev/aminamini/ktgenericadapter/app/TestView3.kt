package dev.aminamini.ktgenericadapter.app

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import dev.aminamini.ktgenericadapter.GenericAdapterView
import kotlinx.android.synthetic.main.view_test3.view.*

class TestView3 @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr), GenericAdapterView<TestModel> {



    init {
        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.view_test3, this, true)
    }


    @SuppressLint("SetTextI18n")
    override fun onBind(model: TestModel, position: Int, extraObject: Any?) {
        foo.text = "${model.a} position : $position"
        bar.text = model.b
    }
}