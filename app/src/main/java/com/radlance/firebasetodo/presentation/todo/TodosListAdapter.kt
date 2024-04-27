package com.radlance.firebasetodo.presentation.todo

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.radlance.firebasetodo.R
import com.radlance.firebasetodo.databinding.ItemTodoActiveBinding
import com.radlance.firebasetodo.domain.entity.Todo

class TodosListAdapter : RecyclerView.Adapter<TodosListAdapter.TodosListViewHolder>() {
    var todosList = listOf<Todo>()
        set(value) {
            val callback = TodosListDiffCallback(todosList, value)
            val diffResult = DiffUtil.calculateDiff(callback)
            diffResult.dispatchUpdatesTo(this)
            field = value
        }

    var onCompleteClickListener: ((Todo) -> Unit)? = null
    var onRemoveClickListener: ((Todo) -> Unit)? = null
    var onTodoClickListener: ((Todo) -> Unit)? = null
    class TodosListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemTodoActiveBinding.bind(itemView)
        fun bind(todo: Todo) {
            binding.tvTodo.text = todo.title
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (todosList[position].isCompleted == true) {
            VIEW_TYPE_COMPLETED
        } else {
            VIEW_TYPE_ACTIVE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodosListViewHolder {
        val layout = when (viewType) {
            VIEW_TYPE_ACTIVE -> R.layout.item_todo_active
            VIEW_TYPE_COMPLETED -> R.layout.item_todo_completed


            else -> throw RuntimeException("Unknown viewType $viewType")
        }
        val view =
            LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return TodosListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return todosList.size
    }

    override fun onBindViewHolder(holder: TodosListViewHolder, position: Int) {
        val todo = todosList[position]
        holder.binding.ivCheck.setOnClickListener {
            onCompleteClickListener?.invoke(todo)
        }
//        holder.itemView.setOnClickListener {
//            onRemoveClickListener?.invoke(todo)
//        }
//        holder.itemView.setOnClickListener {
//            onTodoClickListener?.invoke(todo)
//        }
        holder.bind(todo)
    }

    companion object {
        private const val VIEW_TYPE_ACTIVE = 1
        private const val VIEW_TYPE_COMPLETED = 0
    }
}