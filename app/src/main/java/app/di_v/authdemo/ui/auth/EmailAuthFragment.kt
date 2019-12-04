package app.di_v.authdemo.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import app.di_v.authdemo.R
import app.di_v.authdemo.data.model.FormType
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.email_auth_fragment.view.*

class EmailAuthFragment : Fragment() {

    companion object {
        fun newInstance() = EmailAuthFragment()
    }

    private lateinit var viewModel: AuthViewModel
    private lateinit var emailForm: TextInputLayout
    private lateinit var passwordForm: TextInputLayout
    private lateinit var emailEditText: TextInputEditText
    private lateinit var passwordEditText: TextInputEditText
    private lateinit var btn: Button
    private lateinit var progress: ProgressBar
    private lateinit var TYPE: FormType

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.email_auth_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        emailForm = view.findViewById(R.id.auth_email_layout)
        passwordForm = view.findViewById(R.id.auth_password_layout)
        emailEditText = view.findViewById(R.id.auth_email_edit_text)
        passwordEditText = view.findViewById(R.id.auth_password_edit_text)
        btn = view.auth_btn
        progress = view.auth_progressbar
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AuthViewModel::class.java)
        TYPE = viewModel.authenticatedResultLiveData.value?.type ?: FormType.NEXT

        when (TYPE) {
            FormType.NEXT -> btn.text = getString(R.string.auth_action_next)
            FormType.LOGIN -> btn.text = getString(R.string.auth_action_login)
            FormType.REGISTER -> btn.text = getString(R.string.auth_action_create)
        }

        btn.setOnClickListener {
            btn.visibility = View.GONE
            progress.visibility = View.VISIBLE
            when(TYPE) {
                FormType.NEXT -> viewModel.verificationEmail(emailEditText.text.toString())
                FormType.LOGIN -> viewModel.signInWithEmail(emailEditText.text.toString(), passwordEditText.text.toString())
                FormType.REGISTER -> viewModel.createUserWithEmail(emailEditText.text.toString(), passwordEditText.text.toString())
            }

        }

        viewModel.authenticatedResultLiveData.observe(this, Observer { data ->
            TYPE = data.type

            when (data.type) {
                FormType.NEXT -> btn.text = getString(R.string.auth_action_next)
                FormType.LOGIN -> btn.text = getString(R.string.auth_action_login)
                FormType.REGISTER -> btn.text = getString(R.string.auth_action_create)
            }
            emailForm.isEnabled = false
            passwordForm.visibility = View.VISIBLE
            progress.visibility = View.GONE
            btn.isEnabled = true
            btn.visibility = View.VISIBLE
        })

        viewModel.formState.observe(this, Observer { formState ->
            emailForm.error = formState.emailError?.let { it1 -> getString(it1) }
            passwordForm.error = formState.passwordError?.let { it1 -> getString(it1) }
            btn.isEnabled = formState.isDataValid
        })

        emailEditText.doAfterTextChanged { email ->
            viewModel.isEmailValid(email.toString())
        }

        passwordEditText.doAfterTextChanged { password ->
            viewModel.isPasswordValid(password.toString())
        }
    }
}