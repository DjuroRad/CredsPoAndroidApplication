package com.example.credspoapp.ui.profile

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.credspoapp.BuildConfig
import com.example.credspoapp.R
import com.example.credspoapp.databinding.FragmentProfileBinding
import com.example.credspoapp.models.User
import com.example.credspoapp.models.UserResponse
import com.example.credspoapp.ui.home.getBearerToken
import com.example.credspoapp.ui.home.getUser
import com.example.credspoapp.ui.login.LoginViewModel
import com.google.gson.Gson
import java.io.File

class ProfileFragment: Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private var bearerToken: String? = ""
    private lateinit var viewModel: ProfileViewModel
    private lateinit var user: User
    private var cameraPhotoUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        bearerToken = getBearerToken(requireContext())
        user = getUser(requireContext());

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val name_text: String = "${user.firstName} ${user.lastName}"
        binding.nameTextView.text = name_text
        binding.changePhotoImageView.setOnClickListener {
            showDialogFragment()
        }

        binding.logoutConstraintLayout.setOnClickListener{
            viewModel.logout(bearerToken = bearerToken)
        }

        binding.deleteAccountConstraintLayout.setOnClickListener {
            viewModel.deleteAccount(bearerToken)
        }


        viewModel.logoutStatus.observe(viewLifecycleOwner, {
            it?.let{
                if( it ){
                    val fm: FragmentManager? = activity?.supportFragmentManager
                    val count:Int? = fm?.backStackEntryCount
                    Log.e("count", "$count")

                    findNavController().navigate(R.id.action_mainMenuFragment_to_loginFragment)

                }
            }
        })

        viewModel.deleteAccountStatus.observe(viewLifecycleOwner, {
            it?.let {
                if( it ){
                    findNavController().navigate(R.id.action_mainMenuFragment_to_loginFragment)
                    activity?.supportFragmentManager?.popBackStack()
                }
            }
        })

        val uri = getPhotoUri()
        if( !Uri.EMPTY.equals(uri) ){
            binding.profilePhotoImageView.setImageURI(uri)
        }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun showDialogFragment(){
        val cameraDialogFragment = CameraPromptDialogFragment.newInstance( onCameraClickListener, onGalleryClickListener, "Change profile picture", "Do you want to take a photo or choose a photo from gallery", "Camera", "Gallery")
        cameraDialogFragment.show(childFragmentManager, "camera_dialog_fragment")
    }
    private val onGalleryClickListener = View.OnClickListener { selectImageFromGallery() }
    private val onCameraClickListener = View.OnClickListener { takeImage() }

    private fun selectImageFromGallery() = selectImageFromGalleryResult.launch("image/*")
    private val selectImageFromGalleryResult = registerForActivityResult(ActivityResultContracts.GetContent()){ uri ->
        uri?.let {
            binding.profilePhotoImageView.setImageURI(uri)
            saveUri(uri)
        }
    }

    private val takeImageResult = registerForActivityResult(ActivityResultContracts.TakePicture()){ success ->
        if(success){
            cameraPhotoUri?.let {
                binding.profilePhotoImageView.setImageURI(it)
                saveUri(it)
            }
        }
    }
    private fun saveUri(photoUri: Uri){
        val sp = requireContext().getSharedPreferences("USER_PREFS", Context.MODE_PRIVATE)
        with(sp.edit()){
            putString("URI", photoUri.toString())
            apply()
        }
    }
    private fun takeImage(){
        //make file luch
        cameraPhotoUri = getTemporaryFileUri()
        takeImageResult.launch(cameraPhotoUri)
    }

    private fun getTemporaryFileUri(): Uri{
        val tempFile = File.createTempFile("temp_file_profile_photo", ".png", requireActivity().cacheDir).apply {
            createNewFile()
            deleteOnExit()
        }

        return FileProvider.getUriForFile(
            requireContext().applicationContext,
            "${BuildConfig.APPLICATION_ID}.provider",
            tempFile
        )
    }


    private fun getPhotoUri(): Uri {
        val sp = requireContext().getSharedPreferences("USER_PREFS", Context.MODE_PRIVATE)
        val photo_uri = sp.getString("URI", "")
        val uri = Uri.parse(photo_uri)
        return uri
    }
}