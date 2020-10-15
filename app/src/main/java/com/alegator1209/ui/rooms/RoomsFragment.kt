package com.alegator1209.ui.rooms

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.transition.addListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.alegator1209.R
import com.alegator1209.interactors.FlatRoomsUseCases
import com.alegator1209.model.Room
import com.alegator1209.ui.rooms.adapter.RoomAdapter
import com.alegator1209.ui.rooms.dialog.RoomDialog
import kotlinx.android.synthetic.main.rooms_fragment.*


class RoomsFragment : Fragment() {
    companion object {
        fun newInstance(useCases: FlatRoomsUseCases): RoomsFragment {
            val fragment = RoomsFragment()
            fragment.useCases = useCases
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val inflater = TransitionInflater.from(requireContext())
        enterTransition = inflater.inflateTransition(R.transition.slide_from_bottom).also {
            it.addListener(onEnd = {
                viewModel.rooms.observe(viewLifecycleOwner, Observer { rooms ->
                    adapter.update(rooms)
                    recyclerRooms.scheduleLayoutAnimation()
                })
            })
        }
        exitTransition = inflater.inflateTransition(R.transition.slide_to_bottom)
    }

    private lateinit var viewModel: RoomsViewModel
    private lateinit var adapter: RoomAdapter
    private var useCases: FlatRoomsUseCases? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.rooms_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.inflateMenu(R.menu.toolbar_menu)
        toolbar.setOnMenuItemClickListener {
            when (it.itemId)  {
                R.id.refresh -> {
                    viewModel.refresh()
                    true
                }
                R.id.addNew -> {
                    val dialog = RoomDialog()
                    dialog.setOnDone(this::onCreateRoom)
                    dialog.show(requireActivity().supportFragmentManager, "newRoom")
                    true
                }
                else -> false
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(this).get(RoomsViewModel::class.java)
        viewModel.setUseCases(useCases)

        toolbar.title = viewModel.flat.name
        viewModel.refreshing.observe(viewLifecycleOwner, Observer { setRefreshing(it) })

        adapter = RoomAdapter(viewModel.rooms.value ?: listOf())
        adapter.setOnEditRoomListener(this::onEditRoom)
        adapter.setOnDeleteRoomListener(this::onDeleteRoom)
        recyclerRooms.adapter = adapter
        recyclerRooms.layoutManager = LinearLayoutManager(context)
        recyclerRooms.layoutAnimation = AnimationUtils.loadLayoutAnimation(
            context,
            R.anim.layout_animation_fall_down
        )

        viewModel.refresh()
    }

    private fun setRefreshing(b: Boolean) {
        loading.visibility = if (b) View.VISIBLE else View.GONE
        recyclerRooms.visibility = if (b) View.GONE else View.VISIBLE
    }

    private fun onCreateRoom(name: String) {
        if (name.isBlank()) {
            shortToast(getString(R.string.room_data_empty))
            return
        }
        viewModel.addRoom(name)
        viewModel.setOnRoomAdded {
            adapter.update(viewModel.rooms.value ?: listOf())
        }
    }

    private fun onEditRoom(room: Room) {
        val dialog = RoomDialog()
        dialog.setOnDone {
            if (it.isBlank()) {
                shortToast(getString(R.string.room_data_empty))
                return@setOnDone
            }

            viewModel.editRoom(room.id, it)
            viewModel.setOnRoomUpdated { r ->
                room.name = r.name
                adapter.updateRoom(room)
            }
            viewModel.setOnRoomUpdateFailed {
                shortToast(getString(R.string.failure))
            }
        }
        dialog.show(requireActivity().supportFragmentManager, "newRoom")
    }

    private fun onDeleteRoom(room: Room) {
        viewModel.deleteRoom(room)
        viewModel.setOnRoomDeleted { adapter.deleteRoom(room) }
        viewModel.setOnRoomDeleteFailed { shortToast(getString(R.string.failure)) }
    }

    private fun shortToast(msg: String) = Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
}