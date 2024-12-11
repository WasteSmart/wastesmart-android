package com.frxcl.wastesmart.ui.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import com.frxcl.wastesmart.R
import com.frxcl.wastesmart.data.remote.response.QuizItem
import com.frxcl.wastesmart.databinding.QuizResultListBinding

class QuizResultListAdapter(
    private val questions: List<QuizItem>,
    private val userAnswers: List<String>
) : RecyclerView.Adapter<QuizResultListAdapter.QuizViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizViewHolder {
        val binding = QuizResultListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return QuizViewHolder(binding)
    }

    override fun onBindViewHolder(holder: QuizViewHolder, position: Int) {
        val question = questions[position]
        holder.bind(question, userAnswers[position])
    }

    override fun getItemCount(): Int = questions.size

    inner class QuizViewHolder(private val binding: QuizResultListBinding) : RecyclerView.ViewHolder(binding.root) {
        private val questionText = binding.textViewQuestion
        private val radioButtons: List<RadioButton> = listOf(
            itemView.findViewById(R.id.option1),
            itemView.findViewById(R.id.option2),
            itemView.findViewById(R.id.option3),
            itemView.findViewById(R.id.option4)
        )

        fun bind(q: QuizItem, userAnswer: String) {
            questionText.text = q.question
            val options = q.options

            for (i in radioButtons.indices) {
                radioButtons[i].text = options!![i]!!.text
                if (userAnswer == options[i]!!.text) {
                    if (userAnswer == q.answer!!.text) {
                        radioButtons[i].setTextColor(Color.GREEN)
                        radioButtons[i].isChecked = true
                    } else {
                        radioButtons[i].setTextColor(Color.RED)
                        radioButtons[i].isChecked = true
                    }
                } else {
                    if (radioButtons[i].text == q.answer!!.text) {
                        radioButtons[i].setTextColor(Color.GREEN)
                    }
                }
            }
        }
    }
}