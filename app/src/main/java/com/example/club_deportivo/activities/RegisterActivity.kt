package com.example.club_deportivo.activities

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.widget.ImageView
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.card.MaterialCardView
import androidx.core.content.ContextCompat
import com.example.club_deportivo.R
import com.example.club_deportivo.models.InputConfig
import com.example.club_deportivo.models.MembershipType
import com.example.club_deportivo.ui.CustomButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class RegisterActivity : AppCompatActivity() {
    private lateinit var registerButton: CustomButton
    private lateinit var registerCancelButton: CustomButton
    private lateinit var memberCard: MaterialCardView
    private lateinit var noMemberCard: MaterialCardView
    private lateinit var memberIcon: ImageView
    private lateinit var noMemberIcon: ImageView
    private lateinit var memberIconBackground: View
    private lateinit var noMemberIconBackground: View
    private var selectedMembershipType: MembershipType? = null

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

    private fun setupInputs() {
        getInputConfig().forEach { config ->
            setupTextInput(config)
        }
    }

    private fun setupTextInput(config: InputConfig) {
        val inputLayout = findViewById<TextInputLayout>(config.layoutId)
        val editText = inputLayout.editText as? TextInputEditText

        inputLayout.hint = getString(config.hintStringId)
        editText?.inputType = config.inputType

        if (config.endIconMode != TextInputLayout.END_ICON_NONE) {
            inputLayout.endIconMode = config.endIconMode
        }
    }

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

    private fun setupMembershipSelector() {
        memberCard = findViewById(R.id.memberCard)
        noMemberCard = findViewById(R.id.noMemberCard)
        memberIcon = findViewById(R.id.memberIcon)
        noMemberIcon = findViewById(R.id.noMemberIcon)
        memberIconBackground = findViewById(R.id.memberIconBackground)
        noMemberIconBackground = findViewById(R.id.noMemberIconBackground)

        memberCard.setOnClickListener {
            selectMembershipType(MembershipType.MEMBER)
        }

        noMemberCard.setOnClickListener {
            selectMembershipType(MembershipType.NO_MEMBER)
        }
    }

    private fun selectMembershipType(membershipType: MembershipType) {
        selectedMembershipType = membershipType
        updateMembershipUI()
        updateButtonState()
    }

    private fun updateMembershipUI() {
        when (selectedMembershipType) {
            MembershipType.MEMBER -> {
                memberCard.setCardBackgroundColor(ContextCompat.getColor(this, R.color.primary_light))
                memberCard.strokeColor = ContextCompat.getColor(this, R.color.primary_main)
                memberIcon.visibility = View.VISIBLE
                memberIcon.imageTintList = ContextCompat.getColorStateList(this, R.color.white)
                memberIconBackground.visibility = View.VISIBLE

                noMemberCard.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white))
                noMemberCard.strokeColor = ContextCompat.getColor(this, R.color.primary_light)
                noMemberIcon.visibility = View.VISIBLE
                noMemberIcon.imageTintList = ContextCompat.getColorStateList(this, R.color.primary_main)
                noMemberIconBackground.visibility = View.INVISIBLE
            }
            MembershipType.NO_MEMBER -> {
                noMemberCard.setCardBackgroundColor(ContextCompat.getColor(this, R.color.primary_light))
                noMemberCard.strokeColor = ContextCompat.getColor(this, R.color.primary_main)
                noMemberIcon.visibility = View.VISIBLE
                noMemberIcon.imageTintList = ContextCompat.getColorStateList(this, R.color.white)
                noMemberIconBackground.visibility = View.VISIBLE

                memberCard.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white))
                memberCard.strokeColor = ContextCompat.getColor(this, R.color.primary_light)
                memberIcon.visibility = View.VISIBLE
                memberIcon.imageTintList = ContextCompat.getColorStateList(this, R.color.primary_main)
                memberIconBackground.visibility = View.INVISIBLE
            }
            null -> {
                memberCard.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white))
                memberCard.strokeColor = ContextCompat.getColor(this, R.color.primary_light)
                memberIcon.visibility = View.VISIBLE
                noMemberIcon.imageTintList = ContextCompat.getColorStateList(this, R.color.primary_main)
                memberIconBackground.visibility = View.INVISIBLE

                noMemberCard.setCardBackgroundColor(ContextCompat.getColor(this, R.color.white))
                noMemberCard.strokeColor = ContextCompat.getColor(this, R.color.primary_light)
                noMemberIcon.visibility = View.VISIBLE
                noMemberIconBackground.visibility = View.INVISIBLE
            }
        }
    }

    private fun updateButtonState() {
        if (validateInputs() && selectedMembershipType != null) {
            registerButton.enableButton()
        } else {
            registerButton.disableButton()
        }
    }

    private fun handleRegister() {
        val intent = Intent(this, SuccessfulRegistrationActivity::class.java)
        startActivity(intent)
        finish()
    }
}