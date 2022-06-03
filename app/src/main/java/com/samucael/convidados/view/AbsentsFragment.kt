package com.samucael.convidados.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.samucael.convidados.R
import com.samucael.convidados.databinding.FragmentAbsentBinding
import com.samucael.convidados.service.constants.GuestConstants
import com.samucael.convidados.view.adapter.GuestAdapter
import com.samucael.convidados.view.listener.GuestListener
import com.samucael.convidados.viewmodel.AllGuestsViewModel

class AbsentsFragment : Fragment() {

    private var _binding: FragmentAbsentBinding? = null
    private val mAdapter: GuestAdapter = GuestAdapter()
    private lateinit var mViewModel: AllGuestsViewModel
    private lateinit var mListener: GuestListener

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, s: Bundle?): View {
        mViewModel = ViewModelProvider(this).get(AllGuestsViewModel::class.java)

        _binding = FragmentAbsentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // RecylerView
        // 1 - Obter a recycler
        val recycler = root.findViewById<RecyclerView>(R.id.recycler_absents)

        // 2- Definir um layout
        recycler.layoutManager = LinearLayoutManager(context)

        // 3 - Definir um adapter
        recycler.adapter = mAdapter

        mListener = object : GuestListener {
            override fun onClick(id: Int) {
                val intent = Intent(context, GuestFormActivity::class.java)

                val bundle = Bundle()
                bundle.putInt(GuestConstants.GUESTID, id)

                intent.putExtras(bundle)
                startActivity(intent)
            }

            override fun onDelete(id: Int) {
                mViewModel.delete(id)
                mViewModel.load(GuestConstants.FILTER.ABSENT)
            }
        }

        mAdapter.attachListener(mListener)
        observe()

        return root
    }

    override fun onResume() {
        super.onResume()
        mViewModel.load(GuestConstants.FILTER.ABSENT)
    }

    private fun observe(){
        mViewModel.guestList.observe(viewLifecycleOwner, Observer {
            mAdapter.updateGuests(it)
        })
    }
}