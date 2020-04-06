package com.example.nestedfragmentbottomsheetdialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_test_recycler_view.view.*
import kotlinx.android.synthetic.main.item_test.view.*

class TestRecyclerViewFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_test_recycler_view, container, false)


        view.testRecyclerView.adapter = TestAdapter().apply {
            this.list = (1..100).toList().map { it.toString() }
        }

        view.testRecyclerView.layoutManager =
            LinearLayoutManager(view.context, RecyclerView.VERTICAL, false)

        return view
    }

    class TestAdapter : RecyclerView.Adapter<TestViewHolder>() {

        var list: List<String> = emptyList()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_test, parent, false)
            return TestViewHolder(view)
        }

        override fun getItemCount(): Int = list.size

        override fun onBindViewHolder(holder: TestViewHolder, position: Int) {
            holder.bind(list[position])
        }

    }

    class TestViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(text: String) {
            view.testTextView.text = text
        }

    }
}