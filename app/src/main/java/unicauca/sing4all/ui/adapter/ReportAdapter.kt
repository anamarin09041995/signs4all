package unicauca.sing4all.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import unicauca.sing4all.R
import unicauca.sing4all.data.models.ReportChar
import unicauca.sing4all.databinding.TemplateReportBinding
import unicauca.sing4all.util.inflate
import java.math.RoundingMode
import java.text.DecimalFormat

class ReportAdapter:RecyclerView.Adapter<ReportHolder>(){

    var data:List<ReportChar> = emptyList()
     set(value){
         field = value
         notifyDataSetChanged()
     }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ReportHolder  =
            ReportHolder(p0.inflate(R.layout.template_report))

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(p0: ReportHolder, p1: Int) {
        p0.bind(data[p1])
    }

}

class ReportHolder(view:View):RecyclerView.ViewHolder(view){

    private val binding:TemplateReportBinding = TemplateReportBinding.bind(view)
    val format:DecimalFormat = DecimalFormat("#.###")
    init{
        format.roundingMode = RoundingMode.CEILING
    }

    fun bind(report:ReportChar){
        binding.report = report
        binding.successInfo = format.format(report.successProbability)
        binding.timeInfo = format.format(report.time)
        binding.timeAInfo = format.format(report.timeAverage)

    }
}