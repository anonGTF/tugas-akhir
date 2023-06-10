package com.galih.makhrajulhuruf.base

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewbinding.ViewBinding
import com.galih.makhrajulhuruf.utils.Resource
import com.galih.makhrajulhuruf.utils.gone
import com.galih.makhrajulhuruf.utils.visible
import com.github.florent37.runtimepermission.PermissionResult
import com.github.florent37.runtimepermission.kotlin.askPermission

abstract class BaseActivity<VB: ViewBinding>: AppCompatActivity() {
    private var _binding: ViewBinding? = null
    abstract val bindingInflater: (LayoutInflater) -> VB

    @Suppress("UNCHECKED_CAST")
    protected val binding: VB
        get() = _binding as VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = bindingInflater.invoke(layoutInflater)
        setContentView(requireNotNull(_binding).root)
        setup()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    abstract fun setup()

    protected fun <T> setObserver(
        onSuccess: (Resource<T>) -> Unit,
        onError: (Resource<T>) -> Unit = {},
        onLoading: (Resource<T>) -> Unit = {},
        loadingItem: View? = null,
        swiperItem: SwipeRefreshLayout? = null
    ): Observer<in Resource<T>> {
        return Observer<Resource<T>> { data ->
            when(data) {
                is Resource.Success -> {
                    loadingItem?.gone()
                    swiperItem?.isRefreshing = false
                    onSuccess(data)
                }
                is Resource.Error -> {
                    swiperItem?.isRefreshing = false
                    loadingItem?.gone()
                    onError(data)
                }
                is Resource.Loading -> {
                    loadingItem?.visible()
                    onLoading(data)
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun goToActivity(
        actDestination: Class<*>,
        data: Bundle? = null,
        clearIntent: Boolean = false,
        isFinish: Boolean = false
    ) {

        val intent = Intent(this, actDestination)

        if (clearIntent) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        data?.let { intent.putExtras(data) }

        startActivity(intent)

        if (isFinish) {
            finish()
        }
    }

    fun goToActivityForResult(resultCode: Int, actDestination:  Class<*>, data: Bundle?) {
        val activityIntent = Intent(this, actDestination)

        data?.let { activityIntent.putExtras(data) }

        startActivityForResult(activityIntent, resultCode)
    }

    protected fun setupBackButton() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    protected fun setTitle(title: String) {
        supportActionBar?.title = title
    }

    protected fun askPermissions(vararg permissions: String, onAccepted: (PermissionResult) -> Unit) {
        askPermission(*permissions) {
            if (it.isAccepted) {
                onAccepted.invoke(it)
            }
        }.onDeclined { e ->
            if (e.hasDenied()) {
                e.denied.forEach { _ ->
                    AlertDialog.Builder(this)
                        .setMessage("Mohon menyetujui permintaan kami")
                        .setPositiveButton("Ya"){ _, _ ->
                            e.askAgain()
                        }
                        .setNegativeButton("Tidak"){ dialog, _ ->
                            dialog.dismiss()
                        }
                        .show()
                }
            }

            if (e.hasForeverDenied()) {
                e.foreverDenied.forEach { _ ->
                    e.goToSettings()
                }
            }
        }
    }

    protected fun hideSoftKeyboard() {
        currentFocus?.let {
            val inputMethodManager = ContextCompat.getSystemService(this, InputMethodManager::class.java)!!
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

    protected fun showToast(message: String) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

    fun getColorResource(resId: Int) = ContextCompat.getColor(this, resId)
}