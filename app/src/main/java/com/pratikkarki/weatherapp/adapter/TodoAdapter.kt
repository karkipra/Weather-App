package com.pratikkarki.weatherapp.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pratikkarki.weatherapp.MainActivity
import com.pratikkarki.weatherapp.R
import com.pratikkarki.weatherapp.WeatherDetailsActivity
import com.pratikkarki.weatherapp.data.AppDatabase
import com.pratikkarki.weatherapp.data.Todo
import com.pratikkarki.weatherapp.touch.TodoTouchHelperAdapter
import kotlinx.android.synthetic.main.todo_row.view.*
import java.util.*

class TodoAdapter : RecyclerView.Adapter<TodoAdapter.ViewHolder>, TodoTouchHelperAdapter {

    companion object {
        val CITY_NAME = "CITY_NAME"
    }

    var todoItems = mutableListOf<Todo>()


    val context : Context

    constructor(context: Context, items: List<Todo>) : super() {
        this.context = context
        this.todoItems.addAll(items)
    }

    constructor(context: Context) : super() {
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(
                R.layout.todo_row, parent, false
        )
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return todoItems.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val todo = todoItems[position]

        holder.tvItemName.text = todo.itemName

        holder.itemView.setOnClickListener{

            val intent = Intent(context, WeatherDetailsActivity::class.java)
            intent.putExtra(CITY_NAME, todoItems[holder.adapterPosition].itemName)
            context.startActivity(intent)
        }


        holder.btnDelete.setOnClickListener {
            deleteTodo(holder.adapterPosition)
        }

    }



    private fun deleteTodo(adapterPosition: Int) {

        Thread {
            AppDatabase.getInstance(context).todoDao().deleteTodo(
                    todoItems[adapterPosition]
            )

            todoItems.removeAt(adapterPosition)

            (context as MainActivity).runOnUiThread{
                notifyItemRemoved(adapterPosition)
            }
        }.start()
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        val tvItemName = itemView.tvItemName
        val btnDelete = itemView.btnDelete
    }


    fun addTodo(todo: Todo) {
        todoItems.add(0, todo)
        //notifyDataSetChanged()
        notifyItemInserted(0)
    }

    override fun onDismissed(position: Int) {
        deleteTodo(position)
    }

    override fun onItemMoved(fromPosition: Int, toPosition: Int) {
        Collections.swap(todoItems, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }

    fun updateTodo(item: Todo, idx: Int) {
        todoItems[idx] = item
        notifyItemChanged(idx)
    }

    fun removeAll() {
        todoItems.clear()
        notifyDataSetChanged()
    }

}