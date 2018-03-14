package de.panzenbaby.ui.views.utils

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View

class RecyclerFooterHelper {

    private var mDecoration: FooterItemDecoration? = null
    private var mScroller: FooterScroller? = null

    /**
     * Attach the given view as a footer to the given recycler view.
     *
     * @param recyclerView The recycler view which will get the footer view.
     * @param footerView The view which will be the footer of the recycler view.
     */
    fun attach(recyclerView: RecyclerView, footerView: View) {
        clear()

        mDecoration = FooterItemDecoration(footerView)
        mScroller = FooterScroller(footerView)

        recyclerView.addOnScrollListener(mScroller)
        recyclerView.addItemDecoration(mDecoration)

        mScroller?.onScrolled(recyclerView, 0, 0)
    }

    /**
     * This method can be useful if the data of the recycler view adapter changed e.g. if new items has been inserted
     * after the last item. In this case the item decoration has the be redrawn.
     *
     * @param recyclerView The {@link RecyclerView} to which this handler is attached to.
     */
    fun refreshItemDecoration(recyclerView: RecyclerView) {
        recyclerView.removeItemDecoration(mDecoration)
        recyclerView.addItemDecoration(mDecoration)
    }

    private fun clear() {
        mScroller?.clear()
        mDecoration?.clear()

        mScroller = null
        mDecoration = null
    }

    class FooterScroller(footerView: View) : RecyclerView.OnScrollListener() {

        private var mFooterView: View? = footerView

        override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val itemCount = recyclerView?.layoutManager?.itemCount ?: 0
            val lastChild = recyclerView?.layoutManager?.findViewByPosition(itemCount - 1)
            val maxDy = recyclerView?.height!!.toFloat()

            val dY = if (lastChild != null) {
                Math.min(lastChild.bottom.toFloat() - mFooterView?.top!!, maxDy)
            } else {
                maxDy
            }

            if (mFooterView?.translationY != dY) {
                mFooterView?.translationY = dY
            }
        }

        fun clear() {
            mFooterView = null
        }
    }

    class FooterItemDecoration(footerView: View) : RecyclerView.ItemDecoration() {

        private var mFooterView: View? = footerView

        override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
            super.getItemOffsets(outRect, view, parent, state)

            val itemCount = parent?.layoutManager?.itemCount ?: 0
            if (parent?.layoutManager?.findViewByPosition(itemCount - 1) == view) {
                outRect?.bottom = getFooterHeight()
            } else {
                outRect?.bottom = 0
            }
        }


        private fun getFooterHeight(): Int {
            var height = mFooterView?.layoutParams?.height ?: 0
            if (height <= 0) {
                mFooterView?.measure(0, 0)
                height = mFooterView?.measuredHeight!!
            }

            if (height <= 0) {
                height = mFooterView?.height!!
            }

            return height
        }

        fun clear() {
            mFooterView = null
        }
    }
}