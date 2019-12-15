package dev.aminamini.ktgenericadapter.app

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.widget.RelativeLayout
import dev.aminamini.ktgenericadapter.GenericAdapterView
import kotlinx.android.synthetic.main.view_test2.view.*

@SuppressLint("ViewConstructor")
class TestView2(val activity: MainActivity) : RelativeLayout(activity), GenericAdapterView<TestModel> {


    init {
        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.view_test2, this, true)
    }


    @SuppressLint("SetTextI18n")
    override fun onBind(model: TestModel, position: Int, extraObject: Any?) {
        foo.text = "${model.a} position : $position"
        bar.text = "${model.b} activityClass : ${activity.javaClass.name}"
    }
}