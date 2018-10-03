package unicauca.sing4all.data.models

import java.util.*

class ReportTest(val init: Date,
                 val end:Date,
                 val duration:Long,
                 val success:Boolean,
                 val charExpected:String,
                 val result:List<String> )

class ReportTestNN(val init: Date,
                   val end:Date,
                   val duration:Long,
                   val success:Boolean,
                   val charExpected:String,
                   val result:String)

class ReportChar(val letter:String,
                 var time:Long = 0,
                 var success:Int = 0,
                 var fail:Int = 0,
                 var count:Int = 0,
                 var timeAverage:Double = 0.0,
                 var successProbability:Float = 0f)

class ReportGlobal(var time:Long = 0,
                   var success:Int = 0,
                   var fail:Int = 0,
                   var count:Int = 0,
                   var timeAverage:Double = 0.0,
                   var successProbability:Float = 0f)