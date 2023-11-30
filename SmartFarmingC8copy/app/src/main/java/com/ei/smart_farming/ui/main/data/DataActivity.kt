package com.ei.smart_farming.ui.main.data

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ei.smart_farming.R
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DataActivity : AppCompatActivity() {
    private val database = FirebaseDatabase.getInstance().reference.child("")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data)

        supportActionBar?.title = "Detail Data"
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        supportActionBar?.setDisplayShowHomeEnabled(true)

        val mRecyclerView = findViewById<RecyclerView>(R.id.recycler_view)

        mRecyclerView.layoutManager = LinearLayoutManager(this@DataActivity, LinearLayoutManager.VERTICAL, true)
        (mRecyclerView.layoutManager as LinearLayoutManager).stackFromEnd = true
        mRecyclerView.setHasFixedSize(true)

        val ref = FirebaseDatabase.getInstance().reference.child("data")

        val option = FirebaseRecyclerOptions.Builder<Model>()
            .setQuery(ref, Model::class.java)
            .build()

        val firebaseRVA = object: FirebaseRecyclerAdapter<Model, ItemViewHolder>(option) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

                val itemView = LayoutInflater.from(this@DataActivity).inflate(R.layout.data_list,parent,false)
                return ItemViewHolder(itemView)
            }

            override fun onBindViewHolder(item: ItemViewHolder, position: Int, model: Model) {
                //get Firebase position
                val itemId = getRef(position).key.toString().reversed()

                ref.child(itemId).addValueEventListener(object: ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        item.date.text = model.date.toString()
                        item.time.text = model.time.toString()
                        item.temperature.text = model.temperature.toString()
                        item.humidity.text = model.humidity.toString()
                        item.plot1.text = model.plot1.toString()
                        item.plot2.text = model.plot2.toString()
                    }
                })
            }
        }

        mRecyclerView.adapter = firebaseRVA
        firebaseRVA.startListening()
    }

    inner class ItemViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        internal var date = itemView!!.findViewById<TextView>(R.id.tv_date)
        internal var time = itemView!!.findViewById<TextView>(R.id.tv_time)
        internal var temperature = itemView!!.findViewById<TextView>(R.id.tv_temperature)
        internal var humidity = itemView!!.findViewById<TextView>(R.id.tv_humidity)
        internal var plot1 = itemView!!.findViewById<TextView>(R.id.tv_plot1)
        internal var plot2 = itemView!!.findViewById<TextView>(R.id.tv_plot2)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_delete, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.btn_menu_delete_log_data){
            dialogDeleteLogData()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun dialogDeleteLogData() {
        val dialog = Dialog(this)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE) // before
        dialog.setContentView(R.layout.dialog_delete_data)
        dialog.setCancelable(true)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT

        val btnConfirmDeleteTransaction = dialog.findViewById<Button>(R.id.btn_confirm_delete_data)
        val btnCancelDeleteTransaction = dialog.findViewById<Button>(R.id.btn_cancel_delete_data)

        btnCancelDeleteTransaction.setOnClickListener {
            dialog.hide()
            // do this
        }
        btnConfirmDeleteTransaction.setOnClickListener {
            val rstCounter = database.child("control").child("state")
            val dataLog = database.child("data")

            dialog.hide()
            dataLog.removeValue()
            rstCounter.setValue("rst-post-counter")

            recreate()
            Toast.makeText(this, "Data berhasil dihapus", Toast.LENGTH_SHORT).show()

        }

        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
        dialog.window!!.attributes = lp
    }


}