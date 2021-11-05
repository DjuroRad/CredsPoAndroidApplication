package com.example.credspoapp.ui.login

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import android.widget.TextView
import android.widget.EditText
import android.widget.TextSwitcher
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.credspoapp.R
import com.example.credspoapp.models.UserResponse
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragmet_login_page.*


class LoginFragment: Fragment() {

    private lateinit var toggleView: TextSwitcher
    private lateinit var passwordEditTextInputView: EditText
    private lateinit var loginButton: TextView
    private lateinit var errorText: TextView
    private lateinit var createAnAccountButton: TextView
    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragment_login = inflater.inflate(R.layout.fragmet_login_page, container, false)

        toggleView = fragment_login.findViewById(R.id.login_password_toggle_button)
        passwordEditTextInputView = fragment_login.findViewById(R.id.passwordEditText)
        loginButton = fragment_login.findViewById<TextView>(R.id.loginButton)
        errorText = fragment_login.findViewById(R.id.errorMessageTextView)
        createAnAccountButton = fragment_login.findViewById(R.id.createAnAccountTextView)

        toggleView.setOnClickListener{
            setToggler(toggleView, passwordEditTextInputView, requireContext())
        }

        loginButton.setOnClickListener{
            setLoginButtonOnClickListener()
        }

        createAnAccountButton.setOnClickListener(){
            findNavController().navigate(R.id.action_loginFragment_to_registrationFragment)
        }

        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        return fragment_login;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loginStatus.observe(viewLifecycleOwner, {
            it?.let {   loginStatusState ->
                if(loginStatusState){
                    Log.e("LOGIN STATUS STATE","loginsStatus = $loginStatusState")
                    findNavController().navigate(R.id.action_loginFragment_to_mainMenuFragment)
                }
                else{
                    Log.e("LOGIN STATUS STATE","loginsStatus = $loginStatusState")
                    errorText.visibility = android.view.View.VISIBLE
                }
            }
        })

        viewModel.userResponseLiveData.observe(viewLifecycleOwner,{ userResponse ->
            userResponse?.let {
                saveToSharedPreferences(requireContext(), it)
                Log.e("USER RESPONSE LIVE DATA", "Username = ${userResponse.user.lastName}")
            }
        })

        viewModel.verificationStatus.observe(viewLifecycleOwner, {
            it?.let{
                if(it){
                    findNavController().navigate(R.id.action_loginFragment_to_verificationFragment)
                }
            }
        })
    }

    private fun setLoginButtonOnClickListener(){
        val email:String = emailTextInputEditText.text.toString()
        val password:String = passwordEditText.text.toString()
        viewModel.bearerToken(email = email,password = password);
    }
}

// SOME GENERAL FUNCTIONS
fun saveToSharedPreferences(context: Context, userResponse: UserResponse){
    val sp = context.getSharedPreferences("USER_PREFS", Context.MODE_PRIVATE)
    val userJsonFormat = Gson().toJson(userResponse.user)
    val uri = Gson().toJson(Uri.EMPTY)
    with(sp.edit()){
        putString("USER", userJsonFormat)
        putString("TOKEN", userResponse.accessToken)
        putString("URI", uri)
        apply()
    }
}
fun setToggler(toggleViewTextSwitcher: TextSwitcher, passwordEditTextContentSwitcher: EditText, context: Context){
    val inAnim = AnimationUtils.loadAnimation(context, android.R.anim.fade_in)
    inAnim.duration = 150
    val outAnim = AnimationUtils.loadAnimation(context, android.R.anim.fade_out)
    outAnim.duration = 150

    toggleViewTextSwitcher.inAnimation = inAnim
    toggleViewTextSwitcher.outAnimation = outAnim

    val tv: TextView = toggleViewTextSwitcher.currentView as TextView
    if(tv.text.toString() == context.getString(R.string.show)){
        toggleViewTextSwitcher.setText(context.getString(R.string.hide))
        passwordEditTextContentSwitcher.transformationMethod = HideReturnsTransformationMethod.getInstance()
    }
    else{
        toggleViewTextSwitcher.setText(context.getString(R.string.show))
        passwordEditTextContentSwitcher.transformationMethod = PasswordTransformationMethod.getInstance()
    }
}