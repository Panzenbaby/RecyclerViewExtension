package de.panzenbaby.recyclerviewextension

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import de.panzenbaby.ui.views.utils.RecyclerFooterHelper
import de.panzenbaby.ui.views.utils.RecyclerHeaderHelper

import kotlinx.android.synthetic.main.activity_main.*

private const val FAKE_LOADING_TIME: Long = 2000

class MainActivity : AppCompatActivity() {

    private var mHeaderHelper: RecyclerHeaderHelper = RecyclerHeaderHelper()
    private var mFooterHelper: RecyclerFooterHelper = RecyclerFooterHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = SimpleRecyclerAdapter()
        mHeaderHelper.attach(recyclerView, recyclerHeader)
        mFooterHelper.attach(recyclerView, recyclerFooter)

        val margin = resources.getDimension(R.dimen.general_margin)
        swipeRefresh.setProgressViewOffset(false, 0, (margin + recyclerHeader.layoutParams?.height!!).toInt())
        swipeRefresh.setOnRefreshListener {
            swipeRefresh.isRefreshing = true
            simulateDataLoading()
        }
    }

    private fun simulateDataLoading() {
        Handler().postDelayed({
            swipeRefresh.isRefreshing = false
            recyclerView.adapter?.notifyDataSetChanged()
        }, FAKE_LOADING_TIME)
    }

    class SimpleRecyclerAdapter : RecyclerView.Adapter<SimpleViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleViewHolder {
            val child = LayoutInflater.from(parent.context).inflate(R.layout.item_simple, parent, false)
            return SimpleViewHolder(child as TextView)
        }

        override fun onBindViewHolder(holder: SimpleViewHolder, position: Int) {
            holder.mTextView.text = "$position"
        }

        override fun getItemCount(): Int {
            return 20
        }
    }

    class SimpleViewHolder(view: TextView) : RecyclerView.ViewHolder(view) {

        var mTextView: TextView = view
    }
}