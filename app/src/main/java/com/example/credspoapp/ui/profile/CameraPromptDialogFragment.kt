package com.example.credspoapp.ui.profile

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.credspoapp.R

class CameraPromptDialogFragment: DialogFragment() {

    private var  onCameraClickListener: View.OnClickListener? = null
    private var  onGalleryClickListener: View.OnClickListener? = null
    private lateinit var title: String
    private lateinit var question: String
    private lateinit var answer1: String
    private lateinit var answer2: String

    companion object{
        fun newInstance(
            onCameraClickListener: View.OnClickListener?,
            onGalleryClickListener: View.OnClickListener?,
            title: String,
            question: String,
            answer1: String,
            answer2: String
        ): CameraPromptDialogFragment{
            val dialogFragment = CameraPromptDialogFragment()
            dialogFragment.onGalleryClickListener = onGalleryClickListener
            dialogFragment.onCameraClickListener = onCameraClickListener
            dialogFragment.title = title
            dialogFragment.question = question
            dialogFragment.answer1 = answer1
            dialogFragment.answer2 = answer2

            return dialogFragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val view = inflater.inflate(R.layout.camera_dialog_prompt, container, false)

        title?.let {
            val textView = view.findViewById<TextView>(R.id.cameraPromptTextView)
            textView.text = it
        }
        question?.let{
            val textView = view.findViewById<TextView>(R.id.questionTextView)
            textView.text = it
        }

        val cameraButton: TextView = view.findViewById(R.id.cameraButtonTextView)
        val galleryButton: TextView = view.findViewById(R.id.galleryButtonTextView)

        answer1?.let{
            cameraButton.text = it
        }
        answer2?.let{
            galleryButton.text = it
        }

        cameraButton.setOnClickListener{
            if( onCameraClickListener != null) {
                onCameraClickListener?.onClick(it)
                dismiss()
            }
            else
                dismiss()
        }
        galleryButton.setOnClickListener {
            if( onGalleryClickListener != null) {
                onGalleryClickListener?.onClick(it)
                dismiss()
            }
            else
                dismiss()
        }
        return view
    }
}
