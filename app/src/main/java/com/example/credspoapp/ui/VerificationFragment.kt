package com.example.credspoapp.ui

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.getSystemService
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavArgs
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.credspoapp.R
import com.example.credspoapp.databinding.FragmentRegistrationBinding
import com.example.credspoapp.databinding.FragmentVerificationBinding
import com.example.credspoapp.models.RegisterVerificationBody
import com.example.credspoapp.ui.login.saveToSharedPreferences
import com.example.credspoapp.ui.registration.RegistrationFragment
import com.example.credspoapp.ui.registration.RegistrationViewModel

class VerificationFragment: Fragment() {

    private var validationCode:String = "----"
    private var verificationCodeListEditText = mutableListOf<EditText>()
    private val args: VerificationFragmentArgs by navArgs()
    private lateinit var viewModel: RegistrationViewModel
    private var _binding: FragmentVerificationBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentVerificationBinding.inflate(inflater, container, false)

        verificationCodeListEditText.add(binding.firstNumberEditBox)
        verificationCodeListEditText.add(binding.secondNumberEditBox)
        verificationCodeListEditText.add(binding.thirdNumberEditBox)
        verificationCodeListEditText.add(binding.fourthNumberEditBox)
        //focus first box

        viewModel = ViewModelProvider(this).get(RegistrationViewModel::class.java)

        verificationCodeListEditText[0].requestFocus()

        setListener()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //OBSERVERS GO HERE!
        viewModel.validationStatus.observe(viewLifecycleOwner, {
            it?.let {
                if( it ){
                    binding.successfulVerificationTextView.text = getString(R.string.ver_success)
                    binding.successfulVerificationIcon.visibility = VISIBLE
                    binding.successfulVerificationTextView.visibility = VISIBLE
                }
                else{
                    binding.successfulVerificationTextView.text = getString(R.string.ver_code_fail)
                    binding.successfulVerificationTextView.visibility = VISIBLE
                    verificationCodeListEditText[0].requestFocus()
                    resetVerificationCode()
                }
            }
        })
        viewModel.userLiveData.observe(viewLifecycleOwner, {
            it?.let{
                saveToSharedPreferences(requireContext(),it)
                findNavController().navigate(R.id.action_verificationFragment_to_mainMenuFragment)
            }
        })
    }

    private fun setListener(){

        for( numberBox in 0 .. verificationCodeListEditText.size-2 ){
            verificationCodeListEditText[numberBox].doAfterTextChanged {
                if( it.toString().isNotEmpty() ) {
                    val newStr = validationCode.toCharArray()
                    newStr[numberBox] = it.toString()[0]
                    validationCode = String(newStr)
                    verificationCodeListEditText[numberBox + 1].requestFocus()
                }
            }
        }

        //for the alst edittext!
        verificationCodeListEditText[verificationCodeListEditText.size-1].doAfterTextChanged {
            if (it.toString().isNotEmpty() ) {
                val newStr = validationCode.toCharArray()
                newStr[newStr.size - 1] = it.toString()[0]
                validationCode = String(newStr)
                Log.e("verificatoin code:", validationCode)
                hideKeyboard()
                viewModel.verifyUser(RegisterVerificationBody(
                    email = args.email,
                    password = args.password,
                    validationCode
                ))
            }
        }
    }
    private fun resetVerificationCode(){
        for( numberBox in verificationCodeListEditText )
            numberBox.text.clear()
    }
}

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}
fun isNumber(s: String?): Boolean {
    return if (s.isNullOrEmpty()) false else s.all { Character.isDigit(it) }
}