package models

trait FormModels 

case class ResetForm(cause: String) extends FormModels
case class ConfigForm(emailSend: Boolean, displayLogs: Boolean) extends FormModels