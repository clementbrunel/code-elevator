package models


trait FormModels 

case class ResetForm(cause: String) extends FormModels
case class ConfigForm(
    emailSend: Boolean,   //send email on errors
    displayLogs: Boolean,  //logs are display, deasactivate for perfs
    levelLogs:Int,		//log level to display
    algo:String,		//choice of algo used
    pondclient:Int,		//ponderation of client in direction calculation
    pondwaiter:Int,		//pondration of waiter in direction calculation
    minLevel:Int,		//levelMin
    maxLevel:Int,		//levelMax
    cabinCount:Int,		//number of cabines
    bestCapacity:Int,	//limit where waiters are take in consideration
    autoReset:Boolean,	//reset when big charge
    maxCapacity:Int		//maxCapacity of cabines
    ) extends FormModels
    
case class ConcurentForm(concurent:String) extends FormModels