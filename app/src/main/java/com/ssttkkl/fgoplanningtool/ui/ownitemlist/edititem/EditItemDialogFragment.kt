package com.ssttkkl.fgoplanningtool.ui.ownitemlist.edititem

import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.ssttkkl.fgoplanningtool.R
import com.ssttkkl.fgoplanningtool.data.item.Item
import com.ssttkkl.fgoplanningtool.ui.utils.NoInterfaceImplException
import kotlinx.android.synthetic.main.fragment_edititem.*

class EditItemDialogFragment : DialogFragment() {
    private lateinit var item: Item

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        item = arguments!!.getParcelable(ARG_ITEM)
        setStyle(STYLE_NO_TITLE, R.style.AppTheme_Dialog_NoTitle)
    }

    interface OnChangeItemActionListener {
        fun onChangeItemAction(changedItem: Item)
    }

    private var listener: OnChangeItemActionListener? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        listener = when {
            parentFragment is OnChangeItemActionListener -> parentFragment as OnChangeItemActionListener
            activity is OnChangeItemActionListener -> activity as OnChangeItemActionListener
            else -> throw NoInterfaceImplException(OnChangeItemActionListener::class)
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragment_edititem, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.apply {
            Glide.with(this@EditItemDialogFragment).load(item.descriptor?.imgFile).error(R.drawable.item_placeholder).into(avatar_imageView)

            count_editText.apply {
                hint = item.count.toString()
            }

            dec_button.setOnClickListener {
                performOnValue {
                    if (it - 1 >= MIN_VALUE)
                        count_editText.setText((it - 1).toString())
                }
            }
            inc_button.setOnClickListener {
                performOnValue {
                    if (it + 1 <= MAX_VALUE)
                        count_editText.setText((it + 1).toString())
                }
            }
            save_button.setOnClickListener {
                performOnValue {
                    listener?.onChangeItemAction(Item(item.codename, it))
                    dismiss()
                }
            }
            cancel_button.setOnClickListener { dialog.cancel() }
        }
    }

    private fun performOnValue(action: (data: Long) -> Unit) {
        try {
            val value = if (count_editText.text.isNotEmpty()) count_editText.text.toString().toLong() else item.count
            if (value > MAX_VALUE || value < MIN_VALUE)
                throw Exception(getString(R.string.exc_outOfBounds_edititem, MIN_VALUE, MAX_VALUE))
            action.invoke(value)
        } catch (exc: Exception) {
            Toast.makeText(context!!, exc.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        fun newInstance(item: Item) =
                EditItemDialogFragment().apply {
                    arguments = Bundle().apply {
                        putParcelable(ARG_ITEM, item)
                    }
                }

        const val ARG_ITEM = "item"

        private const val MIN_VALUE = 0
        private const val MAX_VALUE = 999_999_999
    }
}