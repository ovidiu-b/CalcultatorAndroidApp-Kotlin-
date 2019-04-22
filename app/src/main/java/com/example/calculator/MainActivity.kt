package com.example.calculator

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import java.lang.NumberFormatException

import kotlinx.android.synthetic.main.activity_main.*

private const val TAG_OPERAND = "TagOperand"
private const val TAG_PENDING_OPERATION = "TagPendingOperation"

class MainActivity : AppCompatActivity() {

    // Variables to hold the operands and type of calculation
    private var operand: Double? = null
    private lateinit var pendingOperation: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val dataInputButtons =
            arrayOf(button0, button1, button2, button3, button4, button5, button6, button7, button8, button9, buttonDot)

        for (button in dataInputButtons) button.setOnClickListener {
            val buttonPressed = it as Button

            newNumber.append(buttonPressed.text)
        }

        val operationButtons = arrayOf(buttonEquals, buttonDivide, buttonMultiply, buttonMinus, buttonPlus)

        for (button in operationButtons) button.setOnClickListener {
            val op = (it as Button).text.toString()

            try {
                val value = newNumber.text.toString().toDouble()
                performOperation(value, op)
            } catch (e: NumberFormatException) {
                newNumber.setText("")
            }

            pendingOperation = op
            operation.text = pendingOperation
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)

        operand = savedInstanceState?.getDouble(TAG_OPERAND) ?: 0.0
        result.setText(operand.toString())

        pendingOperation = savedInstanceState?.getString(TAG_PENDING_OPERATION) ?: "="
        operation.text = pendingOperation
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        outState?.putDouble(TAG_OPERAND, result.text.toString().toDouble())
        outState?.putString(TAG_PENDING_OPERATION, operation.text.toString())
    }

    private fun performOperation(value: Double, operation: String) {
        if (operand == null) {
            operand = value
        } else {
            if (pendingOperation == "=") pendingOperation = operation

            when (pendingOperation) {
                "=" -> operand = value
                "/" -> operand = if (value == 0.0) Double.NaN else operand!! / value
                "*" -> operand = operand!! * value
                "-" -> operand = operand!! - value
                "+" -> operand = operand!! + value
            }
        }

        result.setText(operand.toString())
        newNumber.setText("")
    }
}
