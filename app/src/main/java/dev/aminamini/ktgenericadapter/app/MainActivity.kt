package dev.aminamini.ktgenericadapter.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import dev.aminamini.ktgenericadapter.GenericRecyclerAdapter
import java.util.*

class MainActivity : AppCompatActivity() {

    private val adapter by lazy {
        val adapter = GenericRecyclerAdapter()
        adapter.provider { ctx -> TestView2(this) }


        val models1 = ArrayList<TestModel>()
        models1.add(TestModel("Model_1 foo", "Model_1 bar"))
        models1.add(TestModel("Model_1 foo", "Model_1 bar"))

        val models2 = ArrayList<TestModel>()
        models2.add(TestModel("Model_2 foo", "Model_2 bar"))
        models2.add(TestModel("Model_2 foo", "Model_2 bar"))
        models2.add(TestModel("Model_2 foo", "Model_2 bar"))
        models2.add(TestModel("Model_2 foo", "Model_2 bar"))

        val models3 = ArrayList<TestModel>()
        models3.add(TestModel("Model_3 foo", "Model_3 bar"))
        models3.add(TestModel("Model_3 foo", "Model_3 bar"))

        adapter.addSections<TestView, TestModel>(models1, this)
        adapter.addSections<TestView2, TestModel>(models2)
        adapter.addSections<TestView3, TestModel>(models3)

        adapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.adapter = adapter
    }
}
