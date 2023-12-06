package com.slayers.auth_todo

import android.content.ContentValues
import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.Add
import kotlinx.android.synthetic.main.activity_main.Delete
import kotlinx.android.synthetic.main.activity_main.editTextText
import kotlinx.android.synthetic.main.activity_main.rvTodoItems

class MainActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var todoAdapter: TodoAdaptar
    private lateinit var todoList: MutableList<Todo>
    var currentUser = FirebaseAuth.getInstance().currentUser
    var userId = currentUser?.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        database = FirebaseDatabase.getInstance().reference.child("todos")
        todoList = mutableListOf()
        todoAdapter = TodoAdaptar(todoList, userId.toString())
        rvTodoItems.adapter = todoAdapter
        rvTodoItems.layoutManager = LinearLayoutManager(this)

        Add.setOnClickListener {
            val todoTitle2 = editTextText.text.toString()
            if(todoTitle2.isNotEmpty()){
                val newTodoRef = database.push()
//                val id = newTodoRef.key!!.toIntOrNull() ?: 0
                val todo = Todo(newTodoRef.key!!,todoTitle2,false,userId.toString())
                newTodoRef.setValue(todo)
                editTextText.text.clear()
            }
        }

        Delete.setOnClickListener {
            val idDeleted = todoAdapter.deleteDoneTodos()
            val newTodoRefDelete = database.child(idDeleted.toString())
            newTodoRefDelete.removeValue()
        }


        database.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val item = snapshot.getValue(Todo::class.java)
                if (item != null) {
                    todoList.add(item)
                    todoAdapter.notifyItemInserted(todoList.size - 1)
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {

                val removedTodo = snapshot.getValue(Todo::class.java)
                removedTodo?.let {
                    val position = todoAdapter.todos.indexOfFirst { it.id == removedTodo.id }
                    Log.d(ContentValues.TAG, position.toString())
                    if (position != -1) {
                        Log.d(ContentValues.TAG, "Child removed: ${it.title}")
                        todoAdapter.deleteTodo(position)
                    }
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(ContentValues.TAG, "Database operation cancelled: ${error.message}")

            }
        }
        )
    }
}