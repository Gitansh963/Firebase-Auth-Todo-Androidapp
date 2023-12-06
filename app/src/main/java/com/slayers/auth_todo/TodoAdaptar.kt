package com.slayers.auth_todo

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.todoview.view.checkTodo
import kotlinx.android.synthetic.main.todoview.view.todoTitle


class TodoAdaptar (
        val todos: MutableList<Todo>, private val userId: String) : RecyclerView.Adapter<TodoAdaptar.TodoViewHolder>() {
        class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.todoview, parent, false)
            return TodoViewHolder(view)
        }
        override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
            val filteredTodos = todos.filter { it.userId == userId }
            val currentTodo = filteredTodos[position]

            if (currentTodo.userId == userId) {
                holder.itemView.apply {
                    todoTitle.text = currentTodo.title
                    checkTodo.isChecked = currentTodo.isChecked
                    toggleStrikeThrough(todoTitle, currentTodo.isChecked)
                    checkTodo.setOnCheckedChangeListener { _, isChecked ->
                        toggleStrikeThrough(todoTitle, isChecked)
                        currentTodo.isChecked = isChecked
                    }
                }
            }
        }

    fun addTodo(todo: Todo){
        todos.add(todo)
        notifyItemInserted(todos.size -1)
    }

    //    fun deleteDoneTodos(){
//        todos.removeAll{
//                todo -> todo.isChecked
//        }
//        notifyDataSetChanged()
//
//    }
    fun deleteTodo(position:Int) {
        val todo = todos[position]
        Log.d(ContentValues.TAG, "Deleting todo: ${todo.title}")
        todos.removeAt(position)
        notifyItemRemoved(position)
    }


    fun deleteDoneTodos(): String? {
        val doneTodos = todos.filter { it.isChecked }
        Log.d(ContentValues.TAG, doneTodos.toString())
        for (todo in doneTodos) {
            val position = todos.indexOf(todo)
            if (position != -1) {
                todos.removeAt(position)
                notifyItemRemoved(position)
                return todo.id
            }
        }
        return null
    }


    private fun toggleStrikeThrough(todoTitle: TextView, isChecked: Boolean){
        if(isChecked){
            todoTitle.paintFlags = todoTitle.paintFlags
        } else {
            todoTitle.paintFlags = todoTitle.paintFlags
        }
    }

//    override fun getItemCount(): Int {
//        return todos.size
////            .filter { it.userId == userId }.
//    }
    override fun getItemCount(): Int {
        return todos.filter { it.userId == userId }.size
    }
}