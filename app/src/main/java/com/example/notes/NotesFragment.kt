package com.example.notes

import android.app.AlertDialog
import com.example.notes.CardsSource
import androidx.recyclerview.widget.RecyclerView
import com.example.notes.ItemAdapter
import android.widget.CheckBox
import android.widget.TextView
import android.os.Bundle
import com.example.notes.R.layout
import com.example.notes.CardsSourceFirebaseImpl
import com.example.notes.CardsSourceResponse
import com.example.notes.R
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notes.NotesFragment
import com.example.notes.NoteStructure
import android.widget.Toast
import android.view.ContextMenu.ContextMenuInfo
import android.content.DialogInterface
import com.example.notes.TextFragment
import android.content.Intent
import android.content.res.Configuration
import android.view.*
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.notes.TextActivity2
import java.util.*

class NotesFragment : Fragment() {
    private var isLands = false
    private var currentPosition: CardsSource? = null
    private lateinit var data: CardsSource
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ItemAdapter
    private var checkBox: CheckBox? = null
    private var textView: TextView? = null
    private lateinit var ADD: Button

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(layout.fragment_notes, container, false)
        data = CardsSourceFirebaseImpl()
        (data as CardsSourceFirebaseImpl).init(CardsSourceResponse { adapter.notifyDataSetChanged() })
        initView(view)
        return view
    }

    private fun initView(view: View) {
        recyclerView = view.findViewById(R.id.recycler_view_lines)
        checkBox = view.findViewById(R.id.check)
        textView = view.findViewById(R.id.title)
        ADD = view.findViewById(R.id.addNotes)
        initButton()
        init()
    }

    fun init() {
        recyclerView.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
        adapter = ItemAdapter(data, this)
        recyclerView.adapter = adapter
        adapter.setListener {
            currentPosition = CardsSourceFirebaseImpl()
            showText(currentPosition as CardsSourceFirebaseImpl)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable(CURRENT_NOTE, currentPosition)
        super.onSaveInstanceState(outState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        isLands = (resources.configuration.orientation
                == Configuration.ORIENTATION_LANDSCAPE)
        currentPosition = if (savedInstanceState != null) {
            savedInstanceState.getParcelable(CURRENT_NOTE)
        } else {
            CardsSourceFirebaseImpl().init { adapter.notifyDataSetChanged() }
        }
        if (isLands) {
            showLandText(currentPosition)
        }
    }

    private fun initButton() {
        ADD.setOnClickListener { v: View? ->
            val noteStructure = NoteStructure("Title", "dd.mm.ee", "description", false)
            noteStructure.id = UUID.randomUUID().toString()
            data.addCardData(noteStructure)
            Toast.makeText(context, "ADD Notes", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_fragment, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val fm = item.itemId
        when (fm) {
            R.id.share -> {
                Toast.makeText(context, "Скоро будет можно делиться", Toast.LENGTH_LONG).show()
                Toast.makeText(context, "Скоро будет фото", Toast.LENGTH_LONG).show()
                val noteStructure = NoteStructure("Title", "dd.mm.ee", "description", false)
                noteStructure.id = UUID.randomUUID().toString()
                data.addCardData(noteStructure)
                adapter.notifyItemInserted(data.size() - 1)
                Toast.makeText(context, "ADD Notes", Toast.LENGTH_LONG).show()
                return true
            }
            R.id.photo -> {
                Toast.makeText(context, "Скоро будет фото", Toast.LENGTH_LONG).show()
                val noteStructure = NoteStructure("Title", "dd.mm.ee", "description", false)
                noteStructure.id = UUID.randomUUID().toString()
                data.addCardData(noteStructure)
                adapter.notifyItemInserted(data.size() - 1)
                Toast.makeText(context, "ADD Notes", Toast.LENGTH_LONG).show()
                return true
            }
            R.id.addNotes -> {
                val noteStructure = NoteStructure("Title", "dd.mm.ee", "description", false)
                noteStructure.id = UUID.randomUUID().toString()
                data.addCardData(noteStructure)
                adapter.notifyItemInserted(data.size() - 1)
                Toast.makeText(context, "ADD Notes", Toast.LENGTH_LONG).show()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater = requireActivity().menuInflater
        inflater.inflate(R.menu.contex_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val buttonPosition = adapter.menuPosition
        when (item.itemId) {
            R.id.action_clear -> {
                val builder = AlertDialog.Builder(activity)
                builder.setTitle("Точно удалить?")
                        .setIcon(R.drawable.ic_dialog_close_light)
                        .setCancelable(true)
                        .setPositiveButton("Ok") { dialog: DialogInterface?, which: Int ->
                            data.deletePosition(buttonPosition)
                            adapter.notifyItemRemoved(buttonPosition)
                            Toast.makeText(context, "Земтка удалена", Toast.LENGTH_LONG).show()
                        }
                        .setNegativeButton("Cancel") { _: DialogInterface?, _: Int -> Toast.makeText(context, "Отмена", Toast.LENGTH_LONG).show() }
                        .show()
                return true
            }
            else -> {
            }
        }
        return super.onContextItemSelected(item)
    }

    private fun showText(currentPosition: CardsSource) {
        if (isLands) {
            showLandText(currentPosition)
        } else {
            showPortText(currentPosition)
        }
    }

    private fun showLandText(currentPosition: CardsSource?) {
        val fragment = TextFragment.newInstance(currentPosition)
        requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.text_land, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit()
    }

    private fun showPortText(currentPosition: CardsSource) {
        val intent = Intent()
        intent.setClass(activity!!, TextActivity2::class.java)
        intent.putExtra(TextFragment.ARG_NOTE, currentPosition)
        startActivity(intent)
    }

    companion object {
        const val CURRENT_NOTE = "currentNote"
        fun newInstance(): NotesFragment {
            return NotesFragment()
        }
    }
}