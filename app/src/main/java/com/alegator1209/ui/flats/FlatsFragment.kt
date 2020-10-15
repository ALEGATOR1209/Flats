package com.alegator1209.ui.flats

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.alegator1209.R
import com.alegator1209.datasource.remote.RemoteDataSource
import com.alegator1209.interactors.FlatRoomsUseCases
import com.alegator1209.interactors.FlatsUseCases
import com.alegator1209.ui.flats.adapter.FlatsAdapter
import com.alegator1209.ui.flats.dialog.AddFlatDialog
import kotlinx.android.synthetic.main.flats_fragment.*

class FlatsFragment : Fragment() {
    companion object {
        fun newInstance(
            dataSource: RemoteDataSource,
            onFlatChosen: (FlatRoomsUseCases) -> Unit
        ): FlatsFragment {
            val fragment = FlatsFragment()
            fragment.dataSource = dataSource
            fragment.onFlatChosen = onFlatChosen
            return fragment
        }
    }

    private lateinit var viewModel: FlatsViewModel
    private lateinit var dataSource: RemoteDataSource
    private lateinit var adapter: FlatsAdapter
    private lateinit var onFlatChosen: (FlatRoomsUseCases) -> Unit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val inflater = TransitionInflater.from(requireContext())
        enterTransition = inflater.inflateTransition(R.transition.slide_from_bottom)
        exitTransition = inflater.inflateTransition(R.transition.slide_to_top)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.flats_fragment, container, false)
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
                    val dialog = AddFlatDialog()
                    dialog.setOnCreateFlat(this::createNewFlat)
                    dialog.show(requireActivity().supportFragmentManager, "newFlat")
                    true
                }
                else -> false
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(this).get(FlatsViewModel::class.java)
        viewModel.flatsUseCases = FlatsUseCases(dataSource)

        viewModel.setOnRefreshFailed {
            shortToast(it.message ?: "")
        }

        viewModel.setOnRefreshFailed {
            shortToast(it.message ?: "")
        }

        viewModel.refreshing.observe(viewLifecycleOwner, Observer { setRefreshing(it) })
        viewModel.flats.observe(viewLifecycleOwner, Observer { adapter.changeList(it) })

        adapter = FlatsAdapter(viewModel.flats.value ?: listOf())
        adapter.setOnFlatClickListener {
            val inflater = TransitionInflater.from(requireContext())
            enterTransition = inflater.inflateTransition(R.transition.slide_from_top)
            onFlatChosen(viewModel.getRoomsUseCases(it))
        }
        with(recyclerFlats) {
            val linearLayoutManager = LinearLayoutManager(this@FlatsFragment.context)
            layoutManager = linearLayoutManager
            adapter = this@FlatsFragment.adapter

            val divider = DividerItemDecoration(context, linearLayoutManager.orientation)
            addItemDecoration(divider)
        }

        viewModel.refresh()
    }

    private fun createNewFlat(name: String, address: String) {
        if (name.isBlank() || address.isBlank()) {
            shortToast(getString(R.string.flat_data_empty))
            return
        }
        viewModel.addFlat(name, address)
    }

    private fun setRefreshing(b: Boolean) {
        loading.visibility = if (b) View.VISIBLE else View.GONE
    }

    private fun shortToast(msg: String) = Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
}