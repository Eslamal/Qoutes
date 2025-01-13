package com.example.qoutes.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.qoutes.R
import com.example.qoutes.databinding.QuoteItemBinding
import com.example.qoutes.models.Quote
import com.example.qoutes.util.ShareUtils


class SavedQuotesAdapter : RecyclerView.Adapter<SavedQuotesAdapter.QuoteViewHolder>() {

    inner class QuoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    // differ callback that checks if elements are same of not
    private val differCallback = object : DiffUtil.ItemCallback<Quote>() {
        override fun areItemsTheSame(oldItem: Quote, newItem: Quote): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Quote, newItem: Quote): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)
    private lateinit var binding: QuoteItemBinding

    // inflate layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuoteViewHolder {
        binding = QuoteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return QuoteViewHolder(binding.root)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    // Custom item onLongClick listener
    private var onItemLongClickListener: ((Quote) -> Boolean)? = null

    override fun onBindViewHolder(holder: QuoteViewHolder, position: Int) {
        val quote = differ.currentList[position]

        with(binding) {
            holder.itemView.apply {


                rvQuoteTv.text = resources.getString(R.string.quote, quote.quote)
                rvAuthorTv.text = resources.getString(R.string.author, quote.author)

                rvQuoteTv.visibility = View.VISIBLE
                rvAuthorTv.visibility = View.VISIBLE

                rvQuoteLoading.visibility = View.GONE

                rvQuoteShare.setOnClickListener {

                    rvQuoteShare.visibility = View.GONE

                    ShareUtils.share(rvItemHolder, context)


                    rvQuoteShare.visibility = View.VISIBLE
                }


                onItemLongClickListener?.let {
                    setOnLongClickListener {
                        it(quote)
                    }
                }
            }
        }
    }


    fun setOnItemLongClickListener(listener: (Quote) -> Boolean) {
        onItemLongClickListener = listener
    }
}
