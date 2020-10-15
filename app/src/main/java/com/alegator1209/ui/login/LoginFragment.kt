package com.alegator1209.ui.login

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.alegator1209.R
import com.alegator1209.interactors.LoginUseCase
import com.alegator1209.utils.Result
import kotlinx.android.synthetic.main.login_fragment.*

class LoginFragment : Fragment() {

    companion object {
        fun newInstance(onLogin: (LoginUseCase) -> Unit = {}): LoginFragment {
            val fragment = LoginFragment()
            fragment.onLogin = onLogin
            return fragment
        }
    }

    private lateinit var viewModel: LoginViewModel
    private lateinit var onLogin: (LoginUseCase) -> Unit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val inflater = TransitionInflater.from(requireContext())
        exitTransition = inflater.inflateTransition(R.transition.slide_to_top)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.login_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        login.setOnClickListener(this::login)

        setRefreshing(viewModel.refreshing.value ?: false)
        viewModel.refreshing.observe(viewLifecycleOwner, Observer { setRefreshing(it) })

        viewModel.loginResult.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Result.Success -> onLogin(viewModel.loginUseCase)
                is Result.Failure -> shortToast(getString(R.string.login_failed))
            }
        })

        with(username) {
            setText(viewModel.username)
            addTextChangedListener {
                viewModel.username = it.toString()
            }
        }

        with(password) {
            setText(viewModel.password)
            addTextChangedListener {
                viewModel.password = it.toString()
            }
        }
    }

    private fun setRefreshing(b: Boolean) {
        loading.visibility = if (b) View.VISIBLE else View.GONE
    }

    @Suppress("UNUSED_PARAMETER")
    fun login(view: View) {
        if (!viewModel.canLogin) shortToast(getString(R.string.login_failed))
        else viewModel.login()
    }

    private fun shortToast(msg: String) = Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
}