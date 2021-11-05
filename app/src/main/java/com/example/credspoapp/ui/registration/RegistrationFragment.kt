package com.example.credspoapp.ui.registration

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.credspoapp.R
import com.example.credspoapp.databinding.FragmentRegistrationBinding
import com.example.credspoapp.models.RegisterUserbody
import com.example.credspoapp.models.RegisterVerificationBody
import com.example.credspoapp.ui.login.setToggler
import java.util.*

class RegistrationFragment: Fragment() {

    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: RegistrationViewModel

    private var errorMessage: String = ""

    private lateinit var arrayOfYearStrings: MutableList<String?>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this).get(RegistrationViewModel::class.java)
        //views etc.
        binding.backButtonRegisterFragment.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.registerPasswordToggleButton.setOnClickListener {
            setToggler(binding.registerPasswordToggleButton, binding.passwordEditText, binding.root.context)
        }
        binding.repeatRegisterPasswordToggleButton.setOnClickListener {
            setToggler(binding.repeatRegisterPasswordToggleButton, binding.repeatPasswordEditText,requireContext())
        }
        binding.createAnAccountButtonRegister.setOnClickListener{
            createAnAccountOnClick()
        }
        //adding year for the selector ( autoCompleteTextView {within TextInputLayout} )
        populateYearsList();
        populateDropdownMenu(binding.root, R.layout.dropdown_item, arrayOfYearStrings, R.id.birtYearAutoCompleteTextView)
        populateDropdownMenu(binding.root, R.layout.dropdown_item, Locale.getISOCountries().toMutableList(), R.id.countryAutoCompleteTextView)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.registrationStatus.observe(viewLifecycleOwner, {
            it?.let {
                if( it ){
                    //after register call is done during onClick check here if it was a success
                    //if success send mail and password to verification fragment
                    //navigate and than do VERIFY call

                    Log.e("REGISTER_OBSERVER", "USER SUCCESSFULLY REGISTERED")

                    val valAction = RegistrationFragmentDirections.actionRegistrationFragmentToVerificationFragment()
                    val validatoinAction = RegistrationFragmentDirections.actionRegistrationFragmentToVerificationFragment(
                        binding.emailTextInputEditTextRegister.text.toString(),
                        binding.passwordEditText.text.toString()
                    )
                    findNavController().navigate(validatoinAction)
                }
                else{
                    Log.e("REGISTER_OBSERVER", "USER NOT REGISTERED")
                }
            }
        })
    }
    private fun createAnAccountOnClick(){
        if ( isUserValid() ){
            binding.errorMessageTextView.visibility = GONE
            //register the user
            val registerUserBody = RegisterUserbody(
                email = binding.emailTextInputEditTextRegister.text.toString(),
                firstName = binding.firstNameEditText.text.toString(),
                lastName = binding.lastNameEditText.text.toString(),
                password = binding.passwordEditText.text.toString(),
                repeatPassword = binding.repeatPasswordEditText.text.toString()
            )
            viewModel.registerUser(registerUserBody)
        }
        else {
            //Show Error othewise
            binding.errorMessageTextView.text = errorMessage
            binding.errorMessageTextView.visibility = VISIBLE
            binding.root.smoothScrollTo(0,binding.root.bottom)
        }
    }

    private fun isUserValid():Boolean{

        var validUser:Boolean = false
        errorMessage = ""
        if( binding.firstNameEditText.text.toString().length < 2 ){
            errorMessage = "Your First Name should contain at least 2 letters."
        }
        if( binding.lastNameEditText.text.toString().length < 2 ){
            if (errorMessage != "")
                errorMessage += "\n"
            errorMessage += "Your Last Name should contain at least 2 letters."
        }
        if( !android.util.Patterns.EMAIL_ADDRESS.matcher(binding.emailTextInputEditTextRegister.text.toString()).matches()){
            if (errorMessage != "")
                errorMessage += "\n"
            errorMessage += "Email is not valid."
        }
        if( binding.passwordEditText.text.toString().length < 8 ){
            if (errorMessage != "")
                errorMessage += "\n"
            errorMessage += "Password needs to  be at lest 8 characters long."
        }
        else {
            if (binding.passwordEditText.text.toString() != binding.repeatPasswordEditText.text.toString()) {
                if (errorMessage != "")
                    errorMessage += "\n"
                errorMessage += "Passwords are not matching"
            }
        }
        if ( !binding.checkBoxPolicy.isChecked ){
            if (errorMessage != "")
                errorMessage += "\n"
            errorMessage += "Please accept Policy and Privacy"
        }

        if( errorMessage.isEmpty() )
            validUser = true

        return validUser
    }

    private fun populateDropdownMenu(fragment: View, layout: Int, list:MutableList<String?>, layoutAutoCompleteText: Int ){
        val arrayAdapter = ArrayAdapter( requireContext(), layout, list.toTypedArray() )
        val autoCompleteTextView = fragment.findViewById<AutoCompleteTextView>(layoutAutoCompleteText)
        autoCompleteTextView.setAdapter(arrayAdapter)
    }

    private fun populateYearsList(){
        arrayOfYearStrings = mutableListOf()

        val startYear = 1900
        val endYear = 2021
        for( i in endYear downTo startYear)
            arrayOfYearStrings.add(i.toString())
    }
}