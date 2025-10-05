package com.example.club_deportivo.activities

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.card.MaterialCardView
import com.example.club_deportivo.R
import com.example.club_deportivo.models.InputConfig
import com.example.club_deportivo.models.MembershipType
import com.example.club_deportivo.models.UserRepository
import com.example.club_deportivo.ui.CustomButton
import com.example.club_deportivo.ui.CustomMembershipSelector
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class RegisterActivity : AppCompatActivity() {
    private lateinit var registerButton: CustomButton
    private lateinit var registerCancelButton: CustomButton
    private lateinit var memberCard: MaterialCardView
    private lateinit var noMemberCard: MaterialCardView
    private var selectedMembershipType: MembershipType?

    init {
        selectedMembershipType = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        setupInputs()
        setupRealTimeValidation()
        setupMembershipSelector()

        registerButton = findViewById<CustomButton>(R.id.registerButton)
        registerCancelButton = findViewById<CustomButton>(R.id.registerCancelButton)

        updateButtonState()

        registerButton.setOnClickListener {
            handleRegister()
        }

        registerCancelButton.setOnClickListener {
            finish()
        }
    }

    /**
     * Configura las reglas de validación para todos los campos del formulario.
     */
    private fun getInputConfig() = listOf<InputConfig>(
        InputConfig(
            layoutId = R.id.fullNameInput,
            hintStringId = R.string.register_full_name_input
        ),
        InputConfig(
            layoutId = R.id.documentInput,
            hintStringId = R.string.register_document_input,
            minLength = 8,
            customValidator = { document ->
                document.length == 8 && document.all { it.isDigit() }
            }
        ),
        InputConfig(
            layoutId = R.id.emailInput,
            hintStringId = R.string.register_email_input,
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS,
            customValidator = { email ->
                android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
            }
        ),
        InputConfig(
            layoutId = R.id.passwordInput,
            hintStringId = R.string.register_password_input,
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD,
            endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE,
            minLength = 8
        )
    )

    /**
     * Inicializa todos los campos de entrada del formulario.
     */
    private fun setupInputs() {
        getInputConfig().forEach { config ->
            setupTextInput(config)
        }
    }

    /**
     * Configura un campo de entrada individual según su configuración.
     */
    private fun setupTextInput(config: InputConfig) {
        val inputLayout = findViewById<TextInputLayout>(config.layoutId)
        val editText = inputLayout.editText as? TextInputEditText

        inputLayout.hint = getString(config.hintStringId)
        editText?.inputType = config.inputType

        if (config.endIconMode != TextInputLayout.END_ICON_NONE) {
            inputLayout.endIconMode = config.endIconMode
        }
    }

    /**
     * Valida todos los campos del formulario según sus reglas definidas.
     */
    private fun validateInputs(): Boolean {
        return getInputConfig().all { config ->
            val inputLayout = findViewById<TextInputLayout>(config.layoutId)
            val text = inputLayout.editText?.text.toString().trim()

            when {
                config.isRequired && text.isEmpty() -> false
                text.isNotEmpty() && text.length < config.minLength -> false
                config.customValidator != null && text.isNotEmpty() && !config.customValidator.invoke(text) -> false
                else -> true
            }
        }
    }

    /**
     * Configura la validación en tiempo real para todos los campos.
     */
    private fun setupRealTimeValidation() {
        getInputConfig().forEach { config ->
            val inputLayout = findViewById<TextInputLayout>(config.layoutId)
            val editText = inputLayout.editText

            editText?.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    updateButtonState()
                }
            })
        }
    }

    /**
     * Inicializa el selector de membresía y configura los callbacks.
     */
    private fun setupMembershipSelector() {
        memberCard = findViewById(R.id.memberCard)
        noMemberCard = findViewById(R.id.noMemberCard)

        CustomMembershipSelector.setup(
            memberCard = memberCard,
            noMemberCard = noMemberCard,
            onSelectionChanged = { membershipType ->
                selectedMembershipType = membershipType
                updateButtonState()
            }
        )
    }

    /**
     * Actualiza el estado del botón de registro según las validaciones.
     */
    private fun updateButtonState() {
        if (validateInputs() && selectedMembershipType != null) {
            registerButton.enableButton()
        } else {
            registerButton.disableButton()
        }
    }

    /**
     * Procesa el registro del usuario creando una nueva cuenta y navegando al siguiente paso.
     */
    private fun handleRegister() {
        val fullName = getInputValue(R.id.fullNameInput)
        val document = getInputValue(R.id.documentInput)
        val email = getInputValue(R.id.emailInput)
        val password = getInputValue(R.id.passwordInput)

        if (!validateEmailUniqueness(email)) {
            return
        }
        
        // Create new user with selected membership
        val newUser = UserRepository.createNewClient(
            fullName,
            document,
            email,
            password,
            membershipType = selectedMembershipType!!
        )

        val intent = Intent(this, SuccessfulRegistrationActivity::class.java)
        intent.putExtra(BaseAuthActivity.LOGGED_USER_ID_KEY, newUser.id)
        startActivity(intent)
        finish()
    }
    
    /**
     * Valida que el email no esté ya registrado.
     */
    private fun validateEmailUniqueness(email: String): Boolean {
        if (UserRepository.emailExists(email)) {
            findViewById<TextInputLayout>(R.id.emailInput).error = getString(R.string.register_email_already_exists)
            return false
        }
        return true
    }
    
    /**
     * Obtiene el valor de texto de un campo de entrada específico.
     */
    private fun getInputValue(layoutId: Int): String {
        return findViewById<TextInputLayout>(layoutId).editText?.text.toString().trim()
    }
}