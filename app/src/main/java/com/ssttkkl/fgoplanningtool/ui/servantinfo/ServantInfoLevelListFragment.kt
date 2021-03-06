package com.ssttkkl.fgoplanningtool.ui.servantinfo

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ssttkkl.fgoplanningtool.R
import com.ssttkkl.fgoplanningtool.resources.ResourcesProvider
import com.ssttkkl.fgoplanningtool.resources.itemdescriptor.ItemType
import com.ssttkkl.fgoplanningtool.ui.iteminfo.ItemInfoDialogFragment
import com.ssttkkl.fgoplanningtool.ui.utils.CommonRecViewItemDecoration
import kotlinx.android.synthetic.main.item_servantinfo_levellist.*

class ServantInfoLevelListFragment : Fragment() {
    var data: List<ServantInfoLevelListEntity> = listOf()
        set(value) {
            field = value
            (recView?.adapter as? ServantInfoLevelListRecViewAdapter)?.data = value
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_servantinfo_levellist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recView.apply {
            adapter = ServantInfoLevelListRecViewAdapter(context).apply {
                data = this@ServantInfoLevelListFragment.data
                setOnItemClickListener { gotoItemInfoUi(it) }
                if (savedInstanceState != null)
                    expandedPosition = savedInstanceState.getInt(KEY_EXPANDED, -1)
            }
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(CommonRecViewItemDecoration(context))
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_EXPANDED, (recView?.adapter as? ServantInfoLevelListRecViewAdapter)?.expandedPosition
                ?: -1)
    }

    private fun gotoItemInfoUi(codename: String) {
        if (ResourcesProvider.instance.itemDescriptors[codename]?.type != ItemType.General)
            ItemInfoDialogFragment.newInstance(codename)
                    .show(childFragmentManager, ItemInfoDialogFragment::class.qualifiedName)
    }

    companion object {
        private const val KEY_EXPANDED = "expanded"
    }
}