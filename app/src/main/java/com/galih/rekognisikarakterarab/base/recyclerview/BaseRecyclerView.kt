package com.galih.rekognisikarakterarab.base.recyclerview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.recyclerview.widget.*
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.facebook.shimmer.ShimmerFrameLayout
import com.galih.rekognisikarakterarab.R
import com.galih.rekognisikarakterarab.databinding.*
import com.galih.rekognisikarakterarab.utils.removeItemDecorations

class BaseRecyclerView : FrameLayout {

    private lateinit var mRecyclerView: FrameLayout
    private lateinit var mEmptyView: FrameLayout
    private lateinit var mErrorView: FrameLayout
    private lateinit var mShimmerContainer: ShimmerFrameLayout

    private var mClipToPadding: Boolean = false
    private var mPadding: Int = 0
    private var mPaddingTop: Int = 0
    private var mPaddingBottom: Int = 0
    private var mPaddingLeft: Int = 0
    private var mPaddingRight: Int = 0
    private var mScrollbarStyle: Int = 0
    private var mScrollbar: Int = 0
    private var mLayoutShimmer: Int = 0

    lateinit var baseEmptyBinding: LayoutBaseEmptyViewBinding
    lateinit var baseErrorBinding: LayoutBaseErrorViewBinding
    lateinit var baseShimmerBinding: LayoutBaseShimmerBinding
    lateinit var baseRecyclerBinding: LayoutBaseRecyclerviewBinding

    private var viewShimmer: View? = null

    interface ReloadListener : OnClickListener

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initAttrs(attrs)
        initView()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        initAttrs(attrs)
        initView()
    }

    private fun initAttrs(attrs: AttributeSet) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.BaseRecyclerView)
        try {
            mClipToPadding = a.getBoolean(R.styleable.BaseRecyclerView_recyclerClipToPadding, false)
            mPadding = a.getDimension(R.styleable.BaseRecyclerView_recyclerPadding, -1.0f).toInt()
            mPaddingLeft =
                a.getDimension(R.styleable.BaseRecyclerView_recyclerPaddingLeft, 0.0f).toInt()
            mPaddingTop =
                a.getDimension(R.styleable.BaseRecyclerView_recyclerPaddingTop, 0.0f).toInt()
            mPaddingRight =
                a.getDimension(R.styleable.BaseRecyclerView_recyclerPaddingRight, 0.0f).toInt()
            mPaddingBottom =
                a.getDimension(R.styleable.BaseRecyclerView_recyclerPaddingBottom, 0.0f).toInt()
            mScrollbar = a.getInteger(R.styleable.BaseRecyclerView_scrollbars, -1)
            mScrollbarStyle = a.getInteger(R.styleable.BaseRecyclerView_scrollbarStyle, -1)
            mLayoutShimmer = a.getResourceId(R.styleable.BaseRecyclerView_layout_shimmer, R.layout.layout_shimmer_banner)

        } finally {
            a.recycle()
        }
    }

    private fun initView() {
        buildViews()

        // Configure the recycler view
        mRecyclerView.apply {
            clipToPadding = mClipToPadding
            if (mPadding != (-1.0f).toInt())
                setPadding(mPadding, mPadding, mPadding, mPadding)
            else
                setPadding(mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom)
            when (mScrollbar) {
                0 -> isVerticalScrollBarEnabled = false
                1 -> isHorizontalScrollBarEnabled = false
                2 -> {
                    isVerticalScrollBarEnabled = false
                    isHorizontalScrollBarEnabled = false
                }
            }
            if (mScrollbarStyle != -1) scrollBarStyle = mScrollbarStyle
        }
    }

    private fun buildViews() {
        mRecyclerView = FrameLayout(context)
        mEmptyView = FrameLayout(context)
        mErrorView = FrameLayout(context)
        mShimmerContainer = ShimmerFrameLayout(context)

        baseEmptyBinding =
            LayoutBaseEmptyViewBinding.inflate(LayoutInflater.from(context), mEmptyView, false)
        baseErrorBinding =
            LayoutBaseErrorViewBinding.inflate(LayoutInflater.from(context), mErrorView, false)
        baseShimmerBinding =
            LayoutBaseShimmerBinding.inflate(LayoutInflater.from(context), mShimmerContainer, false)
        baseRecyclerBinding = LayoutBaseRecyclerviewBinding.inflate(
            LayoutInflater.from(context),
            mRecyclerView,
            false
        )

        addView(baseEmptyBinding.root)
        addView(baseErrorBinding.root)
        addView(baseShimmerBinding.root)
        addView(baseRecyclerBinding.root)
    }

    fun setUpAsSwipeHorizontal() {
        baseRecyclerBinding.recyclerView.apply {
            setHasFixedSize(true)
            isNestedScrollingEnabled = false
            layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
        }
        LinearSnapHelper().attachToRecyclerView(baseRecyclerBinding.recyclerView)
    }

    fun setUpCustom(layManager: LinearLayoutManager, snapHelper: SnapHelper = LinearSnapHelper()) {
        baseRecyclerBinding.recyclerView.apply {
            setHasFixedSize(true)
            isNestedScrollingEnabled = false
            layoutManager = layManager
        }
        LinearSnapHelper().attachToRecyclerView(baseRecyclerBinding.recyclerView)
    }

    fun setUpAsListHorizontal() {
        baseRecyclerBinding.recyclerView.apply {
            setHasFixedSize(true)
            isNestedScrollingEnabled = false
            layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
        }
    }

    fun setUpAsList() {
        baseRecyclerBinding.recyclerView.apply {
            setHasFixedSize(true)
            isNestedScrollingEnabled = false
            layoutManager = LinearLayoutManager(context)
        }
    }

    fun setUpAsListInScroll() {
        baseRecyclerBinding.recyclerView.apply {
            setHasFixedSize(false)
            layoutManager = object : LinearLayoutManager(context) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
        }
    }

    fun setUpAsGrid(spanCount: Int,
                    spaceLeft: Int = 0,
                    spaceTop: Int = 0,
                    spaceRight: Int = 0,
                    spaceBottom: Int = 0
    ) {
        baseRecyclerBinding.recyclerView.removeItemDecorations()
        baseRecyclerBinding.recyclerView.apply {
            setHasFixedSize(true)
            isNestedScrollingEnabled = false
            layoutManager = GridLayoutManager(context, spanCount, GridLayoutManager.VERTICAL, false)
            addItemDecoration(SpaceItemDecoration(spaceLeft, spaceTop, spaceRight, spaceBottom))
        }
    }

    fun setUpAsGridInScroll(spanCount: Int) {
        baseRecyclerBinding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = object : GridLayoutManager(context, spanCount) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
        }
    }

    /**
     * Below are some methods for setting the RecyclerView attributes
     */

    fun setShimmer(layout: Int) {
        mLayoutShimmer = layout

    }

    fun enableSwipeRefresh(value: Boolean){
        baseRecyclerBinding.swipeRefresh.isRefreshing = value
        baseRecyclerBinding.swipeRefresh.isEnabled = value
    }

    var isRefresh: Boolean = false
    set(value) {
        baseRecyclerBinding.swipeRefresh.isRefreshing = value
    }

    fun getSwipeRefreshLayout(): SwipeRefreshLayout {
        return baseRecyclerBinding.swipeRefresh
    }

    fun setSwipeRefreshLoadingListener(listener: SwipeRefreshLayout.OnRefreshListener) {
        baseRecyclerBinding.swipeRefresh.setColorSchemeResources(
            R.color.colorPrimary,
            R.color.colorPrimaryDark,
            R.color.white
        )
        baseRecyclerBinding.swipeRefresh.setOnRefreshListener(listener)
    }

    fun releaseBlock() {
        baseRecyclerBinding.recyclerView.releaseBlock()
    }

    fun setLastPage() {
        baseRecyclerBinding.recyclerView.setLastPage()
    }

    fun setRecyclerViewPadding(left: Int, top: Int, right: Int, bottom: Int) {
        this.mPaddingLeft = left
        this.mPaddingTop = top
        this.mPaddingRight = right
        this.mPaddingBottom = bottom
        /*mRecyclerView.setPadding(mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom)*/
        baseRecyclerBinding.recyclerView.setPadding(mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom)
    }

    fun setRecyclerClipToPadding(clipToPadding: Boolean) {
        /*mClipToPadding = clipToPadding.also {
            mRecyclerView.clipToPadding = it
        }*/
        baseRecyclerBinding.recyclerView.clipToPadding = clipToPadding
    }

    fun setHasFixedSize(hasFixedSize: Boolean) {
        baseRecyclerBinding.recyclerView.setHasFixedSize(hasFixedSize)
    }

    fun setItemAnimator(animator: RecyclerView.ItemAnimator) {
        baseRecyclerBinding.recyclerView.itemAnimator = animator
    }

    fun addItemDecoration(decor: RecyclerView.ItemDecoration) {
        baseRecyclerBinding.recyclerView.addItemDecoration(decor)
    }

    fun addItemDecoration(decor: RecyclerView.ItemDecoration, index: Int) {
        baseRecyclerBinding.recyclerView.addItemDecoration(decor, index)
    }

    fun removeItemDecoration(decor: RecyclerView.ItemDecoration) {
        baseRecyclerBinding.recyclerView.removeItemDecoration(decor)
    }

    fun getAdapter(): RecyclerView.Adapter<*>? {
        return baseRecyclerBinding.recyclerView.adapter
    }

    fun setAdapter(adapter: RecyclerView.Adapter<*>?) {
        baseRecyclerBinding.recyclerView.adapter = adapter
    }

    fun setLayoutManager(layout: RecyclerView.LayoutManager) {
        baseRecyclerBinding.recyclerView.layoutManager = layout
    }

    fun setLoadingListener(listener: EndlessScrollCallback) {
        baseRecyclerBinding.recyclerView.setEndlessScrollCallback(listener)
    }

    fun setNestedOnScrollingEnabled(value: Boolean) {
        baseRecyclerBinding.recyclerView.isNestedScrollingEnabled = value
    }

    fun scrollToPosition(position: Int) {
        baseRecyclerBinding.recyclerView.scrollToPosition(position)
    }

    fun addOnScrollListener(scrollListener: RecyclerView.OnScrollListener) {
        baseRecyclerBinding.recyclerView.addOnScrollListener(scrollListener)
    }

    fun layoutManager() : LayoutManager? {
        return baseRecyclerBinding.recyclerView.layoutManager
    }

    /**
     * Below are some methods for managing layout visibility
     */
    private fun hideAll() {
        baseRecyclerBinding.contentRecyclerView.visibility = View.GONE
        baseEmptyBinding.contentEmpty.visibility = View.GONE
        baseErrorBinding.contentError.visibility = View.GONE
        //baseShimmerBinding.contentShimmer.visibility = View.GONE
        viewShimmer?.visibility = View.GONE
        mShimmerContainer.stopShimmer()
    }

    fun showRecycler() {
        hideAll()
        baseRecyclerBinding.contentRecyclerView.visibility = View.VISIBLE
    }

    fun showShimmer() {
        hideAll()
        //baseShimmerBinding.contentShimmer.visibility = View.VISIBLE
        mShimmerContainer.startShimmer()
        if (viewShimmer == null) {
            baseShimmerBinding.viewStub.layoutResource = mLayoutShimmer
            viewShimmer = baseShimmerBinding.viewStub.inflate()
        } else {
            viewShimmer?.visibility = View.VISIBLE
        }
        //dispatch touch
        viewShimmer?.setOnClickListener { _ -> }
    }

    fun stopShimmer() {
        if (mShimmerContainer.isShimmerStarted) {
            mShimmerContainer.stopShimmer()
            viewShimmer?.visibility = View.GONE
            //baseShimmerBinding.contentShimmer.visibility = View.GONE
        }
    }

    fun showEmpty(title: String, desc: String) {
        hideAll()
        baseEmptyBinding.contentEmpty.visibility = View.VISIBLE
        baseEmptyBinding.tvTitleEmptyView.text = title
        baseEmptyBinding.tvContentEmptyView.text = desc
    }

    fun showEmptyWithoutTitle(desc: String, image: Int? = R.drawable.img_search_not_found) {
        hideAll()
        baseEmptyBinding.contentEmpty.visibility = View.VISIBLE
        baseEmptyBinding.tvTitleEmptyView.visibility = View.GONE
        baseEmptyBinding.tvContentEmptyView.text = desc
        baseEmptyBinding.imgEmptyView.setImageResource(image!!)
    }

    fun hideEmpty() {
        baseEmptyBinding.contentEmpty.visibility = View.GONE
        showRecycler()
    }

    fun showError(title: String, desc: String) {
        hideAll()
        baseErrorBinding.contentError.visibility = View.VISIBLE
        baseErrorBinding.tvTitleErrorView.text = title
        baseErrorBinding.tvContentErrorView.text = desc
    }

    fun initialShimmer() {
        hideEmpty()
        showShimmer()
    }

    fun finishShimmer() {
        stopShimmer()
        showRecycler()
    }

    /**
     * Below are some methods for managing empty state & error state
     */

    fun setImageEmptyView(imageRes: Int) {
        baseEmptyBinding.imgEmptyView.setImageResource(imageRes)
    }

    fun setTitleEmptyView(text: String) {
        baseEmptyBinding.tvTitleEmptyView.text = text
    }

    fun setContentEmptyView(text: String) {
        baseEmptyBinding.tvContentEmptyView.text = text
    }

    fun showEmptyTitleView(status: Boolean) {
        when (status) {
            true -> baseEmptyBinding.tvTitleEmptyView.visibility = View.VISIBLE
            false -> baseEmptyBinding.tvTitleEmptyView.visibility = View.GONE
        }
    }

    /*fun setTextButtonEmptyView(text: String) {
        baseEmptyBinding.btnEmpty.text = text
    }

    fun setBackgroungEmptyButton(drawableRes: Int) {
        baseEmptyBinding.btnEmpty.setBackgroundResource(drawableRes)
    }

    fun setEmptyButtonListener(listener: ReloadListener) {
        baseEmptyBinding.btnEmpty.setOnClickListener(listener)
    }*/

    fun setImageErrorView(imageRes: Int) {
        baseErrorBinding.imgErrorView.setImageResource(imageRes)
    }

    fun setTitleErrorView(text: String) {
        baseErrorBinding.tvTitleErrorView.text = text
    }

    fun setContentErrorView(text: String) {
        baseErrorBinding.tvContentErrorView.text = text
    }

    fun showErrorTitleView(status: Boolean) {
        when (status) {
            true -> baseErrorBinding.tvTitleErrorView.visibility = View.VISIBLE
            false -> baseErrorBinding.tvTitleErrorView.visibility = View.GONE
        }
    }

    fun setTextButtonErrorView(text: String) {
        baseErrorBinding.btnError.text = text
    }

    fun setBackgroungErrorButton(drawableRes: Int) {
        baseErrorBinding.btnError.setBackgroundResource(drawableRes)
    }

    fun setErrorButtonListener(listener: ReloadListener) {
        baseErrorBinding.btnError.setOnClickListener(listener)
    }

    fun setReloadListener(listener: OnClickListener) {
        baseErrorBinding.btnError.setOnClickListener(listener)
    }

}
