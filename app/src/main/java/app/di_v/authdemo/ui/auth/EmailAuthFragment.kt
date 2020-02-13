package app.di_v.authdemo.ui.auth

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import app.di_v.authdemo.R
import app.di_v.authdemo.data.model.FormType
import kotlinx.android.synthetic.main.email_auth_fragment.*

class EmailAuthFragment : DialogFragment() {

    companion object {
        fun newInstance(supportFragmentManager: FragmentManager): EmailAuthFragment {
            val dialog = EmailAuthFragment()
            dialog.show(supportFragmentManager, "auth dialog")
            return dialog
        }
    }

    override fun onStart() {
        super.onStart()
        val mDialog = dialog
        if(mDialog != null) {
            val size = ViewGroup.LayoutParams.MATCH_PARENT
            mDialog.window!!.setLayout(size, size)
            mDialog.window!!.setWindowAnimations(R.style.AppTheme_Slide)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppTheme_FullScreenDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.email_auth_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel = ViewModelProvider(this).get(EmailAuthViewModel::class.java)

        viewModel.formState.observe(viewLifecycleOwner, Observer { formState ->
            auth_email_layout.error = formState.emailError?.let { getString(it) }
            auth_password_layout.error = formState.passwordError?.let { getString(it) }
            loading(formState.loading)
            when (formState.type) {
                FormType.NEXT -> setForm(R.string.auth_action_next, null, View.VISIBLE, View.GONE, View.INVISIBLE)
                FormType.LOGIN -> setForm(R.string.auth_action_login, auth_email_edit_text.text, View.GONE, View.VISIBLE, View.VISIBLE)
                FormType.REGISTER -> setForm(R.string.auth_action_create,auth_email_edit_text.text, View.GONE, View.VISIBLE, View.VISIBLE)
                FormType.SUCCESS -> activity!!.finish()
            }
        })

        auth_toolbar.setNavigationOnClickListener {
            dismiss()
        }

        auth_btn.setOnClickListener {
            viewModel.action(email = auth_email_edit_text.text.toString(),
                password = auth_password_edit_text.text.toString())
        }
    }

    private fun loading(load: Boolean) {
        if (load) {
            auth_btn.visibility = View.GONE
            auth_progressbar.visibility = View.VISIBLE
        } else {
            auth_btn.visibility = View.VISIBLE
            auth_progressbar.visibility = View.GONE
        }
    }

    private fun setForm(
        btnText: Int,
        infoText: Editable?,
        emailVisibility: Int,
        passwordVisibility: Int,
        passwordResetVisibility: Int
    ) {
        auth_btn.text = getString(btnText)
        auth_info.text = infoText
        auth_email_layout.visibility = emailVisibility
        auth_password_layout.visibility = passwordVisibility
        auth_password_reset.visibility = passwordResetVisibility
    }
}