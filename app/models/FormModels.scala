package models


trait FormModels 

case class ResetForm(cause: String) extends FormModels
case class ConfigForm(emailSend: Boolean, displayLogs: Boolean,levelLogs:Int,algo:String,
    pondclient:Int,pondwaiter:Int,
    minLevel:Int,maxLevel:Int,cabinCount:Int,bestCapacity:Int,maxCapacity:Int) extends FormModels