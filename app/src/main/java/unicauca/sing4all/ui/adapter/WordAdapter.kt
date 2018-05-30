package unicauca.sing4all.ui.adapter

import android.content.Context
import android.speech.tts.TextToSpeech
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import io.reactivex.subjects.PublishSubject
import unicauca.sing4all.data.couch.CouchRx
import unicauca.sing4all.data.models.Word
import unicauca.sing4all.databinding.TemplateWordBinding
import unicauca.sing4all.di.ActivityScope
import javax.inject.Inject
import unicauca.sing4all.R
import unicauca.sing4all.util.inflate

@ActivityScope
class WordAdapter @Inject constructor(private val db: CouchRx) : RecyclerView.Adapter<WordViewHolder>() {

    var data: List<Word> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    val onClick:PublishSubject<Word> = PublishSubject.create()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder =
            WordViewHolder(parent.inflate(R.layout.template_word))

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        holder.bind(data[position],onClick, db)
    }


}

class WordViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding: TemplateWordBinding = TemplateWordBinding.bind(view)

    fun bind(word: Word, onClick:PublishSubject<Word>, db: CouchRx) {
        binding.word = word
        binding.onClick = onClick

        val sign = db.getBlob(word._id, word.signImage)
        if (sign.second != null) Glide.with(itemView.context)
                .load(sign.second)
                .into(binding.imgSign)

        val ex = db.getBlogFromDictionary(sign.first, word.meanImage)
        if (ex != null) Glide.with(itemView)
                .load(ex)
                .into(binding.imgEx)
    }

}

