package unicauca.sing4all.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import io.reactivex.subjects.PublishSubject
import unicauca.sing4all.R
import unicauca.sing4all.data.couch.CouchRx
import unicauca.sing4all.data.models.Word
import unicauca.sing4all.databinding.TemplateWord2Binding
import unicauca.sing4all.databinding.TemplateWordBinding
import unicauca.sing4all.di.ActivityScope
import unicauca.sing4all.util.inflate
import javax.inject.Inject

@ActivityScope
class WordListenAdapter @Inject constructor(private val db: CouchRx) : RecyclerView.Adapter<WordListenViewHolder>() {

    var data: List<Word> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    val onClick:PublishSubject<Word> = PublishSubject.create()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordListenViewHolder =
            WordListenViewHolder(parent.inflate(R.layout.template_word_2))

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: WordListenViewHolder, position: Int) {
        holder.bind(data[position],onClick, db)
    }


}

class WordListenViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding: TemplateWord2Binding = TemplateWord2Binding.bind(view)

    fun bind(word: Word, onClick:PublishSubject<Word>, db: CouchRx) {
        binding.word = word
        binding.onClick = onClick

        val sign = db.getBlob(word._id, word.signImage)
        if (sign.second != null) Glide.with(itemView.context)
                .load(sign.second)
                .into(binding.sign)

        val ex = db.getBlogFromDictionary(sign.first, word.meanImage)
        if (ex != null) Glide.with(itemView)
                .load(ex)
                .into(binding.ex)
    }

}

