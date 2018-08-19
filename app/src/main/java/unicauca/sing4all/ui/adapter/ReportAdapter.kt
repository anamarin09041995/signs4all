package unicauca.sing4all.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import unicauca.sing4all.R
import unicauca.sing4all.data.models.ReportChar
import unicauca.sing4all.databinding.TemplateReportBinding

class ReportAdapter:RecyclerView.Adapter<ReportHolder>(){

    var data:List<ReportChar> = emptyList()
     set(value){
         field = value
         notifyDataSetChanged()
     }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ReportHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.template_report, p0, false)
        return ReportHolder(view)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(p0: ReportHolder, p1: Int) {
        p0.bind(data[p1])
    }

}

class ReportHolder(view:View):RecyclerView.ViewHolder(view){

    private val binding:TemplateReportBinding = TemplateReportBinding.bind(view)

    fun bind(report:ReportChar){
        binding.report = report
    }
}