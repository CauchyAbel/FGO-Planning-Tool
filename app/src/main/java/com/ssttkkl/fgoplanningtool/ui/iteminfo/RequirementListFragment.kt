package com.ssttkkl.fgoplanningtool.ui.iteminfo

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ssttkkl.fgoplanningtool.R
import com.ssttkkl.fgoplanningtool.ui.requirementlist.RequirementListEntity
import com.ssttkkl.fgoplanningtool.ui.requirementlist.RequirementListRecViewAdapter
import com.ssttkkl.fgoplanningtool.ui.servantinfo.ServantInfoDialogFragment
import com.ssttkkl.fgoplanningtool.ui.utils.CommonRecViewItemDecoration
import kotlinx.android.synthetic.main.fragment_iteminfo_requirementlist.*

class RequirementListFragment : Fragment() {
    var data: List<RequirementListEntity> = listOf()
        set(value) {
            field = value
            (recView?.adapter as? RequirementListRecViewAdapter)?.data = value
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_iteminfo_requirementlist, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recView.apply {
            adapter = RequirementListRecViewAdapter(context).apply {
                data = this@RequirementListFragment.data
                setOnItemClickListener { _, item -> gotoServantDetailUi(item.servantID) }
            }
            layoutManager = LinearLayoutManager(context!!, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(CommonRecViewItemDecoration(context!!))
            isNestedScrollingEnabled = false
        }
    }

    private fun gotoServantDetailUi(servantID: Int) {
        ServantInfoDialogFragment.newInstance(servantID)
                .show(childFragmentManager, ServantInfoDialogFragment::class.qualifiedName)
    }
}